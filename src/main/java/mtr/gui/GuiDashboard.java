package mtr.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import mtr.data.PacketTrainDataGui;
import mtr.data.Platform;
import mtr.data.Station;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.TranslatableText;

import java.util.Set;

public class GuiDashboard extends LightweightGuiDescription implements IGui {

	private static final int LEFT_PANEL_RIGHT_PADDING = 5;
	private static final int MAP_LEFT_PADDING = 3;
	private static final int PANEL_BACKGROUND_PADDING = 8;
	private static final int LEFT_PANEL_WIDTH = 128;

	public GuiDashboard(Set<Station> stations, Set<Platform> platforms) {
		final MinecraftClient minecraftClient = MinecraftClient.getInstance();
		final Window window = minecraftClient.getWindow();
		final int windowWidth = window.getScaledWidth();
		final int windowHeight = window.getScaledHeight();

		setFullscreen(true);
		WPlainPanel root = new WPlainPanel();
		setRootPanel(root);

		WidgetStationNames stationNames = new WidgetStationNames(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING);

		final double mapCenterX, mapCenterY;
		if (minecraftClient.player == null) {
			mapCenterX = 0;
			mapCenterY = 0;
		} else {
			mapCenterX = minecraftClient.player.getX();
			mapCenterY = minecraftClient.player.getZ();
		}
		WidgetMap map = new WidgetMap(windowWidth - LEFT_PANEL_WIDTH - MAP_LEFT_PADDING, windowHeight, mapCenterX, mapCenterY, stations, platforms);
		map.setOnDoneDrawing((station, name, corner1, corner2, color) -> {
			stations.remove(station);
			station.name = name;
			station.corner1 = corner1;
			station.corner2 = corner2;
			station.color = color;
			stations.add(station);
			sendData(stationNames, map, stations, platforms);
		});
		root.add(map, LEFT_PANEL_WIDTH + MAP_LEFT_PADDING, 0, windowWidth - LEFT_PANEL_WIDTH - MAP_LEFT_PADDING, windowHeight);

		WPlainPanel leftPanel = new WPlainPanel();
		leftPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
		root.add(leftPanel, 0, PANEL_BACKGROUND_PADDING, LEFT_PANEL_WIDTH - LEFT_PANEL_RIGHT_PADDING, windowHeight);

		WLabel label = new WLabel(String.format("%s (%d)", new TranslatableText("gui.mtr.stations").getString(), stations.size()));
		label.setHorizontalAlignment(HorizontalAlignment.CENTER);
		label.setVerticalAlignment(VerticalAlignment.CENTER);
		root.add(label, 0, 0, LEFT_PANEL_WIDTH, SQUARE_SIZE);

		WidgetBetterScrollPanel scrollPanel = new WidgetBetterScrollPanel(stationNames);
		scrollPanel.setScrollingHorizontally(TriState.FALSE);
		scrollPanel.setScrollingVertically(TriState.TRUE);
		root.add(scrollPanel, 0, SQUARE_SIZE, LEFT_PANEL_WIDTH, windowHeight - SQUARE_SIZE);
		refreshStations(stationNames, map, stations, platforms);

		root.validate(this);
	}

	private void refreshStations(WidgetStationNames stationNames, WidgetMap map, Set<Station> stations, Set<Platform> platforms) {
		stationNames.refreshStations(stations, map::find, map::startDrawing, (station) -> {
			stations.remove(station);
			sendData(stationNames, map, stations, platforms);
		});
	}

	private void sendData(WidgetStationNames stationNames, WidgetMap map, Set<Station> stations, Set<Platform> platforms) {
		PacketTrainDataGui.sendC2S(stations, platforms);
		refreshStations(stationNames, map, stations, platforms);
		stationNames.validate(stationNames.getHost());
	}
}
