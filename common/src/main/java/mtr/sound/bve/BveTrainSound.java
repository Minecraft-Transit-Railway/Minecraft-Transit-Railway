package mtr.sound.bve;

import mtr.MTRClient;
import mtr.client.TrainClientRegistry;
import mtr.data.TrainClient;
import mtr.sound.TrainLoopingSoundInstance;
import mtr.sound.TrainSoundBase;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.concurrent.ThreadLocalRandom;

public class BveTrainSound extends TrainSoundBase {

	private float accelLastElapsed;
	private boolean onRouteLastElapsed = false;

	private int motorCurrentMode = 0;
	private float motorSwitchTimer = -1;

	private int mrPress;
	private boolean isCompressorActive;
	private boolean isCompressorActiveLastElapsed;

	public final BveTrainSoundConfig config;

	private final TrainLoopingSoundInstance[] soundLoopMotor;
	private final TrainLoopingSoundInstance soundLoopRun;
	private final TrainLoopingSoundInstance soundLoopFlange;
	private final TrainLoopingSoundInstance soundLoopNoise;
	private final TrainLoopingSoundInstance soundLoopShoe;
	private final TrainLoopingSoundInstance soundLoopCompressor;
	private final int[][] bogieRailId;

	public BveTrainSound(TrainClient train, BveTrainSoundConfig config) {
		super(train);
		this.config = config;
		bogieRailId = new int[train.trainCars][2];

		mrPress = ThreadLocalRandom.current().nextInt(config.soundCfg.mrPressMin, config.soundCfg.mrPressMax + 1);
		isCompressorActive = ThreadLocalRandom.current().nextInt(0, 20) == 0; // Currently, set to 1/20 at client-side load
		isCompressorActiveLastElapsed = isCompressorActive;

		soundLoopRun = config.soundCfg.run[0] == null ? null : new TrainLoopingSoundInstance(config.soundCfg.run[0], train);
		soundLoopFlange = config.soundCfg.flange[0] == null ? null : new TrainLoopingSoundInstance(config.soundCfg.flange[0], train);
		soundLoopNoise = config.soundCfg.noise == null ? null : new TrainLoopingSoundInstance(config.soundCfg.noise, train);
		soundLoopShoe = config.soundCfg.shoe == null ? null : new TrainLoopingSoundInstance(config.soundCfg.shoe, train);
		soundLoopCompressor = config.soundCfg.compressorLoop == null ? null : new TrainLoopingSoundInstance(config.soundCfg.compressorLoop, train);

		soundLoopMotor = new TrainLoopingSoundInstance[config.soundCfg.motor.length];
		for (int i = 0; i < Math.min(config.soundCfg.motor.length, config.motorData.getSoundCount()); ++i) {
			if (config.soundCfg.motor[i] != null) {
				soundLoopMotor[i] = new TrainLoopingSoundInstance(config.soundCfg.motor[i], train);
			}
		}
	}

	@Override
	public void playNearestCar(Level world, BlockPos pos, int carIndex) {
		final float deltaT = MTRClient.getLastFrameDuration() / 20;
		final float speed = train.getSpeed() * 20;
		final float accel = speed > 0 ? train.speedChange() < 0 ? -1 : 1 : 0; // TODO sounds weird when coasting or braking
		final float speedKph = speed * 3.6F;

		// Rolling noise
		if (soundLoopRun != null) {
			soundLoopRun.setData(Math.min(1, speed * 0.04F), speed * 0.04F, pos);
		}

		// Simulation of circuit breaker in traction controller
		final int motorTargetMode = (int) Math.signum(accel);
		if (motorTargetMode < 0 && speed < config.soundCfg.regenerationLimit) {
			motorCurrentMode = 0; // Regeneration brake cut off below limit speed
			motorSwitchTimer = -1;
		} else if (motorTargetMode > 0 && speed < 1) {
			motorCurrentMode = motorTargetMode; // Disable delay at startup
			motorSwitchTimer = -1;
		} else if (motorTargetMode != motorCurrentMode && motorSwitchTimer < 0) {
			motorSwitchTimer = 0;
			if (motorTargetMode != 0 && motorCurrentMode != 0) {
				motorCurrentMode = 0; // Loose behavior but sounds OK
			}
		}
		if (motorSwitchTimer >= 0) {
			motorSwitchTimer += deltaT;
			if (motorSwitchTimer > config.soundCfg.breakerDelay) {
				motorSwitchTimer = -1;
				motorCurrentMode = motorTargetMode;
			}
		}

		// Simulation of main reservoir air compressor
		if (mrPress <= config.soundCfg.mrPressMin) {
			isCompressorActive = true;
			mrPress = config.soundCfg.mrPressMin;
		} else if (mrPress >= config.soundCfg.mrPressMax) {
			isCompressorActive = false;
			mrPress = config.soundCfg.mrPressMax;
		}
		if (isCompressorActive) {
			mrPress += deltaT * config.soundCfg.mrCompressorSpeed;
		}
		if (soundLoopCompressor != null) {
			// NOTE: In BVE specification the compressor loop sound should not be played for the
			// first 5 secs of compressor activity, when the compressor attack sound is played.
			// This is not done, since the playLocalSound is only called once at the nearest car
			// at the time when the attack sound is triggered, and player movement after that might
			// cause unwanted effect.
			soundLoopCompressor.setData(isCompressorActive ? 1 : 0, 1, pos);
		}
		if (isCompressorActive && !isCompressorActiveLastElapsed) {
			playLocalSound(world, config.soundCfg.compressorAttack, pos);
		} else if (!isCompressorActive && isCompressorActiveLastElapsed) {
			playLocalSound(world, config.soundCfg.compressorRelease, pos);
		}

		// Motor noise
		for (int i = 0; i < config.motorData.getSoundCount(); ++i) {
			if (soundLoopMotor[i] == null) {
				continue;
			}
			soundLoopMotor[i].setData(config.motorData.getVolume(i, speedKph, motorCurrentMode) * config.soundCfg.motorVolumeMultiply, config.motorData.getPitch(i, speedKph, motorCurrentMode), pos);
		}

		// TODO Play flange sounds
		// Flange noise
		if (soundLoopFlange != null) {
			soundLoopFlange.setData(0, 1, pos);
		}

		// Brake shoe rubbing noise (below regeneration brake cutoff limit)
		if (soundLoopShoe != null) {
			final float shoePitch = 1 / (speed + 1) + 1;
			float shoeGain = speed < config.soundCfg.regenerationLimit && accel < 0 ? 1 : 0;
			if (speed < 1.39) {
				double t = speed * speed;
				shoeGain *= 1.5552 * t - 0.746496 * speed * t;
			} else if (speed > 12.5) {
				double t = speed - 12.5;
				shoeGain *= 1 / (0.1 * t * t + 1);
			}
			soundLoopShoe.setData(shoeGain, shoePitch, pos);
		}

		// Constant loop noise
		if (soundLoopNoise != null) {
			soundLoopNoise.setData(train.getIsOnRoute() ? 1 : 0, 1, pos);
		}

		// Air brake application and release noise
		if (accelLastElapsed < 0 && accel >= 0) {
			playLocalSound(world, config.soundCfg.brakeHandleRelease, pos);
			if (speed < config.soundCfg.regenerationLimit) {
				playLocalSound(world, config.soundCfg.airZero, pos);
			}
		} else if (accelLastElapsed <= 0 && accel > 0 && speed < 0.3) {
			playLocalSound(world, config.soundCfg.airHigh, pos);
		} else if (accelLastElapsed >= 0 && accel < 0) {
			mrPress -= config.soundCfg.mrServiceBrakeReduce;
			playLocalSound(world, config.soundCfg.brakeHandleApply, pos);
		}

		// Emergency brake application after returning to depot
		if (onRouteLastElapsed && !train.getIsOnRoute()) {
			playLocalSound(world, config.soundCfg.brakeEmergency, pos);
		}

		accelLastElapsed = accel;
		onRouteLastElapsed = train.getIsOnRoute();
		isCompressorActiveLastElapsed = isCompressorActive;
	}

