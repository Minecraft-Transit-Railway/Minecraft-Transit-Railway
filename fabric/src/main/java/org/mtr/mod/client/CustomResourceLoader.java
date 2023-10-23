package org.mtr.mod.client;

import org.mtr.core.data.CustomResources;
import org.mtr.core.data.SignResource;
import org.mtr.core.data.TransportMode;
import org.mtr.core.data.VehicleResource;
import org.mtr.core.serializers.JsonReader;
import org.mtr.core.tools.Utilities;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.render.RenderTrains;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class CustomResourceLoader {

	private static final Object2ObjectAVLTreeMap<TransportMode, ObjectArrayList<VehicleResource>> VEHICLES = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<TransportMode, Object2ObjectAVLTreeMap<String, VehicleResource>> VEHICLES_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<SignResource> SIGNS = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<String, SignResource> SIGNS_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final String CUSTOM_RESOURCES_ID = "mtr_custom_resources";

	static {
		for (final TransportMode transportMode : TransportMode.values()) {
			VEHICLES.put(transportMode, new ObjectArrayList<>());
			VEHICLES_CACHE.put(transportMode, new Object2ObjectAVLTreeMap<>());
		}
	}

	public static void reload() {
		RenderTrains.clearTextureAvailability();
		DynamicTextureCache.instance.resetFonts();
		VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.clear());
		VEHICLES_CACHE.forEach((transportMode, vehicleResourcesCache) -> vehicleResourcesCache.clear());
		SIGNS.clear();
		SIGNS_CACHE.clear();

		ResourceManagerHelper.readAllResources(new Identifier(Init.MOD_ID, CUSTOM_RESOURCES_ID + ".json"), inputStream -> {
			try {
				final CustomResources customResources = new CustomResources(readResource(inputStream));
				customResources.iterateVehicles(vehicleResource -> {
					VEHICLES.get(vehicleResource.getTransportMode()).add(vehicleResource);
					VEHICLES_CACHE.get(vehicleResource.getTransportMode()).put(vehicleResource.getId(), vehicleResource);
				});
				customResources.iterateSigns(signResource -> {
					SIGNS.add(signResource);
					SIGNS_CACHE.put(signResource.getId(), signResource);
				});
			} catch (Exception e) {
				Init.logException(e);
			}
		});

		Init.LOGGER.info("Loaded " + VEHICLES.values().stream().mapToInt(ObjectArrayList::size).reduce(0, Integer::sum) + " vehicle(s)");
		VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.forEach(VehicleResource::print));
		Init.LOGGER.info("Loaded " + SIGNS.size() + " sign(s)");
		SIGNS.forEach(SignResource::print);
	}

	public static void iterateVehicles(TransportMode transportMode, Consumer<VehicleResource> consumer) {
		VEHICLES.get(transportMode).forEach(consumer);
	}

	public static void getVehicleByIndex(TransportMode transportMode, int index, Consumer<VehicleResource> consumer) {
		if (index >= 0) {
			final VehicleResource vehicleResource = Utilities.getElement(VEHICLES.get(transportMode), index);
			if (vehicleResource != null) {
				consumer.accept(vehicleResource);
			}
		}
	}

	public static void getVehicleById(TransportMode transportMode, String vehicleId, Consumer<VehicleResource> consumer) {
		final VehicleResource vehicleResource = VEHICLES_CACHE.get(transportMode).get(vehicleId);
		if (vehicleResource != null) {
			consumer.accept(vehicleResource);
		}
	}

	public static void getSignById(String signId, Consumer<SignResource> consumer) {
		final SignResource signResource = SIGNS_CACHE.get(signId);
		if (signResource != null) {
			consumer.accept(signResource);
		}
	}

	public static ObjectArrayList<String> getSortedSignIds() {
		final ObjectArrayList<String> signIds = new ObjectArrayList<>(SIGNS_CACHE.keySet());
		signIds.sort(String::compareTo);
		return signIds;
	}

	public static JsonReader readResource(InputStream inputStream) {
		try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			return new JsonReader(JsonParser.parseReader(inputStreamReader));
		} catch (Exception e) {
			Init.logException(e);
			return new JsonReader(new JsonObject());
		}
	}
}
