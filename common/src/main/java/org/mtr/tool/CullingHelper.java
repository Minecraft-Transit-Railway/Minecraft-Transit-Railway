package org.mtr.tool;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.util.math.Vec3d;

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
}
