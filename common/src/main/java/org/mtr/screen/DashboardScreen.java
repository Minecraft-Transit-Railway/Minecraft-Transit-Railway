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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class DashboardScreen extends MTRScreenBase {

	private SelectedTab selectedTab = SelectedTab.STATIONS;
	private AreaBase<?, ?> editingArea;
	private Route editingRoute;
	private int editingRoutePlatformIndex;
	private boolean isNew;
	private int tickCount;

	private final TransportMode transportMode;
	private final boolean hasPermission = MinecraftClientData.hasPermission();
	private final MapWidget mapWidget;
	private final TabGroupWidget tabGroupWidget;

	private final ScrollableListWidget<Station> stationsListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Route> routesListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Depot> depotsListWidget = new ScrollableListWidget<>();

	private final BetterTextFieldWidget stationsFilterTextField = new BetterTextFieldWidget(STATIONS_SEARCH_TEXT, 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString(), text -> {
		stationsListWidget.setFilter(text);
		STATIONS_SEARCH_TEXT = text;
	});
	private final BetterTextFieldWidget routesFilterTextField = new BetterTextFieldWidget(ROUTES_SEARCH_TEXT, 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString(), text -> {
		routesListWidget.setFilter(text);
		ROUTES_SEARCH_TEXT = text;
	});
	private final BetterTextFieldWidget depotsFilterTextField = new BetterTextFieldWidget(DEPOTS_SEARCH_TEXT, 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString(), text -> {
		depotsListWidget.setFilter(text);
		DEPOTS_SEARCH_TEXT = text;
	});

	private final BetterButtonWidget expandAllButton = new BetterButtonWidget(GuiHelper.EXPAND_ALL_TEXTURE_ID, null, 0, routesListWidget::toggleExpansion);
	private final BetterButtonWidget collapseAllButton = new BetterButtonWidget(GuiHelper.COLLAPSE_ALL_TEXTURE_ID, null, 0, routesListWidget::toggleExpansion);

	private final BetterButtonWidget addStationButton;
	private final BetterButtonWidget addRouteButton;
	private final BetterButtonWidget addDepotButton;

	private final DeleteConfirmationWidget deleteConfirmationWidget;

	private final ButtonWidget buttonDoneEditingStation;
	private final ButtonWidget buttonDoneEditingRoute;
	private final ButtonWidget buttonDoneEditingRouteDestination;
	private final ButtonWidget buttonZoomIn;
	private final ButtonWidget buttonZoomOut;
	private final ButtonWidget buttonRailActions;
	private final ButtonWidget buttonOptions;
	private final ButtonWidget buttonTransportSystemMap;
	private final ButtonWidget buttonResourcePackCreator;

	//	private final BetterTextFieldWidget textFieldName;
	//	private final BetterTextFieldWidget textFieldCustomDestination;
	private final ColorSelectorWidget colorSelector;

	private static String STATIONS_SEARCH_TEXT = "";
	private static String ROUTES_SEARCH_TEXT = "";
	private static String DEPOTS_SEARCH_TEXT = "";

	public static final int MAX_COLOR_ZONE_LENGTH = 6;
	private static final int PANEL_WIDTH = 144;
	private static final int COLOR_WIDTH = 48;

	public DashboardScreen(TransportMode transportMode) {
		super();
		this.transportMode = transportMode;

//		textFieldName = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_NAME.getString());
//		textFieldCustomDestination = new BetterTextFieldWidget(1024, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_CUSTOM_DESTINATION_SUGGESTION.getString());
		colorSelector = new ColorSelectorWidget(this, true, this::toggleButtons);

		mapWidget = new MapWidget(transportMode, this::onDelete, this::onDrawCorners, this::onDrawCornersMouseRelease);

		tabGroupWidget = new TabGroupWidget(PANEL_WIDTH, index -> {
			selectedTab = SelectedTab.values()[index];
			stopEditing();
			mapWidget.setShowStations(selectedTab != SelectedTab.DEPOTS);
		}, TranslationProvider.GUI_MTR_STATIONS.getMutableText(), TranslationProvider.GUI_MTR_ROUTES.getMutableText(), TranslationProvider.GUI_MTR_DEPOTS.getMutableText());

		addStationButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_STATION.getString(), tabGroupWidget.getWidth(), () -> startEditingArea(new Station(MinecraftClientData.getDashboardInstance()), true));
		addRouteButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_ROUTE.getString(), tabGroupWidget.getWidth(), () -> startEditingRoute(new Route(transportMode, MinecraftClientData.getDashboardInstance()), true));
		addDepotButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_DEPOT.getString(), tabGroupWidget.getWidth(), () -> startEditingArea(new Depot(transportMode, MinecraftClientData.getDashboardInstance()), true));

		deleteConfirmationWidget = new DeleteConfirmationWidget(() -> enableControls(true), this::applyBlur);

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

		final int listY = GuiHelper.DEFAULT_LINE_SIZE * 2 + GuiHelper.DEFAULT_PADDING * 2;
		final int listHeight = height - listY - (hasPermission ? GuiHelper.DEFAULT_LINE_SIZE : 0);
		stationsListWidget.setY(listY);
		stationsListWidget.setBounds(tabGroupWidget.getWidth(), listHeight, tabGroupWidget.getWidth(), listHeight);
		routesListWidget.setY(listY);
		routesListWidget.setBounds(tabGroupWidget.getWidth(), listHeight, tabGroupWidget.getWidth(), listHeight);
		depotsListWidget.setY(listY);
		depotsListWidget.setBounds(tabGroupWidget.getWidth(), listHeight, tabGroupWidget.getWidth(), listHeight);

		stationsFilterTextField.setX(GuiHelper.DEFAULT_PADDING);
		stationsFilterTextField.setY(GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		stationsFilterTextField.setWidth(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2);
		routesFilterTextField.setX(GuiHelper.DEFAULT_PADDING);
		routesFilterTextField.setY(GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		routesFilterTextField.setWidth(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 3 - GuiHelper.DEFAULT_LINE_SIZE);
		depotsFilterTextField.setX(GuiHelper.DEFAULT_PADDING);
		depotsFilterTextField.setY(GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		depotsFilterTextField.setWidth(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2);

		expandAllButton.setX(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING - GuiHelper.DEFAULT_LINE_SIZE);
		expandAllButton.setY(GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		collapseAllButton.setX(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING - GuiHelper.DEFAULT_LINE_SIZE);
		collapseAllButton.setY(GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);

		addStationButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);
		addRouteButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);
		addDepotButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);

		deleteConfirmationWidget.setX(width / 4);
		deleteConfirmationWidget.setWidth(width / 2);

		toggleButtons();

		addDrawableChild(mapWidget);
		addDrawableChild(tabGroupWidget);

		addDrawableChild(stationsListWidget);
		addDrawableChild(routesListWidget);
		addDrawableChild(depotsListWidget);

		addDrawableChild(stationsFilterTextField);
		addDrawableChild(routesFilterTextField);
		addDrawableChild(depotsFilterTextField);

		addDrawableChild(expandAllButton);
		addDrawableChild(collapseAllButton);

		addDrawableChild(addStationButton);
		addDrawableChild(addRouteButton);
		addDrawableChild(addDepotButton);

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
		stationsListWidget.visible = selectedTab == SelectedTab.STATIONS;
		routesListWidget.visible = selectedTab == SelectedTab.ROUTES;
		depotsListWidget.visible = selectedTab == SelectedTab.DEPOTS;

		stationsFilterTextField.visible = selectedTab == SelectedTab.STATIONS;
		routesFilterTextField.visible = selectedTab == SelectedTab.ROUTES;
		depotsFilterTextField.visible = selectedTab == SelectedTab.DEPOTS;

		if (selectedTab == SelectedTab.ROUTES) {
			final boolean canCollapse = routesListWidget.canCollapse();
			expandAllButton.visible = !canCollapse;
			collapseAllButton.visible = canCollapse;
		} else {
			expandAllButton.visible = false;
			collapseAllButton.visible = false;
		}

		addStationButton.visible = selectedTab == SelectedTab.STATIONS && editingArea == null && hasPermission;
		addRouteButton.visible = selectedTab == SelectedTab.ROUTES && editingRoute == null && hasPermission;
		addDepotButton.visible = selectedTab == SelectedTab.DEPOTS && editingArea == null && hasPermission;

		switch (selectedTab) {
			case STATIONS -> {
				if (editingArea == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<Station>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, mapWidget::find));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, station -> System.out.println("editing " + station.getName())));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, station -> onDelete(station, new DeleteDataRequest().addStationId(station.getId()))));
					}
					ScrollableListWidget.setAreas(stationsListWidget, MinecraftClientData.getDashboardInstance().stations, actions);
				}
			}
			case ROUTES -> {
				final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<Route>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, route -> System.out.println("editing " + route.getName())));
				if (hasPermission) {
					actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, route -> onDelete(route, new DeleteDataRequest().addRouteId(route.getId()))));
				}
				ScrollableListWidget.setRoutes(routesListWidget, MinecraftClientData.getDashboardInstance().routes, actions);
			}
			case DEPOTS -> {
				final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<Depot>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, mapWidget::find));
				if (hasPermission) {
					actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, depot -> System.out.println("editing " + depot.getName())));
					actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, depot -> onDelete(depot, new DeleteDataRequest().addDepotId(depot.getId()))));
				}
				ScrollableListWidget.setAreas(depotsListWidget, MinecraftClientData.getDashboardInstance().depots, actions);
			}
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
		stationsFilterTextField.active = enabled;
		routesFilterTextField.active = enabled;
		depotsFilterTextField.active = enabled;
		expandAllButton.active = enabled;
		collapseAllButton.active = enabled;
	}

	private void startEditingArea(AreaBase<?, ?> editingArea, boolean isNew) {
		this.editingArea = editingArea;
		editingRoute = null;
		this.isNew = isNew;

//		textFieldName.setText(editingArea.getName());
		colorSelector.setColor(editingArea.getColor());

		mapWidget.startEditingArea();
		toggleButtons();
	}

	private void startEditingRoute(Route editingRoute, boolean isNew) {
		editingArea = null;
		this.editingRoute = editingRoute;
		this.isNew = isNew;
		editingRoutePlatformIndex = -1;

//		textFieldName.setText(editingRoute.getName());
		colorSelector.setColor(editingRoute.getColor());

		mapWidget.startEditingRoute();
		toggleButtons();
	}

	private void startEditingRouteDestination(int index) {
		editingRoutePlatformIndex = index;
		if (isValidRoutePlatformIndex()) {
//			textFieldCustomDestination.setText(editingRoute.getRoutePlatforms().get(index).getCustomDestination());
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
//		editingArea.setName(IGui.textOrUntitled(textFieldName.getText()));
		editingArea.setColor(colorSelector.getColor());
		if (editingArea instanceof Station) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation((Station) editingArea)));
		} else if (editingArea instanceof Depot) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot((Depot) editingArea)));
		}
		stopEditing();
	}

	private void onDoneEditingRoute() {
//		editingRoute.setName(IGui.textOrUntitled(textFieldName.getText()));
		editingRoute.setColor(colorSelector.getColor());
		RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(editingRoute)));
		stopEditing();
	}

	private void onDoneEditingRouteDestination() {
		if (isValidRoutePlatformIndex()) {
//			editingRoute.getRoutePlatforms().get(editingRoutePlatformIndex).setCustomDestination(textFieldCustomDestination.getText());
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
		final boolean showRouteDestinationFields = isValidRoutePlatformIndex();

		buttonDoneEditingStation.visible = (selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null;
		buttonDoneEditingStation.active = AreaBase.validCorners(editingArea);
		buttonDoneEditingRoute.visible = selectedTab == SelectedTab.ROUTES && editingRoute != null && !showRouteDestinationFields;
		buttonDoneEditingRouteDestination.visible = selectedTab == SelectedTab.ROUTES && editingRoute != null && showRouteDestinationFields;

		final boolean showTextFields = ((selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null) || (selectedTab == SelectedTab.ROUTES && editingRoute != null && !showRouteDestinationFields);
//		textFieldName.visible = showTextFields;
//		textFieldCustomDestination.visible = showRouteDestinationFields;
		colorSelector.visible = showTextFields;
	}

	private enum SelectedTab {STATIONS, ROUTES, DEPOTS}
}
