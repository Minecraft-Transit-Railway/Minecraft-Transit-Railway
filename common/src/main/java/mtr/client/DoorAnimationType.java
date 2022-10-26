package mtr.client;

import net.minecraft.util.Mth;

public enum DoorAnimationType {

	STANDARD(0.5F),
	STANDARD_SLOW(0.5F),
	CONSTANT(0.5F),
	PLUG_FAST(0.5F),
	PLUG_SLOW(2),
	BOUNCY_1(0.5F),
	BOUNCY_2(0.5F),
	MLR(0.7F),
	R179(0.6F),
	R211(0.7F);

	public final float maxTime;

	DoorAnimationType(float maxTime) {
		this.maxTime = maxTime;
	}

	public static float getDoorAnimationX(DoorAnimationType doorAnimationType, float value) {
		switch (doorAnimationType) {
			case PLUG_FAST:
				return value < 0.05 ? -value * 20 - 0.01F : -1.01F;
			case PLUG_SLOW:
				return smoothEnds(-0.01F, -1.01F, 0, 0.1F, value);
			default:
				return 0;
		}
	}

	public static float getDoorAnimationZ(DoorAnimationType doorAnimationType, int doorMax, float duration, float value, boolean opening) {
		switch (doorAnimationType) {
			case CONSTANT:
				return doorMax * Mth.clamp(value, 0, 1) / duration;
			case PLUG_FAST:
				return smoothEnds(-doorMax, doorMax, -duration, duration, value);
			case PLUG_SLOW:
				if (opening) {
					return smoothEnds(0, doorMax, 0.05F, 0.5F, value);
				} else {
					if (value > 0.5) {
						return smoothEnds(2, doorMax, 0.5F, 1, value);
					} else if (value < 0.3) {
						return smoothEnds(0, 2, 0.05F, 0.3F, value);
					} else {
						return 2;
					}
				}
			case BOUNCY_1:
				if (opening) {
					if (value > 0.4) {
						return smoothEnds(doorMax - 1, doorMax - 0.5F, 0.4F, 0.5F, value);
					} else {
						return smoothEnds(-doorMax + 1, doorMax - 1, -0.4F, 0.4F, value);
					}
				} else {
					if (value > 0.2) {
						return smoothEnds(1, doorMax - 0.5F, 0.2F, 0.5F, value);
					} else if (value > 0.1) {
						return smoothEnds(1.5F, 1, 0.1F, 0.2F, value);
					} else {
						return smoothEnds(-1.5F, 1.5F, -0.1F, 0.1F, value);
					}
				}
			case BOUNCY_2:
				if (opening) {
					if (value > 0.4) {
						return smoothEnds(doorMax - 0.5F, doorMax, 0.4F, 0.5F, value);
					} else {
						return smoothEnds(-doorMax + 0.5F, doorMax - 0.5F, -0.4F, 0.4F, value);
					}
				} else {
					if (value > 0.3) {
						return smoothEnds(1, doorMax, 0.3F, 0.5F, value);
					} else if (value > 0.1) {
						return smoothEnds(3, 1, 0.1F, 0.3F, value);
					} else {
						return smoothEnds(-3, 3, -0.1F, 0.1F, value);
					}
				}
			case MLR:
				if (opening) {
					if (value < 0.2) {
						return 0;
					} else if (value > 0.7) {
						return doorMax;
					} else {
						return (value - 0.2F) * 2 * doorMax;
					}
				} else {
					final float stoppingPoint = 1.5F;
					if (value > 0.25) {
						return Math.min((value - 0.25F) * 2 * doorMax + stoppingPoint + 1, doorMax);
					} else if (value > 0.2) {
						return smoothEnds(stoppingPoint, stoppingPoint + 2, 0.2F, 0.3F, value);
					} else if (value < 0.1) {
						return value / 0.1F * stoppingPoint;
					} else {
						return stoppingPoint;
					}
				}
			case R179:
				if (opening) {
					if (value > 0.4) {
						return smoothEnds(doorMax - 1, doorMax - 0.5F, 0.4F, 0.6F, value);
					} else {
						return smoothEnds(-doorMax + 1, doorMax - 1, -0.4F, 0.4F, value);
					}
				} else {
					if (value > 0.2) {
						return smoothEnds(1, doorMax - 0.5F, 0.2F, 0.6F, value);
					} else {
						return smoothEnds(-1.5F, 1.5F, -0.4F, 0.4F, value);
					}
				}
			case R211:
				if (value < 0.2) {
					return 0;
				} else if (value > 0.7) {
					return doorMax;
				} else {
					return (value - 0.2F) * 2 * doorMax;
				}
			case STANDARD_SLOW:
				if (!opening) {
					if (value > 0.2) {
						return smoothEnds(1, doorMax, 0.2F, duration, value);
					} else if (value > 0.1) {
						return 1;
					} else {
						return smoothEnds(0, 1, 0, 0.1F, value);
					}
				}
			default:
				return smoothEnds(0, doorMax, 0, duration, value);
		}
	}

	private static float smoothEnds(float startValue, float endValue, float startTime, float endTime, float time) {
		if (time < startTime) {
			return startValue;
		}
		if (time > endTime) {
			return endValue;
		}

		final float timeChange = endTime - startTime;
		final float valueChange = endValue - startValue;
		return valueChange * (float) (1 - Math.cos(Math.PI * (time - startTime) / timeChange)) / 2 + startValue;
	}
}
