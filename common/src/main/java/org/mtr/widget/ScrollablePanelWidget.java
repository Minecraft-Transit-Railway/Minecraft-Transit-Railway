package org.mtr.widget;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;
import org.mtr.core.tool.Utilities;
import org.mtr.tool.GuiHelper;

public abstract class ScrollablePanelWidget extends ScrollableWidget {

	public ScrollablePanelWidget() {
		super(0, 0, 0, 0, Text.empty());
	}

	@Override
	public final void onClick(double mouseX, double mouseY) {
		if (!checkScrollbarDragged(mouseX, mouseY, 0)) {
			onClickNew(mouseX, mouseY);
		}
	}

	@Override
	protected final void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
		setScrollY(Math.clamp(getScrollY(), 0, Math.max(0, getContentsHeightWithPadding() - height)));
		context.enableScissor(getX(), getY(), getX() + width, getY() + height);
		render(context, active ? mouseX : -1, active ? mouseY : -1);
		context.disableScissor();
		drawScrollbar(context, active ? mouseX : -1, active ? mouseY : -1);
	}

	@Override
	protected final boolean isValidClickButton(int button) {
		return active && visible && super.isValidClickButton(button);
	}

	@Override
	protected final double getDeltaYPerScroll() {
		return GuiHelper.DEFAULT_LINE_SIZE;
	}

	@Override
	protected void appendClickableNarrations(NarrationMessageBuilder builder) {
	}

	/**
	 * Do not call this directly! Use {@link ScrollablePanelWidget#onClick} instead.
	 */
	protected void onClickNew(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
	}

	protected final int getScrollbarWidth() {
		return overflows() ? SCROLLBAR_WIDTH : 0;
	}

	/**
	 * Do not call this directly! Use {@link ScrollablePanelWidget#renderWidget} instead.
	 */
	protected abstract void render(DrawContext context, int mouseX, int mouseY);

	private void drawScrollbar(DrawContext context, int mouseX, int mouseY) {
		if (overflows()) {
			final int x1 = getScrollbarX();
			final int y1 = getScrollbarThumbY();
			final int x2 = x1 + SCROLLBAR_WIDTH;
			final int y2 = y1 + getScrollbarThumbHeight();
			context.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, Utilities.isBetween(mouseX, x1, x2 - 1) && Utilities.isBetween(mouseY, y1, y2 - 1) ? GuiHelper.SCROLL_BAR_HOVER_COLOR : GuiHelper.SCROLL_BAR_COLOR);
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
