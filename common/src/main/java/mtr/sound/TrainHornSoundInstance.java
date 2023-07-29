package mtr.sound;

import mtr.data.TrainClient;
import mtr.render.RenderTrains;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;

public class TrainHornSoundInstance extends TrainLoopingSoundInstance {

    public TrainHornSoundInstance(SoundEvent event, TrainClient train) {
        super(event, train);
    }

    public void setData(BlockPos pos) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final BlockPos playerPos = player.blockPosition();
        final int distance = playerPos.distManhattan(pos);
        final int MAX_DISTANCE = RenderTrains.maxTrainRenderDistance;
        float volume = Mth.clamp(1 - (float) distance / MAX_DISTANCE, 0, 1);

        super.setData(volume, 1, pos);
    }

    public void stopHorn(TrainClient train) {
        if (train == this.train) {
            final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
            soundManager.stop(this);
        }
    }
}
