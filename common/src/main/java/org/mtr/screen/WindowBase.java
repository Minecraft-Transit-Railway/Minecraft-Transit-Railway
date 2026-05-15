package org.mtr.screen;

import gg.essential.elementa.ElementaVersion;
import gg.essential.elementa.WindowScreen;
import gg.essential.universal.UKeyboard;
import gg.essential.universal.UMinecraft;
import net.minecraft.client.MinecraftClient;
import org.jspecify.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public abstract class WindowBase extends WindowScreen {

	private boolean closeScreen = false;

	@Nullable
	private final WindowScreen previousScreen;
	@Nullable
	private final ScreenBase previousScreenLegacy;

	public WindowBase(@Nullable WindowScreen previousScreen) {
		super(ElementaVersion.V10);
		this.previousScreen = previousScreen;
		previousScreenLegacy = null;
	}

	@Deprecated
	public WindowBase(@Nullable ScreenBase previousScreenLegacy) {
		super(ElementaVersion.V10);
		previousScreen = null;
		this.previousScreenLegacy = previousScreenLegacy;
	}

	public WindowBase() {
		this((WindowScreen) null);
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
				MinecraftClient.getInstance().setScreen(previousScreenLegacy);
			} else {
				UMinecraft.setCurrentScreenObj(previousScreen);
			}
		}
	}

	/**
	 * Implicit override
	 */
	public final boolean shouldPause() {
		return false;
	}
}
