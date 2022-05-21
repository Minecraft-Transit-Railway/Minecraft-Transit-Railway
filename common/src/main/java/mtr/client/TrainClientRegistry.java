package mtr.client;

import mtr.MTR;
import mtr.MTRClient;
import mtr.data.Train;
import mtr.data.TrainType;
import mtr.data.TransportMode;
import mtr.model.*;
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
	private static final String SOUND_RANDOM = "_random";
	private static final int RANDOM_SOUND_CHANCE = 300;

	private static void register(TrainType defaultTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, int color, String gangwayConnectionId, String trainBarrierId, float riderOffset, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
		register(defaultTrainType.toString(), defaultTrainType.baseTrainType, model, textureId, speedSoundBaseId, doorSoundBaseId, null, color, gangwayConnectionId, trainBarrierId, riderOffset, speedSoundCount, doorCloseSoundTime, useAccelerationSoundsWhenCoasting);
	}

	public static void register(String key, String baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, String name, int color, String gangwayConnectionId, String trainBarrierId, float riderOffset, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
		final String keyLower = key.toLowerCase();
		final TransportMode transportMode = TrainType.getTransportMode(baseTrainType);
		if (!KEY_ORDERS.containsKey(transportMode)) {
			KEY_ORDERS.put(transportMode, new ArrayList<>());
		}
		if (!KEY_ORDERS.get(transportMode).contains(keyLower)) {
			KEY_ORDERS.get(transportMode).add(keyLower);
		}
		REGISTRY.put(keyLower, new TrainProperties(baseTrainType, model, textureId, speedSoundBaseId, doorSoundBaseId, new TranslatableComponent(name == null ? "train.mtr." + keyLower : name), color, gangwayConnectionId, trainBarrierId, riderOffset, speedSoundCount, doorCloseSoundTime, useAccelerationSoundsWhenCoasting));
	}

	public static void reset() {
		REGISTRY.clear();
		KEY_ORDERS.clear();

		register(TrainType.SP1900, new ModelSP1900(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 0, 120, 0.5F, false);
		register(TrainType.SP1900_SMALL, new ModelSP1900Small(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 0, 120, 0.5F, false);
		register(TrainType.SP1900_MINI, new ModelSP1900Mini(false), "mtr:textures/entity/sp1900", "sp1900", "sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 0, 120, 0.5F, false);
		register(TrainType.C1141A, new ModelSP1900(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", 0xB42249, "mtr:textures/entity/sp1900", "", 0, 96, 0.5F, false);
		register(TrainType.C1141A_SMALL, new ModelSP1900Small(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", 0xB42249, "mtr:textures/entity/sp1900", "", 0, 96, 0.5F, false);
		register(TrainType.C1141A_MINI, new ModelSP1900Mini(true), "mtr:textures/entity/c1141a", "c1141a", "sp1900", 0xB42249, "mtr:textures/entity/sp1900", "", 0, 96, 0.5F, false);
		register(TrainType.M_TRAIN, new ModelMTrain(), "mtr:textures/entity/m_train", "m_train", "m_train", 0x999999, "mtr:textures/entity/m_train", "", 0, 90, 0.5F, false);
		register(TrainType.M_TRAIN_SMALL, new ModelMTrainSmall(), "mtr:textures/entity/m_train", "m_train", "m_train", 0x999999, "mtr:textures/entity/m_train", "", 0, 90, 0.5F, false);
		register(TrainType.M_TRAIN_MINI, new ModelMTrainMini(), "mtr:textures/entity/m_train", "m_train", "m_train", 0x999999, "mtr:textures/entity/m_train", "", 0, 90, 0.5F, false);
		register(TrainType.CM_STOCK, new ModelCMStock(), "mtr:textures/entity/cm_stock", "m_train", "m_train", 0x999999, "mtr:textures/entity/m_train", "", 0, 90, 0.5F, false);
		register(TrainType.CM_STOCK_SMALL, new ModelCMStockSmall(), "mtr:textures/entity/cm_stock", "m_train", "m_train", 0x999999, "mtr:textures/entity/m_train", "", 0, 90, 0.5F, false);
		register(TrainType.CM_STOCK_MINI, new ModelCMStockMini(), "mtr:textures/entity/cm_stock", "m_train", "m_train", 0x999999, "mtr:textures/entity/m_train", "", 0, 90, 0.5F, false);
		register(TrainType.MLR, new ModelMLR(), "mtr:textures/entity/mlr", "mlr", "mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 0, 93, 0.5F, true);
		register(TrainType.MLR_SMALL, new ModelMLRSmall(), "mtr:textures/entity/mlr", "mlr", "mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 0, 93, 0.5F, true);
		register(TrainType.MLR_MINI, new ModelMLRMini(), "mtr:textures/entity/mlr", "mlr", "mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 0, 93, 0.5F, true);
		register(TrainType.E44, new ModelE44(), "mtr:textures/entity/e44", "mlr", "m_train", 0xE7AF25, "mtr:textures/entity/m_train", "", 0, 93, 0.5F, true);
		register(TrainType.E44_MINI, new ModelE44Mini(), "mtr:textures/entity/e44", "mlr", "m_train", 0xE7AF25, "mtr:textures/entity/m_train", "", 0, 93, 0.5F, true);
		register(TrainType.R_TRAIN, new ModelRTrain(), "mtr:textures/entity/r_train", "c1141a", "sp1900", 0x6CB5E2, "mtr:textures/entity/sp1900", "mlr", 0, 96, 0.5F, false);
		register(TrainType.R_TRAIN_SMALL, new ModelRTrainSmall(), "mtr:textures/entity/r_train", "c1141a", "sp1900", 0x6CB5E2, "mtr:textures/entity/sp1900", "mlr", 0, 96, 0.5F, false);
		register(TrainType.R_TRAIN_MINI, new ModelRTrainMini(), "mtr:textures/entity/r_train", "c1141a", "sp1900", 0x6CB5E2, "mtr:textures/entity/sp1900", "mlr", 0, 96, 0.5F, false);
		register(TrainType.DRL, new ModelDRL(), "mtr:textures/entity/drl", "m_train", null, 0xF287B7, "mtr:textures/entity/m_train", "", 0, 90, 0.5F, false);
		register(TrainType.K_TRAIN, new ModelKTrain(false), "mtr:textures/entity/k_train", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_SMALL, new ModelKTrainSmall(false), "mtr:textures/entity/k_train", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_MINI, new ModelKTrainMini(false), "mtr:textures/entity/k_train", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_TCL, new ModelKTrain(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_TCL_SMALL, new ModelKTrainSmall(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_TCL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_tcl", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_AEL, new ModelKTrain(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_AEL_SMALL, new ModelKTrainSmall(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.K_TRAIN_AEL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_ael", "k_train", "k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 0, 66, 1, false);
		register(TrainType.C_TRAIN, new ModelCTrain(), "mtr:textures/entity/c_train", "c_train", "sp1900", 0xFDD900, "mtr:textures/entity/s_train", "", 0, 69, 0.5F, false);
		register(TrainType.C_TRAIN_SMALL, new ModelCTrainSmall(), "mtr:textures/entity/c_train", "c_train", "sp1900", 0xFDD900, "mtr:textures/entity/s_train", "", 0, 69, 0.5F, false);
		register(TrainType.C_TRAIN_MINI, new ModelCTrainMini(), "mtr:textures/entity/c_train", "c_train", "sp1900", 0xFDD900, "mtr:textures/entity/s_train", "", 0, 69, 0.5F, false);
		register(TrainType.S_TRAIN, new ModelSTrain(), "mtr:textures/entity/s_train", "s_train", "sp1900", 0xC1CD23, "mtr:textures/entity/s_train", "", 0, 42, 0.5F, false);
		register(TrainType.S_TRAIN_SMALL, new ModelSTrainSmall(), "mtr:textures/entity/s_train", "s_train", "sp1900", 0xC1CD23, "mtr:textures/entity/s_train", "", 0, 42, 0.5F, false);
		register(TrainType.S_TRAIN_MINI, new ModelSTrainMini(), "mtr:textures/entity/s_train", "s_train", "sp1900", 0xC1CD23, "mtr:textures/entity/s_train", "", 0, 42, 0.5F, false);
		register(TrainType.A_TRAIN_TCL, new ModelATrain(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", 0xF69447, "mtr:textures/entity/a_train", "", 0, 78, 0.5F, false);
		register(TrainType.A_TRAIN_TCL_SMALL, new ModelATrainSmall(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", 0xF69447, "mtr:textures/entity/a_train", "", 0, 78, 0.5F, false);
		register(TrainType.A_TRAIN_TCL_MINI, new ModelATrainMini(false), "mtr:textures/entity/a_train_tcl", "a_train", "a_train", 0xF69447, "mtr:textures/entity/a_train", "", 0, 78, 0.5F, false);
		register(TrainType.A_TRAIN_AEL, new ModelATrain(true), "mtr:textures/entity/a_train_ael", "a_train", "a_train", 0x008D8D, "mtr:textures/entity/a_train", "", 0, 78, 0.5F, false);
		register(TrainType.A_TRAIN_AEL_MINI, new ModelATrainMini(true), "mtr:textures/entity/a_train_ael", "a_train", "a_train", 0x008D8D, "mtr:textures/entity/a_train", "", 0, 78, 0.5F, false);
		register(TrainType.LIGHT_RAIL_1, new ModelLightRail(1, false), "mtr:textures/entity/light_rail_1", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_1_RHT, new ModelLightRail(1, true), "mtr:textures/entity/light_rail_1", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_1R, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_1r", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_1R_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_1r", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_2, new ModelLightRail(2, false), "mtr:textures/entity/light_rail_2", "light_rail_aeg", "light_rail_3", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_2_RHT, new ModelLightRail(2, true), "mtr:textures/entity/light_rail_2", "light_rail_aeg", "light_rail_3", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_3, new ModelLightRail(3, false), "mtr:textures/entity/light_rail_3", "light_rail_mitsubishi", "light_rail_3", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_3_RHT, new ModelLightRail(3, true), "mtr:textures/entity/light_rail_3", "light_rail_mitsubishi", "light_rail_3", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_4, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_4_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_5, new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_5_RHT, new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_1R_OLD, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_1r_old", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_1R_OLD_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_1r_old", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_4_OLD, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4_old", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_4_OLD_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4_old", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_5_OLD, new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5_old", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_5_OLD_RHT, new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5_old", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_1_ORANGE, new ModelLightRail(1, false), "mtr:textures/entity/light_rail_1_orange", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_1_ORANGE_RHT, new ModelLightRail(1, true), "mtr:textures/entity/light_rail_1_orange", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_1R_ORANGE, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_1r_orange", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_1R_ORANGE_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_1r_orange", "light_rail_aeg", "light_rail_1", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_2_ORANGE, new ModelLightRail(2, false), "mtr:textures/entity/light_rail_2_orange", "light_rail_aeg", "light_rail_3", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_2_ORANGE_RHT, new ModelLightRail(2, true), "mtr:textures/entity/light_rail_2_orange", "light_rail_aeg", "light_rail_3", 0xD2A825, "", "", 0, 48, 1, false);
		register(TrainType.LIGHT_RAIL_3_ORANGE, new ModelLightRail(3, false), "mtr:textures/entity/light_rail_3_orange", "light_rail_mitsubishi", "light_rail_3", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_3_ORANGE_RHT, new ModelLightRail(3, true), "mtr:textures/entity/light_rail_3_orange", "light_rail_mitsubishi", "light_rail_3", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_4_ORANGE, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4_orange", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_4_ORANGE_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4_orange", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_5_ORANGE, new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5_orange", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LIGHT_RAIL_5_ORANGE_RHT, new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5_orange", "light_rail_mitsubishi", "light_rail_4", 0xD2A825, "", "", 0, 45, 1, false);
		register(TrainType.LONDON_UNDERGROUND_D78, new ModelLondonUndergroundD78(), "mtr:textures/entity/london_underground_d78", "mlr", "london_underground_d78", 0x007229, "", "mtr:textures/entity/london_underground_d78", 0, 93, 1, true);
		register(TrainType.LONDON_UNDERGROUND_D78_MINI, new ModelLondonUndergroundD78Mini(), "mtr:textures/entity/london_underground_d78", "mlr", "london_underground_d78", 0x007229, "", "mtr:textures/entity/london_underground_d78", 0, 93, 1, true);
		register(TrainType.LONDON_UNDERGROUND_1995, new ModelLondonUnderground1995(), "mtr:textures/entity/london_underground_1995", "london_underground_1995", "london_underground_1995", 0x333333, "", "mtr:textures/entity/london_underground_1995", 0, 72, 0.5F, false);
		register(TrainType.LONDON_UNDERGROUND_1996, new ModelLondonUnderground1995(), "mtr:textures/entity/london_underground_1996", "london_underground_1996", "london_underground_1995", 0xA1A5A7, "", "mtr:textures/entity/london_underground_1995", 0, 93, 0.5F, false);
		register(TrainType.R179, new ModelR179(), "mtr:textures/entity/r179", "r179", "r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 0, 66, 1, false);
		register(TrainType.R179_MINI, new ModelR179Mini(), "mtr:textures/entity/r179", "r179", "r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 0, 66, 1, false);
		register(TrainType.R211, new ModelR211(false), "mtr:textures/entity/r211", "s_train", "r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 0, 42, 1, false);
		register(TrainType.R211_MINI, new ModelR211Mini(false), "mtr:textures/entity/r211", "s_train", "r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 0, 42, 1, false);
		register(TrainType.R211T, new ModelR211(true), "mtr:textures/entity/r211", "s_train", "r179", 0xD5D5D5, "mtr:textures/entity/r211", "mtr:textures/entity/r179", 0, 42, 1, false);
		register(TrainType.R211T_MINI, new ModelR211Mini(true), "mtr:textures/entity/r211", "s_train", "r179", 0xD5D5D5, "mtr:textures/entity/r211", "mtr:textures/entity/r179", 0, 42, 1, false);
		register(TrainType.CLASS_802_GWR, new ModelClass802(), "mtr:textures/entity/class_802_gwr", "class_802", "class_802", 0x021E15, "mtr:textures/entity/sp1900", "", 0, 120, 1, false);
		register(TrainType.CLASS_802_GWR_MINI, new ModelClass802Mini(), "mtr:textures/entity/class_802_gwr", "class_802", "class_802", 0x021E15, "mtr:textures/entity/sp1900", "", 0, 120, 1, false);
		register(TrainType.CLASS_802_TPE, new ModelClass802(), "mtr:textures/entity/class_802_tpe", "class_802", "class_802", 0x00A6E6, "mtr:textures/entity/sp1900", "", 0, 120, 1, false);
		register(TrainType.CLASS_802_TPE_MINI, new ModelClass802Mini(), "mtr:textures/entity/class_802_tpe", "class_802", "class_802", 0x00A6E6, "mtr:textures/entity/sp1900", "", 0, 120, 1, false);
		register(TrainType.MINECART, null, "textures/entity/minecart", null, null, 0x666666, "", "", -0.5F, 0, 0.5F, false);
		register(TrainType.OAK_BOAT, null, "textures/entity/boat/oak", null, null, 0x8F7748, "", "", -0.5F, 0, 0.5F, false);
		register(TrainType.SPRUCE_BOAT, null, "textures/entity/boat/spruce", null, null, 0x8F7748, "", "", -0.5F, 0, 0.5F, false);
		register(TrainType.BIRCH_BOAT, null, "textures/entity/boat/birch", null, null, 0x8F7748, "", "", -0.5F, 0, 0.5F, false);
		register(TrainType.JUNGLE_BOAT, null, "textures/entity/boat/jungle", null, null, 0x8F7748, "", "", -0.5F, 0, 0.5F, false);
		register(TrainType.ACACIA_BOAT, null, "textures/entity/boat/acacia", null, null, 0x8F7748, "", "", -0.5F, 0, 0.5F, false);
		register(TrainType.DARK_OAK_BOAT, null, "textures/entity/boat/dark_oak", null, null, 0x8F7748, "", "", -0.5F, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_CRYSTAL, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_crystal", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_CRYSTAL_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_crystal", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_NORMAL_RED, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_red", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_NORMAL_RED_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_red", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_NORMAL_ORANGE, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_orange", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_NORMAL_ORANGE_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_orange", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_NORMAL_LIGHT_BLUE, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_light_blue", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
		register(TrainType.NGONG_PING_360_NORMAL_LIGHT_BLUE_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_light_blue", null, null, 0x062540, "", "", 0, 0, 0.5F, false);
	}

	public static TrainProperties getTrainProperties(String key) {
		final String keyLower = key.toLowerCase();
		return REGISTRY.containsKey(keyLower) ? REGISTRY.get(keyLower) : getBlankProperties();
	}

	public static TrainProperties getTrainProperties(TransportMode transportMode, int index) {
		return index >= 0 && index < KEY_ORDERS.get(transportMode).size() ? REGISTRY.get(KEY_ORDERS.get(transportMode).get(index)) : getBlankProperties();
	}

	public static String getTrainId(TransportMode transportMode, int index) {
		return KEY_ORDERS.get(transportMode).get(index >= 0 && index < KEY_ORDERS.get(transportMode).size() ? index : 0);
	}

	public static void forEach(TransportMode transportMode, BiConsumer<String, TrainProperties> biConsumer) {
		KEY_ORDERS.get(transportMode).forEach(key -> biConsumer.accept(key, REGISTRY.get(key)));
	}

	private static TrainProperties getBlankProperties() {
		return new TrainProperties("", null, null, null, null, new TranslatableComponent(""), 0, "", "", 0, 0, 0.5F, false);
	}

	public static class TrainProperties {

		public final String baseTrainType;
		public final ModelTrainBase model;
		public final String textureId;
		public final String speedSoundBaseId;
		public final String doorSoundBaseId;
		public final TranslatableComponent name;
		public final int color;
		public final String gangwayConnectionId;
		public final String trainBarrierId;
		public final float riderOffset;
		public final int speedSoundCount;
		public final float doorCloseSoundTime;

		private final boolean useAccelerationSoundsWhenCoasting;

		private final char[] SOUND_GROUP_LETTERS = {'a', 'b', 'c'};
		private final int SOUND_GROUP_SIZE = SOUND_GROUP_LETTERS.length;

		private TrainProperties(String baseTrainType, ModelTrainBase model, String textureId, String speedSoundBaseId, String doorSoundBaseId, TranslatableComponent name, int color, String gangwayConnectionId, String trainBarrierId, float riderOffset, int speedSoundCount, float doorCloseSoundTime, boolean useAccelerationSoundsWhenCoasting) {
			this.baseTrainType = baseTrainType;
			this.model = model;
			this.textureId = resolvePath(textureId);
			this.speedSoundBaseId = resolvePath(speedSoundBaseId);
			this.doorSoundBaseId = resolvePath(doorSoundBaseId);
			this.name = name;
			this.color = color;
			this.gangwayConnectionId = gangwayConnectionId;
			this.trainBarrierId = trainBarrierId;
			this.riderOffset = riderOffset;
			this.speedSoundCount = speedSoundCount;
			this.doorCloseSoundTime = doorCloseSoundTime;
			this.useAccelerationSoundsWhenCoasting = useAccelerationSoundsWhenCoasting;
		}

		public void playSpeedSoundEffect(Level world, BlockPos pos, float oldSpeed, float speed) {
			if (world instanceof ClientLevel && MTRClient.canPlaySound() && speedSoundCount > 0 && speedSoundBaseId != null) {
				// TODO: Better sound system to adapt to different acceleration
				final int floorSpeed = (int) Math.floor(speed / Train.ACCELERATION_DEFAULT / MTRClient.TICKS_PER_SPEED_SOUND);
				if (floorSpeed > 0) {
					final Random random = new Random();

					if (floorSpeed >= 30 && random.nextInt(RANDOM_SOUND_CHANCE) == 0) {
						((ClientLevel) world).playLocalSound(pos, new SoundEvent(new ResourceLocation(MTR.MOD_ID, speedSoundBaseId + SOUND_RANDOM)), SoundSource.BLOCKS, 10, 1, true);
					}

					final int index = Math.min(floorSpeed, speedSoundCount) - 1;
					final boolean isAccelerating = speed == oldSpeed ? useAccelerationSoundsWhenCoasting || random.nextBoolean() : speed > oldSpeed;
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
