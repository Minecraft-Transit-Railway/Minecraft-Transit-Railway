package org.mtr.screen;

import gg.essential.elementa.components.UIBlock;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMinecraft;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.MTRClient;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.*;
import org.mtr.core.operation.DeleteDataRequest;
import org.mtr.core.operation.UpdateDataRequest;
import org.mtr.core.serializer.SerializedDataBase;
import org.mtr.core.tool.Utilities;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.packet.PacketDeleteData;
import org.mtr.packet.PacketUpdateData;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import java.awt.*;
import java.util.Random;
import java.util.stream.Collectors;

public final class DashboardScreen extends WindowBase {

	private Tab currentTab = Tab.STATIONS;
	@Nullable
	private SimpleAreaBase editingArea;
	@Nullable
	private Route editingRoute;
	private int editingRoutePlatformIndex = -1;

	private final TransportMode transportMode;
	private final boolean hasPermission = MinecraftClientData.hasPermission();
	private final MapComponent mapComponent;

	private final ButtonComponent stationsTabButton;
	private final ButtonComponent routesTabButton;
	private final ButtonComponent depotsTabButton;
	private final ButtonComponent homesTabButton;
	private final ButtonComponent landmarksTabButton;

	private final ListComponent<Station> stationsListComponent = new ListComponent<>();
	private final ListComponent<Platform> stationPlatformsListComponent = new ListComponent<>();
	private final ListComponent<Route> routesListComponent = new ListComponent<>();
	private final ListComponent<RoutePlatformData> routePlatformsListComponent = new ListComponent<>();
	private final ListComponent<Depot> depotsListComponent = new ListComponent<>();
	private final ListComponent<Siding> depotSidingsListComponent = new ListComponent<>();
	private final ListComponent<Home> homesListComponent = new ListComponent<>();
	private final ListComponent<Landmark> landmarksListComponent = new ListComponent<>();

	private final TextInputComponent stationNameTextInput = new TextInputComponent();
	private final TextInputComponent routeNameTextInput = new TextInputComponent();
	private final TextInputComponent depotNameTextInput = new TextInputComponent();
	private final TextInputComponent routeDestinationTextInput = new TextInputComponent();
	private final TextInputComponent homeNameTextInput = new TextInputComponent();
	private final TextInputComponent landmarkNameTextInput = new TextInputComponent();

	private final UIContainer stationsTabContainer;
	private final UIContainer stationPlatformsTabContainer;
	private final UIContainer routesTabContainer;
	private final UIContainer routePlatformsTabContainer;
	private final UIContainer routeDestinationTabContainer;
	private final UIContainer depotsTabContainer;
	private final UIContainer depotSidingsTabContainer;
	private final UIContainer homesTabContainer;
	private final UIContainer homeTabContainer;
	private final UIContainer landmarksTabContainer;
	private final UIContainer landmarkTabContainer;

	private static final int PANEL_WIDTH = 160;

