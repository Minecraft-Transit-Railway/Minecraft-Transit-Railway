package org.mtr.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Matrix4f;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.Init;
import org.mtr.mod.resource.OptimizedModelWrapper;

import javax.annotation.Nullable;

public final class GpuObjDebugStats {

	public enum Source {RAIL, VEHICLE}

	public enum RailFallbackReason {
		CONFIG_DISABLED("configDisabled"),
		OPTIMIZED_RENDERING_UNAVAILABLE("optimizedUnavailable"),
		STYLE_NOT_OBJ("styleNotObj"),
		GPU_CACHE_UNAVAILABLE("gpuCacheUnavailable"),
		GPU_CACHE_EMPTY("gpuCacheEmpty"),
		HAS_TRANSLUCENT_MESH("hasTranslucentMesh"),
		QUEUE_RETURNED_FALSE_AFTER_CACHE_LOOKUP("queueReturnedFalse");

		public final String label;

		RailFallbackReason(String label) {
			this.label = label;
		}
	}

	public enum VehicleFallbackReason {
		CONFIG_DISABLED("configDisabled"),
		OPTIMIZED_RENDERING_UNAVAILABLE("optimizedUnavailable"),
		MODEL_NOT_OBJ("modelNotObj"),
		HAS_TRANSLUCENT_MESH("hasTranslucentMesh"),
		PART_TYPE_UNSUPPORTED("partTypeUnsupported"),
		DOOR_PART("doorPart"),
		RENDER_STAGE_UNSUPPORTED("renderStageUnsupported"),
		OBJ_GROUP_NOT_FOUND("objGroupNotFound"),
		GPU_CACHE_UNAVAILABLE("gpuCacheUnavailable"),
		SHADER_UNSUPPORTED("shaderUnsupported");

		public final String label;

		VehicleFallbackReason(String label) {
			this.label = label;
		}
	}

	private static final Snapshot CURRENT_FRAME = new Snapshot();
	private static boolean statusPending;
	private static boolean collectingStatus;
	private static boolean instancingEnabled;
	@Nullable
	private static DiagnosticSample currentRailDiagnosticSample;
	@Nullable
	private static DiagnosticSample currentVehicleDiagnosticSample;

	private GpuObjDebugStats() {
	}

	public static void beginFrame(boolean newInstancingEnabled) {
		instancingEnabled = newInstancingEnabled;
		collectingStatus = statusPending;
		if (collectingStatus) {
			CURRENT_FRAME.clear();
			currentRailDiagnosticSample = null;
			currentVehicleDiagnosticSample = null;
		}
	}

	public static void finishFrame() {
		if (collectingStatus) {
			emitStatus();
			statusPending = false;
			collectingStatus = false;
		}
	}

	public static void resetSession() {
		statusPending = false;
		collectingStatus = false;
		CURRENT_FRAME.clear();
		currentRailDiagnosticSample = null;
		currentVehicleDiagnosticSample = null;
	}

	public static void requestStatus() {
		statusPending = true;
	}

	public static String getStatusSummary() {
		return "statusPending=" + statusPending;
	}

	public static boolean isDiagnosticEnabled() {
		return collectingStatus;
	}

	public static boolean shouldCollectTimings() {
		return collectingStatus;
	}

	public static boolean shouldSkipCameraOffset() {
		return false;
	}

	public static void scheduleDiagnosticRender() {
	}

	public static void handleClientDisconnect() {
		resetSession();
	}

	public static void tickWatch() {
	}

	public static void recordInstanceQueued(Source source, boolean newBatch, boolean newMesh) {
		if (!collectingStatus) {
			return;
		}
		CURRENT_FRAME.instancesTotal++;
		if (source == Source.RAIL) {
			CURRENT_FRAME.railInstances++;
		} else {
			CURRENT_FRAME.vehicleInstances++;
		}
		if (newBatch) {
			CURRENT_FRAME.activeBatches++;
		}
		if (newMesh) {
			CURRENT_FRAME.activeMeshes++;
		}
	}

