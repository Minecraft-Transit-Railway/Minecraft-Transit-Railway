package org.mtr.tool;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

/**
 * Camera-relative distance / cull helpers shared by every world-space renderer.
 *
 * <p>All distances returned here are <em>orthogonal (Manhattan)</em> rather than Euclidean
 * because Minecraft's own render-distance budget is expressed in chunks (16-block grid
 * steps), and a cheap |dx| + |dy| + |dz| is a strict upper bound on the true distance — so
 * culling on it is conservative and matches vanilla's per-chunk culling.</p>
 */
public final class CullingHelper {

	/**
	 * @return the orthogonal distance of a point to the camera or {@code Integer.MAX_VALUE} if the point is beyond the render distance or behind the camera
	 */
	public static double getDistanceFromCamera(double x, double y, double z) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final double renderDistance = minecraftClient.worldRenderer.getViewDistance() * 16;
		final Camera camera = minecraftClient.gameRenderer.getCamera();

		final Vec3d cameraPosition = camera.getPos();
		final double orthogonalDistanceFromCamera = Math.abs(x - cameraPosition.x) + Math.abs(y - cameraPosition.y) + Math.abs(z - cameraPosition.z);

		final boolean inFrontOfCamera;
		if (orthogonalDistanceFromCamera <= 32) {
			inFrontOfCamera = true;
		} else {
			final double angleFromCamera = Math.atan2(z - cameraPosition.z, x - cameraPosition.x);
			double cameraAngleDifference = angleFromCamera - Math.toRadians(camera.getYaw()) - Math.PI / 2;
			while (cameraAngleDifference < -Math.PI) {
				cameraAngleDifference += Math.PI * 2;
			}
			while (cameraAngleDifference > Math.PI) {
				cameraAngleDifference -= Math.PI * 2;
			}
			inFrontOfCamera = Math.abs(cameraAngleDifference) < Math.PI / 2;
		}

		return inFrontOfCamera && orthogonalDistanceFromCamera <= renderDistance ? orthogonalDistanceFromCamera : Integer.MAX_VALUE;
	}

	/**
	 * Conservative AABB-vs-camera distance test. Returns the orthogonal distance of the
	 * <em>closest</em> point on the axis-aligned bounding box to the camera, or
	 * {@link Integer#MAX_VALUE} if that distance exceeds the current render-distance
	 * budget.
	 *
	 * <p>Designed for whole-object culling (e.g. an entire {@link org.mtr.core.data.Rail}
	 * baked as one logical entity). When the bounding box itself is within range we render
	 * <em>every</em> segment of the object — there is no second per-segment culling pass.
	 * This eliminates the "section of rail ends abruptly" artifact that older per-segment
	 * culling produces while still cheaply skipping rails that are entirely far away.</p>
	 *
	 * <p>No frustum / behind-camera test is performed: a long rail can stretch behind the
	 * camera while most of it is still on-screen, and the vanilla world-renderer's own
	 * frustum culling at draw time already handles the off-screen case. See
	 * {@code docs/PERFORMANCE.md} §3.10 for the rationale.</p>
	 *
	 * @return the closest-point orthogonal distance to the camera, or {@code Integer.MAX_VALUE} if beyond render distance
	 */
	public static double getDistanceFromCameraToBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final double renderDistance = minecraftClient.worldRenderer.getViewDistance() * 16;
		final Vec3d cameraPosition = minecraftClient.gameRenderer.getCamera().getPos();

		final double dx = Math.max(Math.max(minX - cameraPosition.x, 0), cameraPosition.x - maxX);
		final double dy = Math.max(Math.max(minY - cameraPosition.y, 0), cameraPosition.y - maxY);
		final double dz = Math.max(Math.max(minZ - cameraPosition.z, 0), cameraPosition.z - maxZ);
		final double distance = dx + dy + dz;
		return distance <= renderDistance ? distance : Integer.MAX_VALUE;
	}
}
