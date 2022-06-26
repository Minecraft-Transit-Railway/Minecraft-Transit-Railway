package mtr.render;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import net.minecraft.client.Minecraft;

public class RenderDrivingOverlay implements IGui {

	private static int accelerationSign;
	private static float doorValue;
	private static int coolDown;

	public static void render(PoseStack matrices) {
		if (coolDown > 0) {
			coolDown--;
		} else {
			return;
		}

		final Minecraft client = Minecraft.getInstance();
		final Window window = client.getWindow();
		if (window == null) {
			return;
		}

		client.font.drawShadow(matrices, "P", window.getGuiScaledWidth() - 12, 8, accelerationSign > 0 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, "N", window.getGuiScaledWidth() - 12, 20, accelerationSign == 0 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, "B", window.getGuiScaledWidth() - 12, 32, accelerationSign < 0 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, doorValue > 0 ? "DO" : "DC", window.getGuiScaledWidth() - 18, 48, doorValue < 1 && doorValue > 0 ? ARGB_WHITE : ARGB_GRAY);
	}

	public static void setData(int accelerationSign, float doorValue) {
		RenderDrivingOverlay.accelerationSign = accelerationSign;
		RenderDrivingOverlay.doorValue = doorValue;
		coolDown = 2;
	}
}
