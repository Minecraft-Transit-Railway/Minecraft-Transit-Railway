package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMinecraft;
import gg.essential.universal.utils.ReleasedDynamicTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.DepotOperationByIds;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.Long2LongAVLTreeMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketDepotClear;
import org.mtr.packet.PacketDepotGenerate;
import org.mtr.packet.PacketDepotInstantDeploy;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.tool.ReleasedDynamicTextureRegistry;
import org.mtr.widget.*;

import java.awt.*;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;

public final class DepotScreen extends NameColorDataScreenBase<Depot> {

	private String oldSuccessfulSegmentsText = "";

	private final UIWrappedText depotInstructionsLabel;
	private final CheckboxComponent repeatIndefinitelyCheckbox;
	private final CheckboxComponent useMinecraftTimeCheckbox;
	private final CheckboxComponent useRealTimeCheckbox;
	private final MultiLineTextWidget multiLineTextWidget;
	private final ScrollComponent minecraftScheduleScrollComponent;
	private final UIContainer realTimeScheduleContainer;
	private final Grouped24HourSlidersComponent minecraftTimeSliders;
	private final TextInputComponent realTimeDepartureTextInput;
	private final ButtonComponent addRealTimeDepartureButton;
	private final ListComponent<RealTimeDepartureForList> realTimeDeparturesListComponent;

	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int FREQUENCY_MULTIPLIER = 4;
	private static final Long2LongAVLTreeMap DEPOT_GENERATION_START_TIME = new Long2LongAVLTreeMap();

