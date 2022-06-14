package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.Platform;
import mtr.data.TransportMode;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PlatformScreen extends SavedRailScreenBase<Platform> {

	private final WidgetShorterSlider sliderDwellTimeSec;
	private final WidgetShorterSlider sliderDwellTimeMin;
	private static final Component DWELL_TIME_TEXT = Text.translatable("gui.mtr.dwell_time");
	private static final int SECONDS_PER_MINUTE = 60;

	public PlatformScreen(Platform savedRailBase, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(savedRailBase, transportMode, dashboardScreen, DWELL_TIME_TEXT);
		final int textWidth = Math.max(font.width(Text.translatable("gui.mtr.arrival_min", "88")), font.width(Text.translatable("gui.mtr.arrival_sec", "88.8")));
		sliderDwellTimeMin = new WidgetShorterSlider(0, SLIDER_WIDTH - textWidth - TEXT_PADDING, (int) Math.floor(Platform.MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE), value -> Text.translatable("gui.mtr.arrival_min", value).getString(), null);
		sliderDwellTimeSec = new WidgetShorterSlider(0, SLIDER_WIDTH - textWidth - TEXT_PADDING, SECONDS_PER_MINUTE * 2 - 1, 10, 2, value -> Text.translatable("gui.mtr.arrival_sec", value / 2F).getString(), null);
	}

	@Override
	protected void init() {
		super.init();
		sliderDwellTimeMin.x = startX + textWidth;
		sliderDwellTimeMin.y = height / 2 + TEXT_FIELD_PADDING / 2;
		sliderDwellTimeMin.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeMin.setValue((int) Math.floor(savedRailBase.getDwellTime() / 2F / SECONDS_PER_MINUTE));

		sliderDwellTimeSec.x = startX + textWidth;
		sliderDwellTimeSec.y = (height / 2 + TEXT_FIELD_PADDING / 2) + SQUARE_SIZE / 2;
		sliderDwellTimeSec.setHeight(SQUARE_SIZE / 2);
		sliderDwellTimeSec.setValue(savedRailBase.getDwellTime() % (SECONDS_PER_MINUTE * 2));

		if (showScheduleControls) {
			addDrawableChild(sliderDwellTimeMin);
			addDrawableChild(sliderDwellTimeSec);
		}
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		super.render(matrices, mouseX, mouseY, delta);
		if (showScheduleControls) {
			font.draw(matrices, DWELL_TIME_TEXT, startX, height / 2F + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
		}
	}

	@Override
	public void tick() {
		final int maxMin = (int) Math.floor(Platform.MAX_DWELL_TIME / 2F / SECONDS_PER_MINUTE);
		if (sliderDwellTimeMin.getIntValue() == 0 && sliderDwellTimeSec.getIntValue() == 0) {
			sliderDwellTimeSec.setValue(1);
		}
		if (sliderDwellTimeMin.getIntValue() == maxMin && sliderDwellTimeSec.getIntValue() > Platform.MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2)) {
			sliderDwellTimeSec.setValue(Platform.MAX_DWELL_TIME % (SECONDS_PER_MINUTE * 2));
		}
	}

	@Override
	public void onClose() {
		final int minutes = sliderDwellTimeMin.getIntValue();
		final float second = sliderDwellTimeSec.getIntValue() / 2F;
		savedRailBase.setDwellTime((int) ((second + minutes * SECONDS_PER_MINUTE) * 2), packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_PLATFORM, packet));
		super.onClose();
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.platform_number";
	}

	@Override
	protected ResourceLocation getPacketIdentifier() {
		return PACKET_UPDATE_PLATFORM;
	}
}
