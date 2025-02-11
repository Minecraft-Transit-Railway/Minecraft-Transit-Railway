package org.mtr.mod.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.servlet.HttpResponseStatus;
import org.mtr.core.servlet.ServletBase;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServlet;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.BlockbenchModel;
import org.mtr.mod.resource.BlockbenchModelValidator;
import org.mtr.mod.resource.ModelWrapper;
import org.mtr.mod.resource.ResourceWrapper;
import org.mtr.mod.screen.ReloadCustomResourcesScreen;

import javax.annotation.Nullable;
import java.nio.ByteBuffer;
import java.nio.file.Path;

public abstract class AbstractResourcePackCreatorServlet extends HttpServlet {

	protected static final Object2ObjectAVLTreeMap<String, String> MODELS = new Object2ObjectAVLTreeMap<>();
	protected static final Object2ObjectAVLTreeMap<String, byte[]> TEXTURES = new Object2ObjectAVLTreeMap<>();

	@Nullable
	protected static ResourceWrapper resourceWrapper;

	protected static void returnStandardResponse(HttpServletResponse httpServletResponse, AsyncContext asyncContext, @Nullable String refreshVehicleId) {
		if (resourceWrapper == null) {
			ServletBase.sendResponse(httpServletResponse, asyncContext, String.valueOf((Object) null), "", HttpResponseStatus.OK);
		} else {
			resourceWrapper.updateMinecraftInfo();
			ServletBase.sendResponse(httpServletResponse, asyncContext, Utilities.getJsonObjectFromData(resourceWrapper).toString(), "", HttpResponseStatus.OK);
			if (refreshVehicleId != null) {
				final JsonObject vehiclesFlattened = resourceWrapper.flatten();
				final MinecraftClient minecraftClient = MinecraftClient.getInstance();
				minecraftClient.execute(() -> {
					if (refreshVehicleId.isEmpty()) {
						minecraftClient.openScreen(new Screen(new ReloadCustomResourcesScreen(() -> refreshVehicles(refreshVehicleId, vehiclesFlattened))));
					} else {
						refreshVehicles(refreshVehicleId, vehiclesFlattened);
					}
				});
			}
		}
	}

	protected static void returnErrorResponse(HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		ServletBase.sendResponse(httpServletResponse, asyncContext, new JsonObject().toString(), "", HttpResponseStatus.BAD_REQUEST);
	}

	protected static void uploadResource(String name, byte[] bytes, String content) {
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

	protected static Path getBackupFile() {
		return MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("config/mtr-resource-pack-creator-backup.json");
	}

	protected static void setEncoding(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		try {
			httpServletRequest.setCharacterEncoding("UTF-8");
			httpServletResponse.setCharacterEncoding("UTF-8");
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	private static void refreshVehicles(String refreshVehicleId, JsonObject vehiclesFlattened) {
		CustomResourceLoader.clearCustomVehicles(refreshVehicleId);
		new ResourceWrapper(new JsonReader(vehiclesFlattened), new ObjectArrayList<>(), new ObjectArrayList<>()).iterateVehicles(vehicleResourceWrapper -> {
			if (refreshVehicleId.isEmpty() || vehicleResourceWrapper.getId().equals(refreshVehicleId)) {
				CustomResourceLoader.registerVehicle(vehicleResourceWrapper.toVehicleResource(identifier -> {
					final String modelString = ResourcePackCreatorUploadServlet.getModel(identifier.data.toString());
					return modelString == null ? ResourceManagerHelper.readResource(identifier) : modelString;
				}, null, null));
			}
		});
	}
}
