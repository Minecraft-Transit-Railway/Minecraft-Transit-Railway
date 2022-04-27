package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.Platform;
import mtr.data.TransportMode;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

public class PlatformScreen extends SavedRailScreenBase<Platform> {

	private static final Component DWELL_TIME_TEXT = new TranslatableComponent("gui.mtr.dwell_time");
	private final WidgetShorterSlider sliderDwellTime;

	public PlatformScreen(Platform savedRailBase, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(savedRailBase, transportMode, dashboardScreen, DWELL_TIME_TEXT);
		sliderDwellTime = new WidgetShorterSlider(0, SLIDER_WIDTH - font.width("88.8s") - TEXT_PADDING, Platform.MAX_DWELL_TIME - 1, value -> String.format("%ss", (value + 1) / 2F), null);
	}

	@Override
	protected void init() {
		super.init();
		sliderDwellTime.x = startX + textWidth;
		sliderDwellTime.y = height / 2 + TEXT_FIELD_PADDING / 2;
		sliderDwellTime.setHeight(SQUARE_SIZE);
		sliderDwellTime.setValue(savedRailBase.getDwellTime() - 1);
		if (showScheduleControls) {
			addDrawableChild(sliderDwellTime);
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
	public void onClose() {
		savedRailBase.setDwellTime(sliderDwellTime.getIntValue() + 1, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_PLATFORM, packet));
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
