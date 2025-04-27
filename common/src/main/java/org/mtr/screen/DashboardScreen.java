package org.mtr.screen;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketDeleteData;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

public final class DashboardScreen extends MTRScreenBase {

	private SelectedTab selectedTab = SelectedTab.STATIONS;
	private AreaBase<?, ?> editingArea;
	private Route editingRoute;
	private int editingRoutePlatformIndex;
	private boolean isNew;
	private int tickCount;

	private final TransportMode transportMode;
	private final MapWidget mapWidget;
	private final TabGroupWidget tabGroupWidget;

	private final ScrollableListWidget<Station> stationsListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Route> routesListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Depot> depotsListWidget = new ScrollableListWidget<>();

	private final DeleteConfirmationWidget deleteConfirmationWidget;

	private final ButtonWidget buttonAddStation;
	private final ButtonWidget buttonAddRoute;
	private final ButtonWidget buttonAddDepot;
	private final ButtonWidget buttonDoneEditingStation;
	private final ButtonWidget buttonDoneEditingRoute;
	private final ButtonWidget buttonDoneEditingRouteDestination;
	private final ButtonWidget buttonZoomIn;
	private final ButtonWidget buttonZoomOut;
	private final ButtonWidget buttonRailActions;
	private final ButtonWidget buttonOptions;
	private final ButtonWidget buttonTransportSystemMap;
	private final ButtonWidget buttonResourcePackCreator;

	private final BetterTextFieldWidget textFieldName;
	private final BetterTextFieldWidget textFieldCustomDestination;
	private final ColorSelectorWidget colorSelector;

	public static final int MAX_COLOR_ZONE_LENGTH = 6;
	private static final int PANEL_WIDTH = 144;
	private static final int COLOR_WIDTH = 48;

