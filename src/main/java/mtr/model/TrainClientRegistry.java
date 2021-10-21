package mtr.model;

import mtr.MTR;
import mtr.data.Train;
import mtr.data.TrainType;
import mtr.render.RenderTrains;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiConsumer;

public class TrainClientRegistry {

	private static final Map<String, TrainProperties> REGISTRY = new HashMap<>();
	private static final List<String> KEY_ORDER = new ArrayList<>();

	private static final String SOUND_ACCELERATION = "_acceleration_";
	private static final String SOUND_DECELERATION = "_deceleration_";
	private static final String SOUND_DOOR_OPEN = "_door_open";
	private static final String SOUND_DOOR_CLOSE = "_door_close";

	public static void register(String key, TrainType baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, String name, int color, boolean hasGangwayConnection, boolean trainBarrierOption, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
		final String keyLower = key.toLowerCase();
		if (!KEY_ORDER.contains(keyLower)) {
			KEY_ORDER.add(keyLower);
		}
		REGISTRY.put(keyLower, new TrainProperties(baseTrainType, model, textureId, speedSoundBaseId, doorSoundBaseId, new TranslatableText(name == null ? "train.mtr." + keyLower : name), color, hasGangwayConnection, trainBarrierOption, speedSoundCount, doorCloseSoundTime, useAccelerationSoundsWhenCoasting));
	}

