package org.mtr.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;

import java.util.Arrays;
import java.util.function.Consumer;

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
	private static final Snapshot CURRENT_FRAME = new Snapshot();
	private static final Snapshot LAST_FRAME = new Snapshot();
	private static final Snapshot WINDOW_SNAPSHOT = new Snapshot();
	private static final Snapshot RELOAD_SESSION = new Snapshot();
	private static final Snapshot WATCH_SESSION = new Snapshot();
	private static final ObjectArrayList<TimedSnapshot> WINDOW_HISTORY = new ObjectArrayList<>();
	private static boolean instancingEnabled;
	private static boolean watchActive;
	private static long nextWatchReportMillis;

	private GpuObjDebugStats() {
	}

	public static void beginFrame(boolean newInstancingEnabled) {
		instancingEnabled = newInstancingEnabled;
		CURRENT_FRAME.clear();
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
		if (watchActive) {
			WATCH_SESSION.clear();
		}
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
		emitReport("watch", GpuObjDebugStats::sendChatMessage);
	}

	public static void emitReport(String reason, Consumer<String> chatConsumer) {
		final ObjectArrayList<String> lines = buildReportLines(reason);
		lines.forEach(line -> {
			Init.LOGGER.info(line);
			chatConsumer.accept(line);
		});
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

	private static void sendChatMessage(String line) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			clientPlayerEntity.sendMessage(new Text(TextHelper.literal(line).data), false);
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
}
