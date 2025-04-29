package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockView;
import org.mtr.mapping.holder.ClientWorld;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.CustomThread;
import org.mtr.mod.Init;

import java.util.function.Consumer;

/**
 * A background thread to perform intensive rendering tasks (e.g. Occlusion culling, generate dynamic textures etc.)
 */
public final class WorkerThread extends CustomThread {

	private static final int MAX_OCCLUSION_CHUNK_DISTANCE = 32;
	private static final int MAX_QUEUE_SIZE = 2;
	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance;
	private final ObjectArrayList<Consumer<OcclusionCullingInstance>> occlusionQueueVehicle = new ObjectArrayList<>();
	private final ObjectArrayList<Consumer<OcclusionCullingInstance>> occlusionQueueLift = new ObjectArrayList<>();
	private final ObjectArrayList<Consumer<OcclusionCullingInstance>> occlusionQueueRail = new ObjectArrayList<>();
	private final ObjectArrayList<Runnable> dynamicTextureQueue = new ObjectArrayList<>();

	@Override
	protected void runTick() {
		try {
			Thread.sleep(10); // Give the CPU a little break
		} catch (InterruptedException e) {}

		if (!occlusionQueueVehicle.isEmpty() || !occlusionQueueLift.isEmpty() || !occlusionQueueRail.isEmpty()) {
			updateInstance();
			occlusionCullingInstance.resetCache();
			run(occlusionQueueVehicle, task -> task.accept(occlusionCullingInstance));
			run(occlusionQueueLift, task -> task.accept(occlusionCullingInstance));
			run(occlusionQueueRail, task -> task.accept(occlusionCullingInstance));
		}

		run(dynamicTextureQueue, Runnable::run);
	}

	@Override
	protected boolean isRunning() {
		return MinecraftClient.getInstance().isRunning();
	}

	public void scheduleVehicles(Consumer<OcclusionCullingInstance> consumer) {
		if (occlusionQueueVehicle.size() < MAX_QUEUE_SIZE) {
			occlusionQueueVehicle.add(consumer);
		}
	}

	public void scheduleLifts(Consumer<OcclusionCullingInstance> consumer) {
		if (occlusionQueueLift.size() < MAX_QUEUE_SIZE) {
			occlusionQueueLift.add(consumer);
		}
	}

	public void scheduleRails(Consumer<OcclusionCullingInstance> consumer) {
		if (occlusionQueueRail.size() < MAX_QUEUE_SIZE) {
			occlusionQueueRail.add(consumer);
		}
	}

	public void scheduleDynamicTextures(Runnable runnable) {
		dynamicTextureQueue.add(runnable);
	}

	private void updateInstance() {
		final int newRenderDistance = MinecraftClientHelper.getRenderDistance();
		if (renderDistance != newRenderDistance) {
			renderDistance = newRenderDistance;
			occlusionCullingInstance = new OcclusionCullingInstance(Math.min(renderDistance, MAX_OCCLUSION_CHUNK_DISTANCE) * 16, new CullingDataProvider());
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
				Init.LOGGER.error("", e);
			}
		}
	}

	private static final class CullingDataProvider implements DataProvider {

		private final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		private ClientWorld clientWorld = null;

		@Override
		public boolean prepareChunk(int chunkX, int chunkZ) {
			clientWorld = minecraftClient.getWorldMapped();
			return clientWorld != null;
		}

		@Override
		public boolean isOpaqueFullCube(int x, int y, int z) {
			final BlockPos blockPos = new BlockPos(x, y, z);
			return clientWorld != null && clientWorld.getBlockState(blockPos).isOpaqueFullCube(new BlockView(clientWorld.data), blockPos);
		}

		@Override
		public void cleanup() {
			clientWorld = null;
		}
	}
}
