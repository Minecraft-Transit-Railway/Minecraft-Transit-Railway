package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.SavedRailBase;
import mtr.data.TransportMode;
import mtr.mappings.ScreenMapper;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public abstract class SavedRailScreenBase<T extends SavedRailBase> extends ScreenMapper implements IGui, IPacket {

	protected int startX;

	protected final T savedRailBase;
	protected final int textWidth;
	protected final boolean showScheduleControls;

	private final DashboardScreen dashboardScreen;
	private final WidgetBetterTextField textFieldSavedRailNumber;

	private final Component savedRailNumberText;

	protected static final int SLIDER_WIDTH = 240;
	private static final int MAX_SAVED_RAIL_NUMBER_LENGTH = 10;

	public SavedRailScreenBase(T savedRailBase, TransportMode transportMode, DashboardScreen dashboardScreen, Component... additionalTexts) {
		super(new TextComponent(""));
		this.savedRailBase = savedRailBase;
		showScheduleControls = !transportMode.continuousMovement;
		this.dashboardScreen = dashboardScreen;
		savedRailNumberText = new TranslatableComponent(getNumberStringKey());

		font = Minecraft.getInstance().font;
		textFieldSavedRailNumber = new WidgetBetterTextField(null, "1", MAX_SAVED_RAIL_NUMBER_LENGTH);

		int additionalTextWidths = 0;
		for (final Component additionalText : additionalTexts) {
			additionalTextWidths = Math.max(additionalTextWidths, font.width(additionalText));
		}
		textWidth = Math.max(font.width(savedRailNumberText), additionalTextWidths) + TEXT_PADDING;
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(textFieldSavedRailNumber, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, SLIDER_WIDTH - TEXT_FIELD_PADDING);
		textFieldSavedRailNumber.setValue(savedRailBase.name);
		textFieldSavedRailNumber.setResponder(text -> savedRailBase.name = textFieldSavedRailNumber.getValue());

		addDrawableChild(textFieldSavedRailNumber);
		startX = (width - textWidth - SLIDER_WIDTH) / 2;
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

	protected abstract ResourceLocation getPacketIdentifier();
}
