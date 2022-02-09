package mtr.data;

public enum TrainType {

	SP1900(TransportMode.TRAIN,  0, 24, 2, 0),
	SP1900_SMALL(TransportMode.TRAIN,  0, 20, 2, 0),
	SP1900_MINI(TransportMode.TRAIN,  0, 12, 2, 0),
	C1141A(TransportMode.TRAIN,  0, 24, 2, 0),
	C1141A_SMALL(TransportMode.TRAIN,  0, 20, 2, 0),
	C1141A_MINI(TransportMode.TRAIN,  0, 12, 2, 0),
	M_TRAIN(TransportMode.TRAIN,  0, 24, 2, 0),
	M_TRAIN_SMALL(TransportMode.TRAIN,  0, 19, 2, 0),
	M_TRAIN_MINI(TransportMode.TRAIN,  0, 9, 2, 0),
	MLR(TransportMode.TRAIN,  0, 24, 2, 0),
	MLR_SMALL(TransportMode.TRAIN,  0, 20, 2, 0),
	MLR_MINI(TransportMode.TRAIN,  0, 12, 2, 0),
	E44(TransportMode.TRAIN,  0, 24, 2, 0),
	E44_MINI(TransportMode.TRAIN,  0, 12, 2, 0),
	DRL(TransportMode.TRAIN,  0, 24, 2, 0),
	K_TRAIN(TransportMode.TRAIN,  0, 24, 2, 0),
	K_TRAIN_SMALL(TransportMode.TRAIN,  0, 19, 2, 0),
	K_TRAIN_MINI(TransportMode.TRAIN,  0, 9, 2, 0),
	K_TRAIN_TCL(TransportMode.TRAIN,  0, 24, 2, 0),
	K_TRAIN_TCL_SMALL(TransportMode.TRAIN,  0, 19, 2, 0),
	K_TRAIN_TCL_MINI(TransportMode.TRAIN,  0, 9, 2, 0),
	K_TRAIN_AEL(TransportMode.TRAIN,  0, 24, 2, 0),
	K_TRAIN_AEL_SMALL(TransportMode.TRAIN,  0, 19, 2, 0),
	K_TRAIN_AEL_MINI(TransportMode.TRAIN,  0, 9, 2, 0),
	C_TRAIN(TransportMode.TRAIN,  0, 24, 2, 0),
	C_TRAIN_SMALL(TransportMode.TRAIN,  0, 19, 2, 0),
	C_TRAIN_MINI(TransportMode.TRAIN,  0, 9, 2, 0),
	A_TRAIN_TCL(TransportMode.TRAIN,  0, 24, 2, 0),
	A_TRAIN_TCL_SMALL(TransportMode.TRAIN,  0, 19, 2, 0),
	A_TRAIN_TCL_MINI(TransportMode.TRAIN,  0, 9, 2, 0),
	A_TRAIN_AEL(TransportMode.TRAIN,  0, 24, 2, 0),
	A_TRAIN_AEL_MINI(TransportMode.TRAIN,  0, 14, 2, 0),
	LIGHT_RAIL_1(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_1R(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_2(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_3(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_4(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_5(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_1R_OLD(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_4_OLD(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_5_OLD(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_1_ORANGE(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_1R_ORANGE(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_2_ORANGE(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_3_ORANGE(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_4_ORANGE(TransportMode.TRAIN,  0, 22, 2, 0),
	LIGHT_RAIL_5_ORANGE(TransportMode.TRAIN,  0, 22, 2, 0),
	LONDON_UNDERGROUND_D78(TransportMode.TRAIN,  0, 18, 2, 0),
	LONDON_UNDERGROUND_D78_MINI(TransportMode.TRAIN,  0, 10, 2, 0),
	LONDON_UNDERGROUND_1995(TransportMode.TRAIN,  0, 19, 2, 0.5F),
	LONDON_UNDERGROUND_1996(TransportMode.TRAIN,  0, 19, 2, 0.5F),
	R179(TransportMode.TRAIN,  0, 19, 2, 0),
	R179_TB(TransportMode.TRAIN,  0.2f, 19, 2, 0),
	R179_MINI(TransportMode.TRAIN,  0, 9, 2, 0),
	MINECART(TransportMode.TRAIN,  0, 1, 1, 0),
	OAK_BOAT(TransportMode.BOAT,  0, 1, 1, 0),
	SPRUCE_BOAT(TransportMode.BOAT,  0, 1, 1, 0),
	BIRCH_BOAT(TransportMode.BOAT,  0, 1, 1, 0),
	JUNGLE_BOAT(TransportMode.BOAT,  0, 1, 1, 0),
	ACACIA_BOAT(TransportMode.BOAT,  0, 1, 1, 0),
	DARK_OAK_BOAT(TransportMode.BOAT,  0, 1, 1, 0),
	BASE_2_2(TransportMode.TRAIN,  0, 2, 2, 0),
	BASE_3_2(TransportMode.TRAIN,  0, 3, 2, 0),
	BASE_4_2(TransportMode.TRAIN,  0, 4, 2, 0),
	BASE_5_2(TransportMode.TRAIN,  0, 5, 2, 0),
	BASE_6_2(TransportMode.TRAIN,  0, 6, 2, 0),
	BASE_7_2(TransportMode.TRAIN,  0, 7, 2, 0),
	BASE_8_2(TransportMode.TRAIN,  0, 8, 2, 0),
	BASE_9_2(TransportMode.TRAIN,  0, 9, 2, 0),
	BASE_10_2(TransportMode.TRAIN,  0, 10, 2, 0),
	BASE_11_2(TransportMode.TRAIN,  0, 11, 2, 0),
	BASE_12_2(TransportMode.TRAIN,  0, 12, 2, 0),
	BASE_13_2(TransportMode.TRAIN,  0, 13, 2, 0),
	BASE_14_2(TransportMode.TRAIN,  0, 14, 2, 0),
	BASE_15_2(TransportMode.TRAIN,  0, 15, 2, 0),
	BASE_16_2(TransportMode.TRAIN,  0, 16, 2, 0),
	BASE_17_2(TransportMode.TRAIN,  0, 17, 2, 0),
	BASE_18_2(TransportMode.TRAIN,  0, 18, 2, 0),
	BASE_19_2(TransportMode.TRAIN,  0, 19, 2, 0),
	BASE_20_2(TransportMode.TRAIN,  0, 20, 2, 0),
	BASE_21_2(TransportMode.TRAIN,  0, 21, 2, 0),
	BASE_22_2(TransportMode.TRAIN,  0, 22, 2, 0),
	BASE_23_2(TransportMode.TRAIN,  0, 23, 2, 0),
	BASE_24_2(TransportMode.TRAIN,  0, 24, 2, 0),
	BASE_25_2(TransportMode.TRAIN,  0, 25, 2, 0),
	BASE_26_2(TransportMode.TRAIN,  0, 26, 2, 0),
	BASE_27_2(TransportMode.TRAIN,  0, 27, 2, 0),
	BASE_28_2(TransportMode.TRAIN,  0, 28, 2, 0),
	BASE_29_2(TransportMode.TRAIN,  0, 29, 2, 0),
	BASE_30_2(TransportMode.TRAIN,  0, 30, 2, 0);

	public final TransportMode transportMode;
	public final float trainBarrierLength;
	public final int width;
	public final float offset;
	private final int length;

	TrainType(TransportMode transportMode, float trainBarrierLength,  int length, int width, float offset) {
		this.transportMode = transportMode;
		this.trainBarrierLength = trainBarrierLength;
		this.length = length;
		this.width = width;
		this.offset = offset;
	}

	public float getSpacing() {
		return length + 1 + trainBarrierLength;
	}

	public static TrainType getOrDefault(String name) {
		try {
			return TrainType.valueOf(name.toUpperCase());
		} catch (Exception ignored) {
			return SP1900;
		}
	}
}