	public static void reset() {
		REGISTRY.clear();
		KEY_ORDER.clear();

		register("sp1900", TrainType.SP1900, new ModelSP1900(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", null, 0x003399, true, false, 120, 0.5F, false);
		register("sp1900_mini", TrainType.SP1900_MINI, new ModelSP1900Mini(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", null, 0x003399, true, false, 120, 0.5F, false);
		register("c1141a", TrainType.C1141A, new ModelSP1900(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", null, 0xB42249, true, false, 96, 0.5F, false);
		register("c1141a_mini", TrainType.C1141A_MINI, new ModelSP1900Mini(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", null, 0xB42249, true, false, 96, 0.5F, false);
		register("m_train", TrainType.M_TRAIN, new ModelMTrain(), "mtr:textures/entity/m_train", "m_train", "m_train", null, 0x999999, true, false, 90, 0.5F, false);
		register("m_train_mini", TrainType.M_TRAIN_MINI, new ModelMTrainMini(), "mtr:textures/entity/m_train", "m_train", "m_train", null, 0x999999, true, false, 90, 0.5F, false);
		register("mlr", TrainType.MLR, new ModelMLR(), "mtr:textures/entity/mlr", "mlr", "mlr", null, 0x6CB5E2, true, false, 93, 0.5F, true);
		register("mlr_mini", TrainType.MLR_MINI, new ModelMLRMini(), "mtr:textures/entity/mlr", "mlr", "mlr", null, 0x6CB5E2, true, false, 93, 0.5F, true);
		register("e44", TrainType.E44, new ModelE44(), "mtr:textures/entity/e44", "mlr", "m_train", null, 0xE7AF25, true, false, 93, 0.5F, true);
		register("e44_mini", TrainType.E44_MINI, new ModelE44Mini(), "mtr:textures/entity/e44", "mlr", "m_train", null, 0xE7AF25, true, false, 93, 0.5F, true);
		register("k_train", TrainType.K_TRAIN, new ModelKTrain(false), "mtr:textures/entity/k_train", "k_train", "k_train", null, 0x0EAB52, true, false, 66, 1, false);
		register("k_train_mini", TrainType.K_TRAIN_MINI, new ModelKTrainMini(false), "mtr:textures/entity/k_train", "k_train", "k_train", null, 0x0EAB52, true, false, 66, 1, false);
		register("k_train_tcl", TrainType.K_TRAIN_TCL, new ModelKTrain(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", null, 0x0EAB52, true, false, 66, 1, false);
		register("k_train_tcl_mini", TrainType.K_TRAIN_TCL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", null, 0x0EAB52, true, false, 66, 1, false);
		register("k_train_ael", TrainType.K_TRAIN_AEL, new ModelKTrain(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", null, 0x0EAB52, true, false, 66, 1, false);
		register("k_train_ael_mini", TrainType.K_TRAIN_AEL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", null, 0x0EAB52, true, false, 66, 1, false);
		register("a_train_tcl", TrainType.A_TRAIN_TCL, new ModelATrain(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", null, 0xF69447, true, false, 78, 0.5F, false);
		register("a_train_tcl_mini", TrainType.A_TRAIN_TCL_MINI, new ModelATrainMini(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", null, 0xF69447, true, false, 78, 0.5F, false);
		register("a_train_ael", TrainType.A_TRAIN_AEL, new ModelATrain(true), "mtr:textures/entity/a_train_ael", "a_train", "a_train", null, 0x008D8D, true, false, 78, 0.5F, false);
		register("a_train_ael_mini", TrainType.A_TRAIN_AEL_MINI, new ModelATrainMini(true), "mtr:textures/entity/a_train_ael", "a_train", "a_train", null, 0x008D8D, true, false, 78, 0.5F, false);
		register("light_rail_1", TrainType.LIGHT_RAIL_1, new ModelLightRail(1), "mtr:textures/entity/light_rail_1", "light_rail", "light_rail_1", null, 0xD2A825, false, false, 48, 1, false);
		register("light_rail_1r", TrainType.LIGHT_RAIL_1R, new ModelLightRail(4), "mtr:textures/entity/light_rail_1r", "light_rail", "light_rail_1", null, 0xD2A825, false, false, 48, 1, false);
		register("light_rail_2", TrainType.LIGHT_RAIL_2, new ModelLightRail(2), "mtr:textures/entity/light_rail_2", "light_rail", "light_rail_3", null, 0xD2A825, false, false, 48, 1, false);
		register("light_rail_3", TrainType.LIGHT_RAIL_3, new ModelLightRail(3), "mtr:textures/entity/light_rail_3", "light_rail", "light_rail_3", null, 0xD2A825, false, false, 48, 1, false);
		register("light_rail_4", TrainType.LIGHT_RAIL_4, new ModelLightRail(4), "mtr:textures/entity/light_rail_4", "light_rail", "light_rail_4", null, 0xD2A825, false, false, 48, 1, false);
		register("light_rail_5", TrainType.LIGHT_RAIL_5, new ModelLightRail(5), "mtr:textures/entity/light_rail_5", "light_rail", "light_rail_4", null, 0xD2A825, false, false, 48, 1, false);
		register("london_underground_1995", TrainType.LONDON_UNDERGROUND_1995, new ModelLondonUnderground1995(), "mtr:textures/entity/london_underground_1995", null, null, null, 0x333333, false, false, 0, 0.5F, false);
		register("r179", TrainType.R179, new ModelR179(), "mtr:textures/entity/r179", "r179", "r179", null, 0xD5D5D5, false, true, 66, 1, false);
		register("minecart", TrainType.MINECART, null, null, null, null, null, 0x666666, false, false, 0, 0.5F, false);
	}

	public static TrainProperties getTrainProperties(String key, TrainType baseTrainType) {
		final String keyLower = key.toLowerCase();
		if (REGISTRY.containsKey(keyLower)) {
			return REGISTRY.get(keyLower);
		} else {
			final String fallbackKeyLower = baseTrainType.toString().toLowerCase();
			return REGISTRY.containsKey(fallbackKeyLower) ? REGISTRY.get(fallbackKeyLower) : getBlankProperties(baseTrainType);
		}
	}

	public static TrainProperties getTrainProperties(int index) {
		return index >= 0 && index < KEY_ORDER.size() ? REGISTRY.get(KEY_ORDER.get(index)) : getBlankProperties(TrainType.values()[0]);
	}

	public static String getTrainId(int index) {
		return KEY_ORDER.get(index >= 0 && index < KEY_ORDER.size() ? index : 0);
	}

	public static void forEach(BiConsumer<String, TrainProperties> biConsumer) {
		KEY_ORDER.forEach(key -> biConsumer.accept(key, REGISTRY.get(key)));
	}

	private static TrainProperties getBlankProperties(TrainType baseTrainType) {
		return new TrainProperties(baseTrainType, null, null, null, null, null, 0, false, false,0, 0.5F, false);
	}

	public static class TrainProperties {

		public final TrainType baseTrainType;
		public final ModelTrainBase model;
		public final String textureId;
		public final String speedSoundBaseId;
		public final String doorSoundBaseId;
		public final TranslatableText name;
		public final int color;
		public final boolean hasGangwayConnection;
		public final boolean trainBarrierOption;
		public final int speedSoundCount;
		private final float doorCloseSoundTime;
		private final boolean useAccelerationSoundsWhenCoasting;

		private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
		private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

		private TrainProperties(TrainType baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, TranslatableText name, int color, boolean hasGangwayConnection, boolean trainBarrierOption, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
			this.baseTrainType = baseTrainType;
			this.model = model;
			this.textureId = resolvePath(textureId);
			this.speedSoundBaseId = resolvePath(speedSoundBaseId);
			this.doorSoundBaseId = resolvePath(doorSoundBaseId);
			this.name = name;
			this.color = color;
			this.hasGangwayConnection = hasGangwayConnection;
			this.trainBarrierOption = trainBarrierOption;
			this.speedSoundCount = speedSoundCount;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
		}

		public void playSpeedSoundEffect(World world, BlockPos pos, float oldSpeed, float speed) {
			if (world instanceof ClientWorld && RenderTrains.canPlaySound() && speedSoundCount > 0 && speedSoundBaseId != null) {
				final int floorSpeed = (int) Math.floor(speed / Train.ACCELERATION / RenderTrains.TICKS_PER_SPEED_SOUND);
				if (floorSpeed > 0) {
					final int index = Math.min(floorSpeed, speedSoundCount) - 1;
					final boolean isAccelerating = speed == oldSpeed ? useAccelerationSoundsWhenCoasting || new Random().nextBoolean() : speed > oldSpeed;
					final String soundId = speedSoundBaseId + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
					((ClientWorld) world).playSound(pos, new SoundEvent(new Identifier(MTR.MOD_ID, soundId)), SoundCategory.BLOCKS, 1, 1, true);
				}
			}
		}

		public void playDoorSoundEffect(World world, BlockPos pos, float oldDoorValue, float doorValue) {
			if (world instanceof ClientWorld && doorSoundBaseId != null) {
				final String soundId;
				if (oldDoorValue <= 0 && doorValue > 0) {
					soundId = doorSoundBaseId + SOUND_DOOR_OPEN;
				} else if (oldDoorValue >= doorCloseSoundTime && doorValue < doorCloseSoundTime) {
					soundId = doorSoundBaseId + SOUND_DOOR_CLOSE;
				} else {
					soundId = null;
				}
				if (soundId != null) {
					((ClientWorld) world).playSound(pos, new SoundEvent(new Identifier(MTR.MOD_ID, soundId)), SoundCategory.BLOCKS, 1, 1, true);
				}
			}
		}

		private static String resolvePath(String path) {
			return path == null ? null : path.toLowerCase().split("\\.png")[0];
		}
	}
}
