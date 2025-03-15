package org.mtr.font;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;

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

	public static void renderMTR(Matrix4f matrix4f, VertexConsumer vertexConsumer, String text, int color, int light) {
		if (mtr != null) {
			mtr.render(matrix4f, vertexConsumer, text, color, light);
		}
	}

	public static void renderKCR(Matrix4f matrix4f, VertexConsumer vertexConsumer, String text, int color, int light) {
		if (kcr != null) {
			kcr.render(matrix4f, vertexConsumer, text, color, light);
		}
	}

	public static void renderCalligraphy(Matrix4f matrix4f, VertexConsumer vertexConsumer, String text, int color, int light) {
		if (calligraphy != null) {
			calligraphy.render(matrix4f, vertexConsumer, text, color, light);
		}
	}
}
