package org.mtr.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;

public abstract class WidgetScrollablePanel extends ScrollableWidget {

	private static final int SCROLL_BAR_COLOR = 0xFF444444;
	private static final int SCROLL_BAR_HOVER_COLOR = 0xFF888888;

	public WidgetScrollablePanel(int x, int y, int width, int height) {
		super(x, y, width, height, Text.literal(""));
	}

	@Override
	public final boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (checkScrollbarDragged(mouseX, mouseY, button)) {
			return true;
		} else {
			return mouseClickedNew(mouseX, mouseY, button);
		}
	}

	@Override
	protected final void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		context.enableScissor(getX(), getY(), getX() + width, getY() + height);
		render(context, mouseX, mouseY);
		context.disableScissor();
		drawScrollbar(context, mouseX, mouseY);
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	/**
	 * Do not call this directly! Use {@link WidgetScrollablePanel#mouseClicked} instead.
	 */
	protected boolean mouseClickedNew(double mouseX, double mouseY, int button) {
		return super.mouseClicked(mouseX, mouseY, button);
	}

	protected final int getScrollbarWidth() {
		return overflows() ? SCROLLBAR_WIDTH : 0;
	}

	/**
	 * Do not call this directly! Use {@link WidgetScrollablePanel#renderWidget} instead.
	 */
	protected abstract void render(DrawContext context, int mouseX, int mouseY);

	private void drawScrollbar(DrawContext context, int mouseX, int mouseY) {
		if (overflows()) {
			final int x1 = getScrollbarX();
			final int y1 = getScrollbarThumbY();
			final int x2 = x1 + SCROLLBAR_WIDTH;
			final int y2 = y1 + getScrollbarThumbHeight();
			context.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, Utilities.isBetween(mouseX, x1, x2) && Utilities.isBetween(mouseY, y1, y2) ? SCROLL_BAR_HOVER_COLOR : SCROLL_BAR_COLOR);
		}
	}

	/**
	 * @deprecated This does nothing now!
	 */
	@Deprecated
	@Override
	protected final void drawScrollbar(DrawContext context) {
	}
}
