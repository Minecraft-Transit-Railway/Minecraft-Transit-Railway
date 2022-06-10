package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.Platform;
import mtr.data.TransportMode;
import mtr.mappings.Text;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class PlatformScreen extends SavedRailScreenBase<Platform> {
	private static final Component DWELL_TIME_TEXT = Text.translatable("gui.mtr.dwell_time");
	private static final int MAX_SEC_VALUE = 119;
	private final WidgetShorterSlider sliderDwellTimeSec;
	private final WidgetShorterSlider sliderDwellTimeMin;

	public PlatformScreen(Platform savedRailBase, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(savedRailBase, transportMode, dashboardScreen, DWELL_TIME_TEXT);
		sliderDwellTimeMin = new WidgetShorterSlider(0, SLIDER_WIDTH - font.width("88min") - TEXT_PADDING, (int) Math.floor((Platform.MAX_DWELL_TIME / 2.0) / 60.0), value -> String.format("%smin", (value)), null);
		sliderDwellTimeSec = new WidgetShorterSlider(0, SLIDER_WIDTH - font.width("88.8s") - TEXT_PADDING, MAX_SEC_VALUE, 10, 2, value -> String.format("%ss", (value) / 2F), null);
	}

	@Override
	protected void init() {
		super.init();
		sliderDwellTimeMin.x = startX + textWidth;
		sliderDwellTimeMin.y = height / 2 + TEXT_FIELD_PADDING / 2;
		sliderDwellTimeMin.setHeight(SQUARE_SIZE);
		sliderDwellTimeMin.setValue((int) Math.floor((savedRailBase.getDwellTime() / 2.0) / 60.0));

		sliderDwellTimeSec.x = startX + textWidth;
		sliderDwellTimeSec.y = (height / 2 + TEXT_FIELD_PADDING / 2) + sliderDwellTimeMin.getHeight();
		sliderDwellTimeSec.setHeight(SQUARE_SIZE);
		sliderDwellTimeSec.setValue(savedRailBase.getDwellTime() % 120);

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
		int maxMin = (int) Math.floor(Platform.MAX_DWELL_TIME / 2.0) / 60;
		if(sliderDwellTimeMin.getIntValue() == 0 && sliderDwellTimeSec.getIntValue() == 0) {
			sliderDwellTimeSec.setValue(1);
		}

		if(sliderDwellTimeMin.getIntValue() == maxMin && sliderDwellTimeSec.getIntValue() > Platform.MAX_DWELL_TIME % 120) {
			sliderDwellTimeSec.setValue(Platform.MAX_DWELL_TIME % 120);
		}
	}

	@Override
	public void onClose() {
		int minutes = sliderDwellTimeMin.getIntValue();
		double second = sliderDwellTimeSec.getIntValue() / 2.0;
		savedRailBase.setDwellTime((int)((second + minutes * 60) * 2), packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_PLATFORM, packet));
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
