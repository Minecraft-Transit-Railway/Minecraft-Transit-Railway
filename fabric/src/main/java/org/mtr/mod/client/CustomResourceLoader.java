package org.mtr.mod.client;

import org.apache.commons.io.IOUtils;
import org.mtr.core.data.TransportMode;
import org.mtr.core.tool.Utilities;
import org.mtr.legacy.resource.CustomResourcesConverter;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.ResourceManagerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.Keys;
import org.mtr.mod.config.Config;
import org.mtr.mod.resource.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CustomResourceLoader {

	private static long TEST_DURATION;

	public static final OptimizedRendererWrapper OPTIMIZED_RENDERER_WRAPPER = new OptimizedRendererWrapper();
	public static final String CUSTOM_RESOURCES_ID = "mtr_custom_resources";
	public static final String CUSTOM_RESOURCES_PENDING_MIGRATION_ID = "mtr_custom_resources_pending_migration";
	public static final String DEFAULT_RAIL_ID = "default";
	public static final String DEFAULT_RAIL_3D_ID = "default_3d";
	public static final String DEFAULT_RAIL_3D_SIDING_ID = "default_3d_siding";

	private static final Object2ObjectAVLTreeMap<String, String> RESOURCE_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<TransportMode, ObjectArrayList<VehicleResource>> VEHICLES = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<TransportMode, Object2ObjectAVLTreeMap<String, ObjectBooleanImmutablePair<VehicleResource>>> VEHICLES_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final Object2ObjectAVLTreeMap<TransportMode, Object2ObjectAVLTreeMap<String, Object2ObjectAVLTreeMap<String, ObjectArrayList<String>>>> VEHICLES_TAGS = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<SignResource> SIGNS = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<String, SignResource> SIGNS_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<RailResource> RAILS = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<String, RailResource> RAILS_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArrayList<ObjectResource> OBJECTS = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<String, ObjectResource> OBJECTS_CACHE = new Object2ObjectAVLTreeMap<>();
	private static final ObjectArraySet<MinecraftModelResource> MINECRAFT_MODEL_RESOURCES = new ObjectArraySet<>();
	private static final ObjectArraySet<String> MINECRAFT_TEXTURE_RESOURCES = new ObjectArraySet<>();

	static {
		for (final TransportMode transportMode : TransportMode.values()) {
			VEHICLES.put(transportMode, new ObjectArrayList<>());
			VEHICLES_CACHE.put(transportMode, new Object2ObjectAVLTreeMap<>());
			VEHICLES_TAGS.put(transportMode, new Object2ObjectAVLTreeMap<>());
		}
	}

	public static void reload() {
		MINECRAFT_MODEL_RESOURCES.clear();
		MINECRAFT_TEXTURE_RESOURCES.clear();
		RESOURCE_CACHE.clear();
		VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.clear());
		VEHICLES_CACHE.forEach((transportMode, vehicleResourcesCache) -> vehicleResourcesCache.clear());
		VEHICLES_TAGS.forEach((transportMode, vehicleResourcesCache) -> vehicleResourcesCache.clear());
		SIGNS.clear();
		SIGNS_CACHE.clear();
		RAILS.clear();
		RAILS_CACHE.clear();
		OBJECTS.clear();
		OBJECTS_CACHE.clear();
		TEST_DURATION = 0;

		final RailResource defaultRailResource = new RailResource(DEFAULT_RAIL_ID, "Default", CustomResourceLoader::readResource);
		RAILS.add(defaultRailResource);
		RAILS_CACHE.put(DEFAULT_RAIL_ID, defaultRailResource);

		ResourceManagerHelper.readAllResources(new Identifier(Init.MOD_ID, CUSTOM_RESOURCES_ID + ".json"), inputStream -> {
			try {
				final CustomResources customResources = CustomResourcesConverter.convert(Config.readResource(inputStream).getAsJsonObject(), CustomResourceLoader::readResource);
				customResources.iterateVehicles(vehicleResource -> registerVehicle(vehicleResource, false));
				customResources.iterateSigns(signResource -> {
					SIGNS.add(signResource);
					SIGNS_CACHE.put(signResource.getId(), signResource);
				});
				customResources.iterateRails(railResource -> {
					RAILS.add(railResource);
					RAILS_CACHE.put(railResource.getId(), railResource);
				});
				customResources.iterateObjects(objectResource -> {
					OBJECTS.add(objectResource);
					OBJECTS_CACHE.put(objectResource.getId(), objectResource);
				});
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		});

		// TODO temporary code for loading models pending migration
		ResourceManagerHelper.readAllResources(new Identifier(Init.MOD_ID, CUSTOM_RESOURCES_PENDING_MIGRATION_ID + ".json"), inputStream -> {
			try {
				CustomResourcesConverter.convert(Config.readResource(inputStream).getAsJsonObject(), CustomResourceLoader::readResource).iterateVehicles(vehicleResource -> registerVehicle(vehicleResource, false));
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		});

		CustomResourcesConverter.convertRails(railResource -> {
			RAILS.add(railResource);
			RAILS_CACHE.put(railResource.getId(), railResource);
		}, CustomResourceLoader::readResource);

		CustomResourcesConverter.convertObjects(objectResource -> {
			OBJECTS.add(objectResource);
			OBJECTS_CACHE.put(objectResource.getId(), objectResource);
		}, CustomResourceLoader::readResource);

		VEHICLES.forEach((transportMode, vehicleResources) -> validateDataset("Vehicle", vehicleResources, VehicleResource::getId));
		validateDataset("Sign", SIGNS, SignResource::getId);
		validateDataset("Rail", RAILS, RailResource::getId);
		validateDataset("Object", OBJECTS, ObjectResource::getId);

		Init.LOGGER.info("Loaded {} vehicles and completed door movement validation in {} ms", VEHICLES.values().stream().mapToInt(ObjectArrayList::size).reduce(0, Integer::sum), TEST_DURATION / 1E6);
		Init.LOGGER.info("Loaded {} signs", SIGNS.size());
		Init.LOGGER.info("Loaded {} rails", RAILS.size());
		Init.LOGGER.info("Loaded {} objects", OBJECTS.size());

		final long time1 = System.currentTimeMillis();

		final int[] preloadedVehicleCount = {0};
		VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.forEach(vehicleResource -> {
			if (vehicleResource.shouldPreload) {
				vehicleResource.getCachedVehicleResource(0, 0, true);
				preloadedVehicleCount[0]++;
			}
		}));

		final long time2 = System.currentTimeMillis();
		if (preloadedVehicleCount[0] > 0) {
			Init.LOGGER.info("Preloaded {} vehicles in {} ms", preloadedVehicleCount[0], time2 - time1);
		}

		final int[] preloadedRailCount = {0};
		RAILS.forEach(railResource -> {
			if (railResource.shouldPreload) {
				railResource.preload();
				preloadedRailCount[0]++;
			}
		});

		final long time3 = System.currentTimeMillis();
		if (preloadedRailCount[0] > 0) {
			Init.LOGGER.info("Preloaded {} rails in {} ms", preloadedRailCount[0], time3 - time2);
		}

		final int[] preloadedObjectCount = {0};
		OBJECTS.forEach(objectResource -> {
			if (objectResource.shouldPreload) {
				objectResource.preload();
				preloadedObjectCount[0]++;
			}
		});

		final long time4 = System.currentTimeMillis();
		if (preloadedObjectCount[0] > 0) {
			Init.LOGGER.info("Preloaded {} objects in {} ms", preloadedObjectCount[0], time4 - time3);
		}
	}

	public static void iterateVehicles(TransportMode transportMode, Consumer<VehicleResource> consumer) {
		VEHICLES.get(transportMode).forEach(consumer);
	}

	public static void clearCustomVehicles(String vehicleId) {
		for (final TransportMode transportMode : TransportMode.values()) {
			final ObjectArrayList<String> vehicleIdsToRemove = new ObjectArrayList<>();
			VEHICLES_CACHE.get(transportMode).values().forEach(vehicleResourceDetails -> {
				final VehicleResource vehicleResource = vehicleResourceDetails.left();
				if (vehicleResourceDetails.rightBoolean() && (vehicleId.isEmpty() || vehicleResource.getId().equals(vehicleId))) {
					vehicleIdsToRemove.add(vehicleResource.getId());
					VEHICLES.get(transportMode).remove(vehicleResource);
					VEHICLES_TAGS.get(transportMode).remove(vehicleResource.getId());
				}
			});
			vehicleIdsToRemove.forEach(checkVehicleId -> VEHICLES_CACHE.get(transportMode).remove(checkVehicleId));
		}
	}

	/**
	 * For registering preview vehicles from the Resource Pack Creator
	 *
	 * @param vehicleResource the vehicle to register
	 */
	public static void registerVehicle(VehicleResource vehicleResource) {
		registerVehicle(vehicleResource, true);
	}

	/**
	 * Validate and report any abnormality of the loaded resources (e.g. Duplicated ids)
	 */
	private static <T> void validateDataset(String dataSetName, List<T> dataSet, Function<T, String> getId) {
		ObjectOpenHashSet<String> addedIds = new ObjectOpenHashSet<>();
		for(T data : dataSet) {
			String id = getId.apply(data);
			if(addedIds.contains(id)) {
				Init.LOGGER.warn("MTR {} resource contains duplicated id {}!", dataSetName, id);
			} else {
				addedIds.add(id);
			}
		}
	}

	public static void getVehicleByIndex(TransportMode transportMode, int index, Consumer<VehicleResource> ifPresent) {
		if (index >= 0) {
			final VehicleResource vehicleResource = Utilities.getElement(VEHICLES.get(transportMode), index);
			if (vehicleResource != null) {
				ifPresent.accept(vehicleResource);
			}
		}
	}

	public static void getVehicleById(TransportMode transportMode, String vehicleId, Consumer<ObjectBooleanImmutablePair<VehicleResource>> ifPresent) {
		final ObjectBooleanImmutablePair<VehicleResource> vehicleResourceDetails = VEHICLES_CACHE.get(transportMode).get(vehicleId);
		if (vehicleResourceDetails != null) {
			ifPresent.accept(vehicleResourceDetails);
		}
	}

	public static Object2ObjectAVLTreeMap<String, Object2ObjectAVLTreeMap<String, ObjectArrayList<String>>> getVehicleTags(TransportMode transportMode) {
		return VEHICLES_TAGS.get(transportMode);
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

	public static ObjectImmutableList<ObjectResource> getObjects() {
		return new ObjectImmutableList<>(OBJECTS);
	}

	public static void getObjectById(String objectId, Consumer<ObjectResource> ifPresent) {
		final ObjectResource objectResource = OBJECTS_CACHE.get(objectId);
		if (objectResource != null) {
			ifPresent.accept(objectResource);
		}
	}

	public static void incrementTestDuration(long duration) {
		TEST_DURATION += duration;
	}

	public static ObjectArrayList<MinecraftModelResource> getMinecraftModelResources() {
		return new ObjectArrayList<>(MINECRAFT_MODEL_RESOURCES);
	}

	public static ObjectArrayList<String> getTextureResources() {
		return new ObjectArrayList<>(MINECRAFT_TEXTURE_RESOURCES);
	}

	private static void registerVehicle(VehicleResource vehicleResource, boolean fromResourcePackCreator) {
		VEHICLES.get(vehicleResource.getTransportMode()).add(vehicleResource);
		VEHICLES_CACHE.get(vehicleResource.getTransportMode()).put(vehicleResource.getId(), new ObjectBooleanImmutablePair<>(vehicleResource, fromResourcePackCreator));
		vehicleResource.collectTags(VEHICLES_TAGS.get(vehicleResource.getTransportMode()));
		if (!fromResourcePackCreator) {
			vehicleResource.writeMinecraftResource(MINECRAFT_MODEL_RESOURCES, MINECRAFT_TEXTURE_RESOURCES);
		}
	}

	private static String readResource(Identifier identifier) {
		final String identifierString = identifier.data.toString();
		final String cache = RESOURCE_CACHE.get(identifierString);
		if (cache == null) {
			if (Keys.DEBUG) {
				try (final InputStream inputStream = Files.newInputStream(MinecraftClient.getInstance().getRunDirectoryMapped().toPath().resolve("../src/main/resources/assets").resolve(identifier.getNamespace()).resolve(identifier.getPath()), StandardOpenOption.READ)) {
					final String content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
					RESOURCE_CACHE.put(identifierString, content);
					return content;
				} catch (Exception e) {
					Init.LOGGER.error("", e);
					return "";
				}
			} else {
				final String content = ResourceManagerHelper.readResource(identifier);
				RESOURCE_CACHE.put(identifierString, content);
				return content;
			}
		} else {
			return cache;
		}
	}
}
