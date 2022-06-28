package mtr.sound;

import mtr.MTR;
import mtr.MTRClient;
import mtr.data.Train;
import mtr.data.TrainClient;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.Random;

public class JonTrainSound extends TrainSoundBase {

	public final JonTrainSoundConfig config;
	private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
	private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

	private static final String SOUND_ACCELERATION = "_acceleration_";
	private static final String SOUND_DECELERATION = "_deceleration_";
	private static final String SOUND_DOOR_OPEN = "_door_open";
	private static final String SOUND_DOOR_CLOSE = "_door_close";
	private static final String SOUND_RANDOM = "_random";
	private static final int RANDOM_SOUND_CHANCE = 300;

	public JonTrainSound(TrainClient train, String speedSoundBaseId, String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting, boolean playbackSpeedDoesNotDependOnCustomAcceleration) {
		super(train);
		config = new JonTrainSoundConfig(speedSoundBaseId, doorSoundBaseId, speedSoundCount, doorCloseSoundTime, useAccelerationSoundsWhenCoasting, playbackSpeedDoesNotDependOnCustomAcceleration);
	}

	@Override
	public void playNearestCar(Level world, BlockPos pos, int carIndex) {
		if (!(world instanceof ClientLevel && MTRClient.canPlaySound())) {
			return;
		}

		if (config.speedSoundCount > 0 && config.speedSoundBaseId != null) {
			// TODO: Better sound system to adapt to different acceleration
			final float referenceAcceleration = config.playbackSpeedDoesNotDependOnCustomAcceleration ? train.accelerationConstant : Train.ACCELERATION_DEFAULT;
			final int floorSpeed = (int) Math.floor(train.speed / referenceAcceleration / MTRClient.TICKS_PER_SPEED_SOUND);
			if (floorSpeed > 0) {
				final Random random = new Random();

				if (floorSpeed >= 30 && random.nextInt(RANDOM_SOUND_CHANCE) == 0) {
					((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, config.speedSoundBaseId + SOUND_RANDOM)), SoundSource.BLOCKS, 10, 1, true);
				}

				final int index = Math.min(floorSpeed, config.speedSoundCount) - 1;
				final boolean isAccelerating = train.speed == train.speedLastElapse ? config.useAccelerationSoundsWhenCoasting || random.nextBoolean() : train.speed > train.speedLastElapse;
				final String soundId = config.speedSoundBaseId + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
				((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, true);
			}
		}
	}

	@Override
	public void playAllCars(Level world, BlockPos pos, int carIndex) {
	}

	@Override
	public void playAllCarsDoorOpening(Level world, BlockPos pos, int carIndex) {
		// TODO Check why door sounds are not playing
		if (!(world instanceof ClientLevel && MTRClient.canPlaySound())) {
			return;
		}

		final float doorValue = Math.abs(train.rawDoorValue);
		if (config.doorSoundBaseId != null) {
			final String soundId;
			if (train.doorValueLastElapse <= 0 && doorValue > 0) {
				soundId = config.doorSoundBaseId + SOUND_DOOR_OPEN;
			} else if (train.doorValueLastElapse >= config.doorCloseSoundTime && doorValue < config.doorCloseSoundTime) {
				soundId = config.doorSoundBaseId + SOUND_DOOR_CLOSE;
			} else {
				soundId = null;
			}
			if (soundId != null) {
				((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, true);
			}
		}
	}

	private static class JonTrainSoundConfig {

		public final String speedSoundBaseId;
		public final String doorSoundBaseId;
		public final int speedSoundCount;
		public final float doorCloseSoundTime;
		public final boolean useAccelerationSoundsWhenCoasting;
		public final boolean playbackSpeedDoesNotDependOnCustomAcceleration;

		private JonTrainSoundConfig(String speedSoundBaseId, String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting, boolean playbackSpeedDoesNotDependOnCustomAcceleration) {
			this.speedSoundBaseId = speedSoundBaseId;
			this.doorSoundBaseId = doorSoundBaseId;
			this.speedSoundCount = speedSoundCount;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
			this.playbackSpeedDoesNotDependOnCustomAcceleration = playbackSpeedDoesNotDependOnCustomAcceleration;
		}
	}
}
