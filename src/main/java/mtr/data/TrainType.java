package mtr.data;

public enum TrainType {

	SP1900(24, 2, 0, 0),
	SP1900_SMALL(20, 2, 0, 0),
	SP1900_MINI(12, 2, 0, 0),
	C1141A(24, 2, 0, 0),
	C1141A_SMALL(20, 2, 0, 0),
	C1141A_MINI(12, 2, 0, 0),
	M_TRAIN(24, 2, 0, 0),
	M_TRAIN_SMALL(19, 2, 0, 0),
	M_TRAIN_MINI(9, 2, 0, 0),
	MLR(24, 2, 0, 0),
	MLR_SMALL(20, 2, 0, 0),
	MLR_MINI(12, 2, 0, 0),
	E44(24, 2, 0,0),
	E44_MINI(12, 2, 0, 0),
	K_TRAIN(24, 2, 0, 0),
	K_TRAIN_SMALL(19, 2, 0, 0),
	K_TRAIN_MINI(9, 2, 0, 0),
	K_TRAIN_TCL(24, 2, 0, 0),
	K_TRAIN_TCL_SMALL(19, 2, 0, 0),
	K_TRAIN_TCL_MINI(9, 2, 0, 0),
	K_TRAIN_AEL(24, 2, 0, 0),
	K_TRAIN_AEL_SMALL(19, 2, 0, 0),
	K_TRAIN_AEL_MINI(9, 2, 0, 0),
	C_TRAIN(24, 2, 0, 0),
	C_TRAIN_SMALL(19, 2, 0, 0),
	C_TRAIN_MINI(9, 2, 0, 0),
	A_TRAIN_TCL(24, 2, 0, 0),
	A_TRAIN_TCL_SMALL(19, 2, 0, 0),
	A_TRAIN_TCL_MINI(9, 2, 0, 0),
	A_TRAIN_AEL(24, 2, 0, 0),
	A_TRAIN_AEL_MINI(14, 2, 0, 0),
	LIGHT_RAIL_1(22, 2, 0, 0),
	LIGHT_RAIL_1R(22, 2, 0, 0),
	LIGHT_RAIL_2(22, 2, 0, 0),
	LIGHT_RAIL_3(22, 2, 0, 0),
	LIGHT_RAIL_4(22, 2, 0, 0),
	LIGHT_RAIL_5(22, 2, 0, 0),
	LIGHT_RAIL_1R_OLD(22, 2, 0, 0),
	LIGHT_RAIL_4_OLD(22, 2, 0, 0),
	LIGHT_RAIL_5_OLD(22, 2, 0, 0),
	LIGHT_RAIL_1_ORANGE(22, 2, 0, 0),
	LIGHT_RAIL_1R_ORANGE(22, 2, 0, 0),
	LIGHT_RAIL_2_ORANGE(22, 2, 0, 0),
	LIGHT_RAIL_3_ORANGE(22, 2, 0, 0),
	LIGHT_RAIL_4_ORANGE(22, 2, 0, 0),
	LIGHT_RAIL_5_ORANGE(22, 2, 0, 0),
	LONDON_UNDERGROUND_1995(19, 2, 0.5F, 0),
	R179(19, 2, 0, 0),
	R179_TB(19, 2, 0, 0.2f),
	R179_MINI(9, 2, 0, 0),
	MINECART(1, 1, 0, 0),
	BASE_2_2(2, 2, 0, 0),
	BASE_3_2(3, 2, 0, 0),
	BASE_4_2(4, 2, 0, 0),
	BASE_5_2(5, 2, 0, 0),
	BASE_6_2(6, 2, 0, 0),
	BASE_7_2(7, 2, 0, 0),
	BASE_8_2(8, 2, 0, 0),
	BASE_9_2(9, 2, 0, 0),
	BASE_10_2(10, 2, 0, 0),
	BASE_11_2(11, 2, 0, 0),
	BASE_12_2(12, 2, 0, 0),
	BASE_13_2(13, 2, 0, 0),
	BASE_14_2(14, 2, 0, 0),
	BASE_15_2(15, 2, 0, 0),
	BASE_16_2(16, 2, 0, 0),
	BASE_17_2(17, 2, 0, 0),
	BASE_18_2(18, 2, 0, 0),
	BASE_19_2(19, 2, 0, 0),
	BASE_20_2(20, 2, 0, 0),
	BASE_21_2(21, 2, 0, 0),
	BASE_22_2(22, 2, 0, 0),
	BASE_23_2(23, 2, 0, 0),
	BASE_24_2(24, 2, 0, 0),
	BASE_25_2(25, 2, 0, 0),
	BASE_26_2(26, 2, 0, 0),
	BASE_27_2(27, 2, 0, 0),
	BASE_28_2(28, 2, 0, 0),
	BASE_29_2(29, 2, 0, 0),
	BASE_30_2(30, 2, 0, 0);
  
	public final int width;
	public final float offset;
	private final int length;
	public float trainBarrierLength;
	
	TrainType(int length, int width, float offset, float trainBarrierLength) {
		this.length = length;
		this.width = width;
		this.trainBarrierLength = trainBarrierLength;
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
