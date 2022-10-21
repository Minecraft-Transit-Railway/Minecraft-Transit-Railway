package mtr.client;

import mtr.data.TrainType;
import mtr.data.TransportMode;
import mtr.mappings.Text;
import mtr.model.*;
import mtr.render.JonModelTrainRenderer;
import mtr.render.TrainRendererBase;
import mtr.sound.JonTrainSound;
import mtr.sound.TrainSoundBase;
import mtr.sound.bve.BveTrainSound;
import mtr.sound.bve.BveTrainSoundConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.BiConsumer;

public class TrainClientRegistry {

	private static final Map<String, TrainProperties> REGISTRY = new HashMap<>();
	private static final Map<TransportMode, List<String>> KEY_ORDERS = new HashMap<>();

	public static void register(String key, String baseTrainType, String name, int color, float riderOffset, float bogiePosition, boolean isJacobsBogie, boolean hasGangwayConnection, TrainRendererBase renderer, TrainSoundBase sound) {
		final String keyLower = key.toLowerCase(Locale.ENGLISH);
		final TransportMode transportMode = TrainType.getTransportMode(baseTrainType);
		if (!KEY_ORDERS.containsKey(transportMode)) {
			KEY_ORDERS.put(transportMode, new ArrayList<>());
		}
		if (!KEY_ORDERS.get(transportMode).contains(keyLower)) {
			KEY_ORDERS.get(transportMode).add(keyLower);
		}
		REGISTRY.put(keyLower, new TrainProperties(baseTrainType, Text.translatable(name == null ? "train.mtr." + keyLower : name), color, riderOffset, bogiePosition, isJacobsBogie, hasGangwayConnection, renderer, sound));
	}

	public static void register(String key, String baseTrainType, ModelTrainBase model, String textureId, String name, int color, String gangwayConnectionId, String trainBarrierId, float riderOffset, float bogiePosition, boolean isJacobsBogie, String soundId, JonTrainSound.JonTrainSoundConfig legacySoundConfig) {
		final TrainRendererBase renderer = new JonModelTrainRenderer(model, textureId, gangwayConnectionId, trainBarrierId);
		final TrainSoundBase sound = legacySoundConfig == null ? new BveTrainSound(new BveTrainSoundConfig(Minecraft.getInstance().getResourceManager(), soundId == null ? "" : soundId)) : new JonTrainSound(soundId, legacySoundConfig);
		register(key, baseTrainType, name, color, riderOffset, bogiePosition, isJacobsBogie, !StringUtils.isEmpty(gangwayConnectionId), renderer, sound);
	}

	private static void register(TrainType defaultTrainType, ModelTrainBase model, String textureId, int color, String gangwayConnectionId, String trainBarrierId, float bogiePosition, boolean isJacobsBogie, String soundId, JonTrainSound.JonTrainSoundConfig legacySoundConfig) {
		register(defaultTrainType.toString(), defaultTrainType.baseTrainType, model, textureId, null, color, gangwayConnectionId, trainBarrierId, 0, bogiePosition, isJacobsBogie, soundId, legacySoundConfig);
	}

	private static void register(TrainType defaultTrainType, ModelTrainBase model, String textureId, int color, float riderOffset) {
		register(defaultTrainType.toString(), defaultTrainType.baseTrainType, model, textureId, null, color, "", "", riderOffset, 0, false, null, new JonTrainSound.JonTrainSoundConfig(null, 0, 0.5F, false));
	}

