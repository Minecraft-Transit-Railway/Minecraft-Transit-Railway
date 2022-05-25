package mtr.packet;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.*;
import mtr.MTR;
import mtr.data.AreaBase;
import mtr.data.IGui;
import mtr.data.RailwayData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.AreaMarker;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class UpdateWebMaps implements IGui {

	private static DynmapCommonAPI dynmapCommonAPI;
	private static String bluemapStationIcon = "";
	private static String bluemapDepotIcon = "";
	private static final String MARKER_SET_STATIONS_ID = "mtr_stations";
	private static final String MARKER_SET_STATION_AREAS_ID = "mtr_station_areas";
	private static final String MARKER_SET_STATIONS_TITLE = "Stations";
	private static final String MARKER_SET_STATION_AREAS_TITLE = "Station Areas";
	private static final String MARKER_SET_DEPOTS_ID = "mtr_depots";
	private static final String MARKER_SET_DEPOT_AREAS_ID = "mtr_depot_areas";
	private static final String MARKER_SET_DEPOTS_TITLE = "Depots";
	private static final String MARKER_SET_DEPOT_AREAS_TITLE = "Depot Areas";
	private static final String ICON_STATION_KEY = "mtr_station";
	private static final String ICON_DEPOT_KEY = "mtr_depot";
	private static final int ICON_SIZE = 24;

	static {
		final String stationPath = "/assets/mtr/textures/sign/logo.png";
		final String depotPath = "/assets/mtr/textures/sign/logo_grayscale.png";

		try {
			DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {

				@Override
				public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
					UpdateWebMaps.dynmapCommonAPI = dynmapCommonAPI;
					try {
						final org.dynmap.markers.MarkerAPI markerAPI = dynmapCommonAPI.getMarkerAPI();
						readResource(stationPath, inputStream -> markerAPI.createMarkerIcon(ICON_STATION_KEY, ICON_STATION_KEY, inputStream));
						readResource(depotPath, inputStream -> markerAPI.createMarkerIcon(ICON_DEPOT_KEY, ICON_DEPOT_KEY, inputStream));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} catch (NoClassDefFoundError ignored) {
			System.out.println("Dynmap is not loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BlueMapAPI.getInstance().ifPresent(api -> {
				readResource(stationPath, inputStream -> resizeImage(inputStream, bufferedImage -> {
					try {
						bluemapStationIcon = api.createImage(bufferedImage, ICON_STATION_KEY + "_bluemap");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
				readResource(depotPath, inputStream -> resizeImage(inputStream, bufferedImage -> {
					try {
						bluemapDepotIcon = api.createImage(bufferedImage, ICON_DEPOT_KEY + "_bluemap");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
			});
		} catch (NoClassDefFoundError ignored) {
			System.out.println("BlueMap is not loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			final Registry<BufferedImage> iconRegistry = SquaremapProvider.get().iconRegistry();
			readResource(stationPath, inputStream -> {
				try {
					iconRegistry.register(Key.of(ICON_STATION_KEY), ImageIO.read(inputStream));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
			readResource(depotPath, inputStream -> {
				try {
					iconRegistry.register(Key.of(ICON_DEPOT_KEY), ImageIO.read(inputStream));
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		} catch (NoClassDefFoundError | IllegalStateException ignored) {
			System.out.println("Squaremap is not loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateDynmap(Level world, RailwayData railwayData) {
		try {
			updateDynmap(world, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, ICON_STATION_KEY);
			updateDynmap(world, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, ICON_DEPOT_KEY);
		} catch (NoClassDefFoundError ignored) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateBlueMap(Level world, RailwayData railwayData) {
		try {
			updateBlueMap(world, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, bluemapStationIcon);
			updateBlueMap(world, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, bluemapDepotIcon);
		} catch (NoClassDefFoundError ignored) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateSquaremap(Level world, RailwayData railwayData) {
		try {
			updateSquaremap(world, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, ICON_STATION_KEY);
			updateSquaremap(world, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, ICON_DEPOT_KEY);
		} catch (NoClassDefFoundError | IllegalStateException ignored) {
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static <T extends AreaBase> void updateDynmap(Level world, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		if (dynmapCommonAPI != null) {
			final String worldId;
			switch (world.dimension().location().toString()) {
				case "minecraft:overworld":
					final MinecraftServer minecraftServer = world.getServer();
					worldId = minecraftServer == null ? "world" : minecraftServer.getWorldData().getLevelName();
					break;
				case "minecraft:the_nether":
					worldId = "DIM-1";
					break;
				case "minecraft:the_end":
					worldId = "DIM1";
					break;
				default:
					worldId = world.dimension().location().getPath();
					break;
			}

			final int areaY = world.getSeaLevel();
			final org.dynmap.markers.MarkerAPI markerAPI = dynmapCommonAPI.getMarkerAPI();

			final org.dynmap.markers.MarkerSet markerSetAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreas = markerAPI.getMarkerSet(areasId);
			markerSetAreas = tempMarkerSetAreas == null ? markerAPI.createMarkerSet(areasId, areasTitle, Collections.singleton(markerAPI.getMarkerIcon(iconKey)), false) : tempMarkerSetAreas;
			markerSetAreas.getMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldId)) {
					marker.deleteMarker();
				}
			});

			final org.dynmap.markers.MarkerSet markerSetAreaAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreaAreas = markerAPI.getMarkerSet(areaAreasId);
			markerSetAreaAreas = tempMarkerSetAreaAreas == null ? markerAPI.createMarkerSet(areaAreasId, areaAreasTitle, new HashSet<>(), false) : tempMarkerSetAreaAreas;
			markerSetAreaAreas.setHideByDefault(true);
			markerSetAreaAreas.getMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldId)) {
					marker.deleteMarker();
				}
			});

			iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
				markerSetAreas.createMarker(worldId + id, name, worldId, areaX, areaY, areaZ, markerAPI.getMarkerIcon(iconKey), false);
				final AreaMarker areaMarker = markerSetAreaAreas.createAreaMarker(worldId + id, name, false, worldId, new double[]{areaCorner1X, areaCorner2X}, new double[]{areaCorner1Z, areaCorner2Z}, false);
				areaMarker.setFillStyle(0.5, color.getRGB() & RGB_WHITE);
				areaMarker.setLineStyle(1, 1, color.darker().getRGB() & RGB_WHITE);
			});
		}
	}

	private static <T extends AreaBase> void updateBlueMap(Level world, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) throws IOException {
		final BlueMapAPI api = BlueMapAPI.getInstance().orElse(null);
		if (api == null) {
			return;
		}

		final String worldId = world.dimension().location().getPath();
		final BlueMapMap map = api.getMaps().stream().filter(map1 -> worldId.contains(map1.getId())).findFirst().orElse(null);
		if (map == null) {
			return;
		}

		final MarkerAPI markerApi = api.getMarkerAPI();
		final int areaY = world.getSeaLevel();

		final MarkerSet markerSetAreas = markerApi.createMarkerSet(areasId);
		markerSetAreas.setLabel(areasTitle);
		markerSetAreas.getMarkers().forEach(marker -> {
			if (marker.getId().startsWith("1_" + worldId)) {
				markerSetAreas.removeMarker(marker);
			}
		});

		final MarkerSet markerSetAreaAreas = markerApi.createMarkerSet(areaAreasId);
		markerSetAreaAreas.setLabel(areaAreasTitle);
		markerSetAreaAreas.setDefaultHidden(true);
		markerSetAreaAreas.getMarkers().forEach(marker -> {
			if (marker.getId().startsWith("2_" + worldId)) {
				markerSetAreaAreas.removeMarker(marker);
			}
		});

		iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
			final POIMarker markerArea = markerSetAreas.createPOIMarker("1_" + worldId + id, map, areaX, areaY, areaZ);
			markerArea.setLabel(name);
			markerArea.setIcon(iconKey, ICON_SIZE / 2, ICON_SIZE / 2);
			final ShapeMarker markerAreaArea = markerSetAreaAreas.createShapeMarker("2_" + worldId + id, map, areaX, areaY, areaZ, Shape.createRect(areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z), areaY);
			markerAreaArea.setLabel(name);
			markerAreaArea.setFillColor(new Color((color.getRGB() & RGB_WHITE) | ARGB_BLACK_TRANSLUCENT, true));
			markerAreaArea.setLineColor(color.darker());
		});

		markerApi.save();
	}

	private static <T extends AreaBase> void updateSquaremap(Level world, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		final MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(WorldIdentifier.parse(world.dimension().location().toString())).orElse(null);
		if (mapWorld == null) {
			return;
		}

		final Registry<LayerProvider> layerRegistry = mapWorld.layerRegistry();

		final SimpleLayerProvider providerAreas;
		if (layerRegistry.hasEntry(Key.of(areasId))) {
			providerAreas = (SimpleLayerProvider) layerRegistry.get(Key.of(areasId));
			providerAreas.clearMarkers();
		} else {
			providerAreas = SimpleLayerProvider.builder(areasTitle).showControls(true).build();
			layerRegistry.register(Key.of(areasId), providerAreas);
		}

		final SimpleLayerProvider providerAreaAreas;
		if (layerRegistry.hasEntry(Key.of(areaAreasId))) {
			providerAreaAreas = (SimpleLayerProvider) layerRegistry.get(Key.of(areaAreasId));
			providerAreaAreas.clearMarkers();
		} else {
			providerAreaAreas = SimpleLayerProvider.builder(areaAreasTitle).showControls(true).defaultHidden(true).build();
			layerRegistry.register(Key.of(areaAreasId), providerAreaAreas);
		}

		iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
			final MarkerOptions markerOptions = MarkerOptions.builder().hoverTooltip(name).fillColor(color).strokeColor(color.darker()).build();
			providerAreas.addMarker(Key.of(id), Marker.icon(Point.of(areaX, areaZ), Key.of(iconKey), ICON_SIZE).markerOptions(markerOptions));
			providerAreaAreas.addMarker(Key.of(id), Marker.rectangle(Point.of(areaCorner1X, areaCorner1Z), Point.of(areaCorner2X, areaCorner2Z)).markerOptions(markerOptions));
		});
	}

	private static <T extends AreaBase> void iterateAreas(Set<T> areas, AreaCallback areaCallback) {
		areas.forEach(area -> {
			final Tuple<Integer, Integer> corner1 = area.corner1;
			final Tuple<Integer, Integer> corner2 = area.corner2;
			final BlockPos areaPos = area.getCenter();
			if (corner1 != null && corner2 != null && areaPos != null) {
				areaCallback.areaCallback(String.valueOf(area.id), IGui.formatStationName(area.name), new Color(area.color), corner1.getA(), corner1.getB(), corner2.getA(), corner2.getB(), areaPos.getX(), areaPos.getZ());
			}
		});
	}

	private static void readResource(String path, Consumer<InputStream> callback) {
		final InputStream inputStream = MTR.class.getResourceAsStream(path);
		if (inputStream != null) {
			callback.accept(inputStream);
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void resizeImage(InputStream inputStream, Consumer<BufferedImage> callback) {
		try {
			final BufferedImage bufferedImage = ImageIO.read(inputStream);
			final BufferedImage newBufferedImage = new BufferedImage(ICON_SIZE, ICON_SIZE, bufferedImage.getType());
			newBufferedImage.getGraphics().drawImage(bufferedImage.getScaledInstance(ICON_SIZE, ICON_SIZE, Image.SCALE_SMOOTH), 0, 0, null);
			callback.accept(newBufferedImage);
			inputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FunctionalInterface
	private interface AreaCallback {
		void areaCallback(String id, String name, Color color, int areaCorner1X, int areaCorner1Z, int areaCorner2X, int areaCorner2Z, int areaX, int areaZ);
	}
}
