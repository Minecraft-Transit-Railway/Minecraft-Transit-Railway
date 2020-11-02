package mtr.gui;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WTabPanel;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import mtr.MTR;
import mtr.data.TrainSpawner;
import mtr.packet.PacketTrainDataGuiClient;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class TrainSpawnerScreen extends ScreenBase implements IGui {

	public TrainSpawnerScreen(BlockPos pos) {
		super(new GuiSpawner(pos));
	}

	private static class GuiSpawner extends ScreenBase.GuiBase {

		private final WidgetRouteOnlyList routesAvailable, routesAdded;
		private final BlockPos pos;
		private final Set<Long> selectedRoutes = new HashSet<>();

		private static final int PANEL_SIZE = 160;
		private static final int SCROLL_BAR_WIDTH = 8;

		private GuiSpawner(BlockPos pos) {
			this.pos = pos;

			WTabPanel root = new WTabPanel();
			setRootPanel(root);

			WPlainPanel panelRoutes = new WPlainPanel();

			WLabel labelAvailable = new WLabel(new TranslatableText("gui.mtr.available"));
			panelRoutes.add(labelAvailable, TEXT_PADDING, TEXT_PADDING);

			WLabel labelAdded = new WLabel(new TranslatableText("gui.mtr.added"));
			panelRoutes.add(labelAdded, PANEL_SIZE + TEXT_PADDING, TEXT_PADDING);

			routesAvailable = new WidgetRouteOnlyList(PANEL_SIZE - SCROLL_BAR_WIDTH);
			routesAdded = new WidgetRouteOnlyList(PANEL_SIZE - SCROLL_BAR_WIDTH);

			WidgetBetterScrollPanel scrollPanelAvailable = new WidgetBetterScrollPanel(routesAvailable);
			scrollPanelAvailable.setScrollingHorizontally(TriState.FALSE);
			scrollPanelAvailable.setScrollingVertically(TriState.TRUE);
			panelRoutes.add(scrollPanelAvailable, 0, SQUARE_SIZE, PANEL_SIZE, PANEL_SIZE);

			WidgetBetterScrollPanel scrollPanelAdded = new WidgetBetterScrollPanel(routesAdded);
			scrollPanelAdded.setScrollingHorizontally(TriState.FALSE);
			scrollPanelAdded.setScrollingVertically(TriState.TRUE);
			panelRoutes.add(scrollPanelAdded, PANEL_SIZE, SQUARE_SIZE, PANEL_SIZE, PANEL_SIZE);

			WPlainPanel panelTrains = new WPlainPanel();

			root.add(panelRoutes, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/gui/icon_routes.png"))).tooltip(new TranslatableText("gui.mtr.routes")));
			root.add(panelTrains, tab -> tab.icon(new TextureIcon(new Identifier(MTR.MOD_ID, "textures/item/train.png"))).tooltip(new TranslatableText("gui.mtr.trains")));

			refreshInterface();
			root.validate(this);
		}

		@Override
		public void refreshInterface() {
			selectedRoutes.clear();
			selectedRoutes.addAll(trainSpawners.stream().filter(trainSpawner -> trainSpawner.pos.equals(pos)).map(trainSpawner -> trainSpawner.routeIds).findFirst().orElse(new HashSet<>()));
			routesAvailable.refreshList(routes.stream().filter(route -> !selectedRoutes.contains(route.id)).collect(Collectors.toSet()), "icon_add", route -> {
				selectedRoutes.add(route.id);
				sendRouteData();
			});
			routesAdded.refreshList(routes.stream().filter(route -> selectedRoutes.contains(route.id)).collect(Collectors.toSet()), "icon_delete", route -> {
				selectedRoutes.remove(route.id);
				sendRouteData();
			});
			rootPanel.validate(this);
		}

		private void sendRouteData() {
			PacketTrainDataGuiClient.sendTrainSpawnerC2S(new TrainSpawner(pos, selectedRoutes));
			refreshInterface();
		}

		@Override
		public void addPainters() {
		}
	}
}
