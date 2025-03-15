package org.mtr.font;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.util.math.MatrixStack;

public final class FontGroups {

	private static FontGroup mtr;
	private static FontGroup kcr;
	private static FontGroup calligraphy;

	public static void reload() {
		final FontProvider notoSansProvider = new FontProvider("noto-sans-semibold.ttf");
		mtr = new FontGroup(ObjectImmutableList.of(notoSansProvider, new FontProvider("noto-serif-tc-semibold.ttf"), new FontProvider("noto-serif-sc-semibold.ttf")));
		kcr = new FontGroup(ObjectImmutableList.of(notoSansProvider, new FontProvider("noto-sans-tc-semibold.ttf"), new FontProvider("noto-sans-sc-semibold.ttf")));
		calligraphy = new FontGroup(ObjectImmutableList.of(new FontProvider("masa-font-bold.ttf")));
	}

	public static void renderMTR(MatrixStack matrixStack, String text, int color, int light) {
		if (mtr != null) {
			mtr.render(matrixStack, text, color, light);
		}
	}

	public static void renderKCR(MatrixStack matrixStack, String text, int color, int light) {
		if (kcr != null) {
			kcr.render(matrixStack, text, color, light);
		}
	}

	public static void renderCalligraphy(MatrixStack matrixStack, String text, int color, int light) {
		if (calligraphy != null) {
			calligraphy.render(matrixStack, text, color, light);
		}
	}
}
