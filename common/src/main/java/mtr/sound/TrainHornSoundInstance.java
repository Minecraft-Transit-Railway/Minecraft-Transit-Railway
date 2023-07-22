package mtr.sound;

import mtr.data.TrainClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundEvent;

public class TrainHornSoundInstance extends TrainLoopingSoundInstance {

    public TrainHornSoundInstance(SoundEvent event, TrainClient train) {
        super(event, train);
    }

    public void stopHorn() {
        final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        soundManager.stop(this);
    }
}
