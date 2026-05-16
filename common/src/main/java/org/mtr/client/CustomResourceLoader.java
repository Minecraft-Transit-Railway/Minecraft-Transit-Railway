package org.mtr.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.Nullable;
import org.mtr.Keys;
import org.mtr.MTR;
import org.mtr.config.Config;
import org.mtr.core.data.TransportMode;
import org.mtr.legacy.resource.CustomResourcesConverter;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.model.ModelLoaderBase;
import org.mtr.resource.*;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Vehicle, sign, rail, object and lift resource loading and registration.
 *
 * <p>{@link #reload()} is the single entry point invoked on every Minecraft resource
 * reload. It clears every static cache, then loads in this order:</p>
 * <ol>
 *   <li>The bundled default rail.</li>
 *   <li>Every {@code mtr:mtr_custom_resources.json} found across active resource packs.
 *       Each file goes through {@link org.mtr.legacy.resource.CustomResourcesConverter}
 *       which transparently upgrades MTR 3.x format JSON to the current schema (see
 *       {@code docs/MIGRATIONS.md} §1).</li>
 *   <li>The temporary {@code mtr_custom_resources_pending_migration.json} manifest, which
 *       holds bundled vehicles in the process of being moved to the new schema (see
 *       {@code docs/MIGRATIONS.md} §2).</li>
 *   <li>{@code mtrsteamloco}-namespaced rails and eyecandies, converted on the fly.</li>
 *   <li>Validation pass and then a synchronous preload pass for resources matching the
 *       user-configured preload pattern.</li>
 * </ol>
 *
 * <p>All public accessors return defensive snapshots so callers may iterate freely without
 * worrying about concurrent reloads.</p>
 *
 * <p>Performance notes for the load pipeline live in
 * {@code docs/PERFORMANCE.md} §1.</p>
 */
public class CustomResourceLoader {

	private static long TEST_DURATION;

	public static final String CUSTOM_RESOURCES_ID = "mtr_custom_resources";
	public static final String CUSTOM_RESOURCES_PENDING_MIGRATION_ID = "mtr_custom_resources_pending_migration";
	public static final String DEFAULT_RAIL_ID = "default";
	public static final String DEFAULT_RAIL_3D_ID = "default_3d";
	public static final String DEFAULT_RAIL_3D_SIDING_ID = "default_3d_siding";
	public static final String DEFAULT_LIFT_TRANSPARENT_ID = "default_transparent";

	/**
	 * Shared, thread-safe cache of resource-pack file contents keyed by the resource's
	 * full string identifier. Reads happen from both the main thread (during
	 * {@link #reload()}) and from worker threads (during deferred OBJ / Blockbench
	 * parsing). Kept unbounded for the lifetime of the JVM — see
	 * {@code docs/PERFORMANCE.md} §1.1 and §1.4 for the planned move to a bounded
	 * byte-array cache. The {@link MTR#randomString()}-free namespace means duplicate
	 * fetches always return identical content.
	 */
	private static final ConcurrentMap<String, String> RESOURCE_CACHE = new ConcurrentHashMap<>();
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
	private static final ObjectArrayList<LiftResource> LIFTS = new ObjectArrayList<>();
	private static final Object2ObjectAVLTreeMap<String, LiftResource> LIFTS_CACHE = new Object2ObjectAVLTreeMap<>();

	static {
		for (final TransportMode transportMode : TransportMode.values()) {
			VEHICLES.put(transportMode, new ObjectArrayList<>());
			VEHICLES_CACHE.put(transportMode, new Object2ObjectAVLTreeMap<>());
			VEHICLES_TAGS.put(transportMode, new Object2ObjectAVLTreeMap<>());
		}
	}

	/**
	 * Rebuild every resource cache from active resource packs.
	 *
	 * <p>Idempotent and safe to call from a Minecraft resource-reload listener. Heavy:
	 * blocks the main thread for the full duration of OBJ / Blockbench parsing for every
	 * vehicle, then again for the preload pass. See {@code docs/PERFORMANCE.md} §1 for the
	 * detailed cost breakdown and proposed parallelisation.</p>
	 */
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
		LIFTS.clear();
		LIFTS_CACHE.clear();
		TEST_DURATION = 0;
		DynamicTextureCache.instance.reload();

		final ObjectArrayList<SignResource> defaultSigns = new ObjectArrayList<>();

		final RailResource defaultRailResource = new RailResource(DEFAULT_RAIL_ID, "Default", CustomResourceLoader::readResource);
		RAILS.add(defaultRailResource);
		RAILS_CACHE.put(DEFAULT_RAIL_ID, defaultRailResource);

		ResourceManagerHelper.readAllResources(Identifier.of(MTR.MOD_ID, CUSTOM_RESOURCES_ID + ".json"), inputStream -> {
			try {
				final CustomResources customResources = CustomResourcesConverter.convert(Config.readResource(inputStream).getAsJsonObject(), CustomResourceLoader::readResource);
				customResources.iterateVehicles(vehicleResource -> registerVehicle(vehicleResource, false));
				customResources.iterateSigns(signResource -> {
					if (signResource.isDefault) {
						defaultSigns.add(signResource);
					} else {
						SIGNS.add(signResource);
						SIGNS_CACHE.put(signResource.signId, signResource);
					}
				});
				customResources.iterateRails(railResource -> {
					RAILS.add(railResource);
					RAILS_CACHE.put(railResource.getId(), railResource);
				});
				customResources.iterateObjects(objectResource -> {
					OBJECTS.add(objectResource);
					OBJECTS_CACHE.put(objectResource.getId(), objectResource);
				});
				customResources.iterateLifts(liftResource -> {
					LIFTS.add(liftResource);
					LIFTS_CACHE.put(liftResource.getId(), liftResource);
				});
			} catch (Exception e) {
				MTR.LOGGER.error("Failed to parse custom resources from {}.json — skipping this resource pack entry", CUSTOM_RESOURCES_ID, e);
			}
		});

		// TODO temporary code for loading models pending migration
		ResourceManagerHelper.readAllResources(Identifier.of(MTR.MOD_ID, CUSTOM_RESOURCES_PENDING_MIGRATION_ID + ".json"), inputStream -> {
			try {
				CustomResourcesConverter.convert(Config.readResource(inputStream).getAsJsonObject(), CustomResourceLoader::readResource).iterateVehicles(vehicleResource -> registerVehicle(vehicleResource, false));
			} catch (Exception e) {
				MTR.LOGGER.error("Failed to parse pending-migration custom resources from {}.json — skipping this resource pack entry", CUSTOM_RESOURCES_PENDING_MIGRATION_ID, e);
			}
		});

		SIGNS.addAll(0, defaultSigns);
		defaultSigns.forEach(signResource -> SIGNS_CACHE.put(signResource.signId, signResource));

		CustomResourcesConverter.convertRails(railResource -> {
			RAILS.add(railResource);
			RAILS_CACHE.put(railResource.getId(), railResource);
		}, CustomResourceLoader::readResource);

		CustomResourcesConverter.convertObjects(objectResource -> {
			OBJECTS.add(objectResource);
			OBJECTS_CACHE.put(objectResource.getId(), objectResource);
		}, CustomResourceLoader::readResource);

		VEHICLES.forEach((transportMode, vehicleResources) -> validateDataset("Vehicle", vehicleResources, VehicleResource::getId));
		validateDataset("Sign", SIGNS, signResource -> signResource.signId);
		validateDataset("Rail", RAILS, RailResource::getId);
		validateDataset("Object", OBJECTS, ObjectResource::getId);
		validateDataset("Lift", LIFTS, LiftResource::getId);

		MTR.LOGGER.info("Loaded {} vehicles and completed door movement validation in {} ms", VEHICLES.values().stream().mapToInt(ObjectArrayList::size).reduce(0, Integer::sum), TEST_DURATION / 1E6);
		MTR.LOGGER.info("Loaded {} signs", SIGNS.size());
		MTR.LOGGER.info("Loaded {} rails", RAILS.size());
		MTR.LOGGER.info("Loaded {} objects", OBJECTS.size());
		MTR.LOGGER.info("Loaded {} lifts", LIFTS.size());

		// Wait for any in-flight OBJ / Blockbench parses (submitted by VehicleResource /
		// RailResource / ObjectResource constructors above) to drain on the worker pool
		// before we start the preload pass. Without this, models whose parse is still
		// running would silently no-op out of {@code getCachedVehicleResource} and would
		// then incur the parse-and-upload cost on the next render frame — exactly the
		// first-encounter lag spike we are trying to remove. The 60 second cap is a
		// safety net for pathological packs; in normal use parsing completes in well
		// under one second after the synchronous registration loop above finishes.
		if (!ModelLoaderBase.awaitParsing(60_000L)) {
			MTR.LOGGER.warn("Timed out waiting for async model parsing to finish; preload may incur extra lag");
		}

		final long time1 = System.currentTimeMillis();

		// NOTE: the preload pass must run on the render thread because the inner build
		// step eventually calls VertexBuffer#createAndUpload (a GL call). The bulk of the
		// work (OBJ / Blockbench parsing) has already been awaited above and runs in
		// parallel on virtual threads; by the time we reach here every model is parsed
		// and the synchronous build below is just GPU upload.
		final int[] preloadedVehicleCount = {0};
		VEHICLES.forEach((transportMode, vehicleResources) -> vehicleResources.forEach(vehicleResource -> {
			if (vehicleResource.shouldPreload) {
				vehicleResource.getCachedVehicleResource(0, 0);
				preloadedVehicleCount[0]++;
			}
		}));

		final long time2 = System.currentTimeMillis();
		if (preloadedVehicleCount[0] > 0) {
			MTR.LOGGER.info("Preloaded {} vehicles in {} ms", preloadedVehicleCount[0], time2 - time1);
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
			MTR.LOGGER.info("Preloaded {} rails in {} ms", preloadedRailCount[0], time3 - time2);
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
			MTR.LOGGER.info("Preloaded {} objects in {} ms", preloadedObjectCount[0], time4 - time3);
		}
	}

	/**
	 * Visit every registered vehicle for the given transport mode.
	 */
	public static void iterateVehicles(TransportMode transportMode, Consumer<VehicleResource> consumer) {
		VEHICLES.get(transportMode).forEach(consumer);
	}

	/**
	 * Drop preview vehicles published by the resource-pack creator. Pass {@code ""} to
	 * clear every creator-registered vehicle; pass a specific id to clear just that one.
	 * Vehicles loaded from a resource pack are not affected.
	 */
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
	 * Resolve a vehicle by id within the given transport mode, invoking the callback only
	 * if a match exists. The callback receives a {@code (resource, fromResourcePackCreator)}
	 * pair so callers can distinguish creator previews from on-disk packs.
	 */
	public static void getVehicleById(TransportMode transportMode, String vehicleId, Consumer<ObjectBooleanImmutablePair<VehicleResource>> ifPresent) {
		final ObjectBooleanImmutablePair<VehicleResource> vehicleResourceDetails = VEHICLES_CACHE.get(transportMode).get(vehicleId);
		if (vehicleResourceDetails != null) {
			ifPresent.accept(vehicleResourceDetails);
		}
	}

	/**
	 * @return the tag-organised vehicle index for the given transport mode:
	 * {@code tagKey -> tagValue -> [vehicleId, ...]}. Used by the dashboard's tag
	 * filter.
	 */
	public static Object2ObjectAVLTreeMap<String, Object2ObjectAVLTreeMap<String, ObjectArrayList<String>>> getVehicleTags(TransportMode transportMode) {
		return VEHICLES_TAGS.get(transportMode);
	}

	@Nullable
	public static SignResource getSignById(@Nullable String signId) {
		return signId == null ? null : SIGNS_CACHE.get(signId);
	}

	public static ObjectArrayList<SignResource> getSortedSigns() {
		return SIGNS;
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

	public static ObjectImmutableList<LiftResource> getLifts() {
		return new ObjectImmutableList<>(LIFTS);
	}

	public static void getLiftById(String liftId, Consumer<LiftResource> ifPresent) {
		final LiftResource liftResource = LIFTS_CACHE.get(liftId);
		if (liftResource != null) {
			ifPresent.accept(liftResource);
		}
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

	/**
	 * Validate and report any abnormalities of the loaded resources (for example, duplicated ids)
	 */
	private static <T> void validateDataset(String dataSetName, ObjectArrayList<T> dataSet, Function<T, String> getId) {
		ObjectOpenHashSet<String> addedIds = new ObjectOpenHashSet<>();
		for (T data : dataSet) {
			String id = getId.apply(data);
			if (addedIds.contains(id)) {
				MTR.LOGGER.warn("Custom [{}] resource contains duplicate ID [{}]!", dataSetName, id);
			} else {
				addedIds.add(id);
			}
		}
	}

	private static String readResource(Identifier identifier) {
		final String identifierString = identifier.toString();
		// computeIfAbsent guarantees a single resource fetch per key even when multiple
		// worker threads request the same file concurrently (e.g. several vehicles sharing
		// an MTL or texture during async OBJ parsing).
		return RESOURCE_CACHE.computeIfAbsent(identifierString, key -> {
			if (Keys.DEBUG) {
				try (final InputStream inputStream = Files.newInputStream(MinecraftClient.getInstance().runDirectory.toPath().resolve("../src/main/resources/assets").resolve(identifier.getNamespace()).resolve(identifier.getPath()), StandardOpenOption.READ)) {
					return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
				} catch (Exception e) {
					MTR.LOGGER.error("Failed to read debug-mode resource [{}] from the development source tree", key, e);
					return "";
				}
			} else {
				return ResourceManagerHelper.readResource(identifier);
			}
		});
	}
}
