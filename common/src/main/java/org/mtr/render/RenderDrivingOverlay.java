package org.mtr.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.Window;
import net.minecraft.util.Identifier;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;

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

	public static void render(DrawContext context) {
		if (cooldown > 0) {
			cooldown--;
		} else {
			return;
		}

		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
		final Window window = minecraftClient.getWindow();
		if (clientPlayerEntity == null) {
			return;
		}

		context.getMatrices().push();
		final Identifier widgetsTexture = Identifier.of("textures/gui/widgets.png");
		final int startX = (window.getScaledWidth() - HOT_BAR_WIDTH) / 2;
		final int startY = window.getScaledHeight() - (clientPlayerEntity.isCreative() ? 47 : 63);

		context.drawGuiTexture(RenderLayer::getGuiTextured, widgetsTexture, startX, startY, 0, 0, 61, HOT_BAR_HEIGHT, 256, 256);
		context.drawGuiTexture(RenderLayer::getGuiTextured, widgetsTexture, startX + 61, startY, 141, 0, 41, HOT_BAR_HEIGHT, 256, 256);
		context.drawGuiTexture(RenderLayer::getGuiTextured, widgetsTexture, startX + 120, startY, 0, 0, 21, HOT_BAR_HEIGHT, 256, 256);
		context.drawGuiTexture(RenderLayer::getGuiTextured, widgetsTexture, startX + 141, startY, 141, 0, 41, HOT_BAR_HEIGHT, 256, 256);

		context.drawGuiTexture(RenderLayer::getGuiTextured, widgetsTexture, startX + 39 + Math.max(accelerationSign, -2) * 20, startY - 1, 0, 22, 24, 24, 256, 256);
		context.drawGuiTexture(RenderLayer::getGuiTextured, widgetsTexture, startX + (doorValue > 0 ? doorValue < 1 ? 139 : 159 : 119), startY - 1, 0, 22, 24, 24, 256, 256);

		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "B2", (int) (startX + 5.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == -2 ? ARGB_WHITE : ARGB_GRAY);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "B1", (int) (startX + 25.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == -1 ? ARGB_WHITE : ARGB_GRAY);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "N", (int) (startX + 48.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 0 ? ARGB_WHITE : ARGB_GRAY);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "P1", (int) (startX + 65.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 1 ? ARGB_WHITE : ARGB_GRAY);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "P2", (int) (startX + 85.5F), (int) (startY + 7.5F), doorValue == 0 && accelerationSign == 2 ? ARGB_WHITE : ARGB_GRAY);

		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "DC", (int) (startX + 125.5F), (int) (startY + 7.5F), speed == 0 && doorValue == 0 ? ARGB_WHITE : ARGB_GRAY);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, String.valueOf(Math.round(doorValue * 10) / 10F), (int) (startX + 144.5F), (int) (startY + 7.5F), doorValue > 0 && doorValue < 1 ? ARGB_WHITE : ARGB_GRAY);
		context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, "DO", (int) (startX + 165.5F), (int) (startY + 7.5F), speed == 0 && doorValue == 1 ? ARGB_WHITE : ARGB_GRAY);

		final String speedText = Utilities.round(speed * 3.6F, 1) + " km/h";
		final TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
		context.drawText(textRenderer, speedText, startX - textRenderer.getWidth(speedText) - TEXT_PADDING, (int) (window.getScaledHeight() - 14.5F), ARGB_WHITE, true);
		if (thisStation != null) {
			context.drawText(textRenderer, thisStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 44.5F), ARGB_WHITE, true);
		}
		if (nextStation != null) {
			context.drawText(textRenderer, "> " + nextStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 34.5F), ARGB_WHITE, true);
		}
		if (thisRoute != null) {
			context.drawText(textRenderer, thisRoute, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 19.5F), ARGB_WHITE, true);
		}
		if (lastStation != null) {
			context.drawText(textRenderer, "> " + lastStation, startX + HOT_BAR_WIDTH + TEXT_PADDING, (int) (window.getScaledHeight() - 9.5F), ARGB_WHITE, true);
		}

		RenderSystem.disableBlend();
		context.getMatrices().pop();
	}
}
