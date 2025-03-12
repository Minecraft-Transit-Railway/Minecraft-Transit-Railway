package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.mtr.MTR;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;

public class DeleteConfirmationScreen extends MTRScreenBase implements IGui {

	private final Runnable deleteCallback;
	private final String name;
	private final ButtonWidget buttonYes;
	private final ButtonWidget buttonNo;

	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HALF_PADDING = 10;

	public DeleteConfirmationScreen(Runnable deleteCallback, String name, Screen previousScreen) {
		super(previousScreen);

		this.deleteCallback = deleteCallback;
		this.name = name;
		buttonYes = ButtonWidget.builder(Text.translatable("gui.yes"), button -> onYes()).build();
		buttonNo = ButtonWidget.builder(Text.translatable("gui.no"), button -> onNo()).build();
	}

	@Override
	protected void init() {
		super.init();
		IDrawing.setPositionAndWidth(buttonYes, width / 2 - BUTTON_WIDTH - BUTTON_HALF_PADDING, height / 2, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonNo, width / 2 + BUTTON_HALF_PADDING, height / 2, BUTTON_WIDTH);
		addDrawableChild(buttonYes);
		addDrawableChild(buttonNo);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(context, mouseX, mouseY, delta);
			super.render(context, mouseX, mouseY, delta);
			context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, TranslationProvider.GUI_MTR_DELETE_CONFIRMATION.getMutableText(IGui.formatStationName(name)), width / 2, height / 2 - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private void onYes() {
		deleteCallback.run();
		close();
	}

	private void onNo() {
		close();
	}
}