	@Override
	public void playAllCars(Level world, BlockPos pos, int carIndex) {
		final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(train.trainId);

		if (config.soundCfg.joint[0] == null || trainProperties.bogiePosition == 0) {
			return;
		}

		final float bogieOffsetFront;
		final float bogieOffsetRear;
		if (trainProperties.isJacobsBogie) {
			if (carIndex == 0) {
				bogieOffsetFront = train.spacing / 2F - trainProperties.bogiePosition;
				bogieOffsetRear = -1;
			} else if (carIndex == train.trainCars - 1) {
				bogieOffsetFront = 0;
				bogieOffsetRear = train.spacing / 2F + trainProperties.bogiePosition;
			} else {
				bogieOffsetFront = 0;
				bogieOffsetRear = -1;
			}
		} else {
			bogieOffsetFront = train.spacing / 2F - trainProperties.bogiePosition;
			bogieOffsetRear = train.spacing / 2F + trainProperties.bogiePosition;
		}

		final float pitch = train.getSpeed() * 20 / 12.5F;
		final float gain = pitch < 0.5F ? 2 * pitch : 1;
		if (bogieOffsetFront >= 0) {
			int indexFront = train.getIndex(train.getRailProgress() - train.spacing * carIndex - bogieOffsetFront, false);
			if (indexFront != bogieRailId[carIndex][0]) {
				bogieRailId[carIndex][0] = indexFront;
				playLocalSound(world, config.soundCfg.joint[0], pos, gain, pitch);
			}
		}
		if (bogieOffsetRear >= 0) {
			final int indexRear = train.getIndex(train.getRailProgress() - train.spacing * carIndex - bogieOffsetRear, false);
			if (indexRear != bogieRailId[carIndex][1]) {
				bogieRailId[carIndex][1] = indexRear;
				playLocalSound(world, config.soundCfg.joint[0], pos, gain, pitch);
			}
		}
	}

	@Override
	public void playAllCarsDoorOpening(Level world, BlockPos pos, int carIndex) {
		// TODO Check why door sounds are not playing
		if (!(world instanceof ClientLevel)) {
			return;
		}

		final SoundEvent soundEvent;
		if (train.justOpening() && config.soundCfg.doorOpen != null) {
			soundEvent = config.soundCfg.doorOpen;
		} else if (train.justClosing(config.soundCfg.doorCloseSoundLength) && config.soundCfg.doorClose != null) {
			soundEvent = config.soundCfg.doorClose;
		} else {
			soundEvent = null;
		}

		playLocalSound(world, soundEvent, pos);
	}

	private static void playLocalSound(Level world, SoundEvent event, BlockPos pos, float gain, float pitch) {
		if (event == null) {
			return;
		}
		((ClientLevel) world).playLocalSound(pos, event, SoundSource.BLOCKS, Math.min(1, gain), pitch, true);
	}

	private static void playLocalSound(Level world, SoundEvent event, BlockPos pos) {
		if (event == null) {
			return;
		}
		((ClientLevel) world).playLocalSound(pos, event, SoundSource.BLOCKS, 1, 1, true);
	}
}
