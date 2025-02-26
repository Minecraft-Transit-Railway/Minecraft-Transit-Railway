package org.mtr.mod.sound;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.SoundEvent;
import org.mtr.mod.InitClient;

import javax.annotation.Nullable;
import java.util.Random;

public class BveVehicleSound extends VehicleSoundBase {

	private final BveVehicleSoundConfig config;

	private float oldSpeedChange;
	private boolean oldOnRoute = false;

	private float motorCurrentOutput = 0;
	private float motorBreakerTimer = -1;

	private int mrPress;
	private boolean isCompressorActive;
	private boolean isCompressorActiveLastElapsed;

	private final VehicleLoopingSoundHolder vehicleLoopingSoundHolder;

	public BveVehicleSound(BveVehicleSoundConfig config) {
		this.config = config;

		mrPress = randomInt(config.config.mrPressMin, config.config.mrPressMax + 1);
		isCompressorActive = randomInt(0, 20) == 0; // Currently set to 1/20 at client-side load
		isCompressorActiveLastElapsed = isCompressorActive;

		final VehicleLoopingSoundInstance[] soundLoopMotor = new VehicleLoopingSoundInstance[config.config.motor.length];
		for (int i = 0; i < Math.min(config.config.motor.length, config.motorData.getSoundCount()); i++) {
			if (config.config.motor[i] != null) {
				soundLoopMotor[i] = new VehicleLoopingSoundInstance(config.config.motor[i]);
			}
		}
		vehicleLoopingSoundHolder = new VehicleLoopingSoundHolder(
				soundLoopMotor,
				config.config.run[0] == null ? null : new VehicleLoopingSoundInstance(config.config.run[0]),
				config.config.flange[0] == null ? null : new VehicleLoopingSoundInstance(config.config.flange[0]),
				config.config.noise == null ? null : new VehicleLoopingSoundInstance(config.config.noise),
				config.config.shoe == null ? null : new VehicleLoopingSoundInstance(config.config.shoe),
				config.config.compressorLoop == null ? null : new VehicleLoopingSoundInstance(config.config.compressorLoop)
		);
	}

	@Override
	public void playMotorSound(BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute) {
		if (!InitClient.canPlaySound()) {
			return;
		}

		final float secondsElapsed = MinecraftClient.getInstance().getLastFrameDuration() / 20;
		final float speedKilometersPerHour = speed * 3600;
		final float speedMetersPerSecond = speed * 1000;

		// Rolling noise
		if (vehicleLoopingSoundHolder.soundLoopRun != null) {
			vehicleLoopingSoundHolder.soundLoopRun.setData(Math.min(1, speedMetersPerSecond * 0.04F), speedMetersPerSecond * 0.04F, blockPos);
		}

		// Simulation of circuit breaker in traction controller
		float motorTarget = Math.signum(speedChange);
		if (motorTarget == 0 && speedMetersPerSecond != 0) {
			motorTarget = config.config.motorOutputAtCoast;
		}
		if (motorTarget < 0 && speedMetersPerSecond < config.config.regenerationLimit) {
			motorCurrentOutput = 0; // Regeneration brake cut off below limit speed
			motorBreakerTimer = -1;
		} else if (motorTarget > 0 && speedMetersPerSecond < 1) {
			motorCurrentOutput = 1; // Disable delay at startup
			motorBreakerTimer = -1;
		} else if (motorTarget != motorCurrentOutput && motorBreakerTimer < 0) {
			motorBreakerTimer = 0;
			if (motorTarget != 0 && motorCurrentOutput != 0) {
				motorCurrentOutput = 0; // Loose behavior but sounds OK
			}
		}
		if (motorBreakerTimer >= 0) {
			motorBreakerTimer += secondsElapsed;
			if (motorBreakerTimer > config.config.breakerDelay) {
				motorBreakerTimer = -1;
				motorCurrentOutput = motorTarget;
			}
		}

		// Simulation of main reservoir air compressor
		if (mrPress <= config.config.mrPressMin) {
			isCompressorActive = true;
			mrPress = config.config.mrPressMin;
		} else if (mrPress >= config.config.mrPressMax) {
			isCompressorActive = false;
			mrPress = config.config.mrPressMax;
		}
		if (isCompressorActive) {
			mrPress += (int) (secondsElapsed * config.config.mrCompressorSpeed);
		}
		if (vehicleLoopingSoundHolder.soundLoopCompressor != null) {
			// NOTE: Attack sound playback is not to BVE specification.
			vehicleLoopingSoundHolder.soundLoopCompressor.setData(isCompressorActive ? 1 : 0, 1, blockPos);
		}
		if (isCompressorActive && !isCompressorActiveLastElapsed) {
			playSoundInWorld(config.config.compressorAttack, blockPos);
		} else if (!isCompressorActive && isCompressorActiveLastElapsed) {
			playSoundInWorld(config.config.compressorRelease, blockPos);
		}

		// Motor noise
		for (int i = 0; i < config.motorData.getSoundCount(); i++) {
			if (vehicleLoopingSoundHolder.soundLoopMotor[i] != null) {
				vehicleLoopingSoundHolder.soundLoopMotor[i].setData(config.motorData.getVolume(i, speedKilometersPerHour, motorCurrentOutput) * config.config.motorVolumeMultiply, config.motorData.getPitch(i, speedKilometersPerHour, motorCurrentOutput), blockPos);
			}
		}

		// TODO play flange sounds
		// Flange noise
		if (vehicleLoopingSoundHolder.soundLoopFlange != null) {
			vehicleLoopingSoundHolder.soundLoopFlange.setData(0, 1, blockPos);
		}

		// Brake shoe rubbing noise (below regeneration brake cutoff limit)
		if (vehicleLoopingSoundHolder.soundLoopShoe != null) {
			final float shoePitch = 1 / (speedMetersPerSecond + 1) + 1;
			float shoeGain = speedMetersPerSecond < config.config.regenerationLimit && speedChange < 0 ? 1 : 0;
			if (speedMetersPerSecond < 1.39) {
				final float t = speedMetersPerSecond * speedMetersPerSecond;
				shoeGain *= 1.5552F * t - 0.746496F * speedMetersPerSecond * t;
			} else if (speedMetersPerSecond > 12.5) {
				final float t = speedMetersPerSecond - 12.5F;
				shoeGain *= 1 / (0.1F * t * t + 1);
			}
			vehicleLoopingSoundHolder.soundLoopShoe.setData(shoeGain, shoePitch, blockPos);
		}

		// Constant loop noise
		if (vehicleLoopingSoundHolder.soundLoopNoise != null) {
			vehicleLoopingSoundHolder.soundLoopNoise.setData(isOnRoute ? 1 : 0, 1, blockPos);
		}

		// Air brake application and release noise
		if (oldSpeedChange < 0 && speedChange >= 0) {
			playSoundInWorld(config.config.brakeHandleRelease, blockPos);
			if (speedMetersPerSecond < config.config.regenerationLimit) {
				playSoundInWorld(config.config.airZero, blockPos);
			}
		} else if (oldSpeedChange <= 0 && speedChange > 0 && speedMetersPerSecond < 0.3) {
			playSoundInWorld(config.config.airHigh, blockPos);
		} else if (oldSpeedChange >= 0 && speedChange < 0) {
			mrPress -= (int) config.config.mrServiceBrakeReduce;
			playSoundInWorld(config.config.brakeHandleApply, blockPos);
		}

		// Emergency brake application after returning to depot
		if (oldOnRoute && !isOnRoute) {
			playSoundInWorld(config.config.brakeEmergency, blockPos);
		}

		oldSpeedChange = speedChange;
		oldOnRoute = isOnRoute;
		isCompressorActiveLastElapsed = isCompressorActive;
	}

