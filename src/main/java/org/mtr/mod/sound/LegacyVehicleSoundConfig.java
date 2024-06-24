package org.mtr.mod.sound;

import javax.annotation.Nullable;

public class LegacyVehicleSoundConfig {

	public final String doorSoundBaseId;
	public final int speedSoundCount;
	public final float doorCloseSoundTime;
	public final boolean useAccelerationSoundsWhenCoasting;
	public final boolean constantPlaybackSpeed;

	public LegacyVehicleSoundConfig(@Nullable String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting, boolean constantPlaybackSpeed) {
		this.doorSoundBaseId = doorSoundBaseId;
		this.speedSoundCount = speedSoundCount;
		this.doorCloseSoundTime = doorCloseSoundTime;
		this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
		this.constantPlaybackSpeed = constantPlaybackSpeed;
	}

	public LegacyVehicleSoundConfig(@Nullable String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
		this.doorSoundBaseId = doorSoundBaseId;
		this.speedSoundCount = speedSoundCount;
		this.doorCloseSoundTime = doorCloseSoundTime;
		this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
		constantPlaybackSpeed = false;
	}
}
