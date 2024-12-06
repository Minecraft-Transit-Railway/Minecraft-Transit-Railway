package org.mtr.mod.screen;

import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

public class FakePauseScreen extends MTRScreenBase implements IGui {

	private long textCooldown;
	private final String dismissPauseScreenText = TranslationProvider.GUI_MTR_DISMISS_PAUSE_SCREEN.getString();
	private final int textWidth = GraphicsHolder.getTextWidth(dismissPauseScreenText);

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		if (System.currentTimeMillis() < textCooldown) {
			graphicsHolder.push();
			final float newWidth = textWidth + TEXT_PADDING * 2F;
			final float newHeight = TEXT_HEIGHT + TEXT_PADDING * 2F;
			graphicsHolder.translate(width / 2F - newWidth / 2F, height / 2F - newHeight / 2F, 0);
			graphicsHolder.scale(newWidth / width, newHeight / height, 1);
			renderBackground(graphicsHolder);
			graphicsHolder.pop();
			graphicsHolder.drawCenteredText(dismissPauseScreenText, width / 2, height / 2 - TEXT_HEIGHT / 2, ARGB_WHITE);
		}
		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public boolean mouseClicked2(double mouseX, double mouseY, int button) {
		MinecraftClient.getInstance().openScreen(null);
		return super.mouseClicked2(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		textCooldown = System.currentTimeMillis() + 1000;
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}
}
