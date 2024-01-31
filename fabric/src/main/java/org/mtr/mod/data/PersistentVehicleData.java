package org.mtr.mod.data;

import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleExtraData;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.resource.VehicleResource;
import org.mtr.mod.sound.VehicleSoundBase;

public final class PersistentVehicleData {

	private double doorValue;
	private int doorCoolDown;
	private final ObjectArrayList<VehicleSoundBase> vehicleSoundBaseList = new ObjectArrayList<>();
	private final ObjectArrayList<ObjectArrayList<ScrollingText>> scrollingTexts = new ObjectArrayList<>();

	public ObjectArrayList<ScrollingText> getScrollingText(int car) {
		while (scrollingTexts.size() <= car) {
			scrollingTexts.add(new ObjectArrayList<>());
		}
		return scrollingTexts.get(car);
	}

	public void tick(long millisElapsed, VehicleExtraData vehicleExtraData) {
		doorValue = Utilities.clamp(doorValue + (double) (millisElapsed * vehicleExtraData.getDoorMultiplier()) / Vehicle.DOOR_MOVE_TIME, 0, 1);
		if (checkCanOpenDoors()) {
			doorCoolDown--;
		}
		if (doorValue > 0) {
			doorCoolDown = 2;
		}
	}

	public double getDoorValue() {
		return doorValue;
	}

	public boolean checkCanOpenDoors() {
		return doorCoolDown > 0;
	}

	public void playSound(VehicleResource vehicleResource, int carNumber, int bogieIndex, BlockPos bogiePosition, float speed, float speedChange, float acceleration, boolean isOnRoute) {
		while (vehicleSoundBaseList.size() <= carNumber) {
			vehicleSoundBaseList.add(vehicleResource.createVehicleSoundBase.get());
		}
		vehicleSoundBaseList.get(carNumber).playSound(carNumber * 2 + bogieIndex, bogiePosition, speed, speedChange, acceleration, isOnRoute);
	}
}
