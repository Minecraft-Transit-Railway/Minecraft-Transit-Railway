package mtr.sound;

import mtr.MTR;
import mtr.MTRClient;
import mtr.data.Train;
import mtr.data.TrainClient;
import mtr.mappings.RegistryUtilities;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.Random;

public class JonTrainSound extends TrainSoundBase {

	private final TrainClient train;

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

	private JonTrainSound(String soundId, JonTrainSoundConfig config, TrainClient train) {
		this.config = config;
		this.soundId = soundId;
		this.train = train;
	}

	public JonTrainSound(String soundId, JonTrainSoundConfig config) {
		this(soundId, config, null);
	}

	@Override
	public TrainSoundBase createTrainInstance(TrainClient train) {
		return new JonTrainSound(soundId, config, train);
	}

	@Override
	public void playNearestCar(Level world, BlockPos pos, int carIndex) {
		if (!(world instanceof ClientLevel && MTRClient.canPlaySound())) {
			return;
		}

		if (config.speedSoundCount > 0 && soundId != null) {
			// TODO: Better sound system to adapt to different acceleration
			final float referenceAcceleration = config.constantPlaybackSpeed ? train.accelerationConstant : Train.ACCELERATION_DEFAULT;
			final int floorSpeed = (int) Math.floor(train.getSpeed() / referenceAcceleration / MTRClient.TICKS_PER_SPEED_SOUND);
			if (floorSpeed > 0) {
				final Random random = new Random();

				if (floorSpeed >= 30 && random.nextInt(RANDOM_SOUND_CHANCE) == 0) {
					((ClientLevel) world).playLocalSound(pos, RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, soundId + SOUND_RANDOM)), SoundSource.BLOCKS, 10, 1, false);
				}

				final int index = Math.min(floorSpeed, config.speedSoundCount) - 1;
				final boolean isAccelerating = train.speedChange() == 0 ? config.useAccelerationSoundsWhenCoasting || random.nextBoolean() : train.speedChange() > 0;
				final String speedSoundId = soundId + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
				((ClientLevel) world).playLocalSound(pos, RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, speedSoundId)), SoundSource.BLOCKS, 1, 1, false);
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
				((ClientLevel) world).playLocalSound(pos, RegistryUtilities.createSoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, false);
			}
		}
	}

	public static class JonTrainSoundConfig {

		public final String doorSoundBaseId;
		public final int speedSoundCount;
		public final float doorCloseSoundTime;
		public final boolean useAccelerationSoundsWhenCoasting;
		public final boolean constantPlaybackSpeed;

		public JonTrainSoundConfig(String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting, boolean constantPlaybackSpeed) {
			this.doorSoundBaseId = doorSoundBaseId;
			this.speedSoundCount = speedSoundCount;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
			this.constantPlaybackSpeed = constantPlaybackSpeed;
		}

		public JonTrainSoundConfig(String doorSoundBaseId, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
			this.doorSoundBaseId = doorSoundBaseId;
			this.speedSoundCount = speedSoundCount;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
			constantPlaybackSpeed = false;
		}
	}
}
