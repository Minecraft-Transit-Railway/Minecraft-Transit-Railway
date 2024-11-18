package org.mtr.mod.servlet;

import org.apache.commons.io.IOUtils;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.resource.CustomResourcesConverter;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.libraries.javax.servlet.http.Part;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public final class ResourcePackCreatorUploadServlet extends AbstractResourcePackCreatorServlet {

	private static final Object2ObjectAVLTreeMap<String, String> MODELS = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<String, byte[]> TEXTURES = new Object2ObjectAVLTreeMap<>();

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);
		setEncoding(httpServletRequest, httpServletResponse);

		switch (httpServletRequest.getPathInfo()) {
			case "/reset":
				reset();
				MinecraftClient.getInstance().execute(CustomResourceLoader::reload);
				returnStandardResponse(httpServletResponse, asyncContext, true);
				break;
			case "/export":
				export(httpServletRequest, httpServletResponse, asyncContext);
				break;
			default:
				returnErrorResponse(httpServletResponse, asyncContext);
				break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);
		setEncoding(httpServletRequest, httpServletResponse);

		switch (httpServletRequest.getPathInfo()) {
			case "/zip":
				reset();
				uploadZip(httpServletRequest, httpServletResponse, asyncContext);
				break;
			case "/resources":
				uploadResource(httpServletRequest, httpServletResponse, asyncContext);
				break;
			default:
				returnErrorResponse(httpServletResponse, asyncContext);
				break;
		}
	}

	static String getModel(String name) {
		return MODELS.get(name);
	}

	private static void reset() {
		final ObjectArrayList<String> texturesToDestroy = new ObjectArrayList<>(TEXTURES.keySet());
		resourceWrapper = null;
		MODELS.clear();
		TEXTURES.clear();

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.execute(() -> texturesToDestroy.forEach(texture -> {
			try {
				minecraftClient.getTextureManager().destroyTexture(new Identifier(texture));
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		}));
	}

	private static void uploadZip(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		try {
			for (final Part part : httpServletRequest.getParts()) {
				Init.LOGGER.info("Processing {} uploaded from the Resource Pack Creator", part.getSubmittedFileName());
				JsonObject customResourcesObject = null;
				final Object2ObjectAVLTreeMap<String, String> jsonCache = new Object2ObjectAVLTreeMap<>();
				final ObjectArrayList<Runnable> resourceUploadTasks = new ObjectArrayList<>();
				String lastNameRead = "";

				try (final ZipInputStream zipInputStream = new ZipInputStream(part.getInputStream())) {
					ZipEntry zipEntry;

					while ((zipEntry = zipInputStream.getNextEntry()) != null) {
						final String name = pathToIdentifier(zipEntry.getName());
						lastNameRead = name;
						Init.LOGGER.debug("Reading {}", name);
						final byte[] bytes = IOUtils.toByteArray(zipInputStream);
						final String content = new String(bytes);

						if (name.equals(String.format("%s:%s.json", Init.MOD_ID, CustomResourceLoader.CUSTOM_RESOURCES_ID))) {
							customResourcesObject = Utilities.parseJson(content);
						} else if (name.endsWith(".png") || name.endsWith(".bbmodel") || name.endsWith(".obj") || name.endsWith(".mtl")) {
							resourceUploadTasks.add(() -> uploadResource(name, bytes, content));
						} else if (name.endsWith(".json")) {
							jsonCache.put(name, content);
						}

						zipInputStream.closeEntry();
					}
				} catch (Exception e) {
					Init.LOGGER.error("Processing {} failed; last successful file read was {}", part.getSubmittedFileName(), lastNameRead);
					Init.LOGGER.error("", e);
				}

				if (customResourcesObject == null) {
					break;
				}

				final ObjectArrayList<VehicleResourceWrapper> vehicles = new ObjectArrayList<>();
				CustomResourcesConverter.convert(customResourcesObject, identifier -> jsonCache.getOrDefault(identifier.data.toString(), ResourceManagerHelper.readResource(identifier))).iterateVehicles(vehicleResource -> vehicles.add(vehicleResource.toVehicleResourceWrapper()));
				resourceWrapper = new ResourceWrapper(vehicles, new ObjectArrayList<>(), new ObjectArrayList<>(), CustomResourceLoader.getMinecraftModelResources(), CustomResourceLoader.getTextureResources());
				resourceUploadTasks.forEach(Runnable::run);
				returnStandardResponse(httpServletResponse, asyncContext, true);
				return;
			}
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}

		returnErrorResponse(httpServletResponse, asyncContext);
	}

	private static void uploadResource(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		if (resourceWrapper != null) {
			try {
				for (final Part part : httpServletRequest.getParts()) {
					try (final InputStream inputStream = part.getInputStream()) {
						final byte[] bytes = IOUtils.toByteArray(inputStream);
						uploadResource(String.format("%s:%s", Init.MOD_ID, part.getSubmittedFileName()), bytes, new String(bytes));
					} catch (Exception e) {
						Init.LOGGER.error("", e);
					}
				}
				returnStandardResponse(httpServletResponse, asyncContext, true);
				return;
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		}

		returnErrorResponse(httpServletResponse, asyncContext);
	}

	private static void uploadResource(String name, byte[] bytes, String content) {
		if (resourceWrapper != null) {
			if (name.endsWith(".png")) {
				resourceWrapper.addTextureResource(name);
				TEXTURES.put(name, bytes);
				try {
					final Identifier identifier = new Identifier(name);
					final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(bytes.length);
					byteBuffer.put(bytes);
					byteBuffer.rewind();
					final AbstractTexture abstractTexture = new AbstractTexture(new NativeImageBackedTexture(NativeImage.read(byteBuffer)).data);
					final MinecraftClient minecraftClient = MinecraftClient.getInstance();
					minecraftClient.execute(() -> {
						minecraftClient.getTextureManager().registerTexture(identifier, abstractTexture);
						Init.LOGGER.info("Registered temporary texture [{}]", identifier.data.toString());
					});
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			} else if (name.endsWith(".bbmodel")) {
				final String[] nameSplit = name.split("[^a-z0-9_]");
				final ObjectArrayList<String> modelParts = new ObjectArrayList<>();
				final JsonObject modelObject = Utilities.parseJson(content);
				BlockbenchModelValidator.validate(modelObject, nameSplit[Math.max(0, nameSplit.length - 2)], null);
				new BlockbenchModel(new JsonReader(modelObject)).getOutlines().forEach(blockbenchOutline -> modelParts.add(blockbenchOutline.getName()));
				resourceWrapper.addModelResource(new ModelWrapper(name, modelParts));
				MODELS.put(name, modelObject.toString());
			} else if (name.endsWith(".obj")) {
				resourceWrapper.addModelResource(new ModelWrapper(name, new ObjectArrayList<>(OptimizedModel.ObjModel.loadModel(content, mtlString -> "", textureString -> new Identifier(""), null, true, false).keySet())));
				MODELS.put(name, content);
			} else if (name.endsWith(".mtl")) {
				MODELS.put(name, content);
			}
		}
	}

	private static void export(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		final String name = httpServletRequest.getParameter("name");
		final String description = httpServletRequest.getParameter("description");

		if (resourceWrapper != null && name != null && description != null) {
			final JsonObject vehiclesFlattened = resourceWrapper.flatten();
			MinecraftClient.getInstance().execute(() -> {
				final ObjectArrayList<VehicleResource> vehicles = new ObjectArrayList<>();
				final Object2ObjectArrayMap<String, ModelProperties> modelPropertiesMap = new Object2ObjectArrayMap<>();
				final Object2ObjectArrayMap<String, PositionDefinitions> positionDefinitionsMap = new Object2ObjectArrayMap<>();
				new ResourceWrapper(new JsonReader(vehiclesFlattened), new ObjectArrayList<>(), new ObjectArrayList<>()).iterateVehicles(vehicleResourceWrapper -> vehicles.add(vehicleResourceWrapper.toVehicleResource(identifier -> {
					final String modelString = ResourcePackCreatorUploadServlet.getModel(identifier.data.toString());
					return modelString == null ? ResourceManagerHelper.readResource(identifier) : modelString;
				}, modelPropertiesMap, positionDefinitionsMap)));
				final CustomResources customResources = new CustomResources(vehicles, new ObjectArrayList<>());
				final String resourcePackFolder = String.format("%s/resourcepacks", MinecraftClient.getInstance().getRunDirectoryMapped());
				final String filePath = String.format("%s/%s-%s.zip", resourcePackFolder, name, DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss").format(LocalDateTime.now()));
				Init.LOGGER.info("Exporting Resource Pack at {}", filePath);

				try (
						final FileOutputStream fileOutputStream = new FileOutputStream(filePath);
						final ZipOutputStream zipOutputStream = new ZipOutputStream(fileOutputStream)
				) {
					writeToZipOutputStream(modelPropertiesMap, zipOutputStream, modelProperties -> IOUtils.write(Utilities.getJsonObjectFromData(modelProperties).toString(), zipOutputStream, StandardCharsets.UTF_8));
					writeToZipOutputStream(positionDefinitionsMap, zipOutputStream, positionDefinitions -> IOUtils.write(Utilities.getJsonObjectFromData(positionDefinitions).toString(), zipOutputStream, StandardCharsets.UTF_8));
					writeToZipOutputStream(MODELS, zipOutputStream, modelString -> IOUtils.write(modelString, zipOutputStream, StandardCharsets.UTF_8));
					writeToZipOutputStream(TEXTURES, zipOutputStream, textureBytes -> IOUtils.write(textureBytes, zipOutputStream));

					zipOutputStream.putNextEntry(new ZipEntry(String.format("assets/%s/%s.json", Init.MOD_ID, CustomResourceLoader.CUSTOM_RESOURCES_ID)));
					IOUtils.write(Utilities.getJsonObjectFromData(customResources).toString(), zipOutputStream, StandardCharsets.UTF_8);

					zipOutputStream.putNextEntry(new ZipEntry("pack.mcmeta"));
					final JsonObject packInfoObject = new JsonObject();
					packInfoObject.addProperty("pack_format", ResourceManagerHelper.getResourcePackVersion());
					packInfoObject.addProperty("description", description);
					final JsonObject packObject = new JsonObject();
					packObject.add("pack", packInfoObject);
					IOUtils.write(Utilities.prettyPrint(packObject), zipOutputStream, StandardCharsets.UTF_8);

					ResourceManagerHelper.readResource(new Identifier(Init.MOD_ID, "textures/block/sign/logo_grayscale.png"), inputStream -> {
						try {
							zipOutputStream.putNextEntry(new ZipEntry("pack.png"));
							IOUtils.write(IOUtils.toByteArray(inputStream), zipOutputStream);
						} catch (Exception e) {
							Init.LOGGER.error("", e);
						}
					});
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}

				Util.getOperatingSystem().open(new File(resourcePackFolder));
			});

			returnStandardResponse(httpServletResponse, asyncContext, false);
		} else {
			returnErrorResponse(httpServletResponse, asyncContext);
		}
	}

	private static <T> void writeToZipOutputStream(Object2ObjectMap<String, T> dataMap, ZipOutputStream zipOutputStream, DataConsumer<T> writeData) {
		dataMap.forEach((name, data) -> {
			try {
				zipOutputStream.putNextEntry(new ZipEntry(identifierToPath(name)));
				writeData.accept(data);
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		});
	}

	private static String pathToIdentifier(String name) {
		final String[] nameSplit = name.split("/");
		if (nameSplit.length >= 3) {
			final String[] newNameSplit = new String[nameSplit.length - 2];
			System.arraycopy(nameSplit, 2, newNameSplit, 0, newNameSplit.length);
			return String.format("%s:%s", nameSplit[1], String.join("/", newNameSplit));
		} else {
			return "";
		}
	}

	private static String identifierToPath(String identifierString) {
		final String[] stringSplit = identifierString.split(":");
		if (stringSplit.length == 2) {
			return String.format("assets/%s/%s", stringSplit[0], stringSplit[1]);
		} else {
			Init.LOGGER.error("Invalid identifier {}", identifierString);
			return Init.randomString();
		}
	}

	@FunctionalInterface
	private interface DataConsumer<T> {
		void accept(T data) throws Exception;
	}
}
