package mtr.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.MTR;
import mtr.data.TrainType;
import mtr.gui.DashboardScreen;
import mtr.model.TrainClientRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

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
import java.util.stream.IntStream;

public class CustomResources implements SimpleSynchronousResourceReloadListener {

	public static final Map<String, CustomSign> CUSTOM_SIGNS = new HashMap<>();

	private static final String CUSTOM_RESOURCES_ID = "mtr_custom_resources";
	private static final String CUSTOM_TRAIN_ID_PREFIX = "mtr_custom_train_";
	private static final String CUSTOM_SIGN_ID_PREFIX = "mtr_custom_sign_";

	private static final String CUSTOM_TRAINS_KEY = "custom_trains";
	private static final String CUSTOM_SIGNS_KEY = "custom_signs";

	private static final String CUSTOM_TRAINS_BASE_TRAIN_TYPE = "base_train_type";
	private static final String CUSTOM_TRAINS_COLOR = "color";
	private static final String CUSTOM_TRAINS_MODEL = "model";
	private static final String CUSTOM_TRAINS_MODEL_PROPERTIES = "model_properties";
	private static final String CUSTOM_TRAINS_TEXTURE_ID = "texture_id";
	private static final String CUSTOM_TRAINS_SPEED_SOUND_COUNT = "speed_sound_count";
	private static final String CUSTOM_TRAINS_SPEED_SOUND_BASE_ID = "speed_sound_base_id";
	private static final String CUSTOM_TRAINS_DOOR_SOUND_BASE_ID = "door_sound_base_id";
	private static final String CUSTOM_TRAINS_HAS_GANGWAY_CONNECTION = "has_gangway_connection";

	private static final String CUSTOM_SIGNS_TEXTURE_ID = "texture_id";
	private static final String CUSTOM_SIGNS_FLIP_TEXTURE = "flip_texture";
	private static final String CUSTOM_SIGNS_CUSTOM_TEXT = "custom_text";
	private static final String CUSTOM_SIGNS_FLIP_CUSTOM_TEXT = "flip_custom_text";
	private static final String CUSTOM_SIGNS_SMALL = "small";
	private static final String CUSTOM_SIGNS_BACKGROUND_COLOR = "background_color";

	@Override
	public Identifier getFabricId() {
		return new Identifier(MTR.MOD_ID, CUSTOM_RESOURCES_ID);
	}

	@Override
	public void reload(ResourceManager manager) {
		TrainClientRegistry.reset();
		CUSTOM_SIGNS.clear();
		final List<String> customTrains = new ArrayList<>();

		readResource(manager, MTR.MOD_ID + ":" + CUSTOM_RESOURCES_ID + ".json", jsonConfig -> {
			try {
				jsonConfig.get(CUSTOM_TRAINS_KEY).getAsJsonObject().entrySet().forEach(entry -> {
					try {
						final JsonObject jsonObject = entry.getValue().getAsJsonObject();
						final String trainId = CUSTOM_TRAIN_ID_PREFIX + entry.getKey();

						final TrainType baseTrainType = TrainType.getOrDefault(jsonObject.get(CUSTOM_TRAINS_BASE_TRAIN_TYPE).getAsString());
						final TrainClientRegistry.TrainProperties baseTrainProperties = TrainClientRegistry.getTrainProperties(baseTrainType.toString());
						final int color = getOrDefault(jsonObject, CUSTOM_TRAINS_COLOR, baseTrainProperties.color, jsonElement -> DashboardScreen.colorStringToInt(jsonElement.getAsString()));
						final String textureId = getOrDefault(jsonObject, CUSTOM_TRAINS_TEXTURE_ID, baseTrainProperties.textureId, JsonElement::getAsString);
						final int speedSoundCount = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_COUNT, baseTrainProperties.speedSoundCount, JsonElement::getAsInt);
						final String speedSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_SPEED_SOUND_BASE_ID, baseTrainProperties.speedSoundBaseId, JsonElement::getAsString);
						final String doorSoundBaseId = getOrDefault(jsonObject, CUSTOM_TRAINS_DOOR_SOUND_BASE_ID, baseTrainProperties.doorSoundBaseId, JsonElement::getAsString);
						final boolean hasGangwayConnection = getOrDefault(jsonObject, CUSTOM_TRAINS_HAS_GANGWAY_CONNECTION, baseTrainProperties.hasGangwayConnection, JsonElement::getAsBoolean);

						if (jsonObject.has(CUSTOM_TRAINS_MODEL) && jsonObject.has(CUSTOM_TRAINS_MODEL_PROPERTIES)) {
							readResource(manager, jsonObject.get(CUSTOM_TRAINS_MODEL).getAsString(), jsonModel -> readResource(manager, jsonObject.get(CUSTOM_TRAINS_MODEL_PROPERTIES).getAsString(), jsonProperties -> {
								TrainClientRegistry.register(trainId, baseTrainType, new DynamicTrainModel(jsonModel, jsonProperties), textureId, speedSoundBaseId, doorSoundBaseId, color, hasGangwayConnection, speedSoundCount, 0.5F, false);
								customTrains.add(trainId);
							}));
						} else {
							TrainClientRegistry.register(trainId, baseTrainType, baseTrainProperties.model, textureId, speedSoundBaseId, doorSoundBaseId, color, hasGangwayConnection, speedSoundCount, 0.5F, false);
							customTrains.add(trainId);
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
						final int backgroundColor = getOrDefault(jsonObject, CUSTOM_SIGNS_BACKGROUND_COLOR, 0, jsonElement -> DashboardScreen.colorStringToInt(jsonElement.getAsString()));

						CUSTOM_SIGNS.put(CUSTOM_SIGN_ID_PREFIX + entry.getKey(), new CustomSign(new Identifier(jsonObject.get(CUSTOM_SIGNS_TEXTURE_ID).getAsString()), flipTexture, customText, flipCustomText, small, backgroundColor));
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

	private static void readResource(ResourceManager manager, String path, Consumer<JsonObject> callback) {
		try {
			manager.getAllResources(new Identifier(path)).forEach(resource -> {
				try (final InputStream stream = resource.getInputStream()) {
					callback.accept(new JsonParser().parse(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject());
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					resource.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (Exception ignored) {
		}
	}

	private static SoundEvent[] registerSoundEvents(int size, int groupSize, String path) {
		return IntStream.range(0, size).mapToObj(i -> {
			String group;
			switch (i % groupSize) {
				case 0:
					group = "a";
					break;
				case 1:
					group = "b";
					break;
				case 2:
					group = "c";
					break;
				default:
					group = "";
					break;
			}
			return new SoundEvent(new Identifier(path + (i / 3) + group));
		}).toArray(SoundEvent[]::new);
	}

	private static <T> T getOrDefault(JsonObject jsonObject, String key, T defaultValue, Function<JsonElement, T> function) {
		if (jsonObject.has(key)) {
			return function.apply(jsonObject.get(key));
		} else {
			return defaultValue;
		}
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