	public static void recordDrawInstanced() {
		if (collectingStatus) {
			CURRENT_FRAME.instancedDraws++;
		}
	}

	public static void recordRailQueueNanos(long nanos) {
		if (collectingStatus) {
			CURRENT_FRAME.railQueueNanos += nanos;
		}
	}

	public static void recordVehicleQueueNanos(long nanos) {
		if (collectingStatus) {
			CURRENT_FRAME.vehicleQueueNanos += nanos;
		}
	}

	public static void recordRenderOpaqueNanos(long nanos) {
		if (collectingStatus) {
			CURRENT_FRAME.renderOpaqueNanos += nanos;
		}
	}

	public static void recordInstanceUploadNanos(long nanos) {
		if (collectingStatus) {
			CURRENT_FRAME.instanceUploadNanos += nanos;
		}
	}

	public static void recordInstanceDrawNanos(long nanos) {
		if (collectingStatus) {
			CURRENT_FRAME.instanceDrawNanos += nanos;
		}
	}

	public static void recordRailOutcome(boolean success, RailFallbackReason fallbackReason) {
		if (!collectingStatus) {
			return;
		}
		CURRENT_FRAME.railAttemptedSegments++;
		if (success) {
			CURRENT_FRAME.railGpuQueuedSegments++;
		} else {
			CURRENT_FRAME.railFallbackSegments++;
			CURRENT_FRAME.railFallbackReasons[fallbackReason.ordinal()]++;
		}
	}

	public static void recordVehicleEligibleParts(long count) {
		if (collectingStatus) {
			CURRENT_FRAME.vehicleEligibleParts += count;
		}
	}

	public static void recordVehicleQueuedParts(long count) {
		if (collectingStatus) {
			CURRENT_FRAME.vehicleGpuParts += count;
		}
	}

	public static void recordVehicleGpuQueueCall() {
		if (collectingStatus) {
			CURRENT_FRAME.vehicleGpuQueues++;
		}
	}

	public static void recordVehicleFallbackParts(VehicleFallbackReason reason, long count) {
		if (!collectingStatus || count <= 0) {
			return;
		}
		CURRENT_FRAME.vehicleFallbackParts += count;
		CURRENT_FRAME.vehicleFallbackReasons[reason.ordinal()] += count;
	}

	public static void recordVehicleFallbackScheduled() {
		if (collectingStatus) {
			CURRENT_FRAME.vehicleFallbackScheduled++;
		}
	}

	public static void recordVehicleFallbackExecuted() {
		if (collectingStatus) {
			CURRENT_FRAME.vehicleFallbackExecuted++;
		}
	}

	public static void recordVehicleFallbackOptimizedQueue(boolean queued) {
		if (!collectingStatus) {
			return;
		}
		CURRENT_FRAME.vehicleFallbackOptimizedQueueCalls++;
		if (queued) {
			CURRENT_FRAME.vehicleFallbackOptimizedQueueAccepted++;
		}
	}

	public static void recordOptimizedRendererRender(boolean available, boolean renderTranslucent) {
		if (!collectingStatus) {
			return;
		}
		CURRENT_FRAME.optimizedRendererRenderCalls++;
		if (available) {
			CURRENT_FRAME.optimizedRendererRenderAvailable++;
		}
		if (renderTranslucent) {
			CURRENT_FRAME.optimizedRendererRenderTranslucent++;
		}
	}

	public static void recordVehicleConditionFilteredParts(long count) {
		if (collectingStatus) {
			CURRENT_FRAME.vehicleConditionFilteredParts += count;
		}
	}

	public static void recordVehicleFallbackQueueSample(String sample) {
	}

	public static void recordVehicleConditionBucketSample(String sample) {
	}

