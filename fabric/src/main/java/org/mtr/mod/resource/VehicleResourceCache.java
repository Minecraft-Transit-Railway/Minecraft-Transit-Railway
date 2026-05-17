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
	public final VehicleGpuCache vehicleGpuCache;
	public final VehicleGpuCache bogie1GpuCache;
	public final VehicleGpuCache bogie2GpuCache;

	public VehicleResourceCache(
			ObjectImmutableList<Box> floors,
			ObjectImmutableList<Box> doorways,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsDoorsClosed,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie1,
			Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie2,
			VehicleGpuCache vehicleGpuCache,
			VehicleGpuCache bogie1GpuCache,
			VehicleGpuCache bogie2GpuCache
	) {
		this.floors = floors;
		this.doorways = doorways;
		this.optimizedModels = optimizedModels;
		this.optimizedModelsDoorsClosed = optimizedModelsDoorsClosed;
		this.optimizedModelsBogie1 = optimizedModelsBogie1;
		this.optimizedModelsBogie2 = optimizedModelsBogie2;
		this.vehicleGpuCache = vehicleGpuCache;
		this.bogie1GpuCache = bogie1GpuCache;
		this.bogie2GpuCache = bogie2GpuCache;
	}
}
