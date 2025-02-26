package org.mtr.mod.resource;


import org.mtr.core.tool.Utilities;

public enum DoorAnimationType {

	STANDARD,
	STANDARD_SLOW,
	CONSTANT,
	PLUG_FAST,
	PLUG_SLOW,
	PLUG_SLOW_2,
	PLUG_SLOW_3,
	BOUNCY_1,
	BOUNCY_2,
	MLR,
	R179,
	R211;

	DoorAnimationType() {
	}

	public double getDoorAnimationX(double multiplier, boolean flipped, double time) {
		return multiplier == 0 ? 0 : Math.copySign(getDoorAnimationXAbsolute(Math.abs(multiplier), time), (flipped ? -1 : 1) * multiplier);
	}

	public double getDoorAnimationZ(double multiplier, boolean flipped, double time, boolean opening) {
		return multiplier == 0 ? 0 : Math.copySign(getDoorAnimationZAbsolute(Math.abs(multiplier), time, opening), (flipped ? -1 : 1) * multiplier);
	}

	private double getDoorAnimationXAbsolute(double multiplier, double time) {
		switch (this) {
			case PLUG_FAST:
			case PLUG_SLOW_2:
			case PLUG_SLOW_3:
				return time < 0.05 ? time * 20 * multiplier + 0.05 : multiplier + 0.05;
			case PLUG_SLOW:
				return smoothEnds(0.05, multiplier + 0.05, 0, 0.1, time);
			default:
				return 0;
		}
	}

	private double getDoorAnimationZAbsolute(double doorMax, double time, boolean opening) {
		switch (this) {
			case CONSTANT:
				return doorMax * Utilities.clamp(time / 0.5, 0, 1);
			case PLUG_FAST:
				return smoothEnds(-doorMax, doorMax, -0.5, 0.5, time);
			case PLUG_SLOW:
				if (opening) {
					return smoothEnds(0, doorMax, 0.05, 0.5, time);
				} else {
					if (time > 0.5) {
						return smoothEnds(2, doorMax, 0.5, 1, time);
					} else if (time < 0.3) {
						return smoothEnds(0, 2, 0.05, 0.3, time);
					} else {
						return 2;
					}
				}
			case PLUG_SLOW_2:
				return smoothEnds(-doorMax, doorMax, -0.75, 0.75, time);
			case PLUG_SLOW_3:
				return smoothEnds(-doorMax, doorMax, -1, 1, time);
			case BOUNCY_1:
				if (opening) {
					if (time > 0.4) {
						return smoothEnds(doorMax - 1, doorMax - 0.5, 0.4, 0.5, time);
					} else {
						return smoothEnds(-doorMax + 1, doorMax - 1, -0.4, 0.4, time);
					}
				} else {
					if (time > 0.2) {
						return smoothEnds(1, doorMax - 0.5, 0.2, 0.5, time);
					} else if (time > 0.1) {
						return smoothEnds(1.5, 1, 0.1, 0.2, time);
					} else {
						return smoothEnds(-1.5, 1.5, -0.1, 0.1, time);
					}
				}
			case BOUNCY_2:
				if (opening) {
					if (time > 0.4) {
						return smoothEnds(doorMax - 0.5, doorMax, 0.4, 0.5, time);
					} else {
						return smoothEnds(-doorMax + 0.5, doorMax - 0.5, -0.4, 0.4, time);
					}
				} else {
					if (time > 0.3) {
						return smoothEnds(1, doorMax, 0.3, 0.5, time);
					} else if (time > 0.1) {
						return smoothEnds(3, 1, 0.1, 0.3, time);
					} else {
						return smoothEnds(-3, 3, -0.1, 0.1, time);
					}
				}
			case MLR:
				if (opening) {
					if (time < 0.2) {
						return 0;
					} else if (time > 0.7) {
						return doorMax;
					} else {
						return (time - 0.2) * 2 * doorMax;
					}
				} else {
					final double stoppingPoint = 1.5;
					if (time > 0.25) {
						return Math.min((time - 0.25) * 2 * doorMax + stoppingPoint + 1, doorMax);
					} else if (time > 0.2) {
						return smoothEnds(stoppingPoint, stoppingPoint + 2, 0.2, 0.3, time);
					} else if (time < 0.1) {
						return time / 0.1 * stoppingPoint;
					} else {
						return stoppingPoint;
					}
				}
			case R179:
				if (opening) {
					if (time > 0.4) {
						return smoothEnds(doorMax - 1, doorMax - 0.5, 0.4, 0.6, time);
					} else {
						return smoothEnds(-doorMax + 1, doorMax - 1, -0.4, 0.4, time);
					}
				} else {
					if (time > 0.2) {
						return smoothEnds(1, doorMax - 0.5, 0.2, 0.6, time);
					} else {
						return smoothEnds(-1.5, 1.5, -0.4, 0.4, time);
					}
				}
			case R211:
				if (time < 0.2) {
					return 0;
				} else if (time > 0.7) {
					return doorMax;
				} else {
					return (time - 0.2) * 2 * doorMax;
				}
			case STANDARD_SLOW:
				if (!opening) {
					if (time > 0.2) {
						return smoothEnds(1, doorMax, 0.2, 0.5, time);
					} else if (time > 0.1) {
						return 1;
					} else {
						return smoothEnds(0, 1, 0, 0.1, time);
					}
				}
			default:
				return smoothEnds(0, doorMax, 0, 0.5, time);
		}
	}

	private static double smoothEnds(double startValue, double endValue, double startTime, double endTime, double time) {
		if (time < startTime) {
			return startValue;
		}
		if (time > endTime) {
			return endValue;
		}

		final double timeChange = endTime - startTime;
		final double valueChange = endValue - startValue;
		return valueChange * (1 - Math.cos(Math.PI * (time - startTime) / timeChange)) / 2 + startValue;
	}
}
