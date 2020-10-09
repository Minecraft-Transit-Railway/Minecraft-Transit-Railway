package mtr.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.data.Station;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Window;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;

import java.util.Set;

public class GuiDashboard extends LightweightGuiDescription {

	private static final int PADDING = 6;
	private static final int SQUARE_SIZE = 20;
	private static final int LEFT_PANEL_WIDTH = 128;
	private static final int SCROLL_BAR_WIDTH = 8;
	private static final int WHITE_COLOR = 0xFFFFFF;

	public GuiDashboard(Set<Station> stations) {
		final Window window = MinecraftClient.getInstance().getWindow();
		final int windowWidth = window.getScaledWidth();
		final int windowHeight = window.getScaledHeight();

		setFullscreen(true);
		WPlainPanel root = new WPlainPanel();
		setRootPanel(root);

		WLabel label = new WLabel(new TranslatableText("gui.mtr.stations"), WHITE_COLOR);
		label.setHorizontalAlignment(HorizontalAlignment.CENTER);
		label.setVerticalAlignment(VerticalAlignment.CENTER);
		root.add(label, 0, 0, LEFT_PANEL_WIDTH, SQUARE_SIZE);

		WPlainPanel panelStations = new WPlainPanel();
		int i = 0;
		for (Station station : stations) {
			WLabel labelStationName = new WLabel(new TranslatableText(station.getName()), WHITE_COLOR);
			labelStationName.setVerticalAlignment(VerticalAlignment.CENTER);
			panelStations.add(labelStationName, PADDING, i * SQUARE_SIZE, 0, SQUARE_SIZE);

			WButton buttonFind = new WButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_find.png")));
			panelStations.add(buttonFind, LEFT_PANEL_WIDTH - SQUARE_SIZE * 2, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

			WButton buttonDelete = new WButton(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_delete.png")));
			panelStations.add(buttonDelete, LEFT_PANEL_WIDTH - SQUARE_SIZE, i * SQUARE_SIZE, SQUARE_SIZE, SQUARE_SIZE);

			i++;
		}

		WScrollPanel scrollPanel = new WScrollPanel(panelStations);
		scrollPanel.setScrollingHorizontally(TriState.FALSE);
		scrollPanel.setScrollingVertically(TriState.TRUE);
		root.add(scrollPanel, 0, SQUARE_SIZE, LEFT_PANEL_WIDTH + SCROLL_BAR_WIDTH, windowHeight - SQUARE_SIZE);

		root.validate(this);
	}
}
