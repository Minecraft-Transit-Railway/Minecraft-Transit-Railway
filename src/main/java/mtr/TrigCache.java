package mtr;

import net.minecraft.util.Mth;

public class TrigCache {

	private static final float INCREMENT = 0.01F;
	private static final double[] ASIN_TABLE = new double[(int) (2 / INCREMENT) + 1];

	public static double asin(double value) {
		return ASIN_TABLE[Mth.clamp((int) Math.round(value / INCREMENT) + 100, 0, ASIN_TABLE.length - 1)];
	}

	static {
		float j = -1;
		for (int i = 0; i < ASIN_TABLE.length; i++) {
			ASIN_TABLE[i] = Math.asin(j);
			j += INCREMENT;
		}
	}
}
