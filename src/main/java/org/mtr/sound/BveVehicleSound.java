package org.mtr.sound;

import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.Nullable;
import org.mtr.MTRClient;

import java.util.Random;

public class BveVehicleSound extends VehicleSoundBase {

	private final BveVehicleSoundConfig config;

	private float oldSpeedChange;
	private boolean oldOnRoute = false;

	private float motorCurrentOutput = 0;
	private float motorBreakerTimer = -1;

	private float mrPress;
	private boolean isCompressorActive;
	private boolean isCompressorActiveLastElapsed;

	private int defaultRunSoundIndex = -1;

	private final VehicleLoopingSoundHolder vehicleLoopingSoundHolder;

	public BveVehicleSound(BveVehicleSoundConfig config) {
		this.config = config;

		mrPress = randomInt(config.config.mrPressMin, config.config.mrPressMax + 1);
		isCompressorActive = randomInt(0, 20) == 0; // Currently set to 1/20 at client-side load
		isCompressorActiveLastElapsed = isCompressorActive;

		final Int2ObjectOpenHashMap<VehicleLoopingSoundInstance> soundLoopMotor = new Int2ObjectOpenHashMap<>();
		final Int2ObjectOpenHashMap<VehicleLoopingSoundInstance> soundLoopRun = new Int2ObjectOpenHashMap<>();

		config.config.motor.forEach((index, soundEvent) -> {
			soundLoopMotor.put((int)index, new VehicleLoopingSoundInstance(soundEvent));
		});
		config.config.run.forEach((index, soundEvent) -> {
			if(defaultRunSoundIndex == -1) defaultRunSoundIndex = index;
			soundLoopRun.put((int)index, new VehicleLoopingSoundInstance(soundEvent));
		});

		vehicleLoopingSoundHolder = new VehicleLoopingSoundHolder(
			soundLoopMotor,
			soundLoopRun,
			config.config.flange.isEmpty() ? null : new VehicleLoopingSoundInstance(config.config.flange.get(0)),
			config.config.noise == null ? null : new VehicleLoopingSoundInstance(config.config.noise),
			config.config.shoe == null ? null : new VehicleLoopingSoundInstance(config.config.shoe),
			config.config.compressorLoop == null ? null : new VehicleLoopingSoundInstance(config.config.compressorLoop)
		);
	}

