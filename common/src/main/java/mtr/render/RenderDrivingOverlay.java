package mtr.render;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import mtr.data.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

public class RenderDrivingOverlay implements IGui {

	private static int accelerationSign;
	private static float doorValue;
	private static float speed;
	private static String thisStation;
	private static String nextStation;
	private static String thisRoute;
	private static String lastStation;
	private static int coolDown;

	private static final int HOT_BAR_WIDTH = 182;
	private static final int HOT_BAR_HEIGHT = 22;

	public static void render(GuiGraphics guiGraphics) {
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

		guiGraphics.pose().pushPose();
		RenderSystem.enableBlend();
		final ResourceLocation resourceLocation = new ResourceLocation("textures/gui/widgets.png");
		final int startX = (window.getGuiScaledWidth() - HOT_BAR_WIDTH) / 2;
		final int startY = window.getGuiScaledHeight() - (player.isCreative() ? 47 : 63);

		guiGraphics.blit(resourceLocation, startX, startY, 0, 0, 0, 61, HOT_BAR_HEIGHT, 256, 256);
		guiGraphics.blit(resourceLocation, startX + 61, startY, 0, 141, 0, 41, HOT_BAR_HEIGHT, 256, 256);
		guiGraphics.blit(resourceLocation, startX + 120, startY, 0, 0, 0, 21, HOT_BAR_HEIGHT, 256, 256);
		guiGraphics.blit(resourceLocation, startX + 141, startY, 0, 141, 0, 41, HOT_BAR_HEIGHT, 256, 256);

		guiGraphics.blit(resourceLocation, startX + 39 + Math.max(accelerationSign, -2) * 20, startY - 1, 0, 0, 22, 24, 24, 256, 256);
		guiGraphics.blit(resourceLocation, startX + (doorValue > 0 ? doorValue < 1 ? 139 : 159 : 119), startY - 1, 0, 0, 22, 24, 24, 256, 256);

		guiGraphics.drawString(client.font, "B2", (int) (startX + 5.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == -2 ? ARGB_WHITE : ARGB_GRAY, true);
		guiGraphics.drawString(client.font, "B1", (int) (startX + 25.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == -1 ? ARGB_WHITE : ARGB_GRAY, true);
		guiGraphics.drawString(client.font, "N", (int) (startX + 48.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 0 ? ARGB_WHITE : ARGB_GRAY, true);
		guiGraphics.drawString(client.font, "P1", (int) (startX + 65.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 1 ? ARGB_WHITE : ARGB_GRAY, true);
		guiGraphics.drawString(client.font, "P2", (int) (startX + 85.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 2 ? ARGB_WHITE : ARGB_GRAY, true);

		guiGraphics.drawString(client.font, "DC", (int) (startX + 125.5F), (int) (startY + 7.5F), speed == 0 && doorValue == 0 ? ARGB_WHITE : ARGB_GRAY, true);
		guiGraphics.drawString(client.font, String.valueOf(Math.round(doorValue * 10) / 10F), (int) (startX + 144.5F), (int) (startY + 7.5F), doorValue > 0 && doorValue < 1 ? ARGB_WHITE : ARGB_GRAY, true);
		guiGraphics.drawString(client.font, "DO", (int) (startX + 165.5F), (int) (startY + 7.5F), speed == 0 && doorValue == 1 ? ARGB_WHITE : ARGB_GRAY, true);

		final String speedText = RailwayData.round(speed * 3.6F, 1) + " km/h";
		guiGraphics.drawString(client.font, speedText, startX - client.font.width(speedText) - TEXT_PADDING, (int) (window.getGuiScaledHeight() - 14.5F), ARGB_WHITE, true);
		if (thisStation != null) {
			guiGraphics.drawString(client.font, thisStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getGuiScaledHeight() - 44.5F), ARGB_WHITE, true);
		}
		if (nextStation != null) {
			guiGraphics.drawString(client.font, "> " + nextStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getGuiScaledHeight() - 34.5F), ARGB_WHITE, true);
		}
		if (thisRoute != null) {
			guiGraphics.drawString(client.font, thisRoute, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getGuiScaledHeight() - 19.5F), ARGB_WHITE, true);
		}
		if (lastStation != null) {
			guiGraphics.drawString(client.font, "> " + lastStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getGuiScaledHeight() - 9.5F), ARGB_WHITE, true);
		}

		RenderSystem.disableBlend();
		guiGraphics.pose().popPose();
	}

	public static void setData(int accelerationSign, TrainClient trainClient) {
		RenderDrivingOverlay.accelerationSign = accelerationSign;
		RenderDrivingOverlay.doorValue = trainClient.getDoorValue();
		coolDown = 2;
		RenderDrivingOverlay.speed = trainClient.getSpeed() * 20;
		final Route thisRoute = trainClient.getThisRoute();
		final Route nextRoute = trainClient.getNextRoute();
		final Station thisStation = trainClient.getThisStation();
		final Station nextStation = trainClient.getNextStation();
		final Station lastStation = trainClient.getLastStation();
		RenderDrivingOverlay.thisStation = thisStation == null ? null : IGui.formatStationName(thisStation.name);
		RenderDrivingOverlay.nextStation = nextStation == null ? nextRoute == null ? null : IGui.formatStationName(nextRoute.name) : IGui.formatStationName(nextStation.name);
		RenderDrivingOverlay.thisRoute = thisRoute == null ? null : IGui.formatStationName(thisRoute.name);
		RenderDrivingOverlay.lastStation = lastStation == null ? null : IGui.formatStationName(lastStation.name);
	}
}
