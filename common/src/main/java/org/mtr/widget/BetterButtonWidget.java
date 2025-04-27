package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

public final class BetterButtonWidget extends ButtonWidget {

	private final boolean dynamicWidth;

	public BetterButtonWidget(Text message, int width, Runnable onPress) {
		super(0, 0, Math.max(0, width), GuiHelper.DEFAULT_LINE_SIZE, message, button -> onPress.run(), DEFAULT_NARRATION_SUPPLIER);
		dynamicWidth = width <= 0;
	}

	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		final Drawing drawing = new Drawing(context.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
		drawing.setVerticesWH(getX(), getY(), getWidth(), getHeight()).setColor(Utilities.isBetween(mouseX, getX(), getX() + width) && Utilities.isBetween(mouseY, getY(), getY() + height) ? GuiHelper.HOVER_COLOR : GuiHelper.BACKGROUND_COLOR).draw();
		final float textWidth = FontGroups.renderMinecraft(drawing, getMessage().getString(), FontRenderOptions.builder()
				.horizontalSpace(dynamicWidth ? 0 : width - GuiHelper.DEFAULT_PADDING * 2)
				.verticalSpace(height - GuiHelper.DEFAULT_PADDING * 2)
				.horizontalTextAlignment(dynamicWidth ? FontRenderOptions.Alignment.START : FontRenderOptions.Alignment.CENTER)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.offsetX(getX() + GuiHelper.DEFAULT_PADDING)
				.offsetY(getY() + GuiHelper.DEFAULT_PADDING)
				.lineBreak(FontRenderOptions.LineBreak.FORCE_ONE_LINE)
				.textOverflow(dynamicWidth ? FontRenderOptions.TextOverflow.NONE : FontRenderOptions.TextOverflow.COMPRESS)
				.build()
		).leftFloat();
		if (dynamicWidth) {
			setWidth((int) Math.ceil(textWidth) + GuiHelper.DEFAULT_PADDING * 2);
		}
	}
}
