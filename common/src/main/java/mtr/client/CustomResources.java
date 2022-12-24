package mtr.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.MTR;
import mtr.data.EnumHelper;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import mtr.model.ModelSimpleTrainBase;
import mtr.model.ModelTrainBase;
import mtr.render.JonModelTrainRenderer;
import mtr.render.RenderTrains;
import mtr.sound.JonTrainSound;
import mtr.sound.bve.BveTrainSound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomResources implements IResourcePackCreatorProperties, ICustomResources {

	public static final Map<String, CustomSign> CUSTOM_SIGNS = new HashMap<>();
	private static final List<Consumer<ResourceManager>> RELOAD_LISTENERS = new ArrayList<>();

	public static void reload(ResourceManager manager) {
		TrainClientRegistry.reset();
		RenderTrains.clearTextureAvailability();
		ClientData.DATA_CACHE.resetFonts();
		CUSTOM_SIGNS.clear();
		final List<String> customTrains = new ArrayList<>();

		readResource(manager, MTR.MOD_ID + ":" + CUSTOM_RESOURCES_ID + ".json", jsonConfig -> {
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

						final JonModelTrainRenderer jonRendererOrDefault = baseTrainProperties.renderer instanceof JonModelTrainRenderer ? (JonModelTrainRenderer) baseTrainProperties.renderer : new JonModelTrainRenderer(null, "", "", "");
						final JonTrainSound jonSoundOrDefault = baseTrainProperties.sound instanceof JonTrainSound ? (JonTrainSound) baseTrainProperties.sound : new JonTrainSound("", new JonTrainSound.JonTrainSoundConfig(null, 0, 0.5F, false, false));
						final String baseBveSoundBaseId = baseTrainProperties.sound instanceof BveTrainSound ? ((BveTrainSound) baseTrainProperties.sound).config.baseName : "";
						final ModelSimpleTrainBase<?> modelSimpleTrainBase = jonRendererOrDefault.model instanceof ModelSimpleTrainBase<?> ? ((ModelSimpleTrainBase<?>) jonRendererOrDefault.model) : null;

						final String textureId = getOrDefault(jsonObject, CUSTOM_TRAINS_TEXTURE_ID, jonRendererOrDefault.textureId, JsonElement::getAsString);
						final String gangwayConnectionId = getOrDefault(jsonObject, CUSTOM_TRAINS_GANGWAY_CONNECTION_ID, jonRendererOrDefault.gangwayConnectionId, JsonElement::getAsString);
						final String trainBarrierId = getOrDefault(jsonObject, CUSTOM_TRAINS_TRAIN_BARRIER_ID, jonRendererOrDefault.trainBarrierId, JsonElement::getAsString);
						final DoorAnimationType doorAnimationType = EnumHelper.valueOf(modelSimpleTrainBase == null ? DoorAnimationType.STANDARD : modelSimpleTrainBase.doorAnimationType, getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_ANIMATION_TYPE, "", JsonElement::getAsString));
						final boolean renderDoorOverlay = getOrDefault(jsonObject, CUSTOM_TRAINS_RENDER_DOOR_OVERLAY, modelSimpleTrainBase != null, JsonElement::getAsBoolean);
						final float riderOffset = getOrDefault(jsonObject, CUSTOM_TRAINS_RIDER_OFFSET, baseTrainProperties.riderOffset, JsonElement::getAsFloat);
						final String bveSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_BVE_SOUND_BASE_ID, baseBveSoundBaseId, JsonElement::getAsString);
						final int speedSoundCount = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_COUNT, jonSoundOrDefault.config.speedSoundCount, JsonElement::getAsInt);
						final String speedSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_BASE_ID, jonSoundOrDefault.soundId, JsonElement::getAsString);
						final String doorSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_SOUND_BASE_ID, jonSoundOrDefault.config.doorSoundBaseId, JsonElement::getAsString);
						final float doorCloseSoundTime = getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_CLOSE_SOUND_TIME, jonSoundOrDefault.config.doorCloseSoundTime, JsonElement::getAsFloat);
						final boolean accelSoundAtCoast = getOrDefault(jsonObject, CUSTOM_TRAINS_ACCEL_SOUND_AT_COAST, jonSoundOrDefault.config.useAccelerationSoundsWhenCoasting, JsonElement::getAsBoolean);
						final boolean constPlaybackSpeed = getOrDefault(jsonObject, CUSTOM_TRAINS_CONST_PLAYBACK_SPEED, jonSoundOrDefault.config.constantPlaybackSpeed, JsonElement::getAsBoolean);

						final boolean useBveSound;
						if (StringUtils.isEmpty(bveSoundBaseId)) {
							useBveSound = false;
						} else {
							if (jsonObject.has(CUSTOM_TRAINS_BVE_SOUND_BASE_ID)) {
								useBveSound = true;
							} else if (jsonObject.has(CUSTOM_TRAINS_SPEED_SOUND_BASE_ID)) {
								useBveSound = false;
							} else {
								useBveSound = baseTrainProperties.sound instanceof BveTrainSound;
							}
						}

						if (!baseTrainProperties.baseTrainType.isEmpty()) {
							final ModelTrainBase model = modelSimpleTrainBase == null ? jonRendererOrDefault.model : (ModelTrainBase) modelSimpleTrainBase.createNew(doorAnimationType, renderDoorOverlay);
							final String soundBaseId = useBveSound ? bveSoundBaseId : speedSoundBaseId;
							final JonTrainSound.JonTrainSoundConfig soundConfig = useBveSound ? null : new JonTrainSound.JonTrainSoundConfig(doorSoundBaseId, speedSoundCount, doorCloseSoundTime, accelSoundAtCoast, constPlaybackSpeed);
							TrainClientRegistry.register(trainId, baseTrainType, name, description, wikipediaArticle, model, textureId, color, gangwayConnectionId, trainBarrierId, riderOffset, riderOffset, baseTrainProperties.bogiePosition, baseTrainProperties.isJacobsBogie, soundBaseId, soundConfig);
							customTrains.add(trainId);
						}

						if (jsonObject.has(CUSTOM_TRAINS_MODEL) && jsonObject.has(CUSTOM_TRAINS_MODEL_PROPERTIES)) {
							readResource(manager, jsonObject.get(CUSTOM_TRAINS_MODEL).getAsString(), jsonModel -> readResource(manager, jsonObject.get(CUSTOM_TRAINS_MODEL_PROPERTIES).getAsString(), jsonProperties -> {
								IResourcePackCreatorProperties.checkSchema(jsonProperties);
								final String newBaseTrainType = String.format("%s_%s_%s", jsonProperties.get(KEY_PROPERTIES_TRANSPORT_MODE).getAsString(), jsonProperties.get(KEY_PROPERTIES_LENGTH).getAsInt(), jsonProperties.get(KEY_PROPERTIES_WIDTH).getAsInt());

								// TODO temporary code for backwards compatibility
								final String gangwayConnectionId2 = gangwayConnectionId.isEmpty() ? getOrDefault(jsonObject, "has_gangway_connection", true, JsonElement::getAsBoolean) ? "mtr:textures/entity/sp1900" : "" : gangwayConnectionId;
								final String newBaseTrainType2 = baseTrainType.startsWith("base_") ? baseTrainType.replace("base_", "train_") : newBaseTrainType;
								final boolean useLegacy = jsonProperties.has("parts_normal");
								// TODO temporary code end

								final ModelTrainBase model = useLegacy ? new DynamicTrainModelLegacy(jsonModel, jsonProperties, doorAnimationType) : new DynamicTrainModel(jsonModel, jsonProperties, doorAnimationType);
								final String soundBaseId = useBveSound ? bveSoundBaseId : speedSoundBaseId;
								final JonTrainSound.JonTrainSoundConfig soundConfig = useBveSound ? null : new JonTrainSound.JonTrainSoundConfig(doorSoundBaseId, speedSoundCount, doorCloseSoundTime, accelSoundAtCoast, constPlaybackSpeed);
								TrainClientRegistry.register(trainId, newBaseTrainType2.toLowerCase(Locale.ENGLISH), name, description, wikipediaArticle, model, textureId, color, gangwayConnectionId2, trainBarrierId, riderOffset, riderOffset, baseTrainProperties.bogiePosition, baseTrainProperties.isJacobsBogie, soundBaseId, soundConfig);
								customTrains.add(trainId);
							}));
						}
					} catch (Exception e) {
						e.printStackTrace();
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

						CUSTOM_SIGNS.put(CUSTOM_SIGN_ID_PREFIX + entry.getKey(), new CustomSign(new ResourceLocation(jsonObject.get(CUSTOM_SIGNS_TEXTURE_ID).getAsString()), flipTexture, customText, flipCustomText, small, backgroundColor));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} catch (Exception ignored) {
			}
		});

		RELOAD_LISTENERS.forEach(resourceManagerConsumer -> resourceManagerConsumer.accept(manager));

		System.out.println("Loaded " + customTrains.size() + " custom train(s)");
		customTrains.forEach(System.out::println);
		System.out.println("Loaded " + CUSTOM_SIGNS.size() + " custom sign(s)");
		CUSTOM_SIGNS.keySet().forEach(System.out::println);
	}

	public static int colorStringToInt(String string) {
		try {
			return Integer.parseInt(string.toUpperCase(Locale.ENGLISH).replaceAll("[^\\dA-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static void readResource(ResourceManager manager, String path, Consumer<JsonObject> callback) {
		try {
			UtilitiesClient.getResources(manager, new ResourceLocation(path)).forEach(resource -> {
				try (final InputStream stream = Utilities.getInputStream(resource)) {
					callback.accept(new JsonParser().parse(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject());
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					Utilities.closeResource(resource);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception ignored) {
		}
	}

	private static <T> T getOrDefault(JsonObject jsonObject, String key, T defaultValue, Function<JsonElement, T> function) {
		if (jsonObject.has(key)) {
			return function.apply(jsonObject.get(key));
		} else {
			return defaultValue;
		}
	}

	public static void registerReloadListener(Consumer<ResourceManager> listener) {
		RELOAD_LISTENERS.add(listener);
	}

	public static class CustomSign {

		public final ResourceLocation textureId;
		public final boolean flipTexture;
		public final String customText;
		public final boolean flipCustomText;
		public final boolean small;
		public final int backgroundColor;

		public CustomSign(ResourceLocation textureId, boolean flipTexture, String customText, boolean flipCustomText, boolean small, int backgroundColor) {
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
