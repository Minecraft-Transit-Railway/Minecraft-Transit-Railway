package org.mtr.mod.sound;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MovingSoundInstanceExtension;

public class VehicleLoopingSoundInstance extends MovingSoundInstanceExtension {

	private int cooldown;
	private static final int MAX_DISTANCE = 64;

	public VehicleLoopingSoundInstance(SoundEvent event) {
		super(event, SoundCategory.getBlocksMapped());
		setIsRepeatableMapped(true);
		setRepeatDelay(0);
		setVolume(0);
		setPitch(1);
	}

	public void setData(float volume, float pitch, BlockPos blockPos) {
		cooldown = 20;
		setPitch(pitch == 0 ? 1 : pitch);
		setVolume(volume);

		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		if (clientPlayerEntity != null && clientPlayerEntity.getBlockPos().getManhattanDistance(new Vector3i(blockPos.data)) < MAX_DISTANCE) {
			setX(blockPos.getX());
			setY(blockPos.getY());
			setZ(blockPos.getZ());
		}

		final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
		if (volume > 0 && !soundManager.isPlaying(new SoundInstance(this))) {
			setIsRepeatableMapped(true);
			soundManager.play(new SoundInstance(this));
		}
	}

	@Override
	public void tick2() {
		if (cooldown == 0) {
			setDone2();
		} else {
			cooldown--;
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
