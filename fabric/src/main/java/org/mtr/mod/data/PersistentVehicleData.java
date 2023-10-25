package org.mtr.mod.data;

import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleExtraData;
import org.mtr.core.tools.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mod.client.ScrollingText;

public final class PersistentVehicleData {

	private double doorValue;
	private final ObjectArrayList<ObjectArrayList<ScrollingText>> scrollingTexts = new ObjectArrayList<>();

	public ObjectArrayList<ScrollingText> getScrollingText(int car) {
		while (scrollingTexts.size() <= car) {
			scrollingTexts.add(new ObjectArrayList<>());
		}
		return scrollingTexts.get(car);
	}

	public void tick(long millisElapsed, VehicleExtraData vehicleExtraData) {
		doorValue = Utilities.clamp(doorValue + (double) (millisElapsed * vehicleExtraData.getDoorMultiplier()) / Vehicle.DOOR_MOVE_TIME, 0, 1);
	}

	public double getDoorValue() {
		return doorValue;
	}
}
