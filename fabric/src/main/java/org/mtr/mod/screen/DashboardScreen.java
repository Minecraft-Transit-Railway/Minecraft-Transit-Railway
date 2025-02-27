package org.mtr.mod.screen;

import org.mtr.core.data.Position;
import org.mtr.core.data.*;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.Init;
import org.mtr.mod.InitClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketDeleteData;
import org.mtr.mod.packet.PacketUpdateData;

import java.util.Objects;
import java.util.stream.Collectors;

public class DashboardScreen extends ScreenExtension implements IGui {

	private SelectedTab selectedTab;
	private AreaBase<?, ?> editingArea;
	private Route editingRoute;
	private int editingRoutePlatformIndex;
	private boolean isNew;

	private final TransportMode transportMode;
	private final WidgetMap widgetMap;

	private final ButtonWidgetExtension buttonTabStations;
	private final ButtonWidgetExtension buttonTabRoutes;
	private final ButtonWidgetExtension buttonTabDepots;
	private final ButtonWidgetExtension buttonAddStation;
	private final ButtonWidgetExtension buttonAddRoute;
	private final ButtonWidgetExtension buttonAddDepot;
	private final ButtonWidgetExtension buttonDoneEditingStation;
	private final ButtonWidgetExtension buttonDoneEditingRoute;
	private final ButtonWidgetExtension buttonDoneEditingRouteDestination;
	private final ButtonWidgetExtension buttonZoomIn;
	private final ButtonWidgetExtension buttonZoomOut;
	private final TexturedButtonWidgetExtension buttonMapTopView;
	private final TexturedButtonWidgetExtension buttonMapCurrentY;
	private final ButtonWidgetExtension buttonRailActions;
	private final ButtonWidgetExtension buttonOptions;
	private final ButtonWidgetExtension buttonTransportSystemMap;
	private final ButtonWidgetExtension buttonResourcePackCreator;

	private final TextFieldWidgetExtension textFieldName;
	private final TextFieldWidgetExtension textFieldCustomDestination;
	private final WidgetColorSelector colorSelector;

	private final DashboardList dashboardList;

	public static final int MAX_COLOR_ZONE_LENGTH = 6;
	private static final int COLOR_WIDTH = 48;

