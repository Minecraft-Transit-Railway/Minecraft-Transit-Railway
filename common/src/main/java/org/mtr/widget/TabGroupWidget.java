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
import org.mtr.tool.GuiHelper;

import java.util.function.IntConsumer;

public final class TabGroupWidget extends ClickableWidget {

	private int selectedIndex = -1;
	private int mouseOverIndex = -1;

	private double animationStart1, animationStart2;
	private double animationCurrent1, animationCurrent2;
	private int animationTarget1, animationTarget2;
	private long animationStartMillis;

	private final IntConsumer onChangeTab;
	private final Text[] messages;
	private final int[] rawTextWidths;
	private final int[] scaledTextWidths;
	private final int totalWidgetWidth;

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
		int x = getX();
		mouseOverIndex = -1;

		// Render tab
		for (int i = 0; i < messages.length; i++) {
			// Calculate mouse hover and draw the hover background (z = 1)
			if (active && Utilities.isBetween(mouseX, x, x + GuiHelper.DEFAULT_PADDING * 2 + scaledTextWidths[i] - 1) && Utilities.isBetween(mouseY, getY(), getY() + height - 1)) {
				mouseOverIndex = i;
				context.fill(x, getY(), x + GuiHelper.DEFAULT_PADDING * 2 + scaledTextWidths[i], getY() + getHeight(), 1, GuiHelper.HOVER_COLOR);
			}

			// Set the selected tab indicator position (1)
			final boolean isSelected = i == selectedIndex;
			if (isSelected) {
				animationTarget1 = x;
			}

			// Render text (z = 2)
			x += GuiHelper.DEFAULT_PADDING;
			matrixStack.push();
			matrixStack.translate(x + (scaledTextWidths[i] - rawTextWidths[i]) / 2F, getY() + GuiHelper.DEFAULT_PADDING, 2);
			context.drawText(MinecraftClient.getInstance().textRenderer, messages[i], 0, 0, isSelected ? GuiHelper.WHITE_COLOR : GuiHelper.LIGHT_GRAY_COLOR, false);
			matrixStack.pop();
			x += scaledTextWidths[i] + GuiHelper.DEFAULT_PADDING;

			// Set the selected tab indicator position (2)
			if (isSelected) {
				animationTarget2 = x;
			}
		}

		// Draw widget background (z = 0)
		context.fill(getX(), getY(), getX() + width, getY() + height - 1, GuiHelper.BACKGROUND_COLOR);
		context.fill(getX(), getY() + height - 1, getX() + width, getY() + height, GuiHelper.BACKGROUND_ACCENT_COLOR);

		// Handle animation
		if (Math.abs(animationTarget1 - animationCurrent1) < 0.01 && Math.abs(animationTarget2 - animationCurrent2) < 0.01) {
			animationStartMillis = 0;
		} else {
			final long currentMillis = System.currentTimeMillis();
			if (animationStartMillis == 0) {
				animationStartMillis = currentMillis;
				animationStart1 = animationCurrent1;
				animationStart2 = animationCurrent2;
			} else {
				final double progress = Math.sin(Math.PI / 2 * Utilities.clamp((double) (currentMillis - animationStartMillis) / ANIMATION_DURATION, 0, 1));
				animationCurrent1 = animationStart1 + progress * (animationTarget1 - animationStart1);
				animationCurrent2 = animationStart2 + progress * (animationTarget2 - animationStart2);
			}
		}

		// Draw selected tab indicator (z = 1)
		new Drawing(matrixStack, MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui())).setVertices(animationCurrent1, getY() + height - 1, animationCurrent2, getY() + height, 1).setColor(GuiHelper.WHITE_COLOR).draw();
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		selectTab(mouseOverIndex);
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return active && super.isValidClickButton(button);
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
