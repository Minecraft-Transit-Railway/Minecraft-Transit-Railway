package org.mtr.screen;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.ColorHelper;
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
import org.mtr.tool.Drawing;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public final class DashboardScreen extends ScreenBase {

	@Nullable
	private AreaBase<?, ?> editingArea;
	@Nullable
	private Route editingRoute;
	private int editingRoutePlatformIndex = -1;

	@Nullable
	private ColorSelectorWidget colorSelector;
	@Nullable
	private DeleteConfirmationWidget deleteConfirmationWidget;

	private final TransportMode transportMode;
	private final boolean hasPermission = MinecraftClientData.hasPermission();
	private final MapWidget mapWidget;
	private final TabGroupWidget tabGroupWidget;

	private final ScrollableListWidget<Station> stationsListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Platform> stationPlatformsListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Route> routesListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<ObjectIntImmutablePair<RoutePlatformData>> routePlatformsListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Depot> depotsListWidget = new ScrollableListWidget<>();
	private final ScrollableListWidget<Siding> depotSidingsListWidget = new ScrollableListWidget<>();

	private final BetterTextFieldWidget stationsFilterTextField;
	private final BetterTextFieldWidget routesFilterTextField;
	private final BetterTextFieldWidget depotsFilterTextField;

	private final BetterButtonWidget expandAllButton = new BetterButtonWidget(GuiHelper.EXPAND_ALL_TEXTURE_ID, null, 0, routesListWidget::toggleExpansion);
	private final BetterButtonWidget collapseAllButton = new BetterButtonWidget(GuiHelper.COLLAPSE_ALL_TEXTURE_ID, null, 0, routesListWidget::toggleExpansion);
	private final BetterButtonWidget openColorSelectorButton = new BetterButtonWidget(GuiHelper.COLOR_TEXTURE_ID, null, 0, () -> {
		onOpenColorSelector(editingArea);
		onOpenColorSelector(editingRoute);
	});

	private final BetterButtonWidget addStationButton;
	private final BetterButtonWidget addRouteButton;
	private final BetterButtonWidget addDepotButton;

	private final BetterButtonWidget doneEditingStationButton;
	private final BetterButtonWidget doneEditingRouteButton;
	private final BetterButtonWidget doneEditingDepotButton;
	private final BetterButtonWidget doneEditingRouteDestinationButton;

	private final BetterTextFieldWidget stationNameTextField;
	private final BetterTextFieldWidget routeNameTextField;
	private final BetterTextFieldWidget depotNameTextField;
	private final BetterTextFieldWidget routeDestinationTextField;

	private final BetterButtonWidget transportSystemMapButton = new BetterButtonWidget(GuiHelper.MAP_TEXTURE_ID, null, GuiHelper.DEFAULT_LINE_SIZE, () -> Util.getOperatingSystem().open(String.format("http://localhost:%s", MTRClient.getServerPort())));
	private final BetterButtonWidget resourcePackCreatorButton = new BetterButtonWidget(GuiHelper.EDITOR_TEXTURE_ID, null, GuiHelper.DEFAULT_LINE_SIZE, () -> Util.getOperatingSystem().open(String.format("http://localhost:%s/creator/", MTRClient.getServerPort())));
	private final BetterButtonWidget optionsButton = new BetterButtonWidget(GuiHelper.SETTINGS_TEXTURE_ID, null, GuiHelper.DEFAULT_LINE_SIZE, () -> MinecraftClient.getInstance().setScreen(new ConfigScreen(this)));
	private final BetterButtonWidget zoomInButton;
	private final BetterButtonWidget zoomOutButton;

	private final ObjectImmutableList<OrderedText> editAreaText;
	private final ObjectImmutableList<OrderedText> editRouteText;

	private static String STATIONS_SEARCH_TEXT = "";
	private static String ROUTES_SEARCH_TEXT = "";
	private static String DEPOTS_SEARCH_TEXT = "";

	public static final int MAX_COLOR_ZONE_LENGTH = 6;
	private static final int PANEL_WIDTH = 144;

	public DashboardScreen(TransportMode transportMode) {
		this.transportMode = transportMode;

		mapWidget = new MapWidget(this, transportMode, this::startEditingArea, this::onDeleteData);

		tabGroupWidget = new TabGroupWidget(PANEL_WIDTH, index -> {
			stopEditing();
			mapWidget.setShowStations(index < 2);
		}, TranslationProvider.GUI_MTR_STATIONS.getString(), TranslationProvider.GUI_MTR_ROUTES.getString(), TranslationProvider.GUI_MTR_DEPOTS.getString());

		stationsFilterTextField = new BetterTextFieldWidget(STATIONS_SEARCH_TEXT, 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2, text -> {
			stationsListWidget.setFilter(text);
			STATIONS_SEARCH_TEXT = text;
		});
		routesFilterTextField = new BetterTextFieldWidget(ROUTES_SEARCH_TEXT, 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 3 - GuiHelper.DEFAULT_LINE_SIZE, text -> {
			routesListWidget.setFilter(text);
			ROUTES_SEARCH_TEXT = text;
		});
		depotsFilterTextField = new BetterTextFieldWidget(DEPOTS_SEARCH_TEXT, 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_SEARCH.getString(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2, text -> {
			depotsListWidget.setFilter(text);
			DEPOTS_SEARCH_TEXT = text;
		});

		addStationButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_STATION.getString(), tabGroupWidget.getWidth(), () -> startEditingDataNew(new Station(MinecraftClientData.getDashboardInstance())));
		addRouteButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_ROUTE.getString(), tabGroupWidget.getWidth(), () -> startEditingDataNew(new Route(transportMode, MinecraftClientData.getDashboardInstance())));
		addDepotButton = new BetterButtonWidget(GuiHelper.ADD_TEXTURE_ID, TranslationProvider.GUI_MTR_ADD_DEPOT.getString(), tabGroupWidget.getWidth(), () -> startEditingDataNew(new Depot(transportMode, MinecraftClientData.getDashboardInstance())));

		doneEditingStationButton = new BetterButtonWidget(GuiHelper.CHECK_TEXTURE_ID, Text.translatable("gui.done").getString(), tabGroupWidget.getWidth(), this::stopEditing);
		doneEditingRouteButton = new BetterButtonWidget(GuiHelper.CHECK_TEXTURE_ID, Text.translatable("gui.done").getString(), tabGroupWidget.getWidth(), this::stopEditing);
		doneEditingDepotButton = new BetterButtonWidget(GuiHelper.CHECK_TEXTURE_ID, Text.translatable("gui.done").getString(), tabGroupWidget.getWidth(), this::stopEditing);
		doneEditingRouteDestinationButton = new BetterButtonWidget(GuiHelper.CHECK_TEXTURE_ID, Text.translatable("gui.done").getString(), tabGroupWidget.getWidth(), this::onDoneEditingRouteDestination);

		stationNameTextField = new BetterTextFieldWidget("", 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_STATION_NAME.getString(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 3 - GuiHelper.DEFAULT_LINE_SIZE, text -> {
			if (editingArea != null) {
				editingArea.setName(IGui.textOrUntitled(text));
			}
		});
		routeNameTextField = new BetterTextFieldWidget("", 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_ROUTE_NAME.getString(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 3 - GuiHelper.DEFAULT_LINE_SIZE, text -> {
			if (editingRoute != null) {
				editingRoute.setName(IGui.textOrUntitled(text));
			}
		});
		depotNameTextField = new BetterTextFieldWidget("", 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_DEPOT_NAME.getString(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 3 - GuiHelper.DEFAULT_LINE_SIZE, text -> {
			if (editingArea != null) {
				editingArea.setName(IGui.textOrUntitled(text));
			}
		});
		routeDestinationTextField = new BetterTextFieldWidget("", 256, TextCase.DEFAULT, null, TranslationProvider.GUI_MTR_CUSTOM_DESTINATION_SUGGESTION.getString(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2, text -> {
		});

		zoomInButton = new BetterButtonWidget(GuiHelper.ZOOM_IN_TEXTURE_ID, null, GuiHelper.DEFAULT_LINE_SIZE, () -> mapWidget.scale(1));
		zoomOutButton = new BetterButtonWidget(GuiHelper.ZOOM_OUT_TEXTURE_ID, null, GuiHelper.DEFAULT_LINE_SIZE, () -> mapWidget.scale(-1));

		tabGroupWidget.selectTab(0);
		textRenderer = MinecraftClient.getInstance().textRenderer;
		editAreaText = new ObjectImmutableList<>(textRenderer.wrapLines(TranslationProvider.GUI_MTR_EDIT_AREA.getText(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2));
		editRouteText = new ObjectImmutableList<>(textRenderer.wrapLines(TranslationProvider.GUI_MTR_EDIT_ROUTE.getText(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2));
	}

	@SuppressWarnings("DuplicatedCode")
	@Override
	protected void init() {
		super.init();

		mapWidget.setPosition(tabGroupWidget.getWidth(), GuiHelper.DEFAULT_LINE_SIZE);
		mapWidget.setDimensions(width - tabGroupWidget.getWidth(), height - GuiHelper.DEFAULT_LINE_SIZE);

		final int listY1 = GuiHelper.DEFAULT_LINE_SIZE * 2 + GuiHelper.DEFAULT_PADDING * 2;
		final int listY2 = GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING * 2 + (editAreaText.size() - 1) * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT + GuiHelper.MINECRAFT_FONT_SIZE;
		final int listY3 = GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING * 2 + (editRouteText.size() - 1) * GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT + GuiHelper.MINECRAFT_FONT_SIZE;
		final int listHeight1 = height - listY1 - (hasPermission ? GuiHelper.DEFAULT_LINE_SIZE : 0);
		final int listHeight2 = height - listY2 - GuiHelper.DEFAULT_LINE_SIZE * 2 - GuiHelper.DEFAULT_PADDING * 2;
		final int listHeight3 = height - listY3 - GuiHelper.DEFAULT_LINE_SIZE * 2 - GuiHelper.DEFAULT_PADDING * 2;
		stationsListWidget.setY(listY1);
		stationsListWidget.setBounds(tabGroupWidget.getWidth(), listHeight1, tabGroupWidget.getWidth(), listHeight1);
		stationPlatformsListWidget.setY(listY2);
		stationPlatformsListWidget.setBounds(tabGroupWidget.getWidth(), listHeight2, tabGroupWidget.getWidth(), listHeight2);
		routesListWidget.setY(listY1);
		routesListWidget.setBounds(tabGroupWidget.getWidth(), listHeight1, tabGroupWidget.getWidth(), listHeight1);
		routePlatformsListWidget.setY(listY3);
		routePlatformsListWidget.setBounds(tabGroupWidget.getWidth(), listHeight3, tabGroupWidget.getWidth(), listHeight3);
		depotsListWidget.setY(listY1);
		depotsListWidget.setBounds(tabGroupWidget.getWidth(), listHeight1, tabGroupWidget.getWidth(), listHeight1);
		depotSidingsListWidget.setY(listY2);
		depotSidingsListWidget.setBounds(tabGroupWidget.getWidth(), listHeight2, tabGroupWidget.getWidth(), listHeight2);

		stationsFilterTextField.setPosition(GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		routesFilterTextField.setPosition(GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		depotsFilterTextField.setPosition(GuiHelper.DEFAULT_PADDING, GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);

		expandAllButton.setPosition(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING - GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		collapseAllButton.setPosition(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING - GuiHelper.DEFAULT_LINE_SIZE, GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING);
		openColorSelectorButton.setPosition(tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING - GuiHelper.DEFAULT_LINE_SIZE, height - GuiHelper.DEFAULT_LINE_SIZE * 2 - GuiHelper.DEFAULT_PADDING);

		addStationButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);
		addRouteButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);
		addDepotButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);

		doneEditingStationButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);
		doneEditingRouteButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);
		doneEditingDepotButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);
		doneEditingRouteDestinationButton.setY(height - GuiHelper.DEFAULT_LINE_SIZE);

		stationNameTextField.setPosition(GuiHelper.DEFAULT_PADDING, height - GuiHelper.DEFAULT_LINE_SIZE * 2 - GuiHelper.DEFAULT_PADDING);
		routeNameTextField.setPosition(GuiHelper.DEFAULT_PADDING, height - GuiHelper.DEFAULT_LINE_SIZE * 2 - GuiHelper.DEFAULT_PADDING);
		depotNameTextField.setPosition(GuiHelper.DEFAULT_PADDING, height - GuiHelper.DEFAULT_LINE_SIZE * 2 - GuiHelper.DEFAULT_PADDING);
		routeDestinationTextField.setPosition(GuiHelper.DEFAULT_PADDING, height - GuiHelper.DEFAULT_LINE_SIZE * 2 - GuiHelper.DEFAULT_PADDING);

		transportSystemMapButton.setX(width - GuiHelper.DEFAULT_LINE_SIZE * 5 - 1);
		resourcePackCreatorButton.setX(width - GuiHelper.DEFAULT_LINE_SIZE * 4 - 1);
		optionsButton.setX(width - GuiHelper.DEFAULT_LINE_SIZE * 3 - 1);
		zoomInButton.setX(width - GuiHelper.DEFAULT_LINE_SIZE * 2);
		zoomOutButton.setX(width - GuiHelper.DEFAULT_LINE_SIZE);

		addDrawableChild(mapWidget);
		addDrawableChild(tabGroupWidget);

		addDrawableChild(stationsListWidget);
		addDrawableChild(stationPlatformsListWidget);
		addDrawableChild(routesListWidget);
		addDrawableChild(routePlatformsListWidget);
		addDrawableChild(depotsListWidget);
		addDrawableChild(depotSidingsListWidget);

		addDrawableChild(stationsFilterTextField);
		addDrawableChild(routesFilterTextField);
		addDrawableChild(depotsFilterTextField);

		addDrawableChild(expandAllButton);
		addDrawableChild(collapseAllButton);
		addDrawableChild(openColorSelectorButton);

		addDrawableChild(addStationButton);
		addDrawableChild(addRouteButton);
		addDrawableChild(addDepotButton);

		addDrawableChild(doneEditingStationButton);
		addDrawableChild(doneEditingRouteButton);
		addDrawableChild(doneEditingDepotButton);
		addDrawableChild(doneEditingRouteDestinationButton);

		addDrawableChild(stationNameTextField);
		addDrawableChild(routeNameTextField);
		addDrawableChild(depotNameTextField);
		addDrawableChild(routeDestinationTextField);

		addDrawableChild(transportSystemMapButton);
		addDrawableChild(resourcePackCreatorButton);
		addDrawableChild(optionsButton);
		addDrawableChild(zoomInButton);
		addDrawableChild(zoomOutButton);

		colorSelector = new ColorSelectorWidget(width / 2, () -> enableControls(true), this::applyBlur);
		colorSelector.setPosition(width / 4, height * 2);
		addDrawableChild(colorSelector);

		deleteConfirmationWidget = new DeleteConfirmationWidget(width / 2, () -> enableControls(true), this::applyBlur);
		deleteConfirmationWidget.setPosition(width / 4, height * 2);
		addDrawableChild(deleteConfirmationWidget);
	}

	@Override
	public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
		context.fill(0, 0, width, height, GuiHelper.BACKGROUND_COLOR);
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		// Draw menu separator lines
		final Drawing drawing = new Drawing(context.getMatrices(), MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers().getBuffer(RenderLayer.getGui()));
		drawing.setVerticesWH(width - GuiHelper.DEFAULT_LINE_SIZE * 5 - 2, 0, 1, GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();
		drawing.setVerticesWH(width - GuiHelper.DEFAULT_LINE_SIZE * 2 - 1, 0, 1, GuiHelper.DEFAULT_LINE_SIZE).setColor(GuiHelper.BACKGROUND_ACCENT_COLOR).draw();

		// Draw help text
		final int[] textY = {GuiHelper.DEFAULT_LINE_SIZE + GuiHelper.DEFAULT_PADDING};
		if (editingArea != null || editingRoute != null) {
			textRenderer.wrapLines(editingRoute == null ? TranslationProvider.GUI_MTR_EDIT_AREA.getText() : TranslationProvider.GUI_MTR_EDIT_ROUTE.getText(), tabGroupWidget.getWidth() - GuiHelper.DEFAULT_PADDING * 2).forEach(text -> {
				context.drawText(textRenderer, text, GuiHelper.DEFAULT_PADDING, textY[0], GuiHelper.WHITE_COLOR, false);
				textY[0] += GuiHelper.MINECRAFT_TEXT_LINE_HEIGHT;
			});
		}
	}

	@Override
	public void tick() {
		final int selectedIndex = tabGroupWidget.getSelectedIndex();

		stationsListWidget.visible = selectedIndex == 0 && editingArea == null;
		stationPlatformsListWidget.visible = selectedIndex == 0 && editingArea != null;
		routesListWidget.visible = selectedIndex == 1 && editingRoute == null;
		routePlatformsListWidget.visible = selectedIndex == 1 && editingRoute != null;
		depotsListWidget.visible = selectedIndex == 2 && editingArea == null;
		depotSidingsListWidget.visible = selectedIndex == 2 && editingArea != null;

		stationsFilterTextField.visible = selectedIndex == 0 && editingArea == null;
		routesFilterTextField.visible = selectedIndex == 1 && editingRoute == null;
		depotsFilterTextField.visible = selectedIndex == 2 && editingArea == null;

		if (selectedIndex == 1 && editingRoute == null) {
			final boolean canCollapse = routesListWidget.canCollapse();
			expandAllButton.visible = !canCollapse;
			collapseAllButton.visible = canCollapse;
		} else {
			expandAllButton.visible = false;
			collapseAllButton.visible = false;
		}

		addStationButton.visible = selectedIndex == 0 && editingArea == null && hasPermission;
		addRouteButton.visible = selectedIndex == 1 && editingRoute == null && hasPermission;
		addDepotButton.visible = selectedIndex == 2 && editingArea == null && hasPermission;

		doneEditingStationButton.visible = stationNameTextField.visible = selectedIndex == 0 && editingArea != null && hasPermission;
		doneEditingRouteButton.visible = routeNameTextField.visible = selectedIndex == 1 && editingRoute != null && editingRoutePlatformIndex < 0 && hasPermission;
		doneEditingDepotButton.visible = depotNameTextField.visible = selectedIndex == 2 && editingArea != null && hasPermission;
		doneEditingRouteDestinationButton.visible = routeDestinationTextField.visible = selectedIndex == 1 && editingRoute != null && editingRoutePlatformIndex >= 0 && hasPermission;

		openColorSelectorButton.visible = stationNameTextField.visible || routeNameTextField.visible || depotNameTextField.visible;

		switch (selectedIndex) {
			case 0 -> {
				if (editingArea == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<Station>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, mapWidget::find));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, this::startEditingArea));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, station -> System.out.println("editing " + station.getName())));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, station -> onDeleteData(station, new DeleteDataRequest().addStationId(station.getId()))));
					}
					ScrollableListWidget.setAreas(stationsListWidget, MinecraftClientData.getDashboardInstance().stations, null, actions);
				} else {
					ScrollableListWidget.setSavedRails(stationPlatformsListWidget, MinecraftClientData.getDashboardInstance().platforms.stream().filter(platform -> editingArea.inArea(platform.getMidPosition())).collect(Collectors.toCollection(ObjectArraySet::new)), new ObjectArrayList<>());
				}
			}
			case 1 -> {
				if (editingRoute == null) {
					final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<Route>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, route -> System.out.println("editing " + route.getName())));
					if (hasPermission) {
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, this::startEditingRoute));
						actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, route -> onDeleteData(route, new DeleteDataRequest().addRouteId(route.getId()))));
					}
					ScrollableListWidget.setRoutes(routesListWidget, MinecraftClientData.getDashboardInstance().routes, transportMode, actions);
				} else {
					ScrollableListWidget.setRoutePlatforms(routePlatformsListWidget, editingRoute.getRoutePlatforms(), hasPermission ? ObjectArrayList.of(
							new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, routePlatformDataWithIndex -> startEditingRouteDestination(routePlatformDataWithIndex.rightInt())),
							new ObjectObjectImmutablePair<>(GuiHelper.UP_TEXTURE_ID, routePlatformDataWithIndex -> moveRoutePlatform(routePlatformDataWithIndex.rightInt(), -1)),
							new ObjectObjectImmutablePair<>(GuiHelper.DOWN_TEXTURE_ID, routePlatformDataWithIndex -> moveRoutePlatform(routePlatformDataWithIndex.rightInt(), 1)),
							new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, routePlatformDataWithIndex -> onDeletePlatformFromRoute(routePlatformDataWithIndex.rightInt()))
					) : new ObjectArrayList<>());
				}
			}
			case 2 -> {
				final ObjectArrayList<ObjectObjectImmutablePair<Identifier, Consumer<Depot>>> actions = ObjectArrayList.of(new ObjectObjectImmutablePair<>(GuiHelper.FIND_TEXTURE_ID, mapWidget::find));
				if (hasPermission) {
					actions.add(new ObjectObjectImmutablePair<>(GuiHelper.SELECT_TEXTURE_ID, this::startEditingArea));
					actions.add(new ObjectObjectImmutablePair<>(GuiHelper.EDIT_TEXTURE_ID, depot -> System.out.println("editing " + depot.getName())));
					actions.add(new ObjectObjectImmutablePair<>(GuiHelper.DELETE_TEXTURE_ID, depot -> onDeleteData(depot, new DeleteDataRequest().addDepotId(depot.getId()))));
				}
				ScrollableListWidget.setAreas(depotsListWidget, MinecraftClientData.getDashboardInstance().depots, transportMode, actions);
			}
		}
	}

	private <T extends NameColorDataBase> void onDeleteData(T data, DeleteDataRequest deleteDataRequest) {
		if (deleteConfirmationWidget != null) {
			deleteConfirmationWidget.setDeleteCallback(() -> RegistryClient.sendPacketToServer(new PacketDeleteData(deleteDataRequest)), IGui.formatStationName(data.getName()));
			deleteConfirmationWidget.setY(height / 2 - deleteConfirmationWidget.getHeight() / 2);
			enableControls(false);
		}
	}

	private void onOpenColorSelector(@Nullable NameColorDataBase data) {
		if (colorSelector != null && data != null) {
			colorSelector.setColorCallback(color -> {
				data.setColor(color);
				openColorSelectorButton.setBackgroundColor(ColorHelper.fullAlpha(color));
			}, data.getColor());
			colorSelector.setY((height - colorSelector.getHeight()) / 2);
			enableControls(false);
		}
	}

	@SuppressWarnings("DuplicatedCode")
	private void enableControls(boolean enabled) {
		mapWidget.active = enabled;
		tabGroupWidget.active = enabled;

		stationsListWidget.active = enabled;
		stationPlatformsListWidget.active = enabled;
		routesListWidget.active = enabled;
		routePlatformsListWidget.active = enabled;
		depotsListWidget.active = enabled;
		depotSidingsListWidget.active = enabled;

		stationsFilterTextField.active = enabled;
		routesFilterTextField.active = enabled;
		depotsFilterTextField.active = enabled;

		expandAllButton.active = enabled;
		collapseAllButton.active = enabled;

		addStationButton.active = enabled;
		addRouteButton.active = enabled;
		addDepotButton.active = enabled;

		doneEditingStationButton.active = enabled;
		doneEditingRouteButton.active = enabled;
		doneEditingDepotButton.active = enabled;
		doneEditingRouteDestinationButton.active = enabled;

		stationNameTextField.active = enabled;
		routeNameTextField.active = enabled;
		depotNameTextField.active = enabled;
		routeDestinationTextField.active = enabled;

		transportSystemMapButton.active = enabled;
		resourcePackCreatorButton.active = enabled;
		optionsButton.active = enabled;
		zoomInButton.active = enabled;
		zoomOutButton.active = enabled;
	}

	private void startEditingDataNew(NameColorDataBase data) {
		data.setName(TranslationProvider.GUI_MTR_UNTITLED.getString());
		data.setColor(new Random().nextInt());

		switch (data) {
			case Station station -> startEditingArea(station);
			case Route route -> startEditingRoute(route);
			case Depot depot -> startEditingArea(depot);
			default -> {
			}
		}
	}

	private void startEditingArea(AreaBase<?, ?> editingArea) {
		setEditingData(editingArea, null);

		stationNameTextField.setText(editingArea.getName());
		depotNameTextField.setText(editingArea.getName());
		openColorSelectorButton.setBackgroundColor(ColorHelper.fullAlpha(editingArea.getColor()));

		final boolean isStation = editingArea instanceof Station;
		tabGroupWidget.selectTab(isStation ? 0 : 2);
		mapWidget.setShowStations(isStation);
		mapWidget.startEditingArea(editingArea);
	}

	private void startEditingRoute(Route editingRoute) {
		setEditingData(null, editingRoute);

		routeNameTextField.setText(editingRoute.getName());
		openColorSelectorButton.setBackgroundColor(ColorHelper.fullAlpha(editingRoute.getColor()));

		tabGroupWidget.selectTab(1);
		mapWidget.setShowStations(true);
		mapWidget.startEditingRoute(editingRoute);
	}

	private void moveRoutePlatform(int index, int direction) {
		if (editingRoute != null && (direction > 0 && index < editingRoute.getRoutePlatforms().size() - 1 || direction < 0 && index > 0)) {
			final RoutePlatformData routePlatformData = editingRoute.getRoutePlatforms().remove(index);
			if (hasShiftDown()) {
				if (direction > 0) {
					editingRoute.getRoutePlatforms().add(routePlatformData);
				} else {
					editingRoute.getRoutePlatforms().addFirst(routePlatformData);
				}
			} else {
				editingRoute.getRoutePlatforms().add(index + direction, routePlatformData);
			}
		}
	}

	private void startEditingRouteDestination(int index) {
		if (editingRoute != null) {
			editingRoutePlatformIndex = index;
			if (isValidRoutePlatformIndex()) {
				routeDestinationTextField.setText(editingRoute.getRoutePlatforms().get(index).getCustomDestination());
			}
		}
	}

	private void onDeletePlatformFromRoute(int index) {
		if (editingRoute != null && index >= 0 && index < editingRoute.getRoutePlatforms().size()) {
			editingRoute.getRoutePlatforms().remove(index);
		}
	}

	private void onDoneEditingRouteDestination() {
		if (editingRoute != null) {
			if (isValidRoutePlatformIndex()) {
				editingRoute.getRoutePlatforms().get(editingRoutePlatformIndex).setCustomDestination(routeDestinationTextField.getText());
			}
			startEditingRoute(editingRoute);
		}
	}

	private void stopEditing() {
		setEditingData(null, null);
		mapWidget.stopEditing();
	}

	private void setEditingData(@Nullable AreaBase<?, ?> editingArea, @Nullable Route editingRoute) {
		if (this.editingArea != null) {
			switch (this.editingArea) {
				case Station station -> RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addStation(station)));
				case Depot depot -> RegistryClient.sendPacketToServer(new PacketUpdateData(new UpdateDataRequest(MinecraftClientData.getDashboardInstance()).addDepot(depot)));
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
}
