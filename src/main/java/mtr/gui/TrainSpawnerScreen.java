package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.data.Route;
import mtr.data.Train;
import mtr.data.TrainSpawner;
import mtr.packet.PacketTrainDataGuiClient;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TrainSpawnerScreen extends ScreenBase {

	public TrainSpawnerScreen(BlockPos pos) {
		super(new GuiSpawner(pos));
	}

	private static class GuiSpawner extends ScreenBase.GuiBase {

		private final WidgetSet<Route> widgetRoutesAvailable;
		private final WidgetRouteList widgetRoutesAdded;
		private final WidgetTrainTypeList widgetTrainsAvailable;
		private final WidgetTrainTypeList widgetTrainsAdded;
		private final BlockPos pos;
		private final List<Long> selectedRouteIds = new ArrayList<>();
		private final List<Train.TrainType> selectedTrains = new ArrayList<>();

		private static final int PANEL_WIDTH = 160;
		private static final int PANEL_HEIGHT = 120;
		private static final int SCROLL_BAR_WIDTH = 8;

		private GuiSpawner(BlockPos pos) {
			this.pos = pos;

			WTabPanel root = new WTabPanel();
			setRootPanel(root);

			widgetRoutesAvailable = new WidgetSet<>(PANEL_WIDTH - SCROLL_BAR_WIDTH);
			widgetRoutesAdded = new WidgetRouteList(PANEL_WIDTH - SCROLL_BAR_WIDTH);
			widgetTrainsAvailable = new WidgetTrainTypeList(PANEL_WIDTH - SCROLL_BAR_WIDTH);
			widgetTrainsAdded = new WidgetTrainTypeList(PANEL_WIDTH - SCROLL_BAR_WIDTH);

			WPlainPanel panelSettings = new WPlainPanel();

			WToggleButton toggleRemoveTrains = new WToggleButton(new TranslatableText("gui.mtr.remove_trains"));
			panelSettings.add(toggleRemoveTrains, 0, 0);
			WToggleButton toggleShuffleRoutes = new WToggleButton(new TranslatableText("gui.mtr.shuffle_routes"));
			panelSettings.add(toggleShuffleRoutes, 0, SQUARE_SIZE);
			WToggleButton toggleShuffleTrains = new WToggleButton(new TranslatableText("gui.mtr.shuffle_trains"));
			panelSettings.add(toggleShuffleTrains, 0, SQUARE_SIZE * 2);

			root.add(createSelectionScreen(widgetRoutesAvailable, widgetRoutesAdded), tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_routes.png"))).tooltip(new TranslatableText("gui.mtr.routes")));
			root.add(createSelectionScreen(widgetTrainsAvailable, widgetTrainsAdded), tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/item/train.png"))).tooltip(new TranslatableText("gui.mtr.trains")));
			root.add(panelSettings, tab -> tab.icon(new TextureIcon(new Identifier("textures/item/iron_pickaxe.png"))).tooltip(new TranslatableText("gui.mtr.settings")));

			refreshInterface();
			root.validate(this);
		}

		@Override
		public void refreshInterface() {
			selectedRouteIds.clear();
			selectedTrains.clear();
			final Optional<TrainSpawner> optionalTrainSpawner = trainSpawners.stream().filter(trainSpawner -> trainSpawner.pos.equals(pos)).findFirst();
			if (optionalTrainSpawner.isPresent()) {
				selectedRouteIds.addAll(optionalTrainSpawner.get().routeIds);
				selectedTrains.addAll(optionalTrainSpawner.get().trainTypes);
			}

			widgetRoutesAvailable.refreshList(routes, null, null, null, null, "icon_add", route -> {
				selectedRouteIds.add(route.id);
				sendData();
			}, null);
			widgetRoutesAdded.refreshList(selectedRouteIds.stream().map(GuiSpawner::getRouteById).collect(Collectors.toList()), this::sendData, selectedRouteIds);
			widgetTrainsAvailable.refreshList(Arrays.asList(Train.TrainType.values()), trainType -> {
				selectedTrains.add(trainType);
				sendData();
			});
			widgetTrainsAdded.refreshList(selectedTrains, this::sendData, selectedTrains);

			rootPanel.validate(this);
		}

		@Override
		public void sendData() {
			PacketTrainDataGuiClient.sendTrainSpawnerC2S(new TrainSpawner(pos, selectedRouteIds, selectedTrains));
		}

		private static Route getRouteById(long id) {
			return routes.stream().filter(route -> route.id == id).findFirst().orElse(null);
		}

		private static WPlainPanel createSelectionScreen(WBox widgetAvailable, WBox widgetAdded) {
			WPlainPanel panelTrains = new WPlainPanel();
			panelTrains.add(new WLabel(new TranslatableText("gui.mtr.available")), TEXT_PADDING, TEXT_PADDING);
			panelTrains.add(new WLabel(new TranslatableText("gui.mtr.added")), PANEL_WIDTH + TEXT_PADDING, TEXT_PADDING);

			WidgetBetterScrollPanel scrollPanelTrainsAvailable = new WidgetBetterScrollPanel(widgetAvailable);
			scrollPanelTrainsAvailable.setScrollingHorizontally(TriState.FALSE);
			scrollPanelTrainsAvailable.setScrollingVertically(TriState.TRUE);
			panelTrains.add(scrollPanelTrainsAvailable, 0, SQUARE_SIZE, PANEL_WIDTH, PANEL_HEIGHT);

			WidgetBetterScrollPanel scrollPanelTrainsAdded = new WidgetBetterScrollPanel(widgetAdded);
			scrollPanelTrainsAdded.setScrollingHorizontally(TriState.FALSE);
			scrollPanelTrainsAdded.setScrollingVertically(TriState.TRUE);
			panelTrains.add(scrollPanelTrainsAdded, PANEL_WIDTH, SQUARE_SIZE, PANEL_WIDTH, PANEL_HEIGHT);

			return panelTrains;
		}

		@Override
		public void addPainters() {
		}
	}
}
