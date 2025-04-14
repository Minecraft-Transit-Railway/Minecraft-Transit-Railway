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

public final class WorkerThread {

	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance;
	private boolean canStart = true;
	private int runCooldown;

	private final AtomicReference<Consumer<OcclusionCullingInstance>> occlusionQueue1 = new AtomicReference<>();
	private final AtomicReference<Consumer<OcclusionCullingInstance>> occlusionQueue2 = new AtomicReference<>();
	private final AtomicReference<Consumer<OcclusionCullingInstance>> occlusionQueue3 = new AtomicReference<>();
	private final AtomicReference<Runnable> queue = new AtomicReference<>();
	private final ExecutorService worker = Executors.newVirtualThreadPerTaskExecutor();

	private static final int COOLDOWN = 100;

	public void start() {
		if (canStart && isRunning()) {
			canStart = false;
			worker.submit(() -> {
				while (isRunning()) {
					if (occlusionQueue1.get() != null || occlusionQueue2.get() != null || occlusionQueue3.get() != null) {
						updateInstance();
						occlusionCullingInstance.resetCache();
						run(occlusionQueue1, task -> task.accept(occlusionCullingInstance));
						run(occlusionQueue2, task -> task.accept(occlusionCullingInstance));
						run(occlusionQueue3, task -> task.accept(occlusionCullingInstance));
					}

					run(queue, Runnable::run);

					try {
						Thread.sleep(1);
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
		occlusionQueue1.set(consumer);
	}

	public void scheduleLifts(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueue2.set(consumer);
	}

	public void scheduleRails(Consumer<OcclusionCullingInstance> consumer) {
		occlusionQueue3.set(consumer);
	}

	public void scheduleDynamicTextures(Runnable runnable) {
		queue.set(runnable);
	}

	private void updateInstance() {
		final int newRenderDistance = (int) MinecraftClient.getInstance().worldRenderer.getViewDistance();
		if (renderDistance != newRenderDistance) {
			renderDistance = newRenderDistance;
			occlusionCullingInstance = new OcclusionCullingInstance(renderDistance * 16, new CullingDataProvider());
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
