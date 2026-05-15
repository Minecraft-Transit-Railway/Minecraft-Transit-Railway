package org.mtr.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.function.Consumer;

/**
 * Background thread driving the off-main-thread part of every frame.
 *
 * <p>The thread owns:</p>
 * <ul>
 *   <li>Two single-slot {@link AtomicReference} inboxes for occlusion-culling work
 *       (vehicles / lifts). Each is refreshed every frame; the per-frame nature
 *       means superseding the previous task is the desired behaviour (last writer wins —
 *       there's no point doing culling against a stale camera position). Rail culling
 *       was removed in favour of per-rail AABB distance checks — see
 *       {@code docs/PERFORMANCE.md} §3.10.</li>
 *   <li>A bounded {@link Deque}-backed queue for dynamic-texture generation, fed by
 *       {@link org.mtr.client.DynamicTextureCache}. Up to
 *       {@value #DYNAMIC_TEXTURE_QUEUE_LIMIT} jobs may sit in flight; older jobs are
 *       discarded once the cap is hit so the queue tracks the user's current viewport
 *       and doesn't accumulate work for textures that scrolled offscreen long ago.</li>
 * </ul>
 *
 * <p>The loop parks on {@link LockSupport#park()} when there is no work, and is woken by
 * the scheduling methods. {@link #start()} is idempotent — the loop is launched once on
 * first call and survives until {@link MinecraftClient#isRunning()} returns
 * {@code false}.</p>
 *
 * <p>The {@link #worker} executor is a {@code newVirtualThreadPerTaskExecutor}, suitable
 * for one-shot async submissions (model parsing, IO-bound resource decoding).</p>
 *
 * <p>Performance details for the queueing strategy are documented in
 * {@code docs/PERFORMANCE.md} §2.1 and §4.2.</p>
 */
public final class WorkerThread {

	private static final int MAX_OCCLUSION_CHUNK_DISTANCE = 32;
	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance = new OcclusionCullingInstance(1, new CullingDataProvider());
	private boolean canStart = true;
	private int runCooldown;

	public final ExecutorService worker = Executors.newVirtualThreadPerTaskExecutor();
	private final AtomicReference<@Nullable Consumer<OcclusionCullingInstance>> occlusionQueueVehicle = new AtomicReference<>();
	private final AtomicReference<@Nullable Consumer<OcclusionCullingInstance>> occlusionQueueLift = new AtomicReference<>();
	/**
	 * Bounded dynamic-texture inbox. Drained FIFO every loop iteration up to
	 * {@value #DYNAMIC_TEXTURE_BATCH} jobs per pass; further work waits for the next pass
	 * so the worker remains responsive to occlusion-culling refreshes.
	 */
	private final Deque<Runnable> dynamicTextureQueue = new ArrayDeque<>();
	private final Object dynamicTextureLock = new Object();
	@Nullable
	private volatile Thread loopThread;

	private static final int COOLDOWN = 100;
	/** Maximum number of pending dynamic-texture jobs. Older jobs are dropped on overflow. */
	private static final int DYNAMIC_TEXTURE_QUEUE_LIMIT = 256;
	/** Maximum jobs drained per loop iteration, to keep occlusion culling responsive. */
	private static final int DYNAMIC_TEXTURE_BATCH = 8;
	/** Worker idle park duration when no occlusion work is pending. */
	private static final long IDLE_PARK_NANOS = 10_000_000L; // 10 ms

	/**
	 * Idempotent loop launcher. Called once per frame from
	 * {@link org.mtr.render.MainRenderer#render}; the loop body only spawns the first time
	 * and then re-arms after a {@value #COOLDOWN}-tick idle period to recover from a
	 * Minecraft client restart without leaking the thread.
	 */
	public void start() {
		if (canStart && isRunning()) {
			canStart = false;
			worker.submit(() -> {
				loopThread = Thread.currentThread();
				while (isRunning()) {
				if (occlusionQueueVehicle.get() != null || occlusionQueueLift.get() != null) {
					updateInstance();
					occlusionCullingInstance.resetCache();
					run(occlusionQueueVehicle, task -> task.accept(occlusionCullingInstance));
					run(occlusionQueueLift, task -> task.accept(occlusionCullingInstance));
				}

					drainDynamicTextureBatch();

					// Park briefly if nothing is queued; scheduling methods unpark us
					// directly so the wakeup latency is near zero when work arrives.
					if (!hasWork()) {
						LockSupport.parkNanos(IDLE_PARK_NANOS);
					}

					runCooldown = 0;
				}
				loopThread = null;
			});
		}

		if (runCooldown == COOLDOWN) {
			canStart = true;
			runCooldown = 0;
			MTR.LOGGER.debug("Restarting worker thread");
		} else {
			runCooldown++;
		}
	}

