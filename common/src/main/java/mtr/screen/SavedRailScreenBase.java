package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class SavedRailScreenBase<T extends SavedRailBase> extends ScreenMapper implements IGui, IPacket {

	protected int startX;

	protected final T savedRailBase;
	protected final int textWidth;
	protected final boolean showScheduleControls;
	protected final WidgetShorterSlider sliderDwellTimeMin;
	protected final WidgetShorterSlider sliderDwellTimeSec;

	private final DashboardScreen dashboardScreen;
	private final WidgetBetterTextField textFieldSavedRailNumber;

	private final Component savedRailNumberText;

	protected static final int SLIDER_WIDTH = 240;
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

		final int sliderTextWidth = Math.max(font.width(Text.translatable("gui.mtr.arrival_min", "88")), font.width(Text.translatable("gui.mtr.arrival_sec", "88.8")));
		sliderDwellTimeMin = new WidgetShorterSlider(0, SLIDER_WIDTH - sliderTextWidth - TEXT_PADDING, (int) Math.floor(Platform.MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE), value -> Text.translatable("gui.mtr.arrival_min", value).getString(), null);
		sliderDwellTimeSec = new WidgetShorterSlider(0, SLIDER_WIDTH - sliderTextWidth - TEXT_PADDING, SECONDS_PER_MINUTE * 2 - 1, 10, 2, value -> Text.translatable("gui.mtr.arrival_sec", value / 2F).getString(), null);
	}

	@Override
	protected void init() {
		super.init();

		startX = (width - textWidth - SLIDER_WIDTH) / 2;
		IDrawing.setPositionAndWidth(textFieldSavedRailNumber, startX + textWidth + TEXT_FIELD_PADDING / 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SLIDER_WIDTH - TEXT_FIELD_PADDING);
		textFieldSavedRailNumber.setValue(savedRailBase.name);
		textFieldSavedRailNumber.setResponder(text -> savedRailBase.name = textFieldSavedRailNumber.getValue());

		sliderDwellTimeMin.x = startX + textWidth;
		sliderDwellTimeMin.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeMin.setValue((int) Math.floor(savedRailBase.getDwellTime() / 2F / SECONDS_PER_MINUTE));

		sliderDwellTimeSec.x = startX + textWidth;
		sliderDwellTimeSec.setHeight(SQUARE_SIZE / 2);
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
		textFieldSavedRailNumber.x = shouldRenderExtra() ? width * 2 : startX + textWidth + TEXT_FIELD_PADDING / 2;

		final int maxMin = (int) Math.floor(Platform.MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE);
		if (sliderDwellTimeMin.getIntValue() == 0 && sliderDwellTimeSec.getIntValue() == 0) {
			sliderDwellTimeSec.setValue(1);
		}
		if (sliderDwellTimeMin.getIntValue() == maxMin && sliderDwellTimeSec.getIntValue() > Platform.MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2)) {
			sliderDwellTimeSec.setValue(Platform.MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2));
		}
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			if (shouldRenderExtra()) {
				renderExtra(matrices, mouseX, mouseY, delta);
			} else {
				font.draw(matrices, savedRailNumberText, startX, SQUARE_SIZE + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
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
