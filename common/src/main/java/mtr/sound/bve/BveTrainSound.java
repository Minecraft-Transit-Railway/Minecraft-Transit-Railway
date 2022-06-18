package mtr.sound.bve;

import mtr.MTRClient;
import mtr.client.TrainClientRegistry;
import mtr.sound.TrainLoopingSoundInstance;
import mtr.sound.TrainSoundBase;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class BveTrainSound extends TrainSoundBase {

    public BveTrainSoundConfig config;

    public BveTrainSound() {
        // A constructor without arguments is used by createTrainInstance via reflection.
    }

    public BveTrainSound(BveTrainSoundConfig config) {
        this.config = config;
    }

    private TrainLoopingSoundInstance soundLoopRun;
    private TrainLoopingSoundInstance soundLoopFlange;
    private TrainLoopingSoundInstance soundLoopNoise;
    private TrainLoopingSoundInstance soundLoopShoe;
    private TrainLoopingSoundInstance[] soundLoopMotor;

    private SoundEvent soundEventJoint;
    private SoundEvent soundEventAirZero;
    private SoundEvent soundEventAirHigh;
    private SoundEvent soundEventBrakeHandleApply;
    private SoundEvent soundEventBrakeHandleRelease;
    private SoundEvent soundEventBrakeHandleEmergency;

    private int[][] bogieRailId;
    private float accelLastElapse;
    private boolean onRouteLastElapse = false;

    private int motorCurrentMode = 0;
    private float motorSwitchTimer = -1;

    @Override
    protected void createTrainInstance(TrainSoundBase srcBase) {
        BveTrainSound src = (BveTrainSound) srcBase;
        bogieRailId = new int[train.trainCars][2];
        this.config = src.config;

        if (config.soundCfg.run[0] != null) soundLoopRun = new TrainLoopingSoundInstance(config.audioBaseName + config.soundCfg.run[0], train);
        if (config.soundCfg.flange[0] != null) soundLoopFlange = new TrainLoopingSoundInstance(config.audioBaseName + config.soundCfg.flange[0], train);
        if (config.soundCfg.noise != null) soundLoopNoise = new TrainLoopingSoundInstance(config.audioBaseName + config.soundCfg.noise, train);
        if (config.soundCfg.shoe != null) soundLoopShoe = new TrainLoopingSoundInstance(config.audioBaseName + config.soundCfg.shoe, train);
        soundLoopMotor = new TrainLoopingSoundInstance[config.soundCfg.motor.length];
        for (int i = 0; i < Math.min(config.soundCfg.motor.length, config.motorData.getSoundCount()); ++i) {
            if (config.soundCfg.motor[i] != null) {
                soundLoopMotor[i] = new TrainLoopingSoundInstance(config.audioBaseName + config.soundCfg.motor[i], train);
            }
        }

        if (config.soundCfg.joint[0] != null) soundEventJoint = new SoundEvent(new ResourceLocation(config.audioBaseName + config.soundCfg.joint[0]));
        if (config.soundCfg.airZero != null) soundEventAirZero = new SoundEvent(new ResourceLocation(config.audioBaseName + config.soundCfg.airZero));
        if (config.soundCfg.airHigh != null) soundEventAirHigh = new SoundEvent(new ResourceLocation(config.audioBaseName + config.soundCfg.airHigh));
        if (config.soundCfg.brakeHandleApply != null)
            soundEventBrakeHandleApply = new SoundEvent(new ResourceLocation(config.audioBaseName + config.soundCfg.brakeHandleApply));
        if (config.soundCfg.brakeHandleRelease != null)
            soundEventBrakeHandleRelease = new SoundEvent(new ResourceLocation(config.audioBaseName + config.soundCfg.brakeHandleRelease));
        if (config.soundCfg.brakeEmergency != null)
            soundEventBrakeHandleEmergency = new SoundEvent(new ResourceLocation(config.audioBaseName + config.soundCfg.brakeEmergency));
    }

    @Override
    public void playNearestCar(Level world, BlockPos pos, int carIndex) {
        float accel = (train.speed - train.speedLastElapse) * 20F / (MTRClient.getLastFrameDuration() / 20F);
        float speed = train.speed * 20F;
        float speedKmph = speed * 3.6F;

        // Rolling noise
        if (soundLoopRun != null) {
            float runPitch = speed * 0.04F;
            float runBaseGain = Math.min(1.0F, speed * 0.04F);
            soundLoopRun.setVolumePitch(runBaseGain, runPitch);
            soundLoopRun.setPos(pos);
        }

        // Simulation of circuit breaker in traction controller
        int motorTargetMode = (int)Math.signum(accel);
        if (motorTargetMode < 0 && speed < config.soundCfg.regenerationLimit) {
            motorCurrentMode = 0; // Regeneration brake cut off below limit speed
            motorSwitchTimer = -1;
        } else if (motorTargetMode > 0 && speed < 1F) {
            motorCurrentMode = motorTargetMode; // Disable delay at startup
            motorSwitchTimer = -1;
        } else if (motorTargetMode != motorCurrentMode && motorSwitchTimer < 0) {
            motorSwitchTimer = 0;
            if (motorTargetMode != 0 && motorCurrentMode != 0) {
                motorCurrentMode = 0; // No delay for breaker cut
            }
        }
        if (motorSwitchTimer >= 0) {
            motorSwitchTimer += MTRClient.getLastFrameDuration() / 20F;
            if (motorSwitchTimer > config.soundCfg.breakerDelay) {
                motorSwitchTimer = -1;
                motorCurrentMode = motorTargetMode;
            }
        }

        // Motor noise
        for (int i = 0; i < config.motorData.getSoundCount(); ++i) {
            if (soundLoopMotor[i] == null) continue;
            soundLoopMotor[i].setVolumePitch(
                    config.motorData.getVolume(i, speedKmph, motorCurrentMode) * config.soundCfg.motorVolumeMultiply,
                    config.motorData.getPitch(i, speedKmph, motorCurrentMode)
            );
            soundLoopMotor[i].setPos(pos);
        }

        // TODO Play flange sounds
        // Flange noise
        if (soundLoopFlange != null) {
            soundLoopFlange.setVolumePitch(0, 1);
            soundLoopFlange.setPos(pos);
        }

        // Brake shoe rubbing noise (below regeneration brake cutoff limit)
        if (soundLoopShoe != null) {
            float shoePitch = 1.0F / (speed + 1.0F) + 1.0F;
            float shoeGain = (speed < config.soundCfg.regenerationLimit && accel < 0) ? 1F : 0F;
            if (speed < 1.39) {
                double t = speed * speed;
                shoeGain *= 1.5552 * t - 0.746496 * speed * t;
            } else if (speed > 12.5) {
                double t = speed - 12.5;
                shoeGain *= 1.0 / (0.1 * t * t + 1.0);
            }
            soundLoopShoe.setVolumePitch(shoeGain, shoePitch);
            soundLoopShoe.setPos(pos);
        }

        // Constant loop noise
        if (soundLoopNoise != null) {
            soundLoopNoise.setVolumePitch(train.isOnRoute ? 1 : 0, 1);
            soundLoopNoise.setPos(pos);
        }

        // Air brake application and release noise
        if (accelLastElapse < 0 && accel >= 0) {
            if (soundEventBrakeHandleRelease != null)
                ((ClientLevel) world).playLocalSound(pos, soundEventBrakeHandleRelease, SoundSource.BLOCKS, 1, 1, true);
            if (speed < config.soundCfg.regenerationLimit) {
                if (soundEventAirZero != null)
                    ((ClientLevel) world).playLocalSound(pos, soundEventAirZero, SoundSource.BLOCKS, 1, 1, true);
            }
        } else if (accelLastElapse <= 0 && accel > 0 && speed < 0.3) {
            if (soundEventAirHigh != null)
                ((ClientLevel) world).playLocalSound(pos, soundEventAirHigh, SoundSource.BLOCKS, 1, 1, true);
        } else if (accelLastElapse >= 0 && accel < 0) {
            if (soundEventBrakeHandleApply != null)
                ((ClientLevel) world).playLocalSound(pos, soundEventBrakeHandleApply, SoundSource.BLOCKS, 1, 1, true);
        }

        // Emergency brake application after returning to depot
        if (onRouteLastElapse && !train.isOnRoute && soundEventBrakeHandleEmergency != null) {
            ((ClientLevel) world).playLocalSound(pos, soundEventBrakeHandleEmergency, SoundSource.BLOCKS, 1, 1, true);
        }

        accelLastElapse = accel;
        onRouteLastElapse = train.isOnRoute;
    }

    @Override
    public void playAllCars(Level world, BlockPos pos, int carIndex) {
        final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(train.trainId);
        
        if (soundEventJoint == null || trainProperties.bogiePosition == 0) return;
        float bogieOffsetFront = -1, bogieOffsetRear = -1;
        if (trainProperties.isJacobsBogie) {
            if (carIndex == 0) {
                bogieOffsetFront = train.spacing / 2F - trainProperties.bogiePosition;
            } else if (carIndex == train.trainCars - 1) {
                bogieOffsetFront = 0;
                bogieOffsetRear = train.spacing / 2F + trainProperties.bogiePosition;
            } else {
                bogieOffsetFront = 0;
            }
        } else {
            bogieOffsetFront = train.spacing / 2F - trainProperties.bogiePosition;
            bogieOffsetRear = train.spacing / 2F + trainProperties.bogiePosition;
        }
        float pitch = train.speed * 20F / 12.5F;
        float gain = pitch < 0.5F ? 2.0F * pitch : 1.0F;
        if (bogieOffsetFront >= 0) {
            int indexFront = train.getIndex(train.getRailProgress() - train.spacing * carIndex - bogieOffsetFront, false);
            if (indexFront != bogieRailId[carIndex][0]) {
                bogieRailId[carIndex][0] = indexFront;
                ((ClientLevel) world).playLocalSound(pos, soundEventJoint, SoundSource.BLOCKS, gain, pitch, true);
            }
        }
        if (bogieOffsetRear >= 0) {
            int indexRear = train.getIndex(train.getRailProgress() - train.spacing * carIndex - bogieOffsetRear, false);
            if (indexRear != bogieRailId[carIndex][1]) {
                bogieRailId[carIndex][1] = indexRear;
                ((ClientLevel) world).playLocalSound(pos, soundEventJoint, SoundSource.BLOCKS, gain, pitch, true);
            }
        }
    }

    @Override
    public void playAllCarsDoorOpening(Level world, BlockPos pos, int carIndex) {
        // TODO Check why door sounds are not playing
        if (!(world instanceof ClientLevel)) return;
        final float doorValue = Math.abs(train.rawDoorValue);

        final String soundId;
        if (train.doorValueLastElapse <= 0 && doorValue > 0 && config.soundCfg.doorOpen != null) {
            soundId = config.audioBaseName + config.soundCfg.doorOpen;
        } else if (train.doorValueLastElapse >= config.soundCfg.doorCloseSoundLength && doorValue < config.soundCfg.doorCloseSoundLength
                && config.soundCfg.doorClose != null) {
            soundId = config.audioBaseName + config.soundCfg.doorClose;
        } else {
            soundId = null;
        }
        if (soundId != null) {
            ((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(soundId)), SoundSource.BLOCKS, 1, 1, true);
        }
    }
}
