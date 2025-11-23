package org.mtr.widget;

import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class BetterButtonWidget extends ClickableWidgetBase {

	@Setter
	private int backgroundColor = GuiHelper.BACKGROUND_COLOR;
	@Setter
	private int hoverColor = GuiHelper.HOVER_COLOR;
	@Setter
	private int textColor = GuiHelper.WHITE_COLOR;

	@Nullable
	private final Identifier icon;
	@Nullable
	private final String text;
	private final int textWidth;
	private final int fixedWidth;
	private final Runnable onPress;

	public BetterButtonWidget(@Nullable Identifier icon, @Nullable String text, int width, Runnable onPress) {
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

		// Draw background
		new Drawing(matrixStack, RenderLayer.getGui())
				.setVerticesWH(getX(), getY(), width, height)
				.setColor(isMouseOver(mouseX, mouseY) ? hoverColor : backgroundColor)
				.draw();

		// Draw icon
		if (icon != null) {
			new Drawing(matrixStack, RenderLayer.getGuiTextured(icon))
					.setVerticesWH(getX() + (width - getContentWidth()) / 2F, getY() + GuiHelper.DEFAULT_PADDING / 2F, GuiHelper.DEFAULT_ICON_SIZE, GuiHelper.DEFAULT_ICON_SIZE)
					.setUv()
					.draw();
		}

		// Draw text
		GuiHelper.drawText(context, text, getX() + width - (width - getContentWidth()) / 2F - textWidth, getY() + GuiHelper.DEFAULT_PADDING, 0, active ? textColor : GuiHelper.DISABLED_TEXT_COLOR);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		onPress.run();
	}

	private void setDimensions() {
		setDimensions(fixedWidth <= 0 ? getContentWidth() + GuiHelper.DEFAULT_PADDING * (text == null ? 1 : 2) : fixedWidth, GuiHelper.DEFAULT_LINE_SIZE);
	}

	private int getContentWidth() {
		return (icon == null ? 0 : GuiHelper.DEFAULT_ICON_SIZE) + (icon == null || text == null ? 0 : GuiHelper.DEFAULT_PADDING) + textWidth;
	}
}
