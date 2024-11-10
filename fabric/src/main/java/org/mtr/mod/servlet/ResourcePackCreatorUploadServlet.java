package org.mtr.mod.servlet;

import org.apache.commons.io.IOUtils;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.resource.CustomResourcesConverter;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
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

import javax.annotation.Nullable;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ResourcePackCreatorUploadServlet extends AbstractResourcePackCreatorServlet {

	private static final Object2ObjectAVLTreeMap<String, String> MODELS = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<String, byte[]> TEXTURES = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<String, Identifier> TEXTURE_ID_MAPPING = new Object2ObjectAVLTreeMap<>();

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);

		switch (httpServletRequest.getPathInfo()) {
			case "/reset":
				reset();
				AbstractResourcePackCreatorServlet.returnStandardResponse(httpServletResponse, asyncContext);
				break;
			default:
				AbstractResourcePackCreatorServlet.returnErrorResponse(httpServletResponse, asyncContext);
				break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);

		switch (httpServletRequest.getPathInfo()) {
			case "/zip":
				reset();
				uploadZip(httpServletRequest, httpServletResponse, asyncContext);
				break;
			case "/resources":
				uploadResource(httpServletRequest, httpServletResponse, asyncContext);
				break;
			default:
				AbstractResourcePackCreatorServlet.returnErrorResponse(httpServletResponse, asyncContext);
				break;
		}
	}

	private static void reset() {
		final ObjectArrayList<Identifier> texturesToDestroy = new ObjectArrayList<>();
		texturesToDestroy.addAll(TEXTURE_ID_MAPPING.values());
		AbstractResourcePackCreatorServlet.resourceWrapper = null;
		MODELS.clear();
		TEXTURES.clear();
		TEXTURE_ID_MAPPING.clear();

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.execute(() -> texturesToDestroy.forEach(texture -> {
			try {
				minecraftClient.getTextureManager().destroyTexture(texture);
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

				try (final ZipInputStream zipInputStream = new ZipInputStream(part.getInputStream())) {
					ZipEntry zipEntry;

					while ((zipEntry = zipInputStream.getNextEntry()) != null) {
						final String name = zipEntry.getName();
						Init.LOGGER.debug("Reading {}", name);
						final byte[] bytes = IOUtils.toByteArray(zipInputStream);
						final String content = new String(bytes);

						if (name.equals(String.format("assets/%s/%s.json", Init.MOD_ID, CustomResourceLoader.CUSTOM_RESOURCES_ID))) {
							customResourcesObject = Utilities.parseJson(content);
						} else if (name.endsWith(".png") || name.endsWith(".bbmodel") || name.endsWith(".obj") || name.endsWith(".mtl")) {
							resourceUploadTasks.add(() -> uploadResource(name, bytes, content));
						} else if (name.endsWith(".json")) {
							jsonCache.put(name, content);
						}

						zipInputStream.closeEntry();
					}
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}

				if (customResourcesObject == null) {
					break;
				}

				final CustomResources customResources = CustomResourcesConverter.convert(customResourcesObject, identifier -> jsonCache.getOrDefault(String.format("assets/%s/%s", identifier.getNamespace(), identifier.getPath()), ResourceManagerHelper.readResource(identifier)));
				final ObjectArrayList<VehicleWrapper> vehicles = new ObjectArrayList<>();

				customResources.iterateVehicles(vehicleResource -> {
					final ObjectArrayList<ModelPropertiesAndPositionDefinitionsWrapper> modelPropertiesList = new ObjectArrayList<>();
					vehicleResource.iterateModelsPropertiesAndDefinitions(modelPropertiesList::add);
					final ObjectArrayList<ModelPropertiesAndPositionDefinitionsWrapper> bogie1ModelPropertiesList = new ObjectArrayList<>();
					vehicleResource.iterateBogieModelsPropertiesAndDefinitions(0, bogie1ModelPropertiesList::add);
					final ObjectArrayList<ModelPropertiesAndPositionDefinitionsWrapper> bogie2ModelPropertiesList = new ObjectArrayList<>();
					vehicleResource.iterateBogieModelsPropertiesAndDefinitions(1, bogie2ModelPropertiesList::add);
					vehicles.add(new VehicleWrapper(vehicleResource, modelPropertiesList, bogie1ModelPropertiesList, bogie2ModelPropertiesList));
				});

				AbstractResourcePackCreatorServlet.resourceWrapper = new ResourceWrapper(vehicles, new ObjectArrayList<>(), new ObjectArrayList<>(), new ObjectArrayList<>(CustomResourceLoader.MINECRAFT_RESOURCES));
				resourceUploadTasks.forEach(Runnable::run);
				AbstractResourcePackCreatorServlet.returnStandardResponse(httpServletResponse, asyncContext);
				return;
			}
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}

		AbstractResourcePackCreatorServlet.returnErrorResponse(httpServletResponse, asyncContext);
	}

	private static void uploadResource(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		if (AbstractResourcePackCreatorServlet.resourceWrapper != null) {
			try {
				for (final Part part : httpServletRequest.getParts()) {
					try (final InputStream inputStream = part.getInputStream()) {
						final byte[] bytes = IOUtils.toByteArray(inputStream);
						uploadResource(part.getSubmittedFileName(), bytes, new String(bytes));
					} catch (Exception e) {
						Init.LOGGER.error("", e);
					}
				}
				AbstractResourcePackCreatorServlet.returnStandardResponse(httpServletResponse, asyncContext);
				return;
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		}

		AbstractResourcePackCreatorServlet.returnErrorResponse(httpServletResponse, asyncContext);
	}

	private static void uploadResource(String name, byte[] bytes, String content) {
		if (AbstractResourcePackCreatorServlet.resourceWrapper != null) {
			if (name.endsWith(".png")) {
				AbstractResourcePackCreatorServlet.resourceWrapper.addTextureResource(name);
				TEXTURES.put(name, bytes);
				final Identifier identifier = formatMinecraftResourceName(name);
				if (identifier != null) {
					try {
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
				}
			} else if (name.endsWith(".bbmodel")) {
				final BlockbenchModel blockbenchModel = new BlockbenchModel(new JsonReader(Utilities.parseJson(content)));
				final ObjectArrayList<String> modelParts = new ObjectArrayList<>();
				blockbenchModel.getOutlines().forEach(blockbenchOutline -> modelParts.add(blockbenchOutline.getName()));
				AbstractResourcePackCreatorServlet.resourceWrapper.addModelResource(new ModelWrapper(name, modelParts));
				MODELS.put(name, Utilities.getJsonObjectFromData(blockbenchModel).toString());
			} else if (name.endsWith(".obj")) {
				AbstractResourcePackCreatorServlet.resourceWrapper.addModelResource(new ModelWrapper(name, new ObjectArrayList<>(OptimizedModel.ObjModel.loadModel(content, mtlString -> "", textureString -> new Identifier(""), null, true, false).keySet())));
				MODELS.put(name, content);
			} else if (name.endsWith(".mtl")) {
				MODELS.put(name, content);
			}
		}
	}

	@Nullable
	private static Identifier formatMinecraftResourceName(String name) {
		final String[] nameSplit1 = name.split("\\.");
		if (nameSplit1.length < 2) {
			return null;
		} else {
			final String[] nameSplit2 = name.split("/");
			if (nameSplit2.length >= 3) {
				final String[] nameSplit3 = new String[nameSplit2.length - 2];
				System.arraycopy(nameSplit2, 2, nameSplit3, 0, nameSplit3.length);
				final Identifier identifier = new Identifier(Init.MOD_ID, String.format("resource_%s_%s.%s", nameSplit1[0].toLowerCase(Locale.ENGLISH).replaceAll("[^a-z0-9_]", "_"), Utilities.numberToPaddedHexString(new Random().nextLong()), nameSplit1[1]));
				TEXTURE_ID_MAPPING.put(String.format("%s:%s", nameSplit2[1], String.join("/", nameSplit3)), identifier);
				return identifier;
			} else {
				return null;
			}
		}
	}
}
