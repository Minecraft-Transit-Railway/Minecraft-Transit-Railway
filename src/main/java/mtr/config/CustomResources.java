package mtr.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.MTR;
import mtr.data.TrainRegistry;
import mtr.gui.DashboardScreen;
import mtr.model.TrainModelRegistry;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public class CustomResources implements SimpleSynchronousResourceReloadListener {

	public static final Map<String, CustomTrain> customTrains = new HashMap<>();
	public static final Map<String, CustomSign> customSigns = new HashMap<>();

	private static final String CUSTOM_RESOURCES_ID = "mtr_custom_resources";
	private static final String CUSTOM_TRAIN_ID_PREFIX = "mtr_custom_train_";
	private static final String CUSTOM_SIGN_ID_PREFIX = "mtr_custom_sign_";

	private static final String CUSTOM_TRAINS_KEY = "custom_trains";
	private static final String CUSTOM_SIGNS_KEY = "custom_signs";

	private static final String CUSTOM_TRAINS_NAME = "name";
	private static final String CUSTOM_TRAINS_COLOR = "color";
	private static final String CUSTOM_TRAINS_BASE_TRAIN_TYPE = "base_train_type";
	private static final String CUSTOM_TRAINS_MODEL = "model";
	private static final String CUSTOM_TRAINS_MODEL_PROPERTIES = "model_properties";
	private static final String CUSTOM_TRAINS_TEXTURE_ID = "texture_id";

	private static final String CUSTOM_TRAINS_MODEL_PROPERTIES_LENGTH = "length";
	private static final String CUSTOM_TRAINS_MODEL_PROPERTIES_WIDTH = "width";
	private static final String CUSTOM_TRAINS_MODEL_PROPERTIES_HAS_CONNECTION = "has_connection";
	private static final String CUSTOM_TRAINS_MODEL_PROPERTIES_SOUND_COUNT = "sound_count";

	private static final String CUSTOM_SIGNS_TEXTURE_ID = "texture_id";
	private static final String CUSTOM_SIGNS_FLIP_TEXTURE = "flip_texture";
	private static final String CUSTOM_SIGNS_CUSTOM_TEXT = "custom_text";
	private static final String CUSTOM_SIGNS_FLIP_CUSTOM_TEXT = "flip_custom_text";
	private static final String CUSTOM_SIGNS_SMALL = "small";
	private static final String CUSTOM_SIGNS_BACKGROUND_COLOR = "background_color";

	private static final String SOUND_ACCELERATION = "_acceleration_";
	private static final String SOUND_DECELERATION = "_deceleration_";
	private static final String SOUND_DOOR_OPEN = "_door_open";
	private static final String SOUND_DOOR_CLOSE = "_door_close";

	@Override
	public Identifier getFabricId() {
		return new Identifier(MTR.MOD_ID, CUSTOM_RESOURCES_ID);
	}

	@Override
	public void reload(ResourceManager manager) {
		customTrains.clear();
		customSigns.clear();

		readResource(manager, MTR.MOD_ID + ":" + CUSTOM_RESOURCES_ID + ".json", jsonConfig -> {
			try {
				jsonConfig.get(CUSTOM_TRAINS_KEY).getAsJsonObject().entrySet().forEach(entry -> {
					try {
						final JsonObject jsonObject = entry.getValue().getAsJsonObject();

						final int color;
						if (jsonObject.has(CUSTOM_TRAINS_COLOR)) {
							color = DashboardScreen.colorStringToInt(jsonObject.get(CUSTOM_TRAINS_COLOR).getAsString());
						} else {
							color = 0;
						}

						if (jsonObject.has(CUSTOM_TRAINS_BASE_TRAIN_TYPE)) {
							final TrainRegistry.TrainType trainType = TrainRegistry.getTrainType(jsonObject.get(CUSTOM_TRAINS_BASE_TRAIN_TYPE).getAsString());
							customTrains.put(CUSTOM_TRAIN_ID_PREFIX + entry.getKey(), new CustomTrain(jsonObject.get(CUSTOM_TRAINS_NAME).getAsString(), color, trainType, jsonObject.get(CUSTOM_TRAINS_TEXTURE_ID).getAsString()));
						} else if (jsonObject.has(CUSTOM_TRAINS_MODEL) && jsonObject.has(CUSTOM_TRAINS_MODEL_PROPERTIES)) {
							readResource(manager, jsonObject.get(CUSTOM_TRAINS_MODEL).getAsString(), jsonModel -> readResource(manager, jsonObject.get(CUSTOM_TRAINS_MODEL_PROPERTIES).getAsString(), jsonProperties -> {
								final int length = jsonProperties.get(CUSTOM_TRAINS_MODEL_PROPERTIES_LENGTH).getAsInt();
								final int width = jsonProperties.get(CUSTOM_TRAINS_MODEL_PROPERTIES_WIDTH).getAsInt();
								final boolean hasConnection = jsonProperties.get(CUSTOM_TRAINS_MODEL_PROPERTIES_HAS_CONNECTION).getAsBoolean();
								final int soundCount = jsonProperties.get(CUSTOM_TRAINS_MODEL_PROPERTIES_SOUND_COUNT).getAsInt();

								final SoundEvent[] accelerationSoundEvents = registerSoundEvents(soundCount, 3, entry.getKey() + SOUND_ACCELERATION);
								final SoundEvent[] decelerationSoundEvents = registerSoundEvents(soundCount, 3, entry.getKey() + SOUND_DECELERATION);
								final SoundEvent doorOpenSoundEvent = new SoundEvent(new Identifier(entry.getKey() + SOUND_DOOR_OPEN));
								final SoundEvent doorCloseSoundEvent = new SoundEvent(new Identifier(entry.getKey() + SOUND_DOOR_CLOSE));

								TrainRegistry.register(color, length, -1, width, hasConnection, accelerationSoundEvents, decelerationSoundEvents, doorOpenSoundEvent, doorCloseSoundEvent, 0.5F, false, entry.getKey());
								TrainModelRegistry.register(entry.getKey(), new DynamicTrainModel(jsonModel, jsonProperties));
								System.out.println("Registered train id " + entry.getKey());
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

						final boolean flipTexture;
						if (jsonObject.has(CUSTOM_SIGNS_FLIP_TEXTURE)) {
							flipTexture = jsonObject.get(CUSTOM_SIGNS_FLIP_TEXTURE).getAsBoolean();
						} else {
							flipTexture = false;
						}

						final String customText;
						if (jsonObject.has(CUSTOM_SIGNS_CUSTOM_TEXT)) {
							customText = jsonObject.get(CUSTOM_SIGNS_CUSTOM_TEXT).getAsString();
						} else {
							customText = "";
						}

						final boolean flipCustomText;
						if (jsonObject.has(CUSTOM_SIGNS_FLIP_CUSTOM_TEXT)) {
							flipCustomText = jsonObject.get(CUSTOM_SIGNS_FLIP_CUSTOM_TEXT).getAsBoolean();
						} else {
							flipCustomText = false;
						}

						final boolean small;
						if (jsonObject.has(CUSTOM_SIGNS_SMALL)) {
							small = jsonObject.get(CUSTOM_SIGNS_SMALL).getAsBoolean();
						} else {
							small = false;
						}

						final int backgroundColor;
						if (jsonObject.has(CUSTOM_SIGNS_BACKGROUND_COLOR)) {
							backgroundColor = DashboardScreen.colorStringToInt(jsonObject.get(CUSTOM_SIGNS_BACKGROUND_COLOR).getAsString());
						} else {
							backgroundColor = 0;
						}

						customSigns.put(CUSTOM_SIGN_ID_PREFIX + entry.getKey(), new CustomSign(new Identifier(jsonObject.get(CUSTOM_SIGNS_TEXTURE_ID).getAsString()), flipTexture, customText, flipCustomText, small, backgroundColor));
					} catch (Exception e) {
						e.printStackTrace();
					}
				});
			} catch (Exception ignored) {
			}
		});

		System.out.println("Loaded " + customTrains.size() + " custom train(s)");
		customTrains.keySet().forEach(System.out::println);
		System.out.println("Loaded " + customSigns.size() + " custom sign(s)");
		customSigns.keySet().forEach(System.out::println);
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

	public static class CustomTrain {

		public final String name;
		public final int color;
		public final TrainRegistry.TrainType baseTrainType;
		public final Identifier textureId;

		public CustomTrain(String name, int color, TrainRegistry.TrainType baseTrainType, String textureIdString) {
			this.name = name;
			this.color = color;
			this.baseTrainType = baseTrainType;
			textureId = new Identifier(textureIdString);
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

	public static class TrainMapping {

		public final String customId;
		public final TrainRegistry.TrainType trainType;

		public TrainMapping(String customId, TrainRegistry.TrainType trainType) {
			this.customId = customId;
			this.trainType = trainType;
		}
	}
}
