package mtr;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public interface KeyMappings {

	KeyMapping LIFT_MENU = new KeyMapping("key.mtr.lift_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.mtr.keybinding");
	KeyMapping HIDE_RIDING_TRAIN_MODE = new KeyMapping("key.mtr.hide_riding_train_mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.mtr.keybinding");
	KeyMapping HIDE_ALL_TRAIN_TRACK_MODE = new KeyMapping("key.mtr.hide_all_train_track_mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.mtr.keybinding");
	KeyMapping HIDE_ALL_TRAIN_MODE = new KeyMapping("key.mtr.hide_all_train_mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.mtr.keybinding");
	KeyMapping HIDE_ALL_TRACK_MODE = new KeyMapping("key.mtr.hide_all_track_mode", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UNKNOWN, "category.mtr.keybinding");
}
