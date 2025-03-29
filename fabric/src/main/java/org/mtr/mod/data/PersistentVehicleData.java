package org.mtr.mod.data;

import org.mtr.core.data.TransportMode;
import org.mtr.core.data.Vehicle;
import org.mtr.core.data.VehicleCar;
import org.mtr.core.data.VehicleExtraData;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mod.client.Oscillation;
import org.mtr.mod.client.ScrollingText;
import org.mtr.mod.resource.VehicleResource;
import org.mtr.mod.sound.VehicleSoundBase;

import java.util.function.Supplier;

public final class PersistentVehicleData {

	private double doorValue;
	private double oldDoorValue;
	private double nextAnnouncementRailProgress;
	private int doorCooldown;
	private int overrideDoorMultiplier;

	public final boolean[] rayTracing;
	public final double[] longestDimensions;
	private final TransportMode transportMode;
	private final ObjectArrayList<VehicleSoundBase> vehicleSoundBaseList = new ObjectArrayList<>();
	private final ObjectArrayList<ObjectArrayList<ScrollingText>> scrollingTexts = new ObjectArrayList<>();
	private final ObjectArrayList<Oscillation> oscillations = new ObjectArrayList<>();

	public PersistentVehicleData(ObjectImmutableList<VehicleCar> immutableVehicleCars, TransportMode transportMode) {
		rayTracing = new boolean[immutableVehicleCars.size()];
		longestDimensions = new double[immutableVehicleCars.size()];
		for (int i = 0; i < immutableVehicleCars.size(); i++) {
			longestDimensions[i] = Math.max(immutableVehicleCars.get(i).getLength(), immutableVehicleCars.get(i).getWidth());
		}
		this.transportMode = transportMode;
	}

	public ObjectArrayList<ScrollingText> getScrollingText(int carNumber) {
		return getElement(scrollingTexts, carNumber, ObjectArrayList::new);
	}

	public Oscillation getOscillation(int carNumber) {
		return getElement(oscillations, carNumber, () -> new Oscillation(transportMode));
	}

	public void tick(double railProgress, long millisElapsed, VehicleExtraData vehicleExtraData) {
		oldDoorValue = doorValue;
		doorValue = Utilities.clamp(doorValue + (double) (millisElapsed * getAdjustedDoorMultiplier(vehicleExtraData)) / Vehicle.DOOR_MOVE_TIME, 0, 1);
		if (checkCanOpenDoors()) {
			doorCooldown--;
		} else {
			overrideDoorMultiplier = 0;
		}
		if (doorValue > 0) {
			doorCooldown = 2;
			nextAnnouncementRailProgress = railProgress + vehicleExtraData.getTotalVehicleLength() * 1.5;
		}
		oscillations.forEach(oscillation -> oscillation.tick(millisElapsed));
	}

	public double getDoorValue() {
		return doorValue;
	}

	public boolean checkCanOpenDoors() {
		return doorCooldown > 0;
	}

	/**
	 * Get the actual door value, including any overridden value, for example when debugging a train from the Resource Pack Creator.
	 */
	public int getAdjustedDoorMultiplier(VehicleExtraData vehicleExtraData) {
		return overrideDoorMultiplier != 0 ? overrideDoorMultiplier : vehicleExtraData.getDoorMultiplier();
	}

	/**
	 * Override the door value, for example when debugging a train from the Resource Pack Creator. This must be called every tick.
	 *
	 * @param overrideDoorMultiplier {@code 1} for open and {@code -1} for close
	 */
	public void overrideDoorMultiplier(int overrideDoorMultiplier) {
		this.overrideDoorMultiplier = overrideDoorMultiplier;
	}

	public boolean canAnnounce(double oldRailProgress, double railProgress) {
		return oldRailProgress < nextAnnouncementRailProgress && railProgress >= nextAnnouncementRailProgress;
	}

	public void playMotorSound(VehicleResource vehicleResource, int carNumber, BlockPos bogiePosition, float speed, float speedChange, float acceleration, boolean isOnRoute) {
		getVehicleSoundBase(vehicleResource, carNumber).playMotorSound(bogiePosition, speed, speedChange, acceleration, isOnRoute);
	}

	public void playDoorSound(VehicleResource vehicleResource, int carNumber, BlockPos vehiclePosition) {
		getVehicleSoundBase(vehicleResource, carNumber).playDoorSound(vehiclePosition, doorValue, oldDoorValue);
	}

	private VehicleSoundBase getVehicleSoundBase(VehicleResource vehicleResource, int carNumber) {
		return getElement(vehicleSoundBaseList, carNumber, vehicleResource.createVehicleSoundBase);
	}

	public void dispose() {
		for (VehicleSoundBase sounds : vehicleSoundBaseList) {
			sounds.dispose();
		}
	}

	private static <T> T getElement(ObjectArrayList<T> list, int index, Supplier<T> supplier) {
		while (list.size() <= index) {
			list.add(supplier.get());
		}
		return list.get(index);
	}
}
