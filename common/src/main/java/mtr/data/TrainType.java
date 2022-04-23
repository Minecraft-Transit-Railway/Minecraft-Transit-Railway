package mtr.data;

public enum TrainType {

	SP1900(TransportMode.TRAIN, 24, 2, true),
	SP1900_SMALL(TransportMode.TRAIN, 20, 2, true),
	SP1900_MINI(TransportMode.TRAIN, 12, 2, true),
	C1141A(TransportMode.TRAIN, 24, 2, true),
	C1141A_SMALL(TransportMode.TRAIN, 20, 2, true),
	C1141A_MINI(TransportMode.TRAIN, 12, 2, true),
	M_TRAIN(TransportMode.TRAIN, 24, 2, true),
	M_TRAIN_SMALL(TransportMode.TRAIN, 19, 2, true),
	M_TRAIN_MINI(TransportMode.TRAIN, 9, 2, true),
	CM_STOCK(TransportMode.TRAIN, 24, 2, true),
	CM_STOCK_SMALL(TransportMode.TRAIN, 19, 2, true),
	CM_STOCK_MINI(TransportMode.TRAIN, 9, 2, true),
	MLR(TransportMode.TRAIN, 24, 2, true),
	MLR_SMALL(TransportMode.TRAIN, 20, 2, true),
	MLR_MINI(TransportMode.TRAIN, 12, 2, true),
	E44(TransportMode.TRAIN, 24, 2, true),
	E44_MINI(TransportMode.TRAIN, 12, 2, true),
	DRL(TransportMode.TRAIN, 24, 2, true),
	K_TRAIN(TransportMode.TRAIN, 24, 2, true),
	K_TRAIN_SMALL(TransportMode.TRAIN, 19, 2, true),
	K_TRAIN_MINI(TransportMode.TRAIN, 9, 2, true),
	K_TRAIN_TCL(TransportMode.TRAIN, 24, 2, true),
	K_TRAIN_TCL_SMALL(TransportMode.TRAIN, 19, 2, true),
	K_TRAIN_TCL_MINI(TransportMode.TRAIN, 9, 2, true),
	K_TRAIN_AEL(TransportMode.TRAIN, 24, 2, true),
	K_TRAIN_AEL_SMALL(TransportMode.TRAIN, 19, 2, true),
	K_TRAIN_AEL_MINI(TransportMode.TRAIN, 9, 2, true),
	C_TRAIN(TransportMode.TRAIN, 24, 2, true),
	C_TRAIN_SMALL(TransportMode.TRAIN, 19, 2, true),
	C_TRAIN_MINI(TransportMode.TRAIN, 9, 2, true),
	S_TRAIN(TransportMode.TRAIN, 24, 2, true),
	S_TRAIN_SMALL(TransportMode.TRAIN, 19, 2, true),
	S_TRAIN_MINI(TransportMode.TRAIN, 9, 2, true),
	A_TRAIN_TCL(TransportMode.TRAIN, 24, 2, true),
	A_TRAIN_TCL_SMALL(TransportMode.TRAIN, 19, 2, true),
	A_TRAIN_TCL_MINI(TransportMode.TRAIN, 9, 2, true),
	A_TRAIN_AEL(TransportMode.TRAIN, 24, 2, true),
	A_TRAIN_AEL_MINI(TransportMode.TRAIN, 14, 2, true),
	LIGHT_RAIL_1(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_1R(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_2(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_3(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_4(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_5(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_1R_OLD(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_4_OLD(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_5_OLD(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_1_ORANGE(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_1R_ORANGE(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_2_ORANGE(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_3_ORANGE(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_4_ORANGE(TransportMode.TRAIN, 22, 2, false),
	LIGHT_RAIL_5_ORANGE(TransportMode.TRAIN, 22, 2, false),
	LONDON_UNDERGROUND_D78(TransportMode.TRAIN, 18, 2, false),
	LONDON_UNDERGROUND_D78_MINI(TransportMode.TRAIN, 10, 2, false),
	LONDON_UNDERGROUND_1995(TransportMode.TRAIN, 19, 2, false, 0, 0.5F),
	LONDON_UNDERGROUND_1996(TransportMode.TRAIN, 19, 2, false, 0, 0.5F),
	R179(TransportMode.TRAIN, 19, 2, false),
	R179_MINI(TransportMode.TRAIN, 9, 2, false),
	R211(TransportMode.TRAIN, 19, 2, false),
	R211_MINI(TransportMode.TRAIN, 9, 2, false),
	R211T(TransportMode.TRAIN, 19, 2, true),
	R211T_MINI(TransportMode.TRAIN, 9, 2, true),
	CLASS_802_GWR(TransportMode.TRAIN, 24, 2, true),
	CLASS_802_GWR_MINI(TransportMode.TRAIN, 18, 2, true),
	CLASS_802_TPE(TransportMode.TRAIN, 24, 2, true),
	CLASS_802_TPE_MINI(TransportMode.TRAIN, 18, 2, true),
	MINECART(TransportMode.TRAIN, 1, 1, false, -0.5F, 0),
	OAK_BOAT(TransportMode.BOAT, 1, 1, false, -0.5F, 0),
	SPRUCE_BOAT(TransportMode.BOAT, 1, 1, false, -0.5F, 0),
	BIRCH_BOAT(TransportMode.BOAT, 1, 1, false, -0.5F, 0),
	JUNGLE_BOAT(TransportMode.BOAT, 1, 1, false, -0.5F, 0),
	ACACIA_BOAT(TransportMode.BOAT, 1, 1, false, -0.5F, 0),
	DARK_OAK_BOAT(TransportMode.BOAT, 1, 1, false, -0.5F, 0),
	BASE_2_2(TransportMode.TRAIN, 2, 2, true),
	BASE_3_2(TransportMode.TRAIN, 3, 2, true),
	BASE_4_2(TransportMode.TRAIN, 4, 2, true),
	BASE_5_2(TransportMode.TRAIN, 5, 2, true),
	BASE_6_2(TransportMode.TRAIN, 6, 2, true),
	BASE_7_2(TransportMode.TRAIN, 7, 2, true),
	BASE_8_2(TransportMode.TRAIN, 8, 2, true),
	BASE_9_2(TransportMode.TRAIN, 9, 2, true),
	BASE_10_2(TransportMode.TRAIN, 10, 2, true),
	BASE_11_2(TransportMode.TRAIN, 11, 2, true),
	BASE_12_2(TransportMode.TRAIN, 12, 2, true),
	BASE_13_2(TransportMode.TRAIN, 13, 2, true),
	BASE_14_2(TransportMode.TRAIN, 14, 2, true),
	BASE_15_2(TransportMode.TRAIN, 15, 2, true),
	BASE_16_2(TransportMode.TRAIN, 16, 2, true),
	BASE_17_2(TransportMode.TRAIN, 17, 2, true),
	BASE_18_2(TransportMode.TRAIN, 18, 2, true),
	BASE_19_2(TransportMode.TRAIN, 19, 2, true),
	BASE_20_2(TransportMode.TRAIN, 20, 2, true),
	BASE_21_2(TransportMode.TRAIN, 21, 2, true),
	BASE_22_2(TransportMode.TRAIN, 22, 2, true),
	BASE_23_2(TransportMode.TRAIN, 23, 2, true),
	BASE_24_2(TransportMode.TRAIN, 24, 2, true),
	BASE_25_2(TransportMode.TRAIN, 25, 2, true),
	BASE_26_2(TransportMode.TRAIN, 26, 2, true),
	BASE_27_2(TransportMode.TRAIN, 27, 2, true),
	BASE_28_2(TransportMode.TRAIN, 28, 2, true),
	BASE_29_2(TransportMode.TRAIN, 29, 2, true),
	BASE_30_2(TransportMode.TRAIN, 30, 2, true);

	public final TransportMode transportMode;
	public final int width;
	public final boolean hasGangwayConnection;
	public final float riderOffset;
	public final float modelZOffset;
	private final int length;

	TrainType(TransportMode transportMode, int length, int width, boolean hasGangwayConnection, float riderOffset, float modelZOffset) {
		this.transportMode = transportMode;
		this.length = length;
		this.width = width;
		this.hasGangwayConnection = hasGangwayConnection;
		this.riderOffset = riderOffset;
		this.modelZOffset = modelZOffset;
	}

	TrainType(TransportMode transportMode, int length, int width, boolean hasGangwayConnection) {
		this(transportMode, length, width, hasGangwayConnection, 0, 0);
	}

	public int getSpacing() {
		return length + 1;
	}

	public static TrainType getOrDefault(String name) {
		try {
			return TrainType.valueOf(name.toUpperCase());
		} catch (Exception ignored) {
			return SP1900;
		}
	}
}
