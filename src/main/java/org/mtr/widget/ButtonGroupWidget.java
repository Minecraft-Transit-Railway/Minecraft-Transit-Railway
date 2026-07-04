package org.mtr.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.mtr.tool.GuiHelper;

import java.util.function.Consumer;
import java.util.function.IntConsumer;

public final class ButtonGroupWidget extends ClickableWidgetBase {

	public final BetterButtonWidget[] buttons;
	private final int totalWidgetWidth;

	public ButtonGroupWidget(int minWidth, IntConsumer onClick, String... messages) {
		final int[] rawTextWidths = new int[messages.length];
		int totalTextWidth = 0;

		for (int i = 0; i < messages.length; i++) {
			rawTextWidths[i] = Minecraft.getInstance().font.width(messages[i]);
			totalTextWidth += GuiHelper.DEFAULT_PADDING * 2 + rawTextWidths[i];
		}

		final int extraPadding = Math.ceilDiv(Math.max(0, minWidth - totalTextWidth), messages.length * 2);
		buttons = new BetterButtonWidget[messages.length];
		int totalWidgetWidth = 0;

		for (int i = 0; i < messages.length; i++) {
			final int newWidth = rawTextWidths[i] + (GuiHelper.DEFAULT_PADDING + extraPadding) * 2;
			final int index = i;
			buttons[i] = new BetterButtonWidget(null, messages[i], newWidth, () -> onClick.accept(index));
			totalWidgetWidth += newWidth;
		}

		this.totalWidgetWidth = totalWidgetWidth;
		setSize(totalWidgetWidth, GuiHelper.DEFAULT_LINE_SIZE);
	}

	@Override
	public void init(Consumer<ClickableWidgetBase> addRenderableWidget) {
		for (final BetterButtonWidget button : buttons) {
			addRenderableWidget.accept(button);
		}
	}

	@Override
	protected void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
		setSize(totalWidgetWidth, GuiHelper.DEFAULT_LINE_SIZE);
		int x = getX();
		for (BetterButtonWidget button : buttons) {
			button.active = active;
			button.visible = visible;
			button.setPosition(x, getY());
			button.renderWidget(context, mouseX, mouseY, delta);
			x += button.getWidth();
		}
	}
}
