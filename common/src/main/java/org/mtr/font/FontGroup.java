package org.mtr.font;

import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;

import javax.annotation.Nullable;

public final class FontGroup {

	private final ObjectImmutableList<FontProvider> fontProviders;

	private static final FontProvider FALLBACK = new FontProvider(null);

	public FontGroup(ObjectImmutableList<FontProvider> fontProviders) {
		this.fontProviders = fontProviders;
	}

	public void render(Matrix4f matrix4f, VertexConsumer vertexConsumer, String text, int color, int light) {
		final int[] x = {0};
		text.codePoints().forEach(character -> {
			boolean notRendered = true;
			for (final FontProvider fontProvider : fontProviders) {
				final IntObjectImmutablePair<byte[]> renderData = fontProvider.render(character);
				if (renderData != null) {
					x[0] += render(renderData, fontProvider.isFileFont(), matrix4f, vertexConsumer, x[0], color, light);
					notRendered = false;
					break;
				}
			}
			if (notRendered) {
				x[0] += render(FALLBACK.render(character), true, matrix4f, vertexConsumer, x[0], color, light);
			}
		});
	}

	private static int render(@Nullable IntObjectImmutablePair<byte[]> renderData, boolean isFileFont, Matrix4f matrix4f, VertexConsumer vertexConsumer, int x, int color, int light) {
		if (renderData == null) {
			return 0;
		} else {
			final float scale = 1F / FontProvider.FONT_SIZE;
			int index = renderData.leftInt();
			final byte[] data = renderData.right();
			final int width = data[index++] & 0xFF;
			final int height = data[index++] & 0xFF;
			final int additionalScaleOrXOffset = data[index++];
			final int yOffset = data[index++];
			final int advance = data[index++] & 0xFF;
			final int additionalScale = isFileFont ? 1 : additionalScaleOrXOffset;

			int pixelOffsetX = 0;
			int pixelOffsetY = 0;
			int processedPixels = 0;

			while (processedPixels < width * height) {
				final int alpha = data[index++] & 0xFF;
				int count = (data[index++] & 0xFF) + 1;
				processedPixels += count;

				while (count > 0) {
					final int length = Math.min(width - pixelOffsetX, count);

					if (alpha > 0) {
						final float x1 = (x + pixelOffsetX * additionalScale - (isFileFont ? additionalScaleOrXOffset : 0)) * scale;
						final float x2 = x1 + length * scale * additionalScale;
						final float y1 = (pixelOffsetY * additionalScale + FontProvider.FONT_SIZE - yOffset) * scale;
						final float y2 = y1 + scale * additionalScale;
						final int newColor = (alpha << 24) + (color & 0xFFFFFF);
						vertexConsumer.vertex(matrix4f, x1, y1, 0).color(newColor).light(light);
						vertexConsumer.vertex(matrix4f, x1, y2, 0).color(newColor).light(light);
						vertexConsumer.vertex(matrix4f, x2, y2, 0).color(newColor).light(light);
						vertexConsumer.vertex(matrix4f, x2, y1, 0).color(newColor).light(light);
					}

					pixelOffsetX += length;
					count -= length;

					if (pixelOffsetX == width) {
						pixelOffsetX = 0;
						pixelOffsetY++;
					}
				}
			}

			return advance;
		}
	}
}
