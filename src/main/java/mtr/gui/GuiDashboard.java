package mtr.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WBox;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import mtr.data.PacketTrainDataGui;
import mtr.data.Platform;
import mtr.data.Station;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.TranslatableText;

import java.util.Set;

public class GuiDashboard extends LightweightGuiDescription implements IGui {

	private static final int LEFT_PANEL_RIGHT_PADDING = 5;
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

		double mapCenterX = 0, mapCenterY = 0;
		PlayerEntity player = minecraftClient.player;
		if (player != null) {
			mapCenterX = player.getX();
			mapCenterY = player.getZ();
		}
		WidgetMap map = new WidgetMap(windowWidth - LEFT_PANEL_WIDTH, windowHeight, mapCenterX, mapCenterY, stations, platforms);
		root.add(map, LEFT_PANEL_WIDTH, 0, windowWidth - LEFT_PANEL_WIDTH, windowHeight);

		WPlainPanel leftPanel = new WPlainPanel();
		leftPanel.setBackgroundPainter(BackgroundPainter.VANILLA);
		root.add(leftPanel, 0, PANEL_BACKGROUND_PADDING, LEFT_PANEL_WIDTH - LEFT_PANEL_RIGHT_PADDING, windowHeight);

		WLabel label = new WLabel(new TranslatableText("gui.mtr.stations"));
		label.setHorizontalAlignment(HorizontalAlignment.CENTER);
		label.setVerticalAlignment(VerticalAlignment.CENTER);
		root.add(label, 0, 0, LEFT_PANEL_WIDTH, SQUARE_SIZE);

		WBox panelStations = new WBox(Axis.VERTICAL);
		for (Station station : stations) {
			WidgetStationName panelStation = new WidgetStationName(LEFT_PANEL_WIDTH - PANEL_BACKGROUND_PADDING, station.name);
			panelStation.setOnClick(() -> {
				stations.remove(station);
				PacketTrainDataGui.sendC2S(stations, platforms);
				panelStations.remove(panelStation);
				panelStations.layout();
			});

			panelStations.add(panelStation);
		}
		panelStations.setSpacing(0);

		WScrollPanel scrollPanel = new WScrollPanel(panelStations);
		scrollPanel.setScrollingHorizontally(TriState.FALSE);
		scrollPanel.setScrollingVertically(TriState.TRUE);
		root.add(scrollPanel, 0, SQUARE_SIZE, LEFT_PANEL_WIDTH, windowHeight - SQUARE_SIZE);

		root.validate(this);
	}
}
