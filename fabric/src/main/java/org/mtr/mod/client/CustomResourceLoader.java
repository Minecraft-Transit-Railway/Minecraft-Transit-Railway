package org.mtr.mod.client;

import org.mtr.core.data.TransportMode;
import org.mtr.core.serializer.JsonReader;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.resource.CustomResources;
import org.mtr.mod.resource.SignResource;
import org.mtr.mod.resource.VehicleResource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class CustomResourceLoader {

	public static final OptimizedRenderer OPTIMIZED_RENDERER = new OptimizedRenderer();
	private static final Object2ObjectAVLTreeMap<String, JsonElement> RESOURCE_CACHE = new Object2ObjectAVLTreeMap<>();
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
		OPTIMIZED_RENDERER.beginReload();
		RESOURCE_CACHE.clear();
		VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.clear());
		VEHICLES_CACHE.forEach((transportMode, vehicleResourcesCache) -> vehicleResourcesCache.clear());
		SIGNS.clear();
		SIGNS_CACHE.clear();

		ResourceManagerHelper.readAllResources(new Identifier(Init.MOD_ID, CUSTOM_RESOURCES_ID + ".json"), inputStream -> {
			try {
				final CustomResources customResources = new CustomResources(new JsonReader(readResource(inputStream)));
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

		OPTIMIZED_RENDERER.finishReload();
		Init.LOGGER.info("Loaded " + VEHICLES.values().stream().mapToInt(ObjectArrayList::size).reduce(0, Integer::sum) + " vehicle(s)");
		Init.LOGGER.info("Loaded " + SIGNS.size() + " sign(s)");
	}

	public static void iterateVehicles(TransportMode transportMode, Consumer<VehicleResource> consumer) {
		VEHICLES.get(transportMode).forEach(consumer);
	}

	public static void getVehicleByIndex(TransportMode transportMode, int index, Consumer<VehicleResource> ifPresent) {
		if (index >= 0) {
			final VehicleResource vehicleResource = Utilities.getElement(VEHICLES.get(transportMode), index);
			if (vehicleResource != null) {
				ifPresent.accept(vehicleResource);
			}
		}
	}

	public static void getVehicleById(TransportMode transportMode, String vehicleId, Consumer<VehicleResource> ifPresent) {
		final VehicleResource vehicleResource = VEHICLES_CACHE.get(transportMode).get(vehicleId);
		if (vehicleResource != null) {
			ifPresent.accept(vehicleResource);
		}
	}

	public static void getSignById(String signId, Consumer<SignResource> ifPresent) {
		final SignResource signResource = SIGNS_CACHE.get(signId);
		if (signResource != null) {
			ifPresent.accept(signResource);
		}
	}

	public static ObjectArrayList<String> getSortedSignIds() {
		final ObjectArrayList<String> signIds = new ObjectArrayList<>(SIGNS_CACHE.keySet());
		signIds.sort(String::compareTo);
		return signIds;
	}

	public static void readResource(Identifier identifier, Consumer<JsonElement> consumer) {
		final String identifierString = identifier.data.toString();
		final JsonElement jsonElement = RESOURCE_CACHE.get(identifierString);
		if (jsonElement == null) {
			ResourceManagerHelper.readResource(identifier, inputStream -> {
				final JsonElement newJsonElement = readResource(inputStream);
				consumer.accept(newJsonElement);
				RESOURCE_CACHE.put(identifierString, newJsonElement);
			});
		} else {
			consumer.accept(jsonElement);
		}
	}

	private static JsonElement readResource(InputStream inputStream) {
		try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(inputStreamReader);
		} catch (Exception e) {
			Init.logException(e);
			return new JsonObject();
		}
	}
}
