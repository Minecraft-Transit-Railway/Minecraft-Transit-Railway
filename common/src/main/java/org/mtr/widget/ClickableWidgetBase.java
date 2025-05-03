package org.mtr.widget;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public abstract class ClickableWidgetBase extends ClickableWidget {

	public ClickableWidgetBase() {
		super(0, 0, 0, 0, Text.empty());
	}

	public void init(Consumer<ClickableWidgetBase> addDrawableChild) {
	}

	@Override
	protected final void appendClickableNarrations(NarrationMessageBuilder builder) {
	}
}
