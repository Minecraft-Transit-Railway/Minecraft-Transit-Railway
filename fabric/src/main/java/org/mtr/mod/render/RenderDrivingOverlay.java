package org.mtr.mod.render;

import com.mojang.blaze3d.systems.RenderSystem;
import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.Window;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.GuiDrawing;
import org.mtr.mod.data.IGui;

public class RenderDrivingOverlay implements IGui {

	private static int accelerationSign;
	private static float doorValue;
	private static float speed;
	private static String thisStation;
	private static String nextStation;
	private static String thisRoute;
	private static String lastStation;
	private static int cooldown;

	private static final int HOT_BAR_WIDTH = 182;
	private static final int HOT_BAR_HEIGHT = 22;

	public static void render(GraphicsHolder graphicsHolder) {
		if (cooldown > 0) {
			cooldown--;
		} else {
			return;
		}

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
		final Window window = minecraftClient.getWindow();
		if (clientPlayerEntity == null) {
			return;
		}

		graphicsHolder.push();
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingTexture(new Identifier("textures/gui/widgets.png"));
		final int startX = (window.getScaledWidth() - HOT_BAR_WIDTH) / 2;
		final int startY = window.getScaledHeight() - (clientPlayerEntity.isCreative() ? 47 : 63);

		IGui.drawTexture(guiDrawing, startX, startY, 0F, 0, 61, HOT_BAR_HEIGHT, 256, 256);
		IGui.drawTexture(guiDrawing, startX + 61, startY, 141F, 0, 41, HOT_BAR_HEIGHT, 256, 256);
		IGui.drawTexture(guiDrawing, startX + 120, startY, 0F, 0, 21, HOT_BAR_HEIGHT, 256, 256);
		IGui.drawTexture(guiDrawing, startX + 141, startY, 141F, 0, 41, HOT_BAR_HEIGHT, 256, 256);

		IGui.drawTexture(guiDrawing, startX + 39 + Math.max(accelerationSign, -2) * 20, startY - 1, 0F, 22, 24, 24, 256, 256);
		IGui.drawTexture(guiDrawing, startX + (doorValue > 0 ? doorValue < 1 ? 139 : 159 : 119), startY - 1, 0F, 22, 24, 24, 256, 256);

		guiDrawing.finishDrawingTexture();

		graphicsHolder.drawCenteredText("B2", (int) (startX + 5.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == -2 ? ARGB_WHITE : ARGB_GRAY);
		graphicsHolder.drawCenteredText("B1", (int) (startX + 25.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == -1 ? ARGB_WHITE : ARGB_GRAY);
		graphicsHolder.drawCenteredText("N", (int) (startX + 48.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 0 ? ARGB_WHITE : ARGB_GRAY);
		graphicsHolder.drawCenteredText("P1", (int) (startX + 65.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 1 ? ARGB_WHITE : ARGB_GRAY);
		graphicsHolder.drawCenteredText("P2", (int) (startX + 85.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 2 ? ARGB_WHITE : ARGB_GRAY);

		graphicsHolder.drawCenteredText("DC", (int) (startX + 125.5F), (int) (startY + 7.5F), speed == 0 && doorValue == 0 ? ARGB_WHITE : ARGB_GRAY);
		graphicsHolder.drawCenteredText(String.valueOf(Math.round(doorValue * 10) / 10F), (int) (startX + 144.5F), (int) (startY + 7.5F), doorValue > 0 && doorValue < 1 ? ARGB_WHITE : ARGB_GRAY);
		graphicsHolder.drawCenteredText("DO", (int) (startX + 165.5F), (int) (startY + 7.5F), speed == 0 && doorValue == 1 ? ARGB_WHITE : ARGB_GRAY);

		final String speedText = Utilities.round(speed * 3.6F, 1) + " km/h";
		graphicsHolder.drawText(speedText, startX - GraphicsHolder.getTextWidth(speedText) - TEXT_PADDING, (int) (window.getScaledHeight() - 14.5F), ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
		if (thisStation != null) {
			graphicsHolder.drawText(thisStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 44.5F), ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
		}
		if (nextStation != null) {
			graphicsHolder.drawText("> " + nextStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 34.5F), ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
		}
		if (thisRoute != null) {
			graphicsHolder.drawText(thisRoute, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 19.5F), ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
		}
		if (lastStation != null) {
			graphicsHolder.drawText("> " + lastStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 9.5F), ARGB_WHITE, true, GraphicsHolder.getDefaultLight());
		}

		RenderSystem.disableBlend();
		graphicsHolder.pop();
	}
}
