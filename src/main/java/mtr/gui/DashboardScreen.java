package mtr.gui;

import mtr.data.*;
import mtr.packet.IPacket;
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

import java.util.*;
import java.util.stream.Collectors;

public class DashboardScreen extends Screen implements IGui, IPacket {

	private SelectedTab selectedTab;
	private AreaBase editingArea;
	private Route editingRoute;
	private boolean isNew;

	private final WidgetMap widgetMap;

	private final ButtonWidget buttonTabStations;
	private final ButtonWidget buttonTabRoutes;
	private final ButtonWidget buttonTabDepots;
	private final ButtonWidget buttonAddStation;
	private final ButtonWidget buttonAddRoute;
	private final ButtonWidget buttonAddDepot;
	private final ButtonWidget buttonDoneEditingStation;
	private final ButtonWidget buttonDoneEditingRoute;
	private final ButtonWidget buttonDoneEditingDepot;
	private final ButtonWidget buttonZoomIn;
	private final ButtonWidget buttonZoomOut;
	private final ButtonWidget buttonOptions;

	private final TextFieldWidget textFieldName;
	private final TextFieldWidget textFieldColor;

	private final DashboardList dashboardList;

	public static final int MAX_NAME_LENGTH = 128;
	public static final int MAX_COLOR_ZONE_LENGTH = 6;
	private static final int COLOR_WIDTH = 48;

