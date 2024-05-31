package org.mtr.mod.client;

import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.resource.CustomResourcesConverter;
import org.mtr.libraries.com.google.gson.JsonElement;
import org.mtr.libraries.com.google.gson.JsonObject;
import org.mtr.libraries.com.google.gson.JsonParser;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.Keys;
import org.mtr.mod.resource.*;

import javax.annotation.Nullable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.function.Consumer;

public class CustomResourceLoader {

	private static long TEST_DURATION;

	public static final OptimizedRendererWrapper OPTIMIZED_RENDERER_WRAPPER = new OptimizedRendererWrapper();
	public static final String CUSTOM_RESOURCES_ID = "mtr_custom_resources";
	public static final String DEFAULT_RAIL_ID = "default";

	private static final Object2ObjectAVLTreeMap<String, JsonElement> RESOURCE_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<TransportMode, ObjectArrayList<VehicleResource>> VEHICLES = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<TransportMode, Object2ObjectAVLTreeMap<String, VehicleResource>> VEHICLES_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<SignResource> SIGNS = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<String, SignResource> SIGNS_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<RailResource> RAILS = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<String, RailResource> RAILS_CACHE = new Object2ObjectAVLTreeMap<>();

	static {
		for (final TransportMode transportMode : TransportMode.values()) {
			VEHICLES.put(transportMode, new ObjectArrayList<>());
			VEHICLES_CACHE.put(transportMode, new Object2ObjectAVLTreeMap<>());
		}
	}

	public static void reload() {
		OPTIMIZED_RENDERER_WRAPPER.beginReload();
		RESOURCE_CACHE.clear();
		VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.clear());
		VEHICLES_CACHE.forEach((transportMode, vehicleResourcesCache) -> vehicleResourcesCache.clear());
		SIGNS.clear();
		SIGNS_CACHE.clear();
		RAILS.clear();
		RAILS_CACHE.clear();
		TEST_DURATION = 0;

		final RailResource defaultRailResource = new RailResource(DEFAULT_RAIL_ID, "Default");
		RAILS.add(defaultRailResource);
		RAILS_CACHE.put(DEFAULT_RAIL_ID, defaultRailResource);

		ResourceManagerHelper.readAllResources(new Identifier(Init.MOD_ID, CUSTOM_RESOURCES_ID + ".json"), inputStream -> {
			try {
				final CustomResources customResources = CustomResourcesConverter.convert(readResource(inputStream).getAsJsonObject());
				customResources.iterateVehicles(vehicleResource -> {
					VEHICLES.get(vehicleResource.getTransportMode()).add(vehicleResource);
					VEHICLES_CACHE.get(vehicleResource.getTransportMode()).put(vehicleResource.getId(), vehicleResource);
				});
				customResources.iterateSigns(signResource -> {
					SIGNS.add(signResource);
					SIGNS_CACHE.put(signResource.getId(), signResource);
				});
				customResources.iterateRails(railResource -> {
					RAILS.add(railResource);
					RAILS_CACHE.put(railResource.getId(), railResource);
				});
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		});

		CustomResourcesConverter.convertRails(railResource -> {
			RAILS.add(railResource);
			RAILS_CACHE.put(railResource.getId(), railResource);
		});

		OPTIMIZED_RENDERER_WRAPPER.finishReload();
		Init.LOGGER.info("Loaded {} vehicles and completed door movement validation in {} ms", VEHICLES.values().stream().mapToInt(ObjectArrayList::size).reduce(0, Integer::sum), TEST_DURATION / 1E6);
		Init.LOGGER.info("Loaded {} signs", SIGNS.size());
		Init.LOGGER.info("Loaded {} rails", RAILS.size());
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

	public static ObjectImmutableList<RailResource> getRails() {
		return new ObjectImmutableList<>(RAILS);
	}

	public static void getRailById(String railId, Consumer<RailResource> ifPresent) {
		final RailResource railResource = RAILS_CACHE.get(railId);
		if (railResource != null) {
			ifPresent.accept(railResource);
		}
	}

	public static void readResource(@Nullable Identifier identifier, Consumer<JsonElement> consumer) {
		if (identifier != null) {
			final String identifierString = identifier.data.toString();
			final JsonElement jsonElement = RESOURCE_CACHE.get(identifierString);
			if (jsonElement == null) {
				if (Keys.DEBUG) {
					try (final InputStream inputStream = Files.newInputStream(MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("../src/main/resources/assets").resolve(identifier.getNamespace()).resolve(identifier.getPath()), StandardOpenOption.READ)) {
						final JsonElement newJsonElement = readResource(inputStream);
						consumer.accept(newJsonElement);
						RESOURCE_CACHE.put(identifierString, newJsonElement);
					} catch (Exception e) {
						Init.LOGGER.error("", e);
					}
				} else {
					ResourceManagerHelper.readResource(identifier, inputStream -> {
						final JsonElement newJsonElement = readResource(inputStream);
						consumer.accept(newJsonElement);
						RESOURCE_CACHE.put(identifierString, newJsonElement);
					});
				}
			} else {
				consumer.accept(jsonElement);
			}
		}
	}

	public static JsonElement readResource(InputStream inputStream) {
		try (final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
			return JsonParser.parseReader(inputStreamReader);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
			return new JsonObject();
		}
	}

	public static void incrementTestDuration(long duration) {
		TEST_DURATION += duration;
	}
}
