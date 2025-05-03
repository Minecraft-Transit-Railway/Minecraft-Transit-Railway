package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

public class FakePauseScreen extends ScreenBase implements IGui {

	private long textCooldown;
	private final String dismissPauseScreenText = TranslationProvider.GUI_MTR_DISMISS_PAUSE_SCREEN.getString();
	private final int textWidth = textRenderer.getWidth(dismissPauseScreenText);

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		if (System.currentTimeMillis() < textCooldown) {
			context.getMatrices().push();
			final float newWidth = textWidth + TEXT_PADDING * 2F;
			final float newHeight = TEXT_HEIGHT + TEXT_PADDING * 2F;
			context.getMatrices().translate(width / 2F - newWidth / 2F, height / 2F - newHeight / 2F, 0);
			context.getMatrices().scale(newWidth / width, newHeight / height, 1);
			renderBackground(context, mouseX, mouseY, delta);
			context.getMatrices().pop();
			context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, dismissPauseScreenText, width / 2, height / 2 - TEXT_HEIGHT / 2, ARGB_WHITE);
		}
		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		MinecraftClient.getInstance().setScreen(null);
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		textCooldown = System.currentTimeMillis() + 1000;
	}

	@Override
	public boolean shouldPause() {
		return false;
	}
}
