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

	public final String soundId;
	public final JonTrainSoundConfig config;
	private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
	private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

	private static final String SOUND_ACCELERATION = "_acceleration_";
	private static final String SOUND_DECELERATION = "_deceleration_";
	private static final String SOUND_DOOR_OPEN = "_door_open";
	private static final String SOUND_DOOR_CLOSE = "_door_close";
	private static final String SOUND_RANDOM = "_random";
	private static final int RANDOM_SOUND_CHANCE = 300;

	public JonTrainSound(TrainClient train, String soundId, JonTrainSoundConfig config) {
		super(train);
		this.config = config;
		this.soundId = soundId;
	}

	@Override
	public void playNearestCar(Level world, BlockPos pos, int carIndex) {
		if (!(world instanceof ClientLevel && MTRClient.canPlaySound())) {
			return;
		}

		if (config.speedSoundCount > 0 && soundId != null) {
			// TODO: Better sound system to adapt to different acceleration
			final float referenceAcceleration = config.playbackSpeedDoesNotDependOnCustomAcceleration ? train.accelerationConstant : Train.ACCELERATION_DEFAULT;
			final int floorSpeed = (int) Math.floor(train.getSpeed() / referenceAcceleration / MTRClient.TICKS_PER_SPEED_SOUND);
			if (floorSpeed > 0) {
				final Random random = new Random();

				if (floorSpeed >= 30 && random.nextInt(RANDOM_SOUND_CHANCE) == 0) {
					((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId + SOUND_RANDOM)), SoundSource.BLOCKS, 10, 1, true);
				}

				final int index = Math.min(floorSpeed, config.speedSoundCount) - 1;
				final boolean isAccelerating = train.speedChange() == 0 ? config.useAccelerationSoundsWhenCoasting || random.nextBoolean() : train.speedChange() > 0;
				final String speedSoundId = soundId + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
				((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, speedSoundId)), SoundSource.BLOCKS, 1, 1, true);
			}
		}
	}

	@Override
	public void playAllCars(Level world, BlockPos pos, int carIndex) {
	}

	@Override
	public void playAllCarsDoorOpening(Level world, BlockPos pos, int carIndex) {
		if (world instanceof ClientLevel && config.doorSoundBaseId != null) {
			final String soundId;
			if (train.justOpening()) {
				soundId = config.doorSoundBaseId + SOUND_DOOR_OPEN;
			} else if (train.justClosing(config.doorCloseSoundTime)) {
				soundId = config.doorSoundBaseId + SOUND_DOOR_CLOSE;
			} else {
				soundId = null;
			}
			if (soundId != null) {
				((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, true);
			}
		}
	}

	public static class JonTrainSoundConfig {

		public final String doorSoundBaseId;
		public final int speedSoundCount;
		public final float doorCloseSoundTime;
		public final boolean useAccelerationSoundsWhenCoasting;
		public final boolean playbackSpeedDoesNotDependOnCustomAcceleration = true;

		public JonTrainSoundConfig(String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
			this.doorSoundBaseId = doorSoundBaseId;
			this.speedSoundCount = speedSoundCount;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
		}
	}
}
