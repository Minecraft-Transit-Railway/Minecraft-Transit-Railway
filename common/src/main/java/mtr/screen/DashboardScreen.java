package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.ClientData;
import mtr.client.IDrawing;
import mtr.data.*;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.components.Button;
import net.minecraft.util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class DashboardScreen extends ScreenMapper implements IGui, IPacket {

	private SelectedTab selectedTab;
	private AreaBase editingArea;
	private Route editingRoute;
	private int editingRoutePlatformIndex;
	private boolean isNew;

	private final TransportMode transportMode;
	private final WidgetMap widgetMap;

	private final Button buttonTabStations;
	private final Button buttonTabRoutes;
	private final Button buttonTabDepots;
	private final Button buttonAddStation;
	private final Button buttonAddRoute;
	private final Button buttonAddDepot;
	private final Button buttonDoneEditingStation;
	private final Button buttonDoneEditingRoute;
	private final Button buttonDoneEditingRouteDestination;
	private final Button buttonZoomIn;
	private final Button buttonZoomOut;
	private final Button buttonRailActions;
	private final Button buttonOptions;

	private final WidgetBetterTextField textFieldName;
	private final WidgetBetterTextField textFieldCustomDestination;
	private final WidgetColorSelector colorSelector;

	private final DashboardList dashboardList;

	public static final int MAX_COLOR_ZONE_LENGTH = 6;
	private static final int COLOR_WIDTH = 48;

	public DashboardScreen(TransportMode transportMode, boolean useTimeAndWindSync) {
		super(Text.literal(""));
		this.transportMode = transportMode;

		textFieldName = new WidgetBetterTextField(Text.translatable("gui.mtr.name").getString());
		textFieldCustomDestination = new WidgetBetterTextField(Text.translatable("gui.mtr.custom_destination_suggestion").getString());
		colorSelector = new WidgetColorSelector(this, this::toggleButtons);
		widgetMap = new WidgetMap(transportMode, this::onDrawCorners, this::onDrawCornersMouseRelease, this::onClickAddPlatformToRoute, this::onClickEditSavedRail, colorSelector::isMouseOver);

		buttonTabStations = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.stations"), button -> onSelectTab(SelectedTab.STATIONS));
		buttonTabRoutes = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.routes"), button -> onSelectTab(SelectedTab.ROUTES));
		buttonTabDepots = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.depots"), button -> onSelectTab(SelectedTab.DEPOTS));

		buttonAddStation = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.add_station"), button -> startEditingArea(new Station(), true));
		buttonAddRoute = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.add_route"), button -> startEditingRoute(new Route(transportMode), true));
		buttonAddDepot = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.add_depot"), button -> startEditingArea(new Depot(transportMode), true));
		buttonDoneEditingStation = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.done"), button -> onDoneEditingArea());
		buttonDoneEditingRoute = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.done"), button -> onDoneEditingRoute());
		buttonDoneEditingRouteDestination = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.done"), button -> onDoneEditingRouteDestination());
		buttonZoomIn = new Button(0, 0, 0, SQUARE_SIZE, Text.literal("+"), button -> widgetMap.scale(1));
		buttonZoomOut = new Button(0, 0, 0, SQUARE_SIZE, Text.literal("-"), button -> widgetMap.scale(-1));
		buttonRailActions = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("gui.mtr.rail_actions_button"), button -> {
			if (minecraft != null) {
				UtilitiesClient.setScreen(minecraft, new RailActionsScreen());
			}
		});
		buttonOptions = new Button(0, 0, 0, SQUARE_SIZE, Text.translatable("menu.options"), button -> {
			if (minecraft != null) {
				UtilitiesClient.setScreen(minecraft, new ConfigScreen(useTimeAndWindSync));
			}
		});

		dashboardList = new DashboardList(this::onFind, this::onDrawArea, this::onEdit, this::onSort, null, this::onDelete, this::getList, () -> ClientData.DASHBOARD_SEARCH, text -> ClientData.DASHBOARD_SEARCH = text);

		onSelectTab(SelectedTab.STATIONS);
	}

	@Override
	protected void init() {
		super.init();

		final int tabCount = 3;
		final int bottomRowY = height - SQUARE_SIZE;

		widgetMap.setPositionAndSize(PANEL_WIDTH, 0, width - PANEL_WIDTH, height);

		IDrawing.setPositionAndWidth(buttonTabStations, 0, 0, PANEL_WIDTH / tabCount);
		IDrawing.setPositionAndWidth(buttonTabRoutes, PANEL_WIDTH / tabCount, 0, PANEL_WIDTH / tabCount);
		IDrawing.setPositionAndWidth(buttonTabDepots, 2 * PANEL_WIDTH / tabCount, 0, PANEL_WIDTH / tabCount);
		IDrawing.setPositionAndWidth(buttonAddStation, 0, bottomRowY, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonAddRoute, 0, bottomRowY, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonAddDepot, 0, bottomRowY, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonDoneEditingStation, 0, bottomRowY, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonDoneEditingRoute, 0, bottomRowY, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonDoneEditingRouteDestination, 0, bottomRowY, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonZoomIn, width - SQUARE_SIZE * 2, bottomRowY, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonZoomOut, width - SQUARE_SIZE, bottomRowY, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonRailActions, width - SQUARE_SIZE * 10, bottomRowY, SQUARE_SIZE * 5);
		IDrawing.setPositionAndWidth(buttonOptions, width - SQUARE_SIZE * 5, bottomRowY, SQUARE_SIZE * 3);

		IDrawing.setPositionAndWidth(textFieldName, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - COLOR_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldCustomDestination, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(colorSelector, PANEL_WIDTH - COLOR_WIDTH + TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, COLOR_WIDTH - TEXT_FIELD_PADDING);

		dashboardList.x = 0;
		dashboardList.y = SQUARE_SIZE;
		dashboardList.width = PANEL_WIDTH;
		dashboardList.height = height - SQUARE_SIZE * 2;

		buttonDoneEditingStation.visible = false;
		buttonDoneEditingRoute.visible = false;
		buttonDoneEditingRouteDestination.visible = false;

		textFieldName.setVisible(false);
		textFieldCustomDestination.setVisible(false);
		colorSelector.visible = false;

		dashboardList.init(this::addDrawableChild);

		addWidget(widgetMap);

		addDrawableChild(buttonTabStations);
		addDrawableChild(buttonTabRoutes);
		addDrawableChild(buttonTabDepots);
		addDrawableChild(buttonAddStation);
		addDrawableChild(buttonAddRoute);
		addDrawableChild(buttonAddDepot);
		addDrawableChild(buttonDoneEditingStation);
		addDrawableChild(buttonDoneEditingRoute);
		addDrawableChild(buttonDoneEditingRouteDestination);
		addDrawableChild(buttonZoomIn);
		addDrawableChild(buttonZoomOut);
		addDrawableChild(buttonRailActions);
		addDrawableChild(buttonOptions);

		addDrawableChild(textFieldName);
		addDrawableChild(textFieldCustomDestination);
		addDrawableChild(colorSelector);
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			widgetMap.render(matrices, mouseX, mouseY, delta);
			matrices.pushPose();
			matrices.translate(0, 0, 500);
			Gui.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			dashboardList.render(matrices, font);
			super.render(matrices, mouseX, mouseY, delta);
			matrices.popPose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		dashboardList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		dashboardList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled(mouseX, mouseY, amount);
	}

	@Override
	public void tick() {
		textFieldName.tick();
		textFieldCustomDestination.tick();
		dashboardList.tick();

		try {
			switch (selectedTab) {
				case STATIONS:
					if (editingArea == null) {
						dashboardList.setData(ClientData.STATIONS, true, true, true, false, false, true);
					} else {
						final Map<Long, Platform> platformData = ClientData.DATA_CACHE.requestStationIdToPlatforms(editingArea.id);
						dashboardList.setData(platformData == null ? new ArrayList<>() : new ArrayList<>(platformData.values()), true, false, true, false, false, false);
					}
					break;
				case ROUTES:
					if (editingRoute == null) {
						dashboardList.setData(ClientData.getFilteredDataSet(transportMode, ClientData.ROUTES), false, true, true, false, false, true);
					} else {
						final List<DataConverter> routeData = editingRoute.platformIds.stream().map(platformId -> {
							final Platform platform = ClientData.DATA_CACHE.platformIdMap.get(platformId.platformId);
							if (platform == null) {
								return null;
							} else {
								final String customDestinationPrefix = platformId.customDestination.isEmpty() ? "" : Route.destinationIsReset(platformId.customDestination) ? "\"" : "*";
								final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platform.id);
								if (station != null) {
									return new DataConverter(String.format("%s%s (%s)", customDestinationPrefix, station.name, platform.name), station.color);
								} else {
									return new DataConverter(String.format("%s(%s)", customDestinationPrefix, platform.name), 0);
								}
							}
						}).filter(Objects::nonNull).collect(Collectors.toList());
						dashboardList.setData(routeData, false, false, true, true, false, true);
					}
					break;
				case DEPOTS:
					if (editingArea == null) {
						dashboardList.setData(ClientData.getFilteredDataSet(transportMode, ClientData.DEPOTS), true, true, true, false, false, true);
					} else {
						final Map<Long, Siding> sidingData = ClientData.DATA_CACHE.requestDepotIdToSidings(editingArea.id);
						dashboardList.setData(sidingData == null ? new ArrayList<>() : new ArrayList<>(sidingData.values()), true, false, true, false, false, false);
					}
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}

	private void onSelectTab(SelectedTab tab) {
		selectedTab = tab;
		buttonTabStations.active = tab != SelectedTab.STATIONS;
		buttonTabRoutes.active = tab != SelectedTab.ROUTES;
		buttonTabDepots.active = tab != SelectedTab.DEPOTS;
		stopEditing();
		widgetMap.setShowStations(selectedTab != SelectedTab.DEPOTS);
	}

	private void onFind(NameColorDataBase data, int index) {
		if (selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) {
			if (editingArea == null && data instanceof AreaBase) {
				final AreaBase area = (AreaBase) data;
				if (AreaBase.nonNullCorners(area)) {
					widgetMap.find(area.corner1.getA(), area.corner1.getB(), area.corner2.getA(), area.corner2.getB());
				}
			} else if (selectedTab == SelectedTab.STATIONS) {
				final Platform platform = (Platform) data;
				widgetMap.find(platform.getMidPos());
			}
		}
	}

	private void onDrawArea(NameColorDataBase data, int index) {
		switch (selectedTab) {
			case STATIONS:
			case DEPOTS:
				if (editingArea == null && data instanceof AreaBase) {
					startEditingArea((AreaBase) data, false);
				}
				break;
			case ROUTES:
				if (data instanceof Route) {
					startEditingRoute((Route) data, false);
				}
				break;
		}
		dashboardList.clearSearch();
	}

	private void onEdit(NameColorDataBase data, int index) {
		if (minecraft != null) {
			switch (selectedTab) {
				case STATIONS:
					if (editingArea == null) {
						if (data instanceof Station) {
							UtilitiesClient.setScreen(minecraft, new EditStationScreen((Station) data, this));
						}
					} else {
						if (data instanceof Platform) {
							UtilitiesClient.setScreen(minecraft, new PlatformScreen((Platform) data, transportMode, this));
						}
					}
					break;
				case ROUTES:
					if (editingRoute == null && data instanceof Route) {
						UtilitiesClient.setScreen(minecraft, new EditRouteScreen((Route) data, this));
					} else {
						startEditingRouteDestination(index);
					}
					break;
				case DEPOTS:
					if (editingArea == null) {
						if (data instanceof Depot) {
							UtilitiesClient.setScreen(minecraft, new EditDepotScreen((Depot) data, transportMode, this));
						}
					} else {
						if (data instanceof Siding) {
							UtilitiesClient.setScreen(minecraft, new SidingScreen((Siding) data, transportMode, this));
						}
					}
					break;
			}
		}
	}

	private void onSort() {
		if (selectedTab == SelectedTab.ROUTES && editingRoute != null) {
			editingRoute.setPlatformIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
		}
	}

	private void onDelete(NameColorDataBase data, int index) {
		try {
			switch (selectedTab) {
				case STATIONS:
					if (minecraft != null) {
						final Station station = (Station) data;
						UtilitiesClient.setScreen(minecraft, new DeleteConfirmationScreen(() -> {
							PacketTrainDataGuiClient.sendDeleteData(PACKET_DELETE_STATION, station.id);
							ClientData.STATIONS.remove(station);
						}, IGui.formatStationName(station.name), this));
					}
					break;
				case ROUTES:
					if (editingRoute == null) {
						if (minecraft != null && data instanceof Route) {
							final Route route = (Route) data;
							UtilitiesClient.setScreen(minecraft, new DeleteConfirmationScreen(() -> {
								PacketTrainDataGuiClient.sendDeleteData(PACKET_DELETE_ROUTE, route.id);
								ClientData.ROUTES.remove(route);
							}, IGui.formatStationName(route.name), this));
						}
					} else {
						editingRoute.platformIds.remove(index);
						editingRoute.setPlatformIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
					}
					break;
				case DEPOTS:
					if (minecraft != null && data instanceof Depot) {
						final Depot depot = (Depot) data;
						UtilitiesClient.setScreen(minecraft, new DeleteConfirmationScreen(() -> {
							PacketTrainDataGuiClient.sendDeleteData(PACKET_DELETE_DEPOT, depot.id);
							ClientData.DEPOTS.remove(depot);
						}, IGui.formatStationName(depot.name), this));
					}
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<Route.RoutePlatform> getList() {
		return editingRoute == null ? new ArrayList<>() : editingRoute.platformIds;
	}

	private void startEditingArea(AreaBase editingArea, boolean isNew) {
		this.editingArea = editingArea;
		editingRoute = null;
		this.isNew = isNew;

		textFieldName.setValue(editingArea.name);
		colorSelector.setColor(editingArea.color);

		widgetMap.startEditingArea(editingArea);
		toggleButtons();
	}

	private void startEditingRoute(Route editingRoute, boolean isNew) {
		editingArea = null;
		this.editingRoute = editingRoute;
		this.isNew = isNew;
		editingRoutePlatformIndex = -1;

		textFieldName.setValue(editingRoute.name);
		colorSelector.setColor(editingRoute.color);

		widgetMap.startEditingRoute();
		toggleButtons();
	}

	private void startEditingRouteDestination(int index) {
		editingRoutePlatformIndex = index;
		if (isValidRoutePlatformIndex()) {
			textFieldCustomDestination.setValue(editingRoute.platformIds.get(index).customDestination);
		}
		toggleButtons();
	}

	private void onDrawCorners(Tuple<Integer, Integer> corner1, Tuple<Integer, Integer> corner2) {
		editingArea.corner1 = corner1;
		editingArea.corner2 = corner2;
		toggleButtons();
	}

	private void onDrawCornersMouseRelease() {
		editingArea.setCorners(packet -> PacketTrainDataGuiClient.sendUpdate(editingArea instanceof Station ? PACKET_UPDATE_STATION : PACKET_UPDATE_DEPOT, packet));
	}

	private void onClickAddPlatformToRoute(long platformId) {
		editingRoute.platformIds.add(new Route.RoutePlatform(platformId));
		editingRoute.setPlatformIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
	}

	private void onClickEditSavedRail(SavedRailBase savedRail) {
		if (savedRail instanceof Platform) {
			UtilitiesClient.setScreen(Minecraft.getInstance(), new PlatformScreen((Platform) savedRail, transportMode, this));
		} else if (savedRail instanceof Siding) {
			UtilitiesClient.setScreen(Minecraft.getInstance(), new SidingScreen((Siding) savedRail, transportMode, this));
		}
	}

	private void onDoneEditingArea() {
		if (editingArea instanceof Station || editingArea instanceof Depot) {
			final boolean isStation = editingArea instanceof Station;
			if (isNew) {
				if (isStation) {
					ClientData.STATIONS.add((Station) editingArea);
				} else {
					ClientData.DEPOTS.add((Depot) editingArea);
				}
			}
			editingArea.name = IGui.textOrUntitled(textFieldName.getValue());
			editingArea.color = colorSelector.getColor();
			editingArea.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(isStation ? PACKET_UPDATE_STATION : PACKET_UPDATE_DEPOT, packet));
		}
		stopEditing();
	}

	private void onDoneEditingRoute() {
		if (isNew) {
			try {
				ClientData.ROUTES.add(editingRoute);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		editingRoute.name = IGui.textOrUntitled(textFieldName.getValue());
		editingRoute.color = colorSelector.getColor();
		editingRoute.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
		stopEditing();
	}

	private void onDoneEditingRouteDestination() {
		if (isValidRoutePlatformIndex()) {
			editingRoute.platformIds.get(editingRoutePlatformIndex).customDestination = textFieldCustomDestination.getValue();
			editingRoute.setPlatformIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
		}
		startEditingRoute(editingRoute, isNew);
	}

	private void stopEditing() {
		editingArea = null;
		editingRoute = null;
		widgetMap.stopEditing();
		toggleButtons();
	}

	private boolean isValidRoutePlatformIndex() {
		return editingRoute != null && editingRoutePlatformIndex >= 0 && editingRoutePlatformIndex < editingRoute.platformIds.size();
	}

	private void toggleButtons() {
		final boolean hasPermission = ClientData.hasPermission();
		final boolean showRouteDestinationFields = isValidRoutePlatformIndex();

		buttonAddStation.visible = selectedTab == SelectedTab.STATIONS && editingArea == null && hasPermission;
		buttonAddRoute.visible = selectedTab == SelectedTab.ROUTES && editingRoute == null && hasPermission;
		buttonAddDepot.visible = selectedTab == SelectedTab.DEPOTS && editingArea == null && hasPermission;
		buttonDoneEditingStation.visible = (selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null;
		buttonDoneEditingStation.active = AreaBase.nonNullCorners(editingArea);
		buttonDoneEditingRoute.visible = selectedTab == SelectedTab.ROUTES && editingRoute != null && !showRouteDestinationFields;
		buttonDoneEditingRouteDestination.visible = selectedTab == SelectedTab.ROUTES && editingRoute != null && showRouteDestinationFields;

		final boolean showTextFields = ((selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null) || (selectedTab == SelectedTab.ROUTES && editingRoute != null && !showRouteDestinationFields);
		textFieldName.visible = showTextFields;
		textFieldCustomDestination.visible = showRouteDestinationFields;
		colorSelector.visible = showTextFields;
		dashboardList.height = height - SQUARE_SIZE * 2 - (showTextFields ? SQUARE_SIZE + TEXT_FIELD_PADDING : 0);
	}

	private enum SelectedTab {STATIONS, ROUTES, DEPOTS}
}
