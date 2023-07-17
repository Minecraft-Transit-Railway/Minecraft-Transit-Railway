package mtr.screen;

import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.data.Platform;
import mtr.data.SavedRailBase;
import mtr.data.TransportMode;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class SavedRailScreenBase<T extends SavedRailBase> extends ScreenMapper implements IGui, IPacket {

	protected final T savedRailBase;
	protected final int textWidth;
	protected final boolean showScheduleControls;
	protected final WidgetShorterSlider sliderDwellTimeMin;
	protected final WidgetShorterSlider sliderDwellTimeSec;

	private final DashboardScreen dashboardScreen;
	private final WidgetBetterTextField textFieldSavedRailNumber;

	private final Component savedRailNumberText;

	protected static final int SECONDS_PER_MINUTE = 60;
	private static final int MAX_SAVED_RAIL_NUMBER_LENGTH = 10;

	public SavedRailScreenBase(T savedRailBase, TransportMode transportMode, DashboardScreen dashboardScreen, Component... additionalTexts) {
		super(Text.literal(""));
		this.savedRailBase = savedRailBase;
		showScheduleControls = !transportMode.continuousMovement;
		this.dashboardScreen = dashboardScreen;
		savedRailNumberText = Text.translatable(getNumberStringKey());

		font = Minecraft.getInstance().font;
		textFieldSavedRailNumber = new WidgetBetterTextField("1", MAX_SAVED_RAIL_NUMBER_LENGTH);

		int additionalTextWidths = 0;
		for (final Component additionalText : additionalTexts) {
			additionalTextWidths = Math.max(additionalTextWidths, font.width(additionalText));
		}
		textWidth = Math.max(font.width(savedRailNumberText), additionalTextWidths) + TEXT_PADDING;

		sliderDwellTimeMin = new WidgetShorterSlider(0, 0, (int) Math.floor(Platform.MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE), value -> Text.translatable("gui.mtr.arrival_min", value).getString(), null);
		sliderDwellTimeSec = new WidgetShorterSlider(0, 0, SECONDS_PER_MINUTE * 2 - 1, 10, 2, value -> Text.translatable("gui.mtr.arrival_sec", value / 2F).getString(), null);
	}

	@Override
	protected void init() {
		super.init();

		IDrawing.setPositionAndWidth(textFieldSavedRailNumber, SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, width - textWidth - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		textFieldSavedRailNumber.setValue(savedRailBase.name);
		textFieldSavedRailNumber.setResponder(text -> savedRailBase.name = textFieldSavedRailNumber.getValue());

		final int sliderTextWidth = Math.max(font.width(Text.translatable("gui.mtr.arrival_min", "88")), font.width(Text.translatable("gui.mtr.arrival_sec", "88.8"))) + TEXT_PADDING;
		UtilitiesClient.setWidgetX(sliderDwellTimeMin, SQUARE_SIZE + textWidth);
		sliderDwellTimeMin.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeMin.setWidth(width - textWidth - SQUARE_SIZE * 2 - sliderTextWidth);
		sliderDwellTimeMin.setValue((int) Math.floor(savedRailBase.getDwellTime() / 2F / SECONDS_PER_MINUTE));

		UtilitiesClient.setWidgetX(sliderDwellTimeSec, SQUARE_SIZE + textWidth);
		sliderDwellTimeSec.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeSec.setWidth(width - textWidth - SQUARE_SIZE * 2 - sliderTextWidth);
		sliderDwellTimeSec.setValue(savedRailBase.getDwellTime() % (SECONDS_PER_MINUTE * 2));

		addDrawableChild(textFieldSavedRailNumber);
		if (showScheduleControls) {
			addDrawableChild(sliderDwellTimeMin);
			addDrawableChild(sliderDwellTimeSec);
		}
	}

	@Override
	public void tick() {
		textFieldSavedRailNumber.tick();
		UtilitiesClient.setWidgetX(textFieldSavedRailNumber, shouldRenderExtra() ? width * 2 : SQUARE_SIZE + textWidth + TEXT_FIELD_PADDING / 2);

		final int maxMin = (int) Math.floor(Platform.MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE);
		if (sliderDwellTimeMin.getIntValue() == 0 && sliderDwellTimeSec.getIntValue() == 0) {
			sliderDwellTimeSec.setValue(1);
		}
		if (sliderDwellTimeMin.getIntValue() == maxMin && sliderDwellTimeSec.getIntValue() > Platform.MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2)) {
			sliderDwellTimeSec.setValue(Platform.MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2));
		}
	}

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(guiGraphics);
			if (shouldRenderExtra()) {
				renderExtra(guiGraphics, mouseX, mouseY, delta);
			} else {
				guiGraphics.drawString(font, savedRailNumberText, SQUARE_SIZE, SQUARE_SIZE + TEXT_FIELD_PADDING / 2 + TEXT_PADDING, ARGB_WHITE);
			}
			super.render(guiGraphics, mouseX, mouseY, delta);
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

	protected void renderExtra(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
	}

	protected abstract String getNumberStringKey();

	protected abstract ResourceLocation getPacketIdentifier();
}
