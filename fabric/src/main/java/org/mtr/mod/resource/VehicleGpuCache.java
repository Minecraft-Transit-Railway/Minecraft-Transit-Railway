package org.mtr.mod.resource;

import org.joml.Matrix4f;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.render.GpuObjDebugStats;
import org.mtr.mod.render.ObjBatchKey;
import org.mtr.mod.render.StoredMatrixTransformations;
import org.mtr.mod.render.StaticObjMesh;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.Supplier;

public final class VehicleGpuCache {

	public static final VehicleGpuCache EMPTY = new VehicleGpuCache(new Object2ObjectOpenHashMap<>(), new Object2ObjectOpenHashMap<>());
	public final Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<Part>> partsByCondition;
	public final ObjectArrayList<ConditionBucket> conditionBuckets = new ObjectArrayList<>();
	public final boolean hasParts;

	public VehicleGpuCache(Object2ObjectOpenHashMap<PartCondition, ObjectArrayList<Part>> partsByCondition, Object2ObjectOpenHashMap<PartCondition, PlacementStats> placementStatsByCondition) {
		this.partsByCondition = partsByCondition;
		final ObjectArraySet<PartCondition> partConditions = new ObjectArraySet<>();
		partConditions.addAll(partsByCondition.keySet());
		partConditions.addAll(placementStatsByCondition.keySet());
		partConditions.forEach(partCondition -> {
			final ObjectArrayList<Part> parts = partsByCondition.getOrDefault(partCondition, new ObjectArrayList<>());
			final PlacementStats placementStats = placementStatsByCondition.get(partCondition);
			conditionBuckets.add(new ConditionBucket(partCondition, parts, placementStats == null ? 0 : placementStats.supportedPlacementCount, placementStats == null ? new long[GpuObjDebugStats.VehicleFallbackReason.values().length] : placementStats.copyUnsupportedReasonCounts()));
		});
		hasParts = conditionBuckets.stream().anyMatch(conditionBucket -> !conditionBucket.parts.isEmpty());
	}

	public static PlacementStats getOrCreatePlacementStats(Object2ObjectOpenHashMap<PartCondition, PlacementStats> placementStatsByCondition, PartCondition condition) {
		return placementStatsByCondition.computeIfAbsent(condition, key -> new PlacementStats());
	}

	public static final class PlacementStats {

		private long supportedPlacementCount;
		private final long[] unsupportedReasonCounts = new long[GpuObjDebugStats.VehicleFallbackReason.values().length];

		public void incrementSupportedPlacementCount() {
			supportedPlacementCount++;
		}

		public void addSupportedPlacementCount(long count) {
			supportedPlacementCount += count;
		}

		public void incrementUnsupportedReasonCount(GpuObjDebugStats.VehicleFallbackReason reason) {
			unsupportedReasonCounts[reason.ordinal()]++;
		}

		public void addUnsupportedReasonCount(GpuObjDebugStats.VehicleFallbackReason reason, long count) {
			unsupportedReasonCounts[reason.ordinal()] += count;
		}

		private long[] copyUnsupportedReasonCounts() {
			return Arrays.copyOf(unsupportedReasonCounts, unsupportedReasonCounts.length);
		}
	}

	public static final class ConditionBucket {

		public final PartCondition condition;
		public final ObjectArrayList<Part> parts;
		public final long supportedPlacementCount;
		private final long[] unsupportedPlacementCounts;

		private ConditionBucket(PartCondition condition, ObjectArrayList<Part> parts, long supportedPlacementCount, long[] unsupportedPlacementCounts) {
			this.condition = condition;
			this.parts = parts;
			this.supportedPlacementCount = supportedPlacementCount;
			this.unsupportedPlacementCounts = unsupportedPlacementCounts;
		}

		public long getUnsupportedPlacementCount(GpuObjDebugStats.VehicleFallbackReason reason) {
			return unsupportedPlacementCounts[reason.ordinal()];
		}

		public long getTotalUnsupportedPlacementCount() {
			long count = 0;
			for (final long unsupportedPlacementCount : unsupportedPlacementCounts) {
				count += unsupportedPlacementCount;
			}
			return count;
		}
	}

	public static final class Part {

		public final PartCondition condition;
		public final StaticObjMesh mesh;
		public final ObjBatchKey batchKey;
		public final MaterialProperties materialProperties;
		public final Matrix4f localTransform;
		public final StoredMatrixTransformations normalReferenceLocalTransformations;
		public final String debugSampleId;
		@Nullable
		private final Supplier<OptimizedModelWrapper> normalReferenceModelSupplier;
		@Nullable
		private OptimizedModelWrapper normalReferenceModel;

		public Part(PartCondition condition, StaticObjMesh mesh, ObjBatchKey batchKey, MaterialProperties materialProperties, Matrix4f localTransform, StoredMatrixTransformations normalReferenceLocalTransformations, String debugSampleId, @Nullable Supplier<OptimizedModelWrapper> normalReferenceModelSupplier) {
			this.condition = condition;
			this.mesh = mesh;
			this.batchKey = batchKey;
			this.materialProperties = materialProperties;
			this.localTransform = localTransform;
			this.normalReferenceLocalTransformations = normalReferenceLocalTransformations;
			this.debugSampleId = debugSampleId;
			this.normalReferenceModelSupplier = normalReferenceModelSupplier;
		}

		@Nullable
		public OptimizedModelWrapper getOrCreateNormalReferenceModel() {
			if (normalReferenceModel == null && normalReferenceModelSupplier != null) {
				normalReferenceModel = normalReferenceModelSupplier.get();
			}
			return normalReferenceModel;
		}
	}
}
