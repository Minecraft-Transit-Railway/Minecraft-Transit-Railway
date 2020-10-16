package mtr.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTabPanel;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.data.PacketTrainDataGui;
import mtr.data.Platform;
import mtr.data.Route;
import mtr.data.Station;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Set;

public class GuiDashboard extends LightweightGuiDescription implements IGui {

	private static final int PANEL_BACKGROUND_PADDING = 8;
	private static final int TAB_HEIGHT = 30;
	private static final int LEFT_PANEL_WIDTH = 160;

	public GuiDashboard(Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Window window = minecraftClient.getWindow();
		final int windowWidth = window.getScaledWidth();
		final int windowHeight = window.getScaledHeight();

		setFullscreen(true);
		WPlainPanel root = new WPlainPanel();
		setRootPanel(root);

		WidgetNameColors<Station> stationNames = new WidgetNameColors<>(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 3);
		WidgetNameColors<Route> routeNames = new WidgetNameColors<>(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 3);

		final double mapCenterX, mapCenterY;
		if (minecraftClient.player == null) {
			mapCenterX = 0;
			mapCenterY = 0;
		} else {
			mapCenterX = minecraftClient.player.getX();
			mapCenterY = minecraftClient.player.getZ();
		}
		WidgetMap map = new WidgetMap(windowWidth - LEFT_PANEL_WIDTH, windowHeight, mapCenterX, mapCenterY, stations, platforms);
		map.setOnDoneEditing((station, name, corner1, corner2, color) -> {
			stations.remove(station);
			station.name = name;
			station.corner1 = corner1;
			station.corner2 = corner2;
			station.color = color;
			stations.add(station);
			sendStationData(stationNames, map, stations, platforms, routes);
		}, (route, name, color, moreStations) -> {
			routes.remove(route);
			route.name = name;
			route.color = color;
			moreStations.forEach(station -> route.stationIds.add(station.id));
			routes.add(route);
			sendRouteData(routeNames, map, stations, platforms, routes);
		});
		root.add(map, LEFT_PANEL_WIDTH, 0, windowWidth - LEFT_PANEL_WIDTH, windowHeight);

		WidgetBetterScrollPanel scrollPanelStations = new WidgetBetterScrollPanel(stationNames);
		scrollPanelStations.setScrollingHorizontally(TriState.FALSE);
		scrollPanelStations.setScrollingVertically(TriState.TRUE);
		scrollPanelStations.setSize(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 2, windowHeight - TAB_HEIGHT - PANEL_BACKGROUND_PADDING * 2);
		refreshStations(stationNames, map, stations, platforms, routes);

		WidgetBetterScrollPanel scrollPanelRoutes = new WidgetBetterScrollPanel(routeNames);
		scrollPanelRoutes.setScrollingHorizontally(TriState.FALSE);
		scrollPanelRoutes.setScrollingVertically(TriState.TRUE);
		scrollPanelRoutes.setSize(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 2, windowHeight - TAB_HEIGHT - PANEL_BACKGROUND_PADDING * 2);
		refreshRoutes(routeNames, map, stations, platforms, routes);

		WTabPanel tabPanel = new WTabPanel();
		tabPanel.setBackgroundPainter((left, top, panel) -> ScreenDrawing.coloredRect(left, top, panel.getWidth() + PANEL_BACKGROUND_PADDING, panel.getHeight(), ARGB_BLACK));
		tabPanel.add(scrollPanelStations, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/block/logo.png"))).tooltip(translationAndCount("gui.mtr.stations", stations.size())));
		tabPanel.add(scrollPanelRoutes, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_routes.png"))).tooltip(translationAndCount("gui.mtr.routes", routes.size())));
		root.add(tabPanel, 0, 0, LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 2, windowHeight);

		root.validate(this);
	}

	private void refreshStations(WidgetNameColors<Station> stationNames, WidgetMap map, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		stationNames.refreshList(stations, map::find, map::startEditingStation, (station) -> {
			stations.remove(station);
			sendStationData(stationNames, map, stations, platforms, routes);
		});
	}

	private void sendStationData(WidgetNameColors<Station> stationNames, WidgetMap map, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		PacketTrainDataGui.sendC2S(stations, platforms, routes);
		refreshStations(stationNames, map, stations, platforms, routes);
		stationNames.validate(stationNames.getHost());
	}

	private void refreshRoutes(WidgetNameColors<Route> routeNames, WidgetMap map, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		routeNames.refreshList(routes, stations, map::startEditingRoute, routes::remove, () -> sendRouteData(routeNames, map, stations, platforms, routes));
	}

	private void sendRouteData(WidgetNameColors<Route> routeNames, WidgetMap map, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		PacketTrainDataGui.sendC2S(stations, platforms, routes);
		refreshRoutes(routeNames, map, stations, platforms, routes);
		routeNames.validate(routeNames.getHost());
	}

	private LiteralText translationAndCount(String key, int count) {
		return new LiteralText(String.format("%s (%d)", new TranslatableText(key).getString(), count));
	}
}
