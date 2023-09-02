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
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.function.BiConsumer;

public class TrainClientRegistry {

	private static final Map<String, TrainProperties> REGISTRY = new HashMap<>();
	private static final Map<TransportMode, List<String>> KEY_ORDERS = new HashMap<>();

	public static void register(String key, TrainProperties properties) {
		final String keyLower = key.toLowerCase(Locale.ENGLISH);
		final TransportMode transportMode = TrainType.getTransportMode(properties.baseTrainType);
		if (!KEY_ORDERS.containsKey(transportMode)) {
			KEY_ORDERS.put(transportMode, new ArrayList<>());
		}
		if (!KEY_ORDERS.get(transportMode).contains(keyLower)) {
			KEY_ORDERS.get(transportMode).add(keyLower);
		}
		REGISTRY.put(keyLower, properties);
	}

	public static void register(String key, String baseTrainType, String name, String description, String wikipediaArticle, ModelTrainBase model, String textureId, int color, String gangwayConnectionId, String trainBarrierId, float riderOffset, float riderOffsetDismounting, float bogiePosition, boolean isJacobsBogie, String soundId, JonTrainSound.JonTrainSoundConfig legacySoundConfig) {
		final TrainRendererBase renderer = new JonModelTrainRenderer(model, textureId, gangwayConnectionId, trainBarrierId);
		final TrainSoundBase sound = legacySoundConfig == null ? new BveTrainSound(new BveTrainSoundConfig(Minecraft.getInstance().getResourceManager(), soundId == null ? "" : soundId)) : new JonTrainSound(soundId, legacySoundConfig);
		register(key, new TrainProperties(baseTrainType, Text.translatable(name == null ? "train.mtr." + key.toLowerCase(Locale.ENGLISH) : name), description, wikipediaArticle, color, riderOffset, riderOffsetDismounting, bogiePosition, isJacobsBogie, !StringUtils.isEmpty(gangwayConnectionId), renderer, sound));
	}

	private static void register(TrainType defaultTrainType, String wikipediaArticle, ModelTrainBase model, String textureId, int color, String gangwayConnectionId, String trainBarrierId, float bogiePosition, boolean isJacobsBogie, String soundId, JonTrainSound.JonTrainSoundConfig legacySoundConfig) {
		register(defaultTrainType.toString(), defaultTrainType.baseTrainType, null, null, wikipediaArticle, model, textureId, color, gangwayConnectionId, trainBarrierId, 0, 0, bogiePosition, isJacobsBogie, soundId, legacySoundConfig);
	}

	private static void register(TrainType defaultTrainType, String wikipediaArticle, ModelTrainBase model, String textureId, int color, float riderOffset, float riderOffsetDismounting) {
		register(defaultTrainType.toString(), defaultTrainType.baseTrainType, null, null, wikipediaArticle, model, textureId, color, "", "", riderOffset, riderOffsetDismounting, 0, false, null, new JonTrainSound.JonTrainSoundConfig(null, 0, 0.5F, false));
	}

