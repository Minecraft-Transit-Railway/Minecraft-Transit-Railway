package org.mtr.mod;

import org.lwjgl.glfw.GLFW;
import org.mtr.mapping.holder.KeyBinding;
import org.mtr.mod.generated.lang.TranslationProvider;

public final class KeyBindings {

	static {
		LIFT_MENU = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_LIFT_MENU.key, GLFW.GLFW_KEY_Z, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_ACCELERATE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_ACCELERATE.key, GLFW.GLFW_KEY_UP, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_BRAKE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_BRAKE.key, GLFW.GLFW_KEY_DOWN, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_TOGGLE_DOORS = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_TOGGLE_DOORS.key, GLFW.GLFW_KEY_LEFT, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_1_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_1_NEGATIVE.key, GLFW.GLFW_KEY_KP_4, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_2_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_2_NEGATIVE.key, GLFW.GLFW_KEY_KP_5, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_3_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_3_NEGATIVE.key, GLFW.GLFW_KEY_KP_6, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_1_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_1_POSITIVE.key, GLFW.GLFW_KEY_KP_7, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_2_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_2_POSITIVE.key, GLFW.GLFW_KEY_KP_8, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_3_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_3_POSITIVE.key, GLFW.GLFW_KEY_KP_9, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_ROTATE_CATEGORY_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_CYCLE_NEGATIVE.key, GLFW.GLFW_KEY_KP_SUBTRACT, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		DEBUG_ROTATE_CATEGORY_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding(TranslationProvider.KEY_MTR_DEBUG_CYCLE_POSITIVE.key, GLFW.GLFW_KEY_KP_ADD, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
	}

	public static final KeyBinding LIFT_MENU;
	public static final KeyBinding TRAIN_ACCELERATE;
	public static final KeyBinding TRAIN_BRAKE;
	public static final KeyBinding TRAIN_TOGGLE_DOORS;
	public static final KeyBinding DEBUG_1_NEGATIVE;
	public static final KeyBinding DEBUG_2_NEGATIVE;
	public static final KeyBinding DEBUG_3_NEGATIVE;
	public static final KeyBinding DEBUG_1_POSITIVE;
	public static final KeyBinding DEBUG_2_POSITIVE;
	public static final KeyBinding DEBUG_3_POSITIVE;
	public static final KeyBinding DEBUG_ROTATE_CATEGORY_NEGATIVE;
	public static final KeyBinding DEBUG_ROTATE_CATEGORY_POSITIVE;

	public static void init() {
		Init.LOGGER.info("Registering Minecraft Transit Railway key bindings");
	}
}