	public static void reset() {
		REGISTRY.clear();
		KEY_ORDERS.clear();

		register(TrainType.SP1900, new ModelSP1900(false), "mtr:textures/entity/sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 8.5F, false, "sp1900", new JonTrainSound.JonTrainSoundConfig("sp1900", 120, 0.5F, false));
		register(TrainType.SP1900_SMALL, new ModelSP1900Small(false), "mtr:textures/entity/sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 6F, false, "sp1900", new JonTrainSound.JonTrainSoundConfig("sp1900", 120, 0.5F, false));
		register(TrainType.SP1900_MINI, new ModelSP1900Mini(false), "mtr:textures/entity/sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 3F, true, "sp1900", new JonTrainSound.JonTrainSoundConfig("sp1900", 120, 0.5F, false));
		register(TrainType.C1141A, new ModelSP1900(true), "mtr:textures/entity/c1141a", 0xB42249, "mtr:textures/entity/sp1900", "", 8.5F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.C1141A_SMALL, new ModelSP1900Small(true), "mtr:textures/entity/c1141a", 0xB42249, "mtr:textures/entity/sp1900", "", 6F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.C1141A_MINI, new ModelSP1900Mini(true), "mtr:textures/entity/c1141a", 0xB42249, "mtr:textures/entity/sp1900", "", 3F, true, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.M_TRAIN, new ModelMTrain(), "mtr:textures/entity/m_train", 0x999999, "mtr:textures/entity/m_train", "", 8.5F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.M_TRAIN_SMALL, new ModelMTrainSmall(), "mtr:textures/entity/m_train", 0x999999, "mtr:textures/entity/m_train", "", 6F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.M_TRAIN_MINI, new ModelMTrainMini(), "mtr:textures/entity/m_train", 0x999999, "mtr:textures/entity/m_train", "", 2.5F, true, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.CM_STOCK, new ModelCMStock(), "mtr:textures/entity/cm_stock", 0x999999, "mtr:textures/entity/m_train", "", 8.5F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.CM_STOCK_SMALL, new ModelCMStockSmall(), "mtr:textures/entity/cm_stock", 0x999999, "mtr:textures/entity/m_train", "", 6F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.CM_STOCK_MINI, new ModelCMStockMini(), "mtr:textures/entity/cm_stock", 0x999999, "mtr:textures/entity/m_train", "", 2.5F, true, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.MLR, new ModelMLR(), "mtr:textures/entity/mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 8.5F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.MLR_SMALL, new ModelMLRSmall(), "mtr:textures/entity/mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 6F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.MLR_MINI, new ModelMLRMini(), "mtr:textures/entity/mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 3F, true, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.E44, new ModelE44(), "mtr:textures/entity/e44", 0xE7AF25, "mtr:textures/entity/m_train", "", 8.5F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("m_train", 93, 0.5F, true));
		register(TrainType.E44_MINI, new ModelE44Mini(), "mtr:textures/entity/e44", 0xE7AF25, "mtr:textures/entity/m_train", "", 3F, true, "mlr", new JonTrainSound.JonTrainSoundConfig("m_train", 93, 0.5F, true));
		register(TrainType.R_TRAIN, new ModelRTrain(), "mtr:textures/entity/r_train", 0x6CB5E2, "mtr:textures/entity/sp1900", "mtr:textures/entity/mlr", 8.5F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.R_TRAIN_SMALL, new ModelRTrainSmall(), "mtr:textures/entity/r_train", 0x6CB5E2, "mtr:textures/entity/sp1900", "mtr:textures/entity/mlr", 6F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.R_TRAIN_MINI, new ModelRTrainMini(), "mtr:textures/entity/r_train", 0x6CB5E2, "mtr:textures/entity/sp1900", "mtr:textures/entity/mlr", 2.5F, true, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.DRL, new ModelDRL(), "mtr:textures/entity/drl", 0xF287B7, "mtr:textures/entity/m_train", "", 8.5F, false, "m_train", new JonTrainSound.JonTrainSoundConfig(null, 90, 0.5F, false));
		register(TrainType.K_TRAIN, new ModelKTrain(false), "mtr:textures/entity/k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 8.5F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_SMALL, new ModelKTrainSmall(false), "mtr:textures/entity/k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 6F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_MINI, new ModelKTrainMini(false), "mtr:textures/entity/k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 2.5F, true, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_TCL, new ModelKTrain(true), "mtr:textures/entity/k_train_tcl", 0x0EAB52, "mtr:textures/entity/k_train", "", 8.5F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_TCL_SMALL, new ModelKTrainSmall(true), "mtr:textures/entity/k_train_tcl", 0x0EAB52, "mtr:textures/entity/k_train", "", 6F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_TCL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_tcl", 0x0EAB52, "mtr:textures/entity/k_train", "", 2.5F, true, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_AEL, new ModelKTrain(true), "mtr:textures/entity/k_train_ael", 0x0EAB52, "mtr:textures/entity/k_train", "", 8.5F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_AEL_SMALL, new ModelKTrainSmall(true), "mtr:textures/entity/k_train_ael", 0x0EAB52, "mtr:textures/entity/k_train", "", 6F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_AEL_MINI, new ModelKTrainMini(true), "mtr:textures/entity/k_train_ael", 0x0EAB52, "mtr:textures/entity/k_train", "", 2.5F, true, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.C_TRAIN, new ModelCTrain(), "mtr:textures/entity/c_train", 0xFDD900, "mtr:textures/entity/s_train", "", 8.5F, false, "c_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 69, 0.5F, false));
		register(TrainType.C_TRAIN_SMALL, new ModelCTrainSmall(), "mtr:textures/entity/c_train", 0xFDD900, "mtr:textures/entity/s_train", "", 6F, false, "c_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 69, 0.5F, false));
		register(TrainType.C_TRAIN_MINI, new ModelCTrainMini(), "mtr:textures/entity/c_train", 0xFDD900, "mtr:textures/entity/s_train", "", 2.5F, true, "c_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 69, 0.5F, false));
		register(TrainType.S_TRAIN, new ModelSTrain(), "mtr:textures/entity/s_train", 0xC1CD23, "mtr:textures/entity/s_train", "", 8.5F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 42, 0.5F, false));
		register(TrainType.S_TRAIN_SMALL, new ModelSTrainSmall(), "mtr:textures/entity/s_train", 0xC1CD23, "mtr:textures/entity/s_train", "", 6F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 42, 0.5F, false));
		register(TrainType.S_TRAIN_MINI, new ModelSTrainMini(), "mtr:textures/entity/s_train", 0xC1CD23, "mtr:textures/entity/s_train", "", 2.5F, true, "s_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 42, 0.5F, false));
		register(TrainType.A_TRAIN_TCL, new ModelATrain(false), "mtr:textures/entity/a_train_tcl", 0xF69447, "mtr:textures/entity/a_train", "", 8.5F, false, "a_train", new JonTrainSound.JonTrainSoundConfig("a_train", 78, 0.5F, false));
		register(TrainType.A_TRAIN_TCL_SMALL, new ModelATrainSmall(false), "mtr:textures/entity/a_train_tcl", 0xF69447, "mtr:textures/entity/a_train", "", 6F, false, "a_train", new JonTrainSound.JonTrainSoundConfig("a_train", 78, 0.5F, false));
		register(TrainType.A_TRAIN_TCL_MINI, new ModelATrainMini(false), "mtr:textures/entity/a_train_tcl", 0xF69447, "mtr:textures/entity/a_train", "", 2.5F, true, "a_train", new JonTrainSound.JonTrainSoundConfig("a_train", 78, 0.5F, false));
		register(TrainType.A_TRAIN_AEL, new ModelATrain(true), "mtr:textures/entity/a_train_ael", 0x008D8D, "mtr:textures/entity/a_train", "", 8.5F, false, "a_train", new JonTrainSound.JonTrainSoundConfig("a_train", 78, 0.5F, false));
		register(TrainType.A_TRAIN_AEL_MINI, new ModelATrainMini(true), "mtr:textures/entity/a_train_ael", 0x008D8D, "mtr:textures/entity/a_train", "", 5F, true, "a_train", new JonTrainSound.JonTrainSoundConfig("a_train", 78, 0.5F, false));
		register(TrainType.LIGHT_RAIL_1, new ModelLightRail(1, false), "mtr:textures/entity/light_rail_1", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1_RHT, new ModelLightRail(1, true), "mtr:textures/entity/light_rail_1", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R, new ModelLightRail(6, false), "mtr:textures/entity/light_rail_1r", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_RHT, new ModelLightRail(6, true), "mtr:textures/entity/light_rail_1r", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2, new ModelLightRail(2, false), "mtr:textures/entity/light_rail_2", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2_RHT, new ModelLightRail(2, true), "mtr:textures/entity/light_rail_2", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_3, new ModelLightRail(3, false), "mtr:textures/entity/light_rail_3", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_3_RHT, new ModelLightRail(3, true), "mtr:textures/entity/light_rail_3", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5, new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_RHT, new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_1R_OLD, new ModelLightRail(6, false), "mtr:textures/entity/light_rail_1r_old", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_OLD_RHT, new ModelLightRail(6, true), "mtr:textures/entity/light_rail_1r_old", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_4_OLD, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_OLD_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_OLD, new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_OLD_RHT, new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_1_ORANGE, new ModelLightRail(1, false), "mtr:textures/entity/light_rail_1_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1_ORANGE_RHT, new ModelLightRail(1, true), "mtr:textures/entity/light_rail_1_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_ORANGE, new ModelLightRail(6, false), "mtr:textures/entity/light_rail_1r_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_ORANGE_RHT, new ModelLightRail(6, true), "mtr:textures/entity/light_rail_1r_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2_ORANGE, new ModelLightRail(2, false), "mtr:textures/entity/light_rail_2_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2_ORANGE_RHT, new ModelLightRail(2, true), "mtr:textures/entity/light_rail_2_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_3_ORANGE, new ModelLightRail(3, false), "mtr:textures/entity/light_rail_3_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_3_ORANGE_RHT, new ModelLightRail(3, true), "mtr:textures/entity/light_rail_3_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_ORANGE, new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_ORANGE_RHT, new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_ORANGE, new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_ORANGE_RHT, new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LONDON_UNDERGROUND_D78, new ModelLondonUndergroundD78(), "mtr:textures/entity/london_underground_d78", 0x007229, "", "mtr:textures/entity/london_underground_d78", 5.75F, false, "london_underground_d78", null);
		register(TrainType.LONDON_UNDERGROUND_D78_MINI, new ModelLondonUndergroundD78Mini(), "mtr:textures/entity/london_underground_d78", 0x007229, "", "mtr:textures/entity/london_underground_d78", 2F, true, "london_underground_d78", null);
		register(TrainType.LONDON_UNDERGROUND_1995, new ModelLondonUnderground1995(), "mtr:textures/entity/london_underground_1995", 0x333333, "", "", 6F, false, "london_underground_1995", null);
		register(TrainType.LONDON_UNDERGROUND_1996, new ModelLondonUnderground1995(), "mtr:textures/entity/london_underground_1996", 0xA1A5A7, "", "", 6F, false, "london_underground_1996", null);
		register(TrainType.R179, new ModelR179(), "mtr:textures/entity/r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 7.5F, false, "r179", new JonTrainSound.JonTrainSoundConfig("r179", 66, 1, false));
		register(TrainType.R179_MINI, new ModelR179Mini(), "mtr:textures/entity/r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 2.5F, true, "r179", new JonTrainSound.JonTrainSoundConfig("r179", 66, 1, false));
		register(TrainType.R211, new ModelR211(false), "mtr:textures/entity/r211", 0xD5D5D5, "", "mtr:textures/entity/r179", 7.5F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.R211_MINI, new ModelR211Mini(false), "mtr:textures/entity/r211", 0xD5D5D5, "", "mtr:textures/entity/r179", 2.5F, true, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.R211T, new ModelR211(true), "mtr:textures/entity/r211", 0xD5D5D5, "mtr:textures/entity/r211", "mtr:textures/entity/r179", 7.5F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.R211T_MINI, new ModelR211Mini(true), "mtr:textures/entity/r211", 0xD5D5D5, "mtr:textures/entity/r211", "mtr:textures/entity/r179", 2.5F, true, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.CLASS_377_SOUTHERN, new ModelClass377(), "mtr:textures/entity/class_377_southern", 0x5AB565, "mtr:textures/entity/sp1900", "", 6, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.CLASS_802_GWR, new ModelClass802(), "mtr:textures/entity/class_802_gwr", 0x021E15, "mtr:textures/entity/sp1900", "", 7.75F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.CLASS_802_GWR_MINI, new ModelClass802Mini(), "mtr:textures/entity/class_802_gwr", 0x021E15, "mtr:textures/entity/sp1900", "", 4.94F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.CLASS_802_TPE, new ModelClass802(), "mtr:textures/entity/class_802_tpe", 0x00A6E6, "mtr:textures/entity/sp1900", "", 7.75F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.CLASS_802_TPE_MINI, new ModelClass802Mini(), "mtr:textures/entity/class_802_tpe", 0x00A6E6, "mtr:textures/entity/sp1900", "", 4.94F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.MINECART, null, "textures/entity/minecart", 0x666666, -0.5F);
		register(TrainType.OAK_BOAT, null, "textures/entity/boat/oak", 0x8F7748, -0.5F);
		register(TrainType.SPRUCE_BOAT, null, "textures/entity/boat/spruce", 0x8F7748, -0.5F);
		register(TrainType.BIRCH_BOAT, null, "textures/entity/boat/birch", 0x8F7748, -0.5F);
		register(TrainType.JUNGLE_BOAT, null, "textures/entity/boat/jungle", 0x8F7748, -0.5F);
		register(TrainType.ACACIA_BOAT, null, "textures/entity/boat/acacia", 0x8F7748, -0.5F);
		register(TrainType.DARK_OAK_BOAT, null, "textures/entity/boat/dark_oak", 0x8F7748, -0.5F);
		register(TrainType.NGONG_PING_360_CRYSTAL, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_crystal", 0x062540, 0);
		register(TrainType.NGONG_PING_360_CRYSTAL_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_crystal", 0x062540, 0);
		register(TrainType.NGONG_PING_360_NORMAL_RED, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_red", 0x062540, 0);
		register(TrainType.NGONG_PING_360_NORMAL_RED_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_red", 0x062540, 0);
		register(TrainType.NGONG_PING_360_NORMAL_ORANGE, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_orange", 0x062540, 0);
		register(TrainType.NGONG_PING_360_NORMAL_ORANGE_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_orange", 0x062540, 0);
		register(TrainType.NGONG_PING_360_NORMAL_LIGHT_BLUE, new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_light_blue", 0x062540, 0);
		register(TrainType.NGONG_PING_360_NORMAL_LIGHT_BLUE_RHT, new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_light_blue", 0x062540, 0);
	}

	public static TrainProperties getTrainProperties(String key) {
		final String keyLower = key.toLowerCase(Locale.ENGLISH);
		return REGISTRY.getOrDefault(keyLower, getBlankProperties());
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
		return new TrainProperties(
				"", Text.translatable(""), 0, 0, 0, false, false,
				new JonModelTrainRenderer(null, "", "", ""),
				new JonTrainSound("", new JonTrainSound.JonTrainSoundConfig(null, 0, 0.5F, false))
		);
	}

	public static class TrainProperties {

		public final String baseTrainType;
		public final Component name;
		public final int color;
		public final float riderOffset;
		public final float bogiePosition;
		public final boolean isJacobsBogie;
		public final boolean hasGangwayConnection;
		public final TrainRendererBase renderer;
		public final TrainSoundBase sound;

		private TrainProperties(String baseTrainType, Component name, int color, float riderOffset, float bogiePosition, boolean isJacobsBogie, boolean hasGangwayConnection, TrainRendererBase renderer, TrainSoundBase sound) {
			this.baseTrainType = baseTrainType;
			this.name = name;
			this.color = color;
			this.riderOffset = riderOffset;
			this.bogiePosition = bogiePosition;
			this.isJacobsBogie = isJacobsBogie;
			this.hasGangwayConnection = hasGangwayConnection;
			this.renderer = renderer;
			this.sound = sound;
		}
	}
}
