package org.mtr.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiAnimation;
import org.mtr.tool.GuiHelper;

import java.util.function.IntConsumer;

public final class TabGroupWidget extends ClickableWidget {

	private int selectedIndex = -1;
	private int mouseOverIndex = -1;

	private final IntConsumer onChangeTab;
	private final Text[] messages;
	private final int[] rawTextWidths;
	private final int[] scaledTextWidths;
	private final int totalWidgetWidth;
	private final GuiAnimation guiAnimation1 = new GuiAnimation(ANIMATION_DURATION);
	private final GuiAnimation guiAnimation2 = new GuiAnimation(ANIMATION_DURATION);

	private static final int ANIMATION_DURATION = 200;

	public TabGroupWidget(int minWidth, IntConsumer onChangeTab, Text... messages) {
		super(0, 0, minWidth, GuiHelper.DEFAULT_LINE_SIZE, Text.empty());
		this.onChangeTab = onChangeTab;
		this.messages = messages;
		rawTextWidths = new int[messages.length];
		scaledTextWidths = new int[messages.length];
		int totalTextWidth = 0;

		for (int i = 0; i < messages.length; i++) {
			rawTextWidths[i] = MinecraftClient.getInstance().textRenderer.getWidth(messages[i]);
			totalTextWidth += rawTextWidths[i];
		}

		final float scale = Math.max(1, (minWidth - messages.length * 2F * GuiHelper.DEFAULT_PADDING) / Math.max(1, totalTextWidth));
		int totalWidgetWidth = 0;

		for (int i = 0; i < messages.length; i++) {
			scaledTextWidths[i] = (int) Math.ceil(rawTextWidths[i] * scale);
			totalWidgetWidth += scaledTextWidths[i] + GuiHelper.DEFAULT_PADDING * 2;
		}

		this.totalWidgetWidth = totalWidgetWidth;
		setWidth(totalWidgetWidth);
	}

	@Override
	protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		setWidth(totalWidgetWidth);
		final MatrixStack matrixStack = context.getMatrices();
		final Drawing drawing = new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));

		// Handle animation
		guiAnimation1.tick();
		guiAnimation2.tick();

		// Draw the widget background
		drawing.setVerticesWH(getX(), getY(), width, height).setColor(GuiHelper.BACKGROUND_COLOR).draw();
		drawing.setVerticesWH(getX(), getY() + height - 1, width, 1).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();

		// Draw the selected tab indicator
		drawing.setVertices(getX() + guiAnimation1.getCurrentValue(), getY() + height - 1, getX() + guiAnimation2.getCurrentValue(), getY() + height).setColor(GuiHelper.WHITE_COLOR).draw();

		int x = getX();
		mouseOverIndex = -1;

		// Render tab
		for (int i = 0; i < messages.length; i++) {
			// Calculate mouse hover and draw the hover background
			if (active && Utilities.isBetween(mouseX, x, x + GuiHelper.DEFAULT_PADDING * 2 + scaledTextWidths[i] - 1) && Utilities.isBetween(mouseY, getY(), getY() + height - 1)) {
				mouseOverIndex = i;
				drawing.setVerticesWH(x, getY(), GuiHelper.DEFAULT_PADDING * 2 + scaledTextWidths[i], getHeight()).setColor(GuiHelper.HOVER_COLOR);
			}

			// Set the selected tab indicator position (1)
			final boolean isSelected = i == selectedIndex;
			if (isSelected) {
				guiAnimation1.animate(x - getX());
			}

			// Render text
			x += GuiHelper.DEFAULT_PADDING;
			GuiHelper.drawText(context, messages[i], x + (scaledTextWidths[i] - rawTextWidths[i]) / 2F, getY() + GuiHelper.DEFAULT_PADDING, 0, isSelected ? GuiHelper.WHITE_COLOR : GuiHelper.LIGHT_GRAY_COLOR);
			x += scaledTextWidths[i] + GuiHelper.DEFAULT_PADDING;

			// Set the selected tab indicator position (2)
			if (isSelected) {
				guiAnimation2.animate(x - getX());
			}
		}
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		selectTab(mouseOverIndex);
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return active && visible && super.isValidClickButton(button);
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	public void selectTab(int selectedIndex) {
		if (active && selectedIndex != this.selectedIndex && selectedIndex >= 0 && selectedIndex < messages.length) {
			this.selectedIndex = selectedIndex;
			onChangeTab.accept(selectedIndex);
		}
	}
}
