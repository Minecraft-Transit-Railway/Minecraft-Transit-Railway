package org.mtr.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.mtr.core.data.StationExit;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontRenderHelper;
import org.mtr.font.FontRenderOptions;
import org.mtr.tool.DataHelper;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class SpecialSignStationExitRenderer extends SpecialSignRouteStationExitRendererBase<StationExit> {

	private static final String[] DEFAULT_EXIT_NAMES = {"A", "B", "C", "D"};

	@Override
	protected boolean calculateTextWidths() {
		return false;
	}

	@Override
	protected void renderOverlayText(MatrixStack matrixStack, String overlayText, Identifier font, float x, float y, float zOffset, float width, float height, float padding, boolean flipText) {
		renderText(matrixStack, overlayText, font, x, y, zOffset * 2, width - padding * 2, height);
	}

	@Override
	protected int getColor(@Nullable StationExit data) {
		return GuiHelper.WHITE_COLOR;
	}

	@Nullable
	@Override
	protected String getText(@Nullable StationExit data, String customText) {
		return data == null ? customText : Utilities.getElement(data.getDestinations(), 0);
	}

	@Override
	protected String getOverlayText(@Nullable StationExit data, String customText) {
		return data == null ? DEFAULT_EXIT_NAMES[(int) (System.currentTimeMillis() / 2000) % DEFAULT_EXIT_NAMES.length] : data.getName();
	}

	public static void renderText(MatrixStack matrixStack, String stationExitName, Identifier font, float x, float y, float zOffset, float width, float height) {
		final String[] stationExitNameSplit = DataHelper.getSplitStationExitName(stationExitName);
		final float textSize = height * GuiHelper.MINECRAFT_FONT_SIZE / GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT;
		final float width1 = FontRenderHelper.render(null, stationExitNameSplit[0], FontRenderOptions.builder().maxFontSize(textSize).build()).leftFloat();
		final float width2 = FontRenderHelper.render(null, stationExitNameSplit[1], FontRenderOptions.builder().maxFontSize(textSize / 2).build()).leftFloat();
		final float scale = Math.min(1, width / (width1 + width2));
		final float textX = x + (Math.max(0, width - width1 * scale - width2 * scale) - width) / 2;
		final float textY = y + height / 2;

		FontRenderHelper.render(matrixStack, stationExitNameSplit[0], FontRenderOptions.builder()
				.font(font)
				.horizontalPositioning(FontRenderOptions.Alignment.START)
				.verticalPositioning(FontRenderOptions.Alignment.END)
				.horizontalSpace(width1 * scale)
				.verticalSpace(height)
				.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.maxFontSize(textSize)
				.offsetX(textX)
				.offsetY(textY)
				.offsetZ(-zOffset)
				.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
				.build()
		);

		FontRenderHelper.render(matrixStack, stationExitNameSplit[1], FontRenderOptions.builder()
				.font(font)
				.horizontalPositioning(FontRenderOptions.Alignment.START)
				.verticalPositioning(FontRenderOptions.Alignment.END)
				.horizontalSpace(width2 * scale)
				.verticalSpace(height - textSize * 0.3125F)
				.horizontalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.maxFontSize(textSize / 2)
				.offsetX(textX + width1 * scale)
				.offsetY(textY)
				.offsetZ(-zOffset)
				.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
				.build()
		);
	}
}
