package org.mtr.mod.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.Camera;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Vector3d;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;

import javax.annotation.Nullable;
import java.util.Arrays;

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
	}

	public static void disableDiagnostics() {
		diagnosticEnabled = false;
		diagnosticSkipCameraOffset = false;
		diagnosticForceNoCull = false;
		diagnosticForceWhiteCutout = false;
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

	public static void scheduleDiagnosticRender() {
		if (!diagnosticEnabled) {
			return;
		}

		scheduleDiagnosticRender(currentRailDiagnosticSample, RAIL_SAMPLE_COLOR);
		scheduleDiagnosticRender(currentVehicleDiagnosticSample, VEHICLE_SAMPLE_COLOR);
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
		lines.add(String.format("%s rail attempted/gpu/fallback: %d/%d/%d", label, snapshot.railAttemptedSegments, snapshot.railGpuQueuedSegments, snapshot.railFallbackSegments));
		lines.add(String.format("%s rail fallback reasons: %s", label, formatReasons(snapshot.railFallbackReasons, RailFallbackReason.values())));
		lines.add(String.format("%s vehicle eligible/gpu/fallback/filtered: %d/%d/%d/%d", label, snapshot.vehicleEligibleParts, snapshot.vehicleGpuParts, snapshot.vehicleFallbackParts, snapshot.vehicleConditionFilteredParts));
		lines.add(String.format("%s vehicle gpu queue calls: %d", label, snapshot.vehicleGpuQueues));
		lines.add(String.format("%s vehicle fallback reasons: %s", label, formatReasons(snapshot.vehicleFallbackReasons, VehicleFallbackReason.values())));
	}

	private static void appendDiagnostic(ObjectArrayList<String> lines) {
		if (!diagnosticEnabled && lastRailDiagnosticSample == null && lastVehicleDiagnosticSample == null) {
			return;
		}

		lines.add(String.format("Diagnostic enabled: %s | skipCameraOffset: %s | forceNoCull: %s | forceWhiteCutout: %s", diagnosticEnabled, diagnosticSkipCameraOffset, diagnosticForceNoCull, diagnosticForceWhiteCutout));
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
		lines.add(String.format("%s queued matrix: %s", label, diagnosticSample.formatQueuedMatrix()));
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
		private final String renderStage;
		private final String shaderType;
		private final int vertexCount;
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
		private boolean drawn;
		private boolean skipCameraOffset;
		private boolean forceNoCull;
		private boolean forceWhiteCutout;
		private boolean hasNonFiniteValues;
		private boolean hasHugeCoordinates;

		private DiagnosticSample(Source source, ObjBatchKey batchKey, StaticObjMesh staticObjMesh, Matrix4f matrix, boolean useDefaultOffset) {
			this.source = source;
			textureId = String.valueOf(batchKey.texture);
			renderStage = batchKey.renderStage.name();
			shaderType = batchKey.shaderType.name();
			vertexCount = staticObjMesh.vertexCount;
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

			final Matrix4f adjustedMatrix = new Matrix4f()
					.m00(queuedMatrix[0]).m01(queuedMatrix[1]).m02(queuedMatrix[2]).m03(queuedMatrix[3])
					.m10(queuedMatrix[4]).m11(queuedMatrix[5]).m12(queuedMatrix[6]).m13(queuedMatrix[7])
					.m20(queuedMatrix[8]).m21(queuedMatrix[9]).m22(queuedMatrix[10]).m23(queuedMatrix[11])
					.m30(queuedMatrix[12]).m31(queuedMatrix[13]).m32(queuedMatrix[14]).m33(queuedMatrix[15]);
			if (useDefaultOffset && !skipCameraOffset) {
				adjustedMatrix.m30(adjustedMatrix.m30() - (float) cameraOffsetX);
				adjustedMatrix.m31(adjustedMatrix.m31() - (float) cameraOffsetY);
				adjustedMatrix.m32(adjustedMatrix.m32() - (float) cameraOffsetZ);
			}

			drawTranslationX = adjustedMatrix.m30();
			drawTranslationY = adjustedMatrix.m31();
			drawTranslationZ = adjustedMatrix.m32();

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
			return String.format(
					"[%.5f %.5f %.5f %.5f | %.5f %.5f %.5f %.5f | %.5f %.5f %.5f %.5f | %.5f %.5f %.5f %.5f]",
					queuedMatrix[0], queuedMatrix[1], queuedMatrix[2], queuedMatrix[3],
					queuedMatrix[4], queuedMatrix[5], queuedMatrix[6], queuedMatrix[7],
					queuedMatrix[8], queuedMatrix[9], queuedMatrix[10], queuedMatrix[11],
					queuedMatrix[12], queuedMatrix[13], queuedMatrix[14], queuedMatrix[15]
			);
		}

		private boolean shouldReplace(DiagnosticSample other) {
			if (useDefaultOffset != other.useDefaultOffset) {
				return useDefaultOffset;
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
