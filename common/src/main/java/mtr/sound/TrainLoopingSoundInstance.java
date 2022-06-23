package mtr.sound;

import mtr.MTR;
import mtr.data.TrainClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class TrainLoopingSoundInstance extends AbstractTickableSoundInstance {

    TrainClient binding;

    public TrainLoopingSoundInstance(SoundEvent event, TrainClient binding) {
        super(event, SoundSource.BLOCKS);
        this.looping = true;
        this.binding = binding;
        this.delay = 0;
        this.volume = 0.0f;
        this.pitch = 1.0f;
    }

    public void setVolumePitch(float volume, float pitch) {
        this.pitch = pitch;
        if (this.pitch == 0) this.pitch = 1;
        this.volume = volume;
    }

    public void setPos(BlockPos pos) {
        x = pos.getX();
        y = pos.getY();
        z = pos.getZ();

        final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if (soundManager != null && !binding.isRemoved && this.volume > 0 && !soundManager.isActive(this)) {
            this.looping = true;
            soundManager.play(this);
        }
    }

    @Override
    public void tick() {
        if (binding.isRemoved) {
            this.stop();
        }
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return true;
    }
}
