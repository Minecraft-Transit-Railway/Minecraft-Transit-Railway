package mtr.data;

import java.util.Locale;

public enum TrainType {

	SP1900("train_24_2"),
	SP1900_SMALL("train_20_2"),
	SP1900_MINI("train_12_2"),
	C1141A("train_24_2"),
	C1141A_SMALL("train_20_2"),
	C1141A_MINI("train_12_2"),
	M_TRAIN("train_24_2"),
	M_TRAIN_SMALL("train_19_2"),
	M_TRAIN_MINI("train_9_2"),
	CM_STOCK("train_24_2"),
	CM_STOCK_SMALL("train_19_2"),
	CM_STOCK_MINI("train_9_2"),
	MLR("train_24_2"),
	MLR_SMALL("train_20_2"),
	MLR_MINI("train_12_2"),
	MLR_CHRISTMAS("train_24_2"),
	MLR_CHRISTMAS_SMALL("train_20_2"),
	MLR_CHRISTMAS_MINI("train_12_2"),
	E44("train_24_2"),
	E44_MINI("train_12_2"),
	R_TRAIN("train_24_2"),
	R_TRAIN_SMALL("train_19_2"),
	R_TRAIN_MINI("train_9_2"),
	DRL("train_24_2"),
	K_TRAIN("train_24_2"),
	K_TRAIN_SMALL("train_19_2"),
	K_TRAIN_MINI("train_9_2"),
	K_TRAIN_TCL("train_24_2"),
	K_TRAIN_TCL_SMALL("train_19_2"),
	K_TRAIN_TCL_MINI("train_9_2"),
	K_TRAIN_AEL("train_24_2"),
	K_TRAIN_AEL_SMALL("train_19_2"),
	K_TRAIN_AEL_MINI("train_9_2"),
	C_TRAIN("train_24_2"),
	C_TRAIN_SMALL("train_19_2"),
	C_TRAIN_MINI("train_9_2"),
	S_TRAIN("train_24_2"),
	S_TRAIN_SMALL("train_19_2"),
	S_TRAIN_MINI("train_9_2"),
	A_TRAIN_TCL("train_24_2"),
	A_TRAIN_TCL_SMALL("train_19_2"),
	A_TRAIN_TCL_MINI("train_9_2"),
	A_TRAIN_AEL("train_24_2"),
	A_TRAIN_AEL_MINI("train_14_2"),
	LIGHT_RAIL_1("train_22_2"),
	LIGHT_RAIL_1_RHT("train_22_2"),
	LIGHT_RAIL_1R("train_22_2"),
	LIGHT_RAIL_1R_RHT("train_22_2"),
	LIGHT_RAIL_2("train_22_2"),
	LIGHT_RAIL_2R("train_22_2"),
	LIGHT_RAIL_2_RHT("train_22_2"),
	LIGHT_RAIL_2R_RHT("train_22_2"),
	LIGHT_RAIL_3("train_22_2"),
	LIGHT_RAIL_3_RHT("train_22_2"),
	LIGHT_RAIL_3R("train_22_2"),
	LIGHT_RAIL_3R_RHT("train_22_2"),
	LIGHT_RAIL_4("train_22_2"),
	LIGHT_RAIL_4_RHT("train_22_2"),
	LIGHT_RAIL_5("train_22_2"),
	LIGHT_RAIL_5_RHT("train_22_2"),
	LIGHT_RAIL_1R_OLD("train_22_2"),
	LIGHT_RAIL_1R_OLD_RHT("train_22_2"),
	LIGHT_RAIL_4_OLD("train_22_2"),
	LIGHT_RAIL_4_OLD_RHT("train_22_2"),
	LIGHT_RAIL_5_OLD("train_22_2"),
	LIGHT_RAIL_5_OLD_RHT("train_22_2"),
	LIGHT_RAIL_1_ORANGE("train_22_2"),
	LIGHT_RAIL_1_ORANGE_RHT("train_22_2"),
	LIGHT_RAIL_1R_ORANGE("train_22_2"),
	LIGHT_RAIL_1R_ORANGE_RHT("train_22_2"),
	LIGHT_RAIL_2_ORANGE("train_22_2"),
	LIGHT_RAIL_2_ORANGE_RHT("train_22_2"),
	LIGHT_RAIL_3_ORANGE("train_22_2"),
	LIGHT_RAIL_3_ORANGE_RHT("train_22_2"),
	LIGHT_RAIL_4_ORANGE("train_22_2"),
	LIGHT_RAIL_4_ORANGE_RHT("train_22_2"),
	LIGHT_RAIL_5_ORANGE("train_22_2"),
	LIGHT_RAIL_5_ORANGE_RHT("train_22_2"),
	LONDON_UNDERGROUND_D78("train_18_2"),
	LONDON_UNDERGROUND_D78_MINI("train_10_2"),
	LONDON_UNDERGROUND_1995("train_19_2"),
	LONDON_UNDERGROUND_1996("train_19_2"),
	R179("train_19_2"),
	R179_MINI("train_9_2"),
	R211("train_19_2"),
	R211_MINI("train_9_2"),
	R211T("train_19_2"),
	R211T_MINI("train_9_2"),
	CLASS_377_SOUTHERN("train_16_2"),
	CLASS_802_GWR("train_24_2"),
	CLASS_802_GWR_MINI("train_18_2"),
	CLASS_802_TPE("train_24_2"),
	CLASS_802_TPE_MINI("train_18_2"),
	MPL_85("train_21_2"),
	BR_423("train_15_2"),
	MINECART("train_1_1"),
	OAK_BOAT("boat_1_1"),
	SPRUCE_BOAT("boat_1_1"),
	BIRCH_BOAT("boat_1_1"),
	JUNGLE_BOAT("boat_1_1"),
	ACACIA_BOAT("boat_1_1"),
	DARK_OAK_BOAT("boat_1_1"),
	NGONG_PING_360_CRYSTAL("cable_car_1_1"),
	NGONG_PING_360_CRYSTAL_RHT("cable_car_1_1"),
	NGONG_PING_360_CRYSTAL_PLUS("cable_car_1_1"),
	NGONG_PING_360_CRYSTAL_PLUS_RHT("cable_car_1_1"),
	NGONG_PING_360_NORMAL_RED("cable_car_1_1"),
	NGONG_PING_360_NORMAL_RED_RHT("cable_car_1_1"),
	NGONG_PING_360_NORMAL_ORANGE("cable_car_1_1"),
	NGONG_PING_360_NORMAL_ORANGE_RHT("cable_car_1_1"),
	NGONG_PING_360_NORMAL_LIGHT_BLUE("cable_car_1_1"),
	NGONG_PING_360_NORMAL_LIGHT_BLUE_RHT("cable_car_1_1"),
	A320("airplane_30_3"),
	FLYING_MINECART("airplane_1_1");