	public DashboardScreen() {
		super(new LiteralText(""));

		widgetMap = new WidgetMap(this::onDrawCorners, this::onDrawCornersMouseRelease, this::onClickAddPlatformToRoute, this::onClickEditSavedRail);

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldName = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		textFieldColor = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		buttonTabStations = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.stations"), button -> onSelectTab(SelectedTab.STATIONS));
		buttonTabRoutes = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.routes"), button -> onSelectTab(SelectedTab.ROUTES));
		buttonTabDepots = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.depots"), button -> onSelectTab(SelectedTab.DEPOTS));

		buttonAddStation = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_station"), button -> startEditingArea(new Station(), true));
		buttonAddRoute = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_route"), button -> startEditingRoute(new Route(), true));
		buttonAddDepot = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.mtr.add_depot"), button -> startEditingArea(new Depot(), true));
		buttonDoneEditingStation = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneEditingArea(true));
		buttonDoneEditingRoute = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneEditingRoute());
		buttonDoneEditingDepot = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("gui.done"), button -> onDoneEditingArea(false));
		buttonZoomIn = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText("+"), button -> widgetMap.scale(1));
		buttonZoomOut = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new LiteralText("-"), button -> widgetMap.scale(-1));
		buttonOptions = new ButtonWidget(0, 0, 0, SQUARE_SIZE, new TranslatableText("menu.options"), button -> {
			if (client != null) {
				client.openScreen(new ConfigScreen());
			}
		});

		dashboardList = new DashboardList(this::addButton, this::addChild, this::onFind, this::onDrawArea, this::onEdit, this::onSort, null, this::onDelete, this::getList, () -> ClientData.DASHBOARD_SEARCH, text -> ClientData.DASHBOARD_SEARCH = text);

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
		IDrawing.setPositionAndWidth(buttonDoneEditingDepot, 0, bottomRowY, PANEL_WIDTH);
		IDrawing.setPositionAndWidth(buttonZoomIn, width - SQUARE_SIZE * 2, bottomRowY, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonZoomOut, width - SQUARE_SIZE, bottomRowY, SQUARE_SIZE);
		IDrawing.setPositionAndWidth(buttonOptions, width - SQUARE_SIZE * 5, bottomRowY, SQUARE_SIZE * 3);

		IDrawing.setPositionAndWidth(textFieldName, TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, PANEL_WIDTH - COLOR_WIDTH - TEXT_FIELD_PADDING);
		IDrawing.setPositionAndWidth(textFieldColor, PANEL_WIDTH - COLOR_WIDTH + TEXT_FIELD_PADDING / 2, bottomRowY - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, COLOR_WIDTH - TEXT_FIELD_PADDING);

		dashboardList.x = 0;
		dashboardList.y = SQUARE_SIZE;
		dashboardList.width = PANEL_WIDTH;
		dashboardList.height = height - SQUARE_SIZE * 2;

		buttonDoneEditingStation.visible = false;
		buttonDoneEditingRoute.visible = false;
		buttonDoneEditingDepot.visible = false;

		textFieldName.setVisible(false);
		textFieldName.setMaxLength(MAX_NAME_LENGTH);
		textFieldName.setChangedListener(text -> textFieldName.setSuggestion(text.isEmpty() ? new TranslatableText("gui.mtr.name").getString() : ""));
		textFieldColor.setVisible(false);
		textFieldColor.setMaxLength(MAX_COLOR_ZONE_LENGTH);
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
		addButton(buttonTabDepots);
		addButton(buttonAddStation);
		addButton(buttonAddRoute);
		addButton(buttonAddDepot);
		addButton(buttonDoneEditingStation);
		addButton(buttonDoneEditingRoute);
		addButton(buttonDoneEditingDepot);
		addButton(buttonZoomIn);
		addButton(buttonZoomOut);
		addButton(buttonOptions);

		addChild(textFieldName);
		addChild(textFieldColor);
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			widgetMap.render(matrices, mouseX, mouseY, delta);
			DrawableHelper.fill(matrices, 0, 0, PANEL_WIDTH, height, ARGB_BACKGROUND);
			dashboardList.render(matrices, textRenderer, mouseX, mouseY, delta);
			super.render(matrices, mouseX, mouseY, delta);
			textFieldName.render(matrices, mouseX, mouseY, delta);
			textFieldColor.render(matrices, mouseX, mouseY, delta);
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
		textFieldColor.tick();
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
						dashboardList.setData(ClientData.ROUTES, false, true, true, false, false, true);
					} else {
						final List<DataConverter> routeData = editingRoute.platformIds.stream().map(ClientData.DATA_CACHE.platformIdMap::get).filter(Objects::nonNull).map(platform -> {
							final Station station = ClientData.DATA_CACHE.platformIdToStation.get(platform.id);
							if (station != null) {
								return new DataConverter(String.format("%s (%s)", station.name, platform.name), station.color);
							} else {
								return new DataConverter(String.format("(%s)", platform.name), 0);
							}
						}).collect(Collectors.toList());
						dashboardList.setData(routeData, false, false, false, true, false, true);
					}
					break;
				case DEPOTS:
					if (editingArea == null) {
						dashboardList.setData(ClientData.DEPOTS, true, true, true, false, false, true);
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
			if (editingArea == null) {
				final AreaBase area = (AreaBase) data;
				if (AreaBase.nonNullCorners(area)) {
					widgetMap.find(area.corner1.getLeft(), area.corner1.getRight(), area.corner2.getLeft(), area.corner2.getRight());
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
				if (editingArea == null) {
					startEditingArea((AreaBase) data, false);
				}
				break;
			case ROUTES:
				startEditingRoute((Route) data, false);
				break;
		}
	}

	private void onEdit(NameColorDataBase data, int index) {
		if (client != null) {
			switch (selectedTab) {
				case STATIONS:
					if (editingArea == null) {
						if (data instanceof Station) {
							client.openScreen(new EditStationScreen((Station) data, this));
						}
					} else {
						if (data instanceof Platform) {
							client.openScreen(new PlatformScreen((Platform) data, this));
						}
					}
					break;
				case ROUTES:
					client.openScreen(new EditRouteScreen((Route) data, this));
					break;
				case DEPOTS:
					if (editingArea == null) {
						if (data instanceof Depot) {
							client.openScreen(new EditDepotScreen((Depot) data, this));
						}
					} else {
						if (data instanceof Siding) {
							client.openScreen(new SidingScreen((Siding) data, this));
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
					if (client != null) {
						final Station station = (Station) data;
						client.openScreen(new DeleteConfirmationScreen(() -> {
							PacketTrainDataGuiClient.sendDeleteData(PACKET_DELETE_STATION, station.id);
							ClientData.STATIONS.remove(station);
						}, IGui.formatStationName(station.name), this));
					}
					break;
				case ROUTES:
					if (editingRoute == null) {
						if (client != null) {
							final Route route = (Route) data;
							client.openScreen(new DeleteConfirmationScreen(() -> {
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
					if (client != null) {
						final Depot depot = (Depot) data;
						client.openScreen(new DeleteConfirmationScreen(() -> {
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

	private List<Long> getList() {
		return editingRoute == null ? new ArrayList<>() : editingRoute.platformIds;
	}

	private void startEditingArea(AreaBase editingArea, boolean isNew) {
		this.editingArea = editingArea;
		editingRoute = null;
		this.isNew = isNew;

		textFieldName.setText(editingArea.name);
		textFieldColor.setText(colorIntToString(editingArea.color));

		widgetMap.startEditingArea(editingArea);
		toggleButtons();
	}

	private void startEditingRoute(Route editingRoute, boolean isNew) {
		editingArea = null;
		this.editingRoute = editingRoute;
		this.isNew = isNew;

		textFieldName.setText(editingRoute.name);
		textFieldColor.setText(colorIntToString(editingRoute.color));

		widgetMap.startEditingRoute();
		toggleButtons();
	}

	private void onDrawCorners(Pair<Integer, Integer> corner1, Pair<Integer, Integer> corner2) {
		editingArea.corner1 = corner1;
		editingArea.corner2 = corner2;
		toggleButtons();
	}

	private void onDrawCornersMouseRelease() {
		editingArea.setCorners(packet -> PacketTrainDataGuiClient.sendUpdate(editingArea instanceof Station ? PACKET_UPDATE_STATION : PACKET_UPDATE_DEPOT, packet));
	}

	private void onClickAddPlatformToRoute(long platformId) {
		editingRoute.platformIds.add(platformId);
		editingRoute.setPlatformIds(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
	}

	private void onClickEditSavedRail(SavedRailBase savedRail) {
		if (savedRail instanceof Platform) {
			MinecraftClient.getInstance().openScreen(new PlatformScreen((Platform) savedRail, this));
		} else if (savedRail instanceof Siding) {
			MinecraftClient.getInstance().openScreen(new SidingScreen((Siding) savedRail, this));
		}
	}

	private void onDoneEditingArea(boolean isStation) {
		if (isNew) {
			try {
				if (isStation) {
					ClientData.STATIONS.add((Station) editingArea);
				} else {
					ClientData.DEPOTS.add((Depot) editingArea);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		editingArea.name = IGui.textOrUntitled(textFieldName.getText());
		editingArea.color = colorStringToInt(textFieldColor.getText());
		editingArea.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(isStation ? PACKET_UPDATE_STATION : PACKET_UPDATE_DEPOT, packet));
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
		editingRoute.name = IGui.textOrUntitled(textFieldName.getText());
		editingRoute.color = colorStringToInt(textFieldColor.getText());
		editingRoute.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_ROUTE, packet));
		stopEditing();
	}

	private void stopEditing() {
		editingArea = null;
		editingRoute = null;
		widgetMap.stopEditing();
		toggleButtons();
	}

	private void toggleButtons() {
		buttonAddStation.visible = selectedTab == SelectedTab.STATIONS && editingArea == null;
		buttonAddRoute.visible = selectedTab == SelectedTab.ROUTES && editingRoute == null;
		buttonAddDepot.visible = selectedTab == SelectedTab.DEPOTS && editingArea == null;
		buttonDoneEditingStation.visible = selectedTab == SelectedTab.STATIONS && editingArea != null;
		buttonDoneEditingStation.active = AreaBase.nonNullCorners(editingArea);
		buttonDoneEditingRoute.visible = selectedTab == SelectedTab.ROUTES && editingRoute != null;
		buttonDoneEditingDepot.visible = selectedTab == SelectedTab.DEPOTS && editingArea != null;
		buttonDoneEditingDepot.active = AreaBase.nonNullCorners(editingArea);

		final boolean showTextFields = ((selectedTab == SelectedTab.STATIONS || selectedTab == SelectedTab.DEPOTS) && editingArea != null) || (selectedTab == SelectedTab.ROUTES && editingRoute != null);
		textFieldName.visible = showTextFields;
		textFieldColor.visible = showTextFields;
		dashboardList.height = height - SQUARE_SIZE * 2 - (showTextFields ? SQUARE_SIZE + TEXT_FIELD_PADDING : 0);
	}

	public static int colorStringToInt(String string) {
		try {
			return Integer.parseInt(string.toUpperCase().replaceAll("[^0-9A-F]", ""), 16);
		} catch (Exception ignored) {
			return 0;
		}
	}

	public static String colorIntToString(int color) {
		return StringUtils.leftPad(Integer.toHexString(color == 0 ? (new Random()).nextInt(RGB_WHITE + 1) : color).toUpperCase(), 6, "0");
	}

	private enum SelectedTab {STATIONS, ROUTES, DEPOTS}
}
