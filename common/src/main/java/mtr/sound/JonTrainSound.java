package mtr.sound;

import mtr.MTR;
import mtr.MTRClient;
import mtr.data.Train;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.Random;

public class JonTrainSound extends TrainSoundBase {

    public String speedSoundBaseId;
    public String doorSoundBaseId;
    public int speedSoundCount;
    public float doorCloseSoundTime;
    private boolean useAccelerationSoundsWhenCoasting;

    private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
    private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

    private static final String SOUND_ACCELERATION = "_acceleration_";
    private static final String SOUND_DECELERATION = "_deceleration_";
    private static final String SOUND_DOOR_OPEN = "_door_open";
    private static final String SOUND_DOOR_CLOSE = "_door_close";
    private static final String SOUND_RANDOM = "_random";
    private static final int RANDOM_SOUND_CHANCE = 300;

    public JonTrainSound() {
        // A constructor without arguments is used by createTrainInstance via reflection.
    }

    public JonTrainSound(String speedSoundBaseId, String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
        this.speedSoundBaseId = speedSoundBaseId;
        this.doorSoundBaseId = doorSoundBaseId;
        this.speedSoundCount = speedSoundCount;
        this.doorCloseSoundTime = doorCloseSoundTime;
        this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
    }

    @Override
    protected void copyFrom(TrainSoundBase srcBase) {
        JonTrainSound src = (JonTrainSound)srcBase;
        this.speedSoundBaseId = src.speedSoundBaseId;
        this.doorSoundBaseId = src.doorSoundBaseId;
        this.speedSoundCount = src.speedSoundCount;
        this.doorCloseSoundTime = src.doorCloseSoundTime;
        this.useAccelerationSoundsWhenCoasting = src.useAccelerationSoundsWhenCoasting;
    }

    @Override
    public void playElapseSound(Level world, BlockPos pos) {
        if (!(world instanceof ClientLevel && MTRClient.canPlaySound())) return;
        if (speedSoundCount > 0 && speedSoundBaseId != null) {
            // TODO: Better sound system to adapt to different acceleration
            final int floorSpeed = (int) Math.floor(train.speed / Train.ACCELERATION_DEFAULT / MTRClient.TICKS_PER_SPEED_SOUND);
            if (floorSpeed > 0) {
                final Random random = new Random();

                if (floorSpeed >= 30 && random.nextInt(RANDOM_SOUND_CHANCE) == 0) {
                    ((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, speedSoundBaseId + SOUND_RANDOM)), SoundSource.BLOCKS, 10, 1, true);
                }

                final int index = Math.min(floorSpeed, speedSoundCount) - 1;
                final boolean isAccelerating = train.speed == train.speedLastElapse ? useAccelerationSoundsWhenCoasting || random.nextBoolean() : train.speed > train.speedLastElapse;
                final String soundId = speedSoundBaseId + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
                ((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, true);
            }
        }
    }

    @Override
    public void playDoorSound(Level world, BlockPos pos) {
        // TODO Check why door sounds are not playing
        if (!(world instanceof ClientLevel && MTRClient.canPlaySound())) return;
        final float doorValue = Math.abs(train.rawDoorValue);
        if (doorSoundBaseId != null) {
            final String soundId;
            if (train.doorValueLastElapse <= 0 && doorValue > 0) {
                soundId = doorSoundBaseId + SOUND_DOOR_OPEN;
            } else if (train.doorValueLastElapse >= doorCloseSoundTime && doorValue < doorCloseSoundTime) {
                soundId = doorSoundBaseId + SOUND_DOOR_CLOSE;
            } else {
                soundId = null;
            }
            if (soundId != null) {
                ((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, true);
            }
        }
    }
}