	@Override
	protected void playDoorSound(BlockPos blockPos, boolean isOpen) {
		ScheduledSound.schedule(blockPos, isOpen ? config.config.doorOpen : config.config.doorClose, 2, 1);
	}

	@Override
	public void dispose() {
		vehicleLoopingSoundHolder.dispose();
	}

	@Override
	protected double getDoorCloseSoundTime() {
		return config.config.doorCloseSoundLength;
	}

	private static void playSoundInWorld(@Nullable SoundEvent soundEvent, BlockPos blockPos) {
		ScheduledSound.schedule(blockPos, soundEvent, 1, 1);
	}

	private static int randomInt(int minInclusive, int maxExclusive) {
		return new Random().nextInt(maxExclusive - minInclusive) + minInclusive;
	}

	private static class VehicleLoopingSoundHolder {

		private final VehicleLoopingSoundInstance[] soundLoopMotor;
		@Nullable
		private final VehicleLoopingSoundInstance soundLoopRun;
		@Nullable
		private final VehicleLoopingSoundInstance soundLoopFlange;
		@Nullable
		private final VehicleLoopingSoundInstance soundLoopNoise;
		@Nullable
		private final VehicleLoopingSoundInstance soundLoopShoe;
		@Nullable
		private final VehicleLoopingSoundInstance soundLoopCompressor;

		private VehicleLoopingSoundHolder(
				VehicleLoopingSoundInstance[] soundLoopMotor,
				@Nullable VehicleLoopingSoundInstance soundLoopRun,
				@Nullable VehicleLoopingSoundInstance soundLoopFlange,
				@Nullable VehicleLoopingSoundInstance soundLoopNoise,
				@Nullable VehicleLoopingSoundInstance soundLoopShoe,
				@Nullable VehicleLoopingSoundInstance soundLoopCompressor
		) {
			this.soundLoopMotor = soundLoopMotor;
			this.soundLoopRun = soundLoopRun;
			this.soundLoopFlange = soundLoopFlange;
			this.soundLoopNoise = soundLoopNoise;
			this.soundLoopShoe = soundLoopShoe;
			this.soundLoopCompressor = soundLoopCompressor;
		}

		public void dispose() {
			for (VehicleLoopingSoundInstance instance : soundLoopMotor) {
				if (instance != null) {
					instance.dispose();
				}
			}
			if (soundLoopRun != null) {
				soundLoopRun.dispose();
			}
			if (soundLoopFlange != null) {
				soundLoopFlange.dispose();
			}
			if (soundLoopNoise != null) {
				soundLoopNoise.dispose();
			}
			if (soundLoopShoe != null) {
				soundLoopShoe.dispose();
			}
			if (soundLoopCompressor != null) {
				soundLoopCompressor.dispose();
			}
		}
	}
}
