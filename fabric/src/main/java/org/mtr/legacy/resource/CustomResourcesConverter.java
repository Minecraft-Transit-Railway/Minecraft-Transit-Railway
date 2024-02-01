package org.mtr.legacy.resource;

import org.mtr.core.serializer.JsonReader;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.Init;
import org.mtr.mod.resource.CustomResources;
import org.mtr.mod.resource.SignResource;
import org.mtr.mod.resource.VehicleResource;

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
					vehicleResources.addAll(new LegacyVehicleResource(new JsonReader(entry.getValue())).convert(entry.getKey()));
				} catch (Exception e) {
					Init.logException(e);
				}
			});
		}

		final ObjectArrayList<SignResource> signResources = new ObjectArrayList<>();

		if (hasCustomSigns) {

		}

		return new CustomResources(vehicleResources, signResources);
	}
}