	@Override
	public void playVehicleSound(VehicleSoundParameters input) {
		final float secondsElapsed = MTRClient.getGameTimeDeltaTicks() / 20;
		final float speedKilometersPerHour = input.speed() * 3600;
		final float speedMetersPerSecond = input.speed() * 1000;

		// Run noise
		final float runSoundBlendRatio = input.runSound().blendLevel();
		final float volume = Math.min(1, speedMetersPerSecond * 0.04F);
		final float pitch = speedMetersPerSecond * 0.04F;
		final int runIndexOld;
		final int runIndexNew;

		if(vehicleLoopingSoundHolder.soundLoopRun().containsKey(input.runSound().index())) {
			runIndexOld = input.runSound().index();
		} else {
			// Falls back to default sound index
			runIndexOld = defaultRunSoundIndex;
		}
		if(vehicleLoopingSoundHolder.soundLoopRun().containsKey(input.runSound().nextIndex())) {
			runIndexNew = input.runSound().nextIndex();
		} else {
			runIndexNew = defaultRunSoundIndex;
		}

		vehicleLoopingSoundHolder.soundLoopRun.forEach((runIndex, runSound) -> {
			if(runIndex == runIndexOld || runIndex == runIndexNew) {
				float indexVolumeFactor = runIndexOld == runIndexNew ? 1 : (runIndex == runIndexOld ? 1 - runSoundBlendRatio : runSoundBlendRatio);
				runSound.setData(volume * indexVolumeFactor, pitch, input.blockPos());
			} else {
				runSound.setData(0, pitch, input.blockPos());
			}
		});

		// Simulation of circuit breaker in traction controller
		float motorTarget = Math.signum(input.speedChange());
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

		// Clamp to a minimum volume whenever the inverter/motor is active
		if (motorCurrentOutput != 0) {
			motorCurrentOutput = Math.signum(motorCurrentOutput) * (0.3f + Math.abs(motorCurrentOutput) * (1 - 0.3f));
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
			vehicleLoopingSoundHolder.soundLoopCompressor.setData(isCompressorActive ? 1 : 0, 1, input.blockPos());
		}
		if (isCompressorActive && !isCompressorActiveLastElapsed) {
			playSoundInWorld(config.config.compressorAttack, input.blockPos());
		} else if (!isCompressorActive && isCompressorActiveLastElapsed) {
			playSoundInWorld(config.config.compressorRelease, input.blockPos());
		}

		// Motor noise
		vehicleLoopingSoundHolder.soundLoopMotor.forEach((motorIndex, vehicleLoopingSoundInstance) -> {
			vehicleLoopingSoundInstance.setData(config.motorData.getVolume(motorIndex, speedKilometersPerHour, motorCurrentOutput) * config.config.motorVolumeMultiply, config.motorData.getPitch(motorIndex, speedKilometersPerHour, motorCurrentOutput), input.blockPos());
		});

		// TODO play flange sounds
		// Flange noise
		if (vehicleLoopingSoundHolder.soundLoopFlange != null) {
			vehicleLoopingSoundHolder.soundLoopFlange.setData(0, 1, input.blockPos());
		}

		// Brake shoe rubbing noise (below regeneration brake cutoff limit)
		if (vehicleLoopingSoundHolder.soundLoopShoe != null) {
			final float shoePitch = 1 / (speedMetersPerSecond + 1) + 1;
			float shoeGain = speedMetersPerSecond < config.config.regenerationLimit && input.speedChange() < 0 ? 1 : 0;
			if (speedMetersPerSecond < 1.39) {
				final float t = speedMetersPerSecond * speedMetersPerSecond;
				shoeGain *= 1.5552F * t - 0.746496F * speedMetersPerSecond * t;
			} else if (speedMetersPerSecond > 12.5) {
				final float t = speedMetersPerSecond - 12.5F;
				shoeGain *= 1 / (0.1F * t * t + 1);
			}
			vehicleLoopingSoundHolder.soundLoopShoe.setData(shoeGain, shoePitch, input.blockPos());
		}

		// Constant loop noise
		if (vehicleLoopingSoundHolder.soundLoopNoise != null) {
			vehicleLoopingSoundHolder.soundLoopNoise.setData(input.isOnRoute() ? 1 : 0, 1, input.blockPos());
		}

		// Air brake application and release noise
		if (oldSpeedChange < 0 && input.speedChange() >= 0) {
			playSoundInWorld(config.config.brakeHandleRelease, input.blockPos());
			if (speedMetersPerSecond < config.config.regenerationLimit) {
				playSoundInWorld(config.config.airZero, input.blockPos());
			}
		} else if (oldSpeedChange <= 0 && input.speedChange() > 0 && speedMetersPerSecond < 0.3) {
			playSoundInWorld(config.config.airHigh, input.blockPos());
		} else if (oldSpeedChange >= 0 && input.speedChange() < 0) {
			mrPress -= (int) config.config.mrServiceBrakeReduce;
			playSoundInWorld(config.config.brakeHandleApply, input.blockPos());
		}

		// Emergency brake application after returning to depot
		if (oldOnRoute && !input.isOnRoute()) {
			playSoundInWorld(config.config.brakeEmergency, input.blockPos());
		}

		oldSpeedChange = input.speedChange();
		oldOnRoute = input.isOnRoute();
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

	private record VehicleLoopingSoundHolder(Int2ObjectOpenHashMap<VehicleLoopingSoundInstance> soundLoopMotor, Int2ObjectOpenHashMap<VehicleLoopingSoundInstance> soundLoopRun, @Nullable VehicleLoopingSoundInstance soundLoopFlange, @Nullable VehicleLoopingSoundInstance soundLoopNoise, @Nullable VehicleLoopingSoundInstance soundLoopShoe, @Nullable VehicleLoopingSoundInstance soundLoopCompressor) {

		public void dispose() {
			for (VehicleLoopingSoundInstance instance : soundLoopMotor.values()) {
				if (instance != null) {
					instance.dispose();
				}
			}
			for (VehicleLoopingSoundInstance instance : soundLoopRun.values()) {
				if (instance != null) {
					instance.dispose();
				}
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