	@Nullable
	public static DiagnosticSample captureDiagnosticSample(Source source, ObjBatchKey batchKey, StaticObjMesh staticObjMesh, Matrix4f matrix, boolean useDefaultOffset) {
		if (!collectingStatus) {
			return null;
		}
		if (source == Source.RAIL) {
			if (currentRailDiagnosticSample == null) {
				currentRailDiagnosticSample = new DiagnosticSample(source, batchKey, staticObjMesh);
				return currentRailDiagnosticSample;
			}
		} else if (currentVehicleDiagnosticSample == null) {
			currentVehicleDiagnosticSample = new DiagnosticSample(source, batchKey, staticObjMesh);
			return currentVehicleDiagnosticSample;
		}
		return null;
	}

	public static void finalizeDiagnosticSample(@Nullable DiagnosticSample diagnosticSample, Vector3d offset, int instanceCount) {
		if (diagnosticSample != null && collectingStatus) {
			diagnosticSample.recordDraw(instanceCount);
		}
	}

	private static void emitStatus() {
		final ObjectArrayList<String> lines = new ObjectArrayList<>();
		lines.add("[MTR Debug] GPU instancing status");
		lines.add("Instancing enabled: " + instancingEnabled);
		lines.add(String.format("Frame instances total/rails/vehicles: %d/%d/%d", CURRENT_FRAME.instancesTotal, CURRENT_FRAME.railInstances, CURRENT_FRAME.vehicleInstances));
		lines.add(String.format("Frame active batches/meshes/instanced draws: %d/%d/%d", CURRENT_FRAME.activeBatches, CURRENT_FRAME.activeMeshes, CURRENT_FRAME.instancedDraws));
		lines.add(String.format(
				"Frame GPU timings ms railQueue/vehicleQueue/render/upload/draw: %.3f/%.3f/%.3f/%.3f/%.3f",
				nanosToMillis(CURRENT_FRAME.railQueueNanos),
				nanosToMillis(CURRENT_FRAME.vehicleQueueNanos),
				nanosToMillis(CURRENT_FRAME.renderOpaqueNanos),
				nanosToMillis(CURRENT_FRAME.instanceUploadNanos),
				nanosToMillis(CURRENT_FRAME.instanceDrawNanos)
		));
		lines.add(String.format("Frame rail attempted/gpu/fallback: %d/%d/%d", CURRENT_FRAME.railAttemptedSegments, CURRENT_FRAME.railGpuQueuedSegments, CURRENT_FRAME.railFallbackSegments));
		lines.add("Frame rail fallback reasons: " + formatReasons(CURRENT_FRAME.railFallbackReasons, RailFallbackReason.values()));
		lines.add(String.format("Frame vehicle eligible/gpu/fallback/filtered: %d/%d/%d/%d", CURRENT_FRAME.vehicleEligibleParts, CURRENT_FRAME.vehicleGpuParts, CURRENT_FRAME.vehicleFallbackParts, CURRENT_FRAME.vehicleConditionFilteredParts));
		lines.add("Frame vehicle fallback reasons: " + formatReasons(CURRENT_FRAME.vehicleFallbackReasons, VehicleFallbackReason.values()));
		lines.add(String.format(
				"Frame vehicle fallback pipeline scheduled/executed/queueCalls/queueAccepted optimizedRenderCalls/available/translucent: %d/%d/%d/%d %d/%d/%d",
				CURRENT_FRAME.vehicleFallbackScheduled,
				CURRENT_FRAME.vehicleFallbackExecuted,
				CURRENT_FRAME.vehicleFallbackOptimizedQueueCalls,
				CURRENT_FRAME.vehicleFallbackOptimizedQueueAccepted,
				CURRENT_FRAME.optimizedRendererRenderCalls,
				CURRENT_FRAME.optimizedRendererRenderAvailable,
				CURRENT_FRAME.optimizedRendererRenderTranslucent
		));
		appendDiagnosticSample(lines, "Rail", currentRailDiagnosticSample);
		appendDiagnosticSample(lines, "Vehicle", currentVehicleDiagnosticSample);
		lines.forEach(Init.LOGGER::info);
	}

