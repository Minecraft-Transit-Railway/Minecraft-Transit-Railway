package org.mtr.mod.screen;

import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.ScreenExtension;

import javax.annotation.Nullable;

public class MTRScreenBase extends ScreenExtension {

	@Nullable
	private final Screen previousScreen;

	public MTRScreenBase(@Nullable ScreenExtension previousScreenExtension) {
		super();
		this.previousScreen = previousScreenExtension == null ? null : new Screen(previousScreenExtension);
	}

	public MTRScreenBase(@Nullable Screen previousScreen) {
		super();
		this.previousScreen = previousScreen;
	}

	public MTRScreenBase() {
		this((ScreenExtension) null);
	}

	@Override
	public void onClose2() {
		super.onClose2();
		MinecraftClient.getInstance().openScreen(previousScreen);
	}
}
