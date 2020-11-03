package mtr.gui;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTabPanel;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.packet.PacketTrainDataGuiClient;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

public class DashboardScreen extends ScreenBase {

	public DashboardScreen() {
		super(new GuiDashboard());
	}

	private static class GuiDashboard extends ScreenBase.GuiBase {

		private final WidgetStationList widgetStationList;
		private final WidgetRouteChildrenList widgetRouteChildrenList;
		private final WidgetMap widgetMap;

		private static final int PANEL_BACKGROUND_PADDING = 8;
		private static final int TAB_HEIGHT = 30;
		private static final int LEFT_PANEL_WIDTH = 160;

		private GuiDashboard() {
			final MinecraftClient minecraftClient = MinecraftClient.getInstance();
			final Window window = minecraftClient.getWindow();
			final int windowWidth = window.getScaledWidth();
			final int windowHeight = window.getScaledHeight();

			setFullscreen(true);
			WPlainPanel root = new WPlainPanel();
			setRootPanel(root);

			widgetStationList = new WidgetStationList(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 3);
			widgetRouteChildrenList = new WidgetRouteChildrenList(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 3);

			final double mapCenterX, mapCenterY;
			if (minecraftClient.player == null) {
				mapCenterX = 0;
				mapCenterY = 0;
			} else {
				mapCenterX = minecraftClient.player.getX();
				mapCenterY = minecraftClient.player.getZ();
			}
			widgetMap = new WidgetMap(windowWidth - LEFT_PANEL_WIDTH, windowHeight, mapCenterX, mapCenterY, stations, platforms);
			widgetMap.setOnDoneEditing((station, name, corner1, corner2, color) -> {
				stations.remove(station);
				station.name = name;
				station.corner1 = corner1;
				station.corner2 = corner2;
				station.color = color;
				stations.add(station);
				sendData();
			}, (route, name, color, moreStations) -> {
				routes.remove(route);
				route.name = name;
				route.color = color;
				moreStations.forEach(station -> route.stationIds.add(station.id));
				routes.add(route);
				sendData();
			});
			root.add(widgetMap, LEFT_PANEL_WIDTH, 0, windowWidth - LEFT_PANEL_WIDTH, windowHeight);

			WidgetBetterScrollPanel scrollPanelStations = new WidgetBetterScrollPanel(widgetStationList);
			scrollPanelStations.setScrollingHorizontally(TriState.FALSE);
			scrollPanelStations.setScrollingVertically(TriState.TRUE);
			scrollPanelStations.setSize(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 2, windowHeight - TAB_HEIGHT - PANEL_BACKGROUND_PADDING * 2);

			WidgetBetterScrollPanel scrollPanelRoutes = new WidgetBetterScrollPanel(widgetRouteChildrenList);
			scrollPanelRoutes.setScrollingHorizontally(TriState.FALSE);
			scrollPanelRoutes.setScrollingVertically(TriState.TRUE);
			scrollPanelRoutes.setSize(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 2, windowHeight - TAB_HEIGHT - PANEL_BACKGROUND_PADDING * 2);

			WTabPanel tabPanel = new WTabPanel();
			tabPanel.setBackgroundPainter((left, top, panel) -> ScreenDrawing.coloredRect(left, top, panel.getWidth() + PANEL_BACKGROUND_PADDING, panel.getHeight(), ARGB_BLACK));
			tabPanel.add(scrollPanelStations, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/block/logo.png"))).tooltip(new TranslatableText("gui.mtr.stations")));
			tabPanel.add(scrollPanelRoutes, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_routes.png"))).tooltip(new TranslatableText("gui.mtr.routes")));
			root.add(tabPanel, 0, 0, LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING * 2, windowHeight);

			refreshInterface();
			root.validate(this);
		}

		@Override
		public void refreshInterface() {
			widgetStationList.refreshList(stations, widgetMap::find, widgetMap::startEditingStation, (station) -> {
				stations.remove(station);
				sendData();
			});
			widgetRouteChildrenList.refreshList(routes, stations, widgetMap::startEditingRoute, routes::remove, this::sendData);
			rootPanel.validate(this);
		}

		private void sendData() {
			PacketTrainDataGuiClient.sendStationsAndRoutesC2S(stations, routes);
			refreshInterface();
		}
	}
}
