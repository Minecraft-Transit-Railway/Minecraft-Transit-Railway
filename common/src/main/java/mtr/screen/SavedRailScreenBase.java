package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.SavedRailBase;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class SavedRailScreenBase<T extends SavedRailBase> extends ScreenMapper implements IGui, IPacket {

	protected final T savedRailBase;
	protected final int textWidth, startX;

	private final DashboardScreen dashboardScreen;
	private final WidgetBetterTextField textFieldSavedRailNumber;

	private final Component savedRailNumberText;
	private final Component secondText;

	protected static final int SLIDER_WIDTH = 160;
	private static final int MAX_SAVED_RAIL_NUMBER_LENGTH = 10;

	public SavedRailScreenBase(T savedRailBase, DashboardScreen dashboardScreen, Component additionalText) {
		super(new TextComponent(""));
		this.savedRailBase = savedRailBase;
		this.dashboardScreen = dashboardScreen;
		savedRailNumberText = new TranslatableComponent(getNumberStringKey());
		secondText = new TranslatableComponent(getSecondStringKey());

		font = Minecraft.getInstance().font;
		textFieldSavedRailNumber = new WidgetBetterTextField(null, "1", MAX_SAVED_RAIL_NUMBER_LENGTH);

		textWidth = Math.max(Math.max(font.width(savedRailNumberText), font.width(secondText)), additionalText == null ? 0 : font.width(additionalText)) + TEXT_PADDING;
		startX = (width - textWidth - SLIDER_WIDTH) / 2 + SLIDER_WIDTH;
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(textFieldSavedRailNumber, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, SLIDER_WIDTH - TEXT_FIELD_PADDING);
		textFieldSavedRailNumber.setValue(savedRailBase.name);
		textFieldSavedRailNumber.setResponder(text -> savedRailBase.name = textFieldSavedRailNumber.getValue());

		addDrawableChild(textFieldSavedRailNumber);
	}

	@Override
	public void tick() {
		textFieldSavedRailNumber.tick();
		textFieldSavedRailNumber.x = shouldRenderExtra() ? width * 2 : startX + textWidth + TEXT_FIELD_PADDING / 2;
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			if (shouldRenderExtra()) {
				renderExtra(matrices, mouseX, mouseY, delta);
			} else {
				font.draw(matrices, savedRailNumberText, startX, height / 2F - SQUARE_SIZE - TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
				font.draw(matrices, secondText, startX, height / 2F + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			}
			super.render(matrices, mouseX, mouseY, delta);
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

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	protected boolean shouldRenderExtra() {
		return false;
	}

	protected void renderExtra(PoseStack matrices, int mouseX, int mouseY, float delta) {
	}

	protected abstract String getNumberStringKey();

	protected abstract String getSecondStringKey();

	protected abstract ResourceLocation getPacketIdentifier();
}