	public DashboardScreen(TransportMode transportMode) {
		super();
		this.transportMode = transportMode;

		textFieldName = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_NAME.getString());
		textFieldCustomDestination = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_CUSTOM_DESTINATION_SUGGESTION.getString());
		colorSelector = new ColorSelectorWidget(this, true, this::toggleButtons);

		mapWidget = new MapWidget(transportMode, this::onDelete, this::onDrawCorners, this::onDrawCornersMouseRelease);

		tabGroupWidget = new TabGroupWidget(PANEL_WIDTH, index -> {
			selectedTab = SelectedTab.values()[index];
			stopEditing();
			mapWidget.setShowStations(selectedTab != SelectedTab.DEPOTS);
		}, TranslationProvider.GUI_MTR_STATIONS.getMutableText(), TranslationProvider.GUI_MTR_ROUTES.getMutableText(), TranslationProvider.GUI_MTR_DEPOTS.getMutableText());

		deleteConfirmationWidget = new DeleteConfirmationWidget(() -> enableControls(true), this::applyBlur);

		buttonAddStation = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_STATION.getMutableText(), button -> startEditingArea(new Station(MinecraftClientData.getDashboardInstance()), true)).build();
		buttonAddRoute = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_ROUTE.getMutableText(), button -> startEditingRoute(new Route(transportMode, MinecraftClientData.getDashboardInstance()), true)).build();
		buttonAddDepot = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_DEPOT.getMutableText(), button -> startEditingArea(new Depot(transportMode, MinecraftClientData.getDashboardInstance()), true)).build();
		buttonDoneEditingStation = ButtonWidget.builder(Text.translatable("gui.done"), button -> onDoneEditingArea()).build();
		buttonDoneEditingRoute = ButtonWidget.builder(Text.translatable("gui.done"), button -> onDoneEditingRoute()).build();
		buttonDoneEditingRouteDestination = ButtonWidget.builder(Text.translatable("gui.done"), button -> onDoneEditingRouteDestination()).build();
		buttonZoomIn = ButtonWidget.builder(Text.literal("+"), button -> mapWidget.scale(1)).build();
		buttonZoomOut = ButtonWidget.builder(Text.literal("-"), button -> mapWidget.scale(-1)).build();
		buttonRailActions = ButtonWidget.builder(TranslationProvider.GUI_MTR_RAIL_ACTIONS_BUTTON.getMutableText(), button -> MinecraftClient.getInstance().setScreen(new RailActionsScreen(this))).build();
		buttonOptions = ButtonWidget.builder(Text.translatable("menu.options"), button -> MinecraftClient.getInstance().setScreen(new ConfigScreen(this))).build();
		buttonTransportSystemMap = ButtonWidget.builder(TranslationProvider.GUI_MTR_TRANSPORT_SYSTEM_MAP.getMutableText(), button -> Util.getOperatingSystem().open(String.format("http://localhost:%s", MTRClient.getServerPort()))).build();
		buttonResourcePackCreator = ButtonWidget.builder(TranslationProvider.GUI_MTR_RESOURCE_PACK_CREATOR.getMutableText(), button -> Util.getOperatingSystem().open(String.format("http://localhost:%s/creator/", MTRClient.getServerPort()))).build();

		tabGroupWidget.selectTab(0);
	}

	@Override
	protected void init() {
		super.init();

//		final int tabCount = 3;
//		final int bottomRowY = height - SQUARE_SIZE;
//
//		IDrawing.setPositionAndWidth(buttonAddStation, 0, bottomRowY, PANEL_WIDTH);
//		IDrawing.setPositionAndWidth(buttonAddRoute, 0, bottomRowY, PANEL_WIDTH);
//		IDrawing.setPositionAndWidth(buttonAddDepot, 0, bottomRowY, PANEL_WIDTH);
//		IDrawing.setPositionAndWidth(buttonDoneEditingStation, 0, bottomRowY, PANEL_WIDTH);
//		IDrawing.setPositionAndWidth(buttonDoneEditingRoute, 0, bottomRowY, PANEL_WIDTH);
//		IDrawing.setPositionAndWidth(buttonDoneEditingRouteDestination, 0, bottomRowY, PANEL_WIDTH);
//		IDrawing.setPositionAndWidth(buttonZoomIn, width - SQUARE_SIZE * 2, bottomRowY - SQUARE_SIZE, SQUARE_SIZE);
//		IDrawing.setPositionAndWidth(buttonZoomOut, width - SQUARE_SIZE, bottomRowY - SQUARE_SIZE, SQUARE_SIZE);
//		IDrawing.setPositionAndWidth(buttonRailActions, width - SQUARE_SIZE * 7, bottomRowY - SQUARE_SIZE, SQUARE_SIZE * 5);
//		IDrawing.setPositionAndWidth(buttonOptions, width - SQUARE_SIZE * 7, bottomRowY, SQUARE_SIZE * 5);
//		IDrawing.setPositionAndWidth(buttonTransportSystemMap, PANEL_WIDTH, bottomRowY - SQUARE_SIZE, width - SQUARE_SIZE * 7 - PANEL_WIDTH);
//		IDrawing.setPositionAndWidth(buttonResourcePackCreator, PANEL_WIDTH, bottomRowY, width - SQUARE_SIZE * 7 - PANEL_WIDTH);
//
//		IDrawing.setPositionAndWidth(textFieldName, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - COLOR_WIDTH - TEXT_FIELD_PADDING);
//		IDrawing.setPositionAndWidth(textFieldCustomDestination, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - TEXT_FIELD_PADDING);
//		IDrawing.setPositionAndWidth(colorSelector, PANEL_WIDTH - COLOR_WIDTH + TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, COLOR_WIDTH - TEXT_FIELD_PADDING);

		mapWidget.setX(tabGroupWidget.getWidth());
		mapWidget.setWidth(width - tabGroupWidget.getWidth());
		mapWidget.setHeight(height);

		stationsListWidget.setY(GuiHelper.DEFAULT_LINE_SIZE);
		stationsListWidget.setBounds(tabGroupWidget.getWidth(), height - GuiHelper.DEFAULT_LINE_SIZE, tabGroupWidget.getWidth(), height - GuiHelper.DEFAULT_LINE_SIZE);
		routesListWidget.setY(GuiHelper.DEFAULT_LINE_SIZE);
		routesListWidget.setBounds(tabGroupWidget.getWidth(), height - GuiHelper.DEFAULT_LINE_SIZE, tabGroupWidget.getWidth(), height - GuiHelper.DEFAULT_LINE_SIZE);
		depotsListWidget.setY(GuiHelper.DEFAULT_LINE_SIZE);
		depotsListWidget.setBounds(tabGroupWidget.getWidth(), height - GuiHelper.DEFAULT_LINE_SIZE, tabGroupWidget.getWidth(), height - GuiHelper.DEFAULT_LINE_SIZE);

		deleteConfirmationWidget.setX(width / 4);
		deleteConfirmationWidget.setWidth(width / 2);

		toggleButtons();

		addDrawableChild(mapWidget);
		addDrawableChild(tabGroupWidget);
		addDrawableChild(stationsListWidget);
		addDrawableChild(routesListWidget);
		addDrawableChild(depotsListWidget);
		addDrawableChild(deleteConfirmationWidget);

//		addDrawableChild(buttonAddStation);
//		addDrawableChild(buttonAddRoute);
//		addDrawableChild(buttonAddDepot);
//		addDrawableChild(buttonDoneEditingStation);
//		addDrawableChild(buttonDoneEditingRoute);
//		addDrawableChild(buttonDoneEditingRouteDestination);
//		addDrawableChild(buttonZoomIn);
//		addDrawableChild(buttonZoomOut);
//		addDrawableChild(buttonRailActions);
//		addDrawableChild(buttonOptions);
//		addDrawableChild(buttonTransportSystemMap);
//		addDrawableChild(buttonResourcePackCreator);
//
//		addDrawableChild(textFieldName);
//		addDrawableChild(textFieldCustomDestination);
//		addDrawableChild(colorSelector);
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, width, height, GuiHelper.BACKGROUND_COLOR);
	}

	@Override
	public void tick() {
		stationsListWidget.setX(selectedTab == SelectedTab.STATIONS ? 0 : width);
		routesListWidget.setX(selectedTab == SelectedTab.ROUTES ? 0 : width);
		depotsListWidget.setX(selectedTab == SelectedTab.DEPOTS ? 0 : width);

		switch (tickCount % 3) {
			case 0 -> ScrollableListWidget.setAreas(stationsListWidget, MinecraftClientData.getDashboardInstance().stations, ObjectArrayList.of(
					new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_edit.png"), station -> System.out.println("editing " + station.getName())),
					new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_delete.png"), station -> onDelete(station, new DeleteDataRequest().addStationId(station.getId())))
			));
			case 1 -> {
				final ObjectArrayList<Route> routes = new ObjectArrayList<>(MinecraftClientData.getDashboardInstance().routes);
				Collections.sort(routes);
//				routesListWidget.setData(routes);
			}
			case 2 -> ScrollableListWidget.setAreas(depotsListWidget, MinecraftClientData.getDashboardInstance().depots, ObjectArrayList.of(
					new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_edit.png"), depot -> System.out.println("editing " + depot.getName())),
					new ObjectObjectImmutablePair<>(Identifier.of("textures/gui/sprites/mtr/icon_delete.png"), depot -> onDelete(depot, new DeleteDataRequest().addDepotId(depot.getId())))
			));
		}
		tickCount++;

		try {
			switch (selectedTab) {
				case STATIONS:
					if (editingArea == null) {
//						dashboardList.setData(MinecraftClientData.convertDataSet(MinecraftClientData.getDashboardInstance().stations), true, true, true, false, false, true);
					} else {
//						dashboardList.setData(MinecraftClientData.convertDataSet(editingArea.savedRails), true, false, true, false, false, false);
					}
					break;
				case ROUTES:
					if (editingRoute == null) {
//						dashboardList.setData(MinecraftClientData.getFilteredDataSet(transportMode, MinecraftClientData.getDashboardInstance().routes), false, true, true, false, false, true);
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
//						dashboardList.setData(routeData, false, false, true, true, false, true);
					}
					break;
				case DEPOTS:
					if (editingArea == null) {
//						dashboardList.setData(MinecraftClientData.getFilteredDataSet(transportMode, MinecraftClientData.getDashboardInstance().depots), true, true, true, false, false, true);
					} else {
//						dashboardList.setData(MinecraftClientData.convertDataSet(editingArea.savedRails), true, false, true, false, false, false);
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

	private void onFind(DashboardListItem dashboardListItem, int index) {
		if (selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) {
			if (editingArea == null && dashboardListItem.data instanceof AreaBase<?, ?> area) {
				if (AreaBase.validCorners(area)) {
					mapWidget.find(area.getCenter());
				}
			} else if (selectedTab == SelectedTab.STATIONS) {
				final Platform platform = (Platform) dashboardListItem.data;
				mapWidget.find(platform.getMidPosition());
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
//		dashboardList.clearSearch();
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

	private <T extends NameColorDataBase> void onDelete(T data, DeleteDataRequest deleteDataRequest) {
		deleteConfirmationWidget.setDeleteCallback(() -> RegistryClient.sendPacketToServer(new PacketDeleteData(deleteDataRequest)), IGui.formatStationName(data.getName()));
		deleteConfirmationWidget.setY(height / 2 - deleteConfirmationWidget.getHeight() / 2);
		enableControls(false);
	}

	private void enableControls(boolean enabled) {
		mapWidget.active = enabled;
		tabGroupWidget.active = enabled;
		stationsListWidget.active = enabled;
		routesListWidget.active = enabled;
		depotsListWidget.active = enabled;
	}

	private void startEditingArea(AreaBase<?, ?> editingArea, boolean isNew) {
		this.editingArea = editingArea;
		editingRoute = null;
		this.isNew = isNew;

		textFieldName.setText(editingArea.getName());
		colorSelector.setColor(editingArea.getColor());

		mapWidget.startEditingArea();
		toggleButtons();
	}

	private void startEditingRoute(Route editingRoute, boolean isNew) {
		editingArea = null;
		this.editingRoute = editingRoute;
		this.isNew = isNew;
		editingRoutePlatformIndex = -1;

		textFieldName.setText(editingRoute.getName());
		colorSelector.setColor(editingRoute.getColor());

		mapWidget.startEditingRoute();
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
		mapWidget.stopEditing();
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

		final boolean showTextFields = ((selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null) || (selectedTab == SelectedTab.ROUTES && editingRoute != null && !showRouteDestinationFields);
		textFieldName.visible = showTextFields;
		textFieldCustomDestination.visible = showRouteDestinationFields;
		colorSelector.visible = showTextFields;
	}

	private enum SelectedTab {STATIONS, ROUTES, DEPOTS}
}
