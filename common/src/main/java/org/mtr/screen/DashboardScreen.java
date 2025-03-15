package org.mtr.screen;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.client.IDrawing;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketDeleteData;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;

import java.util.Objects;
import java.util.stream.Collectors;

public class DashboardScreen extends MTRScreenBase implements IGui {

	private SelectedTab selectedTab;
	private AreaBase<?, ?> editingArea;
	private Route editingRoute;
	private int editingRoutePlatformIndex;
	private boolean isNew;

	private final TransportMode transportMode;
	private final WidgetMap widgetMap;

	private final ButtonWidget buttonTabStations;
	private final ButtonWidget buttonTabRoutes;
	private final ButtonWidget buttonTabDepots;
	private final ButtonWidget buttonAddStation;
	private final ButtonWidget buttonAddRoute;
	private final ButtonWidget buttonAddDepot;
	private final ButtonWidget buttonDoneEditingStation;
	private final ButtonWidget buttonDoneEditingRoute;
	private final ButtonWidget buttonDoneEditingRouteDestination;
	private final ButtonWidget buttonZoomIn;
	private final ButtonWidget buttonZoomOut;
	private final WidgetBetterTexturedButton buttonMapTopView;
	private final WidgetBetterTexturedButton buttonMapCurrentY;
	private final ButtonWidget buttonRailActions;
	private final ButtonWidget buttonOptions;
	private final ButtonWidget buttonTransportSystemMap;
	private final ButtonWidget buttonResourcePackCreator;

	private final WidgetBetterTextField textFieldName;
	private final WidgetBetterTextField textFieldCustomDestination;
	private final WidgetColorSelector colorSelector;

	private final DashboardList dashboardList;

	public static final int MAX_COLOR_ZONE_LENGTH = 6;
	private static final int COLOR_WIDTH = 48;