	public DashboardScreen(TransportMode transportMode) {
		super();
		this.transportMode = transportMode;

		textFieldName = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_NAME.getString());
		textFieldCustomDestination = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_CUSTOM_DESTINATION_SUGGESTION.getString());
		colorSelector = new WidgetColorSelector(this, true, this::toggleButtons);
		widgetMap = new WidgetMap(transportMode, this::onDrawCorners, this::onDrawCornersMouseRelease, this::onClickAddPlatformToRoute, this::onClickEditSavedRail, colorSelector::isMouseOver2);

		buttonTabStations = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_STATIONS.getMutableText(), button -> onSelectTab(SelectedTab.STATIONS));
		buttonTabRoutes = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_ROUTES.getMutableText(), button -> onSelectTab(SelectedTab.ROUTES));
		buttonTabDepots = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_DEPOTS.getMutableText(), button -> onSelectTab(SelectedTab.DEPOTS));

		buttonAddStation = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_ADD_STATION.getMutableText(), button -> startEditingArea(new Station(MinecraftClientData.getDashboardInstance()), true));
		buttonAddRoute = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_ADD_ROUTE.getMutableText(), button -> startEditingRoute(new Route(transportMode, MinecraftClientData.getDashboardInstance()), true));
		buttonAddDepot = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_ADD_DEPOT.getMutableText(), button -> startEditingArea(new Depot(transportMode, MinecraftClientData.getDashboardInstance()), true));
		buttonDoneEditingStation = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.done"), button -> onDoneEditingArea());
		buttonDoneEditingRoute = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.done"), button -> onDoneEditingRoute());
		buttonDoneEditingRouteDestination = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.done"), button -> onDoneEditingRouteDestination());
		buttonZoomIn = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+"), button -> widgetMap.scale(1));
		buttonZoomOut = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-"), button -> widgetMap.scale(-1));
		buttonMapTopView = TexturedButtonWidgetHelper.create(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_map_top_view.png"), new Identifier("textures/gui/sprites/mtr/icon_map_top_view_highlighted.png"), new Identifier("textures/gui/sprites/mtr/icon_map_top_view_disabled.png"), button -> {
			widgetMap.setMapOverlayMode(WorldMap.MapOverlayMode.TOP_VIEW);
			toggleButtons();
		});
		buttonMapCurrentY = TexturedButtonWidgetHelper.create(0, 0, 0, SQUARE_SIZE, new Identifier("textures/gui/sprites/mtr/icon_map_current_y.png"), new Identifier("textures/gui/sprites/mtr/icon_map_current_y_highlighted.png"), new Identifier("textures/gui/sprites/mtr/icon_map_current_y_disabled.png"), button -> {
			widgetMap.setMapOverlayMode(WorldMap.MapOverlayMode.CURRENT_Y);
			toggleButtons();
		});
		buttonRailActions = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_RAIL_ACTIONS_BUTTON.getMutableText(), button -> MinecraftClient.getInstance().openScreen(new Screen(new RailActionsScreen(this))));
		buttonOptions = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("menu.options"), button -> MinecraftClient.getInstance().openScreen(new Screen(new ConfigScreen(new Screen(this)))));
		buttonTransportSystemMap = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_TRANSPORT_SYSTEM_MAP.getMutableText(), button -> Util.getOperatingSystem().open(String.format("http://localhost:%s", InitClient.getServerPort())));
		buttonResourcePackCreator = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TranslationProvider.GUI_MTR_RESOURCE_PACK_CREATOR.getMutableText(), button -> Util.getOperatingSystem().open(String.format("http://localhost:%s/creator/", InitClient.getServerPort())));

		dashboardList = new DashboardList(this::onFind, this::onDrawArea, this::onEdit, this::onSort, null, this::onDelete, this::getList, () -> MinecraftClientData.DASHBOARD_SEARCH, text -> MinecraftClientData.DASHBOARD_SEARCH = text);

		onSelectTab(SelectedTab.STATIONS);
	}

	@Override
	protected void init2() {
		super.init2();

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
		IDrawing.setPositionAndWidth(buttonZoomIn, width - SQUARE_SIZE * 2, bottomRowY - SQUARE_SIZE, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonZoomOut, width - SQUARE_SIZE, bottomRowY - SQUARE_SIZE, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonMapCurrentY, width - SQUARE_SIZE * 2, bottomRowY, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonMapTopView, width - SQUARE_SIZE, bottomRowY, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonRailActions, width - SQUARE_SIZE * 7, bottomRowY - SQUARE_SIZE, SQUARE_SIZE * 5);
		IDrawing.setPositionAndWidth(buttonOptions, width - SQUARE_SIZE * 7, bottomRowY, SQUARE_SIZE * 5);
		IDrawing.setPositionAndWidth(buttonTransportSystemMap, PANEL_WIDTH, bottomRowY - SQUARE_SIZE, width - SQUARE_SIZE * 7 - PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonResourcePackCreator, PANEL_WIDTH, bottomRowY, width - SQUARE_SIZE * 7 - PANEL_WIDTH);

		IDrawing.setPositionAndWidth(textFieldName, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - COLOR_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldCustomDestination, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(colorSelector, PANEL_WIDTH - COLOR_WIDTH + TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, COLOR_WIDTH - TEXT_FIELD_PADDING);

		dashboardList.x = 0;
		dashboardList.y = SQUARE_SIZE;
		dashboardList.width = PANEL_WIDTH;

		toggleButtons();
		dashboardList.init(this::addChild);


		addChild(new ClickableWidget(buttonTabStations));
		addChild(new ClickableWidget(buttonTabRoutes));
		addChild(new ClickableWidget(buttonTabDepots));
		addChild(new ClickableWidget(buttonAddStation));
		addChild(new ClickableWidget(buttonAddRoute));
		addChild(new ClickableWidget(buttonAddDepot));
		addChild(new ClickableWidget(buttonDoneEditingStation));
		addChild(new ClickableWidget(buttonDoneEditingRoute));
		addChild(new ClickableWidget(buttonDoneEditingRouteDestination));
		addChild(new ClickableWidget(buttonZoomIn));
		addChild(new ClickableWidget(buttonZoomOut));
		addChild(new ClickableWidget(buttonRailActions));
		addChild(new ClickableWidget(buttonOptions));
		addChild(new ClickableWidget(buttonTransportSystemMap));
		addChild(new ClickableWidget(buttonResourcePackCreator));
		addChild(new ClickableWidget(buttonMapTopView));
		addChild(new ClickableWidget(buttonMapCurrentY));

		addChild(new ClickableWidget(textFieldName));
		addChild(new ClickableWidget(textFieldCustomDestination));
		addChild(new ClickableWidget(colorSelector));

		addSelectableChild(new ClickableWidget(widgetMap));
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		widgetMap.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.push();
		graphicsHolder.translate(0, 0, 500);
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
		guiDrawing.finishDrawingRectangle();
		dashboardList.render(graphicsHolder);
		super.render(graphicsHolder, mouseX, mouseY, delta);
		graphicsHolder.pop();
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		dashboardList.mouseMoved(mouseX, mouseY);
		super.mouseMoved2(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
		dashboardList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled2(mouseX, mouseY, amount);
	}

	@Override
	public void tick2() {
		textFieldName.tick2();
		textFieldCustomDestination.tick2();
		dashboardList.tick();

		try {
			switch (selectedTab) {
				case STATIONS:
					if (editingArea == null) {
						dashboardList.setData(MinecraftClientData.convertDataSet(MinecraftClientData.getDashboardInstance().stations), true, true, true, false, false, true);
					} else {
						dashboardList.setData(MinecraftClientData.convertDataSet(editingArea.savedRails), true, false, true, false, false, false);
					}
					break;
				case ROUTES:
					if (editingRoute == null) {
						dashboardList.setData(MinecraftClientData.getFilteredDataSet(transportMode, MinecraftClientData.getDashboardInstance().routes), false, true, true, false, false, true);
					} else {
						final ObjectArrayList<DashboardListItem> routeData = editingRoute.getRoutePlatforms().stream().map(routePlatformData -> {
							final Platform platform = routePlatformData.platform;
							if (platform == null) {
								return null;
							} else {
								final String customDestinationPrefix = routePlatformData.getCustomDestination().isEmpty() ? "" : Route.destinationIsReset(routePlatformData.getCustomDestination()) ? "\"" : "*";
								final Station station = platform.area;
								if (station != null) {
									return new DashboardListItem(0, String.format("%s%s (%s)", customDestinationPrefix, station.getName(), platform.getName()), station.getColor());
								} else {
									return new DashboardListItem(0, String.format("%s(%s)", customDestinationPrefix, platform.getName()), 0);
								}
							}
						}).filter(Objects::nonNull).collect(Collectors.toCollection(ObjectArrayList::new));
						dashboardList.setData(routeData, false, false, true, true, false, true);
					}
					break;
				case DEPOTS:
					if (editingArea == null) {
						dashboardList.setData(MinecraftClientData.getFilteredDataSet(transportMode, MinecraftClientData.getDashboardInstance().depots), true, true, true, false, false, true);
					} else {
						dashboardList.setData(MinecraftClientData.convertDataSet(editingArea.savedRails), true, false, true, false, false, false);
					}
					break;
			}
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	@Override
	public boolean isPauseScreen2() {
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

	private void onFind(DashboardListItem dashboardListItem, int index) {
		if (selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) {
			if (editingArea == null && dashboardListItem.data instanceof AreaBase) {
				final AreaBase<?, ?> area = (AreaBase<?, ?>) dashboardListItem.data;
				if (AreaBase.validCorners(area)) {
					widgetMap.find(area.getCenter());
				}
			} else if (selectedTab == SelectedTab.STATIONS) {
				final Platform platform = (Platform) dashboardListItem.data;
				widgetMap.find(platform.getMidPosition());
			}
		}
	}

	private void onDrawArea(DashboardListItem dashboardListItem, int index) {
		switch (selectedTab) {
			case STATIONS:
			case DEPOTS:
				if (editingArea == null && dashboardListItem.data instanceof AreaBase) {
					startEditingArea((AreaBase<?, ?>) dashboardListItem.data, false);
				}
				break;
			case ROUTES:
				if (dashboardListItem.data instanceof Route) {
					startEditingRoute((Route) dashboardListItem.data, false);
				}
				break;
		}
		dashboardList.clearSearch();
	}

	private void onEdit(DashboardListItem dashboardListItem, int index) {
		switch (selectedTab) {
			case STATIONS:
				if (editingArea == null) {
					if (dashboardListItem.data instanceof Station) {
						MinecraftClient.getInstance().openScreen(new Screen(new EditStationScreen((Station) dashboardListItem.data, this)));
					}
				} else {
					if (dashboardListItem.data instanceof Platform) {
						MinecraftClient.getInstance().openScreen(new Screen(new PlatformScreen((Platform) dashboardListItem.data, transportMode, this)));
					}
				}
				break;
			case ROUTES:
				if (editingRoute == null && dashboardListItem.data instanceof Route) {
					MinecraftClient.getInstance().openScreen(new Screen(new EditRouteScreen((Route) dashboardListItem.data, this)));
				} else {
					startEditingRouteDestination(index);
				}
				break;
			case DEPOTS:
				if (editingArea == null) {
					if (dashboardListItem.data instanceof Depot) {
						MinecraftClient.getInstance().openScreen(new Screen(new EditDepotScreen((Depot) dashboardListItem.data, transportMode, this)));
					}
				} else {
					if (dashboardListItem.data instanceof Siding) {
						MinecraftClient.getInstance().openScreen(new Screen(new SidingScreen((Siding) dashboardListItem.data, transportMode, this)));
					}
				}
				break;
		}
	}

	private void onSort() {
		if (selectedTab == SelectedTab.ROUTES && editingRoute != null) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
		}
	}

	private void onDelete(DashboardListItem dashboardListItem, int index) {
		switch (selectedTab) {
			case STATIONS:
				if (dashboardListItem.data instanceof Station) {
					final Station station = (Station) dashboardListItem.data;
					MinecraftClient.getInstance().openScreen(new Screen(new DeleteConfirmationScreen(() -> InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDeleteData(new DeleteDataRequest().addStationId(station.getId()))), IGui.formatStationName(station.getName()), this)));
				}
				break;
			case ROUTES:
				if (editingRoute == null) {
					if (dashboardListItem.data instanceof Route) {
						final Route route = (Route) dashboardListItem.data;
						MinecraftClient.getInstance().openScreen(new Screen(new DeleteConfirmationScreen(() -> InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDeleteData(new DeleteDataRequest().addRouteId(route.getId()))), IGui.formatStationName(route.getName()), this)));
					}
				} else {
					editingRoute.getRoutePlatforms().remove(index);
					InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
				}
				break;
			case DEPOTS:
				if (dashboardListItem.data instanceof Depot) {
					final Depot depot = (Depot) dashboardListItem.data;
					MinecraftClient.getInstance().openScreen(new Screen(new DeleteConfirmationScreen(() -> InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketDeleteData(new DeleteDataRequest().addDepotId(depot.getId()))), IGui.formatStationName(depot.getName()), this)));
				}
				break;
		}
	}

	private ObjectArrayList<RoutePlatformData> getList() {
		return editingRoute == null ? new ObjectArrayList<>() : editingRoute.getRoutePlatforms();
	}

	private void startEditingArea(AreaBase<?, ?> editingArea, boolean isNew) {
		this.editingArea = editingArea;
		editingRoute = null;
		this.isNew = isNew;

		textFieldName.setText2(editingArea.getName());
		colorSelector.setColor(editingArea.getColor());

		widgetMap.startEditingArea(editingArea);
		toggleButtons();
	}

	private void startEditingRoute(Route editingRoute, boolean isNew) {
		editingArea = null;
		this.editingRoute = editingRoute;
		this.isNew = isNew;
		editingRoutePlatformIndex = -1;

		textFieldName.setText2(editingRoute.getName());
		colorSelector.setColor(editingRoute.getColor());

		widgetMap.startEditingRoute();
		toggleButtons();
	}

	private void startEditingRouteDestination(int index) {
		editingRoutePlatformIndex = index;
		if (isValidRoutePlatformIndex()) {
			textFieldCustomDestination.setText2(editingRoute.getRoutePlatforms().get(index).getCustomDestination());
		}
		toggleButtons();
	}

	private void onDrawCorners(IntIntImmutablePair corner1, IntIntImmutablePair corner2) {
		editingArea.setCorners(new Position(corner1.leftInt(), Long.MIN_VALUE, corner1.rightInt()), new Position(corner2.leftInt(), Long.MAX_VALUE, corner2.rightInt()));
		toggleButtons();
	}

	private void onDrawCornersMouseRelease() {
		if (editingArea instanceof Station) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation((Station) editingArea)));
		} else if (editingArea instanceof Depot) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot((Depot) editingArea)));
		}
	}

	private void onClickAddPlatformToRoute(long platformId) {
		final RoutePlatformData routePlatformData = new RoutePlatformData(platformId);
		editingRoute.getRoutePlatforms().add(routePlatformData);
		routePlatformData.writePlatformCache(editingRoute, MinecraftClientData.getDashboardInstance().platformIdMap);
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
	}

	private void onClickEditSavedRail(SavedRailBase<?, ?> savedRail) {
		if (savedRail instanceof Platform) {
			MinecraftClient.getInstance().openScreen(new Screen(new PlatformScreen((Platform) savedRail, transportMode, this)));
		} else if (savedRail instanceof Siding) {
			MinecraftClient.getInstance().openScreen(new Screen(new SidingScreen((Siding) savedRail, transportMode, this)));
		}
	}

	private void onDoneEditingArea() {
		editingArea.setName(IGui.textOrUntitled(textFieldName.getText2()));
		editingArea.setColor(colorSelector.getColor());
		if (editingArea instanceof Station) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation((Station) editingArea)));
		} else if (editingArea instanceof Depot) {
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot((Depot) editingArea)));
		}
		stopEditing();
	}

	private void onDoneEditingRoute() {
		editingRoute.setName(IGui.textOrUntitled(textFieldName.getText2()));
		editingRoute.setColor(colorSelector.getColor());
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
		stopEditing();
	}

	private void onDoneEditingRouteDestination() {
		if (isValidRoutePlatformIndex()) {
			editingRoute.getRoutePlatforms().get(editingRoutePlatformIndex).setCustomDestination(textFieldCustomDestination.getText2());
			InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
		}
		startEditingRoute(editingRoute, isNew);
	}

	@Override
	public void onClose2() {
		widgetMap.onClose();
		super.onClose2();
	}

	private void stopEditing() {
		editingArea = null;
		editingRoute = null;
		widgetMap.stopEditing();
		toggleButtons();
	}

	private boolean isValidRoutePlatformIndex() {
		return editingRoute != null && editingRoutePlatformIndex >= 0 && editingRoutePlatformIndex < editingRoute.getRoutePlatforms().size();
	}

	private void toggleButtons() {
		final boolean hasPermission = MinecraftClientData.hasPermission();
		final boolean showRouteDestinationFields = isValidRoutePlatformIndex();

		buttonAddStation.visible = selectedTab == SelectedTab.STATIONS && editingArea == null && hasPermission;
		buttonAddRoute.visible = selectedTab == SelectedTab.ROUTES && editingRoute == null && hasPermission;
		buttonAddDepot.visible = selectedTab == SelectedTab.DEPOTS && editingArea == null && hasPermission;
		buttonDoneEditingStation.visible = (selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null;
		buttonDoneEditingStation.active = AreaBase.validCorners(editingArea);
		buttonDoneEditingRoute.visible = selectedTab == SelectedTab.ROUTES && editingRoute != null && !showRouteDestinationFields;
		buttonDoneEditingRouteDestination.visible = selectedTab == SelectedTab.ROUTES && editingRoute != null && showRouteDestinationFields;

		buttonMapTopView.active = !widgetMap.isMapOverlayMode(WorldMap.MapOverlayMode.TOP_VIEW);
		buttonMapCurrentY.active = !widgetMap.isMapOverlayMode(WorldMap.MapOverlayMode.CURRENT_Y);

		final boolean showTextFields = ((selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null) || (selectedTab == SelectedTab.ROUTES && editingRoute != null && !showRouteDestinationFields);
		textFieldName.visible = showTextFields;
		textFieldCustomDestination.visible = showRouteDestinationFields;
		colorSelector.visible = showTextFields;
		dashboardList.height = height - SQUARE_SIZE * 2 - (showTextFields || showRouteDestinationFields ? SQUARE_SIZE + TEXT_FIELD_PADDING : 0);
	}

	private enum SelectedTab {STATIONS, ROUTES, DEPOTS}
}
