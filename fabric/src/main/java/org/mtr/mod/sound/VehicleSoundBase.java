package org.mtr.mod.sound;

import org.mtr.mapping.holder.*;

import javax.annotation.Nullable;

public abstract class VehicleSoundBase {

	public abstract void playMotorSound(int bogieIndex, BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute);

	public final void playDoorSound(BlockPos blockPos, double doorValue, double oldDoorValue) {
		if (doorValue > 0 && oldDoorValue == 0) {
			playDoorSound(blockPos, true);
		}
		if (doorValue < getDoorCloseSoundTime() && oldDoorValue >= getDoorCloseSoundTime()) {
			playDoorSound(blockPos, false);
		}
	}

	protected abstract void playDoorSound(BlockPos blockPos, boolean isOpen);

	protected abstract double getDoorCloseSoundTime();

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
