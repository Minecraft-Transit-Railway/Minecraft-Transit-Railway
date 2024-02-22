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

public final class WorkerThread extends CustomThread {

	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance;
	private Consumer<OcclusionCullingInstance> queuedOcclusionTask;
	private final ObjectArrayList<Runnable> queue = new ObjectArrayList<>();

	@Override
	protected void runTick() {
		updateInstance();
		occlusionCullingInstance.resetCache();
		final Consumer<OcclusionCullingInstance> currentTask = queuedOcclusionTask;
		queuedOcclusionTask = null;

		if (currentTask != null) {
			currentTask.accept(occlusionCullingInstance);
		}

		if (!queue.isEmpty()) {
			try {
				final Runnable task = queue.remove(0);
				if (task != null) {
					task.run();
				}
			} catch (Exception e) {
				Init.LOGGER.error("", e);
			}
		}
	}

	@Override
	protected boolean isRunning() {
		return MinecraftClient.getInstance().isRunning();
	}

	public void schedule(Consumer<OcclusionCullingInstance> consumer) {
		queuedOcclusionTask = consumer;
	}

	public void schedule(Runnable runnable) {
		queue.add(runnable);
	}

	private void updateInstance() {
		final int newRenderDistance = MinecraftClientHelper.getRenderDistance();
		if (renderDistance != newRenderDistance) {
			renderDistance = newRenderDistance;
			occlusionCullingInstance = new OcclusionCullingInstance(renderDistance * 16, new CullingDataProvider());
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
