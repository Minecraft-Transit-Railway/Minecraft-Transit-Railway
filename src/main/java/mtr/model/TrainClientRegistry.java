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

	public static void register(String key, TrainType baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, int color, boolean hasGangwayConnection, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
		final String keyLower = key.toLowerCase();
		if (!KEY_ORDER.contains(keyLower)) {
			KEY_ORDER.add(keyLower);
		}
		REGISTRY.put(keyLower, new TrainProperties(baseTrainType, model, textureId, speedSoundBaseId, doorSoundBaseId, color, hasGangwayConnection, speedSoundCount, doorCloseSoundTime, useAccelerationSoundsWhenCoasting));
	}

	public static void reset() {
		REGISTRY.clear();
		KEY_ORDER.clear();

		register("sp1900", TrainType.SP1900, new ModelSP1900(false), "sp1900", "sp1900", "sp1900", 0x003399, true, 120, 0.5F, false);
		register("sp1900_mini", TrainType.SP1900_MINI, new ModelSP1900Mini(false), "sp1900", "sp1900", "sp1900", 0x003399, true, 120, 0.5F, false);
		register("c1141a", TrainType.C1141A, new ModelSP1900(true), "c1141a", "c1141a", "sp1900", 0xB42249, true, 96, 0.5F, false);
		register("c1141a_mini", TrainType.C1141A_MINI, new ModelSP1900Mini(true), "c1141a", "c1141a", "sp1900", 0xB42249, true, 96, 0.5F, false);
		register("m_train", TrainType.M_TRAIN, new ModelMTrain(), "m_train", "m_train", "m_train", 0x999999, true, 90, 0.5F, false);
		register("m_train_mini", TrainType.M_TRAIN_MINI, new ModelMTrainMini(), "m_train", "m_train", "m_train", 0x999999, true, 90, 0.5F, false);
		register("mlr", TrainType.MLR, new ModelMLR(), "mlr", "mlr", "mlr", 0x6CB5E2, true, 93, 0.5F, true);
		register("mlr_mini", TrainType.MLR_MINI, new ModelMLRMini(), "mlr", "mlr", "mlr", 0x6CB5E2, true, 93, 0.5F, true);
		register("e44", TrainType.E44, new ModelE44(), "e44", "e44", "e44", 0xE7AF25, true, 93, 0.5F, true);
		register("e44_mini", TrainType.E44_MINI, new ModelE44Mini(), "e44", "e44", "e44", 0xE7AF25, true, 93, 0.5F, true);
		register("k_train", TrainType.K_TRAIN, new ModelKTrain(false), "k_train", "k_train", "k_train", 0x0EAB52, true, 66, 1, false);
		register("k_train_mini", TrainType.K_TRAIN_MINI, new ModelKTrainMini(false), "k_train", "k_train", "k_train", 0x0EAB52, true, 66, 1, false);
		register("k_train_tcl", TrainType.K_TRAIN_TCL, new ModelKTrain(true), "k_train_tcl", "k_train_tcl", "k_train_tcl", 0x0EAB52, true, 66, 1, false);
		register("k_train_tcl_mini", TrainType.K_TRAIN_TCL_MINI, new ModelKTrainMini(true), "k_train_tcl", "k_train_tcl", "k_train_tcl", 0x0EAB52, true, 66, 1, false);
		register("k_train_ael", TrainType.K_TRAIN_AEL, new ModelKTrain(true), "k_train_ael", "k_train_ael", "k_train_ael", 0x0EAB52, true, 66, 1, false);
		register("k_train_ael_mini", TrainType.K_TRAIN_AEL_MINI, new ModelKTrainMini(true), "k_train_ael", "k_train_ael", "k_train_ael", 0x0EAB52, true, 66, 1, false);
		register("a_train_tcl", TrainType.A_TRAIN_TCL, new ModelATrain(false), "a_train_tcl", "a_train_tcl", "a_train_tcl", 0xF69447, true, 78, 0.5F, false);
		register("a_train_tcl_mini", TrainType.A_TRAIN_TCL_MINI, new ModelATrainMini(false), "a_train_tcl", "a_train_tcl", "a_train_tcl", 0xF69447, true, 78, 0.5F, false);
		register("a_train_ael", TrainType.A_TRAIN_AEL, new ModelATrain(true), "a_train_ael", "a_train_ael", "a_train_ael", 0x008D8D, true, 78, 0.5F, false);
		register("a_train_ael_mini", TrainType.A_TRAIN_AEL_MINI, new ModelATrainMini(true), "a_train_ael", "a_train_ael", "a_train_ael", 0x008D8D, true, 78, 0.5F, false);
		register("light_rail_1", TrainType.LIGHT_RAIL_1, new ModelLightRail(1), "light_rail_1", "light_rail", "light_rail_1", 0xD2A825, false, 48, 1, false);
		register("light_rail_1r", TrainType.LIGHT_RAIL_1R, new ModelLightRail(4), "light_rail_1r", "light_rail", "light_rail_1", 0xD2A825, false, 48, 1, false);
		register("light_rail_2", TrainType.LIGHT_RAIL_2, new ModelLightRail(2), "light_rail_2", "light_rail", "light_rail_3", 0xD2A825, false, 48, 1, false);
		register("light_rail_3", TrainType.LIGHT_RAIL_3, new ModelLightRail(3), "light_rail_3", "light_rail", "light_rail_3", 0xD2A825, false, 48, 1, false);
		register("light_rail_4", TrainType.LIGHT_RAIL_4, new ModelLightRail(4), "light_rail_4", "light_rail", "light_rail_4", 0xD2A825, false, 48, 1, false);
		register("light_rail_5", TrainType.LIGHT_RAIL_5, new ModelLightRail(5), "light_rail_5", "light_rail", "light_rail_4", 0xD2A825, false, 48, 1, false);
		register("r179", TrainType.R179, new ModelR179Train(), "r179", "r179", "r179", 0xD5D5D5, false, 66, 1, false);
		register("minecart", TrainType.MINECART, null, null, null, null, 0x666666, false, 0, 0.5F, false);
	}

	public static TrainProperties getTrainProperties(String key) {
		final String keyLower = key.toLowerCase();
		return REGISTRY.containsKey(keyLower) ? REGISTRY.get(keyLower) : getBlankProperties(TrainType.getOrDefault(key));
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

	public static TranslatableText getTrainName(String id) {
		return new TranslatableText("train.mtr." + id.toLowerCase());
	}

	private static TrainProperties getBlankProperties(TrainType baseTrainType) {
		return new TrainProperties(baseTrainType, null, null, null, null, 0, false, 0, 0.5F, false);
	}

	public static class TrainProperties {

		public final TrainType baseTrainType;
		public final ModelTrainBase model;
		public final String textureId;
		public final String speedSoundBaseId;
		public final String doorSoundBaseId;
		public final int color;
		public final boolean hasGangwayConnection;
		public final int speedSoundCount;
		private final float doorCloseSoundTime;
		private final boolean useAccelerationSoundsWhenCoasting;

		private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
		private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

		private TrainProperties(TrainType baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, int color, boolean hasGangwayConnection, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
			this.baseTrainType = baseTrainType;
			this.model = model;
			this.textureId = textureId;
			this.speedSoundBaseId = speedSoundBaseId;
			this.doorSoundBaseId = doorSoundBaseId;
			this.color = color;
			this.hasGangwayConnection = hasGangwayConnection;
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
	}
}
