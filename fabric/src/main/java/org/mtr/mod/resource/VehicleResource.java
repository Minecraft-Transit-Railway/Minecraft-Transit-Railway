package org.mtr.mod.resource;

import org.mtr.core.data.Data;
import org.mtr.core.data.TransportMode;
import org.mtr.core.serializer.ReaderBase;
import org.mtr.core.tool.Utilities;
import org.joml.Matrix4f;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.Int2ObjectAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.Box;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.OptimizedRenderer;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.config.Config;
import org.mtr.mod.data.VehicleExtension;
import org.mtr.mod.generated.resource.VehicleResourceSchema;
import org.mtr.mod.render.DynamicVehicleModel;
import org.mtr.mod.render.GpuObjDebugStats;
import org.mtr.mod.render.GpuObjRenderer;
import org.mtr.mod.render.MainRenderer;
import org.mtr.mod.render.PositionAndRotation;
import org.mtr.mod.render.QueuedRenderLayer;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.mtr.mod.sound.BveVehicleSound;
import org.mtr.mod.sound.BveVehicleSoundConfig;
import org.mtr.mod.sound.LegacyVehicleSound;
import org.mtr.mod.sound.VehicleSoundBase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public final class VehicleResource extends VehicleResourceSchema {

	public final Supplier<VehicleSoundBase> createVehicleSoundBase;
	public final boolean shouldPreload;
	@Nullable
	private final LegacyVehicleSupplier<ObjectArrayList<VehicleModel>> extraModelsSupplier;
	private final Int2ObjectAVLTreeMap<Int2ObjectAVLTreeMap<ObjectArrayList<VehicleModel>>> allModels = new Int2ObjectAVLTreeMap<>();
	private final Int2ObjectAVLTreeMap<Int2ObjectAVLTreeMap<CachedResource<CachedResource<CachedResource<VehicleResourceCacheHolder>>>>> cachedVehicleResource = new Int2ObjectAVLTreeMap<>();

	private static final boolean[][] CHRISTMAS_LIGHT_STAGES = {
			{true, false, false, false},
			{false, true, false, false},
			{false, false, true, false},
			{false, false, false, true},
			{true, false, false, false},
			{false, true, false, false},
			{false, false, true, false},
			{false, false, false, true},

			{true, true, false, false},
			{false, true, true, false},
			{false, false, true, true},
			{true, false, false, true},
			{true, true, false, false},
			{false, true, true, false},
			{false, false, true, true},
			{true, false, false, true},

			{true, false, true, false},
			{false, true, false, true},
			{true, false, true, false},
			{false, true, false, true},
			{true, false, true, false},
			{false, true, false, true},
			{true, false, true, false},
			{false, true, false, true},

			{true, false, false, false},
			{true, true, false, false},
			{true, true, true, false},
			{true, true, true, true},
			{false, true, false, false},
			{false, true, true, false},
			{false, true, true, true},
			{true, true, true, true},
			{false, false, true, false},
			{false, false, true, true},
			{true, false, true, true},
			{true, true, true, true},
			{false, false, false, true},
			{true, false, false, true},
			{true, true, false, true},
			{true, true, true, true},

			{false, false, false, false},
			{true, true, true, true},
			{true, true, true, true},
			{true, true, true, true},
			{false, false, false, false},
			{true, true, true, true},
			{true, true, true, true},
			{true, true, true, true},
	};

	public VehicleResource(ReaderBase readerBase, @Nullable LegacyVehicleSupplier<ObjectArrayList<VehicleModel>> extraModelsSupplier, ResourceProvider resourceProvider) {
		super(readerBase, resourceProvider);
		updateData(readerBase);
		this.extraModelsSupplier = extraModelsSupplier;
		createVehicleSoundBase = createVehicleSoundBaseInitializer();
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		if (shouldPreload) {
			models.forEach(model -> model.shouldPreload = true);
		}
	}

	public VehicleResource(ReaderBase readerBase, ResourceProvider resourceProvider) {
		this(readerBase, null, resourceProvider);
	}

	VehicleResource(
			String id,
			String name,
			String color,
			TransportMode transportMode,
			double length,
			double width,
			double bogie1Position,
			double bogie2Position,
			double couplingPadding1,
			double couplingPadding2,
			String description,
			String wikipediaArticle,
			ObjectArrayList<String> tags,
			ObjectArrayList<VehicleModel> models,
			ObjectArrayList<VehicleModel> bogie1Models,
			ObjectArrayList<VehicleModel> bogie2Models,
			boolean hasGangway1,
			boolean hasGangway2,
			boolean hasBarrier1,
			boolean hasBarrier2,
			double legacyRiderOffset,
			String bveSoundBaseResource,
			String legacySpeedSoundBaseResource,
			long legacySpeedSoundCount,
			boolean legacyUseAccelerationSoundsWhenCoasting,
			boolean legacyConstantPlaybackSpeed,
			String legacyDoorSoundBaseResource,
			double legacyDoorCloseSoundTime,
			ResourceProvider resourceProvider
	) {
		super(
				id,
				name,
				color,
				transportMode,
				length,
				width,
				bogie1Position,
				bogie2Position,
				couplingPadding1,
				couplingPadding2,
				description,
				wikipediaArticle,
				hasGangway1,
				hasGangway2,
				hasBarrier1,
				hasBarrier2,
				legacyRiderOffset,
				bveSoundBaseResource,
				legacySpeedSoundBaseResource,
				legacySpeedSoundCount,
				legacyUseAccelerationSoundsWhenCoasting,
				legacyConstantPlaybackSpeed,
				legacyDoorSoundBaseResource,
				legacyDoorCloseSoundTime,
				resourceProvider
		);
		this.tags.addAll(tags);
		this.models.addAll(models);
		this.bogie1Models.addAll(bogie1Models);
		this.bogie2Models.addAll(bogie2Models);
		this.extraModelsSupplier = null;
		createVehicleSoundBase = createVehicleSoundBaseInitializer();
		shouldPreload = Config.getClient().matchesPreloadResourcePattern(id);
		if (shouldPreload) {
			models.forEach(model -> model.shouldPreload = true);
		}
	}

	@Nonnull
	@Override
	protected ResourceProvider modelsResourceProviderParameter() {
		return resourceProvider;
	}

	@Nonnull
	@Override
	protected ResourceProvider bogie1ModelsResourceProviderParameter() {
		return resourceProvider;
	}

	@Nonnull
	@Override
	protected ResourceProvider bogie2ModelsResourceProviderParameter() {
		return resourceProvider;
	}

	@Nullable
	public VehicleResourceCache getCachedVehicleResource(int carNumber, int totalCars, boolean force) {
		final int newCarNumber = extraModelsSupplier == null ? 0 : carNumber;
		final int newTotalCars = extraModelsSupplier == null ? 0 : totalCars;
		final CachedResource<CachedResource<VehicleResourceCacheHolder>> data1 = cachedVehicleResource.computeIfAbsent(newCarNumber, key -> new Int2ObjectAVLTreeMap<>()).computeIfAbsent(newTotalCars, key -> cachedVehicleResourceInitializer(newCarNumber, newTotalCars, force)).getData(force);
		if (data1 != null) {
			final CachedResource<VehicleResourceCacheHolder> data2 = data1.getData(force);
			if (data2 != null) {
				final VehicleResourceCacheHolder data3 = data2.getData(force);
				if (data3 != null) {
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels = data3.optimizedModels.getData(force);
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsDoorsClosed = data3.optimizedModelsDoorsClosed.getData(force);
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie1 = data3.optimizedModelsBogie1.getData(force);
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie2 = data3.optimizedModelsBogie2.getData(force);
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModels = data3.fallbackOptimizedModels.getData(force);
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsDoorsClosed = data3.fallbackOptimizedModelsDoorsClosed.getData(force);
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsBogie1 = data3.fallbackOptimizedModelsBogie1.getData(force);
					final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsBogie2 = data3.fallbackOptimizedModelsBogie2.getData(force);
					if (optimizedModels != null && optimizedModelsDoorsClosed != null && optimizedModelsBogie1 != null && optimizedModelsBogie2 != null && fallbackOptimizedModels != null && fallbackOptimizedModelsDoorsClosed != null && fallbackOptimizedModelsBogie1 != null && fallbackOptimizedModelsBogie2 != null) {
						return new VehicleResourceCache(data3.floors, data3.doorways, optimizedModels, optimizedModelsDoorsClosed, optimizedModelsBogie1, optimizedModelsBogie2, fallbackOptimizedModels, fallbackOptimizedModelsDoorsClosed, fallbackOptimizedModelsBogie1, fallbackOptimizedModelsBogie2, data3.vehicleGpuCache, data3.bogie1GpuCache, data3.bogie2GpuCache);
					}
				}
			}
		}
		return null;
	}

	public void queue(StoredMatrixTransformations storedMatrixTransformations, boolean useDefaultOffset, PositionAndRotation positionAndRotation, double oscillationAmount, VehicleExtension vehicle, int carNumber, int totalCars, int light, boolean noOpenDoorways) {
		final VehicleResourceCache vehicleResourceCache = getCachedVehicleResource(carNumber, totalCars, false);
		if (vehicleResourceCache != null) {
			final boolean instancingEnabled = Config.getClient().getEnableGpuObjInstancing();
			final boolean optimizedRenderingAvailable = OptimizedRenderer.hasOptimizedRendering();
			final VehicleGpuCache vehicleGpuCache = vehicleResourceCache.vehicleGpuCache.getData(false);
			final boolean gpuQueued = instancingEnabled && optimizedRenderingAvailable && vehicleGpuCache != null && vehicleGpuCache.hasParts && queueGpu(vehicleGpuCache, useDefaultOffset, positionAndRotation, oscillationAmount, vehicle, light, noOpenDoorways);
			if (gpuQueued) {
				recordVehicleCoverage(vehicleGpuCache, vehicle, noOpenDoorways, null, 0);
				if (noOpenDoorways) {
					queueIfPresent(vehicleResourceCache.fallbackOptimizedModelsDoorsClosed, storedMatrixTransformations, vehicle, light, true);
				} else {
					queueIfPresent(vehicleResourceCache.fallbackOptimizedModels, storedMatrixTransformations, vehicle, light, false);
				}
			} else {
				final GpuObjDebugStats.VehicleFallbackReason supportedFallbackReason = !instancingEnabled ? GpuObjDebugStats.VehicleFallbackReason.CONFIG_DISABLED : !optimizedRenderingAvailable ? GpuObjDebugStats.VehicleFallbackReason.OPTIMIZED_RENDERING_UNAVAILABLE : GpuObjDebugStats.VehicleFallbackReason.GPU_CACHE_UNAVAILABLE;
				final int approximateFallbackCount = noOpenDoorways ? countRenderableModels(vehicleResourceCache.optimizedModelsDoorsClosed, vehicle, true) : countRenderableModels(vehicleResourceCache.optimizedModels, vehicle, false);
				recordVehicleCoverage(vehicleGpuCache, vehicle, noOpenDoorways, supportedFallbackReason, approximateFallbackCount);
				if (noOpenDoorways) {
					queueIfPresent(vehicleResourceCache.optimizedModelsDoorsClosed, storedMatrixTransformations, vehicle, light, true);
				} else {
					queueIfPresent(vehicleResourceCache.optimizedModels, storedMatrixTransformations, vehicle, light, false);
				}
			}
		}
	}

	public void queueBogie(int bogieIndex, Supplier<StoredMatrixTransformations> storedMatrixTransformationsSupplier, boolean useDefaultOffset, PositionAndRotation positionAndRotation, VehicleExtension vehicle, int light) {
		final VehicleResourceCache vehicleResourceCache = getCachedVehicleResource(0, 1, false);
		if (vehicleResourceCache != null && Utilities.isBetween(bogieIndex, 0, 1)) {
			final boolean instancingEnabled = Config.getClient().getEnableGpuObjInstancing();
			final boolean optimizedRenderingAvailable = OptimizedRenderer.hasOptimizedRendering();
			final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels = bogieIndex == 0 ? vehicleResourceCache.optimizedModelsBogie1 : vehicleResourceCache.optimizedModelsBogie2;
			final VehicleGpuCache bogieGpuCache = (bogieIndex == 0 ? vehicleResourceCache.bogie1GpuCache : vehicleResourceCache.bogie2GpuCache).getData(false);
			final boolean gpuQueued = instancingEnabled && optimizedRenderingAvailable && bogieGpuCache != null && bogieGpuCache.hasParts && queueGpu(bogieGpuCache, useDefaultOffset, positionAndRotation, 0, vehicle, light, true);
			if (gpuQueued) {
				recordVehicleCoverage(bogieGpuCache, vehicle, true, null, 0);
				queueIfPresent(bogieIndex == 0 ? vehicleResourceCache.fallbackOptimizedModelsBogie1 : vehicleResourceCache.fallbackOptimizedModelsBogie2, storedMatrixTransformationsSupplier, vehicle, light, true);
			} else {
				final GpuObjDebugStats.VehicleFallbackReason supportedFallbackReason = !instancingEnabled ? GpuObjDebugStats.VehicleFallbackReason.CONFIG_DISABLED : !optimizedRenderingAvailable ? GpuObjDebugStats.VehicleFallbackReason.OPTIMIZED_RENDERING_UNAVAILABLE : GpuObjDebugStats.VehicleFallbackReason.GPU_CACHE_UNAVAILABLE;
				recordVehicleCoverage(bogieGpuCache, vehicle, true, supportedFallbackReason, countRenderableModels(optimizedModels, vehicle, true));
				queueIfPresent(optimizedModels, storedMatrixTransformationsSupplier, vehicle, light, true);
			}
		}
	}

	public String getId() {
		return id;
	}

	public MutableText getName() {
		return TextHelper.translatable(name);
	}

	public int getColor() {
		return CustomResourceTools.colorStringToInt(color);
	}

	public TransportMode getTransportMode() {
		return transportMode;
	}

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public double getBogie1Position() {
		return bogie1Position;
	}

	public double getBogie2Position() {
		return bogie2Position;
	}

	public double getCouplingPadding1() {
		return couplingPadding1;
	}

	public double getCouplingPadding2() {
		return couplingPadding2;
	}

	public MutableText getDescription() {
		return TextHelper.translatable(description);
	}

	public String getWikipediaArticle() {
		return wikipediaArticle;
	}

	public void iterateModels(int carNumber, int totalCars, ModelConsumer modelConsumer) {
		iterateModels(getAllModels(carNumber, totalCars), modelConsumer);
	}

	public void iterateBogieModels(int bogieIndex, ModelConsumer modelConsumer) {
		if (Utilities.isBetween(bogieIndex, 0, 1)) {
			iterateModels(bogieIndex == 0 ? bogie1Models : bogie2Models, modelConsumer);
		}
	}

	public VehicleResourceWrapper toVehicleResourceWrapper() {
		final int carNumber = id.endsWith("trailer") ? 1 : id.endsWith("cab_2") ? 2 : 0;
		final int totalCars = id.endsWith("cab_3") ? 1 : 3;
		getCachedVehicleResource(carNumber, totalCars, true);
		return new VehicleResourceWrapper(
				id,
				name,
				color,
				transportMode,
				length,
				width,
				bogie1Position,
				bogie2Position,
				couplingPadding1,
				couplingPadding2,
				description,
				wikipediaArticle,
				tags,
				getAllModels(carNumber, totalCars).stream().map(VehicleModel::toVehicleModelWrapper).collect(Collectors.toCollection(ObjectArrayList::new)),
				bogie1Models.stream().map(VehicleModel::toVehicleModelWrapper).collect(Collectors.toCollection(ObjectArrayList::new)),
				bogie2Models.stream().map(VehicleModel::toVehicleModelWrapper).collect(Collectors.toCollection(ObjectArrayList::new)),
				hasGangway1,
				hasGangway2,
				hasBarrier1,
				hasBarrier2,
				legacyRiderOffset,
				bveSoundBaseResource,
				legacySpeedSoundBaseResource,
				legacySpeedSoundCount,
				legacyUseAccelerationSoundsWhenCoasting,
				legacyConstantPlaybackSpeed,
				legacyDoorSoundBaseResource,
				legacyDoorCloseSoundTime
		);
	}

	public void writeMinecraftResource(ObjectArraySet<MinecraftModelResource> minecraftModelResources, ObjectArraySet<String> minecraftTextureResources) {
		models.forEach(vehicleModel -> {
			minecraftModelResources.add(vehicleModel.getAsMinecraftResource());
			vehicleModel.addToTextureResource(minecraftTextureResources);
		});
		bogie1Models.forEach(vehicleModel -> {
			minecraftModelResources.add(vehicleModel.getAsMinecraftResource());
			vehicleModel.addToTextureResource(minecraftTextureResources);
		});
		bogie2Models.forEach(vehicleModel -> {
			minecraftModelResources.add(vehicleModel.getAsMinecraftResource());
			vehicleModel.addToTextureResource(minecraftTextureResources);
		});
	}

	public boolean hasGangway1() {
		return hasGangway1;
	}

	public boolean hasGangway2() {
		return hasGangway2;
	}

	public boolean hasBarrier1() {
		return hasBarrier1;
	}

	public boolean hasBarrier2() {
		return hasBarrier2;
	}

	private ObjectArrayList<VehicleModel> getAllModels(int carNumber, int totalCars) {
		if (extraModelsSupplier == null) {
			return models;
		} else {
			return allModels.getOrDefault(carNumber, new Int2ObjectAVLTreeMap<>()).getOrDefault(totalCars, new ObjectArrayList<>());
		}
	}

	public static boolean matchesCondition(VehicleExtension vehicle, PartCondition partCondition, boolean noOpenDoorways) {
		switch (partCondition) {
			case AT_DEPOT:
				return !vehicle.getIsOnRoute();
			case ON_ROUTE_FORWARDS:
				return vehicle.getIsOnRoute() && !vehicle.getReversed();
			case ON_ROUTE_BACKWARDS:
				return vehicle.getIsOnRoute() && vehicle.getReversed();
			case DOORS_CLOSED:
				return vehicle.persistentVehicleData.getDoorValue() == 0 && noOpenDoorways;
			case DOORS_OPENED:
				return vehicle.persistentVehicleData.getDoorValue() > 0 || !noOpenDoorways;
			default:
				return getChristmasLightState(partCondition);
		}
	}

	public void collectTags(Object2ObjectAVLTreeMap<String, Object2ObjectAVLTreeMap<String, ObjectArrayList<String>>> tagMap) {
		tags.forEach(tag -> {
			final String[] tagSplit = tag.split(":");
			if (tagSplit.length == 2) {
				tagMap.computeIfAbsent(tagSplit[0], key -> new Object2ObjectAVLTreeMap<>()).computeIfAbsent(tagSplit[1], key -> new ObjectArrayList<>()).add(id);
			}
		});
	}

	private CachedResource<CachedResource<CachedResource<VehicleResourceCacheHolder>>> cachedVehicleResourceInitializer(int carNumber, int totalCars, boolean force) {
		final int modelLifespan = shouldPreload ? Integer.MAX_VALUE : VehicleModel.MODEL_LIFESPAN;
		return new CachedResource<>(() -> {
			final ObjectArrayList<VehicleModel> allModelsList = allModels.computeIfAbsent(carNumber, key -> new Int2ObjectAVLTreeMap<>()).computeIfAbsent(totalCars, key -> new ObjectArrayList<>());
			allModelsList.clear();
			allModelsList.addAll(models);

			if (extraModelsSupplier != null) {
				final long startMillis = System.currentTimeMillis();
				allModelsList.addAll(extraModelsSupplier.apply(carNumber, totalCars));
				final long endMillis = System.currentTimeMillis();
				if (endMillis - startMillis >= 100) {
					Init.LOGGER.warn("[{}] Model loading took {} ms, which is longer than usual!", id, endMillis - startMillis);
				}
			}

			final ObjectArrayList<VehicleModel> modelsToInitialize = new ObjectArrayList<>();
			modelsToInitialize.addAll(allModelsList);
			modelsToInitialize.addAll(bogie1Models);
			modelsToInitialize.addAll(bogie2Models);

			return new CachedResource<>(() -> {
				for (final VehicleModel vehicleModel : modelsToInitialize) {
					final DynamicVehicleModel dynamicVehicleModel = vehicleModel.cachedModel.getData(force);
					if (dynamicVehicleModel == null) {
						return null;
					}
				}

				final ObjectArrayList<Box> floors = new ObjectArrayList<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsModel = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsModelDoorsClosed = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsBogie1Model = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsBogie2Model = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsModel = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsModelDoorsClosed = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsBogie1Model = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsBogie2Model = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> fallbackObjModelsModel = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> fallbackObjModelsModelDoorsClosed = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> fallbackObjModelsBogie1Model = new Object2ObjectOpenHashMap<>();
				final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> fallbackObjModelsBogie2Model = new Object2ObjectOpenHashMap<>();
				final ObjectArrayList<Box> doorways = new ObjectArrayList<>();
				forEachNonNull(allModelsList, dynamicVehicleModel -> dynamicVehicleModel.writeFloorsAndDoorways(floors, doorways, materialGroupsModel, materialGroupsModelDoorsClosed, objModelsModel, objModelsModelDoorsClosed, fallbackObjModelsModel, fallbackObjModelsModelDoorsClosed), force);

				if (floors.isEmpty() && doorways.isEmpty()) {
					Init.LOGGER.info("[{}] No floors or doorways found in vehicle models", id);
					final double x1 = width / 2 + 0.25;
					final double x2 = width / 2 + 0.5;
					final double y = 1 + legacyRiderOffset;
					final double z = length / 2 - 0.5;
					floors.add(new Box(-x1, y, -z, x1, y, z));
					for (double j = -z; j <= z + 0.001; j++) {
						doorways.add(new Box(-x1, y, j, -x2, y, j + 1));
						doorways.add(new Box(x1, y, j, x2, y, j + 1));
					}
				}

				forEachNonNull(allModelsList, dynamicVehicleModel -> dynamicVehicleModel.modelProperties.iterateParts(modelPropertiesPart -> modelPropertiesPart.mapDoors(doorways)), force);
				forEachNonNull(bogie1Models, dynamicVehicleModel -> dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArrayList<>(), new ObjectArrayList<>(), new Object2ObjectOpenHashMap<>(), materialGroupsBogie1Model, new Object2ObjectOpenHashMap<>(), objModelsBogie1Model, fallbackObjModelsBogie1Model, new Object2ObjectOpenHashMap<>()), force);
				forEachNonNull(bogie2Models, dynamicVehicleModel -> dynamicVehicleModel.writeFloorsAndDoorways(new ObjectArrayList<>(), new ObjectArrayList<>(), new Object2ObjectOpenHashMap<>(), materialGroupsBogie2Model, new Object2ObjectOpenHashMap<>(), objModelsBogie2Model, fallbackObjModelsBogie2Model, new Object2ObjectOpenHashMap<>()), force);

				return new CachedResource<>(() -> {
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModels = writeToOptimizedModels(materialGroupsModel, objModelsModel, modelLifespan);
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsDoorsClosed = writeToOptimizedModels(materialGroupsModelDoorsClosed, objModelsModelDoorsClosed, modelLifespan);
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsBogie1 = writeToOptimizedModels(materialGroupsBogie1Model, objModelsBogie1Model, modelLifespan);
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsBogie2 = writeToOptimizedModels(materialGroupsBogie2Model, objModelsBogie2Model, modelLifespan);
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModels = writeToOptimizedModels(materialGroupsModel, fallbackObjModelsModel, modelLifespan);
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsDoorsClosed = writeToOptimizedModels(materialGroupsModelDoorsClosed, fallbackObjModelsModelDoorsClosed, modelLifespan);
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsBogie1 = writeToOptimizedModels(materialGroupsBogie1Model, fallbackObjModelsBogie1Model, modelLifespan);
					final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsBogie2 = writeToOptimizedModels(materialGroupsBogie2Model, fallbackObjModelsBogie2Model, modelLifespan);
					return new VehicleResourceCacheHolder(new ObjectImmutableList<>(floors), new ObjectImmutableList<>(doorways), optimizedModels, optimizedModelsDoorsClosed, optimizedModelsBogie1, optimizedModelsBogie2, fallbackOptimizedModels, fallbackOptimizedModelsDoorsClosed, fallbackOptimizedModelsBogie1, fallbackOptimizedModelsBogie2, writeToGpuCache(allModelsList, modelLifespan), writeToGpuCache(bogie1Models, modelLifespan), writeToGpuCache(bogie2Models, modelLifespan));
				}, modelLifespan);
			}, modelLifespan);
		}, modelLifespan);
	}

	private Supplier<VehicleSoundBase> createVehicleSoundBaseInitializer() {
		if (bveSoundBaseResource.isEmpty()) {
			final LegacyVehicleSound legacyVehicleSound = new LegacyVehicleSound(
					legacySpeedSoundBaseResource,
					(int) legacySpeedSoundCount,
					legacyUseAccelerationSoundsWhenCoasting,
					legacyConstantPlaybackSpeed,
					legacyDoorSoundBaseResource,
					legacyDoorCloseSoundTime
			);
			return () -> legacyVehicleSound;
		} else {
			final BveVehicleSoundConfig bveVehicleSoundConfig = new BveVehicleSoundConfig(bveSoundBaseResource);
			return () -> new BveVehicleSound(bveVehicleSoundConfig);
		}
	}

	private static void iterateModels(ObjectArrayList<VehicleModel> models, ModelConsumer modelConsumer) {
		for (int i = 0; i < models.size(); i++) {
			final VehicleModel vehicleModel = models.get(i);
			if (vehicleModel != null) {
				final DynamicVehicleModel dynamicVehicleModel = vehicleModel.cachedModel.getData(false);
				if (dynamicVehicleModel != null) {
					modelConsumer.accept(i, dynamicVehicleModel);
				}
			}
		}
	}

	private static boolean getChristmasLightState(PartCondition partCondition) {
		final int index;
		switch (partCondition) {
			case CHRISTMAS_LIGHT_RED:
				index = 0;
				break;
			case CHRISTMAS_LIGHT_YELLOW:
				index = 1;
				break;
			case CHRISTMAS_LIGHT_GREEN:
				index = 2;
				break;
			case CHRISTMAS_LIGHT_BLUE:
				index = 3;
				break;
			default:
				return true;
		}
		return CHRISTMAS_LIGHT_STAGES[(int) ((System.currentTimeMillis() / 500) % CHRISTMAS_LIGHT_STAGES.length)][index];
	}

	private static void queue(Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, boolean noOpenDoorways) {
		optimizedModels.forEach((partCondition, optimizedModel) -> {
			if (matchesCondition(vehicle, partCondition, noOpenDoorways)) {
				MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
					storedMatrixTransformations.transform(graphicsHolder, offset);
					CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(optimizedModel, graphicsHolder, light);
					graphicsHolder.pop();
				});
			}
		});
	}

	private static void queueIfPresent(Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels, StoredMatrixTransformations storedMatrixTransformations, VehicleExtension vehicle, int light, boolean noOpenDoorways) {
		if (countRenderableModels(optimizedModels, vehicle, noOpenDoorways) > 0) {
			queue(optimizedModels, storedMatrixTransformations, vehicle, light, noOpenDoorways);
		}
	}

	private static void queueIfPresent(Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels, Supplier<StoredMatrixTransformations> storedMatrixTransformationsSupplier, VehicleExtension vehicle, int light, boolean noOpenDoorways) {
		if (countRenderableModels(optimizedModels, vehicle, noOpenDoorways) > 0) {
			queue(optimizedModels, storedMatrixTransformationsSupplier.get(), vehicle, light, noOpenDoorways);
		}
	}

	private static boolean queueGpu(@Nullable VehicleGpuCache vehicleGpuCache, boolean useDefaultOffset, PositionAndRotation positionAndRotation, double oscillationAmount, VehicleExtension vehicle, int light, boolean noOpenDoorways) {
		if (vehicleGpuCache == null) {
			return false;
		}

		final Matrix4f worldMatrix = createWorldMatrix(positionAndRotation, oscillationAmount);
		final Matrix4f finalMatrix = new Matrix4f();
		final int packedLight = org.mtr.mapping.render.tool.Utilities.exchangeLightmapUVBits(light);
		boolean queuedAny = false;
		long eligiblePartCount = 0;

		for (final VehicleGpuCache.ConditionBucket conditionBucket : vehicleGpuCache.conditionBuckets) {
			if (!matchesCondition(vehicle, conditionBucket.condition, noOpenDoorways)) {
				continue;
			}

			eligiblePartCount += conditionBucket.supportedPlacementCount;
			if (!conditionBucket.parts.isEmpty()) {
				queuedAny = true;
				conditionBucket.parts.forEach(part -> GpuObjRenderer.INSTANCE.queue(part.batchKey, part.materialProperties, part.mesh, finalMatrix.set(worldMatrix).mul(part.localTransform), packedLight, 0xFFFFFFFF, useDefaultOffset, GpuObjDebugStats.Source.VEHICLE));
			}
		}

		GpuObjDebugStats.recordVehicleEligibleParts(eligiblePartCount);
		if (eligiblePartCount > 0) {
			GpuObjDebugStats.recordVehicleGpuQueueCall();
		}
		GpuObjDebugStats.recordVehicleQueuedParts(eligiblePartCount);
		return queuedAny;
	}

	private static void recordVehicleCoverage(@Nullable VehicleGpuCache vehicleGpuCache, VehicleExtension vehicle, boolean noOpenDoorways, @Nullable GpuObjDebugStats.VehicleFallbackReason supportedFallbackReason, long approximateFallbackCountIfCacheUnavailable) {
		if (vehicleGpuCache == null) {
			if (supportedFallbackReason != null && approximateFallbackCountIfCacheUnavailable > 0) {
				GpuObjDebugStats.recordVehicleFallbackParts(supportedFallbackReason, approximateFallbackCountIfCacheUnavailable);
			}
			return;
		}

		for (final VehicleGpuCache.ConditionBucket conditionBucket : vehicleGpuCache.conditionBuckets) {
			final long totalBucketParts = conditionBucket.supportedPlacementCount + conditionBucket.getTotalUnsupportedPlacementCount();
			if (!matchesCondition(vehicle, conditionBucket.condition, noOpenDoorways)) {
				GpuObjDebugStats.recordVehicleConditionFilteredParts(totalBucketParts);
				continue;
			}

			if (supportedFallbackReason != null && conditionBucket.supportedPlacementCount > 0) {
				GpuObjDebugStats.recordVehicleEligibleParts(conditionBucket.supportedPlacementCount);
			}

			if (supportedFallbackReason != null && conditionBucket.supportedPlacementCount > 0) {
				GpuObjDebugStats.recordVehicleFallbackParts(supportedFallbackReason, conditionBucket.supportedPlacementCount);
			}

			for (final GpuObjDebugStats.VehicleFallbackReason reason : GpuObjDebugStats.VehicleFallbackReason.values()) {
				GpuObjDebugStats.recordVehicleFallbackParts(reason, conditionBucket.getUnsupportedPlacementCount(reason));
			}
		}
	}

	private static Matrix4f createWorldMatrix(PositionAndRotation positionAndRotation, double oscillationAmount) {
		return new Matrix4f()
				.translate((float) positionAndRotation.position.x, (float) positionAndRotation.position.y, (float) positionAndRotation.position.z)
				.rotateY((float) (positionAndRotation.yaw + Math.PI))
				.rotateX((float) (positionAndRotation.pitch + Math.PI))
				.rotateZ((float) Math.toRadians(oscillationAmount));
	}

	private static int countRenderableModels(Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels, VehicleExtension vehicle, boolean noOpenDoorways) {
		int count = 0;
		for (final Object2ObjectMap.Entry<PartCondition, OptimizedModelWrapper> entry : optimizedModels.object2ObjectEntrySet()) {
			if (entry.getValue().optimizedModel != null && matchesCondition(vehicle, entry.getKey(), noOpenDoorways)) {
				count++;
			}
		}
		return count;
	}

	private static CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> writeToOptimizedModels(
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper>> materialGroupsModel,
			Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper>> objModelsModel,
			int modelLifespan
	) {
		return new CachedResource<>(() -> {
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.beginReload();
			final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels = new Object2ObjectOpenHashMap<>();

			for (final PartCondition partCondition : PartCondition.values()) {
				final OptimizedModelWrapper optimizedModel1;
				final ObjectArrayList<OptimizedModelWrapper.MaterialGroupWrapper> materialGroups = materialGroupsModel.get(partCondition);
				optimizedModel1 = materialGroups == null ? null : OptimizedModelWrapper.fromMaterialGroups(materialGroups);

				final OptimizedModelWrapper optimizedModel2;
				final ObjectArrayList<OptimizedModelWrapper.ObjModelWrapper> objModels = objModelsModel.get(partCondition);
				optimizedModel2 = objModels == null ? null : OptimizedModelWrapper.fromObjModels(objModels);
				optimizedModels.put(partCondition, new OptimizedModelWrapper(optimizedModel1, optimizedModel2));
			}

			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.finishReload();
			return optimizedModels;
		}, modelLifespan);
	}

	private static CachedResource<VehicleGpuCache> writeToGpuCache(ObjectArrayList<VehicleModel> models, int modelLifespan) {
		return new CachedResource<>(() -> {
			final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<VehicleGpuCache.Part>> gpuPartsForCondition = new Object2ObjectOpenHashMap<>();
			final Object2ObjectOpenHashMap<PartCondition, VehicleGpuCache.PlacementStats> placementStatsByCondition = new Object2ObjectOpenHashMap<>();
			models.forEach(vehicleModel -> {
				final VehicleGpuCache vehicleGpuCache = vehicleModel.cachedGpuCache.getData(true);
				if (vehicleGpuCache != null) {
					vehicleGpuCache.conditionBuckets.forEach(conditionBucket -> {
						if (!conditionBucket.parts.isEmpty()) {
							Data.put(gpuPartsForCondition, conditionBucket.condition, conditionBucket.parts, ObjectArrayList::new);
						}
						final VehicleGpuCache.PlacementStats placementStats = VehicleGpuCache.getOrCreatePlacementStats(placementStatsByCondition, conditionBucket.condition);
						placementStats.addSupportedPlacementCount(conditionBucket.supportedPlacementCount);
						for (final GpuObjDebugStats.VehicleFallbackReason reason : GpuObjDebugStats.VehicleFallbackReason.values()) {
							placementStats.addUnsupportedReasonCount(reason, conditionBucket.getUnsupportedPlacementCount(reason));
						}
					});
				}
			});
			return new VehicleGpuCache(gpuPartsForCondition, placementStatsByCondition);
		}, modelLifespan);
	}

	private static void forEachNonNull(ObjectArrayList<VehicleModel> models, Consumer<DynamicVehicleModel> consumer, boolean force) {
		models.forEach(vehicleModel -> {
			final DynamicVehicleModel dynamicVehicleModel = vehicleModel.cachedModel.getData(force);
			if (dynamicVehicleModel != null) {
				consumer.accept(dynamicVehicleModel);
			}
		});
	}

	private static class VehicleResourceCacheHolder {

		private final ObjectImmutableList<Box> floors;
		private final ObjectImmutableList<Box> doorways;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModels;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsDoorsClosed;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsBogie1;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsBogie2;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModels;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsDoorsClosed;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsBogie1;
		private final CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsBogie2;
		private final CachedResource<VehicleGpuCache> vehicleGpuCache;
		private final CachedResource<VehicleGpuCache> bogie1GpuCache;
		private final CachedResource<VehicleGpuCache> bogie2GpuCache;

		private VehicleResourceCacheHolder(
				ObjectImmutableList<Box> floors, ObjectImmutableList<Box> doorways,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModels,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsDoorsClosed,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsBogie1,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> optimizedModelsBogie2,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModels,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsDoorsClosed,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsBogie1,
				CachedResource<Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper>> fallbackOptimizedModelsBogie2,
				CachedResource<VehicleGpuCache> vehicleGpuCache,
				CachedResource<VehicleGpuCache> bogie1GpuCache,
				CachedResource<VehicleGpuCache> bogie2GpuCache
		) {
			this.floors = floors;
			this.doorways = doorways;
			this.optimizedModels = optimizedModels;
			this.optimizedModelsDoorsClosed = optimizedModelsDoorsClosed;
			this.optimizedModelsBogie1 = optimizedModelsBogie1;
			this.optimizedModelsBogie2 = optimizedModelsBogie2;
			this.fallbackOptimizedModels = fallbackOptimizedModels;
			this.fallbackOptimizedModelsDoorsClosed = fallbackOptimizedModelsDoorsClosed;
			this.fallbackOptimizedModelsBogie1 = fallbackOptimizedModelsBogie1;
			this.fallbackOptimizedModelsBogie2 = fallbackOptimizedModelsBogie2;
			this.vehicleGpuCache = vehicleGpuCache;
			this.bogie1GpuCache = bogie1GpuCache;
			this.bogie2GpuCache = bogie2GpuCache;
		}
	}

	@FunctionalInterface
	public interface ModelConsumer {
		void accept(int index, DynamicVehicleModel dynamicVehicleModel);
	}

	@FunctionalInterface
	public interface LegacyVehicleSupplier<T> {
		T apply(int carNumber, int totalCars);
	}
}