	public static void reset() {
		REGISTRY.clear();
		KEY_ORDERS.clear();

		register(TrainType.SP1900, "SP1900_EMU", new ModelSP1900(false), "mtr:textures/entity/sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 8.5F, false, "sp1900", new JonTrainSound.JonTrainSoundConfig("sp1900", 120, 0.5F, false));
		register(TrainType.SP1900_SMALL, "SP1900_EMU", new ModelSP1900Small(false), "mtr:textures/entity/sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 6F, false, "sp1900", new JonTrainSound.JonTrainSoundConfig("sp1900", 120, 0.5F, false));
		register(TrainType.SP1900_MINI, "SP1900_EMU", new ModelSP1900Mini(false), "mtr:textures/entity/sp1900", 0x003399, "mtr:textures/entity/sp1900", "", 3F, true, "sp1900", new JonTrainSound.JonTrainSoundConfig("sp1900", 120, 0.5F, false));
		register(TrainType.C1141A, "MTR_CRRC_Changchun_EMU", new ModelSP1900(true), "mtr:textures/entity/c1141a", 0xB42249, "mtr:textures/entity/sp1900", "", 8.5F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.C1141A_SMALL, "MTR_CRRC_Changchun_EMU", new ModelSP1900Small(true), "mtr:textures/entity/c1141a", 0xB42249, "mtr:textures/entity/sp1900", "", 6F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.C1141A_MINI, "MTR_CRRC_Changchun_EMU", new ModelSP1900Mini(true), "mtr:textures/entity/c1141a", 0xB42249, "mtr:textures/entity/sp1900", "", 3F, true, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.M_TRAIN, "MTR_Metro_Cammell_EMU_(DC)", new ModelMTrain(), "mtr:textures/entity/m_train", 0x999999, "mtr:textures/entity/m_train", "", 8.5F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.M_TRAIN_SMALL, "MTR_Metro_Cammell_EMU_(DC)", new ModelMTrainSmall(), "mtr:textures/entity/m_train", 0x999999, "mtr:textures/entity/m_train", "", 6F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.M_TRAIN_MINI, "MTR_Metro_Cammell_EMU_(DC)", new ModelMTrainMini(), "mtr:textures/entity/m_train", 0x999999, "mtr:textures/entity/m_train", "", 2.5F, true, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.CM_STOCK, "MTR_Metro_Cammell_EMU_(DC)", new ModelCMStock(), "mtr:textures/entity/cm_stock", 0x999999, "mtr:textures/entity/m_train", "", 8.5F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.CM_STOCK_SMALL, "MTR_Metro_Cammell_EMU_(DC)", new ModelCMStockSmall(), "mtr:textures/entity/cm_stock", 0x999999, "mtr:textures/entity/m_train", "", 6F, false, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.CM_STOCK_MINI, "MTR_Metro_Cammell_EMU_(DC)", new ModelCMStockMini(), "mtr:textures/entity/cm_stock", 0x999999, "mtr:textures/entity/m_train", "", 2.5F, true, "m_train", new JonTrainSound.JonTrainSoundConfig("m_train", 90, 0.5F, false));
		register(TrainType.MLR, "MTR_Metro_Cammell_EMU_(AC)", new ModelMLR(false), "mtr:textures/entity/mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 8.5F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.MLR_SMALL, "MTR_Metro_Cammell_EMU_(AC)", new ModelMLRSmall(false), "mtr:textures/entity/mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 6F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.MLR_MINI, "MTR_Metro_Cammell_EMU_(AC)", new ModelMLRMini(false), "mtr:textures/entity/mlr", 0x6CB5E2, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 3F, true, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.MLR_CHRISTMAS, "Christmas", new ModelMLR(true), "mtr:textures/entity/mlr", 0x00873E, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 8.5F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.MLR_CHRISTMAS_SMALL, "Christmas", new ModelMLRSmall(true), "mtr:textures/entity/mlr", 0x00873E, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 6F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.MLR_CHRISTMAS_MINI, "Christmas", new ModelMLRMini(true), "mtr:textures/entity/mlr", 0x00873E, "mtr:textures/entity/m_train", "mtr:textures/entity/mlr", 3F, true, "mlr", new JonTrainSound.JonTrainSoundConfig("mlr", 93, 0.5F, true));
		register(TrainType.E44, "MTR_Metro_Cammell_EMU_(AC)", new ModelE44(), "mtr:textures/entity/e44", 0xE7AF25, "mtr:textures/entity/m_train", "", 8.5F, false, "mlr", new JonTrainSound.JonTrainSoundConfig("m_train", 93, 0.5F, true));
		register(TrainType.E44_MINI, "MTR_Metro_Cammell_EMU_(AC)", new ModelE44Mini(), "mtr:textures/entity/e44", 0xE7AF25, "mtr:textures/entity/m_train", "", 3F, true, "mlr", new JonTrainSound.JonTrainSoundConfig("m_train", 93, 0.5F, true));
		register(TrainType.R_TRAIN, "MTR_Hyundai_Rotem_EMU", new ModelRTrain(), "mtr:textures/entity/r_train", 0x6CB5E2, "mtr:textures/entity/sp1900", "mtr:textures/entity/mlr", 8.5F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.R_TRAIN_SMALL, "MTR_Hyundai_Rotem_EMU", new ModelRTrainSmall(), "mtr:textures/entity/r_train", 0x6CB5E2, "mtr:textures/entity/sp1900", "mtr:textures/entity/mlr", 6F, false, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.R_TRAIN_MINI, "MTR_Hyundai_Rotem_EMU", new ModelRTrainMini(), "mtr:textures/entity/r_train", 0x6CB5E2, "mtr:textures/entity/sp1900", "mtr:textures/entity/mlr", 2.5F, true, "c1141a", new JonTrainSound.JonTrainSoundConfig("sp1900", 96, 0.5F, false));
		register(TrainType.DRL, "MTR_Metro_Cammell_EMU_(DC)", new ModelDRL(), "mtr:textures/entity/drl", 0xF287B7, "mtr:textures/entity/m_train", "", 8.5F, false, "m_train", new JonTrainSound.JonTrainSoundConfig(null, 90, 0.5F, false));
		register(TrainType.K_TRAIN, "MTR_Rotem_EMU", new ModelKTrain(false), "mtr:textures/entity/k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 8.5F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_SMALL, "MTR_Rotem_EMU", new ModelKTrainSmall(false), "mtr:textures/entity/k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 6F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_MINI, "MTR_Rotem_EMU", new ModelKTrainMini(false), "mtr:textures/entity/k_train", 0x0EAB52, "mtr:textures/entity/k_train", "", 2.5F, true, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_TCL, "MTR_Rotem_EMU", new ModelKTrain(true), "mtr:textures/entity/k_train_tcl", 0x0EAB52, "mtr:textures/entity/k_train", "", 8.5F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_TCL_SMALL, "MTR_Rotem_EMU", new ModelKTrainSmall(true), "mtr:textures/entity/k_train_tcl", 0x0EAB52, "mtr:textures/entity/k_train", "", 6F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_TCL_MINI, "MTR_Rotem_EMU", new ModelKTrainMini(true), "mtr:textures/entity/k_train_tcl", 0x0EAB52, "mtr:textures/entity/k_train", "", 2.5F, true, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_AEL, "MTR_Rotem_EMU", new ModelKTrain(true), "mtr:textures/entity/k_train_ael", 0x0EAB52, "mtr:textures/entity/k_train", "", 8.5F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_AEL_SMALL, "MTR_Rotem_EMU", new ModelKTrainSmall(true), "mtr:textures/entity/k_train_ael", 0x0EAB52, "mtr:textures/entity/k_train", "", 6F, false, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.K_TRAIN_AEL_MINI, "MTR_Rotem_EMU", new ModelKTrainMini(true), "mtr:textures/entity/k_train_ael", 0x0EAB52, "mtr:textures/entity/k_train", "", 2.5F, true, "k_train", new JonTrainSound.JonTrainSoundConfig("k_train", 66, 1, false));
		register(TrainType.C_TRAIN, "MTR_CNR_Changchun_EMU", new ModelCTrain(), "mtr:textures/entity/c_train", 0xFDD900, "mtr:textures/entity/s_train", "", 8.5F, false, "c_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 69, 0.5F, false));
		register(TrainType.C_TRAIN_SMALL, "MTR_CNR_Changchun_EMU", new ModelCTrainSmall(), "mtr:textures/entity/c_train", 0xFDD900, "mtr:textures/entity/s_train", "", 6F, false, "c_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 69, 0.5F, false));
		register(TrainType.C_TRAIN_MINI, "MTR_CNR_Changchun_EMU", new ModelCTrainMini(), "mtr:textures/entity/c_train", 0xFDD900, "mtr:textures/entity/s_train", "", 2.5F, true, "c_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 69, 0.5F, false));
		register(TrainType.S_TRAIN, "MTR_Urban_Lines_Vision_Train", new ModelSTrain(), "mtr:textures/entity/s_train", 0xC1CD23, "mtr:textures/entity/s_train", "", 8.5F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 42, 0.5F, false));
		register(TrainType.S_TRAIN_SMALL, "MTR_Urban_Lines_Vision_Train", new ModelSTrainSmall(), "mtr:textures/entity/s_train", 0xC1CD23, "mtr:textures/entity/s_train", "", 6F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 42, 0.5F, false));
		register(TrainType.S_TRAIN_MINI, "MTR_Urban_Lines_Vision_Train", new ModelSTrainMini(), "mtr:textures/entity/s_train", 0xC1CD23, "mtr:textures/entity/s_train", "", 2.5F, true, "s_train", new JonTrainSound.JonTrainSoundConfig("sp1900", 42, 0.5F, false));
		register(TrainType.A_TRAIN_TCL, "MTR_Adtranz%E2%80%93CAF_EMU", new ModelATrain(false), "mtr:textures/entity/a_train_tcl", 0xF69447, "mtr:textures/entity/a_train", "", 8.5F, false, "a_train", null);
		register(TrainType.A_TRAIN_TCL_SMALL, "MTR_Adtranz%E2%80%93CAF_EMU", new ModelATrainSmall(false), "mtr:textures/entity/a_train_tcl", 0xF69447, "mtr:textures/entity/a_train", "", 6F, false, "a_train", null);
		register(TrainType.A_TRAIN_TCL_MINI, "MTR_Adtranz%E2%80%93CAF_EMU", new ModelATrainMini(false), "mtr:textures/entity/a_train_tcl", 0xF69447, "mtr:textures/entity/a_train", "", 2.5F, true, "a_train", null);
		register(TrainType.A_TRAIN_AEL, "MTR_Adtranz%E2%80%93CAF_EMU", new ModelATrain(true), "mtr:textures/entity/a_train_ael", 0x008D8D, "mtr:textures/entity/a_train", "", 8.5F, false, "a_train", null);
		register(TrainType.A_TRAIN_AEL_MINI, "MTR_Adtranz%E2%80%93CAF_EMU", new ModelATrainMini(true), "mtr:textures/entity/a_train_ael", 0x008D8D, "mtr:textures/entity/a_train", "", 5F, true, "a_train", null);
		register(TrainType.LIGHT_RAIL_1, "Light_Rail_(MTR)", new ModelLightRail(1, false), "mtr:textures/entity/light_rail_1", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1_RHT, "Light_Rail_(MTR)", new ModelLightRail(1, true), "mtr:textures/entity/light_rail_1", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R, "Light_Rail_(MTR)", new ModelLightRail(6, false), "mtr:textures/entity/light_rail_1r", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_RHT, "Light_Rail_(MTR)", new ModelLightRail(6, true), "mtr:textures/entity/light_rail_1r", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2, "Light_Rail_(MTR)", new ModelLightRail(2, false), "mtr:textures/entity/light_rail_2", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2_RHT, "Light_Rail_(MTR)", new ModelLightRail(2, true), "mtr:textures/entity/light_rail_2", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2R, "Light_Rail_(MTR)", new ModelLightRail(5, false), "mtr:textures/entity/light_rail_2r", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2R_RHT, "Light_Rail_(MTR)", new ModelLightRail(5, true), "mtr:textures/entity/light_rail_2r", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_3, "Light_Rail_(MTR)", new ModelLightRail(3, false), "mtr:textures/entity/light_rail_3", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_3_RHT, "Light_Rail_(MTR)", new ModelLightRail(3, true), "mtr:textures/entity/light_rail_3", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_3R, "Light_Rail_(MTR)", new ModelLightRail(7, false), "mtr:textures/entity/light_rail_3r", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_3R_RHT, "Light_Rail_(MTR)", new ModelLightRail(7, true), "mtr:textures/entity/light_rail_3r", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4, "Light_Rail_(MTR)", new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_RHT, "Light_Rail_(MTR)", new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5, "Light_Rail_(MTR)", new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_RHT, "Light_Rail_(MTR)", new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_1R_OLD, "Light_Rail_(MTR)", new ModelLightRail(6, false), "mtr:textures/entity/light_rail_1r_old", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_OLD_RHT, "Light_Rail_(MTR)", new ModelLightRail(6, true), "mtr:textures/entity/light_rail_1r_old", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_4_OLD, "Light_Rail_(MTR)", new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_OLD_RHT, "Light_Rail_(MTR)", new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_OLD, "Light_Rail_(MTR)", new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_OLD_RHT, "Light_Rail_(MTR)", new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5_old", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_1_ORANGE, "Light_Rail_(MTR)", new ModelLightRail(1, false), "mtr:textures/entity/light_rail_1_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1_ORANGE_RHT, "Light_Rail_(MTR)", new ModelLightRail(1, true), "mtr:textures/entity/light_rail_1_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_ORANGE, "Light_Rail_(MTR)", new ModelLightRail(6, false), "mtr:textures/entity/light_rail_1r_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_1R_ORANGE_RHT, "Light_Rail_(MTR)", new ModelLightRail(6, true), "mtr:textures/entity/light_rail_1r_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_1", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2_ORANGE, "Light_Rail_(MTR)", new ModelLightRail(2, false), "mtr:textures/entity/light_rail_2_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_2_ORANGE_RHT, "Light_Rail_(MTR)", new ModelLightRail(2, true), "mtr:textures/entity/light_rail_2_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_aeg", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 48, 1, false));
		register(TrainType.LIGHT_RAIL_3_ORANGE, "Light_Rail_(MTR)", new ModelLightRail(3, false), "mtr:textures/entity/light_rail_3_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_3_ORANGE_RHT, "Light_Rail_(MTR)", new ModelLightRail(3, true), "mtr:textures/entity/light_rail_3_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_3", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_ORANGE, "Light_Rail_(MTR)", new ModelLightRail(4, false), "mtr:textures/entity/light_rail_4_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_4_ORANGE_RHT, "Light_Rail_(MTR)", new ModelLightRail(4, true), "mtr:textures/entity/light_rail_4_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_ORANGE, "Light_Rail_(MTR)", new ModelLightRail(5, false), "mtr:textures/entity/light_rail_5_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LIGHT_RAIL_5_ORANGE_RHT, "Light_Rail_(MTR)", new ModelLightRail(5, true), "mtr:textures/entity/light_rail_5_orange", 0xD2A825, "", "", 6.25F, false, "light_rail_mitsubishi", new JonTrainSound.JonTrainSoundConfig("light_rail_4", 45, 1, false));
		register(TrainType.LONDON_UNDERGROUND_D78, "London_Underground_D78_Stock", new ModelLondonUndergroundD78(), "mtr:textures/entity/london_underground_d78", 0x007229, "", "mtr:textures/entity/london_underground_d78", 5.75F, false, "london_underground_d78", null);
		register(TrainType.LONDON_UNDERGROUND_D78_MINI, "London_Underground_D78_Stock", new ModelLondonUndergroundD78Mini(), "mtr:textures/entity/london_underground_d78", 0x007229, "", "mtr:textures/entity/london_underground_d78", 2F, true, "london_underground_d78", null);
		register(TrainType.LONDON_UNDERGROUND_1995, "London_Underground_1995_Stock", new ModelLondonUnderground1995(true), "mtr:textures/entity/london_underground_1995", 0x333333, "", "", 6F, false, "london_underground_1995", null);
		register(TrainType.LONDON_UNDERGROUND_1996, "London_Underground_1996_Stock", new ModelLondonUnderground1995(false), "mtr:textures/entity/london_underground_1996", 0xA1A5A7, "", "", 6F, false, "london_underground_1996", null);
		register(TrainType.R179, "R179_(New_York_City_Subway_car)", new ModelR179(), "mtr:textures/entity/r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 7.5F, false, "r179", new JonTrainSound.JonTrainSoundConfig("r179", 66, 1, false));
		register(TrainType.R179_MINI, "R179_(New_York_City_Subway_car)", new ModelR179Mini(), "mtr:textures/entity/r179", 0xD5D5D5, "", "mtr:textures/entity/r179", 2.5F, true, "r179", new JonTrainSound.JonTrainSoundConfig("r179", 66, 1, false));
		register(TrainType.R211, "R211_(New_York_City_Subway_car)", new ModelR211(false), "mtr:textures/entity/r211", 0xD5D5D5, "", "mtr:textures/entity/r179", 7.5F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.R211_MINI, "R211_(New_York_City_Subway_car)", new ModelR211Mini(false), "mtr:textures/entity/r211", 0xD5D5D5, "", "mtr:textures/entity/r179", 2.5F, true, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.R211T, "R211_(New_York_City_Subway_car)", new ModelR211(true), "mtr:textures/entity/r211", 0xD5D5D5, "mtr:textures/entity/r211", "mtr:textures/entity/r179", 7.5F, false, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.R211T_MINI, "R211_(New_York_City_Subway_car)", new ModelR211Mini(true), "mtr:textures/entity/r211", 0xD5D5D5, "mtr:textures/entity/r211", "mtr:textures/entity/r179", 2.5F, true, "s_train", new JonTrainSound.JonTrainSoundConfig("r179", 42, 1, false));
		register(TrainType.CLASS_377_SOUTHERN, "British_Rail_Class_377", new ModelClass377(), "mtr:textures/entity/class_377_southern", 0x5AB565, "mtr:textures/entity/sp1900", "", 6, false, "class_377", new JonTrainSound.JonTrainSoundConfig("class_377", 51, 1, false));
		register(TrainType.CLASS_802_GWR, "British_Rail_Class_802", new ModelClass802(), "mtr:textures/entity/class_802_gwr", 0x021E15, "mtr:textures/entity/sp1900", "", 7.75F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.CLASS_802_GWR_MINI, "British_Rail_Class_802", new ModelClass802Mini(), "mtr:textures/entity/class_802_gwr", 0x021E15, "mtr:textures/entity/sp1900", "", 4.94F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.CLASS_802_TPE, "British_Rail_Class_802", new ModelClass802(), "mtr:textures/entity/class_802_tpe", 0x00A6E6, "mtr:textures/entity/sp1900", "", 7.75F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.CLASS_802_TPE_MINI, "British_Rail_Class_802", new ModelClass802Mini(), "mtr:textures/entity/class_802_tpe", 0x00A6E6, "mtr:textures/entity/sp1900", "", 4.94F, false, "class_802", new JonTrainSound.JonTrainSoundConfig("class_802", 120, 1, false));
		register(TrainType.MPL_85, "MPL_85", new ModelMPL85(), "mtr:textures/entity/mpl_85", 0xEF7011, "", "", 6.75F, false, "mpl_85", new JonTrainSound.JonTrainSoundConfig("mpl_85", 48, 1, false));
		register(TrainType.BR_423, "DBAG_Class_423", new ModelBR423(), "mtr:textures/entity/br_423", 0xE3010F, "mtr:textures/entity/sp1900", "", 7, true, "br_423", new JonTrainSound.JonTrainSoundConfig("br_423", 72, 0.5F, false));
		register(TrainType.MINECART, "Minecart", null, "textures/entity/minecart", 0x666666, -0.5F, 0);
		register(TrainType.OAK_BOAT, "Boat", null, "textures/entity/boat/oak", 0x8F7748, -1.5F, 0);
		register(TrainType.SPRUCE_BOAT, "Boat", null, "textures/entity/boat/spruce", 0x8F7748, -1.5F, 0);
		register(TrainType.BIRCH_BOAT, "Boat", null, "textures/entity/boat/birch", 0x8F7748, -1.5F, 0);
		register(TrainType.JUNGLE_BOAT, "Boat", null, "textures/entity/boat/jungle", 0x8F7748, -1.5F, 0);
		register(TrainType.ACACIA_BOAT, "Boat", null, "textures/entity/boat/acacia", 0x8F7748, -1.5F, 0);
		register(TrainType.DARK_OAK_BOAT, "Boat", null, "textures/entity/boat/dark_oak", 0x8F7748, -1.5F, 0);
		register(TrainType.NGONG_PING_360_CRYSTAL, "Ngong_Ping_360", new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_crystal", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_CRYSTAL_RHT, "Ngong_Ping_360", new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_crystal", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_CRYSTAL_PLUS, "Ngong_Ping_360", new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_crystal_plus", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_CRYSTAL_PLUS_RHT, "Ngong_Ping_360", new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_crystal_plus", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_NORMAL_RED, "Ngong_Ping_360", new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_red", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_NORMAL_RED_RHT, "Ngong_Ping_360", new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_red", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_NORMAL_ORANGE, "Ngong_Ping_360", new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_orange", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_NORMAL_ORANGE_RHT, "Ngong_Ping_360", new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_orange", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_NORMAL_LIGHT_BLUE, "Ngong_Ping_360", new ModelNgongPing360(false), "mtr:textures/entity/ngong_ping_360_normal_light_blue", 0x062540, 0, 0);
		register(TrainType.NGONG_PING_360_NORMAL_LIGHT_BLUE_RHT, "Ngong_Ping_360", new ModelNgongPing360(true), "mtr:textures/entity/ngong_ping_360_normal_light_blue", 0x062540, 0, 0);
		register(TrainType.A320, "Airbus_A320_family", new ModelA320(), "mtr:textures/entity/a320", 0xCCCCCC, 2.6F, 3);
		register(TrainType.FLYING_MINECART, "Minecart", null, "textures/entity/minecart", 0x666666, -0.5F, 0);
	}

	public static TrainProperties getTrainProperties(String key) {
		final String keyLower = key.toLowerCase(Locale.ENGLISH);
		return REGISTRY.getOrDefault(keyLower, TrainProperties.getBlankProperties());
	}

	public static TrainProperties getTrainProperties(TransportMode transportMode, int index) {
		return index >= 0 && index < KEY_ORDERS.get(transportMode).size() ? REGISTRY.get(KEY_ORDERS.get(transportMode).get(index)) : TrainProperties.getBlankProperties();
	}

	public static String getTrainId(TransportMode transportMode, int index) {
		return KEY_ORDERS.get(transportMode).get(index >= 0 && index < KEY_ORDERS.get(transportMode).size() ? index : 0);
	}

	public static void forEach(TransportMode transportMode, BiConsumer<String, TrainProperties> biConsumer) {
		KEY_ORDERS.get(transportMode).forEach(key -> biConsumer.accept(key, REGISTRY.get(key)));
	}
}
