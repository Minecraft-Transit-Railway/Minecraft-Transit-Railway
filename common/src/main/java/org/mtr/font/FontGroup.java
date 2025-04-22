package org.mtr.font;

import it.unimi.dsi.fastutil.floats.FloatFloatImmutablePair;
import it.unimi.dsi.fastutil.ints.IntObjectImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;
import org.mtr.data.IGui;

import javax.annotation.Nullable;
import java.awt.*;

public final class FontGroup {

	private final ObjectImmutableList<FontProvider> fontProviders;

	private static final float LINE_SPACING = 0.25F;
	private static final FontProvider FALLBACK = new FontProvider(null);

	public FontGroup(ObjectImmutableList<FontProvider> fontProviders) {
		this.fontProviders = fontProviders;
	}

	public FloatFloatImmutablePair render(@Nullable Matrix4f matrix4f, @Nullable VertexConsumer vertexConsumer, String text, FontRenderOptions fontRenderOptions) {
		// Split lines
		final String[] lines;
		switch (fontRenderOptions.getLineBreak()) {
			case SPLIT, ALTERNATE -> lines = text.split("\\|");
			case FORCE_ONE_LINE -> lines = new String[]{IGui.formatStationName(text)};
			default -> lines = new String[0];
		}

		// Calculate dimensions
		final float[] rawLineWidths = new float[lines.length];
		final float[] rawLineHeights = new float[lines.length];
		final float[] fontSize = new float[lines.length];
		final int[] colors = new int[lines.length];
		float maxLineWidth = 0;
		float totalLineHeight = 0;
		final boolean hasCjkColor = fontRenderOptions.getCjkColor() != 0 && fontRenderOptions.getCjkColor() != fontRenderOptions.getColor();
		final boolean shouldCheckCjk = matrix4f != null && vertexConsumer != null && (hasCjkColor || fontRenderOptions.getCjkScaling() != 1);
		for (int i = 0; i < lines.length; i++) {
			final boolean isCjk = shouldCheckCjk && IGui.isCjk(lines[i]);
			fontSize[i] = (isCjk ? fontRenderOptions.getCjkScaling() : 1) * fontRenderOptions.getMaxFontSize();
			colors[i] = hasCjkColor && isCjk ? fontRenderOptions.getCjkColor() : fontRenderOptions.getColor();
			final float[] dimensions = renderRaw(null, null, lines[i], 0, 0, fontSize[i], fontSize[i], 0, 0);
			rawLineWidths[i] = dimensions[0];
			rawLineHeights[i] = dimensions[1];
			maxLineWidth = Math.max(maxLineWidth, rawLineWidths[i]);
			totalLineHeight += rawLineHeights[i];
		}

		if (matrix4f != null && vertexConsumer != null) {
			// Calculate scale
			final float yScale;
			if (fontRenderOptions.getTextOverflow() == FontRenderOptions.TextOverflow.SCALE) {
				yScale = Math.min(1, Math.min(fontRenderOptions.getVerticalSpace() / totalLineHeight, fontRenderOptions.getHorizontalSpace() / maxLineWidth));
			} else {
				yScale = Math.min(1, fontRenderOptions.getVerticalSpace() / totalLineHeight);
			}

			// Calculate stating position
			final float x = fontRenderOptions.getHorizontalPositioning().getOffset(fontRenderOptions.getHorizontalSpace());
			float y = fontRenderOptions.getVerticalPositioning().getOffset(fontRenderOptions.getVerticalSpace()) + fontRenderOptions.getVerticalTextAlignment().getOffset(totalLineHeight * yScale - fontRenderOptions.getVerticalSpace());

			// Render text
			for (int i = 0; i < lines.length; i++) {
				final float xScale = fontRenderOptions.getTextOverflow() == FontRenderOptions.TextOverflow.COMPRESS ? Math.min(yScale, Math.min(1, fontRenderOptions.getHorizontalSpace() / rawLineWidths[i])) : yScale;
				final float horizontalOffset = fontRenderOptions.getHorizontalTextAlignment().getOffset(rawLineWidths[i] * xScale - fontRenderOptions.getHorizontalSpace());
				renderRaw(matrix4f, vertexConsumer, lines[i], x + horizontalOffset, y, xScale * fontSize[i], yScale * fontSize[i], colors[i], fontRenderOptions.getLight());
				y += rawLineHeights[i] * yScale;
			}
		}

		return new FloatFloatImmutablePair(Math.min(maxLineWidth, fontRenderOptions.getHorizontalSpace()), Math.min(totalLineHeight, fontRenderOptions.getVerticalSpace()));
	}

	private float[] renderRaw(@Nullable Matrix4f matrix4f, @Nullable VertexConsumer vertexConsumer, String text, float x, float y, float xScale, float yScale, int color, int light) {
		final float[] xOffset = {0};
		text.codePoints().forEach(character -> {
			boolean notRendered = true;
			for (final FontProvider fontProvider : fontProviders) {
				final IntObjectImmutablePair<byte[]> renderData = fontProvider.render(character);
				if (renderData != null) {
					xOffset[0] += render(renderData, fontProvider.isFileFont(), matrix4f, vertexConsumer, x + xOffset[0], y, xScale, yScale, color, light);
					notRendered = false;
					break;
				}
			}
			if (notRendered) {
				xOffset[0] += render(FALLBACK.render(character), true, matrix4f, vertexConsumer, x + xOffset[0], y, xScale, yScale, color, light);
			}
		});
		return new float[]{xOffset[0], yScale * (1 + LINE_SPACING)};
	}

	private static float render(@Nullable IntObjectImmutablePair<byte[]> renderData, boolean isFileFont, @Nullable Matrix4f matrix4f, @Nullable VertexConsumer vertexConsumer, float x, float y, float xScale, float yScale, int color, int light) {
		if (renderData == null) {
			return 0;
		} else {
			int index = renderData.leftInt();
			final byte[] data = renderData.right();
			final int width = data[index++] & 0xFF;
			final int height = data[index++] & 0xFF;
			final int additionalScaleOrXOffset = data[index++];
			final int yOffset = data[index++];
			final int advance = data[index++] & 0xFF;
			final float newXScale = xScale / FontResourceBase.FONT_SIZE;
			final float newYScale = yScale / FontResourceBase.FONT_SIZE;

			if (matrix4f != null && vertexConsumer != null) {
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
							final float x1 = x + (pixelOffsetX * additionalScale - (isFileFont ? additionalScaleOrXOffset : 0)) * newXScale;
							final float x2 = x1 + length * newXScale * additionalScale;
							final float y1 = y + (pixelOffsetY * additionalScale + FontResourceBase.FONT_SIZE - yOffset) * newYScale;
							final float y2 = y1 + newYScale * additionalScale;
							final Color inputColor = new Color(color, true);
							final int newColor = new Color(inputColor.getRed(), inputColor.getGreen(), inputColor.getBlue(), (alpha + inputColor.getAlpha()) / 2).getRGB();
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
			}

			return advance * newXScale;
		}
	}
}
