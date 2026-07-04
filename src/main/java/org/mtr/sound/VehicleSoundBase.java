package org.mtr.sound;

import net.minecraft.core.BlockPos;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.Rail;
import org.mtr.resource.RailResource;

public abstract class VehicleSoundBase {

	public abstract void playVehicleSound(VehicleSoundParameters vehicleSoundParameters);

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

	public record VehicleSoundParameters(RunSoundInfo runSound, BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute) {};

	public record RunSoundInfo(int index, int nextIndex, float blendLevel) {
		public static RunSoundInfo create(Rail railTail, Rail railHead, float pathDelta) {
			int lastRunSound = 0;
			if(!railTail.getStyles().isEmpty()) {
				RailResource railResource = CustomResourceLoader.getRailById(railTail.getStyles().getFirst());
				if(railResource != null) lastRunSound = railResource.getSoundIndex();
			}
			int nextRunSound = lastRunSound;
			if(!railHead.equals(railTail) && !railHead.getStyles().isEmpty()) {
				RailResource railResource = CustomResourceLoader.getRailById(railHead.getStyles().getFirst());
				if(railResource != null) nextRunSound = railResource.getSoundIndex();
			}
			return new RunSoundInfo(lastRunSound, nextRunSound, pathDelta);
		}
	}
}
