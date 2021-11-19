package mtr.data;

public enum RailAngle {
	E(0),
	SEE(22.5F),
	SE(45),
	SSE(67.5F),
	S(90),
	SSW(112.5F),
	SW(135),
	SWW(157.5F),
	W(180),
	NWW(202.5F),
	NW(225),
	NNW(247.5F),
	N(270),
	NNE(292.5F),
	NE(315),
	NEE(337.5F);

//	public static final int ScaleFactor = 1024;
//	public static final double PropertyDegree = 22.5;
//	public static final int PropertyFactor = (int) (ScaleFactor * PropertyDegree);
//	public static final int RightAngle = 90 * ScaleFactor;
//	public static final int HalfCircleAngle = 180 * ScaleFactor;
//	public static final int CircleAngle = 360 * ScaleFactor;

//	public static final RailAngle NORTH = new RailAngle(-90 * ScaleFactor);
//	public static final RailAngle SOUTH = new RailAngle(90 * ScaleFactor);
//	public static final RailAngle EAST = new RailAngle(0 * ScaleFactor);
//	public static final RailAngle WEST = new RailAngle(-180 * ScaleFactor);
//	public static final RailAngle NE = new RailAngle(-45 * ScaleFactor);
//	public static final RailAngle NW = new RailAngle(-135 * ScaleFactor);
//	public static final RailAngle SE = new RailAngle(45 * ScaleFactor);
//	public static final RailAngle SW = new RailAngle(135 * ScaleFactor);
//	public static final RailAngle UP = new RailAngle(0 * ScaleFactor);
//	public static final RailAngle DOWN = new RailAngle(180 * ScaleFactor);

//	public static final Vec3d VNORTH = new Vec3d(0, 0, -1);
//	public static final Vec3d VSOUTH = new Vec3d(0, 0, 1);
//	public static final Vec3d VEAST = new Vec3d(1, 0, 0);
//	public static final Vec3d VWEST = new Vec3d(-1, 0, 0);
//	public static final Vec3d VNE = new Vec3d(1, 0, -1);
//	public static final Vec3d VNW = new Vec3d(-1, 0, -1);
//	public static final Vec3d VSE = new Vec3d(1, 0, 1);
//	public static final Vec3d VSW = new Vec3d(-1, 0, 1);

	private final float angle;
	public static final int DEGREES_IN_CIRCLE = 360;
	public static final int QUADRANTS = values().length;
	public static final float ANGLE_INCREMENT = (float) DEGREES_IN_CIRCLE / QUADRANTS;
//	public static final double ACCEPT_THRESHOLD = 1E-4;
//	public static final int ACCEPT_ANGLE_ERROR = 10;

	RailAngle(float angle) {
		this.angle = angle;
	}

	public RailAngle getOpposite() {
		switch (this) {
			case N:
				return S;
			case NNE:
				return SSW;
			case NE:
				return SW;
			case NEE:
				return SWW;
			case E:
				return W;
			case SEE:
				return NWW;
			case SE:
				return NW;
			case SSE:
				return NNW;
			default:
				return N;
			case SSW:
				return NNE;
			case SW:
				return NE;
			case SWW:
				return NEE;
			case W:
				return E;
			case NWW:
				return SEE;
			case NW:
				return SE;
			case NNW:
				return SSE;
		}
	}

	public final double getRadians() {
		return Math.toRadians(clampAngle());
	}

	public RailAngle add(RailAngle railAngle) {
		return fromAngle(angle + railAngle.angle);
	}

	public RailAngle sub(RailAngle railAngle) {
		return fromAngle(angle - railAngle.angle);
	}

	public boolean isParallel(RailAngle railAngle) {
		return this == railAngle || this == railAngle.getOpposite();
	}

	public boolean similarFacing(float newAngle) {
		return similarFacing(angle, newAngle);
	}

	public double sin() {
		return Math.sin(getRadians());
	}

	public double cos() {
		return Math.cos(getRadians());
	}

	public double tan() {
		return tanSafe(getRadians());
	}

	public double halfTan() {
		return tanSafe(getRadians() / 2);
	}

	private double clampAngle() {
		return angle - (angle >= DEGREES_IN_CIRCLE / 2F ? DEGREES_IN_CIRCLE : 0);
	}

	public static float normalizeAngle(float angle) {
		int additional = 0;
		while (angle + additional < 0) {
			additional += DEGREES_IN_CIRCLE;
		}
		while (angle + additional >= DEGREES_IN_CIRCLE) {
			additional -= DEGREES_IN_CIRCLE;
		}
		return angle + additional;
	}

	public static boolean similarFacing(float angle1, float angle2) {
		final float difference = normalizeAngle(angle1 - angle2);
		return difference < DEGREES_IN_CIRCLE / 4F || difference >= DEGREES_IN_CIRCLE * 3 / 4F;
	}

	public static RailAngle fromAngle(float angle) {
		return RailAngle.values()[Math.round(normalizeAngle(angle) / ANGLE_INCREMENT) % QUADRANTS];
	}

	private static double tanSafe(double angle) {
		return angle == DEGREES_IN_CIRCLE / 4F || angle == -DEGREES_IN_CIRCLE / 4F ? 1E100 : Math.tan(angle);
	}
}
