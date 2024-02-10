package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockView;
import org.mtr.mapping.holder.ClientWorld;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.CustomThread;

import java.util.function.Consumer;

public final class OcclusionCullingThread extends CustomThread {

	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance;
	private Consumer<OcclusionCullingInstance> queuedTask;

	@Override
	public void runTick() {
		updateInstance();
		occlusionCullingInstance.resetCache();
		final Consumer<OcclusionCullingInstance> currentTask = queuedTask;
		queuedTask = null;

		if (currentTask != null) {
			currentTask.accept(occlusionCullingInstance);
		}
	}

	private void updateInstance() {
		final int newRenderDistance = MinecraftClientHelper.getRenderDistance();
		if (renderDistance != newRenderDistance) {
			renderDistance = newRenderDistance;
			occlusionCullingInstance = new OcclusionCullingInstance(renderDistance * 16, new CullingDataProvider());
		}
	}

	public void schedule(Consumer<OcclusionCullingInstance> consumer) {
		queuedTask = consumer;
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
