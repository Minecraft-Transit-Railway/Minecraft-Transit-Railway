package org.mtr.mod;

import org.lwjgl.glfw.GLFW;
import org.mtr.mapping.holder.KeyBinding;

public final class KeyBindings {

	static {
		LIFT_MENU = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.lift_menu", GLFW.GLFW_KEY_Z, "category.mtr.keybinding");
		TRAIN_ACCELERATE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.train_accelerate", GLFW.GLFW_KEY_UP, "category.mtr.keybinding");
		TRAIN_BRAKE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.train_brake", GLFW.GLFW_KEY_DOWN, "category.mtr.keybinding");
		TRAIN_TOGGLE_DOORS = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.train_toggle_doors", GLFW.GLFW_KEY_LEFT, "category.mtr.keybinding");
		DEBUG_1_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_1_negative", GLFW.GLFW_KEY_KP_4, "category.mtr.keybinding");
		DEBUG_2_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_2_negative", GLFW.GLFW_KEY_KP_5, "category.mtr.keybinding");
		DEBUG_3_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_3_negative", GLFW.GLFW_KEY_KP_6, "category.mtr.keybinding");
		DEBUG_1_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_1_positive", GLFW.GLFW_KEY_KP_7, "category.mtr.keybinding");
		DEBUG_2_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_2_positive", GLFW.GLFW_KEY_KP_8, "category.mtr.keybinding");
		DEBUG_3_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_3_positive", GLFW.GLFW_KEY_KP_9, "category.mtr.keybinding");
		DEBUG_ROTATE_CATEGORY_NEGATIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_cycle_negative", GLFW.GLFW_KEY_KP_SUBTRACT, "category.mtr.keybinding");
		DEBUG_ROTATE_CATEGORY_POSITIVE = InitClient.REGISTRY_CLIENT.registerKeyBinding("key.mtr.debug_cycle_positive", GLFW.GLFW_KEY_KP_ADD, "category.mtr.keybinding");
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
