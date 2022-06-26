package mtr.render;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class RenderDrivingOverlay implements IGui {

	private static int accelerationSign;
	private static float doorValue;
	private static int coolDown;

	private static final int HOT_BAR_WIDTH = 182;
	private static final int HOT_BAR_HEIGHT = 22;

	public static void render(PoseStack matrices) {
		if (coolDown > 0) {
			coolDown--;
		} else {
			return;
		}

		final Minecraft client = Minecraft.getInstance();
		final LocalPlayer player = client.player;
		final Window window = client.getWindow();
		if (window == null || player == null) {
			return;
		}

		matrices.pushPose();
		RenderSystem.enableBlend();
		UtilitiesClient.beginDrawingTexture(new ResourceLocation("textures/gui/widgets.png"));
		final int startX = (window.getGuiScaledWidth() - HOT_BAR_WIDTH) / 2;
		final int startY = window.getGuiScaledHeight() - (player.isCreative() ? 47 : 63);

		GuiComponent.blit(matrices, startX, startY, 0, 0, 0, 61, HOT_BAR_HEIGHT, 256, 256);
		GuiComponent.blit(matrices, startX + 61, startY, 0, 141, 0, 41, HOT_BAR_HEIGHT, 256, 256);
		GuiComponent.blit(matrices, startX + 120, startY, 0, 0, 0, 21, HOT_BAR_HEIGHT, 256, 256);
		GuiComponent.blit(matrices, startX + 141, startY, 0, 141, 0, 41, HOT_BAR_HEIGHT, 256, 256);

		GuiComponent.blit(matrices, startX + 39 + accelerationSign * 20, startY - 1, 0, 0, 22, 24, 24, 256, 256);
		GuiComponent.blit(matrices, startX + (doorValue > 0 ? doorValue < 1 ? 139 : 159 : 119), startY - 1, 0, 0, 22, 24, 24, 256, 256);

		client.font.drawShadow(matrices, "B2", startX + 5.5F, startY + 7.5F, accelerationSign == -2 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, "B1", startX + 25.5F, startY + 7.5F, accelerationSign == -1 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, "N", startX + 48.5F, startY + 7.5F, accelerationSign == 0 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, "P1", startX + 65.5F, startY + 7.5F, accelerationSign == 1 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, "P2", startX + 85.5F, startY + 7.5F, accelerationSign == 2 ? ARGB_WHITE : ARGB_GRAY);

		client.font.drawShadow(matrices, "DC", startX + 125.5F, startY + 7.5F, doorValue == 0 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, String.valueOf(Math.round(doorValue * 10) / 10F), startX + 144.5F, startY + 7.5F, doorValue > 0 && doorValue < 1 ? ARGB_WHITE : ARGB_GRAY);
		client.font.drawShadow(matrices, "DO", startX + 165.5F, startY + 7.5F, doorValue == 1 ? ARGB_WHITE : ARGB_GRAY);

		RenderSystem.disableBlend();
		matrices.popPose();
	}

	public static void setData(int accelerationSign, float doorValue) {
		RenderDrivingOverlay.accelerationSign = accelerationSign;
		RenderDrivingOverlay.doorValue = doorValue;
		coolDown = 2;
	}
}
