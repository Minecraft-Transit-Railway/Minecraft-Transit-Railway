package org.mtr.mod.screen;

import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

public class ReloadCustomResourcesScreen extends MTRScreenBase implements IGui {

	int i = 0;

	private final Runnable task;
	private final String resourceReloadingInProgressText = TranslationProvider.GUI_MTR_RESOURCE_RELOADING_IN_PROGRESS.getString();

	public ReloadCustomResourcesScreen(Runnable task) {
		this.task = task;
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		graphicsHolder.drawCenteredText(resourceReloadingInProgressText, width / 2, height / 2 - TEXT_HEIGHT / 2, ARGB_WHITE);
		super.render(graphicsHolder, mouseX, mouseY, delta);

		if (i > 10) {
			i = -1;
			task.run();
		} else if (i < 0) {
			MinecraftClient.getInstance().openScreen(null);
		} else {
			i++;
		}
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}
}
