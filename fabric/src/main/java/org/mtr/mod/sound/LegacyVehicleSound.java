package org.mtr.mod.sound;

import org.mtr.core.data.Siding;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.mapper.AbstractSoundInstanceExtension;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;

import javax.annotation.Nullable;
import java.util.Random;

public class LegacyVehicleSound extends VehicleSoundBase {

	private final String legacySpeedSoundBaseResource;
	private final int legacySpeedSoundCount;
	private final boolean legacyUseAccelerationSoundsWhenCoasting;
	private final boolean legacyConstantPlaybackSpeed;
	private final String legacyDoorSoundBaseResource;
	private final double legacyDoorCloseSoundTime;

	private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
	private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

	private static final String SOUND_ACCELERATION = "_acceleration_";
	private static final String SOUND_DECELERATION = "_deceleration_";
	private static final String SOUND_DOOR_OPEN = "_door_open";
	private static final String SOUND_DOOR_CLOSE = "_door_close";
	private static final String SOUND_RANDOM = "_random";
	private static final int RANDOM_SOUND_CHANCE = 300;

	public LegacyVehicleSound(@Nullable String legacySpeedSoundBaseResource, int legacySpeedSoundCount, boolean legacyUseAccelerationSoundsWhenCoasting, boolean legacyConstantPlaybackSpeed, String legacyDoorSoundBaseResource, double legacyDoorCloseSoundTime) {
		this.legacySpeedSoundBaseResource = legacySpeedSoundBaseResource;
		this.legacySpeedSoundCount = legacySpeedSoundCount;
		this.legacyUseAccelerationSoundsWhenCoasting = legacyUseAccelerationSoundsWhenCoasting;
		this.legacyConstantPlaybackSpeed = legacyConstantPlaybackSpeed;
		this.legacyDoorSoundBaseResource = legacyDoorSoundBaseResource;
		this.legacyDoorCloseSoundTime = legacyDoorCloseSoundTime;
	}

	@Override
	public void playMotorSound(int bogieIndex, BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute) {
		if (!InitClient.canPlaySound()) {
			return;
		}

		if (legacySpeedSoundCount > 0 && legacySpeedSoundBaseResource != null) {
			final double referenceAcceleration = legacyConstantPlaybackSpeed ? acceleration : Siding.ACCELERATION_DEFAULT;
			final int floorSpeed = (int) Math.floor(speed / referenceAcceleration / InitClient.MILLIS_PER_SPEED_SOUND);
			if (floorSpeed > 0) {
				final Random random = new Random();

				if (floorSpeed >= 30 && random.nextInt(RANDOM_SOUND_CHANCE) == 0) {
					playSoundInWorld(AbstractSoundInstanceExtension.createSoundEvent(new Identifier(Init.MOD_ID, legacySpeedSoundBaseResource + SOUND_RANDOM)), blockPos, 10, 1);
				}

				final int index = Math.min(floorSpeed, legacySpeedSoundCount) - 1;
				final boolean isAccelerating = speedChange == 0 ? legacyUseAccelerationSoundsWhenCoasting || random.nextBoolean() : speedChange > 0;
				final String speedSoundId = legacySpeedSoundBaseResource + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
				playSoundInWorld(AbstractSoundInstanceExtension.createSoundEvent(new Identifier(Init.MOD_ID, speedSoundId)), blockPos, 1, 1);
			}
		}
	}

	@Override
	protected void playDoorSound(BlockPos blockPos, boolean isOpen) {
		playSoundInWorld(AbstractSoundInstanceExtension.createSoundEvent(new Identifier(Init.MOD_ID, String.format("%s%s", legacyDoorSoundBaseResource, isOpen ? SOUND_DOOR_OPEN : SOUND_DOOR_CLOSE))), blockPos, 2, 1);
	}

	@Override
	protected double getDoorCloseSoundTime() {
		return legacyDoorCloseSoundTime;
	}
}
