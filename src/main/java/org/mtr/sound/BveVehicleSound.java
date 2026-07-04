package org.mtr.sound;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import org.jspecify.annotations.Nullable;
import org.mtr.MTRClient;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

import java.util.Random;

public class BveVehicleSound extends VehicleSoundBase {

	private final BveVehicleSoundConfig config;

	private float oldSpeedChange;
	private boolean oldOnRoute;
	private float motorCurrentOutput;
	private float motorBreakerTimer = -1;

	private float mrPress;
	private boolean isCompressorActive;
	private boolean isCompressorActiveLastElapsed;

	private final int defaultRunSoundIndex;
	private final VehicleLoopingSoundHolder vehicleLoopingSoundHolder;

	public BveVehicleSound(BveVehicleSoundConfig config) {
		this.config = config;
		mrPress = randomInt(config.config.mrPressMin, config.config.mrPressMax + 1);
		isCompressorActive = randomInt(0, 20) == 0; // Currently set to 1/20 at client-side load
		isCompressorActiveLastElapsed = isCompressorActive;

		final Int2ObjectOpenHashMap<VehicleLoopingSoundInstance> soundLoopMotor = new Int2ObjectOpenHashMap<>();
		config.config.motor.int2ObjectEntrySet().forEach(entry -> soundLoopMotor.put(entry.getIntKey(), new VehicleLoopingSoundInstance(entry.getValue())));

		int firstRunIndex = -1;
		final Int2ObjectOpenHashMap<VehicleLoopingSoundInstance> soundLoopRun = new Int2ObjectOpenHashMap<>();

		for (final int key : config.config.run.keySet()) {
			if (firstRunIndex == -1) {
				firstRunIndex = key;
			}
			soundLoopRun.put(key, new VehicleLoopingSoundInstance(config.config.run.get(key)));
		}

		defaultRunSoundIndex = Math.max(0, firstRunIndex);

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
		final BlockPos blockPos = input.blockPos();

		// Run noise
		final VehicleSoundBase.RunSoundInfo runSound = input.runSound();
		if (runSound != null) {
			final float runSoundBlendRatio = runSound.blendLevel();
			final float volume = Math.min(1, speedMetersPerSecond * 0.04F);
			final float pitch = speedMetersPerSecond * 0.04F;
			final int runIndexOld = vehicleLoopingSoundHolder.soundLoopRun().containsKey(runSound.index()) ? runSound.index() : defaultRunSoundIndex;
			final int runIndexNew = vehicleLoopingSoundHolder.soundLoopRun().containsKey(runSound.nextIndex()) ? runSound.nextIndex() : defaultRunSoundIndex;

			vehicleLoopingSoundHolder.soundLoopRun.forEach((runIndex, vehicleLoopingSoundInstance) -> {
				final float indexVolumeFactor;
				if (runIndex == runIndexOld || runIndex == runIndexNew) {
					indexVolumeFactor = runIndexOld == runIndexNew ? 1 : (runIndex == runIndexOld ? 1 - runSoundBlendRatio : runSoundBlendRatio);
				} else {
					indexVolumeFactor = 0;
				}
				vehicleLoopingSoundInstance.setData(volume * indexVolumeFactor, pitch, blockPos);
			});
		}

		// Simulation of circuit breaker in traction controller
		{
			final float motorTargetRaw = Math.signum(input.speedChange());
			final float motorTarget = motorTargetRaw == 0 && speedMetersPerSecond != 0 ? config.config.motorOutputAtCoast : motorTargetRaw;

			if (motorTarget < 0 && speedMetersPerSecond < config.config.regenerationLimit) {
				motorCurrentOutput = 0;
				motorBreakerTimer = -1;
			} else if (motorTarget > 0 && speedMetersPerSecond < 1) {
				motorCurrentOutput = 1;
				motorBreakerTimer = -1;
			} else if (motorTarget != motorCurrentOutput && motorBreakerTimer < 0) {
				motorBreakerTimer = 0;
				if (motorTarget != 0 && motorCurrentOutput != 0) {
					motorCurrentOutput = 0;
				}
			}

			if (motorBreakerTimer >= 0) {
				motorBreakerTimer += secondsElapsed;
				if (motorBreakerTimer > config.config.breakerDelay) {
					motorBreakerTimer = -1;
					motorCurrentOutput = motorTarget;
				}
			}

			if (motorCurrentOutput != 0) {
				motorCurrentOutput = Math.signum(motorCurrentOutput) * (0.3F + Math.abs(motorCurrentOutput) * 0.7F);
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
			vehicleLoopingSoundHolder.soundLoopCompressor.setData(isCompressorActive ? 1 : 0, 1, blockPos);
		}

		if (isCompressorActive && !isCompressorActiveLastElapsed) {
			playSoundInWorld(config.config.compressorAttack, blockPos);
		} else if (!isCompressorActive && isCompressorActiveLastElapsed) {
			playSoundInWorld(config.config.compressorRelease, blockPos);
		}

		// Motor noise
		vehicleLoopingSoundHolder.soundLoopMotor.forEach((motorIndex, vehicleLoopingSoundInstance) -> {
			final float volume = config.motorData.getVolume(motorIndex, speedKilometersPerHour, motorCurrentOutput) * config.config.motorVolumeMultiply;
			final float pitch = config.motorData.getPitch(motorIndex, speedKilometersPerHour, motorCurrentOutput);
			vehicleLoopingSoundInstance.setData(volume, pitch, blockPos);
		});

		// Flange noise
		if (vehicleLoopingSoundHolder.soundLoopFlange != null) {
			vehicleLoopingSoundHolder.soundLoopFlange.setData(0, 1, blockPos);
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

			vehicleLoopingSoundHolder.soundLoopShoe.setData(shoeGain, shoePitch, blockPos);
		}

		// Constant loop noise
		if (vehicleLoopingSoundHolder.soundLoopNoise != null) {
			vehicleLoopingSoundHolder.soundLoopNoise.setData(input.isOnRoute() ? 1 : 0, 1, blockPos);
		}

		final float speedChange = input.speedChange();
		final boolean isOnRoute = input.isOnRoute();

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
			for (final VehicleLoopingSoundInstance instance : soundLoopMotor.values()) {
				if (instance != null) {
					instance.dispose();
				}
			}

			for (final VehicleLoopingSoundInstance instance : soundLoopRun.values()) {
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
