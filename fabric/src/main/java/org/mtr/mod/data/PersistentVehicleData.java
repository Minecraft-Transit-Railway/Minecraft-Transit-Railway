package org.mtr.mod.data;

import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.data.VehicleExtraData;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.resource.VehicleResource;
import org.mtr.mod.sound.VehicleSoundBase;

public final class PersistentVehicleData {

	private double doorValue;
	private double oldDoorValue;
	private int doorCoolDown;

	public final boolean[] rayTracing;
	public final double[] longestDimensions;
	private final ObjectArrayList<VehicleSoundBase> vehicleSoundBaseList = new ObjectArrayList<>();
	private final ObjectArrayList<ObjectArrayList<ScrollingText>> scrollingTexts = new ObjectArrayList<>();

	public PersistentVehicleData(ObjectImmutableList<VehicleCar> immutableVehicleCars) {
		rayTracing = new boolean[immutableVehicleCars.size()];
		longestDimensions = new double[immutableVehicleCars.size()];
		for (int i = 0; i < immutableVehicleCars.size(); i++) {
			longestDimensions[i] = Math.max(immutableVehicleCars.get(i).getLength(), immutableVehicleCars.get(i).getWidth());
		}
	}

	public ObjectArrayList<ScrollingText> getScrollingText(int car) {
		while (scrollingTexts.size() <= car) {
			scrollingTexts.add(new ObjectArrayList<>());
		}
		return scrollingTexts.get(car);
	}

	public void tick(long millisElapsed, VehicleExtraData vehicleExtraData) {
		oldDoorValue = doorValue;
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

	public void playMotorSound(VehicleResource vehicleResource, int carNumber, int bogieIndex, BlockPos bogiePosition, float speed, float speedChange, float acceleration, boolean isOnRoute) {
		getVehicleSoundBase(vehicleResource, carNumber).playMotorSound(carNumber * 2 + bogieIndex, bogiePosition, speed, speedChange, acceleration, isOnRoute);
	}

	public void playDoorSound(VehicleResource vehicleResource, int carNumber, BlockPos vehiclePosition) {
		getVehicleSoundBase(vehicleResource, carNumber).playDoorSound(vehiclePosition, doorValue, oldDoorValue);
	}

	private VehicleSoundBase getVehicleSoundBase(VehicleResource vehicleResource, int carNumber) {
		while (vehicleSoundBaseList.size() <= carNumber) {
			vehicleSoundBaseList.add(vehicleResource.createVehicleSoundBase.get());
		}
		return vehicleSoundBaseList.get(carNumber);
	}
}