	/** Stops the loop and cancels any in-flight virtual-thread submissions. */
	public void shutdown() {
		worker.shutdownNow();
	}

	/**
	 * Replace the pending vehicle-culling task. Overwrites any previous task that has not
	 * yet been picked up by the worker loop — only the most recent submission per frame
	 * will run.
	 */
	public void scheduleVehicles(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueueVehicle.set(consumer);
		wake();
	}

	/** See {@link #scheduleVehicles(Consumer)}. */
	public void scheduleLifts(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueueLift.set(consumer);
		wake();
	}

	/**
	 * Queue a dynamic-texture-generation task. Tasks are processed FIFO; if more than
	 * {@value #DYNAMIC_TEXTURE_QUEUE_LIMIT} jobs are pending the oldest is discarded.
	 *
	 * <p>Replaces the historical single-slot {@code AtomicReference}-based queue that
	 * silently dropped all but the most recent submission per frame — see
	 * {@code docs/PERFORMANCE.md} §2.1.</p>
	 */
	public void scheduleDynamicTextures(Runnable runnable) {
		synchronized (dynamicTextureLock) {
			while (dynamicTextureQueue.size() >= DYNAMIC_TEXTURE_QUEUE_LIMIT) {
				dynamicTextureQueue.pollFirst();
			}
			dynamicTextureQueue.addLast(runnable);
		}
		wake();
	}

	private void drainDynamicTextureBatch() {
		for (int i = 0; i < DYNAMIC_TEXTURE_BATCH; i++) {
			final Runnable next;
			synchronized (dynamicTextureLock) {
				next = dynamicTextureQueue.pollFirst();
			}
			if (next == null) {
				return;
			}
			try {
				next.run();
			} catch (Exception e) {
				MTR.LOGGER.error("Dynamic texture generation task failed", e);
			}
		}
	}

	private boolean hasWork() {
		if (occlusionQueueVehicle.get() != null || occlusionQueueLift.get() != null) {
			return true;
		}
		synchronized (dynamicTextureLock) {
			return !dynamicTextureQueue.isEmpty();
		}
	}

	private void wake() {
		final Thread thread = loopThread;
		if (thread != null) {
			LockSupport.unpark(thread);
		}
	}

	private void updateInstance() {
		final int newRenderDistance = (int) MinecraftClient.getInstance().worldRenderer.getViewDistance();
		if (renderDistance != newRenderDistance) {
			renderDistance = newRenderDistance;
			occlusionCullingInstance = new OcclusionCullingInstance(Math.min(renderDistance, MAX_OCCLUSION_CHUNK_DISTANCE) * 16, new CullingDataProvider());
		}
	}

	private static boolean isRunning() {
		return MinecraftClient.getInstance().isRunning();
	}

	private static <T> void run(AtomicReference<@Nullable T> nextTask, Consumer<T> consumer) {
		try {
			final T task = nextTask.getAndSet(null);
			if (task != null) {
				consumer.accept(task);
			}
		} catch (Exception e) {
			MTR.LOGGER.error("Occlusion culling task failed", e);
		}
	}

	private static final class CullingDataProvider implements DataProvider {

		private final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		@Nullable
		private ClientWorld clientWorld = null;

		@Override
		public boolean prepareChunk(int chunkX, int chunkZ) {
			clientWorld = minecraftClient.world;
			return clientWorld != null;
		}

		@Override
		public boolean isOpaqueFullCube(int x, int y, int z) {
			final BlockPos blockPos = new BlockPos(x, y, z);
			return clientWorld != null && clientWorld.getBlockState(blockPos).isOpaqueFullCube();
		}

		@Override
		public void cleanup() {
			clientWorld = null;
		}
	}
}
