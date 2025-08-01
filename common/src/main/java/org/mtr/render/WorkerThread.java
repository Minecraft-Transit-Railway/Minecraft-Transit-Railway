package org.mtr.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * A background thread to perform intensive rendering tasks (e.g. Occlusion culling, generate dynamic textures etc.)
 */
public final class WorkerThread {

	private static final int MAX_OCCLUSION_CHUNK_DISTANCE = 32;
	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance;
	private boolean canStart = true;
	private int runCooldown;

	public final ExecutorService worker = Executors.newVirtualThreadPerTaskExecutor();
	private final AtomicReference<Consumer<OcclusionCullingInstance>> occlusionQueueVehicle = new AtomicReference<>();
	private final AtomicReference<Consumer<OcclusionCullingInstance>> occlusionQueueLift = new AtomicReference<>();
	private final AtomicReference<Consumer<OcclusionCullingInstance>> occlusionQueueRail = new AtomicReference<>();
	private final AtomicReference<Runnable> dynamicTextureQueue = new AtomicReference<>();

	private static final int COOLDOWN = 100;

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

	public void shutdown() {
		worker.shutdownNow();
	}

	public void scheduleVehicles(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueueVehicle.set(consumer);
	}

	public void scheduleLifts(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueueLift.set(consumer);
	}

	public void scheduleRails(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueueRail.set(consumer);
	}

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

	private static <T> void run(AtomicReference<T> nextTask, Consumer<T> consumer) {
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
