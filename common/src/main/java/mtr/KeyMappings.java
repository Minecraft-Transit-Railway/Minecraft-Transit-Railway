package mtr;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public interface KeyMappings {

	KeyMapping LIFT_MENU = new KeyMapping("key.mtr.lift_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.mtr.keybinding");
	KeyMapping TRAIN_ACCELERATE = new KeyMapping("key.mtr.train_accelerate", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_UP, "category.mtr.keybinding");
	KeyMapping TRAIN_BRAKE = new KeyMapping("key.mtr.train_brake", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, "category.mtr.keybinding");
	KeyMapping TRAIN_TOGGLE_DOORS = new KeyMapping("key.mtr.train_toggle_doors", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_LEFT, "category.mtr.keybinding");
	KeyMapping CUSTOM_RAIL_MENU = new KeyMapping("key.mtr.custom_rail_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_R, "category.mtr.keybinding");
}
