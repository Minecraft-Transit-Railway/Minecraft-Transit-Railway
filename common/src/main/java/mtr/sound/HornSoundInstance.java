package mtr.sound;

import mtr.MTR;
import mtr.data.RailwayData;
import mtr.mappings.RegistryUtilities;
import mtr.mappings.SoundInstanceMapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.TickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;

public class HornSoundInstance extends SoundInstanceMapper implements TickableSoundInstance {

    private static final int MAX_DISTANCE = 32;
    private boolean stopped;

    public HornSoundInstance(String soundId) {
        super(RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS);
        looping = true;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    @Override
    public void tick() {

    }

    public void play(BlockPos pos) {
        final LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        final BlockPos playerPos = player.blockPosition();
        final int distance = playerPos.distManhattan(pos);

        if (distance <= MAX_DISTANCE) {
            final int currentDistance = playerPos.distManhattan(RailwayData.newBlockPos(x, y, z));

            if (distance < currentDistance) {
                x = pos.getX();
                y = pos.getY();
                z = pos.getZ();
            }

            final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
            if (soundManager != null && !soundManager.isActive(this)) {
                soundManager.play(this);
                stopped = false;
            }
        }
    }

    public void stop() {
        final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
        if (soundManager != null && soundManager.isActive(this)) {
            soundManager.stop(this);
            stopped = true;
        }
    }
}
