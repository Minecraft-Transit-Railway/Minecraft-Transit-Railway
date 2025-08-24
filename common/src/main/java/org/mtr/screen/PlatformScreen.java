package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.Platform;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BetterSliderWidget;
import org.mtr.widget.BetterTextFieldWidget;

public final class PlatformScreen extends ScrollableScreenBase {

	private final Platform platform;

	private final BetterTextFieldWidget platformNumberTextField;
	private final BetterSliderWidget dwellTimeSlider;

	private static final int DWELL_TIME_SLIDER_SCALE = 500; // 500 ms interval
	private static final int MAX_DWELL_TIME = 10 * Utilities.MILLIS_PER_MINUTE; // 10 minutes
	private static final MutableText DWELL_TIME_TEXT = TranslationProvider.GUI_MTR_DWELL_TIME.getMutableText();
	private static final MutableText ROUTES_AT_PLATFORM_TEXT = TranslationProvider.GUI_MTR_ROUTES_AT_PLATFORM.getMutableText();

	public PlatformScreen(Platform platform, Screen previousScreen) {
		super(previousScreen);
		this.platform = platform;

		platformNumberTextField = new BetterTextFieldWidget(16, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_PLATFORM_NUMBER.getString(), FULL_WIDGET_WIDTH, null);
		dwellTimeSlider = new BetterSliderWidget(MAX_DWELL_TIME / DWELL_TIME_SLIDER_SCALE - 1, PlatformScreen::timeoutFormatter, TranslationProvider.GUI_MTR_DWELL_TIME.getString(), FULL_WIDGET_WIDTH, null);
	}

	@Override
	protected void init() {
		super.init();
		final int widgetColumn1 = getWidgetColumn1();

		int widgetY = 0;
		platformNumberTextField.setPosition(widgetColumn1, widgetY);
		platformNumberTextField.setText(platform.getName());

		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		dwellTimeSlider.setPosition(widgetColumn1, widgetY);
		dwellTimeSlider.setValue((int) (platform.getDwellTime() / DWELL_TIME_SLIDER_SCALE) - 1);

		addDrawableChild(platformNumberTextField);

		if (!platform.getTransportMode().continuousMovement) {
			addDrawableChild(dwellTimeSlider);
		}
	}

	@Override
	public void close() {
		platform.setName(platformNumberTextField.getText());
		platform.setDwellTime((long) dwellTimeSlider.getIntValue() * DWELL_TIME_SLIDER_SCALE + DWELL_TIME_SLIDER_SCALE);

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addPlatform(platform)));

		super.close();
	}

	@Override
	public ObjectArrayList<MutableText> getScreenTitle() {
		return ObjectArrayList.of(TranslationProvider.GUI_MTR_PLATFORM.getMutableText(Utilities.formatName(platformNumberTextField.getText())));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenSubtitle() {
		return ObjectArrayList.of(Text.literal(Utilities.formatName(platform.getStationName())));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenDescription() {
		final ObjectArrayList<MutableText> lines = new ObjectArrayList<>();
		lines.add(TranslationProvider.GUI_MTR_ROUTES_AT_PLATFORM.getMutableText(platform.routes.size()));
		platform.routes.stream().sorted().map(route -> {
			final String routeNumber = Utilities.formatName(route.getRouteNumber());
			return Text.literal("- ").withColor(route.getColor()).append(Text.literal(String.format("%s%s", Utilities.formatName(route.getName()), routeNumber.isEmpty() ? "" : " " + routeNumber)).withColor(GuiHelper.WHITE_COLOR));
		}).forEach(lines::add);
		return lines;
	}

	private static String timeoutFormatter(int value) {
		final int millis = value * DWELL_TIME_SLIDER_SCALE + DWELL_TIME_SLIDER_SCALE;
		return String.format("%s %s", TranslationProvider.GUI_MTR_MINUTES.getString(millis / 60 / 1000), TranslationProvider.GUI_MTR_SECONDS.getString((millis / 1000F) % 60));
	}
}
