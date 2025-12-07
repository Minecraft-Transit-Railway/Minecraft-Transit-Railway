package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.Long2LongAVLTreeMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.math.ColorHelper;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.DepotOperationByIds;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketDepotClear;
import org.mtr.packet.PacketDepotGenerate;
import org.mtr.packet.PacketDepotInstantDeploy;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public final class DepotScreen extends ScrollableScreenBase {

	private int tempColor;
	private final LongArrayList tempRealTimeDepartures = new LongArrayList();

	private final Depot depot;
	private final boolean showScheduleControls;

	private final BetterTextFieldWidget nameTextField;
	private final BetterButtonWidget openColorSelectorButton;

	private final BetterButtonWidget editInstructionsButton;
	private final BetterButtonWidget instantDeployButton;
	private final BetterButtonWidget generateRouteButton;
	private final BetterButtonWidget clearVehiclesButton;
	private final BetterCheckboxWidget repeatIndefinitelyCheckbox;

	private final BetterCheckboxWidget useMinecraftTimeCheckbox;
	private final BetterCheckboxWidget useRealTimeCheckbox;
	private final BetterTextFieldWidget realTimeDepartureTextField;
	private final BetterButtonWidget addRealTimeDepartureButton;

	private final BetterSliderWidget[] minecraftTimeSliders = new BetterSliderWidget[Utilities.HOURS_PER_DAY];
	private final ScrollableListWidget<RealTimeDepartureForList> realTimeDeparturesListWidget = new ScrollableListWidget<>();
	private final ColorSelectorWidget colorSelector;

	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int FREQUENCY_MULTIPLIER = 4;
	private static final Long2LongAVLTreeMap DEPOT_GENERATION_START_TIME = new Long2LongAVLTreeMap();

	public DepotScreen(Depot depot, Screen previousScreen) {
		super(previousScreen);
		this.depot = depot;
		showScheduleControls = !depot.getTransportMode().continuousMovement;

		openColorSelectorButton = new BetterButtonWidget(GuiHelper.COLOR_TEXTURE_ID, null, 0, this::onOpenColorSelector);
		nameTextField = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_STATION_NAME.getString(), FULL_WIDGET_WIDTH - GuiHelper.DEFAULT_PADDING - openColorSelectorButton.getWidth(), null);

		editInstructionsButton = new BetterButtonWidget(GuiHelper.EDIT_TEXTURE_ID, TranslationProvider.GUI_MTR_EDIT_INSTRUCTIONS.getString(), HALF_WIDGET_WIDTH, () -> {
			close();
			final ObjectArrayList<DashboardListItem> routes = new ObjectArrayList<>(MinecraftClientData.getFilteredDataSet(depot.getTransportMode(), MinecraftClientData.getDashboardInstance().routes));
			Collections.sort(routes);
			MinecraftClient.getInstance().setScreen(new DashboardListSelectorScreen(new ObjectImmutableList<>(routes), depot.getRouteIds(), false, true, this));
		});
		instantDeployButton = new BetterButtonWidget(null, TranslationProvider.GUI_MTR_INSTANT_DEPLOY.getString(), HALF_WIDGET_WIDTH, () -> {
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			RegistryClient.sendPacketToServer(new PacketDepotInstantDeploy(depotOperationByIds));
		});
		generateRouteButton = new BetterButtonWidget(null, TranslationProvider.GUI_MTR_REFRESH_PATH.getString(), HALF_WIDGET_WIDTH, () -> {
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			DEPOT_GENERATION_START_TIME.put(depot.getId(), System.currentTimeMillis());
			RegistryClient.sendPacketToServer(new PacketDepotGenerate(depotOperationByIds));
		});
		clearVehiclesButton = new BetterButtonWidget(null, TranslationProvider.GUI_MTR_CLEAR_VEHICLES.getString(), HALF_WIDGET_WIDTH, () -> {
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			RegistryClient.sendPacketToServer(new PacketDepotClear(depotOperationByIds));
		});
		repeatIndefinitelyCheckbox = new BetterCheckboxWidget(TranslationProvider.GUI_MTR_REPEAT_INDEFINITELY.getString(), () -> {
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			RegistryClient.sendPacketToServer(new PacketDepotGenerate(depotOperationByIds));
		});

		useMinecraftTimeCheckbox = new BetterCheckboxWidget(TranslationProvider.GUI_MTR_SCHEDULE_MODE_MINECRAFT_TIME.getString(), this::toggleUseMinecraftTime);
		useRealTimeCheckbox = new BetterCheckboxWidget(TranslationProvider.GUI_MTR_SCHEDULE_MODE_REAL_TIME.getString(), this::toggleUseRealTime);
		addRealTimeDepartureButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_DEPARTURE.getString(), 0, this::addRealTimeDeparture);
		realTimeDepartureTextField = new BetterTextFieldWidget(25, TextCase.DEFAULT, "[^\\d:+* ]", TranslationProvider.GUI_MTR_REALTIME_DEPARTURE.getString(), FULL_WIDGET_WIDTH - GuiHelper.DEFAULT_PADDING - addRealTimeDepartureButton.getWidth(), this::textFieldDepartureCallback);

		for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
			final int currentIndex = i;
			minecraftTimeSliders[currentIndex] = new BetterSliderWidget(MAX_TRAINS_PER_HOUR * 2, DepotScreen::getSliderString, String.format("%1$02d:00–%1$02d:59", currentIndex), HALF_WIDGET_WIDTH, value -> {
				for (int j = 0; j < Utilities.HOURS_PER_DAY; j++) {
					if (j != currentIndex) {
						minecraftTimeSliders[j].setValue(value);
					}
				}
			});
		}
		realTimeDeparturesListWidget.setBounds(FULL_WIDGET_WIDTH, 0, FULL_WIDGET_WIDTH, Integer.MAX_VALUE);
		colorSelector = new ColorSelectorWidget(width / 2, () -> enableControls(true), this::applyBlur);
	}

	@Override
	protected void init() {
		super.init();
		final int widgetColumn1 = getWidgetColumn1();
		final int widgetColumn2Of2 = getWidgetColumn2Of2();

		int widgetY = 0;
		nameTextField.setPosition(widgetColumn1, widgetY);
		nameTextField.setText(depot.getName());
		openColorSelectorButton.setPosition(widgetColumn1 + FULL_WIDGET_WIDTH - openColorSelectorButton.getWidth(), widgetY);
		openColorSelectorButton.setBackgroundColor(ColorHelper.fullAlpha(depot.getColor()));
		tempColor = depot.getColor();

		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		editInstructionsButton.setPosition(widgetColumn1, widgetY);
		instantDeployButton.setPosition(widgetColumn2Of2, widgetY);
		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		generateRouteButton.setPosition(widgetColumn1, widgetY);
		clearVehiclesButton.setPosition(widgetColumn2Of2, widgetY);
		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		repeatIndefinitelyCheckbox.setPosition(widgetColumn1, widgetY);
		repeatIndefinitelyCheckbox.isChecked = depot.getRepeatInfinitely();

		widgetY += GuiHelper.DEFAULT_LINE_SIZE * 2;
		useMinecraftTimeCheckbox.setPosition(widgetColumn1, widgetY);
		useMinecraftTimeCheckbox.isChecked = !depot.getUseRealTime();
		useRealTimeCheckbox.setPosition(widgetColumn2Of2, widgetY);
		useRealTimeCheckbox.isChecked = depot.getUseRealTime();
		widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		realTimeDepartureTextField.setPosition(widgetColumn1, widgetY);
		addRealTimeDepartureButton.setPosition(widgetColumn1 + FULL_WIDGET_WIDTH - addRealTimeDepartureButton.getWidth(), widgetY);

		realTimeDeparturesListWidget.setPosition(widgetColumn1, widgetY + GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		for (int i = 0; i < Utilities.HOURS_PER_DAY / 2; i++) {
			final int index2 = i + Utilities.HOURS_PER_DAY / 2;
			minecraftTimeSliders[i].setPosition(widgetColumn1, widgetY);
			minecraftTimeSliders[i].setValue((int) depot.getFrequency(i));
			minecraftTimeSliders[index2].setPosition(widgetColumn2Of2, widgetY);
			minecraftTimeSliders[index2].setValue((int) depot.getFrequency(index2));
			widgetY += GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING;
		}
		tempRealTimeDepartures.clear();
		tempRealTimeDepartures.addAll(depot.getRealTimeDepartures());
		toggleControls();
		setRealTimeDeparturesListItems();

		colorSelector.setPosition(width / 4, height * 2);

		addDrawableChild(nameTextField);
		addDrawableChild(openColorSelectorButton);

		addDrawableChild(editInstructionsButton);
		addDrawableChild(instantDeployButton);
		addDrawableChild(generateRouteButton);
		addDrawableChild(clearVehiclesButton);

		if (showScheduleControls) {
			addDrawableChild(repeatIndefinitelyCheckbox);

			addDrawableChild(useMinecraftTimeCheckbox);
			addDrawableChild(useRealTimeCheckbox);
			addDrawableChild(realTimeDepartureTextField);
			addDrawableChild(addRealTimeDepartureButton);

			for (final BetterSliderWidget minecraftTimeSlider : minecraftTimeSliders) {
				addDrawableChild(minecraftTimeSlider);
			}
			addDrawableChild(realTimeDeparturesListWidget);
		}

		addDrawableChild(colorSelector);
	}

	@Override
	public void tick() {
		super.tick();
		if (depot.routes.isEmpty()) {
			repeatIndefinitelyCheckbox.visible = false;
		} else {
			final Route firstRoute = MinecraftClientData.getDashboardInstance().routeIdMap.get(depot.routes.getFirst().getId());
			final Route lastRoute = MinecraftClientData.getDashboardInstance().routeIdMap.get(depot.routes.getLast().getId());
			repeatIndefinitelyCheckbox.visible = firstRoute != null && lastRoute != null && !firstRoute.getRoutePlatforms().isEmpty() && !lastRoute.getRoutePlatforms().isEmpty() && firstRoute.getRoutePlatforms().getFirst().getPlatform().getId() == lastRoute.getRoutePlatforms().getLast().getPlatform().getId();
		}
	}

	@Override
	public void close() {
		depot.setName(nameTextField.getText());
		depot.setColor(tempColor);

		depot.setRepeatInfinitely(repeatIndefinitelyCheckbox.visible && repeatIndefinitelyCheckbox.isChecked);
		depot.setUseRealTime(useRealTimeCheckbox.isChecked);

		if (useRealTimeCheckbox.isChecked) {
			depot.getRealTimeDepartures().clear();
			depot.getRealTimeDepartures().addAll(tempRealTimeDepartures);
		} else {
			for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
				depot.setFrequency(i, minecraftTimeSliders[i].getIntValue());
			}
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot(depot)));
		super.close();
	}

	@Override
	public ObjectArrayList<MutableText> getScreenTitle() {
		return ObjectArrayList.of(Text.literal(Utilities.formatName(nameTextField.getText())));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenSubtitle() {
		return ObjectArrayList.of(TranslationProvider.GUI_MTR_SIDINGS_IN_DEPOT.getMutableText(depot.savedRails.size()));
	}

	@Override
	public ObjectArrayList<MutableText> getScreenDescription() {
		final ObjectArrayList<MutableText> description = new ObjectArrayList<>();

		// Temporary workaround to get the latest depot path generation status
		final Depot newDepot = MinecraftClientData.getDashboardInstance().depotIdMap.get(depot.getId());
		if (newDepot != null) {
			final String[] stringSplit = getSuccessfulSegmentsText(newDepot).split("\\|");
			for (final String stringPart : stringSplit) {
				description.add(Text.literal(stringPart));
			}
		}

		return description;
	}

	private void onOpenColorSelector() {
		colorSelector.setColorCallback(color -> {
			tempColor = color;
			openColorSelectorButton.setBackgroundColor(ColorHelper.fullAlpha(color));
			setRealTimeDeparturesListItems();
		}, tempColor);
		colorSelector.setY((height - colorSelector.getHeight()) / 2);
		enableControls(false);
	}

	private void toggleUseMinecraftTime() {
		useRealTimeCheckbox.isChecked = !useMinecraftTimeCheckbox.isChecked;
		toggleControls();
	}

	private void toggleUseRealTime() {
		useMinecraftTimeCheckbox.isChecked = !useRealTimeCheckbox.isChecked;
		toggleControls();
	}

	private void toggleControls() {
		for (final BetterSliderWidget minecraftTimeSlider : minecraftTimeSliders) {
			minecraftTimeSlider.visible = useMinecraftTimeCheckbox.isChecked;
		}
		realTimeDepartureTextField.visible = useRealTimeCheckbox.isChecked;
		addRealTimeDepartureButton.visible = useRealTimeCheckbox.isChecked;
		realTimeDeparturesListWidget.visible = useRealTimeCheckbox.isChecked;
	}

	private void enableControls(boolean enabled) {
		nameTextField.active = enabled;
		openColorSelectorButton.active = enabled;

		editInstructionsButton.active = enabled;
		instantDeployButton.active = enabled;
		generateRouteButton.active = enabled;
		clearVehiclesButton.active = enabled;
		repeatIndefinitelyCheckbox.active = enabled;

		useMinecraftTimeCheckbox.active = enabled;
		useRealTimeCheckbox.active = enabled;
		for (final BetterSliderWidget minecraftTimeSlider : minecraftTimeSliders) {
			minecraftTimeSlider.active = enabled;
		}
		realTimeDepartureTextField.active = enabled;
		addRealTimeDepartureButton.active = enabled;

		realTimeDeparturesListWidget.active = enabled;
	}

	private void textFieldDepartureCallback(String text) {
		addRealTimeDepartureButton.active = checkRealTimeDeparture(text, false, false);
	}

	private void addRealTimeDeparture() {
		if (checkRealTimeDeparture(realTimeDepartureTextField.getText(), true, false)) {
			realTimeDepartureTextField.setText("");
		}
	}

	private void onDeleteDeparture(String departureString) {
		checkRealTimeDeparture(departureString, false, true);
	}

	private void setRealTimeDeparturesListItems() {
		final long offset = System.currentTimeMillis() / Utilities.MILLIS_PER_DAY * Utilities.MILLIS_PER_DAY;

		final ObjectArrayList<Calendar> sortedDepartures = tempRealTimeDepartures.longStream().mapToObj(departure -> {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(departure + offset);
			return calendar;
		}).sorted(Comparator.comparingInt(calendar -> {
			final int hour = calendar.get(Calendar.HOUR_OF_DAY);
			final int minute = calendar.get(Calendar.MINUTE);
			final int second = calendar.get(Calendar.SECOND);
			return hour * 3600 + minute * 60 + second;
		})).collect(Collectors.toCollection(ObjectArrayList::new));

		final ObjectArrayList<ListItem<RealTimeDepartureForList>> realTimeDeparturesForList = new ObjectArrayList<>();
		for (int i = 0; i < sortedDepartures.size(); i++) {
			final Calendar calendar = sortedDepartures.get(i);
			final int hour = calendar.get(Calendar.HOUR_OF_DAY);
			final int minute = calendar.get(Calendar.MINUTE);
			final int second = calendar.get(Calendar.SECOND);
			final String departureString = String.format("%02d:%02d:%02d", hour, minute, second);
			realTimeDeparturesForList.add(ListItem.createChild(
					(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor(ColorHelper.fullAlpha(tempColor)).draw(),
					null,
					GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
					new RealTimeDepartureForList(calendar.getTimeInMillis() - offset, departureString, i),
					departureString,
					ObjectArrayList.of(
							new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (index, realTimeDepartureForList) -> onDeleteDeparture(realTimeDepartureForList.departureString))
					))
			);
		}

		realTimeDeparturesListWidget.setData(realTimeDeparturesForList);
	}

	private boolean checkRealTimeDeparture(String text, boolean addToList, boolean removeFromList) {
		try {
			final String[] departureSplit = text.replace(" ", "").split("\\+");
			final String[] timeSplit1 = departureSplit[0].split(":");
			final Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit1[0]) % 24);
			calendar.set(Calendar.MINUTE, Integer.parseInt(timeSplit1[1]) % 60);
			calendar.set(Calendar.SECOND, Integer.parseInt(timeSplit1[2]) % 60);
			calendar.set(Calendar.MILLISECOND, 0);
			final int departure = (int) (calendar.getTimeInMillis() % Utilities.MILLIS_PER_DAY);
			final int multiple;
			final int interval;

			if (departureSplit.length > 1) {
				final String[] intervalSplit = departureSplit[1].split("\\*");
				multiple = Integer.parseInt(intervalSplit[0]) + 1;
				final String[] timeSplit2 = intervalSplit[1].split(":");
				interval = (Integer.parseInt(timeSplit2[0]) * 3600 + Integer.parseInt(timeSplit2[1]) * 60 + Integer.parseInt(timeSplit2[2])) * 1000;
			} else {
				multiple = 1;
				interval = 0;
			}

			if (addToList || removeFromList) {
				for (int i = 0; i < multiple; i++) {
					final int rawDeparture = (departure + i * interval) % Utilities.MILLIS_PER_DAY;
					if (addToList) {
						if (!tempRealTimeDepartures.contains(rawDeparture)) {
							tempRealTimeDepartures.add(rawDeparture);
						}
					} else {
						tempRealTimeDepartures.rem(rawDeparture);
					}
				}
				setRealTimeDeparturesListItems();
			}

			return true;
		} catch (Exception ignored) {
		}

		return false;
	}

	private static String getSuccessfulSegmentsText(Depot depot) {
		final long lastGeneratedMillis = depot.getLastGeneratedMillis();
		if (lastGeneratedMillis == 0) {
			return "";
		}

		final long generationStartTime = DEPOT_GENERATION_START_TIME.getOrDefault(depot.getId(), 0);
		if (generationStartTime > lastGeneratedMillis) {
			return TranslationProvider.GUI_MTR_PATH_GENERATING.getString(getTimeDifferenceString(System.currentTimeMillis() - generationStartTime));
		}

		final StringBuilder stringBuilder = new StringBuilder(TranslationProvider.GUI_MTR_PATH_REFRESH_TIME.getString(getTimeDifferenceString(Math.max(0, System.currentTimeMillis() - lastGeneratedMillis)))).append("|").append(DateFormat.getDateTimeInstance().format(new Date(lastGeneratedMillis))).append("||");

		switch (depot.getLastGeneratedStatus()) {
			case SUCCESSFUL:
				stringBuilder.append(TranslationProvider.GUI_MTR_PATH_FOUND.getString());
				break;
			case NO_SIDINGS:
				stringBuilder.append(TranslationProvider.GUI_MTR_PATH_NOT_GENERATED_NO_SIDINGS.getString());
				break;
			case TWO_PLATFORMS_REQUIRED:
				stringBuilder.append(TranslationProvider.GUI_MTR_PATH_NOT_GENERATED_PLATFORMS.getString());
				break;
		}

		depot.getFailedPlatformIds((lastGeneratedFailedStartId, lastGeneratedFailedEndId) -> stringBuilder.append(TranslationProvider.GUI_MTR_PATH_NOT_FOUND_BETWEEN.getString(
				getRoute(depot, lastGeneratedFailedStartId, lastGeneratedFailedEndId),
				getStation(lastGeneratedFailedStartId),
				getStation(lastGeneratedFailedEndId)
		)), lastGeneratedFailedSidingCount -> stringBuilder.append("|").append(TranslationProvider.GUI_MTR_PATH_NOT_FOUND_SIDINGS.getString(lastGeneratedFailedSidingCount)));

		return stringBuilder.toString();
	}

	private static String getSliderString(int value) {
		final String headwayText;
		if (value == 0) {
			headwayText = "";
		} else {
			headwayText = String.format(" (%s%s)", Utilities.round((float) FREQUENCY_MULTIPLIER * MTR.SECONDS_PER_MC_HOUR / value, 1), TranslationProvider.GUI_MTR_S.getString());
		}
		return value / (float) FREQUENCY_MULTIPLIER + TranslationProvider.GUI_MTR_TPH.getString() + headwayText;
	}

	private static String getTimeDifferenceString(long timeDifference) {
		final MutableText mutableText;
		final long newTimeDifference = Math.abs(timeDifference);
		if (newTimeDifference >= Utilities.MILLIS_PER_DAY) {
			mutableText = TranslationProvider.GUI_MTR_DAYS.getMutableText(newTimeDifference / Utilities.MILLIS_PER_DAY);
		} else if (newTimeDifference >= Utilities.MILLIS_PER_HOUR) {
			mutableText = TranslationProvider.GUI_MTR_HOURS.getMutableText(newTimeDifference / Utilities.MILLIS_PER_HOUR);
		} else if (newTimeDifference >= Utilities.MILLIS_PER_MINUTE) {
			mutableText = TranslationProvider.GUI_MTR_MINUTES.getMutableText(newTimeDifference / Utilities.MILLIS_PER_MINUTE);
		} else {
			mutableText = TranslationProvider.GUI_MTR_SECONDS.getMutableText(newTimeDifference / Utilities.MILLIS_PER_SECOND);
		}
		return mutableText.getString();
	}

	private static String getRoute(Depot depot, long lastGeneratedFailedStartId, long lastGeneratedFailedEndId) {
		long previousId = 0;
		String previousRouteName = "";
		for (final Route route : depot.routes) {
			for (final RoutePlatformData routePlatform : route.getRoutePlatforms()) {
				final long thisId = routePlatform.platform.getId();
				if (previousId == lastGeneratedFailedStartId && thisId == lastGeneratedFailedEndId) {
					return IGui.formatStationName(previousRouteName);
				}
				previousId = thisId;
			}
			previousRouteName = route.getName();
		}
		return IGui.formatStationName("");
	}

	private static String getStation(long platformId) {
		final Platform platform = MinecraftClientData.getDashboardInstance().platformIdMap.get(platformId);
		final Station station = platform == null ? null : platform.area;
		return IGui.formatStationName(station == null ? "" : station.getName());
	}

	private record RealTimeDepartureForList(long departure, String departureString, int index) {
	}
}
