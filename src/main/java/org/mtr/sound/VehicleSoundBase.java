package org.mtr.sound;

import net.minecraft.core.BlockPos;
import org.jspecify.annotations.Nullable;
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

	public record VehicleSoundParameters(@Nullable RunSoundInfo runSound, BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute) {
	}

	/**
	 * Describes which rail run sound indices to play and how to blend between them when transitioning across different rail types.
	 *
	 * @param index      the current rail sound index (tail of the car)
	 * @param nextIndex  the next rail sound index to blend into (head of the car)
	 * @param blendLevel how far along the transition between rail types (0 = fully on tail rail, 1 = fully on head rail)
	 */
	public record RunSoundInfo(int index, int nextIndex, float blendLevel) {
		public static RunSoundInfo create(Rail railTail, Rail railHead, float pathDelta) {
			final RailResource tailResource = railTail.getStyles().isEmpty() ? null : CustomResourceLoader.getRailById(railTail.getStyles().getFirst());
			final int lastRunSound = tailResource == null ? 0 : tailResource.getSoundIndex();

			if (railHead.equals(railTail)) {
				return new RunSoundInfo(lastRunSound, lastRunSound, pathDelta);
			}

			final RailResource headResource = railHead.getStyles().isEmpty() ? null : CustomResourceLoader.getRailById(railHead.getStyles().getFirst());
			final int nextRunSound = headResource == null ? lastRunSound : headResource.getSoundIndex();
			return new RunSoundInfo(lastRunSound, nextRunSound, pathDelta);
		}
	}
}
