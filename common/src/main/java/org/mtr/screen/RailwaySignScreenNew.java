package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.constraints.*;
import gg.essential.universal.UMinecraft;
import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockRailwaySign;
import org.mtr.client.CustomResourceLoader;
import org.mtr.core.data.Platform;
import org.mtr.core.data.Route;
import org.mtr.core.data.Station;
import org.mtr.core.data.StationExit;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateRailwaySignConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.resource.SignResource;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.*;

import javax.annotation.Nullable;

public final class RailwaySignScreenNew extends WindowBase {

	@Nullable
	private final BlockPos signPos;
	private final LongAVLTreeSet[] selectedIds;
	private final String[] signIds;

	private static final int MAX_SIGN_TILE_HEIGHT = 32;
	private static final int MAX_SEARCH_WIDTH = 64;
	private static final int SMALL_SIGN_COLUMNS = 8;
	private static final int LARGE_SIGN_COLUMNS = 4;
	private static final int SMALL_SIGN_WIDTH_UNITS = 1;
	private static final int LARGE_SIGN_WIDTH_UNITS = 4;
	private static final int SIGN_COLUMNS = SMALL_SIGN_COLUMNS + LARGE_SIGN_COLUMNS;

	public RailwaySignScreenNew(BlockPos signPos) {
		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		if (clientWorld != null && clientWorld.getBlockEntity(signPos) instanceof BlockRailwaySign.RailwaySignBlockEntity railwaySignBlockEntity) {
			selectedIds = railwaySignBlockEntity.getSelectedIds();
			signIds = railwaySignBlockEntity.getSignIds();

			if (signIds.length > 0) {
				this.signPos = signPos;
				final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of());

				final UIContainer topContainer = (UIContainer) new UIContainer()
						.setChildOf(backgroundComponent)
						.setWidth(new RelativeConstraint())
						.setHeight(new PixelConstraint(MAX_SIGN_TILE_HEIGHT + 2));

				final SignPreviewComponent signPreviewComponent = (SignPreviewComponent) new SignPreviewComponent(signPos, selectedIds, signIds)
						.setChildOf(topContainer)
						.setWidth(new CoerceAtMostConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2 + MAX_SEARCH_WIDTH)), new PixelConstraint(MAX_SIGN_TILE_HEIGHT * signIds.length + 2)))
						.setHeight(GuiHelper.createAspectConstraintWithPadding(1F / signIds.length, 1));

				final TextInputComponent searchComponent = (TextInputComponent) new TextInputComponent()
						.setChildOf(topContainer)
						.setX(new PixelConstraint(0, true))
						.setWidth(new CoerceAtMostConstraint(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)), new PixelConstraint(MAX_SEARCH_WIDTH)))
						.setHeight(new PixelConstraint(20));

				searchComponent.setPlaceholderText(TranslationProvider.GUI_MTR_SEARCH.getString());

				final UIContainer container = (UIContainer) new UIContainer()
						.setChildOf(backgroundComponent)
						.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
						.setWidth(new RelativeConstraint())
						.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING)));

				final ObjectArrayList<SignResource> signResources1 = new ObjectArrayList<>();
				final ObjectArrayList<SignResource> signResources2 = new ObjectArrayList<>();
				CustomResourceLoader.getSortedSigns().forEach(signResource -> (signResource.hasCustomText ? signResources2 : signResources1).add(signResource));
				createMainComponents(container, SMALL_SIGN_COLUMNS, SMALL_SIGN_WIDTH_UNITS, signPreviewComponent, searchComponent, signResources1);
				createMainComponents(container, LARGE_SIGN_COLUMNS, LARGE_SIGN_WIDTH_UNITS, signPreviewComponent, searchComponent, signResources2);
			} else {
				this.signPos = null;
			}
		} else {
			this.signPos = null;
			selectedIds = new LongAVLTreeSet[0];
			signIds = new String[0];
		}
	}

	@Override
	public void onScreenClose() {
		super.onScreenClose();
		if (signPos != null) {
			RegistryClient.sendPacketToServer(new PacketUpdateRailwaySignConfig(signPos, selectedIds, signIds));
		}
	}

	private void createMainComponents(UIContainer container, int columns, int signWidthUnits, SignPreviewComponent signPreviewComponent, TextInputComponent searchComponent, ObjectArrayList<SignResource> signResources) {
		final int slotBackgroundExtraPadding = ScrollPanelComponent.SCROLL_BAR_WIDTH + 2;
		final SlotBackgroundComponent slotBackgroundComponent = (SlotBackgroundComponent) new SlotBackgroundComponent()
				.setChildOf(container)
				.setX(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new AdditiveConstraint(new ScaleConstraint(
						new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING + SignButtonsComponent.PADDING * SIGN_COLUMNS * 2 + slotBackgroundExtraPadding * 2)),
						(float) (columns * signWidthUnits) / (SMALL_SIGN_COLUMNS * SMALL_SIGN_WIDTH_UNITS + LARGE_SIGN_COLUMNS * LARGE_SIGN_WIDTH_UNITS)
				), new PixelConstraint(SignButtonsComponent.PADDING * columns * 2 + slotBackgroundExtraPadding)))
				.setHeight(new RelativeConstraint());

		final ScrollComponent scrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(false)
				.setChildOf(slotBackgroundComponent)
				.setX(new CenterConstraint())
				.setY(new CenterConstraint())
				.setWidth(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(2)))
				.setHeight(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(2)))).contentContainer;

		final SignButtonsComponent signButtonsComponent = (SignButtonsComponent) new SignButtonsComponent(signPos == null ? new BlockPos(0, 0, 0) : signPos, signResources, columns, signWidthUnits)
				.setChildOf(scrollComponent)
				.setWidth(new RelativeConstraint())
				.setHeight(new RelativeConstraint());

		signPreviewComponent.onEdit(signButtonsComponent::setEditingIndex);
		searchComponent.onChange(() -> signButtonsComponent.setSearch(searchComponent.getText()));

		signButtonsComponent.onClick((editingIndex, signResource) -> {
			signIds[editingIndex] = signResource.signId;
			switch (signResource.getSignType()) {
				case EXIT -> UMinecraft.setCurrentScreenObj(createExitListSelectorScreen(editingIndex));
				case PLATFORM -> UMinecraft.setCurrentScreenObj(createPlatformListSelectorScreen(editingIndex));
				case ROUTE -> UMinecraft.setCurrentScreenObj(createRouteListSelectorScreen(editingIndex));
				case STATION -> UMinecraft.setCurrentScreenObj(createStationListSelectorScreen(editingIndex));
			}
		});
	}

	private StationExitListSelectorScreen createExitListSelectorScreen(int editingIndex) {
		final StationExitListSelectorScreen stationExitListSelectorScreen = new StationExitListSelectorScreen(selectedStationExits -> {
			selectedIds[editingIndex].clear();
			selectedStationExits.forEach(selectedStationExit -> selectedIds[editingIndex].add(serializeExit(selectedStationExit.getName())));
		}, this);

		final ObjectArrayList<StationExit> stationExits = SignResource.getStationExits(signPos);
		stationExitListSelectorScreen.setAvailableList(stationExits);
		stationExits.forEach(stationExit -> {
			if (selectedIds[editingIndex].contains(serializeExit(stationExit.getName()))) {
				stationExitListSelectorScreen.selectData(stationExit);
			}
		});

		return stationExitListSelectorScreen;
	}

	private PlatformListSelectorScreen createPlatformListSelectorScreen(int editingIndex) {
		final PlatformListSelectorScreen platformListSelectorScreen = new PlatformListSelectorScreen(selectedPlatforms -> {
			selectedIds[editingIndex].clear();
			selectedPlatforms.forEach(selectedPlatform -> selectedIds[editingIndex].add(selectedPlatform.getId()));
		}, this);

		final ObjectArrayList<Platform> platforms = SignResource.getPlatforms(signPos);
		platformListSelectorScreen.setAvailableList(platforms);
		platforms.forEach(platform -> {
			if (selectedIds[editingIndex].contains(platform.getId())) {
				platformListSelectorScreen.selectData(platform);
			}
		});

		return platformListSelectorScreen;
	}

	private RouteListSelectorScreen createRouteListSelectorScreen(int editingIndex) {
		final RouteListSelectorScreen routeListSelectorScreen = new RouteListSelectorScreen(selectedRoutes -> {
			selectedIds[editingIndex].clear();
			selectedRoutes.forEach(selectedRoute -> selectedIds[editingIndex].add(selectedRoute.getColor()));
		}, this);

		final ObjectArrayList<Route> routes = SignResource.getRoutes(signPos);
		routeListSelectorScreen.setAvailableList(routes);
		routes.forEach(route -> {
			if (selectedIds[editingIndex].contains(route.getColor())) {
				routeListSelectorScreen.selectData(route);
			}
		});

		return routeListSelectorScreen;
	}

	private StationListSelectorScreen createStationListSelectorScreen(int editingIndex) {
		final StationListSelectorScreen stationListSelectorScreen = new StationListSelectorScreen(selectedStations -> {
			selectedIds[editingIndex].clear();
			selectedStations.forEach(selectedStation -> selectedIds[editingIndex].add(selectedStation.getId()));
		}, this);

		final ObjectArrayList<Station> stations = SignResource.getStations(signPos);
		stationListSelectorScreen.setAvailableList(stations);
		stations.forEach(checkStation -> {
			if (selectedIds[editingIndex].contains(checkStation.getId())) {
				stationListSelectorScreen.selectData(checkStation);
			}
		});

		return stationListSelectorScreen;
	}

	public static long serializeExit(String exitName) {
		final char[] characters = exitName.toCharArray();
		long code = 0;
		for (final char character : characters) {
			code = code << 8;
			code += character;
		}
		return code;
	}
}
