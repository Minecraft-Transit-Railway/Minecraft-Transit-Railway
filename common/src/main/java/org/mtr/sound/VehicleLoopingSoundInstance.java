package org.mtr.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.LocalRandom;

public class VehicleLoopingSoundInstance extends MovingSoundInstance {

	public VehicleLoopingSoundInstance(SoundEvent event) {
		super(event, SoundCategory.BLOCKS, new LocalRandom(0));
		repeat = true;
		repeatDelay = 0;
		volume = 0;
		pitch = 1;
	}

	public void setData(float volume, float pitch, BlockPos blockPos) {
		this.pitch = pitch == 0 ? 1 : pitch;
		this.volume = volume;
		x = blockPos.getX();
		y = blockPos.getY();
		z = blockPos.getZ();

		final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
		if (volume > 0 && !soundManager.isPlaying(this)) {
			repeat = true;
			soundManager.play(this);
		}
	}

	@Override
	public void tick() {
	}

	@Override
	public boolean shouldAlwaysPlay() {
		return true;
	}

	@Override
	public boolean canPlay() {
		return true;
	}

	public void dispose() {
		setDone();
	}
}
