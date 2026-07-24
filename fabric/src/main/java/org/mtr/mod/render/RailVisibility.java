package org.mtr.mod.render;

final class RailVisibility {

	private static final double NEAR_RENDER_DISTANCE = 32;
	private static final double NEAR_RENDER_DISTANCE_SQUARED = NEAR_RENDER_DISTANCE * NEAR_RENDER_DISTANCE;

	static boolean shouldRender(double x, double y, double z, double cameraX, double cameraY, double cameraZ, double renderDistance, double renderDistanceSquared, float yawCos, float yawSin, float pitchCos, float pitchSin) {
		final double differenceX = x - cameraX;
		final double differenceZ = z - cameraZ;
		final double distanceSquared = differenceX * differenceX + differenceZ * differenceZ;
		if (!compareSquaredDistance(distanceSquared, renderDistanceSquared, renderDistance, true)) {
			return false;
		}
		if (compareSquaredDistance(distanceSquared, NEAR_RENDER_DISTANCE_SQUARED, NEAR_RENDER_DISTANCE, false)) {
			return true;
		}
		final double yawRotatedZ = differenceZ * yawCos - differenceX * yawSin;
		return yawRotatedZ * pitchCos - (y - cameraY) * pitchSin > 0;
	}

	private static boolean compareSquaredDistance(double distanceSquared, double thresholdSquared, double threshold, boolean inclusive) {
		if (Math.abs(distanceSquared - thresholdSquared) <= Math.ulp(thresholdSquared) * 4) {
			final double distance = Math.sqrt(distanceSquared);
			return inclusive ? distance <= threshold : distance < threshold;
		}
		return inclusive ? distanceSquared <= thresholdSquared : distanceSquared < thresholdSquared;
	}
}
