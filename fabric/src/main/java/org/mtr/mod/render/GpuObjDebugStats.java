package org.mtr.mod.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Camera;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.OptimizedModel;
import org.mtr.mapping.render.batch.MaterialProperties;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.resource.OptimizedModelWrapper;

import javax.annotation.Nullable;
import java.util.Arrays;

public final class GpuObjDebugStats {

	public enum Source {RAIL, VEHICLE}

	public enum RailViewMode {
		ALL("all"),
		INSTANCED("instanced"),
		STATIC_MATCHED("staticMatched"),
		NORMAL("normal");

		public final String label;

		RailViewMode(String label) {
			this.label = label;
		}
	}

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
		GPU_CACHE_UNAVAILABLE("gpuCacheUnavailable");

		public final String label;

		VehicleFallbackReason(String label) {
			this.label = label;
		}
	}

	private static final long WINDOW_MILLIS = 1000;
	private static final long WATCH_INTERVAL_MILLIS = 1000;
	private static final double NEAR_PLANE_EPSILON = 0.25;
	private static final int RAIL_SAMPLE_COLOR = 0xFF00FFFF;
	private static final int VEHICLE_SAMPLE_COLOR = 0xFFFFFF00;
	private static final int RAIL_NORMAL_REFERENCE_COLOR = 0xFFFF8080;
	private static final int VEHICLE_NORMAL_REFERENCE_COLOR = 0xFF80FF80;
	private static final Snapshot CURRENT_FRAME = new Snapshot();
	private static final Snapshot LAST_FRAME = new Snapshot();
	private static final Snapshot WINDOW_SNAPSHOT = new Snapshot();
	private static final Snapshot RELOAD_SESSION = new Snapshot();
	private static final Snapshot WATCH_SESSION = new Snapshot();
	private static final ObjectArrayList<TimedSnapshot> WINDOW_HISTORY = new ObjectArrayList<>();
	private static boolean diagnosticEnabled;
	private static boolean diagnosticSkipCameraOffset;
	private static boolean diagnosticForceNoCull;
	private static boolean diagnosticForceWhiteCutout;
	private static RailViewMode railViewMode = RailViewMode.NORMAL;
	@Nullable
	private static DiagnosticSample currentRailDiagnosticSample;
	@Nullable
	private static DiagnosticSample currentVehicleDiagnosticSample;
	@Nullable
	private static DiagnosticSample lastRailDiagnosticSample;
	@Nullable
	private static DiagnosticSample lastVehicleDiagnosticSample;
	private static boolean instancingEnabled;
	private static boolean watchActive;
	private static boolean pendingRailReport;
	private static RailViewMode pendingRailReportRestoreRailViewMode = RailViewMode.NORMAL;
	private static long nextWatchReportMillis;

	private GpuObjDebugStats() {
	}

	public static void beginFrame(boolean newInstancingEnabled) {
		instancingEnabled = newInstancingEnabled;
		CURRENT_FRAME.clear();
		currentRailDiagnosticSample = null;
		currentVehicleDiagnosticSample = null;
	}

	public static void recordInstanceQueued(Source source, boolean newBatch, boolean newMesh) {
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
		CURRENT_FRAME.instancedDraws++;
	}

	public static void recordRailQueueNanos(long nanos) {
		CURRENT_FRAME.railQueueNanos += nanos;
	}

	public static void recordVehicleQueueNanos(long nanos) {
		CURRENT_FRAME.vehicleQueueNanos += nanos;
	}

	public static void recordRenderOpaqueNanos(long nanos) {
		CURRENT_FRAME.renderOpaqueNanos += nanos;
	}

	public static void recordInstanceUploadNanos(long nanos) {
		CURRENT_FRAME.instanceUploadNanos += nanos;
	}

	public static void recordInstanceDrawNanos(long nanos) {
		CURRENT_FRAME.instanceDrawNanos += nanos;
	}

	public static void recordRailOutcome(boolean success, RailFallbackReason fallbackReason) {
		CURRENT_FRAME.railAttemptedSegments++;
		if (success) {
			CURRENT_FRAME.railGpuQueuedSegments++;
		} else {
			CURRENT_FRAME.railFallbackSegments++;
			CURRENT_FRAME.railFallbackReasons[fallbackReason.ordinal()]++;
		}
	}

	public static void recordVehicleEligibleParts(long count) {
		CURRENT_FRAME.vehicleEligibleParts += count;
	}

	public static void recordVehicleQueuedParts(long count) {
		CURRENT_FRAME.vehicleGpuParts += count;
	}

	public static void recordVehicleGpuQueueCall() {
		CURRENT_FRAME.vehicleGpuQueues++;
	}

	public static void recordVehicleFallbackParts(VehicleFallbackReason reason, long count) {
		if (count <= 0) {
			return;
		}
		CURRENT_FRAME.vehicleFallbackParts += count;
		CURRENT_FRAME.vehicleFallbackReasons[reason.ordinal()] += count;
	}

	public static void recordVehicleConditionFilteredParts(long count) {
		CURRENT_FRAME.vehicleConditionFilteredParts += count;
	}

	public static void finishFrame() {
		LAST_FRAME.copyFrom(CURRENT_FRAME);
		RELOAD_SESSION.add(CURRENT_FRAME);
		if (watchActive) {
			WATCH_SESSION.add(CURRENT_FRAME);
		}
		lastRailDiagnosticSample = currentRailDiagnosticSample;
		lastVehicleDiagnosticSample = currentVehicleDiagnosticSample;

		final TimedSnapshot timedSnapshot = new TimedSnapshot(InitClient.getGameMillis(), CURRENT_FRAME.copy());
		WINDOW_HISTORY.add(timedSnapshot);
		pruneWindowHistory(timedSnapshot.timeMillis);

		if (pendingRailReport) {
			pendingRailReport = false;
			emitReport("command report");
			railViewMode = pendingRailReportRestoreRailViewMode;
		}
	}

	public static void resetSession() {
		CURRENT_FRAME.clear();
		LAST_FRAME.clear();
		WINDOW_SNAPSHOT.clear();
		RELOAD_SESSION.clear();
		WINDOW_HISTORY.clear();
		currentRailDiagnosticSample = null;
		currentVehicleDiagnosticSample = null;
		lastRailDiagnosticSample = null;
		lastVehicleDiagnosticSample = null;
		if (watchActive) {
			WATCH_SESSION.clear();
		}
	}

	public static void enableDiagnostics() {
		diagnosticEnabled = true;
		railViewMode = RailViewMode.NORMAL;
	}

	public static void disableDiagnostics() {
		diagnosticEnabled = false;
		diagnosticSkipCameraOffset = false;
		diagnosticForceNoCull = false;
		diagnosticForceWhiteCutout = false;
		railViewMode = RailViewMode.NORMAL;
		currentRailDiagnosticSample = null;
		currentVehicleDiagnosticSample = null;
		lastRailDiagnosticSample = null;
		lastVehicleDiagnosticSample = null;
	}

	public static boolean isDiagnosticEnabled() {
		return diagnosticEnabled;
	}

	public static void setDiagnosticSkipCameraOffset(boolean enabled) {
		if (enabled) {
			diagnosticEnabled = true;
		}
		diagnosticSkipCameraOffset = enabled;
	}

	public static boolean shouldSkipCameraOffset() {
		return diagnosticEnabled && diagnosticSkipCameraOffset;
	}

	public static boolean isDiagnosticSkipCameraOffsetEnabled() {
		return diagnosticSkipCameraOffset;
	}

	public static void setDiagnosticForceNoCull(boolean enabled) {
		if (enabled) {
			diagnosticEnabled = true;
		}
		diagnosticForceNoCull = enabled;
	}

	public static boolean shouldForceNoCull() {
		return diagnosticEnabled && diagnosticForceNoCull;
	}

	public static boolean isDiagnosticForceNoCullEnabled() {
		return diagnosticForceNoCull;
	}

	public static void setDiagnosticForceWhiteCutout(boolean enabled) {
		if (enabled) {
			diagnosticEnabled = true;
		}
		diagnosticForceWhiteCutout = enabled;
	}

	public static boolean shouldForceWhiteCutout() {
		return diagnosticEnabled && diagnosticForceWhiteCutout;
	}

	public static boolean isDiagnosticForceWhiteCutoutEnabled() {
		return diagnosticForceWhiteCutout;
	}

	public static void setRailViewMode(RailViewMode newRailViewMode) {
		diagnosticEnabled = true;
		railViewMode = newRailViewMode;
	}

	public static RailViewMode getRailViewMode() {
		return railViewMode;
	}

	public static void requestRailReport() {
		pendingRailReport = true;
		pendingRailReportRestoreRailViewMode = railViewMode;
		if (diagnosticEnabled) {
			railViewMode = RailViewMode.ALL;
		}
	}

	@Nullable
	public static DiagnosticSample captureDiagnosticSample(Source source, ObjBatchKey batchKey, StaticObjMesh staticObjMesh, Matrix4f matrix, boolean useDefaultOffset) {
		if (!diagnosticEnabled) {
			return null;
		}

		final DiagnosticSample candidate = new DiagnosticSample(source, batchKey, staticObjMesh, matrix, useDefaultOffset);
		if (source == Source.RAIL) {
			if (currentRailDiagnosticSample == null || candidate.shouldReplace(currentRailDiagnosticSample)) {
				currentRailDiagnosticSample = candidate;
				return currentRailDiagnosticSample;
			}
			return null;
		} else {
			if (currentVehicleDiagnosticSample == null || candidate.shouldReplace(currentVehicleDiagnosticSample)) {
				currentVehicleDiagnosticSample = candidate;
				return currentVehicleDiagnosticSample;
			}
			return null;
		}
	}

	public static void finalizeDiagnosticSample(@Nullable DiagnosticSample diagnosticSample, Vector3d offset, int instanceCount) {
		if (diagnosticSample == null || diagnosticSample != currentRailDiagnosticSample && diagnosticSample != currentVehicleDiagnosticSample) {
			return;
		}

		diagnosticSample.recordDraw(offset, instanceCount, shouldSkipCameraOffset(), shouldForceNoCull(), shouldForceWhiteCutout());
	}

	public static void recordVaoAttributeState(@Nullable DiagnosticSample diagnosticSample, String vaoAttributeState) {
		if (diagnosticSample == null || diagnosticSample != currentRailDiagnosticSample && diagnosticSample != currentVehicleDiagnosticSample) {
			return;
		}

		diagnosticSample.setVaoAttributeState(vaoAttributeState);
	}

	@Nullable
	static DiagnosticSample getCurrentRailDiagnosticSample() {
		return currentRailDiagnosticSample;
	}

	@Nullable
	static DiagnosticSample getCurrentVehicleDiagnosticSample() {
		return currentVehicleDiagnosticSample;
	}

	public static void scheduleDiagnosticRender() {
		if (!diagnosticEnabled) {
			return;
		}

		if (railViewMode == RailViewMode.ALL) {
			scheduleDiagnosticRender(currentRailDiagnosticSample, RAIL_SAMPLE_COLOR);
		}
		scheduleDiagnosticRender(currentVehicleDiagnosticSample, VEHICLE_SAMPLE_COLOR);
		if (railViewMode == RailViewMode.ALL || railViewMode == RailViewMode.NORMAL) {
			scheduleNormalReferenceRender(currentRailDiagnosticSample, RAIL_NORMAL_REFERENCE_COLOR);
		}
		scheduleNormalReferenceRender(currentVehicleDiagnosticSample, VEHICLE_NORMAL_REFERENCE_COLOR);
	}

	public static void startWatch() {
		watchActive = true;
		WATCH_SESSION.clear();
		nextWatchReportMillis = InitClient.getGameMillis() + WATCH_INTERVAL_MILLIS;
	}

	public static void stopWatch() {
		watchActive = false;
		nextWatchReportMillis = 0;
		WATCH_SESSION.clear();
	}

	public static boolean isWatchActive() {
		return watchActive;
	}

	public static String getStatusSummary() {
		return String.format(
				"diagnostics=%s watch=%s skipCameraOffset=%s forceNoCull=%s forceWhiteCutout=%s railView=%s",
				diagnosticEnabled,
				watchActive,
				diagnosticSkipCameraOffset,
				diagnosticForceNoCull,
				diagnosticForceWhiteCutout,
				railViewMode.label
		);
	}

	public static void handleClientDisconnect() {
		stopWatch();
	}

	public static void tickWatch() {
		if (!watchActive) {
			return;
		}

		final long currentGameMillis = InitClient.getGameMillis();
		if (currentGameMillis < nextWatchReportMillis) {
			return;
		}

		nextWatchReportMillis = currentGameMillis + WATCH_INTERVAL_MILLIS;
		emitReport("watch");
	}

	public static void emitReport(String reason) {
		final ObjectArrayList<String> lines = buildReportLines(reason);
		lines.forEach(Init.LOGGER::info);
	}

	public static ObjectArrayList<String> buildReportLines(String reason) {
		final ObjectArrayList<String> lines = new ObjectArrayList<>();
		lines.add(String.format("[MTR Debug] GPU instancing report (%s)", reason));
		lines.add(String.format("Instancing enabled: %s | watch active: %s", instancingEnabled, watchActive));
		appendSnapshot(lines, "Frame", LAST_FRAME);
		appendSnapshot(lines, "Window (1s)", buildWindowSnapshot());
		if (watchActive) {
			appendSnapshot(lines, "Watch (since start)", WATCH_SESSION);
		}
		appendSnapshot(lines, "Session (since reload)", RELOAD_SESSION);
		appendDiagnostic(lines);
		return lines;
	}

	private static Snapshot buildWindowSnapshot() {
		final long currentGameMillis = InitClient.getGameMillis();
		pruneWindowHistory(currentGameMillis);
		WINDOW_SNAPSHOT.clear();
		for (int i = 0; i < WINDOW_HISTORY.size(); i++) {
			WINDOW_SNAPSHOT.add(WINDOW_HISTORY.get(i).snapshot);
		}
		return WINDOW_SNAPSHOT;
	}

	private static void pruneWindowHistory(long currentGameMillis) {
		while (!WINDOW_HISTORY.isEmpty() && currentGameMillis - WINDOW_HISTORY.get(0).timeMillis > WINDOW_MILLIS) {
			WINDOW_HISTORY.remove(0);
		}
	}

	private static void appendSnapshot(ObjectArrayList<String> lines, String label, Snapshot snapshot) {
		lines.add(String.format("%s instances total/rails/vehicles: %d/%d/%d", label, snapshot.instancesTotal, snapshot.railInstances, snapshot.vehicleInstances));
		lines.add(String.format("%s active batches/meshes/instanced draws: %d/%d/%d", label, snapshot.activeBatches, snapshot.activeMeshes, snapshot.instancedDraws));
		lines.add(String.format(
				"%s GPU timings ms railQueue/vehicleQueue/render/upload/draw: %.3f/%.3f/%.3f/%.3f/%.3f",
				label,
				nanosToMillis(snapshot.railQueueNanos),
				nanosToMillis(snapshot.vehicleQueueNanos),
				nanosToMillis(snapshot.renderOpaqueNanos),
				nanosToMillis(snapshot.instanceUploadNanos),
				nanosToMillis(snapshot.instanceDrawNanos)
		));
		lines.add(String.format("%s rail attempted/gpu/fallback: %d/%d/%d", label, snapshot.railAttemptedSegments, snapshot.railGpuQueuedSegments, snapshot.railFallbackSegments));
		lines.add(String.format("%s rail fallback reasons: %s", label, formatReasons(snapshot.railFallbackReasons, RailFallbackReason.values())));
		lines.add(String.format("%s vehicle eligible/gpu/fallback/filtered: %d/%d/%d/%d", label, snapshot.vehicleEligibleParts, snapshot.vehicleGpuParts, snapshot.vehicleFallbackParts, snapshot.vehicleConditionFilteredParts));
		lines.add(String.format("%s vehicle gpu queue calls: %d", label, snapshot.vehicleGpuQueues));
		lines.add(String.format("%s vehicle fallback reasons: %s", label, formatReasons(snapshot.vehicleFallbackReasons, VehicleFallbackReason.values())));
	}

	private static double nanosToMillis(long nanos) {
		return nanos / 1_000_000D;
	}

	private static void appendDiagnostic(ObjectArrayList<String> lines) {
		if (!diagnosticEnabled && lastRailDiagnosticSample == null && lastVehicleDiagnosticSample == null) {
			return;
		}

		lines.add(String.format("Diagnostic enabled: %s | skipCameraOffset: %s | forceNoCull: %s | forceWhiteCutout: %s | railView: %s", diagnosticEnabled, diagnosticSkipCameraOffset, diagnosticForceNoCull, diagnosticForceWhiteCutout, railViewMode.label));
		lines.add(String.format(
				"Instance mapping: stride=%d colorPtr=%d lightPtr=%d matrixPtr=%d expected=72/0/4/8",
				GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING.strideInstance,
				GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING.pointers.get(org.mtr.mapping.render.vertex.VertexAttributeType.COLOR),
				GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING.pointers.get(org.mtr.mapping.render.vertex.VertexAttributeType.UV_LIGHTMAP),
				GpuObjRenderer.VERTEX_ATTRIBUTE_MAPPING.pointers.get(org.mtr.mapping.render.vertex.VertexAttributeType.MATRIX_MODEL)
		));
		lines.add("Shader ModelMat patch: Position -> (MODELVIEWMAT * ModelMat * vec4(Position, 1.0)).xyz; Normal -> normalize(mat3(MODELVIEWMAT * ModelMat) * Normal); original ModelViewMat tokens are replaced with mat4(1.0) after sentinel substitution.");
		lines.add("OBJ coordinate note: normal OptimizedModel.ObjModel.loadModel applies rawMesh.applyRotation(X, 180) before upload; GPU RawMesh path now applies the same X180 before StaticObjMesh upload.");
		lines.add("OBJ UV note: normal OptimizedModel.ObjModel applies global flipTextureV with rawMesh.applyUVMirror(false, true); GPU RawMesh path now applies the same global UV mirror after X180.");
		appendDiagnosticSample(lines, "Rail", lastRailDiagnosticSample);
		appendDiagnosticSample(lines, "Vehicle", lastVehicleDiagnosticSample);
	}

	private static void appendDiagnosticSample(ObjectArrayList<String> lines, String label, @Nullable DiagnosticSample diagnosticSample) {
		if (diagnosticSample == null) {
			lines.add(label + " sample: none captured");
			return;
		}

		lines.add(String.format(
				"%s sample: texture=%s stage=%s shader=%s vertices=%d instances=%d useDefaultOffset=%s drawn=%s",
				label,
				diagnosticSample.textureId,
				diagnosticSample.renderStage,
				diagnosticSample.shaderType,
				diagnosticSample.vertexCount,
				diagnosticSample.instanceCount,
				diagnosticSample.useDefaultOffset,
				diagnosticSample.drawn
		));
		lines.add(String.format("%s colors: material=0x%08X instance=0x%08X", label, diagnosticSample.materialColor, diagnosticSample.instanceColor));
		lines.add(String.format("%s sample metadata: source=%s reason=%s normalSample=%s", label, diagnosticSample.source, diagnosticSample.sampleReason, diagnosticSample.normalReferenceSampleId));
		lines.add(String.format(
				"%s bounds: min=(%.5f, %.5f, %.5f) max=(%.5f, %.5f, %.5f) center=(%.5f, %.5f, %.5f)",
				label,
				diagnosticSample.minX,
				diagnosticSample.minY,
				diagnosticSample.minZ,
				diagnosticSample.maxX,
				diagnosticSample.maxY,
				diagnosticSample.maxZ,
				diagnosticSample.centerX,
				diagnosticSample.centerY,
				diagnosticSample.centerZ
		));
		lines.add(String.format("%s GPU raw vertex sample: %s", label, diagnosticSample.staticObjMesh.rawVertexSample));
		lines.add(String.format("%s queued world matrix: %s", label, diagnosticSample.formatQueuedMatrix()));
		lines.add(String.format("%s prepared instanced draw matrix: %s", label, diagnosticSample.formatPreparedDrawMatrix()));
		if (diagnosticSample.hasSingleDrawReferenceMatrix()) {
			lines.add(String.format("%s single-draw debug reference matrix: %s", label, diagnosticSample.formatSingleDrawReferenceMatrix()));
			lines.add(String.format(
					"%s instanced vs single-draw debug delta translation=(%.5f, %.5f, %.5f) maxAbsEntryDelta=%.5f",
					label,
					diagnosticSample.getSingleDrawReferenceTranslationDeltaX(),
					diagnosticSample.getSingleDrawReferenceTranslationDeltaY(),
					diagnosticSample.getSingleDrawReferenceTranslationDeltaZ(),
					diagnosticSample.getSingleDrawReferenceMaxAbsEntryDelta()
			));
		}
		if (diagnosticSample.hasStaticMatchedReferenceMatrix()) {
			lines.add(String.format("%s single-draw material-matched reference matrix: %s", label, diagnosticSample.formatStaticMatchedReferenceMatrix()));
			lines.add(String.format(
					"%s instanced vs single-draw material-matched delta translation=(%.5f, %.5f, %.5f) maxAbsEntryDelta=%.5f",
					label,
					diagnosticSample.getStaticMatchedReferenceTranslationDeltaX(),
					diagnosticSample.getStaticMatchedReferenceTranslationDeltaY(),
					diagnosticSample.getStaticMatchedReferenceTranslationDeltaZ(),
					diagnosticSample.getStaticMatchedReferenceMaxAbsEntryDelta()
			));
		}
		if (diagnosticSample.hasNormalReferenceMatrix()) {
			lines.add(String.format("%s normal-render world matrix: %s", label, diagnosticSample.formatNormalReferenceMatrix()));
			lines.add(String.format("%s normal-render draw matrix: %s", label, diagnosticSample.formatNormalReferenceDrawMatrix()));
			lines.add(String.format(
					"%s queued world vs normal-render world delta translation=(%.5f, %.5f, %.5f) maxAbsEntryDelta=%.5f",
					label,
					diagnosticSample.getQueuedVsNormalReferenceTranslationDeltaX(),
					diagnosticSample.getQueuedVsNormalReferenceTranslationDeltaY(),
					diagnosticSample.getQueuedVsNormalReferenceTranslationDeltaZ(),
					diagnosticSample.getQueuedVsNormalReferenceMaxAbsEntryDelta()
			));
			lines.add(String.format(
					"%s instanced draw vs normal-render draw delta translation=(%.5f, %.5f, %.5f) maxAbsEntryDelta=%.5f",
					label,
					diagnosticSample.getNormalReferenceDrawTranslationDeltaX(),
					diagnosticSample.getNormalReferenceDrawTranslationDeltaY(),
					diagnosticSample.getNormalReferenceDrawTranslationDeltaZ(),
					diagnosticSample.getNormalReferenceDrawMaxAbsEntryDelta()
			));
			if (diagnosticSample.hasSingleDrawReferenceMatrix()) {
				lines.add(String.format(
						"%s single-draw debug vs normal-render draw delta translation=(%.5f, %.5f, %.5f) maxAbsEntryDelta=%.5f",
						label,
						diagnosticSample.getSingleDrawVsNormalReferenceDrawTranslationDeltaX(),
						diagnosticSample.getSingleDrawVsNormalReferenceDrawTranslationDeltaY(),
						diagnosticSample.getSingleDrawVsNormalReferenceDrawTranslationDeltaZ(),
						diagnosticSample.getSingleDrawVsNormalReferenceDrawMaxAbsEntryDelta()
				));
			}
			if (diagnosticSample.hasStaticMatchedReferenceMatrix()) {
				lines.add(String.format(
						"%s single-draw material-matched vs normal-render draw delta translation=(%.5f, %.5f, %.5f) maxAbsEntryDelta=%.5f",
						label,
						diagnosticSample.getStaticMatchedVsNormalReferenceDrawTranslationDeltaX(),
						diagnosticSample.getStaticMatchedVsNormalReferenceDrawTranslationDeltaY(),
						diagnosticSample.getStaticMatchedVsNormalReferenceDrawTranslationDeltaZ(),
						diagnosticSample.getStaticMatchedVsNormalReferenceDrawMaxAbsEntryDelta()
				));
			}
		}
		lines.add(String.format(
				"%s normal-render reference: matched=%s sample=%s origin=(%.5f, %.5f, %.5f) center=(%.5f, %.5f, %.5f)",
				label,
				diagnosticSample.normalReferenceMatched,
				diagnosticSample.normalReferenceSampleId,
				diagnosticSample.normalReferenceWorldOriginX,
				diagnosticSample.normalReferenceWorldOriginY,
				diagnosticSample.normalReferenceWorldOriginZ,
				diagnosticSample.normalReferenceWorldCenterX,
				diagnosticSample.normalReferenceWorldCenterY,
				diagnosticSample.normalReferenceWorldCenterZ
		));
		lines.add(String.format(
				"%s world origin=(%.5f, %.5f, %.5f) world center=(%.5f, %.5f, %.5f)",
				label,
				diagnosticSample.worldOriginX,
				diagnosticSample.worldOriginY,
				diagnosticSample.worldOriginZ,
				diagnosticSample.worldCenterX,
				diagnosticSample.worldCenterY,
				diagnosticSample.worldCenterZ
		));
		lines.add(String.format(
				"%s draw matrix translation=(%.5f, %.5f, %.5f) camera offset=(%.5f, %.5f, %.5f)",
				label,
				diagnosticSample.drawTranslationX,
				diagnosticSample.drawTranslationY,
				diagnosticSample.drawTranslationZ,
				diagnosticSample.cameraOffsetX,
				diagnosticSample.cameraOffsetY,
				diagnosticSample.cameraOffsetZ
		));
		lines.add(String.format(
				"%s camera relative center=(%.5f, %.5f, %.5f) camera space center=(%.5f, %.5f, %.5f) distance=%.5f forwardZ=%.5f finite=%s huge=%s",
				label,
				diagnosticSample.relativeCenterX,
				diagnosticSample.relativeCenterY,
				diagnosticSample.relativeCenterZ,
				diagnosticSample.cameraSpaceCenterX,
				diagnosticSample.cameraSpaceCenterY,
				diagnosticSample.cameraSpaceCenterZ,
				diagnosticSample.distanceFromCamera,
				diagnosticSample.centerForwardZ,
				!diagnosticSample.hasNonFiniteValues,
				diagnosticSample.hasHugeCoordinates
		));
		lines.add(String.format(
				"%s aabb camera-space z range=(%.5f, %.5f) queuedForwardZ=%.5f postOffsetForwardZ=%.5f queuedDistance=%.5f postOffsetDistance=%.5f nearPlaneCross=%s cornerSplit=%s maxAbsXY=(%.5f, %.5f) risk=%.5f reason=%s",
				label,
				diagnosticSample.minForwardZ,
				diagnosticSample.maxForwardZ,
				diagnosticSample.centerForwardZ,
				diagnosticSample.postOffsetForwardZ,
				diagnosticSample.distanceFromCamera,
				diagnosticSample.postOffsetDistance,
				diagnosticSample.crossesNearPlane,
				diagnosticSample.hasCornerBehindAndAhead,
				diagnosticSample.maxAbsCameraSpaceX,
				diagnosticSample.maxAbsCameraSpaceY,
				diagnosticSample.riskScore,
				diagnosticSample.sampleReason
		));
		lines.add(String.format(
				"%s draw toggles: skipCameraOffset=%s forceNoCull=%s forceWhiteCutout=%s",
				label,
				diagnosticSample.skipCameraOffset,
				diagnosticSample.forceNoCull,
				diagnosticSample.forceWhiteCutout
		));
		if (!diagnosticSample.vaoAttributeState.isEmpty()) {
			lines.add(String.format("%s VAO attributes: %s", label, diagnosticSample.vaoAttributeState));
		}
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

	public static final class DiagnosticSample {

		private final Source source;
		private final String textureId;
		private final Identifier texture;
		private final String renderStage;
		private final String shaderType;
		private final OptimizedModel.ShaderType shaderTypeEnum;
		private final StaticObjMesh staticObjMesh;
		private final int vertexCount;
		private final int materialColor;
		private final boolean useDefaultOffset;
		private final float minX;
		private final float minY;
		private final float minZ;
		private final float maxX;
		private final float maxY;
		private final float maxZ;
		private final float centerX;
		private final float centerY;
		private final float centerZ;
		private final float[] queuedMatrix = new float[16];
		private final float[] preparedDrawMatrix = new float[16];
		private final float[] singleDrawReferenceMatrix = new float[16];
		private final float[] staticMatchedReferenceMatrix = new float[16];
		private final float[] normalReferenceMatrix = new float[16];
		private final float[] normalReferenceDrawMatrix = new float[16];
		private final Vector3d[] worldCorners = new Vector3d[8];
		private final double minForwardZ;
		private final double maxForwardZ;
		private final double centerForwardZ;
		private final double maxAbsCameraSpaceX;
		private final double maxAbsCameraSpaceY;
		private final double riskScore;
		private final String sampleReason;
		private final boolean crossesNearPlane;
		private final boolean hasCornerBehindAndAhead;
		private float worldOriginX;
		private float worldOriginY;
		private float worldOriginZ;
		private float worldCenterX;
		private float worldCenterY;
		private float worldCenterZ;
		private float drawTranslationX;
		private float drawTranslationY;
		private float drawTranslationZ;
		private double cameraOffsetX;
		private double cameraOffsetY;
		private double cameraOffsetZ;
		private double relativeCenterX;
		private double relativeCenterY;
		private double relativeCenterZ;
		private double cameraSpaceCenterX;
		private double cameraSpaceCenterY;
		private double cameraSpaceCenterZ;
		private double distanceFromCamera;
		private double postOffsetForwardZ;
		private double postOffsetDistance;
		private int instanceCount;
		private int instanceColor;
		private boolean hasPreparedDrawMatrix;
		private boolean hasSingleDrawReferenceMatrix;
		private boolean hasStaticMatchedReferenceMatrix;
		private boolean hasNormalReferenceMatrix;
		private boolean hasNormalReferenceDrawMatrix;
		private boolean drawn;
		private boolean skipCameraOffset;
		private boolean forceNoCull;
		private boolean forceWhiteCutout;
		private boolean hasNonFiniteValues;
		private boolean hasHugeCoordinates;
		private boolean normalReferenceMatched;
		private float normalReferenceWorldOriginX;
		private float normalReferenceWorldOriginY;
		private float normalReferenceWorldOriginZ;
		private float normalReferenceWorldCenterX;
		private float normalReferenceWorldCenterY;
		private float normalReferenceWorldCenterZ;
		private String vaoAttributeState = "";
		private String normalReferenceSampleId = "none";
		@Nullable
		private StoredMatrixTransformations normalReferenceTransformations;
		@Nullable
		private OptimizedModelWrapper normalReferenceModel;

		private DiagnosticSample(Source source, ObjBatchKey batchKey, StaticObjMesh staticObjMesh, Matrix4f matrix, boolean useDefaultOffset) {
			this.source = source;
			texture = batchKey.texture;
			textureId = String.valueOf(batchKey.texture);
			renderStage = batchKey.renderStage.name();
			shaderType = batchKey.shaderType.name();
			shaderTypeEnum = batchKey.shaderType;
			this.staticObjMesh = staticObjMesh;
			vertexCount = staticObjMesh.vertexCount;
			materialColor = staticObjMesh.materialColor;
			instanceColor = staticObjMesh.materialColor;
			this.useDefaultOffset = useDefaultOffset;
			minX = staticObjMesh.minX;
			minY = staticObjMesh.minY;
			minZ = staticObjMesh.minZ;
			maxX = staticObjMesh.maxX;
			maxY = staticObjMesh.maxY;
			maxZ = staticObjMesh.maxZ;
			centerX = staticObjMesh.centerX;
			centerY = staticObjMesh.centerY;
			centerZ = staticObjMesh.centerZ;
			storeMatrix(matrix, queuedMatrix);
			final Vector3f worldOrigin = matrix.transformPosition(new Vector3f());
			final Vector3f worldCenter = matrix.transformPosition(new Vector3f(centerX, centerY, centerZ));
			worldOriginX = worldOrigin.x;
			worldOriginY = worldOrigin.y;
			worldOriginZ = worldOrigin.z;
			worldCenterX = worldCenter.x;
			worldCenterY = worldCenter.y;
			worldCenterZ = worldCenter.z;

			final Camera camera = MinecraftClient.getInstance().getGameRendererMapped().getCamera();
			if (camera == null) {
				minForwardZ = Double.NEGATIVE_INFINITY;
				maxForwardZ = Double.NEGATIVE_INFINITY;
				centerForwardZ = Double.NEGATIVE_INFINITY;
				maxAbsCameraSpaceX = 0;
				maxAbsCameraSpaceY = 0;
				riskScore = Double.NEGATIVE_INFINITY;
				sampleReason = "no_camera";
				crossesNearPlane = false;
				hasCornerBehindAndAhead = false;
				return;
			}

			double minForward = Double.POSITIVE_INFINITY;
			double maxForward = Double.NEGATIVE_INFINITY;
			double maxAbsX = 0;
			double maxAbsY = 0;
			for (int i = 0; i < 8; i++) {
				final Vector3d worldCorner = createWorldCorner(matrix, staticObjMesh, i);
				worldCorners[i] = worldCorner;
				final Vector3d cameraSpaceCorner = toCameraSpace(worldCorner, camera);
				minForward = Math.min(minForward, cameraSpaceCorner.getZMapped());
				maxForward = Math.max(maxForward, cameraSpaceCorner.getZMapped());
				maxAbsX = Math.max(maxAbsX, Math.abs(cameraSpaceCorner.getXMapped()));
				maxAbsY = Math.max(maxAbsY, Math.abs(cameraSpaceCorner.getYMapped()));
			}

			final Vector3d centerCameraSpace = toCameraSpace(new Vector3d(worldCenterX, worldCenterY, worldCenterZ), camera);
			centerForwardZ = centerCameraSpace.getZMapped();
			distanceFromCamera = distance(worldCenterX, worldCenterY, worldCenterZ, camera.getPos());
			postOffsetForwardZ = centerForwardZ;
			postOffsetDistance = distanceFromCamera;
			minForwardZ = minForward;
			maxForwardZ = maxForward;
			maxAbsCameraSpaceX = maxAbsX;
			maxAbsCameraSpaceY = maxAbsY;
			crossesNearPlane = minForward <= NEAR_PLANE_EPSILON && maxForward >= -NEAR_PLANE_EPSILON;
			hasCornerBehindAndAhead = minForward < 0 && maxForward > 0;
			riskScore = computeRiskScore(useDefaultOffset, crossesNearPlane, centerForwardZ, distanceFromCamera, maxAbsX, maxAbsY);
			sampleReason = describeSampleReason(crossesNearPlane, centerForwardZ);
		}

		private void recordDraw(Vector3d offset, int instanceCount, boolean skipCameraOffset, boolean forceNoCull, boolean forceWhiteCutout) {
			drawn = true;
			this.instanceCount = instanceCount;
			this.skipCameraOffset = skipCameraOffset;
			this.forceNoCull = forceNoCull;
			this.forceWhiteCutout = forceWhiteCutout;
			cameraOffsetX = offset.getXMapped();
			cameraOffsetY = offset.getYMapped();
			cameraOffsetZ = offset.getZMapped();

			final Matrix4f adjustedMatrix = createMatrix(hasPreparedDrawMatrix ? preparedDrawMatrix : queuedMatrix);

			drawTranslationX = adjustedMatrix.m30();
			drawTranslationY = adjustedMatrix.m31();
			drawTranslationZ = adjustedMatrix.m32();
			updateNormalReferenceDrawMatrix();

			final Camera camera = MinecraftClient.getInstance().getGameRendererMapped().getCamera();
			if (camera != null) {
				final Vector3d cameraRelativeCenter = new Vector3d(worldCenterX, worldCenterY, worldCenterZ).subtract(camera.getPos());
				relativeCenterX = cameraRelativeCenter.getXMapped();
				relativeCenterY = cameraRelativeCenter.getYMapped();
				relativeCenterZ = cameraRelativeCenter.getZMapped();
				final Vector3d cameraSpaceCenter = new Vector3d(relativeCenterX, relativeCenterY, relativeCenterZ)
						.rotateY((float) Math.toRadians(camera.getYaw()))
						.rotateX((float) Math.toRadians(camera.getPitch()));
				cameraSpaceCenterX = cameraSpaceCenter.getXMapped();
				cameraSpaceCenterY = cameraSpaceCenter.getYMapped();
				cameraSpaceCenterZ = cameraSpaceCenter.getZMapped();
				distanceFromCamera = Math.sqrt(relativeCenterX * relativeCenterX + relativeCenterY * relativeCenterY + relativeCenterZ * relativeCenterZ);
				final Vector3f adjustedCenter = adjustedMatrix.transformPosition(new Vector3f(centerX, centerY, centerZ));
				final Vector3d adjustedRelativeCenter = useDefaultOffset && !skipCameraOffset ? new Vector3d(adjustedCenter.x, adjustedCenter.y, adjustedCenter.z) : new Vector3d(adjustedCenter.x, adjustedCenter.y, adjustedCenter.z).subtract(camera.getPos());
				final Vector3d adjustedCameraSpaceCenter = new Vector3d(adjustedRelativeCenter.getXMapped(), adjustedRelativeCenter.getYMapped(), adjustedRelativeCenter.getZMapped())
						.rotateY((float) Math.toRadians(camera.getYaw()))
						.rotateX((float) Math.toRadians(camera.getPitch()));
				postOffsetForwardZ = adjustedCameraSpaceCenter.getZMapped();
				postOffsetDistance = Math.sqrt(
						adjustedRelativeCenter.getXMapped() * adjustedRelativeCenter.getXMapped() +
								adjustedRelativeCenter.getYMapped() * adjustedRelativeCenter.getYMapped() +
								adjustedRelativeCenter.getZMapped() * adjustedRelativeCenter.getZMapped()
				);
			}

			hasNonFiniteValues = !isFinite(worldOriginX) || !isFinite(worldOriginY) || !isFinite(worldOriginZ) || !isFinite(worldCenterX) || !isFinite(worldCenterY) || !isFinite(worldCenterZ) || !isFinite(drawTranslationX) || !isFinite(drawTranslationY) || !isFinite(drawTranslationZ) || !isFinite(relativeCenterX) || !isFinite(relativeCenterY) || !isFinite(relativeCenterZ) || !isFinite(cameraSpaceCenterX) || !isFinite(cameraSpaceCenterY) || !isFinite(cameraSpaceCenterZ);
			hasHugeCoordinates = isHuge(worldOriginX) || isHuge(worldOriginY) || isHuge(worldOriginZ) || isHuge(worldCenterX) || isHuge(worldCenterY) || isHuge(worldCenterZ) || isHuge(drawTranslationX) || isHuge(drawTranslationY) || isHuge(drawTranslationZ);
		}

		private String formatQueuedMatrix() {
			return formatMatrix(queuedMatrix);
		}

		void setPreparedDrawMatrix(Matrix4f matrix) {
			storeMatrix(matrix, preparedDrawMatrix);
			hasPreparedDrawMatrix = true;
		}

		void setInstanceColor(int instanceColor) {
			this.instanceColor = instanceColor;
		}

		void setSingleDrawReferenceMatrix(Matrix4f matrix) {
			storeMatrix(matrix, singleDrawReferenceMatrix);
			hasSingleDrawReferenceMatrix = true;
		}

		void setStaticMatchedReferenceMatrix(Matrix4f matrix) {
			storeMatrix(matrix, staticMatchedReferenceMatrix);
			hasStaticMatchedReferenceMatrix = true;
		}

		public void setNormalReference(@Nullable OptimizedModelWrapper model, StoredMatrixTransformations transformations, Matrix4f matrix, boolean matched, String sampleId) {
			normalReferenceModel = model;
			normalReferenceTransformations = transformations;
			storeMatrix(matrix, normalReferenceMatrix);
			hasNormalReferenceMatrix = true;
			normalReferenceMatched = matched;
			normalReferenceSampleId = sampleId;
			final Vector3f normalOrigin = matrix.transformPosition(new Vector3f());
			final Vector3f normalCenter = matrix.transformPosition(new Vector3f(centerX, centerY, centerZ));
			normalReferenceWorldOriginX = normalOrigin.x;
			normalReferenceWorldOriginY = normalOrigin.y;
			normalReferenceWorldOriginZ = normalOrigin.z;
			normalReferenceWorldCenterX = normalCenter.x;
			normalReferenceWorldCenterY = normalCenter.y;
			normalReferenceWorldCenterZ = normalCenter.z;
			updateNormalReferenceDrawMatrix();
		}

		private void setVaoAttributeState(String vaoAttributeState) {
			this.vaoAttributeState = vaoAttributeState;
		}

		boolean isDrawn() {
			return drawn;
		}

		boolean hasNormalReferenceRenderable() {
			return normalReferenceModel != null && normalReferenceTransformations != null;
		}

		StaticObjMesh getStaticObjMesh() {
			return staticObjMesh;
		}

		OptimizedModel.ShaderType getShaderTypeEnum() {
			return shaderTypeEnum;
		}

		String getShaderType() {
			return shaderType;
		}

		String getTextureId() {
			return textureId;
		}

		String getSourceSampleId() {
			return normalReferenceSampleId;
		}

		Matrix4f getPreparedDrawMatrix() {
			return createMatrix(hasPreparedDrawMatrix ? preparedDrawMatrix : queuedMatrix);
		}

		private void updateNormalReferenceDrawMatrix() {
			if (!hasNormalReferenceMatrix) {
				hasNormalReferenceDrawMatrix = false;
				return;
			}

			final Matrix4f adjustedMatrix = createMatrix(normalReferenceMatrix);
			if (useDefaultOffset && drawn && !skipCameraOffset) {
				adjustedMatrix.m30(adjustedMatrix.m30() - (float) cameraOffsetX);
				adjustedMatrix.m31(adjustedMatrix.m31() - (float) cameraOffsetY);
				adjustedMatrix.m32(adjustedMatrix.m32() - (float) cameraOffsetZ);
			}
			storeMatrix(adjustedMatrix, normalReferenceDrawMatrix);
			hasNormalReferenceDrawMatrix = true;
		}

		@Nullable
		OptimizedModelWrapper getNormalReferenceModel() {
			return normalReferenceModel;
		}

		@Nullable
		StoredMatrixTransformations getNormalReferenceTransformations() {
			return normalReferenceTransformations;
		}

		boolean hasSingleDrawReferenceMatrix() {
			return hasSingleDrawReferenceMatrix;
		}

		boolean hasNormalReferenceMatrix() {
			return hasNormalReferenceMatrix;
		}

		boolean hasNormalReferenceDrawMatrix() {
			return hasNormalReferenceDrawMatrix;
		}

		boolean hasStaticMatchedReferenceMatrix() {
			return hasStaticMatchedReferenceMatrix;
		}

		String formatPreparedDrawMatrix() {
			return formatMatrix(hasPreparedDrawMatrix ? preparedDrawMatrix : queuedMatrix);
		}

		String formatSingleDrawReferenceMatrix() {
			return formatMatrix(singleDrawReferenceMatrix);
		}

		String formatNormalReferenceMatrix() {
			return formatMatrix(normalReferenceMatrix);
		}

		String formatNormalReferenceDrawMatrix() {
			return formatMatrix(hasNormalReferenceDrawMatrix ? normalReferenceDrawMatrix : normalReferenceMatrix);
		}

		String formatStaticMatchedReferenceMatrix() {
			return formatMatrix(staticMatchedReferenceMatrix);
		}

		double getSingleDrawReferenceTranslationDeltaX() {
			return singleDrawReferenceMatrix[12] - (hasPreparedDrawMatrix ? preparedDrawMatrix[12] : queuedMatrix[12]);
		}

		double getSingleDrawReferenceTranslationDeltaY() {
			return singleDrawReferenceMatrix[13] - (hasPreparedDrawMatrix ? preparedDrawMatrix[13] : queuedMatrix[13]);
		}

		double getSingleDrawReferenceTranslationDeltaZ() {
			return singleDrawReferenceMatrix[14] - (hasPreparedDrawMatrix ? preparedDrawMatrix[14] : queuedMatrix[14]);
		}

		double getSingleDrawReferenceMaxAbsEntryDelta() {
			double max = 0;
			final float[] reference = hasPreparedDrawMatrix ? preparedDrawMatrix : queuedMatrix;
			for (int i = 0; i < singleDrawReferenceMatrix.length; i++) {
				max = Math.max(max, Math.abs(singleDrawReferenceMatrix[i] - reference[i]));
			}
			return max;
		}

		double getQueuedVsNormalReferenceTranslationDeltaX() {
			return normalReferenceMatrix[12] - queuedMatrix[12];
		}

		double getQueuedVsNormalReferenceTranslationDeltaY() {
			return normalReferenceMatrix[13] - queuedMatrix[13];
		}

		double getQueuedVsNormalReferenceTranslationDeltaZ() {
			return normalReferenceMatrix[14] - queuedMatrix[14];
		}

		double getQueuedVsNormalReferenceMaxAbsEntryDelta() {
			double max = 0;
			for (int i = 0; i < normalReferenceMatrix.length; i++) {
				max = Math.max(max, Math.abs(normalReferenceMatrix[i] - queuedMatrix[i]));
			}
			return max;
		}

		double getNormalReferenceDrawTranslationDeltaX() {
			return normalReferenceDrawMatrix[12] - (hasPreparedDrawMatrix ? preparedDrawMatrix[12] : queuedMatrix[12]);
		}

		double getNormalReferenceDrawTranslationDeltaY() {
			return normalReferenceDrawMatrix[13] - (hasPreparedDrawMatrix ? preparedDrawMatrix[13] : queuedMatrix[13]);
		}

		double getNormalReferenceDrawTranslationDeltaZ() {
			return normalReferenceDrawMatrix[14] - (hasPreparedDrawMatrix ? preparedDrawMatrix[14] : queuedMatrix[14]);
		}

		double getNormalReferenceDrawMaxAbsEntryDelta() {
			double max = 0;
			final float[] reference = hasPreparedDrawMatrix ? preparedDrawMatrix : queuedMatrix;
			for (int i = 0; i < normalReferenceDrawMatrix.length; i++) {
				max = Math.max(max, Math.abs(normalReferenceDrawMatrix[i] - reference[i]));
			}
			return max;
		}

		double getSingleDrawVsNormalReferenceDrawTranslationDeltaX() {
			return normalReferenceDrawMatrix[12] - singleDrawReferenceMatrix[12];
		}

		double getSingleDrawVsNormalReferenceDrawTranslationDeltaY() {
			return normalReferenceDrawMatrix[13] - singleDrawReferenceMatrix[13];
		}

		double getSingleDrawVsNormalReferenceDrawTranslationDeltaZ() {
			return normalReferenceDrawMatrix[14] - singleDrawReferenceMatrix[14];
		}

		double getSingleDrawVsNormalReferenceDrawMaxAbsEntryDelta() {
			double max = 0;
			for (int i = 0; i < normalReferenceDrawMatrix.length; i++) {
				max = Math.max(max, Math.abs(normalReferenceDrawMatrix[i] - singleDrawReferenceMatrix[i]));
			}
			return max;
		}

		double getStaticMatchedReferenceTranslationDeltaX() {
			return staticMatchedReferenceMatrix[12] - (hasPreparedDrawMatrix ? preparedDrawMatrix[12] : queuedMatrix[12]);
		}

		double getStaticMatchedReferenceTranslationDeltaY() {
			return staticMatchedReferenceMatrix[13] - (hasPreparedDrawMatrix ? preparedDrawMatrix[13] : queuedMatrix[13]);
		}

		double getStaticMatchedReferenceTranslationDeltaZ() {
			return staticMatchedReferenceMatrix[14] - (hasPreparedDrawMatrix ? preparedDrawMatrix[14] : queuedMatrix[14]);
		}

		double getStaticMatchedReferenceMaxAbsEntryDelta() {
			double max = 0;
			final float[] reference = hasPreparedDrawMatrix ? preparedDrawMatrix : queuedMatrix;
			for (int i = 0; i < staticMatchedReferenceMatrix.length; i++) {
				max = Math.max(max, Math.abs(staticMatchedReferenceMatrix[i] - reference[i]));
			}
			return max;
		}

		double getStaticMatchedVsNormalReferenceDrawTranslationDeltaX() {
			return normalReferenceDrawMatrix[12] - staticMatchedReferenceMatrix[12];
		}

		double getStaticMatchedVsNormalReferenceDrawTranslationDeltaY() {
			return normalReferenceDrawMatrix[13] - staticMatchedReferenceMatrix[13];
		}

		double getStaticMatchedVsNormalReferenceDrawTranslationDeltaZ() {
			return normalReferenceDrawMatrix[14] - staticMatchedReferenceMatrix[14];
		}

		double getStaticMatchedVsNormalReferenceDrawMaxAbsEntryDelta() {
			double max = 0;
			for (int i = 0; i < normalReferenceDrawMatrix.length; i++) {
				max = Math.max(max, Math.abs(normalReferenceDrawMatrix[i] - staticMatchedReferenceMatrix[i]));
			}
			return max;
		}

		MaterialProperties createMatchedMaterialProperties() {
			return new MaterialProperties(shaderTypeEnum, texture, null);
		}

		private boolean shouldReplace(DiagnosticSample other) {
			if (useDefaultOffset != other.useDefaultOffset) {
				return useDefaultOffset;
			}
			if (source == Source.RAIL) {
				if (crossesNearPlane != other.crossesNearPlane) {
					return !crossesNearPlane;
				}
				if (hasCornerBehindAndAhead != other.hasCornerBehindAndAhead) {
					return !hasCornerBehindAndAhead;
				}
				if ((centerForwardZ > NEAR_PLANE_EPSILON) != (other.centerForwardZ > NEAR_PLANE_EPSILON)) {
					return centerForwardZ > NEAR_PLANE_EPSILON;
				}
				return distanceFromCamera < other.distanceFromCamera;
			}
			if (crossesNearPlane != other.crossesNearPlane) {
				return crossesNearPlane;
			}
			if (hasCornerBehindAndAhead != other.hasCornerBehindAndAhead) {
				return hasCornerBehindAndAhead;
			}
			if ((centerForwardZ > 0) != (other.centerForwardZ > 0)) {
				return centerForwardZ > 0;
			}
			if (Double.compare(riskScore, other.riskScore) != 0) {
				return riskScore > other.riskScore;
			}
			return distanceFromCamera < other.distanceFromCamera;
		}
	}

	private static final class TimedSnapshot {

		private final long timeMillis;
		private final Snapshot snapshot;

		private TimedSnapshot(long timeMillis, Snapshot snapshot) {
			this.timeMillis = timeMillis;
			this.snapshot = snapshot;
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
		private long vehicleEligibleParts;
		private long vehicleGpuParts;
		private long vehicleFallbackParts;
		private long vehicleConditionFilteredParts;
		private long vehicleGpuQueues;
		private final long[] railFallbackReasons = new long[RailFallbackReason.values().length];
		private final long[] vehicleFallbackReasons = new long[VehicleFallbackReason.values().length];

		private void add(Snapshot snapshot) {
			instancesTotal += snapshot.instancesTotal;
			railInstances += snapshot.railInstances;
			vehicleInstances += snapshot.vehicleInstances;
			activeBatches += snapshot.activeBatches;
			activeMeshes += snapshot.activeMeshes;
			instancedDraws += snapshot.instancedDraws;
			railQueueNanos += snapshot.railQueueNanos;
			vehicleQueueNanos += snapshot.vehicleQueueNanos;
			renderOpaqueNanos += snapshot.renderOpaqueNanos;
			instanceUploadNanos += snapshot.instanceUploadNanos;
			instanceDrawNanos += snapshot.instanceDrawNanos;
			railAttemptedSegments += snapshot.railAttemptedSegments;
			railGpuQueuedSegments += snapshot.railGpuQueuedSegments;
			railFallbackSegments += snapshot.railFallbackSegments;
			vehicleEligibleParts += snapshot.vehicleEligibleParts;
			vehicleGpuParts += snapshot.vehicleGpuParts;
			vehicleFallbackParts += snapshot.vehicleFallbackParts;
			vehicleConditionFilteredParts += snapshot.vehicleConditionFilteredParts;
			vehicleGpuQueues += snapshot.vehicleGpuQueues;
			addArrays(railFallbackReasons, snapshot.railFallbackReasons);
			addArrays(vehicleFallbackReasons, snapshot.vehicleFallbackReasons);
		}

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
			vehicleEligibleParts = 0;
			vehicleGpuParts = 0;
			vehicleFallbackParts = 0;
			vehicleConditionFilteredParts = 0;
			vehicleGpuQueues = 0;
			Arrays.fill(railFallbackReasons, 0);
			Arrays.fill(vehicleFallbackReasons, 0);
		}

		private void copyFrom(Snapshot snapshot) {
			instancesTotal = snapshot.instancesTotal;
			railInstances = snapshot.railInstances;
			vehicleInstances = snapshot.vehicleInstances;
			activeBatches = snapshot.activeBatches;
			activeMeshes = snapshot.activeMeshes;
			instancedDraws = snapshot.instancedDraws;
			railQueueNanos = snapshot.railQueueNanos;
			vehicleQueueNanos = snapshot.vehicleQueueNanos;
			renderOpaqueNanos = snapshot.renderOpaqueNanos;
			instanceUploadNanos = snapshot.instanceUploadNanos;
			instanceDrawNanos = snapshot.instanceDrawNanos;
			railAttemptedSegments = snapshot.railAttemptedSegments;
			railGpuQueuedSegments = snapshot.railGpuQueuedSegments;
			railFallbackSegments = snapshot.railFallbackSegments;
			vehicleEligibleParts = snapshot.vehicleEligibleParts;
			vehicleGpuParts = snapshot.vehicleGpuParts;
			vehicleFallbackParts = snapshot.vehicleFallbackParts;
			vehicleConditionFilteredParts = snapshot.vehicleConditionFilteredParts;
			vehicleGpuQueues = snapshot.vehicleGpuQueues;
			System.arraycopy(snapshot.railFallbackReasons, 0, railFallbackReasons, 0, railFallbackReasons.length);
			System.arraycopy(snapshot.vehicleFallbackReasons, 0, vehicleFallbackReasons, 0, vehicleFallbackReasons.length);
		}

		private Snapshot copy() {
			final Snapshot snapshot = new Snapshot();
			snapshot.copyFrom(this);
			return snapshot;
		}

		private static void addArrays(long[] target, long[] source) {
			for (int i = 0; i < target.length; i++) {
				target[i] += source[i];
			}
		}
	}

	private static void storeMatrix(Matrix4f matrix, float[] target) {
		target[0] = matrix.m00();
		target[1] = matrix.m01();
		target[2] = matrix.m02();
		target[3] = matrix.m03();
		target[4] = matrix.m10();
		target[5] = matrix.m11();
		target[6] = matrix.m12();
		target[7] = matrix.m13();
		target[8] = matrix.m20();
		target[9] = matrix.m21();
		target[10] = matrix.m22();
		target[11] = matrix.m23();
		target[12] = matrix.m30();
		target[13] = matrix.m31();
		target[14] = matrix.m32();
		target[15] = matrix.m33();
	}

	private static String formatMatrix(float[] values) {
		return String.format(
				"[%.5f %.5f %.5f %.5f | %.5f %.5f %.5f %.5f | %.5f %.5f %.5f %.5f | %.5f %.5f %.5f %.5f]",
				values[0], values[1], values[2], values[3],
				values[4], values[5], values[6], values[7],
				values[8], values[9], values[10], values[11],
				values[12], values[13], values[14], values[15]
		);
	}

	private static Matrix4f createMatrix(float[] values) {
		return new Matrix4f()
				.m00(values[0]).m01(values[1]).m02(values[2]).m03(values[3])
				.m10(values[4]).m11(values[5]).m12(values[6]).m13(values[7])
				.m20(values[8]).m21(values[9]).m22(values[10]).m23(values[11])
				.m30(values[12]).m31(values[13]).m32(values[14]).m33(values[15]);
	}

	private static boolean isFinite(double value) {
		return !Double.isNaN(value) && !Double.isInfinite(value);
	}

	private static boolean isHuge(double value) {
		return Math.abs(value) > 1_000_000;
	}

	private static void scheduleDiagnosticRender(@Nullable DiagnosticSample diagnosticSample, int color) {
		if (diagnosticSample == null) {
			return;
		}

		MainRenderer.scheduleRender(QueuedRenderLayer.LINES, (graphicsHolder, offset) -> {
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 0, 1, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 1, 2, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 2, 3, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 3, 0, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 4, 5, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 5, 6, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 6, 7, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 7, 4, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 0, 4, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 1, 5, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 2, 6, color);
			drawEdge(graphicsHolder, diagnosticSample.worldCorners, offset, 3, 7, color);
		});
	}

	private static void scheduleNormalReferenceRender(@Nullable DiagnosticSample diagnosticSample, int color) {
		if (diagnosticSample == null || !diagnosticSample.isDrawn() || !diagnosticSample.hasNormalReferenceRenderable()) {
			return;
		}

		final OptimizedModelWrapper normalReferenceModel = diagnosticSample.getNormalReferenceModel();
		final StoredMatrixTransformations normalReferenceTransformations = diagnosticSample.getNormalReferenceTransformations();
		if (normalReferenceModel == null || normalReferenceTransformations == null) {
			return;
		}

		MainRenderer.scheduleRender(QueuedRenderLayer.TEXT, (graphicsHolder, offset) -> {
			normalReferenceTransformations.transform(graphicsHolder, offset);
			CustomResourceLoader.OPTIMIZED_RENDERER_WRAPPER.queue(normalReferenceModel, graphicsHolder, color, GraphicsHolder.getDefaultLight());
			graphicsHolder.pop();
		});
	}

	private static void drawEdge(org.mtr.mapping.mapper.GraphicsHolder graphicsHolder, Vector3d[] corners, Vector3d offset, int index1, int index2, int color) {
		final Vector3d corner1 = corners[index1];
		final Vector3d corner2 = corners[index2];
		if (corner1 == null || corner2 == null) {
			return;
		}
		graphicsHolder.drawLineInWorld(
				(float) (corner1.getXMapped() - offset.getXMapped()),
				(float) (corner1.getYMapped() - offset.getYMapped()),
				(float) (corner1.getZMapped() - offset.getZMapped()),
				(float) (corner2.getXMapped() - offset.getXMapped()),
				(float) (corner2.getYMapped() - offset.getYMapped()),
				(float) (corner2.getZMapped() - offset.getZMapped()),
				color
		);
	}

	private static Vector3d createWorldCorner(Matrix4f matrix, StaticObjMesh staticObjMesh, int index) {
		final float localX = (index & 1) == 0 ? staticObjMesh.minX : staticObjMesh.maxX;
		final float localY = (index & 2) == 0 ? staticObjMesh.minY : staticObjMesh.maxY;
		final float localZ = (index & 4) == 0 ? staticObjMesh.minZ : staticObjMesh.maxZ;
		final Vector3f transformed = matrix.transformPosition(new Vector3f(localX, localY, localZ));
		return new Vector3d(transformed.x, transformed.y, transformed.z);
	}

	private static Vector3d toCameraSpace(Vector3d worldPosition, Camera camera) {
		return new Vector3d(worldPosition.getXMapped(), worldPosition.getYMapped(), worldPosition.getZMapped())
				.subtract(camera.getPos())
				.rotateY((float) Math.toRadians(camera.getYaw()))
				.rotateX((float) Math.toRadians(camera.getPitch()));
	}

	private static double computeRiskScore(boolean useDefaultOffset, boolean crossesNearPlane, double centerForwardZ, double distance, double maxAbsX, double maxAbsY) {
		double score = 0;
		if (useDefaultOffset) {
			score += 1_000_000;
		}
		if (crossesNearPlane) {
			score += 500_000;
		}
		score += 100_000 / (1 + Math.abs(centerForwardZ));
		score += 10_000 / (1 + distance);
		score += Math.min(10_000, maxAbsX + maxAbsY);
		return score;
	}

	private static String describeSampleReason(boolean crossesNearPlane, double centerForwardZ) {
		if (crossesNearPlane) {
			return "near_plane_crossing";
		}
		if (centerForwardZ > 0) {
			return "nearest_visible";
		}
		return "closest_fallback_candidate";
	}

	private static double distance(double x, double y, double z, Vector3d cameraPosition) {
		final double dx = x - cameraPosition.getXMapped();
		final double dy = y - cameraPosition.getYMapped();
		final double dz = z - cameraPosition.getZMapped();
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}
}