	public DashboardScreen(TransportMode transportMode) {
		super();
		this.transportMode = transportMode;

		textFieldName = new WidgetBetterTextField(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_NAME.getString());
		textFieldCustomDestination = new WidgetBetterTextField(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_CUSTOM_DESTINATION_SUGGESTION.getString());
		colorSelector = new WidgetColorSelector(this, true, this::toggleButtons);
		widgetMap = new WidgetMap(transportMode, this::onDrawCorners, this::onDrawCornersMouseRelease, this::onClickAddPlatformToRoute, this::onClickEditSavedRail, colorSelector::isMouseOver);

		buttonTabStations = ButtonWidget.builder(TranslationProvider.GUI_MTR_STATIONS.getMutableText(), button -> onSelectTab(SelectedTab.STATIONS)).build();
		buttonTabRoutes = ButtonWidget.builder(TranslationProvider.GUI_MTR_ROUTES.getMutableText(), button -> onSelectTab(SelectedTab.ROUTES)).build();
		buttonTabDepots = ButtonWidget.builder(TranslationProvider.GUI_MTR_DEPOTS.getMutableText(), button -> onSelectTab(SelectedTab.DEPOTS)).build();

		buttonAddStation = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_STATION.getMutableText(), button -> startEditingArea(new Station(MinecraftClientData.getDashboardInstance()), true)).build();
		buttonAddRoute = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_ROUTE.getMutableText(), button -> startEditingRoute(new Route(transportMode, MinecraftClientData.getDashboardInstance()), true)).build();
		buttonAddDepot = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_DEPOT.getMutableText(), button -> startEditingArea(new Depot(transportMode, MinecraftClientData.getDashboardInstance()), true)).build();
		buttonDoneEditingStation = ButtonWidget.builder(Text.translatable("gui.done"), button -> onDoneEditingArea()).build();
		buttonDoneEditingRoute = ButtonWidget.builder(Text.translatable("gui.done"), button -> onDoneEditingRoute()).build();
		buttonDoneEditingRouteDestination = ButtonWidget.builder(Text.translatable("gui.done"), button -> onDoneEditingRouteDestination()).build();
		buttonZoomIn = ButtonWidget.builder(Text.literal("+"), button -> widgetMap.scale(1)).build();
		buttonZoomOut = ButtonWidget.builder(Text.literal("-"), button -> widgetMap.scale(-1)).build();
		buttonMapTopView = new WidgetBetterTexturedButton(Identifier.of("textures/gui/sprites/mtr/icon_map_top_view.png"), Identifier.of("textures/gui/sprites/mtr/icon_map_top_view_highlighted.png"), Identifier.of("textures/gui/sprites/mtr/icon_map_top_view_disabled.png"), button -> {
			widgetMap.setMapOverlayMode(WorldMap.MapOverlayMode.TOP_VIEW);
			toggleButtons();
		}, true);
		buttonMapCurrentY = new WidgetBetterTexturedButton(Identifier.of("textures/gui/sprites/mtr/icon_map_current_y.png"), Identifier.of("textures/gui/sprites/mtr/icon_map_current_y_highlighted.png"), Identifier.of("textures/gui/sprites/mtr/icon_map_current_y_disabled.png"), button -> {
			widgetMap.setMapOverlayMode(WorldMap.MapOverlayMode.CURRENT_Y);
			toggleButtons();
		}, true);
		buttonRailActions = ButtonWidget.builder(TranslationProvider.GUI_MTR_RAIL_ACTIONS_BUTTON.getMutableText(), button -> MinecraftClient.getInstance().setScreen(new RailActionsScreen(this))).build();
		buttonOptions = ButtonWidget.builder(Text.translatable("menu.options"), button -> MinecraftClient.getInstance().setScreen(new ConfigScreen(this))).build();
		buttonTransportSystemMap = ButtonWidget.builder(TranslationProvider.GUI_MTR_TRANSPORT_SYSTEM_MAP.getMutableText(), button -> Util.getOperatingSystem().open(String.format("http://localhost:%s", MTRClient.getServerPort()))).build();
		buttonResourcePackCreator = ButtonWidget.builder(TranslationProvider.GUI_MTR_RESOURCE_PACK_CREATOR.getMutableText(), button -> Util.getOperatingSystem().open(String.format("http://localhost:%s/creator/", MTRClient.getServerPort()))).build();

		dashboardList = new DashboardList(this::onFind, this::onDrawArea, this::onEdit, this::onSort, null, this::onDelete, this::getList, () -> MinecraftClientData.DASHBOARD_SEARCH, text -> MinecraftClientData.DASHBOARD_SEARCH = text);

		onSelectTab(SelectedTab.STATIONS);
	}

	@Override
	protected void init() {
		super.init();

		final int tabCount = 3;
		final int bottomRowY = height - SQUARE_SIZE;

		widgetMap.setPositionAndSize(PANEL_WIDTH, 0, width - PANEL_WIDTH, height - SQUARE_SIZE * 2);

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
		dashboardList.init(this::addDrawableChild);
		addDrawableChild(widgetMap);

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
		addDrawableChild(buttonTransportSystemMap);
		addDrawableChild(buttonResourcePackCreator);
		addDrawableChild(buttonMapTopView);
		addDrawableChild(buttonMapCurrentY);

		addDrawableChild(textFieldName);
		addDrawableChild(textFieldCustomDestination);
		addDrawableChild(colorSelector);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);
		widgetMap.render(context, mouseX, mouseY, delta);
