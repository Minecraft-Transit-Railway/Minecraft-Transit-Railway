package org.mtr.mod.screen;

import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.mapper.ButtonWidgetExtension;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;

public class DeleteConfirmationScreen extends MTRScreenBase implements IGui {

	private final Runnable deleteCallback;
	private final String name;
	private final ButtonWidgetExtension buttonYes;
	private final ButtonWidgetExtension buttonNo;

	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HALF_PADDING = 10;

	public DeleteConfirmationScreen(Runnable deleteCallback, String name, ScreenExtension previousScreenExtension) {
		super(previousScreenExtension);

		this.deleteCallback = deleteCallback;
		this.name = name;
		buttonYes = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.yes"), button -> onYes());
		buttonNo = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.no"), button -> onNo());
	}

	@Override
	protected void init2() {
		super.init2();
		IDrawing.setPositionAndWidth(buttonYes, width / 2 - BUTTON_WIDTH - BUTTON_HALF_PADDING, height / 2, BUTTON_WIDTH);
		IDrawing.setPositionAndWidth(buttonNo, width / 2 + BUTTON_HALF_PADDING, height / 2, BUTTON_WIDTH);
		addChild(new ClickableWidget(buttonYes));
		addChild(new ClickableWidget(buttonNo));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(graphicsHolder);
			super.render(graphicsHolder, mouseX, mouseY, delta);
			graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_DELETE_CONFIRMATION.getMutableText(IGui.formatStationName(name)), width / 2, height / 2 - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	private void onYes() {
		deleteCallback.run();
		onClose2();
	}

	private void onNo() {
		onClose2();
	}
}
