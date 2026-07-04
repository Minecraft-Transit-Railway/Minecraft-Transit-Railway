package org.mtr.widget;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.function.Consumer;

public abstract class ClickableWidgetBase extends AbstractWidget {

	public ClickableWidgetBase() {
		super(0, 0, 0, 0, Component.empty());
	}

	public void init(Consumer<ClickableWidgetBase> addRenderableWidget) {
	}

	@Override
	protected final void updateWidgetNarration(NarrationElementOutput builder) {
	}
}
