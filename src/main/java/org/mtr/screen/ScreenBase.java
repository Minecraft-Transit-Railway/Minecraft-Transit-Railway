package org.mtr.screen;

import gg.essential.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.Nullable;
import org.mtr.widget.ClickableWidgetBase;

public abstract class ScreenBase extends Screen {

	@Nullable
	private final WindowBase previousScreen;
	@Nullable
	private final Screen previousScreenLegacy;

	public ScreenBase(@Nullable WindowBase previousScreen) {
		super(Component.empty());
		this.previousScreen = previousScreen;
		this.previousScreenLegacy = null;
	}

	public ScreenBase(@Nullable Screen previousScreenLegacy) {
		super(Component.empty());
		this.previousScreen = null;
		this.previousScreenLegacy = previousScreenLegacy;
	}

	public ScreenBase() {
		this((Screen) null);
	}

	@Override
	protected final <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T drawableElement) {
		if (drawableElement instanceof ClickableWidgetBase) {
			((ClickableWidgetBase) drawableElement).init(this::addWidget);
		}
		return super.addRenderableWidget(drawableElement);
	}

	@Override
	protected final <T extends GuiEventListener & NarratableEntry> T addWidget(T drawableElement) {
		if (drawableElement instanceof ClickableWidgetBase) {
			((ClickableWidgetBase) drawableElement).init(this::addWidget);
		}
		return super.addWidget(drawableElement);
	}

	@Override
	public void onClose() {
		super.onClose();
		if (previousScreen != null) {
			UMinecraft.setCurrentScreenObj(previousScreen);
		} else {
			Minecraft.getInstance().setScreen(previousScreenLegacy);
		}
	}

	@Override
	public final boolean isPauseScreen() {
		return false;
	}
}
