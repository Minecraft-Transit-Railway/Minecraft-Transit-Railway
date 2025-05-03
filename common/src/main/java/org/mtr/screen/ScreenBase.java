package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.mtr.widget.ClickableWidgetBase;

import javax.annotation.Nullable;

public abstract class ScreenBase extends Screen {

	@Nullable
	private final Screen previousScreen;

	public ScreenBase(@Nullable Screen previousScreen) {
		super(Text.empty());
		this.previousScreen = previousScreen;
	}

	public ScreenBase() {
		this(null);
	}

	@Override
	protected final <T extends Element & Drawable & Selectable> T addDrawableChild(T drawableElement) {
		if (drawableElement instanceof ClickableWidgetBase) {
			((ClickableWidgetBase) drawableElement).init(this::addSelectableChild);
		}
		return super.addDrawableChild(drawableElement);
	}

	@Override
	protected final <T extends Element & Selectable> T addSelectableChild(T drawableElement) {
		if (drawableElement instanceof ClickableWidgetBase) {
			((ClickableWidgetBase) drawableElement).init(this::addSelectableChild);
		}
		return super.addSelectableChild(drawableElement);
	}

	@Override
	public void close() {
		super.close();
		MinecraftClient.getInstance().setScreen(previousScreen);
	}
}
