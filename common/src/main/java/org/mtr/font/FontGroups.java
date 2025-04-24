package org.mtr.font;

import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.util.Identifier;
import org.mtr.MTR;
import org.mtr.tool.Drawing;

import javax.annotation.Nullable;

public final class FontGroups {

	private static FontGroup minecraft;
	private static FontGroup mtr;
	private static FontGroup kcr;
	private static FontGroup calligraphy;

	public static void reload() {
		final FontProvider notoSansProvider = new FontProvider(Identifier.of(MTR.MOD_ID, "font/noto-sans-semibold.ttf"));
		minecraft = new FontGroup(ObjectImmutableList.of(new FontProvider(Identifier.of("font/default.json"))));
		mtr = new FontGroup(ObjectImmutableList.of(notoSansProvider, new FontProvider(Identifier.of(MTR.MOD_ID, "font/noto-serif-tc-semibold.ttf")), new FontProvider(Identifier.of(MTR.MOD_ID, "font/noto-serif-sc-semibold.ttf"))));
		kcr = new FontGroup(ObjectImmutableList.of(notoSansProvider, new FontProvider(Identifier.of(MTR.MOD_ID, "font/noto-sans-tc-semibold.ttf")), new FontProvider(Identifier.of(MTR.MOD_ID, "font/noto-sans-sc-semibold.ttf"))));
		calligraphy = new FontGroup(ObjectImmutableList.of(new FontProvider(Identifier.of(MTR.MOD_ID, "font/masa-font-bold.ttf"))));
	}

	public static FloatFloatImmutablePair renderMinecraft(@Nullable Drawing drawing, String text, FontRenderOptions fontRenderOptions) {
		if (minecraft == null) {
			return new FloatFloatImmutablePair(0, 0);
		} else {
			return minecraft.render(drawing, text, fontRenderOptions);
		}
	}

	public static FloatFloatImmutablePair renderMTR(@Nullable Drawing drawing, String text, FontRenderOptions fontRenderOptions) {
		if (mtr == null) {
			return new FloatFloatImmutablePair(0, 0);
		} else {
			return mtr.render(drawing, text, fontRenderOptions);
		}
	}

	public static FloatFloatImmutablePair renderKCR(@Nullable Drawing drawing, String text, FontRenderOptions fontRenderOptions) {
		if (kcr == null) {
			return new FloatFloatImmutablePair(0, 0);
		} else {
			return kcr.render(drawing, text, fontRenderOptions);
		}
	}

	public static FloatFloatImmutablePair renderCalligraphy(@Nullable Drawing drawing, String text, FontRenderOptions fontRenderOptions) {
		if (calligraphy == null) {
			return new FloatFloatImmutablePair(0, 0);
		} else {
			return calligraphy.render(drawing, text, fontRenderOptions);
		}
	}
}
