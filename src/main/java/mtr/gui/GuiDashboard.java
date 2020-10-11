package mtr.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.data.Platform;
import mtr.data.Station;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Set;

public class GuiDashboard extends LightweightGuiDescription {

	private static final int LEFT_PANEL_RIGHT_PADDING = 5;
	private static final int TEXT_PADDING = 6;
	private static final int PANEL_BACKGROUND_PADDING = 8;
	private static final int SQUARE_SIZE = 20;
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

		WPlainPanel panelStations = new WPlainPanel();
		int i = 0;
		for (Station station : stations) {
			WLabel labelStationName = new WLabel(new TranslatableText(station.name));
			labelStationName.setVerticalAlignment(VerticalAlignment.CENTER);
			panelStations.add(labelStationName, TEXT_PADDING, i * SQUARE_SIZE, 0, SQUARE_SIZE);

			WButton buttonFind = new WButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_find.png")));
			panelStations.add(buttonFind, LEFT_PANEL_WIDTH - SQUARE_SIZE * 2 - PANEL_BACKGROUND_PADDING, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

			WButton buttonDelete = new WButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_delete.png")));
			panelStations.add(buttonDelete, LEFT_PANEL_WIDTH - SQUARE_SIZE - PANEL_BACKGROUND_PADDING, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

			i++;
		}

		WScrollPanel scrollPanel = new WScrollPanel(panelStations);
		scrollPanel.setScrollingHorizontally(TriState.FALSE);
		scrollPanel.setScrollingVertically(TriState.TRUE);
		root.add(scrollPanel, 0, SQUARE_SIZE, LEFT_PANEL_WIDTH, windowHeight - SQUARE_SIZE);

		WButton buttonZoomIn = new WButton(new LiteralText("+"));
		buttonZoomIn.setOnClick(() -> {
			map.scale(1);
		});
		root.add(buttonZoomIn, windowWidth - SQUARE_SIZE, windowHeight - SQUARE_SIZE * 2, SQUARE_SIZE, SQUARE_SIZE);

		WButton buttonZoomOut = new WButton(new LiteralText("-"));
		buttonZoomOut.setOnClick(() -> {
			map.scale(-1);
		});
		root.add(buttonZoomOut, windowWidth - SQUARE_SIZE, windowHeight - SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

		root.validate(this);
	}
}
