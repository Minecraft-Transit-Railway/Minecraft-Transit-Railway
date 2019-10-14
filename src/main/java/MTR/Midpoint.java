package mtr;

public class Midpoint {

	public final double midX, midY, midZ;
	public final float angleYaw, anglePitch;

	public Midpoint(double x, double y, double z, float yaw, float pitch) {
		midX = x;
		midY = y;
		midZ = z;
		angleYaw = yaw;
		anglePitch = pitch;
	}
}
