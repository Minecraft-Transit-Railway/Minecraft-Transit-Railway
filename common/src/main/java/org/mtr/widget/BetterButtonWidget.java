package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class BetterButtonWidget extends ClickableWidget {

	@Nullable
	private final Identifier icon;
	@Nullable
	private final String text;
	private final int textWidth;
	private final int fixedWidth;
	private final Runnable onPress;

	public BetterButtonWidget(@Nullable Identifier icon, @Nullable String text, int width, Runnable onPress) {
		super(0, 0, 0, GuiHelper.DEFAULT_LINE_SIZE, Text.empty());
		this.icon = icon;
		this.text = text;
		textWidth = text == null ? 0 : MinecraftClient.getInstance().textRenderer.getWidth(text);
		fixedWidth = width;
		this.onPress = onPress;
		setDimensions();
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		setDimensions();
		final MatrixStack matrixStack = context.getMatrices();
		final int middlePadding = icon != null && text != null ? GuiHelper.DEFAULT_PADDING : 0;
		final int iconAndTextWidth = getIconWidth() + middlePadding + textWidth;

		// Draw background
		new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()))
				.setVerticesWH(getX(), getY(), width, height)
				.setColor(isMouseOver(mouseX, mouseY) ? GuiHelper.HOVER_COLOR : GuiHelper.BACKGROUND_COLOR)
				.draw();

		// Draw icon
		if (icon != null) {
			new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGuiTextured(icon)))
					.setVerticesWH(getX() + (width - iconAndTextWidth) / 2F, getY(), GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE)
					.setUv()
					.draw();
		}

		// Draw text
		GuiHelper.drawText(context, text, getX() + (width - iconAndTextWidth) / 2F + getIconWidth() + middlePadding, getY() + GuiHelper.DEFAULT_PADDING, 0, GuiHelper.WHITE_COLOR);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		onPress.run();
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	private void setDimensions() {
		setWidth(fixedWidth <= 0 ? getIconWidth() + (text == null ? 0 : textWidth + GuiHelper.DEFAULT_PADDING * 2) : fixedWidth);
		setHeight(GuiHelper.DEFAULT_LINE_SIZE);
	}

	private int getIconWidth() {
		return icon == null ? 0 : GuiHelper.DEFAULT_LINE_SIZE;
	}
}
