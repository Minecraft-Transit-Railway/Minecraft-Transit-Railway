package org.mtr.render;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.mtr.core.data.NameColorDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontRenderHelper;
import org.mtr.font.FontRenderOptions;
import org.mtr.resource.SignResource;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;
import java.util.function.Consumer;

public abstract class SpecialSignPlatformStationRendererBase<T extends NameColorDataBase> extends SpecialSignRendererBase<T> {

	@Override
	public final void render(
			Drawing textureDrawing, ObjectArrayList<Consumer<MatrixStack>> deferredRenders,
			float x, float y, float zOffset,
			float signSize, ObjectArrayList<T> dataList,
			boolean flipTexture, boolean flipText, boolean small, String customText, Identifier font,
			float totalSpace, boolean renderPlaceholder
	) {
		if (dataList.isEmpty() && !renderPlaceholder) {
			return;
		}

		final int dataCount = Math.max(1, dataList.size());
		final float verticalSpace = small ? (1 - SignResource.SMALL_SIGN_PADDING * 2) * signSize : signSize;
		final float largeTextureSize = verticalSpace / dataCount;
		final float smallTextureSize = (verticalSpace - (dataCount - 1) * SignResource.SMALL_SIGN_PADDING * signSize / dataCount) / dataCount;
		final float textureSize = small ? smallTextureSize : largeTextureSize;

		final float x1 = flipText ? x + signSize - textureSize - (small ? signSize * SignResource.SMALL_SIGN_PADDING : 0) : x + (small ? signSize * SignResource.SMALL_SIGN_PADDING : 0);
		final float x2 = x1 + textureSize;

		for (int i = 0; i < dataCount; i++) {
			final T data = Utilities.getElement(dataList, i);
			final ObjectObjectImmutablePair<IntArrayList, String> dataColorsAndDestinations = getColorsAndText(data, customText);
			final IntArrayList colors = dataColorsAndDestinations == null ? null : dataColorsAndDestinations.left();
			final int colorCount = colors == null ? 1 : colors.size();
			final float y1 = y + (small ? signSize * SignResource.SMALL_SIGN_PADDING : 0) + i * largeTextureSize;

			// Texture
			for (int j = 0; j < colorCount; j++) {
				textureDrawing.setVerticesWH(x1, y1 + textureSize * j / colorCount, textureSize, textureSize / colorCount, -zOffset).setColor(GuiHelper.BLACK_COLOR | (colors == null ? GuiHelper.rainbowColor().getRGB() : colors.getInt(j))).setUv(flipTexture ? 1 : 0, (float) j / colorCount, flipTexture ? 0 : 1, (float) (j + 1) / colorCount).draw();
			}

			deferredRenders.add(matrixStack -> {
				// Texture overlay text
				final String overlayText = getOverlayText(data);
				if (overlayText != null) {
					FontRenderHelper.render(matrixStack, overlayText, FontRenderOptions.builder()
							.font(font)
							.horizontalSpace(textureSize * 0.75F)
							.verticalSpace(textureSize * 0.75F)
							.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
							.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
							.horizontalPositioning(FontRenderOptions.Alignment.CENTER)
							.verticalPositioning(FontRenderOptions.Alignment.CENTER)
							.offsetX(x1 + textureSize / 2)
							.offsetY(y1 + textureSize / 2)
							.offsetZ(-zOffset * 2)
							.maxFontSize(signSize)
							.lineBreak(FontRenderOptions.LineBreak.SPLIT)
							.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
							.build());
				}

				// Text
				final float textSpace = totalSpace - textureSize - signSize * SignResource.SMALL_SIGN_PADDING;
				if (textSpace > 0) {
					FontRenderHelper.render(matrixStack, dataColorsAndDestinations == null ? customText : dataColorsAndDestinations.right(), FontRenderOptions.builder()
							.font(font)
							.horizontalSpace(textSpace)
							.verticalSpace(Math.min(signSize * (1 - SignResource.SMALL_SIGN_PADDING * 2), smallTextureSize))
							.horizontalTextAlignment(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
							.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
							.horizontalPositioning(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
							.verticalPositioning(FontRenderOptions.Alignment.CENTER)
							.offsetX(flipText ? x1 - signSize * SignResource.SMALL_SIGN_PADDING : x2 + signSize * SignResource.SMALL_SIGN_PADDING)
							.offsetY(y1 + textureSize / 2)
							.offsetZ(-zOffset)
							.cjkScaling(2)
							.maxFontSize(signSize / 4)
							.lineBreak(FontRenderOptions.LineBreak.SPLIT)
							.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
							.build());
				}
			});
		}
	}

	@Nullable
	protected abstract ObjectObjectImmutablePair<IntArrayList, String> getColorsAndText(@Nullable T data, String customText);

	@Nullable
	protected abstract String getOverlayText(@Nullable T data);
}
