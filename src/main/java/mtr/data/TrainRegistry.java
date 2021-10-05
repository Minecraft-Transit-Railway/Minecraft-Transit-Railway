package mtr.data;

import mtr.MTR;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;

import java.util.*;
import java.util.stream.Stream;

public class TrainRegistry {

	public static final String MINI_ID_STRING = "_mini";
	private static final List<String> TRAIN_TYPES = new ArrayList<>();
	private static final Map<String, TrainRegistry.TrainType> REGISTRY = new HashMap<>();

	public static void register(int color, int length, int lengthMini, int width, boolean shouldRenderConnection, SoundEvent[] accelerationSoundEvents, SoundEvent[] decelerationSoundEvents, SoundEvent doorOpenSoundEvent, SoundEvent doorCloseSoundEvent, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting, String id) {
		final String idLower = id.toLowerCase();

		if (TRAIN_TYPES.contains(idLower) || REGISTRY.containsKey(idLower)) {
			System.out.println("Train id " + idLower + " already exists!");
		} else {
			TRAIN_TYPES.add(idLower);
			REGISTRY.put(idLower, new TrainType(color, length, width, shouldRenderConnection, accelerationSoundEvents, decelerationSoundEvents, doorOpenSoundEvent, doorCloseSoundEvent, doorCloseSoundTime, useAccelerationSoundsWhenCoasting, idLower));

			if (lengthMini > 0) {
				final String idMini = idLower + MINI_ID_STRING;

				if (TRAIN_TYPES.contains(idMini) || REGISTRY.containsKey(idMini)) {
					System.out.println("Train id " + idMini + " already exists!");
				} else {
					TRAIN_TYPES.add(idMini);
					REGISTRY.put(idMini, new TrainType(color, lengthMini, width, shouldRenderConnection, accelerationSoundEvents, decelerationSoundEvents, doorOpenSoundEvent, doorCloseSoundEvent, doorCloseSoundTime, useAccelerationSoundsWhenCoasting, idMini));
				}
			}
		}
	}

	public static TrainType getTrainType(String key) {
		final String lowerKey = key.toLowerCase();
		return REGISTRY.containsKey(lowerKey) ? REGISTRY.get(lowerKey) : getFirstTrain();
	}

	public static TrainType getTrainType(int index) {
		return index >= 0 && index < TRAIN_TYPES.size() ? REGISTRY.get(TRAIN_TYPES.get(index)) : getFirstTrain();
	}

	public static Stream<TrainType> getTrainTypesStream() {
		return TRAIN_TYPES.stream().map(REGISTRY::get);
	}

	public static int getTrainTypesCount() {
		return TRAIN_TYPES.size();
	}

	public static int getIndex(String key) {
		return TRAIN_TYPES.indexOf(key.toLowerCase());
	}

	public static TrainType getFirstTrain() {
		return REGISTRY.get(TRAIN_TYPES.get(0));
	}

	public static class TrainType {

		public final int color;
		public final int width;
		public final boolean shouldRenderConnection;
		public final SoundEvent doorOpenSoundEvent;
		public final SoundEvent doorCloseSoundEvent;
		public final float doorCloseSoundTime;
		public final String id;

		private final int speedCount;
		private final SoundEvent[] accelerationSoundEvents;
		private final SoundEvent[] decelerationSoundEvents;
		private final boolean useAccelerationSoundsWhenCoasting;
		private final int length;

		private static final int TICKS_PER_SPEED_SOUND = 4;

		private TrainType(int color, int length, int width, boolean shouldRenderConnection, SoundEvent[] accelerationSoundEvents, SoundEvent[] decelerationSoundEvents, SoundEvent doorOpenSoundEvent, SoundEvent doorCloseSoundEvent, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting, String id) {
			this.color = color;
			this.length = length;
			this.width = width;
			this.accelerationSoundEvents = accelerationSoundEvents;
			this.decelerationSoundEvents = decelerationSoundEvents;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
			this.doorOpenSoundEvent = doorOpenSoundEvent;
			this.doorCloseSoundEvent = doorCloseSoundEvent;
			this.shouldRenderConnection = shouldRenderConnection;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.id = id;
			speedCount = accelerationSoundEvents == null || decelerationSoundEvents == null ? 0 : Math.min(accelerationSoundEvents.length, decelerationSoundEvents.length);
		}

		public String getName() {
			return new TranslatableText("train.mtr." + id).getString();
		}

		public int getSpacing() {
			return length + 1;
		}

		public void playSpeedSoundEffect(WorldAccess world, BlockPos pos, float oldSpeed, float speed) {
			if (MTR.isGameTickInterval(TICKS_PER_SPEED_SOUND) && accelerationSoundEvents != null && decelerationSoundEvents != null) {
				final int floorSpeed = (int) Math.floor(speed / Train.ACCELERATION / TICKS_PER_SPEED_SOUND);
				if (floorSpeed > 0) {
					final int index = Math.min(floorSpeed, speedCount) - 1;
					final boolean isAccelerating = speed == oldSpeed ? useAccelerationSoundsWhenCoasting || new Random().nextBoolean() : speed > oldSpeed;
					world.playSound(null, pos, isAccelerating ? accelerationSoundEvents[index] : decelerationSoundEvents[index], SoundCategory.BLOCKS, 1, 1);
				}
			}
		}
	}
}
