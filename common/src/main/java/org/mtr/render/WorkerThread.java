package org.mtr.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Background thread driving the off-main-thread part of every frame.
 *
 * <p>The thread owns four single-slot inboxes:</p>
 * <ul>
 *   <li>Three for occlusion-culling work (vehicles / lifts / rails), each refreshed every
 *       frame from the corresponding renderer.</li>
 *   <li>One for dynamic-texture generation, fed by {@link org.mtr.client.DynamicTextureCache}.</li>
 * </ul>
 *
 * <p>The loop wakes every {@code 10 ms}, drains whichever slots are non-null, and goes back
 * to sleep. {@link #start()} is idempotent — the loop is launched once on first call and
 * survives until {@link MinecraftClient#isRunning()} returns {@code false}.</p>
 *
 * <p><b>Known limitation:</b> all four inboxes are
 * {@link java.util.concurrent.atomic.AtomicReference}s, so {@code set(...)} overwrites any
 * pending runnable. For the dynamic-texture queue this manifests as "textures pop in one
 * per frame when many are requested at once" — tracked in
 * {@code docs/PERFORMANCE.md} §2.1.</p>
 *
 * <p>The {@link #worker} executor is a {@code newVirtualThreadPerTaskExecutor}, suitable for
 * one-shot async submissions (model parsing, IO-bound resource decoding).</p>
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
	private final AtomicReference<@Nullable Consumer<OcclusionCullingInstance>> occlusionQueueRail = new AtomicReference<>();
	private final AtomicReference<@Nullable Runnable> dynamicTextureQueue = new AtomicReference<>();

	private static final int COOLDOWN = 100;

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
				while (isRunning()) {
					if (occlusionQueueVehicle.get() != null || occlusionQueueLift.get() != null || occlusionQueueRail.get() != null) {
						updateInstance();
						occlusionCullingInstance.resetCache();
						run(occlusionQueueVehicle, task -> task.accept(occlusionCullingInstance));
						run(occlusionQueueLift, task -> task.accept(occlusionCullingInstance));
						run(occlusionQueueRail, task -> task.accept(occlusionCullingInstance));
					}

					run(dynamicTextureQueue, Runnable::run);

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						MTR.LOGGER.debug("", e);
					}

					runCooldown = 0;
				}
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
	}

	/** See {@link #scheduleVehicles(Consumer)}. */
	public void scheduleLifts(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueueLift.set(consumer);
	}

	/** See {@link #scheduleVehicles(Consumer)}. */
	public void scheduleRails(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueueRail.set(consumer);
	}

	/**
	 * Replace the pending dynamic-texture-generation task.
	 *
	 * <p><b>Caveat:</b> single-slot queue — overwrites any previous pending task. See the
	 * class-level note and {@code docs/PERFORMANCE.md} §2.1.</p>
	 */
	public void scheduleDynamicTextures(Runnable runnable) {
		dynamicTextureQueue.set(runnable);
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
			MTR.LOGGER.error("", e);
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