//		context.fill(0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
		dashboardList.render(context);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		dashboardList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		dashboardList.mouseScrolled(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	public void tick() {
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
			MTR.LOGGER.error("", e);
		}
	}

	@Override
	public boolean shouldPause() {
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
						MinecraftClient.getInstance().setScreen(new EditStationScreen((Station) dashboardListItem.data, this));
					}
				} else {
					if (dashboardListItem.data instanceof Platform) {
						MinecraftClient.getInstance().setScreen(new PlatformScreen((Platform) dashboardListItem.data, transportMode, this));
					}
				}
				break;
			case ROUTES:
				if (editingRoute == null && dashboardListItem.data instanceof Route) {
					MinecraftClient.getInstance().setScreen(new EditRouteScreen((Route) dashboardListItem.data, this));
				} else {
					startEditingRouteDestination(index);
				}
				break;
			case DEPOTS:
				if (editingArea == null) {
					if (dashboardListItem.data instanceof Depot) {
						MinecraftClient.getInstance().setScreen(new EditDepotScreen((Depot) dashboardListItem.data, transportMode, this));
					}
				} else {
					if (dashboardListItem.data instanceof Siding) {
						MinecraftClient.getInstance().setScreen(new SidingScreen((Siding) dashboardListItem.data, transportMode, this));
					}
				}
				break;
		}
	}

	private void onSort() {
		if (selectedTab == SelectedTab.ROUTES && editingRoute != null) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
		}
	}

	private void onDelete(DashboardListItem dashboardListItem, int index) {
		switch (selectedTab) {
			case STATIONS:
				if (dashboardListItem.data instanceof Station station) {
					MinecraftClient.getInstance().setScreen(new DeleteConfirmationScreen(() -> RegistryClient.sendPacketToServer(new PacketDeleteData(new DeleteDataRequest().addStationId(station.getId()))), IGui.formatStationName(station.getName()), this));
				}
				break;
			case ROUTES:
				if (editingRoute == null) {
					if (dashboardListItem.data instanceof Route route) {
						MinecraftClient.getInstance().setScreen(new DeleteConfirmationScreen(() -> RegistryClient.sendPacketToServer(new PacketDeleteData(new DeleteDataRequest().addRouteId(route.getId()))), IGui.formatStationName(route.getName()), this));
					}
				} else {
					editingRoute.getRoutePlatforms().remove(index);
					RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
				}
				break;
			case DEPOTS:
				if (dashboardListItem.data instanceof Depot depot) {
					MinecraftClient.getInstance().setScreen(new DeleteConfirmationScreen(() -> RegistryClient.sendPacketToServer(new PacketDeleteData(new DeleteDataRequest().addDepotId(depot.getId()))), IGui.formatStationName(depot.getName()), this));
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

		textFieldName.setText(editingArea.getName());
		colorSelector.setColor(editingArea.getColor());

		widgetMap.startEditingArea(editingArea);
		toggleButtons();
	}

	private void startEditingRoute(Route editingRoute, boolean isNew) {
		editingArea = null;
		this.editingRoute = editingRoute;
		this.isNew = isNew;
		editingRoutePlatformIndex = -1;

		textFieldName.setText(editingRoute.getName());
		colorSelector.setColor(editingRoute.getColor());

		widgetMap.startEditingRoute();
		toggleButtons();
	}

	private void startEditingRouteDestination(int index) {
		editingRoutePlatformIndex = index;
		if (isValidRoutePlatformIndex()) {
			textFieldCustomDestination.setText(editingRoute.getRoutePlatforms().get(index).getCustomDestination());
		}
		toggleButtons();
	}

	private void onDrawCorners(IntIntImmutablePair corner1, IntIntImmutablePair corner2) {
		editingArea.setCorners(new Position(corner1.leftInt(), Long.MIN_VALUE, corner1.rightInt()), new Position(corner2.leftInt(), Long.MAX_VALUE, corner2.rightInt()));
		toggleButtons();
	}

	private void onDrawCornersMouseRelease() {
		if (editingArea instanceof Station) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation((Station) editingArea)));
		} else if (editingArea instanceof Depot) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot((Depot) editingArea)));
		}
	}

	private void onClickAddPlatformToRoute(long platformId) {
		final RoutePlatformData routePlatformData = new RoutePlatformData(platformId);
		editingRoute.getRoutePlatforms().add(routePlatformData);
		routePlatformData.writePlatformCache(editingRoute, MinecraftClientData.getDashboardInstance().platformIdMap);
		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
	}

	private void onClickEditSavedRail(SavedRailBase<?, ?> savedRail) {
		if (savedRail instanceof Platform) {
			MinecraftClient.getInstance().setScreen(new PlatformScreen((Platform) savedRail, transportMode, this));
		} else if (savedRail instanceof Siding) {
			MinecraftClient.getInstance().setScreen(new SidingScreen((Siding) savedRail, transportMode, this));
		}
	}

	private void onDoneEditingArea() {
		editingArea.setName(IGui.textOrUntitled(textFieldName.getText()));
		editingArea.setColor(colorSelector.getColor());
		if (editingArea instanceof Station) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation((Station) editingArea)));
		} else if (editingArea instanceof Depot) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot((Depot) editingArea)));
		}
		stopEditing();
	}

	private void onDoneEditingRoute() {
		editingRoute.setName(IGui.textOrUntitled(textFieldName.getText()));
		editingRoute.setColor(colorSelector.getColor());
		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
		stopEditing();
	}

	private void onDoneEditingRouteDestination() {
		if (isValidRoutePlatformIndex()) {
			editingRoute.getRoutePlatforms().get(editingRoutePlatformIndex).setCustomDestination(textFieldCustomDestination.getText());
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
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
