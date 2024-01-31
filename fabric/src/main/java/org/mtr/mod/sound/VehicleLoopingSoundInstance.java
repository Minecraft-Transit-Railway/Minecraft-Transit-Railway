package org.mtr.mod.sound;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MovingSoundInstanceExtension;

public class VehicleLoopingSoundInstance extends MovingSoundInstanceExtension {

	private int coolDown;

	public VehicleLoopingSoundInstance(SoundEvent event) {
		super(event, SoundCategory.getBlocksMapped());
		setIsRepeatableMapped(true);
		setRepeatDelay(0);
		setVolume(0);
		setPitch(1);
	}

	public void setData(float volume, float pitch, BlockPos blockPos) {
		coolDown = 20;
		setPitch(pitch == 0 ? 1 : pitch);
		setVolume(volume);

		setX(blockPos.getX());
		setY(blockPos.getY());
		setZ(blockPos.getZ());

		final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
		if (volume > 0 && !soundManager.isPlaying(new SoundInstance(this))) {
			setIsRepeatableMapped(true);
			soundManager.play(new SoundInstance(this));
		}
	}

	@Override
	public void tick2() {
		if (coolDown == 0) {
			setDone();
		} else {
			coolDown--;
		}
	}

	@Override
	public boolean shouldAlwaysPlay2() {
		return true;
	}

	@Override
	public boolean canPlay2() {
		return true;
	}
}
