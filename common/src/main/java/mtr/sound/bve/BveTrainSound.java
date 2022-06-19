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

import java.util.concurrent.ThreadLocalRandom;

public class BveTrainSound extends TrainSoundBase {

    public BveTrainSoundConfig config;

    public BveTrainSound() {
        // A constructor without arguments is used by createTrainInstance via reflection.
    }

    public BveTrainSound(BveTrainSoundConfig config) {
        this.config = config;
    }

    private TrainLoopingSoundInstance[] soundLoopMotor;
    private TrainLoopingSoundInstance soundLoopRun;
    private TrainLoopingSoundInstance soundLoopFlange;
    private TrainLoopingSoundInstance soundLoopNoise;
    private TrainLoopingSoundInstance soundLoopShoe;
    private TrainLoopingSoundInstance soundLoopCompressor;

    private int[][] bogieRailId;
    private float accelLastElapse;
    private boolean onRouteLastElapse = false;

    private int motorCurrentMode = 0;
    private float motorSwitchTimer = -1;

    private int mrPress;
    private boolean isCompressorActive;
    private boolean isCompressorActiveLastElapse;

    @Override
    protected void createTrainInstance(TrainSoundBase srcBase) {
        BveTrainSound src = (BveTrainSound) srcBase;
        bogieRailId = new int[train.trainCars][2];
        this.config = src.config;
        mrPress = ThreadLocalRandom.current().nextInt(config.soundCfg.mrPressMin, config.soundCfg.mrPressMax + 1);
        isCompressorActive = ThreadLocalRandom.current().nextInt(0, 20) == 0; // Currently, set to 1/20 at client-side load
        isCompressorActiveLastElapse = isCompressorActive;

        if (config.soundCfg.run[0] != null) soundLoopRun = new TrainLoopingSoundInstance(config.soundCfg.run[0], train);
        if (config.soundCfg.flange[0] != null) soundLoopFlange = new TrainLoopingSoundInstance(config.soundCfg.flange[0], train);
        if (config.soundCfg.noise != null) soundLoopNoise = new TrainLoopingSoundInstance(config.soundCfg.noise, train);
        if (config.soundCfg.shoe != null) soundLoopShoe = new TrainLoopingSoundInstance(config.soundCfg.shoe, train);
        soundLoopMotor = new TrainLoopingSoundInstance[config.soundCfg.motor.length];
        for (int i = 0; i < Math.min(config.soundCfg.motor.length, config.motorData.getSoundCount()); ++i) {
            if (config.soundCfg.motor[i] != null) {
                soundLoopMotor[i] = new TrainLoopingSoundInstance(config.soundCfg.motor[i], train);
            }
        }
    }

    @Override
    public void playNearestCar(Level world, BlockPos pos, int carIndex) {
        float deltaT = MTRClient.getLastFrameDuration() / 20F;
        float accel = (train.speed - train.speedLastElapse) * 20F / deltaT;
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
            soundLoopCompressor.setVolumePitch(isCompressorActive ? 1 : 0, 1);
            soundLoopCompressor.setPos(pos);
        }
        if (isCompressorActive && !isCompressorActiveLastElapse) {
            playLocalSound(world, config.soundCfg.compressorAttack, pos);
        } else if (!isCompressorActive && isCompressorActiveLastElapse) {
            playLocalSound(world, config.soundCfg.compressorRelease, pos);
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
            playLocalSound(world, config.soundCfg.brakeHandleRelease, pos);
            if (speed < config.soundCfg.regenerationLimit) {
                playLocalSound(world, config.soundCfg.airZero, pos);
            }
        } else if (accelLastElapse <= 0 && accel > 0 && speed < 0.3) {
            playLocalSound(world, config.soundCfg.airHigh, pos);
        } else if (accelLastElapse >= 0 && accel < 0) {
            mrPress -= config.soundCfg.mrServiceBrakeReduce;
            playLocalSound(world, config.soundCfg.brakeHandleApply, pos);
        }

        // Emergency brake application after returning to depot
        if (onRouteLastElapse && !train.isOnRoute) {
            playLocalSound(world, config.soundCfg.brakeEmergency, pos);
        }

        accelLastElapse = accel;
        onRouteLastElapse = train.isOnRoute;
        isCompressorActiveLastElapse = isCompressorActive;
    }

    @Override
    public void playAllCars(Level world, BlockPos pos, int carIndex) {
        final TrainClientRegistry.TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(train.trainId);
        
        if (config.soundCfg.joint[0] == null || trainProperties.bogiePosition == 0) return;
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
                playLocalSound(world, config.soundCfg.joint[0], pos, gain, pitch);
            }
        }
        if (bogieOffsetRear >= 0) {
            int indexRear = train.getIndex(train.getRailProgress() - train.spacing * carIndex - bogieOffsetRear, false);
            if (indexRear != bogieRailId[carIndex][1]) {
                bogieRailId[carIndex][1] = indexRear;
                playLocalSound(world, config.soundCfg.joint[0], pos, gain, pitch);
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

    private static void playLocalSound(Level world, SoundEvent event, BlockPos pos, float gain, float pitch) {
        if (event == null) return;
        ((ClientLevel) world).playLocalSound(pos, event, SoundSource.BLOCKS, gain, pitch, true);
    }

    private static void playLocalSound(Level world, SoundEvent event, BlockPos pos) {
        if (event == null) return;
        ((ClientLevel) world).playLocalSound(pos, event, SoundSource.BLOCKS, 1, 1, true);
    }
}
