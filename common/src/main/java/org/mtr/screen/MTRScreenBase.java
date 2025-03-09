package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import javax.annotation.Nullable;

public class MTRScreenBase extends Screen {

	@Nullable
	private final Screen previousScreen;

	public MTRScreenBase(@Nullable Screen previousScreen) {
		super(Text.empty());
		this.previousScreen = previousScreen;
	}

	public MTRScreenBase() {
		this(null);
	}

	@Override
	public void close() {
		super.close();
		MinecraftClient.getInstance().setScreen(previousScreen);
	}
}
