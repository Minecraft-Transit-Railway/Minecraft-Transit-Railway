package mtr.gui;

import mtr.data.IGui;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;

public class DeleteConfirmationScreen extends Screen implements IGui {

	private final Runnable deleteCallback;
	private final String name;
	private final DashboardScreen dashboardScreen;
	private final ButtonWidget buttonYes;
	private final ButtonWidget buttonNo;

	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HALF_PADDING = 10;

	public DeleteConfirmationScreen(Runnable deleteCallback, String name, DashboardScreen dashboardScreen) {
		super(new LiteralText(""));

		this.deleteCallback = deleteCallback;
		this.name = name;
		this.dashboardScreen = dashboardScreen;

		buttonYes = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.yes"), button -> onYes());
		buttonNo = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.no"), button -> onNo());
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
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.delete_confirmation", IGui.formatStationName(name)), width / 2, height / 2 - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		if (client != null) {
			client.setScreen(dashboardScreen);
		}
	}

	private void onYes() {
		deleteCallback.run();
		onClose();
	}

	private void onNo() {
		onClose();
	}
}
