package mtr;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public interface KeyMappings {

	KeyMapping LIFT_MENU = new KeyMapping("key.mtr.lift_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_Z, "category.mtr.keybinding");
}
