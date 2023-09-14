package org.mtr.mod.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.commons.lang3.StringUtils;
import org.mtr.core.data.EnumHelper;
import org.mtr.init.MTR;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.model.ModelSimpleTrainBase;
import org.mtr.mod.model.ModelTrainBase;
import org.mtr.mod.render.LegacyVehicleRenderer;
import org.mtr.mod.render.RenderTrains;
import org.mtr.mod.sound.BveVehicleSound;
import org.mtr.mod.sound.LegacyVehicleSound;
import org.mtr.mod.sound.LegacyVehicleSoundConfig;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomResources implements IResourcePackCreatorProperties, ICustomResources {

	public static final Object2ObjectAVLTreeMap<String, CustomSign> CUSTOM_SIGNS = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<Runnable> RELOAD_LISTENERS = new ObjectArrayList<>();

	public static void reload() {
		TrainClientRegistry.reset();
		RenderTrains.clearTextureAvailability();
		DynamicTextureCache.instance.resetFonts();
		CUSTOM_SIGNS.clear();
		final ObjectArrayList<String> customTrains = new ObjectArrayList<>();

		readResource(Init.MOD_ID + ":" + CUSTOM_RESOURCES_ID + ".json", jsonConfig -> {
			try {
				jsonConfig.get(CUSTOM_TRAINS_KEY).getAsJsonObject().entrySet().forEach(entry -> {
					try {
						final JsonObject jsonObject = entry.getValue().getAsJsonObject();
						final String name = getOrDefault(jsonObject, CUSTOM_TRAINS_NAME, entry.getKey(), JsonElement::getAsString);
						final int color = getOrDefault(jsonObject, CUSTOM_TRAINS_COLOR, 0, jsonElement -> colorStringToInt(jsonElement.getAsString()));
						final String trainId = CUSTOM_TRAIN_ID_PREFIX + entry.getKey();

						final String baseTrainType = getOrDefault(jsonObject, CUSTOM_TRAINS_BASE_TRAIN_TYPE, "", JsonElement::getAsString);
						final TrainProperties baseTrainProperties = TrainClientRegistry.getTrainProperties(baseTrainType);
						final String description = getOrDefault(jsonObject, CUSTOM_TRAINS_DESCRIPTION, baseTrainProperties.description, JsonElement::getAsString);
						final String wikipediaArticle = getOrDefault(jsonObject, CUSTOM_TRAINS_WIKIPEDIA_ARTICLE, baseTrainProperties.wikipediaArticle, JsonElement::getAsString);

						final LegacyVehicleRenderer legacyVehicleRenderer = baseTrainProperties.legacyVehicleRenderer;
						final LegacyVehicleSound vehicleSound = baseTrainProperties.vehicleSound instanceof LegacyVehicleSound ? (LegacyVehicleSound) baseTrainProperties.vehicleSound : new LegacyVehicleSound("", new LegacyVehicleSoundConfig(null, 0, 0.5F, false, false));
						final String baseBveSoundBaseId = baseTrainProperties.vehicleSound instanceof BveVehicleSound ? ((BveVehicleSound) baseTrainProperties.vehicleSound).config.baseName : "";
						final ModelSimpleTrainBase<?> modelSimpleTrainBase = legacyVehicleRenderer.model instanceof ModelSimpleTrainBase<?> ? ((ModelSimpleTrainBase<?>) legacyVehicleRenderer.model) : null;

						final String textureId = getOrDefault(jsonObject, CUSTOM_TRAINS_TEXTURE_ID, legacyVehicleRenderer.textureId, JsonElement::getAsString);
						final String gangwayConnectionId = getOrDefault(jsonObject, CUSTOM_TRAINS_GANGWAY_CONNECTION_ID, legacyVehicleRenderer.gangwayConnectionId, JsonElement::getAsString);
						final String trainBarrierId = getOrDefault(jsonObject, CUSTOM_TRAINS_TRAIN_BARRIER_ID, legacyVehicleRenderer.trainBarrierId, JsonElement::getAsString);
						final DoorAnimationType doorAnimationType = EnumHelper.valueOf(modelSimpleTrainBase == null ? DoorAnimationType.STANDARD : modelSimpleTrainBase.doorAnimationType, getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_ANIMATION_TYPE, "", JsonElement::getAsString));
						final boolean renderDoorOverlay = getOrDefault(jsonObject, CUSTOM_TRAINS_RENDER_DOOR_OVERLAY, modelSimpleTrainBase != null, JsonElement::getAsBoolean);
						final float riderOffset = getOrDefault(jsonObject, CUSTOM_TRAINS_RIDER_OFFSET, baseTrainProperties.riderOffset, JsonElement::getAsFloat);
						final String bveSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_BVE_SOUND_BASE_ID, baseBveSoundBaseId, JsonElement::getAsString);
						final int speedSoundCount = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_COUNT, vehicleSound.config.speedSoundCount, JsonElement::getAsInt);
						final String speedSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_BASE_ID, vehicleSound.soundId, JsonElement::getAsString);
						final String doorSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_SOUND_BASE_ID, vehicleSound.config.doorSoundBaseId, JsonElement::getAsString);
						final float doorCloseSoundTime = getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_CLOSE_SOUND_TIME, vehicleSound.config.doorCloseSoundTime, JsonElement::getAsFloat);
						final boolean accelSoundAtCoast = getOrDefault(jsonObject, CUSTOM_TRAINS_ACCEL_SOUND_AT_COAST, vehicleSound.config.useAccelerationSoundsWhenCoasting, JsonElement::getAsBoolean);
						final boolean constPlaybackSpeed = getOrDefault(jsonObject, CUSTOM_TRAINS_CONST_PLAYBACK_SPEED, vehicleSound.config.constantPlaybackSpeed, JsonElement::getAsBoolean);

						final boolean useBveSound;
						if (StringUtils.isEmpty(bveSoundBaseId)) {
							useBveSound = false;
						} else {
							if (jsonObject.has(CUSTOM_TRAINS_BVE_SOUND_BASE_ID)) {
								useBveSound = true;
							} else if (jsonObject.has(CUSTOM_TRAINS_SPEED_SOUND_BASE_ID)) {
								useBveSound = false;
							} else {
								useBveSound = baseTrainProperties.vehicleSound instanceof BveVehicleSound;
							}
						}

						if (!baseTrainProperties.baseTrainType.isEmpty()) {
							final ModelTrainBase model = modelSimpleTrainBase == null ? legacyVehicleRenderer.model : (ModelTrainBase) modelSimpleTrainBase.createNew(doorAnimationType, renderDoorOverlay);
							final String soundBaseId = useBveSound ? bveSoundBaseId : speedSoundBaseId;
							final LegacyVehicleSoundConfig soundConfig = useBveSound ? null : new LegacyVehicleSoundConfig(doorSoundBaseId, speedSoundCount, doorCloseSoundTime, accelSoundAtCoast, constPlaybackSpeed);
							TrainClientRegistry.register(trainId, baseTrainType, name, description, wikipediaArticle, model, textureId, color, gangwayConnectionId, trainBarrierId, riderOffset, riderOffset, baseTrainProperties.bogiePosition, baseTrainProperties.isJacobsBogie, soundBaseId, soundConfig);
							customTrains.add(trainId);
						}

						if (jsonObject.has(CUSTOM_TRAINS_MODEL) && jsonObject.has(CUSTOM_TRAINS_MODEL_PROPERTIES)) {
							readResource(jsonObject.get(CUSTOM_TRAINS_MODEL).getAsString(), jsonModel -> readResource(jsonObject.get(CUSTOM_TRAINS_MODEL_PROPERTIES).getAsString(), jsonProperties -> {
								IResourcePackCreatorProperties.checkSchema(jsonProperties);
								final String newBaseTrainType = String.format("%s_%s_%s", jsonProperties.get(KEY_PROPERTIES_TRANSPORT_MODE).getAsString(), jsonProperties.get(KEY_PROPERTIES_LENGTH).getAsInt(), jsonProperties.get(KEY_PROPERTIES_WIDTH).getAsInt());
								final ModelTrainBase model = new DynamicTrainModel(jsonModel, jsonProperties, doorAnimationType);
								final String soundBaseId = useBveSound ? bveSoundBaseId : speedSoundBaseId;
								final LegacyVehicleSoundConfig soundConfig = useBveSound ? null : new LegacyVehicleSoundConfig(doorSoundBaseId, speedSoundCount, doorCloseSoundTime, accelSoundAtCoast, constPlaybackSpeed);
								TrainClientRegistry.register(trainId, newBaseTrainType.toLowerCase(Locale.ENGLISH), name, description, wikipediaArticle, model, textureId, color, gangwayConnectionId, trainBarrierId, riderOffset, riderOffset, baseTrainProperties.bogiePosition, baseTrainProperties.isJacobsBogie, soundBaseId, soundConfig);
								customTrains.add(trainId);
							}));
						}
					} catch (Exception e) {
						Init.logException(e);
					}
				});
			} catch (Exception ignored) {
			}

			try {
				jsonConfig.get(CUSTOM_SIGNS_KEY).getAsJsonObject().entrySet().forEach(entry -> {
					try {
						final JsonObject jsonObject = entry.getValue().getAsJsonObject();

						final boolean flipTexture = getOrDefault(jsonObject, CUSTOM_SIGNS_FLIP_TEXTURE, false, JsonElement::getAsBoolean);
						final String customText = getOrDefault(jsonObject, CUSTOM_SIGNS_CUSTOM_TEXT, "", JsonElement::getAsString);
						final boolean flipCustomText = getOrDefault(jsonObject, CUSTOM_SIGNS_FLIP_CUSTOM_TEXT, false, JsonElement::getAsBoolean);
						final boolean small = getOrDefault(jsonObject, CUSTOM_SIGNS_SMALL, false, JsonElement::getAsBoolean);
						final int backgroundColor = getOrDefault(jsonObject, CUSTOM_SIGNS_BACKGROUND_COLOR, 0, jsonElement -> colorStringToInt(jsonElement.getAsString()));

						CUSTOM_SIGNS.put(CUSTOM_SIGN_ID_PREFIX + entry.getKey(), new CustomSign(new Identifier(jsonObject.get(CUSTOM_SIGNS_TEXTURE_ID).getAsString()), flipTexture, customText, flipCustomText, small, backgroundColor));
					} catch (Exception e) {
						Init.logException(e);
					}
				});
			} catch (Exception ignored) {
			}
		});

		RELOAD_LISTENERS.forEach(Runnable::run);

		Init.LOGGER.info("Loaded " + customTrains.size() + " custom train(s)");
		customTrains.forEach(System.out::println);
		Init.LOGGER.info("Loaded " + CUSTOM_SIGNS.size() + " custom sign(s)");
		CUSTOM_SIGNS.keySet().forEach(System.out::println);
	}

	public static int colorStringToInt(String string) {
		try {
			return Integer.parseInt(string.toUpperCase(Locale.ENGLISH).replaceAll("[^\\dA-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static void readResource(String path, Consumer<JsonObject> callback) {
		ResourceManagerHelper.readResource(new Identifier(path), inputStream -> callback.accept(JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject()));
	}

	private static <T> T getOrDefault(JsonObject jsonObject, String key, T defaultValue, Function<JsonElement, T> function) {
		if (jsonObject.has(key)) {
			return function.apply(jsonObject.get(key));
		} else {
			return defaultValue;
		}
	}

	public static void registerReloadListener(Runnable listener) {
		RELOAD_LISTENERS.add(listener);
	}

	public static class CustomSign {

		public final Identifier textureId;
		public final boolean flipTexture;
		public final String customText;
		public final boolean flipCustomText;
		public final boolean small;
		public final int backgroundColor;

		public CustomSign(Identifier textureId, boolean flipTexture, String customText, boolean flipCustomText, boolean small, int backgroundColor) {
			this.textureId = textureId;
			this.flipTexture = flipTexture;
			this.customText = customText;
			this.flipCustomText = flipCustomText;
			this.small = small;
			this.backgroundColor = backgroundColor;
		}

		public boolean hasCustomText() {
			return !customText.isEmpty();
		}
	}
}
