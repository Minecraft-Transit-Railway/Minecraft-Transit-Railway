package org.mtr.widget;

import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiAnimation;
import org.mtr.tool.GuiHelper;

import javax.annotation.Nullable;

public final class BetterCheckboxWidget extends ClickableWidgetBase {

	public boolean isChecked;

	@Setter
	private int backgroundColor = GuiHelper.BACKGROUND_COLOR;
	@Setter
	private int hoverColor = GuiHelper.HOVER_COLOR;
	@Setter
	private int textColor = GuiHelper.WHITE_COLOR;

	@Nullable
	private final String text;
	private final int textWidth;
	@Nullable
	private final Runnable onPress;

	private final GuiAnimation guiAnimationIcon = new GuiAnimation();

	private static final int ANIMATION_DURATION = 200;

	public BetterCheckboxWidget(@Nullable String text, @Nullable Runnable onPress) {
		this.text = text;
		textWidth = text == null ? 0 : MinecraftClient.getInstance().textRenderer.getWidth(text);
		this.onPress = onPress;
		setDimensions();
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		setDimensions();
		final MatrixStack matrixStack = context.getMatrices();

		// Draw background
		new Drawing(matrixStack, RenderLayer.getGui())
				.setVerticesWH(getX(), getY(), GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE)
				.setColor(isMouseOver(mouseX, mouseY) ? hoverColor : backgroundColor)
				.draw();

		// Handle animation
		guiAnimationIcon.tick();
		guiAnimationIcon.animate(isChecked ? 1 : 0, ANIMATION_DURATION);

		// Draw icon
		new Drawing(matrixStack, RenderLayer.getGuiTextured(GuiHelper.CHECK_TEXTURE_ID))
				.setVerticesWH(getX() + GuiHelper.DEFAULT_PADDING / 2F, getY() + GuiHelper.DEFAULT_PADDING / 2F, GuiHelper.DEFAULT_ICON_SIZE * guiAnimationIcon.getCurrentValue(), GuiHelper.DEFAULT_ICON_SIZE)
				.setUv(0, 0, guiAnimationIcon.getCurrentValue(), 1)
				.draw();

		// Draw text
		GuiHelper.drawText(context, text, getX() + GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING, getY() + GuiHelper.DEFAULT_PADDING, 0, active ? textColor : GuiHelper.DISABLED_TEXT_COLOR);
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		isChecked = !isChecked;
		if (onPress != null) {
			onPress.run();
		}
	}

	private void setDimensions() {
		setDimensions(GuiHelper.DEFAULT_LINE_SIZE + textWidth + (text == null ? 0 : GuiHelper.DEFAULT_PADDING * 2), GuiHelper.DEFAULT_LINE_SIZE);
	}
}
