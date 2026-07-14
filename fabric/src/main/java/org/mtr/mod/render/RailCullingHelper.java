package org.mtr.mod.render;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mod.client.MinecraftClientData;

final class RailCullingHelper {

	private static final double AABB_EXPANSION = 0.5;
	// OcclusionCulling walks every cell in the expanded AABB at least once. Keep a single rail from monopolizing the shared worker thread.
	private static final long MAX_OCCLUSION_CELL_COUNT = 1L << 20;

	private RailCullingHelper() {
	}

	static boolean isVisible(MinecraftClientData.RailWrapper railWrapper, Vec3d camera, OcclusionCullingInstance occlusionCullingInstance) {
		final Vec3d startVector = railWrapper.startVector;
		final Vec3d endVector = railWrapper.endVector;
		if (canUseOcclusionCulling(startVector.getX(), startVector.getY(), startVector.getZ(), endVector.getX(), endVector.getY(), endVector.getZ())) {
			return occlusionCullingInstance.isAABBVisible(startVector, endVector, camera);
		}
		return isWithinHorizontalRenderDistance(camera.getX(), camera.getZ(), startVector.getX(), startVector.getZ(), endVector.getX(), endVector.getZ(), MinecraftClientHelper.getRenderDistance() * 16D);
	}

	static boolean canUseOcclusionCulling(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		final long spanX = getExpandedBlockSpan(minX, maxX);
		final long spanY = getExpandedBlockSpan(minY, maxY);
		final long spanZ = getExpandedBlockSpan(minZ, maxZ);
		if (spanX > MAX_OCCLUSION_CELL_COUNT || spanY > MAX_OCCLUSION_CELL_COUNT / spanX) {
			return false;
		}
		final long area = spanX * spanY;
		return spanZ <= MAX_OCCLUSION_CELL_COUNT / area;
	}

	static boolean isWithinHorizontalRenderDistance(double cameraX, double cameraZ, double minX, double minZ, double maxX, double maxZ, double renderDistance) {
		if (!Double.isFinite(cameraX) || !Double.isFinite(cameraZ) || !Double.isFinite(minX) || !Double.isFinite(minZ) || !Double.isFinite(maxX) || !Double.isFinite(maxZ) || !Double.isFinite(renderDistance)) {
			return true;
		}
		if (renderDistance < 0) {
			return false;
		}

		final double lowerX = Math.min(minX, maxX);
		final double upperX = Math.max(minX, maxX);
		final double lowerZ = Math.min(minZ, maxZ);
		final double upperZ = Math.max(minZ, maxZ);
		final double distanceX = Math.max(Math.max(lowerX - cameraX, 0), cameraX - upperX);
		final double distanceZ = Math.max(Math.max(lowerZ - cameraZ, 0), cameraZ - upperZ);
		return Math.hypot(distanceX, distanceZ) <= renderDistance;
	}

	private static long getExpandedBlockSpan(double first, double second) {
		if (!Double.isFinite(first) || !Double.isFinite(second)) {
			return MAX_OCCLUSION_CELL_COUNT + 1;
		}

		final double lower = Math.floor(Math.min(first, second) - AABB_EXPANSION);
		final double upper = Math.floor(Math.max(first, second) + AABB_EXPANSION);
		final double span = upper - lower + 1;
		return span > 0 && span <= MAX_OCCLUSION_CELL_COUNT ? (long) span : MAX_OCCLUSION_CELL_COUNT + 1;
	}
}
