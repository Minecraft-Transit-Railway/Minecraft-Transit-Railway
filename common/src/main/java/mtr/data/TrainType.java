package mtr.data;

public enum TrainType {

	SP1900(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	SP1900_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 20, 2, 0),
	SP1900_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 12, 2, 0),
	C1141A(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	C1141A_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 20, 2, 0),
	C1141A_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 12, 2, 0),
	M_TRAIN(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	M_TRAIN_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 19, 2, 0),
	M_TRAIN_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 9, 2, 0),
	MLR(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	MLR_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 20, 2, 0),
	MLR_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 12, 2, 0),
	E44(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	E44_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 12, 2, 0),
	DRL(TransportMode.TRAIN,  WorldRegion.UK, 24, 2, 0),
	K_TRAIN(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	K_TRAIN_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 19, 2, 0),
	K_TRAIN_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 9, 2, 0),
	K_TRAIN_TCL(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	K_TRAIN_TCL_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 19, 2, 0),
	K_TRAIN_TCL_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 9, 2, 0),
	K_TRAIN_AEL(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	K_TRAIN_AEL_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 19, 2, 0),
	K_TRAIN_AEL_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 9, 2, 0),
	C_TRAIN(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	C_TRAIN_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 19, 2, 0),
	C_TRAIN_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 9, 2, 0),
	A_TRAIN_TCL(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	A_TRAIN_TCL_SMALL(TransportMode.TRAIN,  WorldRegion.ASIA, 19, 2, 0),
	A_TRAIN_TCL_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 9, 2, 0),
	A_TRAIN_AEL(TransportMode.TRAIN,  WorldRegion.ASIA, 24, 2, 0),
	A_TRAIN_AEL_MINI(TransportMode.TRAIN,  WorldRegion.ASIA, 14, 2, 0),
	LIGHT_RAIL_1(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_1R(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_2(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_3(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_4(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_5(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_1R_OLD(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_4_OLD(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_5_OLD(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_1_ORANGE(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_1R_ORANGE(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_2_ORANGE(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_3_ORANGE(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_4_ORANGE(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LIGHT_RAIL_5_ORANGE(TransportMode.TRAIN,  WorldRegion.ASIA, 22, 2, 0),
	LONDON_UNDERGROUND_D78(TransportMode.TRAIN,  WorldRegion.UK, 18, 2, 0),
	LONDON_UNDERGROUND_D78_MINI(TransportMode.TRAIN,  WorldRegion.UK, 10, 2, 0),
	LONDON_UNDERGROUND_1995(TransportMode.TRAIN,  WorldRegion.UK, 19, 2, 0.5F),
	LONDON_UNDERGROUND_1996(TransportMode.TRAIN,  WorldRegion.UK, 19, 2, 0.5F),
	R179(TransportMode.TRAIN,  WorldRegion.USA, 19, 2, 0),
	R179_MINI(TransportMode.TRAIN,  WorldRegion.USA, 9, 2, 0),
	MINECART(TransportMode.TRAIN, null, 1, 1, 0),
	OAK_BOAT(TransportMode.BOAT, null,1, 1, 0),
	SPRUCE_BOAT(TransportMode.BOAT, null,1, 1, 0),
	BIRCH_BOAT(TransportMode.BOAT, null,1, 1, 0),
	JUNGLE_BOAT(TransportMode.BOAT, null,1, 1, 0),
	ACACIA_BOAT(TransportMode.BOAT, null,1, 1, 0),
	DARK_OAK_BOAT(TransportMode.BOAT, null, 1, 1, 0),
	BASE_2_2(TransportMode.TRAIN,  null, 2, 2, 0),
	BASE_3_2(TransportMode.TRAIN,  null, 3, 2, 0),
	BASE_4_2(TransportMode.TRAIN,  null, 4, 2, 0),
	BASE_5_2(TransportMode.TRAIN,  null, 5, 2, 0),
	BASE_6_2(TransportMode.TRAIN,  null, 6, 2, 0),
	BASE_7_2(TransportMode.TRAIN,  null, 7, 2, 0),
	BASE_8_2(TransportMode.TRAIN,  null, 8, 2, 0),
	BASE_9_2(TransportMode.TRAIN,  null, 9, 2, 0),
	BASE_10_2(TransportMode.TRAIN,  null, 10, 2, 0),
	BASE_11_2(TransportMode.TRAIN,  null, 11, 2, 0),
	BASE_12_2(TransportMode.TRAIN,  null, 12, 2, 0),
	BASE_13_2(TransportMode.TRAIN,  null, 13, 2, 0),
	BASE_14_2(TransportMode.TRAIN,  null, 14, 2, 0),
	BASE_15_2(TransportMode.TRAIN,  null, 15, 2, 0),
	BASE_16_2(TransportMode.TRAIN,  null, 16, 2, 0),
	BASE_17_2(TransportMode.TRAIN,  null, 17, 2, 0),
	BASE_18_2(TransportMode.TRAIN,  null, 18, 2, 0),
	BASE_19_2(TransportMode.TRAIN,  null, 19, 2, 0),
	BASE_20_2(TransportMode.TRAIN,  null, 20, 2, 0),
	BASE_21_2(TransportMode.TRAIN,  null, 21, 2, 0),
	BASE_22_2(TransportMode.TRAIN,  null, 22, 2, 0),
	BASE_23_2(TransportMode.TRAIN,  null, 23, 2, 0),
	BASE_24_2(TransportMode.TRAIN,  null, 24, 2, 0),
	BASE_25_2(TransportMode.TRAIN,  null, 25, 2, 0),
	BASE_26_2(TransportMode.TRAIN,  null, 26, 2, 0),
	BASE_27_2(TransportMode.TRAIN,  null, 27, 2, 0),
	BASE_28_2(TransportMode.TRAIN,  null, 28, 2, 0),
	BASE_29_2(TransportMode.TRAIN,  null, 29, 2, 0),
	BASE_30_2(TransportMode.TRAIN,  null, 30, 2, 0);

	public final TransportMode transportMode;
	public final WorldRegion worldRegion;
	public final int width;
	public final float offset;
	private final int length;

	TrainType(TransportMode transportMode, WorldRegion worldRegion, int length, int width, float offset) {
		this.transportMode = transportMode;
		this.worldRegion = worldRegion;
		this.length = length;
		this.width = width;
		this.offset = offset;
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
