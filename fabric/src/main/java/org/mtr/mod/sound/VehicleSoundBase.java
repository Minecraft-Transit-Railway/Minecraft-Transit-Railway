package org.mtr.mod.sound;

import org.mtr.mapping.holder.*;

import javax.annotation.Nullable;

public abstract class VehicleSoundBase {

	public abstract void playSound(int bogieIndex, BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute);

	protected static void playSoundInWorld(@Nullable SoundEvent soundEvent, BlockPos blockPos, float gain, float pitch) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld != null && soundEvent != null) {
			clientWorld.playSoundAtBlockCenter(blockPos, soundEvent, SoundCategory.getBlocksMapped(), Math.min(1, gain), pitch, false);
		}
	}

	protected static void playSoundInWorld(@Nullable SoundEvent soundEvent, BlockPos blockPos) {
		playSoundInWorld(soundEvent, blockPos, 1, 1);
	}
}
