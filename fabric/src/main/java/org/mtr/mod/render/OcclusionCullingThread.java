package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.data.Position;
import org.mtr.core.data.Rail;
import org.mtr.core.tool.Vector;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.data.VehicleExtension;

import java.util.concurrent.CopyOnWriteArrayList;

public final class OcclusionCullingThread extends Thread {

	private boolean started;
	private int renderDistance;
	private OcclusionCullingInstance occlusionCullingInstance;

	public static final CopyOnWriteArrayList<VehicleExtension> VEHICLES = new CopyOnWriteArrayList<>();
	public static final CopyOnWriteArrayList<RailWrapper> RAILS = new CopyOnWriteArrayList<>();

	@Override
	public synchronized void start() {
		if (!started) {
			super.start();
		}
		started = true;
	}

	@Override
	public void run() {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		while (minecraftClient.isRunning()) {
			updateInstance();
			occlusionCullingInstance.resetCache();
			final Vector3d cameraPosition = minecraftClient.getGameRendererMapped().getCamera().getPos();
			final Vec3d camera = new Vec3d(cameraPosition.getXMapped(), cameraPosition.getYMapped(), cameraPosition.getZMapped());

			VEHICLES.forEach(vehicle -> {
				for (int i = 0; i < vehicle.persistentVehicleData.pivotPositions.length; i++) {
					final Vector pivotPosition = vehicle.persistentVehicleData.pivotPositions[i];
					if (pivotPosition != null) {
						final double longestDimension = vehicle.persistentVehicleData.longestDimensions[i];
						vehicle.persistentVehicleData.rayTracing[i] = occlusionCullingInstance.isAABBVisible(new Vec3d(
								pivotPosition.x - longestDimension,
								pivotPosition.y - 8,
								pivotPosition.z - longestDimension
						), new Vec3d(
								pivotPosition.x + longestDimension,
								pivotPosition.y + 8,
								pivotPosition.z + longestDimension
						), camera);
					}
				}
			});

			RAILS.forEach(railWrapper -> ClientData.getInstance().railCulling.put(railWrapper.hexId, occlusionCullingInstance.isAABBVisible(railWrapper.minPosition, railWrapper.maxPosition, camera)));
		}
	}

	private void updateInstance() {
		final int newRenderDistance = MinecraftClientHelper.getRenderDistance();
		if (renderDistance != newRenderDistance) {
			renderDistance = newRenderDistance;
			occlusionCullingInstance = new OcclusionCullingInstance(renderDistance * 16, new CullingDataProvider());
		}
	}

	public static final class RailWrapper {

		private final String hexId;
		private final Vec3d minPosition;
		private final Vec3d maxPosition;

		public RailWrapper(Position startPosition, Position endPosition, Rail rail) {
			hexId = rail.getHexId();
			minPosition = new Vec3d(
					Math.min(startPosition.getX(), endPosition.getX()),
					Math.min(startPosition.getY(), endPosition.getY()),
					Math.min(startPosition.getZ(), endPosition.getZ())
			);
			maxPosition = new Vec3d(
					Math.max(startPosition.getX(), endPosition.getX()),
					Math.max(startPosition.getY(), endPosition.getY()),
					Math.max(startPosition.getZ(), endPosition.getZ())
			);
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
