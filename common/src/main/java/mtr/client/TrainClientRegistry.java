package mtr.client;

import mtr.MTR;
import mtr.data.Train;
import mtr.data.TrainType;
import mtr.data.TransportMode;
import mtr.model.*;
import mtr.render.RenderTrains;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.function.BiConsumer;

public class TrainClientRegistry {

	private static final Map<String, TrainProperties> REGISTRY = new HashMap<>();
	private static final Map<TransportMode, List<String>> KEY_ORDERS = new HashMap<>();

	private static final String SOUND_ACCELERATION = "_acceleration_";
	private static final String SOUND_DECELERATION = "_deceleration_";
	private static final String SOUND_DOOR_OPEN = "_door_open";
	private static final String SOUND_DOOR_CLOSE = "_door_close";

	public static void register(String key, TrainType baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, String name, int color, boolean hasGangwayConnection, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
		final String keyLower = key.toLowerCase();
		if (!KEY_ORDERS.containsKey(baseTrainType.transportMode)) {
			KEY_ORDERS.put(baseTrainType.transportMode, new ArrayList<>());
		}
		if (!KEY_ORDERS.get(baseTrainType.transportMode).contains(keyLower)) {
			KEY_ORDERS.get(baseTrainType.transportMode).add(keyLower);
		}
		REGISTRY.put(keyLower, new TrainProperties(baseTrainType, model, textureId, speedSoundBaseId, doorSoundBaseId, new TranslatableComponent(name == null ? "train.mtr." + keyLower : name), color, hasGangwayConnection, speedSoundCount, doorCloseSoundTime, useAccelerationSoundsWhenCoasting));
	}

