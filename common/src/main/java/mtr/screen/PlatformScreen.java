package mtr.screen;

import mtr.data.Platform;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.resources.ResourceLocation;

public class PlatformScreen extends SavedRailScreenBase<Platform> {

	private final WidgetShorterSlider sliderDwellTime;

	public PlatformScreen(Platform savedRailBase, DashboardScreen dashboardScreen) {
		super(savedRailBase, dashboardScreen, null);
		sliderDwellTime = new WidgetShorterSlider(startX + textWidth, SLIDER_WIDTH, Platform.MAX_DWELL_TIME - 1, value -> String.format("%ss", (value + 1) / 2F), null);
	}

	@Override
	protected void init() {
		super.init();
		sliderDwellTime.y = height / 2 + TEXT_FIELD_PADDING / 2;
		sliderDwellTime.setHeight(SQUARE_SIZE);
		sliderDwellTime.setValue(savedRailBase.getDwellTime() - 1);
		addDrawableChild(sliderDwellTime);
	}

	@Override
	public void onClose() {
		savedRailBase.setDwellTime(sliderDwellTime.getIntValue() + 1, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_PLATFORM, packet));
		super.onClose();
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.platform_number";
	}

	@Override
	protected String getSecondStringKey() {
		return "gui.mtr.dwell_time";
	}

	@Override
	protected ResourceLocation getPacketIdentifier() {
		return PACKET_UPDATE_PLATFORM;
	}
}
