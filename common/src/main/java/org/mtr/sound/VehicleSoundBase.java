package org.mtr.mod.sound;

import org.mtr.mapping.holder.BlockPos;

public abstract class VehicleSoundBase {

	public abstract void playMotorSound(BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute);

	public final void playDoorSound(BlockPos blockPos, double doorValue, double oldDoorValue) {
		if (doorValue > 0 && oldDoorValue == 0) {
			playDoorSound(blockPos, true);
		}
		if (doorValue < getDoorCloseSoundTime() && oldDoorValue >= getDoorCloseSoundTime()) {
			playDoorSound(blockPos, false);
		}
	}

	public abstract void dispose();

	protected abstract void playDoorSound(BlockPos blockPos, boolean isOpen);

	protected abstract double getDoorCloseSoundTime();
}
