package org.mtr.legacy.resource;

import org.apache.commons.io.FilenameUtils;
import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.config.Config;
import org.mtr.mod.resource.*;

import java.util.Locale;
import java.util.function.Consumer;

public final class CustomResourcesConverter {

	public static CustomResources convert(JsonObject jsonObject, ResourceProvider resourceProvider) {
		final boolean hasCustomTrains = jsonObject.getAsJsonObject().has("custom_trains");
		final boolean hasCustomSigns = jsonObject.getAsJsonObject().has("custom_signs");

		if (!hasCustomTrains && !hasCustomSigns) {
			return new CustomResources(new JsonReader(jsonObject), resourceProvider);
		}

		final ObjectArrayList<VehicleResource> vehicleResources = new ObjectArrayList<>();

		if (hasCustomTrains) {
			jsonObject.getAsJsonObject("custom_trains").entrySet().forEach(entry -> {
				try {
					new LegacyVehicleResource(new JsonReader(entry.getValue())).convert(vehicleResources, entry.getKey().toLowerCase(Locale.ENGLISH), resourceProvider);
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			});
		}

		final ObjectArrayList<SignResource> signResources = new ObjectArrayList<>();

		if (hasCustomSigns) {
			jsonObject.getAsJsonObject("custom_signs").entrySet().forEach(entry -> {
				try {
					new LegacySignResource(new JsonReader(entry.getValue())).convert(signResources, entry.getKey());
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			});
		}

		return new CustomResources(vehicleResources, signResources);
	}

	public static void convertRails(Consumer<RailResource> callback, ResourceProvider resourceProvider) {
		ResourceManagerHelper.readDirectory("rails", (identifier, inputStream) -> {
			if (identifier.getNamespace().equals(Init.MOD_ID_NTE) && identifier.getPath().endsWith(".json")) {
				try {
					final JsonObject jsonObject = Config.readResource(inputStream).getAsJsonObject();
					if (jsonObject.has("model")) {
						callback.accept(new LegacyRailResource(new JsonReader(jsonObject)).convert(FilenameUtils.getBaseName(identifier.getPath()), resourceProvider));
					} else {
						jsonObject.entrySet().forEach(entry -> callback.accept(new LegacyRailResource(new JsonReader(entry.getValue())).convert(entry.getKey().toLowerCase(Locale.ENGLISH), resourceProvider)));
					}
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			}
		});
	}

	public static void convertObjects(Consumer<ObjectResource> callback, ResourceProvider resourceProvider) {
		ResourceManagerHelper.readDirectory("eyecandies", (identifier, inputStream) -> {
			if (identifier.getNamespace().equals(Init.MOD_ID_NTE) && identifier.getPath().endsWith(".json")) {
				try {
					final JsonObject jsonObject = Config.readResource(inputStream).getAsJsonObject();
					if (jsonObject.has("model")) {
						callback.accept(new LegacyObjectResource(new JsonReader(jsonObject)).convert(FilenameUtils.getBaseName(identifier.getPath()), resourceProvider));
					} else {
						jsonObject.entrySet().forEach(entry -> callback.accept(new LegacyObjectResource(new JsonReader(entry.getValue())).convert(entry.getKey().toLowerCase(Locale.ENGLISH), resourceProvider)));
					}
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			}
		});
	}
}
