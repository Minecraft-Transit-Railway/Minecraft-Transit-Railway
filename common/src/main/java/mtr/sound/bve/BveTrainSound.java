package mtr.sound.bve;

import mtr.MTR;
import mtr.MTRClient;
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

    @Override
    protected void copyFrom(TrainSoundBase srcBase) {
        BveTrainSound src = (BveTrainSound) srcBase;
        this.config = src.config;
    }

    @Override
    public void playElapseSound(Level world, BlockPos pos, int carIndex, float radius) {

    }

    @Override
    public void playDoorSound(Level world, BlockPos pos, int carIndex) {
        // TODO Check why door sounds are not playing
        if (!(world instanceof ClientLevel && MTRClient.canPlaySound())) return;
        final float doorValue = Math.abs(train.rawDoorValue);

        final String soundId;
        if (train.doorValueLastElapse <= 0 && doorValue > 0 && config.soundCfg.doorOpen != null) {
            soundId = config.audioBaseName + config.soundCfg.doorOpen;
        } else if (train.doorValueLastElapse >= config.doorCloseSoundTime && doorValue < config.doorCloseSoundTime) {
            soundId = config.audioBaseName + config.soundCfg.doorClose;
        } else {
            soundId = null;
        }
        if (soundId != null) {
            ((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(soundId)), SoundSource.BLOCKS, 1, 1, true);
        }
    }
}
