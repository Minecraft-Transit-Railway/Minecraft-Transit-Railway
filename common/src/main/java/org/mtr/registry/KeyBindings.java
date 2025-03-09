package org.mtr.registry;

import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;
import org.mtr.MTR;
import org.mtr.generated.lang.TranslationProvider;

public final class KeyBindings {

	static {
		LIFT_MENU = registerKeyBinding(TranslationProvider.KEY_MTR_LIFT_MENU.key, GLFW.GLFW_KEY_Z, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_ACCELERATE = registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_ACCELERATE.key, GLFW.GLFW_KEY_UP, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_BRAKE = registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_BRAKE.key, GLFW.GLFW_KEY_DOWN, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_TOGGLE_DOORS = registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_TOGGLE_DOORS.key, GLFW.GLFW_KEY_LEFT, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
	}

	public static final KeyBinding LIFT_MENU;
	public static final KeyBinding TRAIN_ACCELERATE;
	public static final KeyBinding TRAIN_BRAKE;
	public static final KeyBinding TRAIN_TOGGLE_DOORS;

	public static void init() {
		MTR.LOGGER.info("Registering Minecraft Transit Railway key bindings");
	}

	private static KeyBinding registerKeyBinding(String translationKey, int code, String category) {
		final KeyBinding keyBinding = new KeyBinding(translationKey, code, category);
		RegistryClient.registerKeyBinding(keyBinding);
		return keyBinding;
	}
}
