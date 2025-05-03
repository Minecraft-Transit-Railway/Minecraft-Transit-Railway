package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.mtr.client.IDrawing;
import org.mtr.config.Config;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;

public class BetaWarningScreen extends ScreenBase implements IGui, Utilities {

	private final ButtonWidget buttonPatreon;
	private final ButtonWidget buttonYouTube;

	private static long openTime;
	private static long lastMillis;
	private static final int BUTTON_WIDTH = SQUARE_SIZE * 8;
	private static final int FORCE_OPEN_DURATION = 20000;

	public BetaWarningScreen() {
		super();
		buttonPatreon = ButtonWidget.builder(Text.literal("Support this mod on Patreon"), button -> Util.getOperatingSystem().open("https://www.patreon.com/minecraft_transit_railway")).build();
		buttonYouTube = ButtonWidget.builder(Text.literal("Subscribe on YouTube"), button -> Util.getOperatingSystem().open("https://www.youtube.com/@JonathanHo33")).build();
	}

	@Override
	protected void init() {
		super.init();
		IDrawing.setPositionAndWidth(buttonPatreon, width / 2 - SQUARE_SIZE / 2 - BUTTON_WIDTH, height - SQUARE_SIZE * 2, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonYouTube, width / 2 + SQUARE_SIZE / 2, height - SQUARE_SIZE * 2, BUTTON_WIDTH);
		addDrawableChild(buttonPatreon);
		addDrawableChild(buttonYouTube);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		context.getMatrices().push();
		context.getMatrices().translate(width / 2F, SQUARE_SIZE, 0);
		context.getMatrices().scale(2, 2, 1);
		context.drawCenteredTextWithShadow(textRenderer, "Minecraft Transit Railway 4.0.0", 0, 0, ARGB_WHITE);
		context.getMatrices().pop();

		int i = SQUARE_SIZE * 2;
		context.drawCenteredTextWithShadow(textRenderer, "Please back up your worlds before continuing!", width / 2, i += TEXT_HEIGHT, System.currentTimeMillis() % 1000 < 500 ? 0xFFFF00 : 0xFF9900);
		i = wrapAndRender(context, "- We are very close to the official release of 4.0.0. Please report any bugs you find!", i + SQUARE_SIZE);
		i = wrapAndRender(context, "- If you find any issues with the Resource Pack Creator, please let me know.", i);
		i = wrapAndRender(context, "- Manual driving still has NOT been implemented yet!", i);
		context.drawCenteredTextWithShadow(textRenderer, "Thank you and enjoy :)", width / 2, i, ARGB_WHITE);

		context.getMatrices().push();
		context.getMatrices().translate(width / 2F, i + TEXT_HEIGHT + TEXT_PADDING / 2F, 0);
		context.getMatrices().scale(0.5F, 0.5F, 1);
		context.drawCenteredTextWithShadow(textRenderer, openTime < FORCE_OPEN_DURATION ? String.format("Please read the above carefully to continue! (%s)", (FORCE_OPEN_DURATION - openTime) / MILLIS_PER_SECOND) : "Press ESC to continue", 0, 0, ARGB_WHITE);
		context.getMatrices().pop();

		context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.of("mtr/patreon"), width / 2 - BUTTON_WIDTH / 2 - SQUARE_SIZE, height - SQUARE_SIZE * 3 - TEXT_PADDING, SQUARE_SIZE, SQUARE_SIZE);
		final int youTubeIconPadding = Math.round(SQUARE_SIZE * (90F / 64 - 1) / 2);
		context.drawGuiTexture(RenderLayer::getGuiTextured, Identifier.of("mtr/youtube"), width / 2 + BUTTON_WIDTH / 2 - youTubeIconPadding, height - SQUARE_SIZE * 3 - TEXT_PADDING, SQUARE_SIZE, SQUARE_SIZE);
	}

	@Override
	public void tick() {
		super.tick();
		if (openTime < FORCE_OPEN_DURATION) {
			final long currentMillis = System.currentTimeMillis();
			if (lastMillis > 0) {
				openTime += currentMillis - lastMillis;
			}
			lastMillis = currentMillis;
		}
	}

	@Override
	public void close() {
		if (openTime >= FORCE_OPEN_DURATION) {
			Config.getClient().hideBetaWarningScreen();
			Config.save();
			super.close();
		}
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	private int wrapAndRender(DrawContext context, String text, int y) {
		final int textWidth = Math.min(512, width - SQUARE_SIZE * 2);
		final int[] newY = {y};
		textRenderer.wrapLines(Text.literal(text), textWidth).forEach(orderedText -> {
			context.drawText(textRenderer, orderedText, (width - textWidth) / 2 + (newY[0] == y ? 0 : 10), newY[0], ARGB_LIGHT_GRAY, true);
			newY[0] += TEXT_HEIGHT + TEXT_PADDING / 2;
		});
		return newY[0] + TEXT_PADDING * 3 / 2;
	}

	public static void handle() {
		if (openTime < FORCE_OPEN_DURATION && Config.getClient().showBetaWarningScreen()) {
			final Screen screen = MinecraftClient.getInstance().currentScreen;
			if (screen != null && screen.getTitle().toString().contains("narrator.screen.title")) {
				MinecraftClient.getInstance().setScreen(new BetaWarningScreen());
			}
		} else {
			openTime = FORCE_OPEN_DURATION;
		}
	}
}
