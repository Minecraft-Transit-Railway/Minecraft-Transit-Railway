package org.mtr.render;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.mtr.core.data.Route;
import org.mtr.font.FontRenderHelper;
import org.mtr.font.FontRenderOptions;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class SpecialSignRouteRenderer extends SpecialSignRouteStationExitRendererBase<Route> {

	@Override
	protected boolean calculateTextWidths() {
		return true;
	}

	@Override
	protected void renderOverlayText(MatrixStack matrixStack, String overlayText, Identifier font, float x, float y, float zOffset, float width, float height, float padding, boolean flipText) {
		FontRenderHelper.render(matrixStack, overlayText, FontRenderOptions.builder()
				.font(font)
				.horizontalPositioning(FontRenderOptions.Alignment.CENTER)
				.verticalPositioning(FontRenderOptions.Alignment.CENTER)
				.horizontalSpace(width - padding * 2)
				.verticalSpace(height - padding * 2)
				.horizontalTextAlignment(flipText ? FontRenderOptions.Alignment.END : FontRenderOptions.Alignment.START)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.offsetX(x)
				.offsetY(y)
				.offsetZ(-zOffset * 2)
				.cjkScaling(2)
				.maxFontSize(height / 4)
				.lineBreak(FontRenderOptions.LineBreak.SPLIT)
				.textOverflow(FontRenderOptions.TextOverflow.COMPRESS)
				.build());
	}

	@Override
	protected int getColor(@Nullable Route data) {
		return data == null ? GuiHelper.rainbowColor().getRGB() : GuiHelper.BLACK_COLOR | data.getColor();
	}

	@Nullable
	@Override
	protected String getText(@Nullable Route data, String customText) {
		return null;
	}

	@Override
	protected String getOverlayText(@Nullable Route data, String customText) {
		return data == null ? customText : data.getName().split("\\|\\|")[0];
	}
}
