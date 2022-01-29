package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class DeleteConfirmationScreen extends ScreenMapper implements IGui {

	private final Runnable deleteCallback;
	private final String name;
	private final DashboardScreen dashboardScreen;
	private final Button buttonYes;
	private final Button buttonNo;

	private static final int BUTTON_WIDTH = 100;
	private static final int BUTTON_HALF_PADDING = 10;

	public DeleteConfirmationScreen(Runnable deleteCallback, String name, DashboardScreen dashboardScreen) {
		super(new TextComponent(""));

		this.deleteCallback = deleteCallback;
		this.name = name;
		this.dashboardScreen = dashboardScreen;

		buttonYes = new Button(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.yes"), button -> onYes());
		buttonNo = new Button(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.no"), button -> onNo());
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
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			super.render(matrices, mouseX, mouseY, delta);
			drawCenteredString(matrices, font, new TranslatableComponent("gui.mtr.delete_confirmation", IGui.formatStationName(name)), width / 2, height / 2 - SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		if (minecraft != null) {
			UtilitiesClient.setScreen(minecraft, dashboardScreen);
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
