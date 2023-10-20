package org.mtr.mod.client;

import org.mtr.core.data.CustomResources;
import org.mtr.core.serializers.JsonReader;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.render.RenderTrains;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomResourceLoader {

	public static final Object2ObjectAVLTreeMap<String, CustomSign> CUSTOM_SIGNS = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<Runnable> RELOAD_LISTENERS = new ObjectArrayList<>();
	private static final String CUSTOM_RESOURCES_ID = "mtr_custom_resources";

	public static void reload() {
		TrainClientRegistry.reset();
		RenderTrains.clearTextureAvailability();
		DynamicTextureCache.instance.resetFonts();
		CUSTOM_SIGNS.clear();
		final ObjectArrayList<String> customTrains = new ObjectArrayList<>();

		ResourceManagerHelper.readResource(new Identifier(Init.MOD_ID, CUSTOM_RESOURCES_ID + ".json"), inputStream -> {
			try {
				final CustomResources customResources = new CustomResources(new JsonReader(JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject()));
				// TODO
			} catch (Exception e) {
				Init.logException(e);
			}
		});

		RELOAD_LISTENERS.forEach(Runnable::run);

		Init.LOGGER.info("Loaded " + customTrains.size() + " custom train(s)");
		customTrains.forEach(Init.LOGGER::info);
		Init.LOGGER.info("Loaded " + CUSTOM_SIGNS.size() + " custom sign(s)");
		CUSTOM_SIGNS.keySet().forEach(Init.LOGGER::info);
	}

	public static int colorStringToInt(String string) {
		try {
			return Integer.parseInt(string.toUpperCase(Locale.ENGLISH).replaceAll("[^\\dA-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static void readResource(String path, Consumer<JsonObject> callback) {
		ResourceManagerHelper.readResource(new Identifier(Init.MOD_ID, path), inputStream -> callback.accept(JsonParser.parseReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).getAsJsonObject()));
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
