package org.mtr.mod.resource;

/**
 * A helper class to interpolate from an old value to a new value. This is useful for smoothly animating a value when it jumps suddenly.
 */
public final class Interpolation {

	private long startMillis;
	private double oldValue;
	private double latestValue;

	private final int duration;

	/**
	 * @param duration the amount of time to interpolate in milliseconds
	 */
	public Interpolation(int duration) {
		this.duration = duration;
	}

	/**
	 * Sets the current value.
	 *
	 * @param value              the new value
	 * @param startInterpolation if {@code true}, start the interpolation, otherwise, continue interpolating from what was previously set
	 */
	public void setValue(double value, boolean startInterpolation) {
		if (startInterpolation && oldValue != value) {
			oldValue = getValue();
			startMillis = System.currentTimeMillis();
		}
		latestValue = value;
	}

	/**
	 * Directly set the current value without interpolating.
	 *
	 * @param value the new value
	 */
	public void setValueDirect(double value) {
		startMillis = 0;
		oldValue = value;
		latestValue = value;
	}

	public double getValue() {
		final long difference = System.currentTimeMillis() - startMillis;
		if (difference < duration) {
			return (1 - (float) difference / duration) * oldValue + (float) difference / duration * latestValue;
		} else {
			oldValue = latestValue;
			return latestValue;
		}
	}
}
