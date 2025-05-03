package org.mtr.tool;

import lombok.Getter;
import org.mtr.core.tool.Utilities;

/**
 * A helper class for smoothly animating to a new value using a sine curve.
 */
public final class GuiAnimation {

	private long startMillis;
	private double startValue;
	@Getter
	private double currentValue;
	private double targetValue;
	private int duration;

	/**
	 * Start an animation.
	 *
	 * @param targetValue the value to animate to
	 * @param duration    the duration (in milliseconds) of the animation
	 */
	public void animate(double targetValue, int duration) {
		if (this.targetValue != targetValue) {
			this.targetValue = targetValue;
			this.duration = duration;
			startMillis = 0;
		}
	}

	/**
	 * Directly set the value, skipping the animation. This also cancels any existing animation going on.
	 *
	 * @param value the value to set
	 */
	public void setValue(double value) {
		targetValue = value;
		currentValue = value;
	}

	/**
	 * Call this method every tick to perform the animation.
	 */
	public void tick() {
		if (Math.abs(targetValue - currentValue) < 0.01) {
			startMillis = 0;
		} else {
			final long currentMillis = System.currentTimeMillis();
			if (startMillis == 0) {
				startMillis = currentMillis;
				startValue = currentValue;
			} else {
				final double progress = Math.sin(Math.PI / 2 * Utilities.clamp((double) (currentMillis - startMillis) / Math.max(1, duration), 0, 1));
				currentValue = startValue + progress * (targetValue - startValue);
			}
		}
	}
}
