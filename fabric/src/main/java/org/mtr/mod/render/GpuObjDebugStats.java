package org.mtr.mod.render;

import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;

import java.util.function.Consumer;

public final class GpuObjDebugStats {

	public enum Source {RAIL, VEHICLE}

	private static final long WATCH_INTERVAL_MILLIS = 1000;
	private static final Snapshot CURRENT_FRAME = new Snapshot();
	private static final Snapshot LAST_FRAME = new Snapshot();
	private static final Snapshot RELOAD_SESSION = new Snapshot();
	private static final Snapshot WATCH_SESSION = new Snapshot();
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

	public static void recordRailQueueResult(boolean success) {
		if (success) {
			CURRENT_FRAME.railGpuSegments++;
		} else {
			CURRENT_FRAME.railFallbackSegments++;
		}
	}

	public static void recordVehicleGpuQueue(int gpuPartCount) {
		if (gpuPartCount > 0) {
			CURRENT_FRAME.vehicleGpuQueues++;
			CURRENT_FRAME.vehicleGpuParts += gpuPartCount;
		}
	}

	public static void recordVehicleFallbackModels(int fallbackModelCount) {
		if (fallbackModelCount > 0) {
			CURRENT_FRAME.vehicleFallbackModels += fallbackModelCount;
		}
	}

	public static void finishFrame() {
		LAST_FRAME.copyFrom(CURRENT_FRAME);
		RELOAD_SESSION.add(CURRENT_FRAME);
		if (watchActive) {
			WATCH_SESSION.add(CURRENT_FRAME);
		}
	}

	public static void resetSession() {
		CURRENT_FRAME.clear();
		LAST_FRAME.clear();
		RELOAD_SESSION.clear();
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
		appendSnapshot(lines, "Frame", LAST_FRAME, true);
		appendSnapshot(lines, "Session (since reload)", RELOAD_SESSION, true);
		if (watchActive) {
			appendSnapshot(lines, "Watch (since start)", WATCH_SESSION, false);
		}
		return lines;
	}

	private static void appendSnapshot(ObjectArrayList<String> lines, String label, Snapshot snapshot, boolean includeRailVehicleRates) {
		lines.add(String.format("%s instances total/rails/vehicles: %d/%d/%d", label, snapshot.instancesTotal, snapshot.railInstances, snapshot.vehicleInstances));
		lines.add(String.format("%s active batches/meshes/instanced draws: %d/%d/%d", label, snapshot.activeBatches, snapshot.activeMeshes, snapshot.instancedDraws));
		if (includeRailVehicleRates) {
			lines.add(String.format("%s rail gpu/fallback: %d/%d (%s gpu)", label, snapshot.railGpuSegments, snapshot.railFallbackSegments, formatRatio(snapshot.railGpuSegments, snapshot.railGpuSegments + snapshot.railFallbackSegments)));
			lines.add(String.format("%s vehicle gpu parts/queues/fallback models: %d/%d/%d", label, snapshot.vehicleGpuParts, snapshot.vehicleGpuQueues, snapshot.vehicleFallbackModels));
		} else {
			lines.add(String.format("%s rail gpu/fallback: %d/%d (%s gpu)", label, snapshot.railGpuSegments, snapshot.railFallbackSegments, formatRatio(snapshot.railGpuSegments, snapshot.railGpuSegments + snapshot.railFallbackSegments)));
			lines.add(String.format("%s vehicle gpu parts/queues/fallback models: %d/%d/%d", label, snapshot.vehicleGpuParts, snapshot.vehicleGpuQueues, snapshot.vehicleFallbackModels));
		}
	}

	private static String formatRatio(long value, long total) {
		if (total <= 0) {
			return "n/a";
		}
		return String.format("%.1f%%", value * 100D / total);
	}

	private static void sendChatMessage(String line) {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null) {
			clientPlayerEntity.sendMessage(new Text(TextHelper.literal(line).data), false);
		}
	}

	private static final class Snapshot {

		private long instancesTotal;
		private long railInstances;
		private long vehicleInstances;
		private long activeBatches;
		private long activeMeshes;
		private long instancedDraws;
		private long railGpuSegments;
		private long railFallbackSegments;
		private long vehicleGpuParts;
		private long vehicleGpuQueues;
		private long vehicleFallbackModels;

		private void add(Snapshot snapshot) {
			instancesTotal += snapshot.instancesTotal;
			railInstances += snapshot.railInstances;
			vehicleInstances += snapshot.vehicleInstances;
			activeBatches += snapshot.activeBatches;
			activeMeshes += snapshot.activeMeshes;
			instancedDraws += snapshot.instancedDraws;
			railGpuSegments += snapshot.railGpuSegments;
			railFallbackSegments += snapshot.railFallbackSegments;
			vehicleGpuParts += snapshot.vehicleGpuParts;
			vehicleGpuQueues += snapshot.vehicleGpuQueues;
			vehicleFallbackModels += snapshot.vehicleFallbackModels;
		}

		private void clear() {
			instancesTotal = 0;
			railInstances = 0;
			vehicleInstances = 0;
			activeBatches = 0;
			activeMeshes = 0;
			instancedDraws = 0;
			railGpuSegments = 0;
			railFallbackSegments = 0;
			vehicleGpuParts = 0;
			vehicleGpuQueues = 0;
			vehicleFallbackModels = 0;
		}

		private void copyFrom(Snapshot snapshot) {
			instancesTotal = snapshot.instancesTotal;
			railInstances = snapshot.railInstances;
			vehicleInstances = snapshot.vehicleInstances;
			activeBatches = snapshot.activeBatches;
			activeMeshes = snapshot.activeMeshes;
			instancedDraws = snapshot.instancedDraws;
			railGpuSegments = snapshot.railGpuSegments;
			railFallbackSegments = snapshot.railFallbackSegments;
			vehicleGpuParts = snapshot.vehicleGpuParts;
			vehicleGpuQueues = snapshot.vehicleGpuQueues;
			vehicleFallbackModels = snapshot.vehicleFallbackModels;
		}
	}
}
