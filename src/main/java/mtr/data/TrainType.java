package mtr.data;

public enum TrainType {

	SP1900(24, 2),
	SP1900_MINI(12, 2),
	C1141A(24, 2),
	C1141A_MINI(12, 2),
	M_TRAIN(24, 2),
	M_TRAIN_MINI(9, 2),
	MLR(24, 2),
	MLR_MINI(12, 2),
	E44(24, 2),
	E44_MINI(12, 2),
	K_TRAIN(24, 2),
	K_TRAIN_MINI(9, 2),
	K_TRAIN_TCL(24, 2),
	K_TRAIN_TCL_MINI(9, 2),
	K_TRAIN_AEL(24, 2),
	K_TRAIN_AEL_MINI(9, 2),
	A_TRAIN_TCL(24, 2),
	A_TRAIN_TCL_MINI(9, 2),
	A_TRAIN_AEL(24, 2),
	A_TRAIN_AEL_MINI(14, 2),
	LIGHT_RAIL_1(22, 2),
	LIGHT_RAIL_1R(22, 2),
	LIGHT_RAIL_2(22, 2),
	LIGHT_RAIL_3(22, 2),
	LIGHT_RAIL_4(22, 2),
	LIGHT_RAIL_5(22, 2),
	R179(19, 2),
	MINECART(1, 1),
	BASE_2_2(2, 2),
	BASE_3_2(3, 2),
	BASE_4_2(4, 2),
	BASE_5_2(5, 2),
	BASE_6_2(6, 2),
	BASE_7_2(7, 2),
	BASE_8_2(8, 2),
	BASE_9_2(9, 2),
	BASE_10_2(10, 2),
	BASE_11_2(11, 2),
	BASE_12_2(12, 2),
	BASE_13_2(13, 2),
	BASE_14_2(14, 2),
	BASE_15_2(15, 2),
	BASE_16_2(16, 2),
	BASE_17_2(17, 2),
	BASE_18_2(18, 2),
	BASE_19_2(19, 2),
	BASE_20_2(20, 2),
	BASE_21_2(21, 2),
	BASE_22_2(22, 2),
	BASE_23_2(23, 2),
	BASE_24_2(24, 2),
	BASE_25_2(25, 2),
	BASE_26_2(26, 2),
	BASE_27_2(27, 2),
	BASE_28_2(28, 2),
	BASE_29_2(29, 2),
	BASE_30_2(30, 2);

	public final int width;
	private final int length;

	TrainType(int length, int width) {
		this.length = length;
		this.width = width;
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
