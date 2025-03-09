package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;

public class PlatformScreen extends SavedRailScreenBase<Platform, Station> {

	private static final MutableText DWELL_TIME_TEXT = TranslationProvider.GUI_MTR_DWELL_TIME.getMutableText();
	private static final MutableText ROUTES_AT_PLATFORM_TEXT = TranslationProvider.GUI_MTR_ROUTES_AT_PLATFORM.getMutableText();
	private final ObjectOpenHashSet<Route> routes = new ObjectOpenHashSet<>();

	public PlatformScreen(Platform savedRailBase, TransportMode transportMode, Screen previousScreen) {
		super(savedRailBase, transportMode, previousScreen, DWELL_TIME_TEXT, ROUTES_AT_PLATFORM_TEXT);

		for (Route route : MinecraftClientData.getDashboardInstance().routes) {
			for (RoutePlatformData routePlat : route.getRoutePlatforms()) {
				if (routePlat.getPlatform().getId() == savedRailBase.getId()) {
					routes.add(route);
				}
			}
		}
	}

	@Override
	protected void init() {
		super.init();
		sliderDwellTimeMin.setY(SQUARE_SIZE * 2 + TEXT_FIELD_PADDING);
		sliderDwellTimeMin.setValue((int) Math.floor(savedRailBase.getDwellTime() / 1000F / SECONDS_PER_MINUTE));
		sliderDwellTimeSec.setY(SQUARE_SIZE * 5 / 2 + TEXT_FIELD_PADDING);
		sliderDwellTimeSec.setValue((int) ((savedRailBase.getDwellTime() / 500) % (SECONDS_PER_MINUTE * 2)));
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		if (showScheduleControls) {
			context.drawText(textRenderer, DWELL_TIME_TEXT, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false);
		}

		// Draw route lists
		context.drawText(textRenderer, ROUTES_AT_PLATFORM_TEXT, SQUARE_SIZE, SQUARE_SIZE * 4 + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_WHITE, false);
		int i = 0;
		for (Route rts : routes) {
			context.drawText(textRenderer, IGui.formatStationName(rts.getName()), SQUARE_SIZE + textWidth, (SQUARE_SIZE * 4) + (10 * i) + TEXT_FIELD_PADDING + TEXT_PADDING, ARGB_BLACK + rts.getColor(), false);
			i++;
		}
	}

	@Override
	public void close() {
		final int minutes = sliderDwellTimeMin.getIntValue();
		final float second = sliderDwellTimeSec.getIntValue() / 2F;
		savedRailBase.setDwellTime((long) ((second + (long) minutes * SECONDS_PER_MINUTE) * 1000));

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addPlatform(savedRailBase)));

		super.close();
	}

	@Override
	protected TranslationProvider.TranslationHolder getNumberStringKey() {
		return TranslationProvider.GUI_MTR_PLATFORM_NUMBER;
	}
}
