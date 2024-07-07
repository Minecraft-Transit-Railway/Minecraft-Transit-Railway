package org.mtr.mod.sound;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.AbstractSoundInstanceExtension;
import org.mtr.mapping.mapper.SoundHelper;
import org.mtr.mapping.mapper.TickableSoundInstanceExtension;
import org.mtr.mod.Init;

public class LoopingSoundInstance extends AbstractSoundInstanceExtension implements TickableSoundInstanceExtension {

	private static final int MAX_DISTANCE = 32;

	public LoopingSoundInstance(String soundId) {
		super(SoundHelper.createSoundEvent(new Identifier(Init.MOD_ID, soundId)), SoundCategory.getBlocksMapped());
		setIsRepeatableMapped(true);
	}

	@Override
	public boolean isDone() {
		return false;
	}

	@Override
	public void tick2() {
	}

	public void setPos(BlockPos blockPos, boolean isRemoved) {
		if (isRemoved) {
			if (getXMapped() == blockPos.getX() && getYMapped() == blockPos.getY() && getZMapped() == blockPos.getZ()) {
				setXMapped(0);
				setYMapped(Integer.MAX_VALUE);
				setZMapped(0);
			}
		} else {
			final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
			if (clientPlayerEntity == null) {
				return;
			}

			final BlockPos playerPos = clientPlayerEntity.getBlockPos();
			final int distance = playerPos.getManhattanDistance(new Vector3i(blockPos.data));

			if (distance <= MAX_DISTANCE) {
				final int currentDistance = playerPos.getManhattanDistance(new Vector3i(MathHelper.floor(getXMapped()), MathHelper.floor(getYMapped()), MathHelper.floor(getZMapped())));

				if (distance < currentDistance) {
					setXMapped(blockPos.getX());
					setYMapped(blockPos.getY());
					setZMapped(blockPos.getZ());
				}

				final SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
				if (!soundManager.isPlaying(new SoundInstance(this))) {
					soundManager.play(new SoundInstance(this));
				}
			}
		}
	}
}
