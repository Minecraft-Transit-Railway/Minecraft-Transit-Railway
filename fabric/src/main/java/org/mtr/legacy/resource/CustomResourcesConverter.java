package org.mtr.legacy.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.CustomResources;
import org.mtr.mod.resource.RailResource;
import org.mtr.mod.resource.SignResource;
import org.mtr.mod.resource.VehicleResource;

import java.util.Locale;
import java.util.function.Consumer;

public final class CustomResourcesConverter {

	public static CustomResources convert(JsonObject jsonObject) {
		final boolean hasCustomTrains = jsonObject.getAsJsonObject().has("custom_trains");
		final boolean hasCustomSigns = jsonObject.getAsJsonObject().has("custom_signs");

		if (!hasCustomTrains && !hasCustomSigns) {
			return new CustomResources(new JsonReader(jsonObject));
		}

		final ObjectArrayList<VehicleResource> vehicleResources = new ObjectArrayList<>();

		if (hasCustomTrains) {
			jsonObject.getAsJsonObject("custom_trains").entrySet().forEach(entry -> {
				try {
					new LegacyVehicleResource(new JsonReader(entry.getValue())).convert(vehicleResources, entry.getKey().toLowerCase(Locale.ENGLISH));
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

	public static void convertRails(Consumer<RailResource> callback) {
		ResourceManagerHelper.readDirectory("rails", (identifier, inputStream) -> {
			if (identifier.getNamespace().equals("mtrsteamloco") && identifier.getPath().endsWith(".json")) {
				try {
					CustomResourceLoader.readResource(inputStream).getAsJsonObject().entrySet().forEach(entry -> callback.accept(new LegacyRailResource(new JsonReader(entry.getValue())).convert(entry.getKey())));
				} catch (Exception e) {
					Init.LOGGER.error("", e);
				}
			}
		});
	}
}
