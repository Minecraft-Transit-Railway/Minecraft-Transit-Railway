package org.mtr.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.CustomThread;
import org.mtr.MTR;

import java.util.function.Consumer;

public final class WorkerThread extends CustomThread {

	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance;
	private final ObjectArrayList<Consumer<OcclusionCullingInstance>> occlusionQueue1 = new ObjectArrayList<>();
	private final ObjectArrayList<Consumer<OcclusionCullingInstance>> occlusionQueue2 = new ObjectArrayList<>();
	private final ObjectArrayList<Consumer<OcclusionCullingInstance>> occlusionQueue3 = new ObjectArrayList<>();
	private final ObjectArrayList<Runnable> queue = new ObjectArrayList<>();

	@Override
	protected void runTick() {
		if (!occlusionQueue1.isEmpty() || !occlusionQueue2.isEmpty() || !occlusionQueue3.isEmpty()) {
			updateInstance();
			occlusionCullingInstance.resetCache();
			run(occlusionQueue1, task -> task.accept(occlusionCullingInstance));
			run(occlusionQueue2, task -> task.accept(occlusionCullingInstance));
			run(occlusionQueue3, task -> task.accept(occlusionCullingInstance));
		}

		run(queue, Runnable::run);
	}

	@Override
	protected boolean isRunning() {
		return MinecraftClient.getInstance().isRunning();
	}

	public void scheduleVehicles(Consumer<OcclusionCullingInstance> consumer) {
		if (occlusionQueue1.size() < 2) {
			occlusionQueue1.add(consumer);
		}
	}

	public void scheduleLifts(Consumer<OcclusionCullingInstance> consumer) {
		if (occlusionQueue2.size() < 2) {
			occlusionQueue2.add(consumer);
		}
	}

	public void scheduleRails(Consumer<OcclusionCullingInstance> consumer) {
		if (occlusionQueue3.size() < 2) {
			occlusionQueue3.add(consumer);
		}
	}

	public void scheduleDynamicTextures(Runnable runnable) {
		queue.add(runnable);
	}

	private void updateInstance() {
		final int newRenderDistance = (int) MinecraftClient.getInstance().worldRenderer.getViewDistance();
		if (renderDistance != newRenderDistance) {
			renderDistance = newRenderDistance;
			occlusionCullingInstance = new OcclusionCullingInstance(renderDistance * 16, new CullingDataProvider());
		}
	}

	private static <T> void run(ObjectArrayList<T> queue, Consumer<T> consumer) {
		if (!queue.isEmpty()) {
			try {
				final T task = queue.remove(0);
				if (task != null) {
					consumer.accept(task);
				}
			} catch (Exception e) {
				MTR.LOGGER.error("", e);
			}
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
