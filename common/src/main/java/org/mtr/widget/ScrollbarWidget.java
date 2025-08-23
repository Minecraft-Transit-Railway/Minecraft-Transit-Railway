package org.mtr.widget;

import lombok.Setter;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ScrollableWidget;

public final class ScrollbarWidget extends ScrollablePanelWidget {

	@Setter
	private int contentHeight;

	@Override
	protected void render(DrawContext context, int mouseX, int mouseY) {
		setWidth(ScrollableWidget.SCROLLBAR_WIDTH);
	}

	@Override
	protected int getContentsHeightWithPadding() {
		return contentHeight;
	}
}
