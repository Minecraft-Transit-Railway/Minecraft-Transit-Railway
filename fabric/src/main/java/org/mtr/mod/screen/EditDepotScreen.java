package org.mtr.mod.screen;

import org.apache.commons.lang3.StringUtils;
import org.mtr.core.data.*;
import org.mtr.core.operation.DepotOperationByIds;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2LongAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketDepotClear;
import org.mtr.mod.packet.PacketDepotGenerate;
import org.mtr.mod.packet.PacketDepotInstantDeploy;
import org.mtr.mod.packet.PacketUpdateData;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class EditDepotScreen extends EditNameColorScreenBase<Depot> {

	private final int sliderX;
	private final int sliderWidthWithText;
	private final int rightPanelsX;
	private final boolean showScheduleControls;
	private final boolean showCruisingAltitude;

	private final ButtonWidgetExtension buttonUseRealTime;
	private final ButtonWidgetExtension buttonReset;
	private final WidgetShorterSlider[] sliders = new WidgetShorterSlider[HOURS_PER_DAY];
	private final TextFieldWidgetExtension textFieldDeparture;
	private final ButtonWidgetExtension buttonAddDeparture;

	private final ButtonWidgetExtension buttonEditInstructions;
	private final ButtonWidgetExtension buttonInstantDeploy;
	private final ButtonWidgetExtension buttonGenerateRoute;
	private final ButtonWidgetExtension buttonClearTrains;
	private final CheckboxWidgetExtension checkboxRepeatIndefinitely;
	private final TextFieldWidgetExtension textFieldCruisingAltitude;
	private final DashboardList departuresList;

	private final MutableText cruisingAltitudeText = TranslationProvider.GUI_MTR_CRUISING_ALTITUDE.getMutableText();
	private static final int PANELS_START = SQUARE_SIZE * 2 + TEXT_FIELD_PADDING;
	private static final int SLIDER_WIDTH = 64;
	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int DEFAULT_CRUISING_ALTITUDE = 256;
	private static final int TRAIN_FREQUENCY_MULTIPLIER = 4;
	private static final Long2LongAVLTreeMap DEPOT_GENERATION_START_TIME = new Long2LongAVLTreeMap();

	public EditDepotScreen(Depot depot, TransportMode transportMode, ScreenExtension previousScreenExtension) {
		super(depot, TranslationProvider.GUI_MTR_DEPOT_NAME, TranslationProvider.GUI_MTR_DEPOT_COLOR, previousScreenExtension);

		sliderX = GraphicsHolder.getTextWidth(getTimeString(0)) + TEXT_PADDING * 2;
		sliderWidthWithText = SLIDER_WIDTH + TEXT_PADDING + GraphicsHolder.getTextWidth(getSliderString(0));
		rightPanelsX = sliderX + SLIDER_WIDTH + TEXT_PADDING * 2 + GraphicsHolder.getTextWidth(getSliderString(1));
		showScheduleControls = !transportMode.continuousMovement;
		showCruisingAltitude = transportMode == TransportMode.AIRPLANE;
		buttonUseRealTime = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_SCHEDULE_MODE_REAL_TIME_OFF.getMutableText(), button -> {
			depot.setUseRealTime(!depot.getUseRealTime());
			toggleRealTime();
			saveData();
		});
		buttonReset = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_RESET_SIGN.getMutableText(), button -> {
			for (int i = 0; i < HOURS_PER_DAY; i++) {
				sliders[i].setValue(0);
			}
			data.getRealTimeDepartures().clear();
			updateList();
			saveData();
		});

		for (int i = 0; i < HOURS_PER_DAY; i++) {
			final int currentIndex = i;
			sliders[currentIndex] = new WidgetShorterSlider(sliderX, SLIDER_WIDTH, MAX_TRAINS_PER_HOUR * 2, EditDepotScreen::getSliderString, value -> {
				for (int j = 0; j < HOURS_PER_DAY; j++) {
					if (j != currentIndex) {
						sliders[j].setValue(value);
					}
				}
			});
		}
		departuresList = new DashboardList(null, null, null, null, null, this::onDeleteDeparture, null, () -> "", text -> {
		});

		textFieldDeparture = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 25, TextCase.DEFAULT, "[^\\d:+* ]", "07:10:00 + 10 * 00:03:00");
		buttonAddDeparture = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> {
			checkDeparture(textFieldDeparture.getText2(), true, false);
			saveData();
		});
		buttonEditInstructions = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_EDIT_INSTRUCTIONS.getMutableText(), button -> {
			saveData();
			final ObjectArrayList<DashboardListItem> routes = new ObjectArrayList<>(MinecraftClientData.getFilteredDataSet(transportMode, MinecraftClientData.getDashboardInstance().routes));
			Collections.sort(routes);
			MinecraftClient.getInstance().openScreen(new Screen(new DashboardListSelectorScreen(new ObjectImmutableList<>(routes), data.getRouteIds(), false, true, this)));
		});
		buttonInstantDeploy = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_INSTANT_DEPLOY.getMutableText(), button -> {
			saveData();
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDepotInstantDeploy(depotOperationByIds));
		});
		buttonGenerateRoute = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_REFRESH_PATH.getMutableText(), button -> {
			saveData();
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			DEPOT_GENERATION_START_TIME.put(depot.getId(), System.currentTimeMillis());
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDepotGenerate(depotOperationByIds));
		});
		buttonClearTrains = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_CLEAR_VEHICLES.getMutableText(), button -> {
			saveData();
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDepotClear(depotOperationByIds));
		});
		checkboxRepeatIndefinitely = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, button -> {
			saveData();
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDepotGenerate(depotOperationByIds));
		});
		checkboxRepeatIndefinitely.setMessage2(TranslationProvider.GUI_MTR_REPEAT_INDEFINITELY.getText());
		textFieldCruisingAltitude = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 5, TextCase.DEFAULT, "[^-\\d]", String.valueOf(DEFAULT_CRUISING_ALTITUDE));
	}

	@Override
	protected void init2() {
		setPositionsAndInit(rightPanelsX, width / 4 * 3, width);

		final int buttonWidth = (width - rightPanelsX) / 2;
		IDrawing.setPositionAndWidth(buttonEditInstructions, rightPanelsX, PANELS_START, buttonWidth * 2);
		IDrawing.setPositionAndWidth(buttonInstantDeploy, rightPanelsX, PANELS_START + SQUARE_SIZE, buttonWidth * 2);
		IDrawing.setPositionAndWidth(buttonGenerateRoute, rightPanelsX, PANELS_START + SQUARE_SIZE * 2, buttonWidth * (showScheduleControls ? 1 : 2));
		IDrawing.setPositionAndWidth(buttonClearTrains, rightPanelsX + buttonWidth, PANELS_START + SQUARE_SIZE * 2, buttonWidth);
		IDrawing.setPositionAndWidth(checkboxRepeatIndefinitely, rightPanelsX, PANELS_START + SQUARE_SIZE * 3 + (showCruisingAltitude ? SQUARE_SIZE + TEXT_FIELD_PADDING : 0), buttonWidth * 2);
		checkboxRepeatIndefinitely.setChecked(data.getRepeatInfinitely());

		final int cruisingAltitudeTextWidth = GraphicsHolder.getTextWidth(cruisingAltitudeText) + TEXT_PADDING * 2;
		IDrawing.setPositionAndWidth(textFieldCruisingAltitude, rightPanelsX + Math.min(cruisingAltitudeTextWidth, buttonWidth * 2 - SQUARE_SIZE * 3) + TEXT_FIELD_PADDING / 2, PANELS_START + SQUARE_SIZE * 3 + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 3 - TEXT_FIELD_PADDING);
		textFieldCruisingAltitude.setText2(String.valueOf(data.getCruisingAltitude()));

		if (showScheduleControls) {
			for (WidgetShorterSlider slider : sliders) {
				addChild(new ClickableWidget(slider));
			}
		}
		for (int i = 0; i < HOURS_PER_DAY; i++) {
			sliders[i].setValue((int) data.getFrequency(i));
		}

		final int leftWidth = rightPanelsX - 1;
		IDrawing.setPositionAndWidth(buttonUseRealTime, 0, 0, leftWidth - SQUARE_SIZE * 3);
		IDrawing.setPositionAndWidth(buttonReset, leftWidth - SQUARE_SIZE * 3, 0, SQUARE_SIZE * 3);

		departuresList.y = SQUARE_SIZE;
		departuresList.height = height - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING;
		departuresList.width = leftWidth;
		departuresList.init(this::addChild);

		IDrawing.setPositionAndWidth(textFieldDeparture, TEXT_FIELD_PADDING / 2, height - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, leftWidth - TEXT_FIELD_PADDING - SQUARE_SIZE);
		addChild(new ClickableWidget(textFieldDeparture));
		textFieldDeparture.setChangedListener2(text -> buttonAddDeparture.active = checkDeparture(text, false, false));
		IDrawing.setPositionAndWidth(buttonAddDeparture, leftWidth - SQUARE_SIZE, height - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, SQUARE_SIZE);
		addChild(new ClickableWidget(buttonAddDeparture));
		buttonAddDeparture.active = false;

		addChild(new ClickableWidget(buttonEditInstructions));
		addChild(new ClickableWidget(buttonInstantDeploy));
		addChild(new ClickableWidget(buttonGenerateRoute));
		if (showScheduleControls) {
			addChild(new ClickableWidget(buttonUseRealTime));
			addChild(new ClickableWidget(buttonReset));
			addChild(new ClickableWidget(buttonClearTrains));
			addChild(new ClickableWidget(checkboxRepeatIndefinitely));
		}
		if (showCruisingAltitude) {
			addChild(new ClickableWidget(textFieldCruisingAltitude));
		}

		toggleRealTime();
	}

	@Override
	public void tick2() {
		super.tick2();
		departuresList.tick();
		textFieldDeparture.tick2();
		textFieldCruisingAltitude.tick2();

		for (int i = 0; i < HOURS_PER_DAY; i++) {
			data.setFrequency(i, sliders[i].getIntValue());
		}

		if (data.routes.isEmpty()) {
			checkboxRepeatIndefinitely.visible = false;
		} else {
			final Route firstRoute = MinecraftClientData.getDashboardInstance().routeIdMap.get(Utilities.getElement(data.routes, 0).getId());
			final Route lastRoute = MinecraftClientData.getDashboardInstance().routeIdMap.get(Utilities.getElement(data.routes, -1).getId());
			checkboxRepeatIndefinitely.visible = firstRoute != null && lastRoute != null && !firstRoute.getRoutePlatforms().isEmpty() && !lastRoute.getRoutePlatforms().isEmpty() && Utilities.getElement(firstRoute.getRoutePlatforms(), 0).getPlatform().getId() == Utilities.getElement(lastRoute.getRoutePlatforms(), -1).getPlatform().getId();
		}
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(rightPanelsX - 1, 0, rightPanelsX, height, ARGB_WHITE_TRANSLUCENT);
		guiDrawing.finishDrawingRectangle();
		renderTextFields(graphicsHolder);

		if (showScheduleControls && data.getUseRealTime()) {
			departuresList.render(graphicsHolder);
		}

		final int lineHeight = Math.min(SQUARE_SIZE, (height - SQUARE_SIZE * 2) / HOURS_PER_DAY);
		for (int i = 0; i < HOURS_PER_DAY; i++) {
			if (showScheduleControls && !data.getUseRealTime()) {
				graphicsHolder.drawText(getTimeString(i), TEXT_PADDING, SQUARE_SIZE * 2 + lineHeight * i + (int) ((lineHeight - TEXT_HEIGHT) / 2F), ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			}
			sliders[i].setY2(SQUARE_SIZE * 2 + lineHeight * i);
			sliders[i].setHeight(lineHeight);
		}

		super.render(graphicsHolder, mouseX, mouseY, delta);

		final int yStartRightPane = PANELS_START + SQUARE_SIZE * (checkboxRepeatIndefinitely.visible ? 4 : 3) + (showCruisingAltitude ? SQUARE_SIZE + TEXT_FIELD_PADDING : 0) + TEXT_PADDING;
		if (showCruisingAltitude) {
			graphicsHolder.drawText(cruisingAltitudeText, rightPanelsX + TEXT_PADDING, PANELS_START + SQUARE_SIZE * 3 + TEXT_PADDING + TEXT_FIELD_PADDING / 2, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
		}
		graphicsHolder.drawText(TranslationProvider.GUI_MTR_SIDINGS_IN_DEPOT.getMutableText(data.savedRails.size()), rightPanelsX + TEXT_PADDING, yStartRightPane, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());

		// Temporary workaround to get the latest depot path generation status
		final Depot newDepot = MinecraftClientData.getDashboardInstance().depotIdMap.get(data.getId());
		if (newDepot != null) {
			final String[] stringSplit = getSuccessfulSegmentsText(newDepot).split("\\|");
			for (int i = 0; i < stringSplit.length; i++) {
				graphicsHolder.drawText(stringSplit[i], rightPanelsX + TEXT_PADDING, yStartRightPane + SQUARE_SIZE * 2 + (TEXT_HEIGHT + TEXT_PADDING) * i, ARGB_WHITE, false, GraphicsHolder.getDefaultLight());
			}
		}

		if (showScheduleControls && !data.getUseRealTime()) {
			graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_GAME_TIME.getMutableText(), sliderX / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_LIGHT_GRAY);
			graphicsHolder.drawCenteredText(TranslationProvider.GUI_MTR_VEHICLES_PER_HOUR.getMutableText(), sliderX + sliderWidthWithText / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_LIGHT_GRAY);
		}
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		departuresList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
		departuresList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled2(mouseX, mouseY, amount);
	}

	@Override
	protected void saveData() {
		super.saveData();
		data.setRepeatInfinitely(checkboxRepeatIndefinitely.visible && checkboxRepeatIndefinitely.isChecked2());
		try {
			data.setCruisingAltitude(Integer.parseInt(textFieldCruisingAltitude.getText2()));
		} catch (Exception e) {
			Init.LOGGER.error("", e);
			data.setCruisingAltitude(DEFAULT_CRUISING_ALTITUDE);
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot(data)));
	}

	private void toggleRealTime() {
		for (final WidgetShorterSlider slider : sliders) {
			slider.visible = !data.getUseRealTime();
		}
		departuresList.x = data.getUseRealTime() ? 0 : width;
		textFieldDeparture.setX2(data.getUseRealTime() ? TEXT_FIELD_PADDING / 2 : width);
		buttonAddDeparture.visible = data.getUseRealTime();
		buttonUseRealTime.setMessage2((data.getUseRealTime() ? TranslationProvider.GUI_MTR_SCHEDULE_MODE_REAL_TIME_ON : TranslationProvider.GUI_MTR_SCHEDULE_MODE_REAL_TIME_OFF).getText());
		updateList();
	}

	private void onDeleteDeparture(DashboardListItem dashboardListItem, int index) {
		checkDeparture(dashboardListItem.getName(false), false, true);
		saveData();
	}

	private void updateList() {
		final ObjectArrayList<DashboardListItem> departureData = new ObjectArrayList<>();
		final long offset = System.currentTimeMillis() / MILLIS_PER_DAY * MILLIS_PER_DAY;
		data.getRealTimeDepartures().longStream().mapToObj(departure -> {
			final Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(departure + offset);
			return calendar;
		}).sorted(Comparator.comparingInt(calendar -> {
			final int hour = calendar.get(Calendar.HOUR_OF_DAY);
			final int minute = calendar.get(Calendar.MINUTE);
			final int second = calendar.get(Calendar.SECOND);
			return hour * 3600 + minute * 60 + second;
		})).forEach(calendar -> {
			final int hour = calendar.get(Calendar.HOUR_OF_DAY);
			final int minute = calendar.get(Calendar.MINUTE);
			final int second = calendar.get(Calendar.SECOND);
			departureData.add(new DashboardListItem(0, String.format("%2s:%2s:%2s", hour, minute, second).replace(' ', '0'), 0));
		});
		departuresList.setData(departureData, false, false, false, false, false, true);
	}

	private boolean checkDeparture(String text, boolean addToList, boolean removeFromList) {
		try {
			final String[] departureSplit = text.replace(" ", "").split("\\+");
			final String[] timeSplit1 = departureSplit[0].split(":");
			final Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit1[0]) % 24);
			calendar.set(Calendar.MINUTE, Integer.parseInt(timeSplit1[1]) % 60);
			calendar.set(Calendar.SECOND, Integer.parseInt(timeSplit1[2]) % 60);
			calendar.set(Calendar.MILLISECOND, 0);
			final int departure = (int) (calendar.getTimeInMillis() % MILLIS_PER_DAY);
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
					final int rawDeparture = (departure + i * interval) % MILLIS_PER_DAY;
					if (addToList) {
						if (!data.getRealTimeDepartures().contains(rawDeparture)) {
							data.getRealTimeDepartures().add(rawDeparture);
						}
					} else {
						data.getRealTimeDepartures().rem(rawDeparture);
					}
				}
				updateList();
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

		final StringBuilder stringBuilder = new StringBuilder(TranslationProvider.GUI_MTR_PATH_REFRESH_TIME.getString(getTimeDifferenceString(System.currentTimeMillis() - lastGeneratedMillis))).append("|").append(DateFormat.getDateTimeInstance().format(new Date(lastGeneratedMillis))).append("||");

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
			headwayText = " (" + Utilities.round((float) TRAIN_FREQUENCY_MULTIPLIER * Init.SECONDS_PER_MC_HOUR / value, 1) + TranslationProvider.GUI_MTR_S.getString() + ")";
		}
		return value / (float) TRAIN_FREQUENCY_MULTIPLIER + TranslationProvider.GUI_MTR_TPH.getString() + headwayText;
	}

	private static String getTimeString(int hour) {
		final String hourString = StringUtils.leftPad(String.valueOf(hour), 2, "0");
		return String.format("%s:00-%s:59", hourString, hourString);
	}

	private static String getTimeDifferenceString(long timeDifference) {
		final MutableText mutableText;
		final long newTimeDifference = Math.abs(timeDifference);
		if (newTimeDifference >= HOURS_PER_DAY * 60 * 60 * MILLIS_PER_SECOND) {
			mutableText = TranslationProvider.GUI_MTR_DAYS.getMutableText(newTimeDifference / (HOURS_PER_DAY * 60 * 60 * MILLIS_PER_SECOND));
		} else if (newTimeDifference >= 60 * 60 * MILLIS_PER_SECOND) {
			mutableText = TranslationProvider.GUI_MTR_HOURS.getMutableText(newTimeDifference / (60 * 60 * MILLIS_PER_SECOND));
		} else if (newTimeDifference >= 60 * MILLIS_PER_SECOND) {
			mutableText = TranslationProvider.GUI_MTR_MINUTES.getMutableText(newTimeDifference / (60 * MILLIS_PER_SECOND));
		} else {
			mutableText = TranslationProvider.GUI_MTR_SECONDS.getMutableText(newTimeDifference / MILLIS_PER_SECOND);
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
}
