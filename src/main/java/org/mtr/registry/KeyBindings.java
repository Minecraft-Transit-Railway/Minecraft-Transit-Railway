package org.mtr.registry;

import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;
import org.mtr.MTR;
import org.mtr.generated.lang.TranslationProvider;
//? if >= 26.1 {
/*import net.minecraft.resources.ResourceLocation;
import org.mtr.MTR;
*///? }

public final class KeyBindings {

	static {
		LIFT_MENU = registerKeyBinding(TranslationProvider.KEY_MTR_LIFT_MENU.key, GLFW.GLFW_KEY_Z, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_ACCELERATE = registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_ACCELERATE.key, GLFW.GLFW_KEY_UP, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_BRAKE = registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_BRAKE.key, GLFW.GLFW_KEY_DOWN, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
		TRAIN_TOGGLE_DOORS = registerKeyBinding(TranslationProvider.KEY_MTR_TRAIN_TOGGLE_DOORS.key, GLFW.GLFW_KEY_LEFT, TranslationProvider.CATEGORY_MTR_KEYBINDING.key);
	}

	public static final KeyMapping LIFT_MENU;
	public static final KeyMapping TRAIN_ACCELERATE;
	public static final KeyMapping TRAIN_BRAKE;
	public static final KeyMapping TRAIN_TOGGLE_DOORS;

	public static void init() {
		MTR.LOGGER.info("Registering Minecraft Transit Railway key bindings");
	}

//? if >= 26.1 {
	/*// Categories are registered objects now rather than translation keys, so the mod registers its
	// own once and every binding shares it. The category argument is kept so that the callers read
	// the same on both versions, but it is the identifier below that names the category from 26.1,
	// and its label comes from a different translation key than the one those callers pass.
	private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ResourceLocation.fromNamespaceAndPath(MTR.MOD_ID, "keybinding"));

	private static KeyMapping registerKeyBinding(String translationKey, int code, String category) {
		final KeyMapping keyBinding = new KeyMapping(translationKey, code, CATEGORY);
*///? } else {
	private static KeyMapping registerKeyBinding(String translationKey, int code, String category) {
		final KeyMapping keyBinding = new KeyMapping(translationKey, code, category);
//? }
		RegistryClient.registerKeyBinding(keyBinding);
		return keyBinding;
	}
}
