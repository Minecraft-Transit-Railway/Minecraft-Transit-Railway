package mtr.gui;

import mtr.data.*;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditDepotScreen extends EditNameColorScreenBase<Depot> {

	private boolean addingRoute;

	private final int sliderX;
	private final int sliderWidthWithText;
	private final int rightPanelsX;
	private final Map<Long, Siding> sidingsInDepot;

	private final WidgetShorterSlider[] sliders = new WidgetShorterSlider[Depot.HOURS_IN_DAY];
	private final ButtonWidget buttonEditInstructions;
	private final ButtonWidget buttonGenerateRoute;
	private final ButtonWidget buttonClearTrains;
	private final ButtonWidget buttonDone;

	private final DashboardList addNewList;
	private final DashboardList routeList;

	private static final int PANELS_START = SQUARE_SIZE * 2 + TEXT_FIELD_PADDING;
	private static final int SLIDER_WIDTH = 64;
	private static final int MAX_TRAINS_PER_HOUR = 5;
	private static final int SECONDS_PER_MC_HOUR = Depot.TICKS_PER_HOUR / 20;

	public EditDepotScreen(Depot depot, DashboardScreen dashboardScreen) {
		super(depot, dashboardScreen, "gui.mtr.depot_name", "gui.mtr.depot_color");

		sidingsInDepot = ClientData.DATA_CACHE.requestDepotIdToSidings(depot.id);

		textRenderer = MinecraftClient.getInstance().textRenderer;
		sliderX = textRenderer.getWidth(getTimeString(0)) + TEXT_PADDING * 2;
		sliderWidthWithText = SLIDER_WIDTH + TEXT_PADDING + textRenderer.getWidth(getSliderString(0));
		rightPanelsX = sliderX + SLIDER_WIDTH + TEXT_PADDING * 2 + textRenderer.getWidth(getSliderString(1));

		for (int i = 0; i < Depot.HOURS_IN_DAY; i++) {
			sliders[i] = new WidgetShorterSlider(sliderX, SLIDER_WIDTH, MAX_TRAINS_PER_HOUR * 2, EditDepotScreen::getSliderString);
		}

		buttonEditInstructions = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.edit_instructions"), button -> setIsSelecting(true));
		buttonGenerateRoute = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.refresh_path"), button -> {
			depot.clientPathGenerationSuccessfulSegments = -1;
			PacketTrainDataGuiClient.generatePathC2S(depot.id);
		});
		buttonClearTrains = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.clear_trains"), button -> {
			sidingsInDepot.values().forEach(Siding::clearTrains);
			PacketTrainDataGuiClient.clearTrainsC2S(sidingsInDepot.values());
		});
		buttonDone = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> setIsSelecting(false));

		addNewList = new DashboardList(this::addDrawableChild, null, null, null, null, this::onAdded, null, null, () -> ClientData.ROUTES_PLATFORMS_SEARCH, text -> ClientData.ROUTES_PLATFORMS_SEARCH = text);
		routeList = new DashboardList(this::addDrawableChild, null, null, null, this::onSort, null, this::onRemove, () -> depot.routeIds, () -> ClientData.ROUTES_PLATFORMS_SELECTED_SEARCH, text -> ClientData.ROUTES_PLATFORMS_SELECTED_SEARCH = text);
	}

	@Override
	protected void init() {
		setPositionsAndInit(rightPanelsX, width / 4 * 3, width);

		final int buttonWidth = (width - rightPanelsX) / 2;
		IDrawing.setPositionAndWidth(buttonEditInstructions, rightPanelsX, PANELS_START, buttonWidth * 2);
		IDrawing.setPositionAndWidth(buttonGenerateRoute, rightPanelsX, PANELS_START + SQUARE_SIZE, buttonWidth);
		IDrawing.setPositionAndWidth(buttonClearTrains, rightPanelsX + buttonWidth, PANELS_START + SQUARE_SIZE, buttonWidth);
		IDrawing.setPositionAndWidth(buttonDone, (width - PANEL_WIDTH) / 2, height - SQUARE_SIZE * 2, PANEL_WIDTH);

		addNewList.y = routeList.y = SQUARE_SIZE * 2;
		addNewList.height = routeList.height = height - SQUARE_SIZE * 5;
		addNewList.width = routeList.width = PANEL_WIDTH;

		for (WidgetShorterSlider slider : sliders) {
			addDrawableChild(slider);
		}
		for (int i = 0; i < Depot.HOURS_IN_DAY; i++) {
			sliders[i].setValue(data.getFrequency(i));
		}

		addNewList.init();
		routeList.init();
		setIsSelecting(false);

		addDrawableChild(buttonEditInstructions);
		addDrawableChild(buttonGenerateRoute);
		addDrawableChild(buttonClearTrains);
		addDrawableChild(buttonDone);
	}

	@Override
	public void tick() {
		super.tick();
		addNewList.tick();
		routeList.tick();

		addNewList.setData(ClientData.ROUTES, false, false, false, false, true, false);
		routeList.setData(data.routeIds.stream().map(ClientData.DATA_CACHE.routeIdMap::get).filter(Objects::nonNull).collect(Collectors.toList()), false, false, false, true, false, true);

		buttonGenerateRoute.active = data.clientPathGenerationSuccessfulSegments >= 0;
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			if (addingRoute) {
				renderBackground(matrices);
				addNewList.render(matrices, textRenderer, mouseX, mouseY, delta);
				routeList.render(matrices, textRenderer, mouseX, mouseY, delta);
				super.render(matrices, mouseX, mouseY, delta);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.edit_instructions"), width / 2, SQUARE_SIZE + TEXT_PADDING, ARGB_LIGHT_GRAY);
			} else {
				renderBackground(matrices);
				drawVerticalLine(matrices, rightPanelsX - 1, -1, height, ARGB_WHITE_TRANSLUCENT);
				renderTextFields(matrices, mouseX, mouseY, delta);

				final int lineHeight = Math.min(SQUARE_SIZE, (height - SQUARE_SIZE) / Depot.HOURS_IN_DAY);
				for (int i = 0; i < Depot.HOURS_IN_DAY; i++) {
					drawStringWithShadow(matrices, textRenderer, getTimeString(i), TEXT_PADDING, SQUARE_SIZE + lineHeight * i + (int) ((lineHeight - TEXT_HEIGHT) / 2F), ARGB_WHITE);
					sliders[i].y = SQUARE_SIZE + lineHeight * i;
					sliders[i].setHeight(lineHeight);
				}
				super.render(matrices, mouseX, mouseY, delta);

				textRenderer.draw(matrices, new TranslatableText("gui.mtr.sidings_in_depot", sidingsInDepot.size()), rightPanelsX + TEXT_PADDING, PANELS_START + SQUARE_SIZE * 2 + TEXT_PADDING, ARGB_WHITE);

				final String[] stringSplit = getSuccessfulSegmentsText().getString().split("\\|");
				for (int i = 0; i < stringSplit.length; i++) {
					textRenderer.draw(matrices, stringSplit[i], rightPanelsX + TEXT_PADDING, PANELS_START + SQUARE_SIZE * 3 + TEXT_PADDING + (TEXT_HEIGHT + TEXT_PADDING) * i, ARGB_WHITE);
				}

				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.game_time"), sliderX / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
				drawCenteredText(matrices, textRenderer, new TranslatableText("gui.mtr.trains_per_hour"), sliderX + sliderWidthWithText / 2, TEXT_PADDING, ARGB_LIGHT_GRAY);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		addNewList.mouseMoved(mouseX, mouseY);
		routeList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		addNewList.mouseScrolled(mouseX, mouseY, amount);
		routeList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void onClose() {
		super.onClose();
		for (int i = 0; i < Depot.HOURS_IN_DAY; i++) {
			data.setFrequency(sliders[i].getIntValue(), i);
		}
		data.setFrequencies(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));
	}

	private void setIsSelecting(boolean isSelecting) {
		addingRoute = isSelecting;

		for (final WidgetShorterSlider slider : sliders) {
			slider.visible = !addingRoute;
		}
		addNewList.x = addingRoute ? width / 2 - PANEL_WIDTH - SQUARE_SIZE : width;
		routeList.x = addingRoute ? width / 2 + SQUARE_SIZE : width;
		buttonEditInstructions.visible = !addingRoute;
		buttonGenerateRoute.visible = !addingRoute;
		buttonClearTrains.visible = !addingRoute;
		buttonDone.visible = addingRoute;
		textFieldName.visible = !addingRoute;
		textFieldColor.visible = !addingRoute;
	}

	private void onAdded(NameColorDataBase listData, int index) {
		data.routeIds.add(listData.id);
		data.setRouteIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));
	}

	private void onSort() {
		data.setRouteIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));
	}

	private void onRemove(NameColorDataBase listData, int index) {
		data.routeIds.remove(index);
		data.setRouteIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_DEPOT, packet));
	}

	private Text getSuccessfulSegmentsText() {
		final int successfulSegments = data.clientPathGenerationSuccessfulSegments;

		if (successfulSegments < 0) {
			return new TranslatableText("gui.mtr.generating_path");
		} else if (successfulSegments == 0) {
			return new TranslatableText("gui.mtr.path_not_generated");
		} else {
			final List<String> stationNames = new ArrayList<>();
			final List<String> routeNames = new ArrayList<>();
			final String depotName = IGui.textOrUntitled(IGui.formatStationName(data.name));

			if (successfulSegments == 1) {
				RailwayData.useRoutesAndStationsFromIndex(0, data.routeIds, ClientData.DATA_CACHE, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
					stationNames.add(IGui.textOrUntitled(IGui.formatStationName(thisStation.name)));
					routeNames.add(IGui.textOrUntitled(IGui.formatStationName(thisRoute.name)));
				});
				stationNames.add("-");
				routeNames.add("-");

				return new TranslatableText("gui.mtr.path_not_found_between", routeNames.get(0), depotName, stationNames.get(0));
			} else {
				int sum = 0;
				for (int i = 0; i < data.routeIds.size(); i++) {
					final Route thisRoute = ClientData.DATA_CACHE.routeIdMap.get(data.routeIds.get(i));
					final Route nextRoute = i < data.routeIds.size() - 1 ? ClientData.DATA_CACHE.routeIdMap.get(data.routeIds.get(i + 1)) : null;
					if (thisRoute != null) {
						sum += thisRoute.platformIds.size();
						if (!thisRoute.platformIds.isEmpty() && nextRoute != null && !nextRoute.platformIds.isEmpty() && thisRoute.platformIds.get(thisRoute.platformIds.size() - 1).equals(nextRoute.platformIds.get(0))) {
							sum--;
						}
					}
				}

				if (successfulSegments >= sum + 2) {
					return new TranslatableText("gui.mtr.path_found");
				} else {
					RailwayData.useRoutesAndStationsFromIndex(successfulSegments - 2, data.routeIds, ClientData.DATA_CACHE, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
						stationNames.add(IGui.textOrUntitled(IGui.formatStationName(thisStation.name)));
						stationNames.add(IGui.textOrUntitled(IGui.formatStationName(nextStation == null ? "" : nextStation.name)));
						routeNames.add(IGui.textOrUntitled(IGui.formatStationName(thisRoute.name)));
					});
					stationNames.add("-");
					stationNames.add("-");
					routeNames.add("-");

					if (successfulSegments < sum + 1) {
						return new TranslatableText("gui.mtr.path_not_found_between", routeNames.get(0), stationNames.get(0), stationNames.get(1));
					} else {
						return new TranslatableText("gui.mtr.path_not_found_between", routeNames.get(0), stationNames.get(0), depotName);
					}
				}
			}
		}
	}

	private static String getSliderString(int value) {
		final String headwayText;
		if (value == 0) {
			headwayText = "";
		} else {
			headwayText = " (" + (Math.round(Depot.TRAIN_FREQUENCY_MULTIPLIER * 10F * SECONDS_PER_MC_HOUR / value) / 10F) + new TranslatableText("gui.mtr.s").getString() + ")";
		}
		return value / (float) Depot.TRAIN_FREQUENCY_MULTIPLIER + new TranslatableText("gui.mtr.tph").getString() + headwayText;
	}

	private static String getTimeString(int hour) {
		final String hourString = StringUtils.leftPad(String.valueOf(hour), 2, "0");
		return String.format("%s:00-%s:59", hourString, hourString);
	}
}
