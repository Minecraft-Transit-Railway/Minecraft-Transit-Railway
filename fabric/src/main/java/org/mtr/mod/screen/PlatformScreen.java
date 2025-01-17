package org.mtr.mod.screen;

import org.mtr.core.data.*;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateData;

public class PlatformScreen extends SavedRailScreenBase<Platform, Station> {

	private static final MutableText DWELL_TIME_TEXT = TranslationProvider.GUI_MTR_DWELL_TIME.getMutableText();
	private static final MutableText ROUTES_AT_PLATFORM_TEXT = TranslationProvider.GUI_MTR_ROUTES_AT_PLATFORM.getMutableText();
	private final ObjectOpenHashSet<Route> routes = new ObjectOpenHashSet<>();

	public PlatformScreen(Platform savedRailBase, TransportMode transportMode, ScreenExtension previousScreenExtension) {
		super(savedRailBase, transportMode, previousScreenExtension, DWELL_TIME_TEXT, ROUTES_AT_PLATFORM_TEXT);

		for (Route route : MinecraftClientData.getDashboardInstance().routes) {
			for (RoutePlatformData routePlat : route.getRoutePlatforms()) {
				if (routePlat.getPlatform().getId() == savedRailBase.getId()) {
					routes.add(route);
				}
			}
		}
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
			graphicsHolder.drawText(DWELL_TIME_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		}

		// Draw route lists
		graphicsHolder.drawText(ROUTES_AT_PLATFORM_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		int i = 0;
		for (Route rts : routes) {
			graphicsHolder.drawText(IGui.formatStationName(rts.getName()), SQUARE_SIZE + textWidth, (SQUARE_SIZE * 4) + (10 * i) + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_BLACK + rts.getColor(), false, GraphicsHolder.getDefaultLight());
			i++;
		}
	}

	@Override
	public void onClose2() {
		final int minutes = sliderDwellTimeMin.getIntValue();
		final float second = sliderDwellTimeSec.getIntValue() / 2F;
		savedRailBase.setDwellTime((long) ((second + (long) minutes * SECONDS_PER_MINUTE) * 1000));

		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addPlatform(savedRailBase)));

		super.onClose2();
	}

	@Override
	protected TranslationProvider.TranslationHolder getNumberStringKey() {
		return TranslationProvider.GUI_MTR_PLATFORM_NUMBER;
	}
}
