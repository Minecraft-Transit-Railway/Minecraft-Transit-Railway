package mtr.path;

import mtr.data.Rail;
import net.minecraft.util.math.Vec3d;

public class PathData {

	public final double length;
	public final double tOffset;
	public final double finalSpeed;

	private final Rail rail;
	private final double a1, b1;
	private final double a2, b2;
	private final double tSwitch;
	private final double tEnd;
	private final int delay;

	// distance = aT^2 + bT

	private static final double ACCELERATION = 0.01;

	public PathData(Rail rail, double startSpeed, boolean shouldStop, double tOffset) {
		length = rail.getLength();
		this.rail = rail;
		this.tOffset = tOffset;
		final double maxBlocksPerTick = rail.railType.maxBlocksPerTick;

		if (shouldStop) {
			delay = 40; // TODO editable platform dwell time
			double maxSlowdownTime = 2 * length / startSpeed;
			double minAcceleration = startSpeed / maxSlowdownTime;

			a1 = 0;
			a2 = -Math.max(minAcceleration, ACCELERATION);
			b1 = b2 = startSpeed;

			final double slowTime = getTimeAtSpeed(a2, b2, 0);
			final double distance = getDistance(a2, b2, slowTime);
			tSwitch = (length - distance) / startSpeed;
			tEnd = tSwitch + slowTime;

			finalSpeed = 0;
		} else {
			delay = 0;
			a1 = Math.signum(maxBlocksPerTick - startSpeed) * ACCELERATION;
			b1 = startSpeed;
			a2 = 0;
			b2 = maxBlocksPerTick;

			tSwitch = getTimeAtSpeed(a1, b1, maxBlocksPerTick);

			final double distance = getDistance(a1, b1, tSwitch);
			if (distance < length) {
				tEnd = tSwitch + (length - distance) / maxBlocksPerTick;
				finalSpeed = maxBlocksPerTick;
			} else {
				tEnd = solveQuadratic(a1, b1, -length);
				finalSpeed = 2 * a1 * tEnd + b1;
			}
		}
	}

	public Vec3d getPosition(double value) {
		final double offsetValue = value - tOffset;
		if (offsetValue < tSwitch) {
			return rail.getPosition(getDistance(a1, b1, offsetValue));
		} else if (offsetValue < tEnd) {
			return rail.getPosition(getDistance(a2, b2, offsetValue - tSwitch) + getDistance(a1, b1, tSwitch));
		} else if (offsetValue < tEnd + delay) {
			return rail.getPosition(getDistance(a2, b2, tEnd - tSwitch) + getDistance(a1, b1, tSwitch));
		} else {
			return null;
		}
	}

	public double getTime() {
		return tEnd + delay;
	}

	private static double solveQuadratic(double a, double b, double c) {
		final double d = b * b - 4 * a * c;
		return d < 0 ? 0 : getTimeAtSpeed(a, b, Math.sqrt(d));
	}

	private static double getDistance(double a, double b, double t) {
		return a * t * t + b * t;
	}

	private static double getTimeAtSpeed(double a, double b, double speed) {
		return a == 0 ? 0 : (speed - b) / (2 * a);
	}
}