	public final String baseTrainType;

	TrainType(String baseTrainType) {
		this.baseTrainType = baseTrainType;
	}

	public static TransportMode getTransportMode(String trainType) {
		final TransportMode[] returnTransportMode = {TransportMode.TRAIN};
		splitTrainType(trainType, ((transportMode, length, width) -> returnTransportMode[0] = transportMode));
		return returnTransportMode[0];
	}

	public static int getSpacing(String trainType) {
		final int[] returnLength = {1};
		splitTrainType(trainType, ((transportMode, length, width) -> returnLength[0] = length));
		return returnLength[0] + 1;
	}

	public static int getWidth(String trainType) {
		final int[] returnWidth = {1};
		splitTrainType(trainType, ((transportMode, length, width) -> returnWidth[0] = width));
		return returnWidth[0];
	}

	private static void splitTrainType(String trainType, TrainTypeCallback trainTypeCallback) {
		for (final TransportMode transportMode : TransportMode.values()) {
			final String checkString = transportMode.toString().toLowerCase(Locale.ENGLISH) + "_";

			if (trainType.toLowerCase(Locale.ENGLISH).startsWith(checkString)) {
				final String[] remainingSplit = trainType.substring(checkString.length()).split("_");
				int length = 1;
				int width = 1;

				try {
					length = Integer.parseInt(remainingSplit[0]);
					width = Integer.parseInt(remainingSplit[1]);
				} catch (Exception e) {
					e.printStackTrace();
				}

				trainTypeCallback.trainTypeCallback(transportMode, Math.max(length, 1), Math.max(width, 1));
				return;
			}
		}

		for (final TrainType defaultTrainType : values()) {
			if (trainType.equalsIgnoreCase(defaultTrainType.toString())) {
				splitTrainType(defaultTrainType.baseTrainType, trainTypeCallback);
				return;
			}
		}
	}

	@FunctionalInterface
	private interface TrainTypeCallback {
		void trainTypeCallback(TransportMode transportMode, int length, int width);
	}
}
