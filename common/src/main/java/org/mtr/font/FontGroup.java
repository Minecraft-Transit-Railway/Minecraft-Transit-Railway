package org.mtr.font;

import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public final class FontGroup {

	private final ObjectImmutableList<FontProvider> fontProviders;

	private static final FontProvider FALLBACK = new FontProvider(null);

	public FontGroup(ObjectImmutableList<FontProvider> fontProviders) {
		this.fontProviders = fontProviders;
	}

	public void render(MatrixStack matrixStack, String text, int color, int light) {
		final float[] x = {0};
		text.chars().forEach(c -> {
			boolean notRendered = true;
			for (final FontProvider fontProvider : fontProviders) {
				final ObjectObjectImmutablePair<Identifier, FontProvider.GlyphCoordinates> textureAndGlyphCoordinates = fontProvider.render((char) c);
				if (textureAndGlyphCoordinates != null) {
					x[0] += render(textureAndGlyphCoordinates, matrixStack, x[0], color, light);
					notRendered = false;
					break;
				}
			}
			if (notRendered) {
				x[0] += render(FALLBACK.render((char) c), matrixStack, x[0], color, light);
			}
		});
	}

	private static float render(@Nullable ObjectObjectImmutablePair<Identifier, FontProvider.GlyphCoordinates> textureAndGlyphCoordinates, MatrixStack matrixStack, float x, int color, int light) {
		if (textureAndGlyphCoordinates == null) {
			return 0;
		} else {
			int scale = 16;
			final VertexConsumer vertexConsumer = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getText(textureAndGlyphCoordinates.left()));
			final FontProvider.GlyphCoordinates glyphCoordinates = textureAndGlyphCoordinates.right();
			final Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();

			final float u1 = glyphCoordinates.u1();
			final float v1 = glyphCoordinates.v1();
			final float u2 = glyphCoordinates.u2();
			final float v2 = glyphCoordinates.v2();

			final float x1 = (x - glyphCoordinates.xOffset()) * scale;
			final float y1 = (1 - glyphCoordinates.yOffset()) * scale;
			final float x2 = x1 + glyphCoordinates.width() * scale;
			final float y2 = y1 + glyphCoordinates.height() * scale;

			vertexConsumer.vertex(matrix4f, x1, y2, 0).texture(u1, v2).color(color).light(light);
			vertexConsumer.vertex(matrix4f, x2, y2, 0).texture(u2, v2).color(color).light(light);
			vertexConsumer.vertex(matrix4f, x2, y1, 0).texture(u2, v1).color(color).light(light);
			vertexConsumer.vertex(matrix4f, x1, y1, 0).texture(u1, v1).color(color).light(light);

			return glyphCoordinates.xAdvance();
		}
	}
}
