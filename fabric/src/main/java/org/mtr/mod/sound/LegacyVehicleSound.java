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

	private final String soundId;
	private final LegacyVehicleSoundConfig config;
	private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
	private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

	private static final String SOUND_ACCELERATION = "_acceleration_";
	private static final String SOUND_DECELERATION = "_deceleration_";
	private static final String SOUND_DOOR_OPEN = "_door_open";
	private static final String SOUND_DOOR_CLOSE = "_door_close";
	private static final String SOUND_RANDOM = "_random";
	private static final int RANDOM_SOUND_CHANCE = 300;

	public LegacyVehicleSound(@Nullable String soundId, LegacyVehicleSoundConfig config) {
		this.soundId = soundId;
		this.config = config;
	}

	@Override
	public void playSound(int bogieIndex, BlockPos blockPos, float speed, float speedChange, float acceleration, boolean isOnRoute) {
		if (!InitClient.canPlaySound()) {
			return;
		}

		if (config.speedSoundCount > 0 && soundId != null) {
			final double referenceAcceleration = config.constantPlaybackSpeed ? acceleration : Siding.ACCELERATION_DEFAULT;
			final int floorSpeed = (int) Math.floor(speed / referenceAcceleration / InitClient.MILLIS_PER_SPEED_SOUND);
			if (floorSpeed > 0) {
				final Random random = new Random();

				if (floorSpeed >= 30 && random.nextInt(RANDOM_SOUND_CHANCE) == 0) {
					playSoundInWorld(AbstractSoundInstanceExtension.createSoundEvent(new Identifier(Init.MOD_ID, soundId + SOUND_RANDOM)), blockPos, 10, 1);
				}

				final int index = Math.min(floorSpeed, config.speedSoundCount) - 1;
				final boolean isAccelerating = speedChange == 0 ? config.useAccelerationSoundsWhenCoasting || random.nextBoolean() : speedChange > 0;
				final String speedSoundId = soundId + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
				playSoundInWorld(AbstractSoundInstanceExtension.createSoundEvent(new Identifier(Init.MOD_ID, speedSoundId)), blockPos, 1, 1);
			}
		}
	}
}
