package org.mtr.client;

public interface ColorHelper {

	static void unpackColor(int color, ColorUnpacker colorUnpacker) {
		final int a = (color >> 24) & 0xFF;
		final int r = (color >> 16) & 0xFF;
		final int g = (color >> 8) & 0xFF;
		final int b = color & 0xFF;
		if (a != 0) {
			colorUnpacker.unpack(a, r, g, b);
		}
	}

	@FunctionalInterface
	interface ColorUnpacker {
		void unpack(int a, int r, int g, int b);
	}
}
