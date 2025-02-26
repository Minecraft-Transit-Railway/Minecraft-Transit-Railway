package org.mtr.mod.client;

import org.mtr.core.data.TransportMode;

public class Oscillation {

	private double amount;
	private double amplitude;
	private double scheduledAmplitude;
	private long time;
	private final boolean enabled;

	private static final long PERIOD = 1000;
	private static final double PERIOD_MULTIPLIER = 2 * Math.PI / PERIOD;
	private static final double DAMPING = 2000;

	public Oscillation(TransportMode transportMode) {
		// No oscillation for airplanes
		enabled = transportMode != TransportMode.AIRPLANE;
	}

	public void tick(long millisElapsed) {
		if (enabled) {
			final long oldTime = time;
			time += millisElapsed;

			if (scheduledAmplitude > 0 && (oldTime == 0 || oldTime % PERIOD > time % PERIOD)) {
				amplitude = scheduledAmplitude;
				scheduledAmplitude = 0;
			}

			amount = amplitude * Math.sin(PERIOD_MULTIPLIER * time);

			if (amplitude > 0.01) {
				amplitude /= Math.exp(millisElapsed / DAMPING);
			} else {
				amplitude = 0;
				time = 0;
			}
		}
	}

	public void startOscillation(double magnitude) {
		scheduledAmplitude = magnitude;
	}

	public double getAmount() {
		return enabled ? amount : 0;
	}
}
