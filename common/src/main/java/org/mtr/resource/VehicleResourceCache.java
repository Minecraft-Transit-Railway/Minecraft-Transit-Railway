package org.mtr.resource;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.util.math.Box;
import org.mtr.model.NewOptimizedModel;

public record VehicleResourceCache(
		ObjectImmutableList<Box> floors,
		ObjectImmutableList<Box> doorways,
		Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> optimizedModels,
		Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> optimizedModelsDoorsClosed,
		Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> optimizedModelsBogie1,
		Object2ObjectOpenHashMap<PartCondition, Object2ObjectOpenHashMap<RenderStage, ObjectArrayList<NewOptimizedModel>>> optimizedModelsBogie2
) {
}
