package org.mtr.mod.sound;

import javax.annotation.Nullable;

public class LegacyVehicleSound extends VehicleSoundBase {

	public final String soundId;
	public final LegacyVehicleSoundConfig config;

	public LegacyVehicleSound(@Nullable String soundId, LegacyVehicleSoundConfig config) {
		this.soundId = soundId;
		this.config = config;
	}
}
