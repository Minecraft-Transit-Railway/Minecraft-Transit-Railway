package mtr.client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.MTR;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import mtr.render.RenderTrains;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomResources implements IResourcePackCreatorProperties, ICustomResources {

	public static final Map<String, CustomSign> CUSTOM_SIGNS = new HashMap<>();


	public static void reload(ResourceManager manager) {
		TrainClientRegistry.reset();
		RenderTrains.clearTextureAvailability();
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
						final TrainClientRegistry.TrainProperties baseTrainProperties = TrainClientRegistry.getTrainProperties(baseTrainType);

						final String textureId = getOrDefault(jsonObject, CUSTOM_TRAINS_TEXTURE_ID, baseTrainProperties.textureId, JsonElement::getAsString);
						final String gangwayConnectionId = getOrDefault(jsonObject, CUSTOM_TRAINS_GANGWAY_CONNECTION_ID, baseTrainProperties.gangwayConnectionId, JsonElement::getAsString);
						final String trainBarrierId = getOrDefault(jsonObject, CUSTOM_TRAINS_TRAIN_BARRIER_ID, baseTrainProperties.trainBarrierId, JsonElement::getAsString);
						final float riderOffset = getOrDefault(jsonObject, CUSTOM_TRAINS_RIDER_OFFSET, baseTrainProperties.riderOffset, JsonElement::getAsFloat);
						final int speedSoundCount = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_COUNT, baseTrainProperties.speedSoundCount, JsonElement::getAsInt);
						final String speedSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_BASE_ID, baseTrainProperties.speedSoundBaseId, JsonElement::getAsString);
						final String doorSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_SOUND_BASE_ID, baseTrainProperties.doorSoundBaseId, JsonElement::getAsString);
						final float doorCloseSoundTime = getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_CLOSE_SOUND_TIME, baseTrainProperties.doorCloseSoundTime, JsonElement::getAsFloat);

						if (!baseTrainProperties.baseTrainType.isEmpty()) {
							TrainClientRegistry.register(trainId, baseTrainType, baseTrainProperties.model, textureId, speedSoundBaseId, doorSoundBaseId, name, color, gangwayConnectionId, trainBarrierId, riderOffset, speedSoundCount, doorCloseSoundTime, false);
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

								TrainClientRegistry.register(trainId, newBaseTrainType2.toLowerCase(), useLegacy ? new DynamicTrainModelLegacy(jsonModel, jsonProperties) : new DynamicTrainModel(jsonModel, jsonProperties), textureId, speedSoundBaseId, doorSoundBaseId, name, color, gangwayConnectionId2, trainBarrierId, riderOffset, speedSoundCount, doorCloseSoundTime, false);
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

		System.out.println("Loaded " + customTrains.size() + " custom train(s)");
		customTrains.forEach(System.out::println);
		System.out.println("Loaded " + CUSTOM_SIGNS.size() + " custom sign(s)");
		CUSTOM_SIGNS.keySet().forEach(System.out::println);
	}

	public static int colorStringToInt(String string) {
		try {
			return Integer.parseInt(string.toUpperCase().replaceAll("[^\\dA-F]", ""), 16);
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