	private static void appendDiagnosticSample(ObjectArrayList<String> lines, String label, @Nullable DiagnosticSample diagnosticSample) {
		if (diagnosticSample == null) {
			lines.add(label + " sample: none captured");
			return;
		}
		lines.add(String.format(
				"%s sample: stage=%s shader=%s meshShader=%s vertices=%d instances=%d drawn=%s",
				label,
				diagnosticSample.renderStage,
				diagnosticSample.shaderType,
				diagnosticSample.meshShaderType,
				diagnosticSample.vertexCount,
				diagnosticSample.instanceCount,
				diagnosticSample.drawn
		));
		lines.add(String.format(
				"%s material states: batchShader=%s batchTranslucent=%s batchColor=%s | meshShader=%s meshColor=0x%08X | vertexArrayShader=%s vertexArrayTranslucent=%s vertexArrayColor=%s | drawVertexArrayShader=%s drawVertexArrayTranslucent=%s drawVertexArrayColor=%s",
				label,
				diagnosticSample.batchShaderType,
				diagnosticSample.batchTranslucent,
				formatNullableHex(diagnosticSample.batchMaterialColor),
				diagnosticSample.meshShaderType,
				diagnosticSample.materialColor,
				diagnosticSample.vertexArrayShaderType,
				diagnosticSample.vertexArrayTranslucent,
				formatNullableHex(diagnosticSample.vertexArrayMaterialColor),
				diagnosticSample.drawVertexArrayShaderType,
				diagnosticSample.drawVertexArrayTranslucent,
				formatNullableHex(diagnosticSample.drawVertexArrayMaterialColor)
		));
		lines.add(String.format(
				"%s light: raw=0x%08X exchanged=0x%08X instance=0x%08X",
				label,
				diagnosticSample.rawLight,
				diagnosticSample.exchangedLight,
				diagnosticSample.instanceLight
		));
	}

	private static double nanosToMillis(long nanos) {
		return nanos / 1_000_000D;
	}

