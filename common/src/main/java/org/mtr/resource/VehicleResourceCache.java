package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.util.math.Box;

public record VehicleResourceCache(
		ObjectImmutableList<Box> floors,
		ObjectImmutableList<Box> doorways,
		Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModels,
		Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsDoorsClosed,
		Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie1,
		Object2ObjectOpenHashMap<PartCondition, OptimizedModelWrapper> optimizedModelsBogie2
) {
}
