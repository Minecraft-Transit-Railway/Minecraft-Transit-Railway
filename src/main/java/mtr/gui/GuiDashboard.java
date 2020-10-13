package mtr.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
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

		WBox boxPanelStations = new WBox(Axis.VERTICAL);
		boxPanelStations.setSpacing(0);

		final double mapCenterX, mapCenterY;
		if (minecraftClient.player == null) {
			mapCenterX = 0;
			mapCenterY = 0;
		} else {
			mapCenterX = minecraftClient.player.getX();
			mapCenterY = minecraftClient.player.getZ();
		}
		WidgetMap map = new WidgetMap(this, windowWidth - LEFT_PANEL_WIDTH - MAP_LEFT_PADDING, windowHeight, mapCenterX, mapCenterY, stations, platforms);
		map.setOnDoneDrawing((name, corner1, corner2) -> {
			Station station = new Station(name.isEmpty() ? new TranslatableText("gui.mtr.untitled").getString() : name);
			station.changeArea(corner1, corner2);
			stations.add(station);
			PacketTrainDataGui.sendC2S(stations, platforms);
			createWidgetStationName(stations, platforms, station, map, boxPanelStations, root);
			root.validate(this);
		});
		root.add(map, LEFT_PANEL_WIDTH + MAP_LEFT_PADDING, 0, windowWidth - LEFT_PANEL_WIDTH - MAP_LEFT_PADDING, windowHeight);

		WPlainPanel leftPanel = new WPlainPanel();
		leftPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
		root.add(leftPanel, 0, PANEL_BACKGROUND_PADDING, LEFT_PANEL_WIDTH - LEFT_PANEL_RIGHT_PADDING, windowHeight);

		WLabel label = new WLabel(new TranslatableText("gui.mtr.stations"));
		label.setHorizontalAlignment(HorizontalAlignment.CENTER);
		label.setVerticalAlignment(VerticalAlignment.CENTER);
		root.add(label, 0, 0, LEFT_PANEL_WIDTH, SQUARE_SIZE);

		for (Station station : stations) {
			createWidgetStationName(stations, platforms, station, map, boxPanelStations, root);
		}

		WScrollPanel scrollPanel = new WScrollPanel(boxPanelStations);
		scrollPanel.setScrollingHorizontally(TriState.FALSE);
		scrollPanel.setScrollingVertically(TriState.TRUE);
		root.add(scrollPanel, 0, SQUARE_SIZE, LEFT_PANEL_WIDTH, windowHeight - SQUARE_SIZE);

		root.validate(this);
	}

	private void createWidgetStationName(Set<Station> stations, Set<Platform> platforms, Station station, WidgetMap map, WBox panel, WWidget root) {
		WidgetStationName panelStation = new WidgetStationName(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING, station.name);
		panelStation.setOnClick(() -> map.find(station), () -> {
			stations.remove(station);
			PacketTrainDataGui.sendC2S(stations, platforms);
			panel.remove(panelStation);
			root.validate(this);
		});
		panel.add(panelStation);
	}
}
