package org.mtr.mod.screen;

import org.mtr.core.data.Platform;
import org.mtr.core.data.Station;
import org.mtr.core.data.TransportMode;
import org.mtr.core.servlet.IntegrationServlet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.InitClient;
import org.mtr.mod.packet.PacketData;

public class PlatformScreen extends SavedRailScreenBase<Platform, Station> {

	private static final MutableText DWELL_TIME_TEXT = TextHelper.translatable("gui.mtr.dwell_time");

	public PlatformScreen(Platform savedRailBase, TransportMode transportMode, DashboardScreen dashboardScreen) {
		super(savedRailBase, transportMode, dashboardScreen, DWELL_TIME_TEXT);
	}

	@Override
	protected void init2() {
		super.init2();
		sliderDwellTimeMin.setY2(SQUARE_SIZE * 2 + TEXT_FIELD_PADDING);
		sliderDwellTimeMin.setValue((int) Math.floor(savedRailBase.getDwellTime() / 1000F / SECONDS_PER_MINUTE));
		sliderDwellTimeSec.setY2(SQUARE_SIZE * 5 / 2 + TEXT_FIELD_PADDING);
		sliderDwellTimeSec.setValue((int) ((savedRailBase.getDwellTime() / 500) % (SECONDS_PER_MINUTE * 2)));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		super.render(graphicsHolder, mouseX, mouseY, delta);
		if (showScheduleControls) {
			graphicsHolder.drawText(DWELL_TIME_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		}
	}

	@Override
	public void onClose2() {
		final int minutes = sliderDwellTimeMin.getIntValue();
		final float second = sliderDwellTimeSec.getIntValue() / 2F;
		savedRailBase.setDwellTime((long) ((second + (long) minutes * SECONDS_PER_MINUTE) * 1000));

		InitClient.REGISTRY_CLIENT.sendPacketToServer(PacketData.fromPlatforms(IntegrationServlet.Operation.UPDATE, ObjectSet.of(savedRailBase)));

		super.onClose2();
	}

	@Override
	protected String getNumberStringKey() {
		return "gui.mtr.platform_number";
	}
}