	public DashboardScreen(TransportMode transportMode) {
		this.transportMode = transportMode;

		final UIContainer leftContainer = (UIContainer) new UIContainer()
			.setChildOf(getWindow())
			.setWidth(new PixelConstraint(PANEL_WIDTH))
			.setHeight(new RelativeConstraint());

		final UIContainer tabButtonsContainer = (UIContainer) new UIContainer()
			.setChildOf(leftContainer)
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		stationsTabButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(tabButtonsContainer)
			.setWidth(new PixelConstraint((float) PANEL_WIDTH / 3)); // TODO hide homes and landmarks tabs

		stationsTabButton.setText(TranslationProvider.GUI_MTR_STATIONS.getString());
		stationsTabButton.onClick(() -> stopEditingAndSelectTab(Tab.STATIONS));

		routesTabButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(tabButtonsContainer)
			.setX(new SiblingConstraint())
			.setWidth(new PixelConstraint((float) PANEL_WIDTH / 3)); // TODO hide homes and landmarks tabs

		routesTabButton.setText(TranslationProvider.GUI_MTR_ROUTES.getString());
		routesTabButton.onClick(() -> stopEditingAndSelectTab(Tab.ROUTES));

		depotsTabButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(tabButtonsContainer)
			.setX(new SiblingConstraint())
			.setWidth(new PixelConstraint((float) PANEL_WIDTH / 3)); // TODO hide homes and landmarks tabs

		depotsTabButton.setText(TranslationProvider.GUI_MTR_DEPOTS.getString());
		depotsTabButton.onClick(() -> stopEditingAndSelectTab(Tab.DEPOTS));

		homesTabButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(tabButtonsContainer)
			.setX(new SiblingConstraint())
			.setWidth(new PixelConstraint((float) 0 * PANEL_WIDTH / Tab.values().length)); // TODO hide homes and landmarks tabs

		homesTabButton.setText(TranslationProvider.GUI_MTR_HOMES.getString());
		homesTabButton.onClick(() -> stopEditingAndSelectTab(Tab.HOMES));

		landmarksTabButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(tabButtonsContainer)
			.setX(new SiblingConstraint())
			.setWidth(new PixelConstraint((float) 0 * PANEL_WIDTH / Tab.values().length)); // TODO hide homes and landmarks tabs

		landmarksTabButton.setText(TranslationProvider.GUI_MTR_LANDMARKS.getString());
		landmarksTabButton.onClick(() -> stopEditingAndSelectTab(Tab.LANDMARKS));

		final UIContainer tabsContainer = (UIContainer) new UIContainer()
			.setChildOf(leftContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new FillConstraint());

		final ButtonComponent addStationButton = new ButtonComponent(true);
		final ButtonComponent addRouteButton = new ButtonComponent(true);
		final ButtonComponent addDepotButton = new ButtonComponent(true);
		final ButtonComponent addHomeButton = new ButtonComponent(true);
		final ButtonComponent addLandmarkButton = new ButtonComponent(true);
		final ButtonComponent doneEditingStationButton = new ButtonComponent(true);
		final ButtonComponent doneEditingRouteButton = new ButtonComponent(true);
		final ButtonComponent doneEditingRouteDestinationButton = new ButtonComponent(true);
		final ButtonComponent doneEditingDepotButton = new ButtonComponent(true);
		final ButtonComponent doneEditingHomeButton = new ButtonComponent(true);
		final ButtonComponent doneEditingLandmarkButton = new ButtonComponent(true);

		stationsTabContainer = createTab(tabsContainer, true, stationsListComponent, null, null, null, addStationButton);
		stationPlatformsTabContainer = createTab(tabsContainer, false, stationPlatformsListComponent, TranslationProvider.GUI_MTR_EDIT_AREA.getString(), TranslationProvider.GUI_MTR_STATION_NAME.getString(), stationNameTextInput, doneEditingStationButton);
		routesTabContainer = createTab(tabsContainer, true, routesListComponent, null, null, null, addRouteButton);
		routePlatformsTabContainer = createTab(tabsContainer, false, routePlatformsListComponent, TranslationProvider.GUI_MTR_EDIT_ROUTE.getString(), TranslationProvider.GUI_MTR_ROUTE_NAME.getString(), routeNameTextInput, doneEditingRouteButton);
		routeDestinationTabContainer = createTab(tabsContainer, false, null, null, TranslationProvider.GUI_MTR_CUSTOM_DESTINATION_SUGGESTION.getString(), routeDestinationTextInput, doneEditingRouteDestinationButton);
		depotsTabContainer = createTab(tabsContainer, true, depotsListComponent, null, null, null, addDepotButton);
		depotSidingsTabContainer = createTab(tabsContainer, false, depotSidingsListComponent, TranslationProvider.GUI_MTR_EDIT_AREA.getString(), TranslationProvider.GUI_MTR_DEPOT_NAME.getString(), depotNameTextInput, doneEditingDepotButton);
		homesTabContainer = createTab(tabsContainer, true, homesListComponent, null, null, null, addHomeButton);
		homeTabContainer = createTab(tabsContainer, false, null, TranslationProvider.GUI_MTR_EDIT_AREA.getString(), TranslationProvider.GUI_MTR_HOME_NAME.getString(), homeNameTextInput, doneEditingHomeButton);
		landmarksTabContainer = createTab(tabsContainer, true, landmarksListComponent, null, null, null, addLandmarkButton);
		landmarkTabContainer = createTab(tabsContainer, false, null, TranslationProvider.GUI_MTR_EDIT_AREA.getString(), TranslationProvider.GUI_MTR_LANDMARK_NAME.getString(), landmarkNameTextInput, doneEditingLandmarkButton);

		addStationButton.setText(TranslationProvider.GUI_MTR_ADD_STATION.getString());
		addStationButton.onClick(() -> startEditingDataNew(new Station(MinecraftClientData.getDashboardInstance()), true));
		addRouteButton.setText(TranslationProvider.GUI_MTR_ADD_ROUTE.getString());
		addRouteButton.onClick(() -> startEditingDataNew(new Route(transportMode, MinecraftClientData.getDashboardInstance()), true));
		addDepotButton.setText(TranslationProvider.GUI_MTR_ADD_DEPOT.getString());
		addDepotButton.onClick(() -> startEditingDataNew(new Depot(transportMode, MinecraftClientData.getDashboardInstance()), true));
		addHomeButton.setText(TranslationProvider.GUI_MTR_ADD_HOME.getString());
		addHomeButton.onClick(() -> startEditingDataNew(new Home(MinecraftClientData.getDashboardInstance()), false));
		addLandmarkButton.setText(TranslationProvider.GUI_MTR_ADD_LANDMARK.getString());
		addLandmarkButton.onClick(() -> startEditingDataNew(new Landmark(MinecraftClientData.getDashboardInstance()), false));

		doneEditingStationButton.setText(Component.translatable("gui.done").getString());
		doneEditingStationButton.onClick(() -> stopEditingAndSelectTab(currentTab));
		doneEditingRouteButton.setText(Component.translatable("gui.done").getString());
		doneEditingRouteButton.onClick(() -> stopEditingAndSelectTab(currentTab));
		doneEditingDepotButton.setText(Component.translatable("gui.done").getString());
		doneEditingDepotButton.onClick(() -> stopEditingAndSelectTab(currentTab));
		doneEditingRouteDestinationButton.setText(Component.translatable("gui.done").getString());
		doneEditingRouteDestinationButton.onClick(this::onDoneEditingRouteDestination);
		doneEditingHomeButton.setText(Component.translatable("gui.done").getString());
		doneEditingHomeButton.onClick(() -> stopEditingAndSelectTab(currentTab));
		doneEditingLandmarkButton.setText(Component.translatable("gui.done").getString());
		doneEditingLandmarkButton.onClick(() -> stopEditingAndSelectTab(currentTab));

		stationNameTextInput.onChange(() -> {
			if (editingArea != null) {
				editingArea.setName(IGui.textOrUntitled(stationNameTextInput.getText()));
			}
		});
		routeNameTextInput.onChange(() -> {
			if (editingRoute != null) {
				editingRoute.setName(IGui.textOrUntitled(routeNameTextInput.getText()));
			}
		});
		depotNameTextInput.onChange(() -> {
			if (editingArea != null) {
				editingArea.setName(IGui.textOrUntitled(depotNameTextInput.getText()));
			}
		});
		homeNameTextInput.onChange(() -> {
			if (editingArea != null) {
				editingArea.setName(IGui.textOrUntitled(homeNameTextInput.getText()));
			}
		});
		landmarkNameTextInput.onChange(() -> {
			if (editingArea != null) {
				editingArea.setName(IGui.textOrUntitled(landmarkNameTextInput.getText()));
			}
		});

		final UIContainer rightContainer = (UIContainer) new UIContainer()
			.setChildOf(getWindow())
			.setX(new SiblingConstraint())
			.setWidth(new FillConstraint())
			.setHeight(new RelativeConstraint());

		final UIContainer buttonRow = (UIContainer) new UIContainer()
			.setChildOf(rightContainer)
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		mapComponent = (MapComponent) new MapComponent(this, transportMode, this::startEditingArea, this::onDeleteData)
			.setChildOf(rightContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new FillConstraint());

		final ButtonComponent transportSystemMapButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonRow)
			.setWidth(new ScaleConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(40)), 0.4F));

		transportSystemMapButton.setText(TranslationProvider.GUI_MTR_TRANSPORT_SYSTEM_MAP.getString());
		transportSystemMapButton.onClick(() -> Util.getPlatform().openUri(String.format("http://localhost:%s", MTRClient.getServerPort())));

		final ButtonComponent resourcePackCreatorButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonRow)
			.setX(new SiblingConstraint())
			.setWidth(new ScaleConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(40)), 0.4F));

		resourcePackCreatorButton.setText(TranslationProvider.GUI_MTR_RESOURCE_PACK_CREATOR.getString());
		resourcePackCreatorButton.onClick(() -> Util.getPlatform().openUri(String.format("http://localhost:%s/creator/", MTRClient.getServerPort())));

		final ButtonComponent optionsButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonRow)
			.setX(new SiblingConstraint())
			.setWidth(new ScaleConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(40)), 0.2F));

		optionsButton.setText(TranslationProvider.GUI_MTR_MTR_OPTIONS.getString());
		optionsButton.onClick(() -> UMinecraft.setCurrentScreenObj(new ConfigScreen(this)));

		final ButtonComponent zoomInButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonRow)
			.setX(new SiblingConstraint())
			.setWidth(new PixelConstraint(20));

		zoomInButton.setText("+");
		zoomInButton.onClick(() -> mapComponent.scale(1));

		final ButtonComponent zoomOutButton = (ButtonComponent) new ButtonComponent(false)
			.setChildOf(buttonRow)
			.setX(new SiblingConstraint())
			.setWidth(new PixelConstraint(20));

		zoomOutButton.setText("-");
		zoomOutButton.onClick(() -> mapComponent.scale(-1));

		stopEditingAndSelectTab(currentTab);
	}

	@Override
	public void onTick() {
		super.onTick();

		switch (currentTab) {
			case STATIONS -> {
				if (editingArea == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<Station>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, (indexList, station) -> mapComponent.find(station)));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, station) -> startEditingArea(station)));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, station) -> UMinecraft.setCurrentScreenObj(new StationScreen(station, this))));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, station) -> onDeleteData(station, new DeleteDataRequest().addStationId(station.getId()))));
					}
					ListComponent.setAreas(stationsListComponent, MinecraftClientData.getDashboardInstance().stations, null, actions);
				} else {
					ListComponent.setSavedRails(stationPlatformsListComponent, MinecraftClientData.getDashboardInstance().platforms.stream().filter(platform -> editingArea.inArea(platform.getMidPosition())).collect(Collectors.toCollection(ObjectArraySet::new)), new ObjectArrayList<>());
				}
			}
			case ROUTES -> {
				if (editingRoute == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<Route>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, route) -> UMinecraft.setCurrentScreenObj(new RouteScreen(route, this))));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, route) -> startEditingRoute(route)));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, route) -> onDeleteData(route, new DeleteDataRequest().addRouteId(route.getId()))));
					}
					ListComponent.setRoutes(routesListComponent, MinecraftClientData.getDashboardInstance().routes, transportMode, false, actions);
				} else {
					final ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<RoutePlatformData>>> actions = hasPermission ? ObjectArrayList.of(
						new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, routePlatformData) -> startEditingRouteDestination(indexList.getFirst())),
						ListComponent.createUpButton(editingRoute.getRoutePlatforms(), null),
						ListComponent.createDownButton(editingRoute.getRoutePlatforms(), null),
						new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, routePlatformData) -> Utilities.removeElement(editingRoute.getRoutePlatforms(), indexList.getFirst()))
					) : new ObjectArrayList<>();
					ListComponent.setRoutePlatforms(routePlatformsListComponent, editingRoute.getRoutePlatforms(), actions);
				}
			}
			case DEPOTS -> {
				if (editingArea == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<Depot>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, (indexList, depot) -> mapComponent.find(depot)));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, depot) -> startEditingArea(depot)));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, depot) -> UMinecraft.setCurrentScreenObj(new DepotScreen(depot, this))));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, depot) -> onDeleteData(depot, new DeleteDataRequest().addDepotId(depot.getId()))));
					}
					ListComponent.setAreas(depotsListComponent, MinecraftClientData.getDashboardInstance().depots, transportMode, actions);
				} else {
					ListComponent.setSavedRails(depotSidingsListComponent, MinecraftClientData.getDashboardInstance().sidings.stream().filter(siding -> editingArea.inArea(siding.getMidPosition())).collect(Collectors.toCollection(ObjectArraySet::new)), new ObjectArrayList<>());
				}
			}
			case HOMES -> {
				if (editingArea == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<Home>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, (indexList, home) -> mapComponent.find(home)));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, home) -> startEditingArea(home)));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, home) -> UMinecraft.setCurrentScreenObj(new HomeScreen(home, this))));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, home) -> onDeleteData(home, new DeleteDataRequest().addHomeId(home.getId()))));
					}
					ListComponent.setAreas(homesListComponent, MinecraftClientData.getDashboardInstance().homes, transportMode, actions);
				}
			}
			case LANDMARKS -> {
				if (editingArea == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<ResourceLocation, ListItem.ActionConsumer<Landmark>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, (indexList, landmark) -> mapComponent.find(landmark)));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, (indexList, landmark) -> startEditingArea(landmark)));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, (indexList, landmark) -> UMinecraft.setCurrentScreenObj(new LandmarkScreen(landmark, this))));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, (indexList, landmark) -> onDeleteData(landmark, new DeleteDataRequest().addLandmarkId(landmark.getId()))));
					}
					ListComponent.setAreas(landmarksListComponent, MinecraftClientData.getDashboardInstance().landmarks, transportMode, actions);
				}
			}
		}
	}

	private <T extends NameColorDataBase> void onDeleteData(T data, DeleteDataRequest deleteDataRequest) {
		UMinecraft.setCurrentScreenObj(new DeleteConfirmationScreen(IGui.formatStationName(data.getName()), () -> RegistryClient.sendPacketToServer(new PacketDeleteData(deleteDataRequest)), this));
	}

	private void startEditingDataNew(NameColorDataBase data, boolean fullHeight) {
		data.setName(TranslationProvider.GUI_MTR_UNTITLED.getString());
		data.setColor(new Random().nextInt());

		if (data instanceof SimpleAreaBase area) {
			final Minecraft minecraft = Minecraft.getInstance();
			final LocalPlayer clientPlayer = minecraft.player;
			if (clientPlayer != null) {
				final BlockPos blockPos = clientPlayer.blockPosition();
				final Position position1;
				final Position position2;
				if (fullHeight) {
					position1 = new Position(blockPos.getX(), Long.MIN_VALUE, blockPos.getZ());
					position2 = new Position(blockPos.getX(), Long.MAX_VALUE, blockPos.getZ());
				} else {
					position1 = MTR.blockPosToPosition(blockPos);
					position2 = position1;
				}
				area.setCorners(position1, position2);
			}
		}

		switch (data) {
			case Station station -> startEditingArea(station);
			case Route route -> startEditingRoute(route);
			case Depot depot -> startEditingArea(depot);
			case Home home -> startEditingArea(home);
			case Landmark landmark -> startEditingArea(landmark);
			default -> {
			}
		}
	}

	private void startEditingArea(SimpleAreaBase editingArea) {
		switch (editingArea) {
			case Station ignored -> {
				stopEditingAndSelectTab(Tab.STATIONS);
				stationsTabContainer.hide(true);
				stationPlatformsTabContainer.unhide(true);
				stationNameTextInput.setText(editingArea.getName());
			}
			case Depot ignored -> {
				stopEditingAndSelectTab(Tab.DEPOTS);
				depotsTabContainer.hide(true);
				depotSidingsTabContainer.unhide(true);
				depotNameTextInput.setText(editingArea.getName());
			}
			case Home ignored -> {
				stopEditingAndSelectTab(Tab.HOMES);
				homesTabContainer.hide(true);
				homeTabContainer.unhide(true);
				homeNameTextInput.setText(editingArea.getName());
			}
			case Landmark ignored -> {
				stopEditingAndSelectTab(Tab.LANDMARKS);
				landmarksTabContainer.hide(true);
				landmarkTabContainer.unhide(true);
				landmarkNameTextInput.setText(editingArea.getName());
			}
			default -> {
			}
		}

		setEditingData(editingArea, null);
		mapComponent.startEditingArea(editingArea);
	}

	private void startEditingRoute(Route editingRoute) {
		stopEditingAndSelectTab(Tab.ROUTES);
		setEditingData(null, editingRoute);

		routeNameTextInput.setText(editingRoute.getName());
		mapComponent.startEditingRoute(editingRoute);

		routesTabContainer.hide(true);
		routePlatformsTabContainer.unhide(true);
		routeDestinationTabContainer.hide(true);
	}

	private void startEditingRouteDestination(int index) {
		if (editingRoute != null) {
			editingRoutePlatformIndex = index;
			if (isValidRoutePlatformIndex()) {
				routeDestinationTextInput.setText(editingRoute.getRoutePlatforms().get(index).getCustomDestination());
			}
			routesTabContainer.hide(true);
			routePlatformsTabContainer.hide(true);
			routeDestinationTabContainer.unhide(true);
		}
	}

	private void onDoneEditingRouteDestination() {
		if (editingRoute != null) {
			if (isValidRoutePlatformIndex()) {
				editingRoute.getRoutePlatforms().get(editingRoutePlatformIndex).setCustomDestination(routeDestinationTextInput.getText());
			}
			startEditingRoute(editingRoute);
			routesTabContainer.hide(true);
			routePlatformsTabContainer.unhide(true);
			routeDestinationTabContainer.hide(true);
		}
	}

	private void setEditingData(@Nullable SimpleAreaBase editingArea, @Nullable Route editingRoute) {
		if (this.editingArea != null) {
			switch (this.editingArea) {
				case Station station -> RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation(station)));
				case Depot depot -> RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot(depot)));
				case Home home -> RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addHome(home)));
				case Landmark landmark -> RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addLandmark(landmark)));
				default -> {
				}
			}
		}

		if (this.editingRoute != null) {
			RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addRoute(this.editingRoute)));
		}

		this.editingArea = editingArea;
		this.editingRoute = editingRoute;
		editingRoutePlatformIndex = -1;
	}

	private boolean isValidRoutePlatformIndex() {
		return editingRoute != null && editingRoutePlatformIndex >= 0 && editingRoutePlatformIndex < editingRoute.getRoutePlatforms().size();
	}

	private void stopEditingAndSelectTab(Tab currentTab) {
		stationsTabButton.setDisabled(currentTab == Tab.STATIONS);
		routesTabButton.setDisabled(currentTab == Tab.ROUTES);
		depotsTabButton.setDisabled(currentTab == Tab.DEPOTS);
		homesTabButton.setDisabled(currentTab == Tab.HOMES);
		landmarksTabButton.setDisabled(currentTab == Tab.LANDMARKS);
		setEditingData(null, null);
		mapComponent.stopEditing();
		mapComponent.setShowStations(currentTab != Tab.DEPOTS);

		switch (currentTab) {
			case STATIONS -> {
				stationsTabContainer.unhide(true);
				stationPlatformsTabContainer.hide(true);
				routesTabContainer.hide(true);
				routePlatformsTabContainer.hide(true);
				routeDestinationTabContainer.hide(true);
				depotsTabContainer.hide(true);
				depotSidingsTabContainer.hide(true);
				homesTabContainer.hide(true);
				homeTabContainer.hide(true);
				landmarksTabContainer.hide(true);
				landmarkTabContainer.hide(true);
			}
			case ROUTES -> {
				stationsTabContainer.hide(true);
				stationPlatformsTabContainer.hide(true);
				routesTabContainer.unhide(true);
				routePlatformsTabContainer.hide(true);
				routeDestinationTabContainer.hide(true);
				depotsTabContainer.hide(true);
				depotSidingsTabContainer.hide(true);
				homesTabContainer.hide(true);
				homeTabContainer.hide(true);
				landmarksTabContainer.hide(true);
				landmarkTabContainer.hide(true);
			}
			case DEPOTS -> {
				stationsTabContainer.hide(true);
				stationPlatformsTabContainer.hide(true);
				routesTabContainer.hide(true);
				routePlatformsTabContainer.hide(true);
				routeDestinationTabContainer.hide(true);
				depotsTabContainer.unhide(true);
				depotSidingsTabContainer.hide(true);
				homesTabContainer.hide(true);
				homeTabContainer.hide(true);
				landmarksTabContainer.hide(true);
				landmarkTabContainer.hide(true);
			}
			case HOMES -> {
				stationsTabContainer.hide(true);
				stationPlatformsTabContainer.hide(true);
				routesTabContainer.hide(true);
				routePlatformsTabContainer.hide(true);
				routeDestinationTabContainer.hide(true);
				depotsTabContainer.hide(true);
				depotSidingsTabContainer.hide(true);
				homesTabContainer.unhide(true);
				homeTabContainer.hide(true);
				landmarksTabContainer.hide(true);
				landmarkTabContainer.hide(true);
			}
			case LANDMARKS -> {
				stationsTabContainer.hide(true);
				stationPlatformsTabContainer.hide(true);
				routesTabContainer.hide(true);
				routePlatformsTabContainer.hide(true);
				routeDestinationTabContainer.hide(true);
				depotsTabContainer.hide(true);
				depotSidingsTabContainer.hide(true);
				homesTabContainer.hide(true);
				homeTabContainer.hide(true);
				landmarksTabContainer.unhide(true);
				landmarkTabContainer.hide(true);
			}
		}

		this.currentTab = currentTab;
	}

	private static <T extends SerializedDataBase> UIContainer createTab(UIContainer parentContainer, boolean useSearch, @Nullable ListComponent<T> listComponent, @Nullable String title, @Nullable String textInputSuggestion, @Nullable TextInputComponent textInput, @Nullable ButtonComponent button) {
		final UIContainer tabContainer = (UIContainer) new UIContainer()
			.setChildOf(parentContainer)
			.setWidth(new RelativeConstraint())
			.setHeight(new RelativeConstraint());

		final UIBlock listContainer = (UIBlock) new UIBlock(new Color(GuiHelper.BACKGROUND_COLOR))
			.setChildOf(tabContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new FillConstraint(true));

		if (title != null) {
			final UIContainer titleContainer = (UIContainer) new UIContainer()
				.setChildOf(listContainer)
				.setWidth(new RelativeConstraint())
				.setHeight(new AdditiveConstraint(new ChildBasedSizeConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)));

			new UIWrappedText(title)
				.setChildOf(titleContainer)
				.setX(new PixelConstraint(GuiHelper.DEFAULT_PADDING))
				.setY(new PixelConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)));
		}

		if (listComponent != null) {
			if (useSearch) {
				final TextInputComponent filterTextInput = (TextInputComponent) new TextInputComponent()
					.setChildOf(listContainer)
					.setY(new SiblingConstraint())
					.setWidth(new RelativeConstraint())
					.setHeight(new PixelConstraint(20));

				filterTextInput.setPlaceholderText(TranslationProvider.GUI_MTR_SEARCH.getString());
				filterTextInput.onChange(() -> listComponent.setFilter(filterTextInput.getText()));
			}

			final ScrollPanelComponent scrollPanelComponent = (ScrollPanelComponent) new ScrollPanelComponent(true)
				.setChildOf(listContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new FillConstraint());

			scrollPanelComponent.setScrollbarColor(Color.WHITE);

			listComponent.setChildOf(scrollPanelComponent.contentContainer)
				.setWidth(new RelativeConstraint())
				.setHeight(new RelativeConstraint());
		}

		if (textInput != null) {
			textInput.setChildOf(tabContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint())
				.setHeight(new PixelConstraint(20));

			if (textInputSuggestion != null) {
				textInput.setPlaceholderText(textInputSuggestion);
			}
		}

		if (button != null) {
			button.setChildOf(tabContainer)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());
		}

		return tabContainer;
	}

	private enum Tab {STATIONS, ROUTES, DEPOTS, HOMES, LANDMARKS}
}