	public static void reset() {
		REGISTRY.clear();
		KEY_ORDERS.clear();

		register("sp1900", TrainType.SP1900, new ModelSP1900(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", null, 0x003399, true, 120, 0.5F, false);
		register("sp1900_small", TrainType.SP1900_SMALL, new ModelSP1900Small(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", null, 0x003399, true, 120, 0.5F, false);
		register("sp1900_mini", TrainType.SP1900_MINI, new ModelSP1900Mini(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", null, 0x003399, true, 120, 0.5F, false);
		register("c1141a", TrainType.C1141A, new ModelSP1900(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", null, 0xB42249, true, 96, 0.5F, false);
		register("c1141a_small", TrainType.C1141A_SMALL, new ModelSP1900Small(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", null, 0xB42249, true, 96, 0.5F, false);
		register("c1141a_mini", TrainType.C1141A_MINI, new ModelSP1900Mini(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", null, 0xB42249, true, 96, 0.5F, false);
		register("m_train", TrainType.M_TRAIN, new ModelMTrain(), "mtr:textures/entity/m_train", "m_train", "m_train", null, 0x999999, true, 90, 0.5F, false);
		register("m_train_small", TrainType.M_TRAIN_SMALL, new ModelMTrainSmall(), "mtr:textures/entity/m_train", "m_train", "m_train", null, 0x999999, true, 90, 0.5F, false);
		register("m_train_mini", TrainType.M_TRAIN_MINI, new ModelMTrainMini(), "mtr:textures/entity/m_train", "m_train", "m_train", null, 0x999999, true, 90, 0.5F, false);
		register("mlr", TrainType.MLR, new ModelMLR(), "mtr:textures/entity/mlr", "mlr", "mlr", null, 0x6CB5E2, true, 93, 0.5F, true);
		register("mlr_small", TrainType.MLR_SMALL, new ModelMLRSmall(), "mtr:textures/entity/mlr", "mlr", "mlr", null, 0x6CB5E2, true, 93, 0.5F, true);
		register("mlr_mini", TrainType.MLR_MINI, new ModelMLRMini(), "mtr:textures/entity/mlr", "mlr", "mlr", null, 0x6CB5E2, true, 93, 0.5F, true);
		register("e44", TrainType.E44, new ModelE44(), "mtr:textures/entity/e44", "mlr", "m_train", null, 0xE7AF25, true, 93, 0.5F, true);
		register("e44_mini", TrainType.E44_MINI, new ModelE44Mini(), "mtr:textures/entity/e44", "mlr", "m_train", null, 0xE7AF25, true, 93, 0.5F, true);
		register("drl", TrainType.DRL, new ModelDRL(), "mtr:textures/entity/drl", "m_train", null, null, 0xF287B7, true, 90, 0.5F, false);
		register("k_train", TrainType.K_TRAIN, new ModelKTrain(false), "mtr:textures/entity/k_train", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_small", TrainType.K_TRAIN_SMALL, new ModelKTrainSmall(false), "mtr:textures/entity/k_train", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_mini", TrainType.K_TRAIN_MINI, new ModelKTrainMini(false), "mtr:textures/entity/k_train", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_tcl", TrainType.K_TRAIN_TCL, new ModelKTrain(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_tcl_small", TrainType.K_TRAIN_TCL_SMALL, new ModelKTrainSmall(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_tcl_mini", TrainType.K_TRAIN_TCL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_ael", TrainType.K_TRAIN_AEL, new ModelKTrain(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_ael_small", TrainType.K_TRAIN_AEL_SMALL, new ModelKTrainSmall(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("k_train_ael_mini", TrainType.K_TRAIN_AEL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", null, 0x0EAB52, true, 66, 1, false);
		register("c_train", TrainType.C_TRAIN, new ModelCTrain(), "mtr:textures/entity/c_train", "c_train", "sp1900", null, 0xFDD900, true, 69, 0.5F, false);
		register("c_train_small", TrainType.C_TRAIN_SMALL, new ModelCTrainSmall(), "mtr:textures/entity/c_train", "c_train", "sp1900", null, 0xFDD900, true, 69, 0.5F, false);
		register("c_train_mini", TrainType.C_TRAIN_MINI, new ModelCTrainMini(), "mtr:textures/entity/c_train", "c_train", "sp1900", null, 0xFDD900, true, 69, 0.5F, false);
		register("a_train_tcl", TrainType.A_TRAIN_TCL, new ModelATrain(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", null, 0xF69447, true, 78, 0.5F, false);
		register("a_train_tcl_small", TrainType.A_TRAIN_TCL_SMALL, new ModelATrainSmall(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", null, 0xF69447, true, 78, 0.5F, false);
		register("a_train_tcl_mini", TrainType.A_TRAIN_TCL_MINI, new ModelATrainMini(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", null, 0xF69447, true, 78, 0.5F, false);
		register("a_train_ael", TrainType.A_TRAIN_AEL, new ModelATrain(true), "mtr:textures/entity/a_train_ael", "a_train", "a_train", null, 0x008D8D, true, 78, 0.5F, false);
		register("a_train_ael_mini", TrainType.A_TRAIN_AEL_MINI, new ModelATrainMini(true), "mtr:textures/entity/a_train_ael", "a_train", "a_train", null, 0x008D8D, true, 78, 0.5F, false);
		register("light_rail_1", TrainType.LIGHT_RAIL_1, new ModelLightRail(1), "mtr:textures/entity/light_rail_1", "light_rail", "light_rail_1", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_1r", TrainType.LIGHT_RAIL_1R, new ModelLightRail(4), "mtr:textures/entity/light_rail_1r", "light_rail", "light_rail_1", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_2", TrainType.LIGHT_RAIL_2, new ModelLightRail(2), "mtr:textures/entity/light_rail_2", "light_rail", "light_rail_3", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_3", TrainType.LIGHT_RAIL_3, new ModelLightRail(3), "mtr:textures/entity/light_rail_3", "light_rail", "light_rail_3", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_4", TrainType.LIGHT_RAIL_4, new ModelLightRail(4), "mtr:textures/entity/light_rail_4", "light_rail", "light_rail_4", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_5", TrainType.LIGHT_RAIL_5, new ModelLightRail(5), "mtr:textures/entity/light_rail_5", "light_rail", "light_rail_4", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_1r_old", TrainType.LIGHT_RAIL_1R_OLD, new ModelLightRail(4), "mtr:textures/entity/light_rail_1r_old", "light_rail", "light_rail_1", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_4_old", TrainType.LIGHT_RAIL_4_OLD, new ModelLightRail(4), "mtr:textures/entity/light_rail_4_old", "light_rail", "light_rail_4", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_5_old", TrainType.LIGHT_RAIL_5_OLD, new ModelLightRail(5), "mtr:textures/entity/light_rail_5_old", "light_rail", "light_rail_4", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_1_orange", TrainType.LIGHT_RAIL_1_ORANGE, new ModelLightRail(1), "mtr:textures/entity/light_rail_1_orange", "light_rail", "light_rail_1", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_1r_orange", TrainType.LIGHT_RAIL_1R_ORANGE, new ModelLightRail(4), "mtr:textures/entity/light_rail_1r_orange", "light_rail", "light_rail_1", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_2_orange", TrainType.LIGHT_RAIL_2_ORANGE, new ModelLightRail(2), "mtr:textures/entity/light_rail_2_orange", "light_rail", "light_rail_3", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_3_orange", TrainType.LIGHT_RAIL_3_ORANGE, new ModelLightRail(3), "mtr:textures/entity/light_rail_3_orange", "light_rail", "light_rail_3", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_4_orange", TrainType.LIGHT_RAIL_4_ORANGE, new ModelLightRail(4), "mtr:textures/entity/light_rail_4_orange", "light_rail", "light_rail_4", null, 0xD2A825, false, 48, 1, false);
		register("light_rail_5_orange", TrainType.LIGHT_RAIL_5_ORANGE, new ModelLightRail(5), "mtr:textures/entity/light_rail_5_orange", "light_rail", "light_rail_4", null, 0xD2A825, false, 48, 1, false);
		register("london_underground_1995", TrainType.LONDON_UNDERGROUND_1995, new ModelLondonUnderground1995(), "mtr:textures/entity/london_underground_1995", "london_underground_1995", "london_underground_1995", null, 0x333333, false, 27, 0.5F, false);
		register("r179", TrainType.R179, new ModelR179(), "mtr:textures/entity/r179", "r179", "r179", null, 0xD5D5D5, false, 66, 1, false);
		register("r179_mini", TrainType.R179_MINI, new ModelR179Mini(), "mtr:textures/entity/r179", "r179", "r179", null, 0xD5D5D5, false, 66, 1, false);
		register("minecart", TrainType.MINECART, null, "textures/entity/minecart", null, null, null, 0x666666, false, 0, 0.5F, false);
		register("oak_boat", TrainType.OAK_BOAT, null, "textures/entity/boat/oak", null, null, null, 0x8F7748, false, 0, 0.5F, false);
		register("spruce_boat", TrainType.SPRUCE_BOAT, null, "textures/entity/boat/spruce", null, null, null, 0x8F7748, false, 0, 0.5F, false);
		register("birch_boat", TrainType.BIRCH_BOAT, null, "textures/entity/boat/birch", null, null, null, 0x8F7748, false, 0, 0.5F, false);
		register("jungle_boat", TrainType.JUNGLE_BOAT, null, "textures/entity/boat/jungle", null, null, null, 0x8F7748, false, 0, 0.5F, false);
		register("acacia_boat", TrainType.ACACIA_BOAT, null, "textures/entity/boat/acacia", null, null, null, 0x8F7748, false, 0, 0.5F, false);
		register("dark_oak_boat", TrainType.DARK_OAK_BOAT, null, "textures/entity/boat/dark_oak", null, null, null, 0x8F7748, false, 0, 0.5F, false);
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

	public static TrainProperties getTrainProperties(TransportMode transportMode, int index) {
		return index >= 0 && index < KEY_ORDERS.get(transportMode).size() ? REGISTRY.get(KEY_ORDERS.get(transportMode).get(index)) : getBlankProperties(TrainType.values()[0]);
	}

	public static String getTrainId(TransportMode transportMode, int index) {
		return KEY_ORDERS.get(transportMode).get(index >= 0 && index < KEY_ORDERS.get(transportMode).size() ? index : 0);
	}

	public static void forEach(TransportMode transportMode, BiConsumer<String, TrainProperties> biConsumer) {
		KEY_ORDERS.get(transportMode).forEach(key -> biConsumer.accept(key, REGISTRY.get(key)));
	}

	private static TrainProperties getBlankProperties(TrainType baseTrainType) {
		return new TrainProperties(baseTrainType, null, null, null, null, new TranslatableComponent(""), 0, false, 0, 0.5F, false);
	}

	public static class TrainProperties {

		public final TrainType baseTrainType;
		public final ModelTrainBase model;
		public final String textureId;
		public final String speedSoundBaseId;
		public final String doorSoundBaseId;
		public final TranslatableComponent name;
		public final int color;
		public final boolean hasGangwayConnection;
		public final int speedSoundCount;
		private final float doorCloseSoundTime;
		private final boolean useAccelerationSoundsWhenCoasting;

		private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
		private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

		private TrainProperties(TrainType baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, TranslatableComponent name, int color, boolean hasGangwayConnection, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
			this.baseTrainType = baseTrainType;
			this.model = model;
			this.textureId = resolvePath(textureId);
			this.speedSoundBaseId = resolvePath(speedSoundBaseId);
			this.doorSoundBaseId = resolvePath(doorSoundBaseId);
			this.name = name;
			this.color = color;
			this.hasGangwayConnection = hasGangwayConnection;
			this.speedSoundCount = speedSoundCount;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
		}

		public void playSpeedSoundEffect(Level world, BlockPos pos, float oldSpeed, float speed) {
			if (world instanceof ClientLevel && RenderTrains.canPlaySound() && speedSoundCount > 0 && speedSoundBaseId != null) {
				final int floorSpeed = (int) Math.floor(speed / Train.ACCELERATION / RenderTrains.TICKS_PER_SPEED_SOUND);
				if (floorSpeed > 0) {
					final int index = Math.min(floorSpeed, speedSoundCount) - 1;
					final boolean isAccelerating = speed == oldSpeed ? useAccelerationSoundsWhenCoasting || new Random().nextBoolean() : speed > oldSpeed;
					final String soundId = speedSoundBaseId + (isAccelerating ? SOUND_ACCELERATION : SOUND_DECELERATION) + index / SOUND_GROUP_SIZE + SOUND_GROUP_LETTERS[index % SOUND_GROUP_SIZE];
					((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, true);
				}
			}
		}

		public void playDoorSoundEffect(Level world, BlockPos pos, float oldDoorValue, float doorValue) {
			if (world instanceof ClientLevel && doorSoundBaseId != null) {
				final String soundId;
				if (oldDoorValue <= 0 && doorValue > 0) {
					soundId = doorSoundBaseId + SOUND_DOOR_OPEN;
				} else if (oldDoorValue >= doorCloseSoundTime && doorValue < doorCloseSoundTime) {
					soundId = doorSoundBaseId + SOUND_DOOR_CLOSE;
				} else {
					soundId = null;
				}
				if (soundId != null) {
					((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, soundId)), SoundSource.BLOCKS, 1, 1, true);
				}
			}
		}

		private static String resolvePath(String path) {
			return path == null ? null : path.toLowerCase().split("\\.png")[0];
		}
	}
}
