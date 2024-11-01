package org.mtr.mod.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.core.servlet.HttpResponseStatus;
import org.mtr.core.servlet.ServletBase;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.resource.CustomResourcesConverter;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServlet;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.libraries.javax.servlet.http.Part;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.BlockbenchModel;
import org.mtr.mod.resource.CustomResources;
import org.mtr.mod.resource.ResourceWrapper;
import org.mtr.mod.sound.BveVehicleSound;
import org.mtr.mod.sound.BveVehicleSoundConfig;
import org.mtr.mod.sound.LegacyVehicleSound;
import org.mtr.mod.sound.VehicleSoundBase;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public final class ResourcePackCreatorServlet extends HttpServlet {

	private static VehicleSoundBase vehicleSoundBase;
	private static long motorSoundExpiry;
	private static float speed;
	private static float targetSpeed;
	private static float acceleration;

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);

		switch (httpServletRequest.getPathInfo()) {
			case "/play-sound":
				playSound(httpServletRequest, httpServletResponse, asyncContext);
				break;
			default:
				ServletBase.sendResponse(httpServletResponse, asyncContext, "", "", HttpResponseStatus.BAD_REQUEST);
				break;
		}
	}

	@Override
	protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);

		switch (httpServletRequest.getPathInfo()) {
			case "/upload":
				upload(httpServletRequest, httpServletResponse, asyncContext);
				break;
			default:
				ServletBase.sendResponse(httpServletResponse, asyncContext, "", "", HttpResponseStatus.BAD_REQUEST);
				break;
		}
	}

	public static void tick(long millisElapsed) {
		if (System.currentTimeMillis() > motorSoundExpiry) {
			vehicleSoundBase = null;
		}
		if (vehicleSoundBase != null) {
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
			if (clientPlayerEntity != null) {
				final float oldSpeed = speed;
				final float speedChange = millisElapsed * acceleration;
				if (targetSpeed > speed) {
					speed = Math.min(targetSpeed, speed + speedChange);
				} else if (targetSpeed < speed) {
					speed = Math.max(targetSpeed, speed - speedChange);
				}
				vehicleSoundBase.playMotorSound(clientPlayerEntity.getBlockPos(), speed, speed - oldSpeed, acceleration, true);
			}
		}
	}

	private static void playSound(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		final String id = httpServletRequest.getParameter("id");
		final String type = httpServletRequest.getParameter("type");
		final String mode = httpServletRequest.getParameter("mode");
		final int value = parseInt(httpServletRequest.getParameter("value"));
		final int legacySpeedSoundCount = parseInt(httpServletRequest.getParameter("speed-sound-count"));
		final boolean legacyUseAccelerationSoundsWhenCoasting = "true".equalsIgnoreCase(httpServletRequest.getParameter("use-acceleration-sounds-when-coasting"));
		final boolean legacyConstantPlaybackSpeed = "true".equalsIgnoreCase(httpServletRequest.getParameter("constant-playback-speed"));

		if (id == null || type == null || mode == null) {
			ServletBase.sendResponse(httpServletResponse, asyncContext, "", "", HttpResponseStatus.BAD_REQUEST);
			return;
		}

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		minecraftClient.execute(() -> {
			final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
			if (clientPlayerEntity != null) {
				final VehicleSoundBase tempVehicleSoundBase;
				switch (type.toLowerCase(Locale.ENGLISH)) {
					case "bve":
						tempVehicleSoundBase = new BveVehicleSound(new BveVehicleSoundConfig(id));
						break;
					case "legacy":
						tempVehicleSoundBase = new LegacyVehicleSound(id, legacySpeedSoundCount, legacyUseAccelerationSoundsWhenCoasting, legacyConstantPlaybackSpeed, id, 0.5);
						break;
					default:
						tempVehicleSoundBase = null;
						break;
				}

				if (tempVehicleSoundBase != null) {
					switch (mode.toLowerCase(Locale.ENGLISH)) {
						case "speed":
							vehicleSoundBase = tempVehicleSoundBase;
							motorSoundExpiry = System.currentTimeMillis() + 5000;
							speed = targetSpeed;
							targetSpeed = value / 3600F;
							acceleration = Math.abs(targetSpeed - speed) / 200;
							break;
						case "door-open":
							tempVehicleSoundBase.playDoorSound(clientPlayerEntity.getBlockPos(), 1, 0);
							break;
						case "door-close":
							tempVehicleSoundBase.playDoorSound(clientPlayerEntity.getBlockPos(), 0, 1);
							break;
					}
				}
			}
		});

		final JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("paused", minecraftClient.isPaused());
		ServletBase.sendResponse(httpServletResponse, asyncContext, jsonObject.toString(), "", HttpResponseStatus.OK);
	}

	private static void upload(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		try {
			for (final Part part : httpServletRequest.getParts()) {
				Init.LOGGER.info("Processing {} uploaded from the Resource Pack Creator", part.getSubmittedFileName());
				final Object2ObjectAVLTreeMap<String, String> files = getUploadedFilesInZip(part);
				final CustomResources customResources = CustomResourcesConverter.convert(Utilities.parseJson(files.get(String.format("assets/%s/%s.json", Init.MOD_ID, CustomResourceLoader.CUSTOM_RESOURCES_ID))));
				final ObjectArrayList<BlockbenchModel> blockbenchModels = new ObjectArrayList<>();
				final ObjectArrayList<String> textures = new ObjectArrayList<>();

				files.forEach((name, content) -> {
					final String nameLowerCase = name.toLowerCase(Locale.ENGLISH);
					if (nameLowerCase.endsWith(".png")) {
						textures.add(name);
					} else if (nameLowerCase.endsWith(".bbmodel")) {
						blockbenchModels.add(new BlockbenchModel(new JsonReader(Utilities.parseJson(content))));
					}
				});

				final ResourceWrapper resourceWrapper = new ResourceWrapper(customResources, blockbenchModels, textures);
				ServletBase.sendResponse(httpServletResponse, asyncContext, Utilities.getJsonObjectFromData(resourceWrapper).toString(), "", HttpResponseStatus.OK);
				return;
			}
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}

		ServletBase.sendResponse(httpServletResponse, asyncContext, "", "", HttpResponseStatus.BAD_REQUEST);
	}

	private static Object2ObjectAVLTreeMap<String, String> getUploadedFilesInZip(Part part) {
		final Object2ObjectAVLTreeMap<String, String> files = new Object2ObjectAVLTreeMap<>();

		try (final ZipInputStream zipInputStream = new ZipInputStream(part.getInputStream())) {
			ZipEntry zipEntry;

			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				final String name = zipEntry.getName();
				Init.LOGGER.debug("Reading {}", name);
				final StringBuilder stringBuilder = new StringBuilder();

				final byte[] bytes = new byte[4096];
				int length;
				while ((length = zipInputStream.read(bytes)) != -1) {
					stringBuilder.append(new String(bytes, 0, length));
				}

				files.put(name, stringBuilder.toString());
				zipInputStream.closeEntry();
			}
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}

		return files;
	}

	private static int parseInt(@Nullable String value) {
		try {
			return value == null ? 0 : Integer.parseInt(value);
		} catch (Exception ignored) {
			return 0;
		}
	}
}
