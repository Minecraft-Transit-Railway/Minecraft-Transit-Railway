package org.mtr.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;

public class VehicleLoopingSoundInstance extends AbstractTickableSoundInstance {

	public VehicleLoopingSoundInstance(SoundEvent event) {
		super(event, SoundSource.BLOCKS, new SingleThreadedRandomSource(0));
		looping = true;
		delay = 0;
		volume = 0;
		pitch = 1;
	}

	public void setData(float volume, float pitch, BlockPos blockPos) {
		this.pitch = pitch == 0 ? 1 : pitch;
		this.volume = volume;
		x = blockPos.getX();
		y = blockPos.getY();
		z = blockPos.getZ();

		final SoundManager soundManager = Minecraft.getInstance().getSoundManager();
		if (soundManager.isActive(this)) {
			if (volume <= 0) {
				soundManager.stop(this);
			}
		} else {
			if (volume > 0) {
				looping = true;
				soundManager.play(this);
			}
		}
	}

	@Override
	public void tick() {
	}

	@Override
	public boolean canStartSilent() {
		return true;
	}

	@Override
	public boolean canPlaySound() {
		return true;
	}

	public void dispose() {
		stop();
		Minecraft.getInstance().getSoundManager().stop(this);
	}
}
