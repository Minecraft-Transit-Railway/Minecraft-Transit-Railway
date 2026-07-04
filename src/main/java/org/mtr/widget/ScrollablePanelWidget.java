package org.mtr.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.mtr.core.tool.Utilities;
import org.mtr.tool.GuiHelper;

public abstract class ScrollablePanelWidget extends AbstractWidget {

	protected double scrollAmount;
	private boolean scrolling;

	private static final int SCROLLBAR_WIDTH = 6;

	public ScrollablePanelWidget() {
		super(0, 0, 0, 0, Component.empty());
	}

	@Override
	public final void onClick(double mouseX, double mouseY) {
		if (!clickedScrollbar(mouseX, mouseY)) {
			onClickNew(mouseX, mouseY);
		}
	}

	@Override
	protected final void renderWidget(GuiGraphics context, int mouseX, int mouseY, float delta) {
		final int h = height;
		scrollAmount = Math.clamp(scrollAmount, 0, Math.max(0, contentHeight() - h));
		context.enableScissor(getX(), getY(), getX() + width, getY() + h);
		render(context, active ? mouseX : -1, active ? mouseY : -1);
		context.disableScissor();
		drawScrollbar(context, active ? mouseX : -1, active ? mouseY : -1);
	}

	@Override
	protected final boolean isValidClickButton(int button) {
		return active && visible && super.isValidClickButton(button);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (isValidClickButton(button)) {
			if (clickedScrollbar(mouseX, mouseY)) {
				scrolling = true;
				return true;
			}
			if (isMouseOver(mouseX, mouseY)) {
				playDownSound(Minecraft.getInstance().getSoundManager());
				onClick(mouseX, mouseY);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
		if (scrolling && button == 0) {
			final int h = height - scrollerHeight();
			if (h > 0) {
				scrollAmount = Math.clamp((mouseY - getY() - scrollerHeight() / 2.0) / h * maxScroll(), 0, maxScroll());
			}
			return true;
		}
		return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
		if (isMouseOver(mouseX, mouseY)) {
			scrollAmount = Math.clamp(scrollAmount - scrollY * GuiHelper.DEFAULT_LINE_SIZE, 0, Math.max(0, contentHeight() - height));
			return true;
		}
		return false;
	}

	@Override
	protected void updateWidgetNarration(NarrationElementOutput builder) {
	}

	protected void onClickNew(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
	}

	protected final int getScrollbarWidth() {
		return scrollbarVisible() ? SCROLLBAR_WIDTH : 0;
	}

	protected abstract void render(GuiGraphics context, int mouseX, int mouseY);

	protected abstract int contentHeight();

	private double maxScroll() {
		return Math.max(0, contentHeight() - height);
	}

	private boolean scrollbarVisible() {
		return maxScroll() > 0;
	}

	private int scrollBarX() {
		return getX() + width - SCROLLBAR_WIDTH;
	}

	private int scrollBarY() {
		final double max = maxScroll();
		if (max <= 0) {
			return getY();
		}
		return getY() + (int) (scrollAmount / max * (height - scrollerHeight()));
	}

	private int scrollerHeight() {
		final int h = height;
		final int contentH = contentHeight();
		if (contentH <= 0) {
			return h;
		}
		return Math.max(32, (int) (h * h / (double) contentH));
	}

	private boolean clickedScrollbar(double mouseX, double mouseY) {
		return scrollbarVisible() && Utilities.isBetween(mouseX, scrollBarX(), scrollBarX() + SCROLLBAR_WIDTH) && Utilities.isBetween(mouseY, getY(), getY() + height);
	}

	private void drawScrollbar(GuiGraphics context, int mouseX, int mouseY) {
		if (scrollbarVisible()) {
			final int x1 = scrollBarX();
			final int y1 = scrollBarY();
			final int x2 = x1 + SCROLLBAR_WIDTH;
			final int y2 = y1 + scrollerHeight();
			context.fill(x1 + 1, y1 + 1, x2 - 1, y2 - 1, Utilities.isBetween(mouseX, x1, x2 - 1) && Utilities.isBetween(mouseY, y1, y2 - 1) ? GuiHelper.SCROLL_BAR_HOVER_COLOR : GuiHelper.SCROLL_BAR_COLOR);
		}
	}
}
