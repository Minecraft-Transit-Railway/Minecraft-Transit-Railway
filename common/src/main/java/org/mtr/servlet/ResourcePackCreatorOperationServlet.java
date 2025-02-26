package org.mtr.mod.servlet;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.javax.servlet.AsyncContext;
import org.mtr.libraries.javax.servlet.http.HttpServletRequest;
import org.mtr.libraries.javax.servlet.http.HttpServletResponse;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.ResourceWrapper;
import org.mtr.mod.screen.FakePauseScreen;
import org.mtr.mod.screen.ReloadCustomResourcesScreen;
import org.mtr.mod.sound.BveVehicleSound;
import org.mtr.mod.sound.BveVehicleSoundConfig;
import org.mtr.mod.sound.LegacyVehicleSound;
import org.mtr.mod.sound.VehicleSoundBase;

import javax.annotation.Nullable;
import java.nio.file.Files;
import java.util.Locale;

public final class ResourcePackCreatorOperationServlet extends AbstractResourcePackCreatorServlet {

	@Nullable
	private static VehicleSoundBase vehicleSoundBase;
	private static long motorSoundExpiry;
	private static float speed;
	private static float targetSpeed;
	private static float acceleration;
	private static int doorMultiplier = -1;

	@Override
	protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		final AsyncContext asyncContext = httpServletRequest.startAsync();
		asyncContext.setTimeout(0);
		setEncoding(httpServletRequest, httpServletResponse);
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();

		switch (httpServletRequest.getPathInfo()) {
			case "/refresh":
				if (resourceWrapper == null && Files.exists(getBackupFile())) {
					// TODO load backup
					returnStandardResponse(httpServletResponse, asyncContext, null);
				} else {
					returnStandardResponse(httpServletResponse, asyncContext, null);
				}
				break;
			case "/play-sound":
				playSound(httpServletRequest, httpServletResponse, asyncContext);
				break;
			case "/preview":
				preview(httpServletRequest, httpServletResponse, asyncContext);
				break;
			case "/force-reload":
				minecraftClient.execute(() -> minecraftClient.openScreen(new Screen(new ReloadCustomResourcesScreen(() -> {
					CustomResourceLoader.reload();
					returnStandardResponse(httpServletResponse, asyncContext, "");
				}))));
				break;
			case "/resume-game":
				minecraftClient.execute(() -> minecraftClient.openScreen(new Screen(new FakePauseScreen())));
				returnStandardResponse(httpServletResponse, asyncContext, null);
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
			case "/update":
				update(httpServletRequest, httpServletResponse, asyncContext);
				break;
			default:
				returnErrorResponse(httpServletResponse, asyncContext);
				break;
		}
	}

	public static void tick(long millisElapsed) {
		if (System.currentTimeMillis() > motorSoundExpiry && vehicleSoundBase != null) {
			vehicleSoundBase.dispose();
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

	public static int getDoorMultiplier() {
		return doorMultiplier;
	}

	private static void update(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		try {
			resourceWrapper = new ResourceWrapper(new JsonReader(JsonParser.parseReader(httpServletRequest.getReader())), new ObjectArrayList<>(CustomResourceLoader.getMinecraftModelResources()), new ObjectArrayList<>(CustomResourceLoader.getTextureResources()));
			resourceWrapper.clean();
			// TODO save backup
			returnStandardResponse(httpServletResponse, asyncContext, httpServletRequest.getParameter("id"));
		} catch (Exception e) {
			Init.LOGGER.error("", e);
			returnErrorResponse(httpServletResponse, asyncContext);
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
			returnErrorResponse(httpServletResponse, asyncContext);
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

		returnStandardResponse(httpServletResponse, asyncContext, null);
	}

	private static void preview(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AsyncContext asyncContext) {
		final String doors = httpServletRequest.getParameter("doors");
		if ("open".equals(doors)) {
			doorMultiplier = 1;
		} else if ("close".equals(doors)) {
			doorMultiplier = -1;
		}

		returnStandardResponse(httpServletResponse, asyncContext, null);
	}

	private static int parseInt(@Nullable String value) {
		try {
			return value == null ? 0 : Integer.parseInt(value);
		} catch (Exception ignored) {
			return 0;
		}
	}
}
