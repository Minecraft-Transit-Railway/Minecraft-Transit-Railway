package mtr.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import mtr.MTR;
import mtr.data.TrainType;
import mtr.gui.DashboardScreen;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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
	private static final String CUSTOM_TRAINS_TEXTURE_ID = "texture_id";

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
	public void apply(ResourceManager manager) {
		customTrains.clear();
		customSigns.clear();

		try {
			manager.getAllResources(new Identifier(MTR.MOD_ID, CUSTOM_RESOURCES_ID + ".json")).forEach(resource -> {
				try (final InputStream stream = resource.getInputStream()) {
					final JsonObject jsonConfig = new JsonParser().parse(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();

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

								final TrainType trainType = TrainType.valueOf(jsonObject.get(CUSTOM_TRAINS_BASE_TRAIN_TYPE).getAsString().toUpperCase());

								customTrains.put(CUSTOM_TRAIN_ID_PREFIX + entry.getKey(), new CustomTrain(jsonObject.get(CUSTOM_TRAINS_NAME).getAsString(), color, trainType, jsonObject.get(CUSTOM_TRAINS_TEXTURE_ID).getAsString()));
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		} catch (Exception ignored) {
		}

		System.out.println("Loaded " + customTrains.size() + " custom train(s)");
		customTrains.keySet().forEach(System.out::println);
		System.out.println("Loaded " + customSigns.size() + " custom sign(s)");
		customSigns.keySet().forEach(System.out::println);
	}

	public static class CustomTrain {

		public final String name;
		public final int color;
		public final TrainType baseTrainType;
		public final Identifier textureId;

		public CustomTrain(String name, int color, TrainType baseTrainType, String textureIdString) {
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
		public final TrainType trainType;

		public TrainMapping(String customId, TrainType trainType) {
			this.customId = customId;
			this.trainType = trainType;
		}
	}
}
