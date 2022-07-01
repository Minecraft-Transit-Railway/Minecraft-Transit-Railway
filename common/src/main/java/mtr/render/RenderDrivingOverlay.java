package mtr.render;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.data.IGui;
import mtr.data.RailwayData;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

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

	public static void render(Object matrices) {
		render((PoseStack) matrices);
	}

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

		final String speedText = RailwayData.round(speed * 3.6F, 1) + " km/h";
		client.font.drawShadow(matrices, speedText, startX - client.font.width(speedText) - TEXT_PADDING, window.getGuiScaledHeight() - 14.5F, ARGB_WHITE);
		if (thisStation != null) {
			client.font.drawShadow(matrices, thisStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, window.getGuiScaledHeight() - 44.5F, ARGB_WHITE);
		}
		if (nextStation != null) {
			client.font.drawShadow(matrices, "> " + nextStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, window.getGuiScaledHeight() - 34.5F, ARGB_WHITE);
		}
		if (thisRoute != null) {
			client.font.drawShadow(matrices, thisRoute, startX + HOT_BAR_WIDTH + TEXT_PADDING, window.getGuiScaledHeight() - 19.5F, ARGB_WHITE);
		}
		if (lastStation != null) {
			client.font.drawShadow(matrices, "> " + lastStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, window.getGuiScaledHeight() - 9.5F, ARGB_WHITE);
		}

		RenderSystem.disableBlend();
		matrices.popPose();
	}

	public static void setData(int accelerationSign, float doorValue, float speed, int stopIndex, List<Long> routeIds) {
		RenderDrivingOverlay.accelerationSign = accelerationSign;
		RenderDrivingOverlay.doorValue = doorValue;
		coolDown = 2;
		RenderDrivingOverlay.speed = speed;
		RailwayData.useRoutesAndStationsFromIndex(stopIndex, routeIds, ClientData.DATA_CACHE, (currentStationIndex, thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
			RenderDrivingOverlay.thisStation = IGui.formatStationName(thisStation.name);
			RenderDrivingOverlay.nextStation = nextStation == null ? nextRoute == null ? null : IGui.formatStationName(nextRoute.name) : IGui.formatStationName(nextStation.name);
			RenderDrivingOverlay.thisRoute = IGui.formatStationName(thisRoute.name);
			RenderDrivingOverlay.lastStation = lastStation == null ? null : IGui.formatStationName(lastStation.name);
		});
	}
}
