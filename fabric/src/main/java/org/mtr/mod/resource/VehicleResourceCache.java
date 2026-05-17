package org.mtr.mod.resource;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.Box;

public final class VehicleResourceCache {

	public final ObjectImmutableList<Box> floors;
	public final ObjectImmutableList<Box> doorways;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsDoorsClosed;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie1;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie2;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModels;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsDoorsClosed;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsBogie1;
	public final Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsBogie2;
	public final CachedResource<VehicleGpuCache> vehicleGpuCache;
	public final CachedResource<VehicleGpuCache> bogie1GpuCache;
	public final CachedResource<VehicleGpuCache> bogie2GpuCache;

	public VehicleResourceCache(
			ObjectImmutableList<Box> floors,
			ObjectImmutableList<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsDoorsClosed,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie1,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie2,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModels,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsDoorsClosed,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsBogie1,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> fallbackOptimizedModelsBogie2,
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
