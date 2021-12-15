package mtr.gui;

import minecraftmappings.ScreenMapper;
import minecraftmappings.UtilitiesClient;
import mtr.data.IGui;
import mtr.data.SavedRailBase;
import mtr.packet.IPacket;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public abstract class SavedRailScreenBase<T extends SavedRailBase> extends ScreenMapper implements IGui, IPacket {

	protected final T savedRailBase;
	protected final int textWidth, startX;

	private final DashboardScreen dashboardScreen;
	private final WidgetBetterTextField textFieldSavedRailNumber;

	private final Text savedRailNumberText;
	private final Text secondText;

	protected static final int SLIDER_WIDTH = 160;
	private static final int MAX_SAVED_RAIL_NUMBER_LENGTH = 10;

	public SavedRailScreenBase(T savedRailBase, DashboardScreen dashboardScreen, Text additionalText) {
		super(new LiteralText(""));
		this.savedRailBase = savedRailBase;
		this.dashboardScreen = dashboardScreen;
		savedRailNumberText = new TranslatableText(getNumberStringKey());
		secondText = new TranslatableText(getSecondStringKey());

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldSavedRailNumber = new WidgetBetterTextField(null, "1", MAX_SAVED_RAIL_NUMBER_LENGTH);

		textWidth = Math.max(Math.max(textRenderer.getWidth(savedRailNumberText), textRenderer.getWidth(secondText)), additionalText == null ? 0 : textRenderer.getWidth(additionalText)) + TEXT_PADDING;
		startX = (width - textWidth - SLIDER_WIDTH) / 2 + SLIDER_WIDTH;
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(textFieldSavedRailNumber, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, SLIDER_WIDTH - TEXT_FIELD_PADDING);
		textFieldSavedRailNumber.setText(savedRailBase.name);
		textFieldSavedRailNumber.setChangedListener(text -> savedRailBase.name = textFieldSavedRailNumber.getText());

		addDrawableChild(textFieldSavedRailNumber);
	}

	@Override
	public void tick() {
		textFieldSavedRailNumber.tick();
		textFieldSavedRailNumber.x = shouldRenderExtra() ? width * 2 : startX + textWidth + TEXT_FIELD_PADDING / 2;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			if (shouldRenderExtra()) {
				renderExtra(matrices, mouseX, mouseY, delta);
			} else {
				textRenderer.draw(matrices, savedRailNumberText, startX, height / 2F - SQUARE_SIZE - TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
				textRenderer.draw(matrices, secondText, startX, height / 2F + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			}
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		if (client != null) {
			UtilitiesClient.setScreen(client, dashboardScreen);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	protected boolean shouldRenderExtra() {
		return false;
	}

	protected void renderExtra(MatrixStack matrices, int mouseX, int mouseY, float delta) {
	}

	protected abstract String getNumberStringKey();

	protected abstract String getSecondStringKey();

	protected abstract Identifier getPacketIdentifier();
}
