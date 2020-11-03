package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTabPanel;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.data.Train;
import mtr.data.TrainSpawner;
import mtr.packet.PacketTrainDataGuiClient;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TrainSpawnerScreen extends ScreenBase implements IGui {

	public TrainSpawnerScreen(BlockPos pos) {
		super(new GuiSpawner(pos));
	}

	private static class GuiSpawner extends ScreenBase.GuiBase {

		private final WidgetRouteOnlyList widgetRoutesAvailable, widgetRoutesAdded;
		private final WidgetTrainTypeList widgetTrainsAvailable, widgetTrainsAdded;
		private final BlockPos pos;
		private final Set<Long> selectedRoutes = new HashSet<>();
		private final Set<Train.TrainType> selectedTrains = new HashSet<>();

		private static final int PANEL_SIZE = 160;
		private static final int SCROLL_BAR_WIDTH = 8;

		private GuiSpawner(BlockPos pos) {
			this.pos = pos;

			WTabPanel root = new WTabPanel();
			setRootPanel(root);

			WPlainPanel panelRoutes = new WPlainPanel();
			panelRoutes.add(new WLabel(new TranslatableText("gui.mtr.available")), TEXT_PADDING, TEXT_PADDING);
			panelRoutes.add(new WLabel(new TranslatableText("gui.mtr.added")), PANEL_SIZE + TEXT_PADDING, TEXT_PADDING);

			widgetRoutesAvailable = new WidgetRouteOnlyList(PANEL_SIZE - SCROLL_BAR_WIDTH);
			widgetRoutesAdded = new WidgetRouteOnlyList(PANEL_SIZE - SCROLL_BAR_WIDTH);

			WidgetBetterScrollPanel scrollPanelRoutesAvailable = new WidgetBetterScrollPanel(widgetRoutesAvailable);
			scrollPanelRoutesAvailable.setScrollingHorizontally(TriState.FALSE);
			scrollPanelRoutesAvailable.setScrollingVertically(TriState.TRUE);
			panelRoutes.add(scrollPanelRoutesAvailable, 0, SQUARE_SIZE, PANEL_SIZE, PANEL_SIZE);

			WidgetBetterScrollPanel scrollPanelRoutesAdded = new WidgetBetterScrollPanel(widgetRoutesAdded);
			scrollPanelRoutesAdded.setScrollingHorizontally(TriState.FALSE);
			scrollPanelRoutesAdded.setScrollingVertically(TriState.TRUE);
			panelRoutes.add(scrollPanelRoutesAdded, PANEL_SIZE, SQUARE_SIZE, PANEL_SIZE, PANEL_SIZE);

			WPlainPanel panelTrains = new WPlainPanel();
			panelTrains.add(new WLabel(new TranslatableText("gui.mtr.available")), TEXT_PADDING, TEXT_PADDING);
			panelTrains.add(new WLabel(new TranslatableText("gui.mtr.added")), PANEL_SIZE + TEXT_PADDING, TEXT_PADDING);

			widgetTrainsAvailable = new WidgetTrainTypeList(PANEL_SIZE - SCROLL_BAR_WIDTH);
			widgetTrainsAdded = new WidgetTrainTypeList(PANEL_SIZE - SCROLL_BAR_WIDTH);

			WidgetBetterScrollPanel scrollPanelTrainsAvailable = new WidgetBetterScrollPanel(widgetTrainsAvailable);
			scrollPanelTrainsAvailable.setScrollingHorizontally(TriState.FALSE);
			scrollPanelTrainsAvailable.setScrollingVertically(TriState.TRUE);
			panelTrains.add(scrollPanelTrainsAvailable, 0, SQUARE_SIZE, PANEL_SIZE, PANEL_SIZE);

			WidgetBetterScrollPanel scrollPanelTrainsAdded = new WidgetBetterScrollPanel(widgetTrainsAdded);
			scrollPanelTrainsAdded.setScrollingHorizontally(TriState.FALSE);
			scrollPanelTrainsAdded.setScrollingVertically(TriState.TRUE);
			panelTrains.add(scrollPanelTrainsAdded, PANEL_SIZE, SQUARE_SIZE, PANEL_SIZE, PANEL_SIZE);


			root.add(panelRoutes, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_routes.png"))).tooltip(new TranslatableText("gui.mtr.routes")));
			root.add(panelTrains, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/item/train.png"))).tooltip(new TranslatableText("gui.mtr.trains")));

			refreshInterface();
			root.validate(this);
		}

		@Override
		public void refreshInterface() {
			selectedRoutes.clear();
			selectedTrains.clear();
			final Optional<TrainSpawner> optionalTrainSpawner = trainSpawners.stream().filter(trainSpawner -> trainSpawner.pos.equals(pos)).findFirst();
			if (optionalTrainSpawner.isPresent()) {
				selectedRoutes.addAll(optionalTrainSpawner.get().routeIds);
				selectedTrains.addAll(optionalTrainSpawner.get().trainTypes);
			}
			widgetRoutesAvailable.refreshList(routes.stream().filter(route -> !selectedRoutes.contains(route.id)).collect(Collectors.toSet()), "icon_add", route -> {
				selectedRoutes.add(route.id);
				sendRouteData();
			});
			widgetRoutesAdded.refreshList(routes.stream().filter(route -> selectedRoutes.contains(route.id)).collect(Collectors.toSet()), "icon_delete", route -> {
				selectedRoutes.remove(route.id);
				sendRouteData();
			});
			widgetTrainsAvailable.refreshList(Arrays.stream(Train.TrainType.values()).filter(trainType -> !selectedTrains.contains(trainType)).collect(Collectors.toList()), "icon_add", (trainType) -> {
				selectedTrains.add(trainType);
				sendRouteData();
			});
			widgetTrainsAdded.refreshList(Arrays.stream(Train.TrainType.values()).filter(selectedTrains::contains).collect(Collectors.toList()), "icon_delete", (trainType) -> {
				selectedTrains.remove(trainType);
				sendRouteData();
			});
			rootPanel.validate(this);
		}

		private void sendRouteData() {
			PacketTrainDataGuiClient.sendTrainSpawnerC2S(new TrainSpawner(pos, selectedRoutes, selectedTrains));
			refreshInterface();
		}

		@Override
		public void addPainters() {
		}
	}
}
