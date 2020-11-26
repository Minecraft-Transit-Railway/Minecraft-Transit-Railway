package mtr.gui;

import mtr.data.*;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class DashboardScreen extends Screen implements IGui {

	private int selectedTab;
	private Station editingStation;
	private Route editingRoute;
	private boolean isNew;

	private final WidgetMap widgetMap;

	private final ButtonWidget buttonTabStations;
	private final ButtonWidget buttonTabRoutes;
	private final ButtonWidget buttonTabTrains;
	private final ButtonWidget buttonAddStation;
	private final ButtonWidget buttonAddRoute;
	private final ButtonWidget buttonDoneEditingStation;
	private final ButtonWidget buttonDoneEditingRoute;
	private final ButtonWidget buttonZoomIn;
	private final ButtonWidget buttonZoomOut;

	private final TextFieldWidget textFieldName;
	private final TextFieldWidget textFieldColor;

	private final DashboardList dashboardList;

	private static final int COLOR_WIDTH = 48;
	private static final int MAX_STATION_LENGTH = 128;
	private static final int MAX_COLOR_LENGTH = 6;

	public DashboardScreen() {
		super(new LiteralText(""));

		widgetMap = new WidgetMap(this::onDrawCorners, this::onClickPlatform);

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldName = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldColor = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		buttonTabStations = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.stations"), button -> onSelectTab(0));
		buttonTabRoutes = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.routes"), button -> onSelectTab(1));
		buttonTabTrains = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.trains"), button -> onSelectTab(2));
		buttonAddStation = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_station"), button -> startEditingStation(new Station(), true));
		buttonAddRoute = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_route"), button -> startEditingRoute(new Route(), true));
		buttonDoneEditingStation = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneEditingStation());
		buttonDoneEditingRoute = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneEditingRoute());
		buttonZoomIn = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText("+"), button -> widgetMap.scale(1));
		buttonZoomOut = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText("-"), button -> widgetMap.scale(-1));

		dashboardList = new DashboardList(this::addButton, this::onFind, this::onEdit, null, this::onDelete, this::getList, DashboardScreen::sendUpdate);

		onSelectTab(0);
	}

	@Override
	protected void init() {
		final int tabCount = 3;
		final int bottomRowY = height - SQUARE_SIZE;

		widgetMap.setPositionAndSize(PANEL_WIDTH, 0, width - PANEL_WIDTH, height);

		IGui.setPositionAndWidth(buttonTabStations, 0, 0, PANEL_WIDTH / tabCount);
		IGui.setPositionAndWidth(buttonTabRoutes, PANEL_WIDTH / tabCount, 0, PANEL_WIDTH / tabCount);
		IGui.setPositionAndWidth(buttonTabTrains, 2 * PANEL_WIDTH / tabCount, 0, PANEL_WIDTH / tabCount);
		IGui.setPositionAndWidth(buttonAddStation, 0, bottomRowY, PANEL_WIDTH);
		IGui.setPositionAndWidth(buttonAddRoute, 0, bottomRowY, PANEL_WIDTH);
		IGui.setPositionAndWidth(buttonDoneEditingStation, 0, bottomRowY, PANEL_WIDTH);
		IGui.setPositionAndWidth(buttonDoneEditingRoute, 0, bottomRowY, PANEL_WIDTH);
		IGui.setPositionAndWidth(buttonZoomIn, width - SQUARE_SIZE, bottomRowY - SQUARE_SIZE, SQUARE_SIZE);
		IGui.setPositionAndWidth(buttonZoomOut, width - SQUARE_SIZE, bottomRowY, SQUARE_SIZE);

		IGui.setPositionAndWidth(textFieldName, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - COLOR_WIDTH - TEXT_FIELD_PADDING);
		IGui.setPositionAndWidth(textFieldColor, PANEL_WIDTH - COLOR_WIDTH + TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, COLOR_WIDTH - TEXT_FIELD_PADDING);

		dashboardList.x = 0;
		dashboardList.y = SQUARE_SIZE;
		dashboardList.width = PANEL_WIDTH;
		dashboardList.height = height - SQUARE_SIZE * 2;

		buttonDoneEditingRoute.visible = false;
		buttonDoneEditingStation.visible = false;

		textFieldName.setVisible(false);
		textFieldName.setMaxLength(MAX_STATION_LENGTH);
		textFieldName.setChangedListener(text -> textFieldName.setSuggestion(text.isEmpty() ? new TranslatableText("gui.mtr.name").getString() : ""));
		textFieldColor.setVisible(false);
		textFieldColor.setMaxLength(MAX_COLOR_LENGTH);
		textFieldColor.setChangedListener(text -> {
			final String newText = text.toUpperCase().replaceAll("[^0-9A-F]", "");
			if (!newText.equals(text)) {
				textFieldColor.setText(newText);
			}
			textFieldColor.setSuggestion(newText.isEmpty() ? new TranslatableText("gui.mtr.color").getString() : "");
		});

		dashboardList.init();

		addChild(widgetMap);

		addButton(buttonTabStations);
		addButton(buttonTabRoutes);
		addButton(buttonTabTrains);
		addButton(buttonAddStation);
		addButton(buttonAddRoute);
		addButton(buttonDoneEditingStation);
		addButton(buttonDoneEditingRoute);
		addButton(buttonZoomIn);
		addButton(buttonZoomOut);

		addChild(textFieldName);
		addChild(textFieldColor);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		widgetMap.render(matrices, mouseX, mouseY, delta);
		DrawableHelper.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
		dashboardList.render(matrices, textRenderer);
		super.render(matrices, mouseX, mouseY, delta);
		textFieldName.render(matrices, mouseX, mouseY, delta);
		textFieldColor.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		dashboardList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		dashboardList.mouseScrolled(amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void tick() {
		textFieldName.tick();
		textFieldColor.tick();

		switch (selectedTab) {
			default:
				dashboardList.setData(ClientData.stations, true, true, false, false, true);
				break;
			case 1:
				if (editingRoute == null) {
					dashboardList.setData(ClientData.routes, false, true, false, false, true);
				} else {
					final List<DataConverter> routeData = editingRoute.platformIds.stream().map(platformId -> RailwayData.getDataById(ClientData.platforms, platformId)).filter(Objects::nonNull).map(platform -> {
						final Station station = RailwayData.getStationByPlatform(ClientData.stations, platform);
						if (station != null) {
							return new DataConverter(String.format("%s (%s)", station.name, platform.name), station.color);
						} else {
							return new DataConverter(String.format("(%s)", platform.name), 0);
						}
					}).collect(Collectors.toList());
					dashboardList.setData(routeData, false, false, true, false, true);
				}
				break;
			case 2:
				dashboardList.setData(ClientData.trains, true, false, false, false, true);
				break;
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onSelectTab(int tab) {
		selectedTab = tab;
		buttonTabStations.active = tab != 0;
		buttonTabRoutes.active = tab != 1;
		buttonTabTrains.active = tab != 2;

		stopEditing();
	}

	private void onFind(DataBase data, int index) {
		switch (selectedTab) {
			case 0:
				final Station station = (Station) data;
				widgetMap.find(station.corner1.getLeft(), station.corner1.getRight(), station.corner2.getLeft(), station.corner2.getRight());
				break;
			case 2:
				final Train train = (Train) data;
				widgetMap.find(train.posX[0], train.posZ[0], train.posX[train.posX.length - 1], train.posZ[train.posZ.length - 1]);
				break;
		}
	}

	private void onEdit(DataBase data, int index) {
		switch (selectedTab) {
			case 0:
				startEditingStation((Station) data, false);
				break;
			case 1:
				startEditingRoute((Route) data, false);
				break;
		}
	}

	private void onDelete(DataBase data, int index) {
		switch (selectedTab) {
			case 0:
				final Station station = (Station) data;
				ClientData.stations.remove(station);
				sendUpdate();
				break;
			case 1:
				if (editingRoute == null) {
					final Route route = (Route) data;
					ClientData.routes.remove(route);
					sendUpdate();
				} else {
					editingRoute.platformIds.remove(index);
				}
				break;
		}
	}

	private List<Long> getList() {
		return editingRoute == null ? new ArrayList<>() : editingRoute.platformIds;
	}

	private void startEditingStation(Station editingStation, boolean isNew) {
		this.editingStation = editingStation;
		editingRoute = null;
		this.isNew = isNew;

		textFieldName.setText(editingStation.name);
		textFieldColor.setText(colorIntToString(editingStation.color));

		widgetMap.startEditingStation(editingStation);
		toggleButtons();
	}

	private void startEditingRoute(Route editingRoute, boolean isNew) {
		editingStation = null;
		this.editingRoute = editingRoute;
		this.isNew = isNew;

		textFieldName.setText(editingRoute.name);
		textFieldColor.setText(colorIntToString(editingRoute.color));

		widgetMap.startEditingRoute();
		toggleButtons();
	}

	private void onDrawCorners(Pair<Integer, Integer> corner1, Pair<Integer, Integer> corner2) {
		editingStation.corner1 = corner1;
		editingStation.corner2 = corner2;
		toggleButtons();
	}

	private void onClickPlatform(long platformId) {
		editingRoute.platformIds.add(platformId);
	}

	private void onDoneEditingStation() {
		if (isNew) {
			ClientData.stations.add(editingStation);
		}
		editingStation.name = IGui.textOrUntitled(textFieldName.getText());
		editingStation.color = colorStringToInt(textFieldColor.getText());
		stopEditing();
	}

	private void onDoneEditingRoute() {
		if (isNew) {
			ClientData.routes.add(editingRoute);
		}
		editingRoute.name = IGui.textOrUntitled(textFieldName.getText());
		editingRoute.color = colorStringToInt(textFieldColor.getText());
		stopEditing();
	}

	private void stopEditing() {
		editingStation = null;
		editingRoute = null;
		widgetMap.stopEditing();
		toggleButtons();
		sendUpdate();
	}

	private void toggleButtons() {
		buttonAddStation.visible = selectedTab == 0 && editingStation == null;
		buttonAddRoute.visible = selectedTab == 1 && editingRoute == null;
		buttonDoneEditingStation.visible = selectedTab == 0 && editingStation != null;
		buttonDoneEditingStation.active = nonNullCorners(editingStation);
		buttonDoneEditingRoute.visible = selectedTab == 1 && editingRoute != null;

		final boolean showTextFields = (selectedTab == 0 && editingStation != null) || (selectedTab == 1 && editingRoute != null);
		textFieldName.visible = showTextFields;
		textFieldColor.visible = showTextFields;
		dashboardList.height = height - SQUARE_SIZE * 2 - (showTextFields ? SQUARE_SIZE + TEXT_FIELD_PADDING : 0);
	}

	private static void sendUpdate() {
		PacketTrainDataGuiClient.sendStationsAndRoutesC2S(ClientData.stations, ClientData.routes);
	}

	private static int colorStringToInt(String string) {
		try {
			return Integer.parseInt(string, 16);
		} catch (Exception ignored) {
			return 0;
		}
	}

	private static String colorIntToString(int color) {
		return StringUtils.leftPad(Integer.toHexString(color == 0 ? (new Random()).nextInt(RGB_WHITE + 1) : color).toUpperCase(), 6, "0");
	}

	private static boolean nonNullCorners(Station station) {
		return station != null && station.corner1 != null && station.corner2 != null;
	}
}