	public DepotScreen(Depot depot, @Nullable WindowBase previousScreen) {
		super(depot, getTabs(depot), TranslationProvider.GUI_MTR_DEPOT_NAME, name -> TranslationProvider.GUI_MTR_DEPOT.getString(Utilities.formatName(name)), TranslationProvider.GUI_MTR_DEPOT_COLOR, previousScreen);
		depotInstructionsLabel = GuiHelper.createLabel(firstTabScrollComponent, "");

		final ButtonComponent editInstructionsButton = (ButtonComponent) new ButtonComponent(true)
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		editInstructionsButton.setText(TranslationProvider.GUI_MTR_EDIT_INSTRUCTIONS.getString());
		editInstructionsButton.onClick(() -> UMinecraft.setCurrentScreenObj(createRouteListSelectorScreen()));

		final UIContainer buttonsContainer = (UIContainer) new UIContainer()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		final ButtonComponent generateRouteButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonsContainer)
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 1F / 3));

		generateRouteButton.setText(TranslationProvider.GUI_MTR_REFRESH_PATH.getString());
		generateRouteButton.onClick(this::refreshPath);

		final ButtonComponent instantDeployButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonsContainer)
			.setX(new SiblingConstraint())
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 1F / 3));

		instantDeployButton.setText(TranslationProvider.GUI_MTR_INSTANT_DEPLOY.getString());
		instantDeployButton.onClick(() -> {
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			RegistryClient.sendPacketToServer(new PacketDepotInstantDeploy(depotOperationByIds));
		});

		final ButtonComponent clearVehiclesButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonsContainer)
			.setX(new SiblingConstraint())
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 1F / 3));

		clearVehiclesButton.setText(TranslationProvider.GUI_MTR_CLEAR_VEHICLES.getString());
		clearVehiclesButton.onClick(() -> {
			final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
			depotOperationByIds.addDepotId(depot.getId());
			RegistryClient.sendPacketToServer(new PacketDepotClear(depotOperationByIds));
		});

		GuiHelper.createSpacing(firstTabScrollComponent);

		repeatIndefinitelyCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		repeatIndefinitelyCheckbox.setText(TranslationProvider.GUI_MTR_REPEAT_INDEFINITELY.getString());
		repeatIndefinitelyCheckbox.setChecked(depot.getRepeatInfinitely());
		repeatIndefinitelyCheckbox.onClick(this::refreshPath);

		GuiHelper.createSpacing(firstTabScrollComponent);

		multiLineTextWidget = (MultiLineTextWidget) new MultiLineTextWidget()
			.setChildOf(firstTabScrollComponent)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		final UIContainer scheduleTabContainer = depot.getTransportMode().continuousMovement ? new UIContainer() : backgroundComponent.containers[2];

		useMinecraftTimeCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(scheduleTabContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		useMinecraftTimeCheckbox.setText(TranslationProvider.GUI_MTR_SCHEDULE_MODE_MINECRAFT_TIME.getString());
		useMinecraftTimeCheckbox.setChecked(!depot.getUseRealTime());
		useMinecraftTimeCheckbox.onClick(this::toggleUseMinecraftTime);

		useRealTimeCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(scheduleTabContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		useRealTimeCheckbox.setText(TranslationProvider.GUI_MTR_SCHEDULE_MODE_REAL_TIME.getString());
		useRealTimeCheckbox.setChecked(depot.getUseRealTime());
		useRealTimeCheckbox.onClick(this::toggleUseRealTime);

		final UIContainer scheduleContainer = (UIContainer) new UIContainer()
			.setChildOf(scheduleTabContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

		minecraftScheduleScrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(scheduleContainer)
			.setWidth(new RelativeConstraint())
			.setHeight(new RelativeConstraint())).contentContainer;

		minecraftTimeSliders = (Grouped24HourSlidersComponent) new Grouped24HourSlidersComponent(MAX_TRAINS_PER_HOUR * 2, Minecraft.getInstance().font.width(getSliderString(1)), DepotScreen::getSliderString)
			.setChildOf(minecraftScheduleScrollComponent)
			.setWidth(new RelativeConstraint());

		for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
			minecraftTimeSliders.setValue(i, (int) depot.getFrequency(i));
		}

		realTimeScheduleContainer = (UIContainer) new UIContainer()
			.setChildOf(scheduleContainer)
			.setWidth(new RelativeConstraint())
			.setHeight(new RelativeConstraint());

		final UIContainer addRealTimeDepartureContainer = (UIContainer) new UIContainer()
			.setChildOf(realTimeScheduleContainer)
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		realTimeDepartureTextInput = (TextInputComponent) new TextInputComponent()
			.setChildOf(addRealTimeDepartureContainer)
			.setWidth(new ScaleConstraint(new RelativeConstraint(), 0.6F))
			.setHeight(new PixelConstraint(20));

		realTimeDepartureTextInput.setMaxLength(25);
		realTimeDepartureTextInput.setFilter("[^\\d:+* ]");
		realTimeDepartureTextInput.setPlaceholderText("07:10:00 + 10 * 00:03:00");
		realTimeDepartureTextInput.onChange(this::textFieldDepartureCallback);

		addRealTimeDepartureButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(addRealTimeDepartureContainer)
			.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new SubtractiveConstraint(new ScaleConstraint(new RelativeConstraint(), 0.4F), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

		addRealTimeDepartureButton.setText(TranslationProvider.GUI_MTR_ADD_DEPARTURE.getString());
		addRealTimeDepartureButton.onClick(this::addRealTimeDeparture);

		final SlotBackgroundComponent slotBackgroundComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
			.setChildOf(realTimeScheduleContainer)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

		realTimeDeparturesListComponent = GuiHelper.createListComponent(slotBackgroundComponent);

		toggleControls();
		updateRoutes();
		setRealTimeDeparturesListItems();
	}

	@Override
	public void onTick() {
		super.onTick();

		// Temporary workaround to get the latest depot path generation status
		final Depot newDepot = MinecraftClientData.getDashboardInstance().depotIdMap.get(data.getId());
		if (newDepot != null) {
			final String successfulSegmentsText = getSuccessfulSegmentsText(newDepot);
			if (!successfulSegmentsText.equals(oldSuccessfulSegmentsText)) {
				final ObjectArrayList<ObjectArrayList<ObjectObjectImmutablePair<String, @Nullable Color>>> lines = new ObjectArrayList<>();
				for (final String line : successfulSegmentsText.split("\\|")) {
					lines.add(ObjectArrayList.of(new ObjectObjectImmutablePair<>(line, null)));
				}
				multiLineTextWidget.write(lines);
				oldSuccessfulSegmentsText = successfulSegmentsText;
			}
		}
	}

	@Override
	protected void close() {
		data.setRepeatInfinitely(shouldShowRepeatIndefinitelyCheckbox() && repeatIndefinitelyCheckbox.isChecked());
		data.setUseRealTime(useRealTimeCheckbox.isChecked());

		if (!useRealTimeCheckbox.isChecked()) {
			for (int i = 0; i < Utilities.HOURS_PER_DAY; i++) {
				data.setFrequency(i, minecraftTimeSliders.getValues(i));
			}
		}

		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot(data)));
	}

	private void refreshPath() {
		final DepotOperationByIds depotOperationByIds = new DepotOperationByIds();
		depotOperationByIds.addDepotId(data.getId());
		DEPOT_GENERATION_START_TIME.put(data.getId(), System.currentTimeMillis());
		RegistryClient.sendPacketToServer(new PacketDepotGenerate(depotOperationByIds));
	}

	private void updateRoutes() {
		depotInstructionsLabel.setText(TranslationProvider.GUI_MTR_DEPOT_INSTRUCTIONS.getString(data.getRouteIds().size()));
		if (shouldShowRepeatIndefinitelyCheckbox()) {
			repeatIndefinitelyCheckbox.unhide(true);
		} else {
			repeatIndefinitelyCheckbox.hide(true);
		}
	}

	private boolean shouldShowRepeatIndefinitelyCheckbox() {
		if (data.getTransportMode().continuousMovement || data.getRouteIds().isEmpty()) {
			return false;
		} else {
			final Route firstRoute = MinecraftClientData.getDashboardInstance().routeIdMap.get((long) data.getRouteIds().getFirst());
			final Route lastRoute = MinecraftClientData.getDashboardInstance().routeIdMap.get((long) data.getRouteIds().getLast());
			return firstRoute != null && lastRoute != null && !firstRoute.getRoutePlatforms().isEmpty() && !lastRoute.getRoutePlatforms().isEmpty() && firstRoute.getRoutePlatforms().getFirst().getPlatform().getId() == lastRoute.getRoutePlatforms().getLast().getPlatform().getId();
		}
	}

	private RouteListSelectorScreen createRouteListSelectorScreen() {
		final RouteListSelectorScreen routeListSelectorScreen = new RouteListSelectorScreen(selectedRoutes -> {
			data.getRouteIds().clear();
			selectedRoutes.forEach(route -> data.getRouteIds().add(route.getId()));
			updateRoutes();
		}, true, true, false, this);

		final ObjectArraySet<Route> routes = MinecraftClientData.getFilteredDataSet(data.getTransportMode(), MinecraftClientData.getDashboardInstance().routes);
		routeListSelectorScreen.setAvailableList(routes);
		data.getRouteIds().forEach(routeId -> routes.stream().filter(route -> route.getId() == routeId).findFirst().ifPresent(routeListSelectorScreen::selectData));
		return routeListSelectorScreen;
	}

	private void toggleUseMinecraftTime() {
		useRealTimeCheckbox.setChecked(!useMinecraftTimeCheckbox.isChecked());
		toggleControls();
	}

	private void toggleUseRealTime() {
		useMinecraftTimeCheckbox.setChecked(!useRealTimeCheckbox.isChecked());
		toggleControls();
	}

	private void toggleControls() {
		if (useRealTimeCheckbox.isChecked()) {
			minecraftScheduleScrollComponent.hide(true);
			realTimeScheduleContainer.unhide(true);
		} else {
			minecraftScheduleScrollComponent.unhide(true);
			realTimeScheduleContainer.hide(true);
		}
	}

	private void textFieldDepartureCallback() {
		addRealTimeDepartureButton.setDisabled(!checkRealTimeDeparture(realTimeDepartureTextInput.getText(), false, false));
	}

	private void addRealTimeDeparture() {
		if (checkRealTimeDeparture(realTimeDepartureTextInput.getText(), true, false)) {
			realTimeDepartureTextInput.setText("");
		}
	}

	private void onDeleteDeparture(String departureString) {
		checkRealTimeDeparture(departureString, false, true);
	}

	private void setRealTimeDeparturesListItems() {
		final long offset = System.currentTimeMillis() / Utilities.MILLIS_PER_DAY * Utilities.MILLIS_PER_DAY;

		final ObjectArrayList<Calendar> sortedDepartures = data.getRealTimeDepartures().longStream().mapToObj(departure -> {
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
				(drawing, x, y) -> drawing.setVerticesWH(x + GuiHelper.DEFAULT_PADDING, y + GuiHelper.DEFAULT_PADDING, GuiHelper.MINECRAFT_FONT_SIZE, GuiHelper.MINECRAFT_FONT_SIZE).setColor((data.getColor() | 0xFF000000)).draw(),
				null,
				GuiHelper.DEFAULT_PADDING + GuiHelper.MINECRAFT_FONT_SIZE,
				new RealTimeDepartureForList(calendar.getTimeInMillis() - offset, departureString, i),
				departureString,
				ObjectArrayList.of(
					new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, realTimeDepartureForList) -> onDeleteDeparture(realTimeDepartureForList.departureString))
				))
			);
		}

		realTimeDeparturesListComponent.setData(realTimeDeparturesForList);
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
						if (!data.getRealTimeDepartures().contains(rawDeparture)) {
							data.getRealTimeDepartures().add(rawDeparture);
						}
					} else {
						data.getRealTimeDepartures().rem(rawDeparture);
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
		return TranslationProvider.GUI_MTR_VEHICLES_PER_HOUR_VALUE.getString((float) value / FREQUENCY_MULTIPLIER) + headwayText;
	}

	private static String getTimeDifferenceString(long timeDifference) {
		final MutableComponent mutableText;
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

	private static ObjectImmutableList<ObjectObjectImmutablePair<ReleasedDynamicTexture, String>> getTabs(Depot depot) {
		if (depot.getTransportMode().continuousMovement) {
			return ObjectImmutableList.of(
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION.getString()),
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION_COLOR.getString())
			);
		} else {
			return ObjectImmutableList.of(
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.BRUSH_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION.getString()),
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.POPPY_TEXTURE.get(), TranslationProvider.GUI_MTR_STATION_COLOR.getString()),
				new ObjectObjectImmutablePair<>(ReleasedDynamicTextureRegistry.CLOCK_TEXTURE.get(), TranslationProvider.GUI_MTR_DEPARTURES.getString())
			);
		}
	}

	private record RealTimeDepartureForList(long departure, String departureString, int index) {
	}
}
