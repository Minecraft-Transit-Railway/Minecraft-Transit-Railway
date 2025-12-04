package org.mtr.render;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontGroupRegistry;
import org.mtr.font.FontRenderOptions;
import org.mtr.resource.SignResource;
import org.mtr.tool.Drawing;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class SpecialSignRouteStationExitRendererBase<T> extends SpecialSignRendererBase<T> {

	@Override
	public final void render(Drawing textureDrawing, ObjectArrayList<Consumer<Drawing>> deferredRenders, float x, float y, float zOffset, float signSize, ObjectArrayList<T> dataList, boolean flipTexture, boolean flipText, boolean small, String customText, float totalSpace, boolean renderPlaceholder) {
		if (dataList.isEmpty() && !renderPlaceholder) {
			return;
		}

		final int dataCount = Math.max(1, dataList.size());
		final float height = signSize * (small ? 1 - SignResource.SMALL_SIGN_PADDING * 2 : 1);
		final float paddingBetweenTiles = calculateTextWidths() ? signSize * SignResource.SMALL_SIGN_PADDING / 2 : 0;
		final float[] dataTextWidths = new float[dataCount];
		final String[] dataNames = new String[dataCount];
		float rawWidth = calculateTextWidths() ? signSize * SignResource.SMALL_SIGN_PADDING * 2 * dataCount + paddingBetweenTiles * (dataCount - 1) : height * dataCount;

		// Calculate text widths
		for (int i = 0; i < dataCount; i++) {
			dataNames[i] = getOverlayText(Utilities.getElement(dataList, i), customText);
			dataTextWidths[i] = calculateTextWidths() ? FontGroupRegistry.MTR.get().render(null, dataNames[i], FontRenderOptions.builder()
					.verticalSpace(height - signSize * SignResource.SMALL_SIGN_PADDING * 2)
					.cjkScaling(2)
					.maxFontSize(height / 4)
					.lineBreak(FontRenderOptions.LineBreak.SPLIT)
					.build()).leftFloat() : 0;
			rawWidth += dataTextWidths[i];
		}

		final float scale = Math.min(1, totalSpace / rawWidth);
		final float y1 = y + (small ? signSize * SignResource.SMALL_SIGN_PADDING : 0);
		final float startX = x + (flipText ? -1 : 1) * (small ? signSize * SignResource.SMALL_SIGN_PADDING : 0) + (flipText ? signSize - rawWidth * scale : 0);
		float x1 = startX;

		// Texture (tiled to the right length)
		for (int i = 0; i < dataCount; i++) {
			final T data = Utilities.getElement(dataList, i);
			final float textureWidth = (calculateTextWidths() ? dataTextWidths[i] + signSize * SignResource.SMALL_SIGN_PADDING * 2 : height) * scale;
			final int color = getColor(data);
			final float endTextureWidth;

			if (textureWidth <= height) {
				endTextureWidth = textureWidth / 2;
			} else {
				endTextureWidth = height / 4;
				for (float j = 0; j < textureWidth - height / 2; j += height / 2) {
					final float newX1 = x1 + endTextureWidth + j;
					final float newX2 = Math.min(x1 + textureWidth - endTextureWidth, newX1 + height / 2);
					final float u1 = endTextureWidth / height;
					final float u2 = u1 + (newX2 - newX1) / height;
					textureDrawing.setVertices(newX1, y1, newX2, y1 + height, -zOffset).setColor(color).setUv(flipTexture ? u2 : u1, 0, flipTexture ? u1 : u2, 1).draw();
				}
			}

			final float u1 = endTextureWidth / height;
			final float u2 = 1 - u1;
			textureDrawing.setVerticesWH(x1, y1, endTextureWidth, height, -zOffset).setColor(color).setUv(flipTexture ? u1 : 0, 0, flipTexture ? 0 : u1, 1).draw();
			textureDrawing.setVerticesWH(x1 + textureWidth - endTextureWidth, y1, endTextureWidth, height, -zOffset).setColor(color).setUv(flipTexture ? 1 : u2, 0, flipTexture ? u2 : 1, 1).draw();

			final float textStart = x1 + textureWidth / 2;
			final float textWidth = textureWidth - signSize * SignResource.SMALL_SIGN_PADDING * 2;
			final String dataName = dataNames[i];

			if (textWidth > 0) {
				deferredRenders.add(textDrawing -> renderOverlayText(textDrawing, dataName, textStart, y1 + height / 2, zOffset, textureWidth, height, signSize * SignResource.SMALL_SIGN_PADDING, flipText));
			}

			x1 += textureWidth + paddingBetweenTiles * scale;
		}

		// Extra text
		final float textSpace = totalSpace - rawWidth - (small ? 1 : 2) * signSize * SignResource.SMALL_SIGN_PADDING;
		if (dataCount == 1 && textSpace >= signSize / 2) {
			final String extraText = getText(Utilities.getElement(dataList, 0), customText);
			if (extraText != null) {
				final float textX = (flipText ? startX : x1) + (flipText ? -1 : 1) * signSize * SignResource.SMALL_SIGN_PADDING;
				deferredRenders.add(drawing -> FontGroupRegistry.MTR.get().render(drawing, extraText, FontRenderOptions.builder()
						.horizontalSpace(textSpace)
						.verticalSpace(signSize * (1 - SignResource.SMALL_SIGN_PADDING * 2))
						.horizontalTextAlignment(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
						.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
						.horizontalPositioning(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
						.offsetX(textX)
						.offsetY(y + signSize * SignResource.SMALL_SIGN_PADDING)
						.offsetZ(-zOffset)
						.cjkScaling(2)
						.maxFontSize(signSize / 4)
						.lineBreak(FontRenderOptions.LineBreak.SPLIT)
						.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
						.build())
				);
			}
		}
	}

	protected abstract boolean calculateTextWidths();

	protected abstract void renderOverlayText(Drawing drawing, String overlayText, float x, float y, float zOffset, float width, float height, float padding, boolean flipText);

	protected abstract int getColor(@Nullable T data);

	@Nullable
	protected abstract String getText(@Nullable T data, String customText);

	protected abstract String getOverlayText(@Nullable T data, String customText);
}
