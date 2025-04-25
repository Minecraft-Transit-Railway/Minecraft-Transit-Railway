package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;
import org.mtr.font.FontGroups;
import org.mtr.font.FontRenderOptions;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

public class BetterButtonWidget extends ButtonWidget {

	private final boolean dynamicWidth;

	public BetterButtonWidget(Text message, int width, Runnable onPress) {
		super(0, 0, Math.max(0, width), GuiHelper.DEFAULT_LINE_SIZE, message, button -> onPress.run(), DEFAULT_NARRATION_SUPPLIER);
		this.dynamicWidth = width <= 0;
	}

	protected final void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		final MatrixStack matrixStack = context.getMatrices();
		final Drawing drawing = new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
		drawing.setVerticesWH(getX(), getY(), getWidth(), getHeight()).setColor(Utilities.isBetween(mouseX, getX(), getX() + width) && Utilities.isBetween(mouseY, getY(), getY() + height) ? GuiHelper.HOVER_COLOR : GuiHelper.BACKGROUND_COLOR).draw();
		matrixStack.push();
		matrixStack.translate(getX() + GuiHelper.DEFAULT_PADDING, getY() + GuiHelper.DEFAULT_PADDING, 0);
		final float textWidth = FontGroups.renderMinecraft(drawing, getMessage().getString(), createFontRenderOptionsBuilder().build()).leftFloat();
		if (dynamicWidth) {
			setWidth((int) Math.ceil(textWidth) + GuiHelper.DEFAULT_PADDING * 2);
		}
		matrixStack.pop();
		renderAdditional(drawing);
	}

	protected void renderAdditional(Drawing drawing) {
	}

	protected FontRenderOptions.FontRenderOptionsBuilder createFontRenderOptionsBuilder() {
		return FontRenderOptions.builder()
				.horizontalSpace(dynamicWidth ? 0 : width - GuiHelper.DEFAULT_PADDING * 2)
				.verticalSpace(height - GuiHelper.DEFAULT_PADDING * 2)
				.horizontalTextAlignment(dynamicWidth ? FontRenderOptions.Alignment.START : FontRenderOptions.Alignment.CENTER)
				.verticalTextAlignment(FontRenderOptions.Alignment.CENTER)
				.lineBreak(FontRenderOptions.LineBreak.FORCE_ONE_LINE)
				.textOverflow(dynamicWidth ? FontRenderOptions.TextOverflow.NONE : FontRenderOptions.TextOverflow.COMPRESS);
	}
}