	private static <T extends Enum<T>> String formatReasons(long[] counts, T[] reasons) {
		final StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < counts.length; i++) {
			if (counts[i] <= 0) {
				continue;
			}
			if (stringBuilder.length() > 0) {
				stringBuilder.append(", ");
			}
			stringBuilder.append(getReasonLabel(reasons[i])).append("=").append(counts[i]);
		}
		return stringBuilder.length() == 0 ? "none" : stringBuilder.toString();
	}

	private static String getReasonLabel(Enum<?> reason) {
		return reason instanceof RailFallbackReason ? ((RailFallbackReason) reason).label : ((VehicleFallbackReason) reason).label;
	}

	private static String formatNullableHex(@Nullable Integer value) {
		return value == null ? "null" : String.format("0x%08X", value);
	}

	public static final class DiagnosticSample {

		private final Source source;
		private final String renderStage;
		private final String shaderType;
		private final String meshShaderType;
		private final int vertexCount;
		private final int materialColor;
		private int instanceCount;
		private int instanceColor;
		private int instanceLight;
		private int rawLight;
		private int exchangedLight;
		private String batchShaderType = "unset";
		private String vertexArrayShaderType = "unset";
		private String drawVertexArrayShaderType = "unset";
		private boolean batchTranslucent;
		private boolean vertexArrayTranslucent;
		private boolean drawVertexArrayTranslucent;
		@Nullable
		private Integer batchMaterialColor;
		@Nullable
		private Integer vertexArrayMaterialColor;
		@Nullable
		private Integer drawVertexArrayMaterialColor;
		private boolean drawn;

		private DiagnosticSample(Source source, ObjBatchKey batchKey, StaticObjMesh staticObjMesh) {
			this.source = source;
			renderStage = batchKey.renderStage.name();
			shaderType = batchKey.shaderType.name();
			meshShaderType = staticObjMesh.shaderType.name();
			vertexCount = staticObjMesh.vertexCount;
			materialColor = staticObjMesh.materialColor;
			instanceColor = staticObjMesh.materialColor;
		}

		private void recordDraw(int instanceCount) {
			drawn = true;
			this.instanceCount = instanceCount;
		}

		void setPreparedDrawMatrix(Matrix4f matrix) {
		}

		void setInstanceColor(int instanceColor) {
			this.instanceColor = instanceColor;
		}

		void setInstanceLight(int instanceLight) {
			this.instanceLight = instanceLight;
		}

		void setMaterialState(MaterialProperties batchMaterialProperties, StaticObjMesh staticObjMesh) {
			batchShaderType = batchMaterialProperties.shaderType.name();
			batchTranslucent = batchMaterialProperties.translucent;
			batchMaterialColor = batchMaterialProperties.vertexAttributeState.color;
			vertexArrayShaderType = staticObjMesh.vertexArray.materialProperties.shaderType.name();
			vertexArrayTranslucent = staticObjMesh.vertexArray.materialProperties.translucent;
			vertexArrayMaterialColor = staticObjMesh.vertexArray.materialProperties.vertexAttributeState.color;
		}

		public void setDrawVertexArrayState(MaterialProperties drawVertexArrayMaterialProperties) {
			drawVertexArrayShaderType = drawVertexArrayMaterialProperties.shaderType.name();
			drawVertexArrayTranslucent = drawVertexArrayMaterialProperties.translucent;
			drawVertexArrayMaterialColor = drawVertexArrayMaterialProperties.vertexAttributeState.color;
		}

		public void setLight(int rawLight, int exchangedLight) {
			this.rawLight = rawLight;
			this.exchangedLight = exchangedLight;
		}

		public void setCaptureOffset(Vector3d offset) {
		}

		public void setNormalReference(@Nullable OptimizedModelWrapper model, StoredMatrixTransformations transformations, Matrix4f matrix, boolean matched, String sampleId) {
		}
	}

	private static final class Snapshot {
		private long instancesTotal;
		private long railInstances;
		private long vehicleInstances;
		private long activeBatches;
		private long activeMeshes;
		private long instancedDraws;
		private long railQueueNanos;
		private long vehicleQueueNanos;
		private long renderOpaqueNanos;
		private long instanceUploadNanos;
		private long instanceDrawNanos;
		private long railAttemptedSegments;
		private long railGpuQueuedSegments;
		private long railFallbackSegments;
		private final long[] railFallbackReasons = new long[RailFallbackReason.values().length];
		private long vehicleEligibleParts;
		private long vehicleGpuParts;
		private long vehicleFallbackParts;
		private long vehicleConditionFilteredParts;
		private long vehicleGpuQueues;
		private final long[] vehicleFallbackReasons = new long[VehicleFallbackReason.values().length];
		private long vehicleFallbackScheduled;
		private long vehicleFallbackExecuted;
		private long vehicleFallbackOptimizedQueueCalls;
		private long vehicleFallbackOptimizedQueueAccepted;
		private long optimizedRendererRenderCalls;
		private long optimizedRendererRenderAvailable;
		private long optimizedRendererRenderTranslucent;

		private void clear() {
			instancesTotal = 0;
			railInstances = 0;
			vehicleInstances = 0;
			activeBatches = 0;
			activeMeshes = 0;
			instancedDraws = 0;
			railQueueNanos = 0;
			vehicleQueueNanos = 0;
			renderOpaqueNanos = 0;
			instanceUploadNanos = 0;
			instanceDrawNanos = 0;
			railAttemptedSegments = 0;
			railGpuQueuedSegments = 0;
			railFallbackSegments = 0;
			clearArray(railFallbackReasons);
			vehicleEligibleParts = 0;
			vehicleGpuParts = 0;
			vehicleFallbackParts = 0;
			vehicleConditionFilteredParts = 0;
			vehicleGpuQueues = 0;
			clearArray(vehicleFallbackReasons);
			vehicleFallbackScheduled = 0;
			vehicleFallbackExecuted = 0;
			vehicleFallbackOptimizedQueueCalls = 0;
			vehicleFallbackOptimizedQueueAccepted = 0;
			optimizedRendererRenderCalls = 0;
			optimizedRendererRenderAvailable = 0;
			optimizedRendererRenderTranslucent = 0;
		}

		private static void clearArray(long[] array) {
			for (int i = 0; i < array.length; i++) {
				array[i] = 0;
			}
		}
	}
}
