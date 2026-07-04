package org.mtr.screen;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.WindowScreen;
import gg.essential.universal.UKeyboard;
import gg.essential.universal.UMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public abstract class WindowBase extends WindowScreen {

	private boolean closeScreen = false;

	@Nullable
	private final WindowScreen previousScreen;
	@Nullable
	private final Screen previousScreenLegacy;

	public WindowBase(@Nullable WindowScreen previousScreen) {
		super(ElementaVersion.V11);
		this.previousScreen = previousScreen;
		previousScreenLegacy = null;
	}

	@Deprecated
	public WindowBase(@Nullable Screen previousScreenLegacy) {
		super(ElementaVersion.V10);
		previousScreen = null;
		this.previousScreenLegacy = previousScreenLegacy;
	}

	public WindowBase() {
		this(null);
	}

	@Override
	public void onKeyPressed(int keyCode, char typedChar, UKeyboard.@Nullable Modifiers modifiers) {
		if (keyCode != GLFW.GLFW_KEY_ESCAPE) {
			super.onKeyPressed(keyCode, typedChar, modifiers);
		}
	}

	@Override
	public void onKeyReleased(int keyCode, char typedChar, UKeyboard.@Nullable Modifiers modifiers) {
		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			closeScreen = true;
		} else {
			super.onKeyReleased(keyCode, typedChar, modifiers);
		}
	}

	@Override
	public void onTick() {
		super.onTick();
		if (closeScreen) {
			if (previousScreen == null) {
				Minecraft.getInstance().setScreen(previousScreenLegacy);
			} else {
				UMinecraft.setCurrentScreenObj(previousScreen);
			}
		}
	}

	@Override
	public final boolean isPauseScreen() {
		return false;
	}

	public final void markScreenForClose() {
		closeScreen = true;
	}
}
