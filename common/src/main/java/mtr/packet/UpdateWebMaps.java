package mtr.packet;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.ShapeMarker;
import mtr.data.AreaBase;
import mtr.data.Depot;
import mtr.data.IGui;
import mtr.data.RailwayData;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.Level;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerIcon;
import xyz.jpenilla.squaremap.api.Point;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UpdateWebMaps implements IGui {

	private static DynmapCommonAPI dynmapCommonAPI;
	private static final String MARKER_SET_STATIONS_ID = "mtr_stations";
	private static final String MARKER_SET_STATION_AREAS_ID = "mtr_station_areas";
	private static final String MARKER_SET_STATIONS_TITLE = "Stations";
	private static final String MARKER_SET_STATION_AREAS_TITLE = "Station Areas";
	private static final String MARKER_SET_DEPOTS_ID = "mtr_depots";
	private static final String MARKER_SET_DEPOT_AREAS_ID = "mtr_depot_areas";
	private static final String MARKER_SET_DEPOTS_TITLE = "Depots";
	private static final String MARKER_SET_DEPOT_AREAS_TITLE = "Depot Areas";

	static {
		try {
			DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {

				@Override
				public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
					UpdateWebMaps.dynmapCommonAPI = dynmapCommonAPI;
				}
			});
		} catch (NoClassDefFoundError ignored) {
			System.out.println("Dynmap is not loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateDynmap(Level world, RailwayData railwayData) {
		try {
			updateDynmap(world, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE);
			updateDynmap(world, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateBlueMap(Level world, RailwayData railwayData) {
		try {
			updateBlueMap(world, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE);
			updateBlueMap(world, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE);
		} catch (NoClassDefFoundError ignored) {
			System.out.println("BlueMap is not loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateSquaremap(Level world, RailwayData railwayData) {
		try {
			updateSquaremap(world, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE);
			updateSquaremap(world, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE);
		} catch (NoClassDefFoundError ignored) {
			System.out.println("Squaremap is not loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static <T extends AreaBase> void updateDynmap(Level world, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle) {
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
			markerSetAreas = tempMarkerSetAreas == null ? markerAPI.createMarkerSet(areasId, areasTitle, new HashSet<>(), false) : tempMarkerSetAreas;
			markerSetAreas.getMarkers().clear();
			markerSetAreas.setHideByDefault(true);

			final org.dynmap.markers.MarkerSet markerSetAreaAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreaAreas = markerAPI.getMarkerSet(areaAreasId);
			markerSetAreaAreas = tempMarkerSetAreaAreas == null ? markerAPI.createMarkerSet(areaAreasId, areaAreasTitle, new HashSet<>(), false) : tempMarkerSetAreaAreas;
			markerSetAreaAreas.getMarkers().clear();

			final MarkerIcon markerIcon = markerAPI.getMarkerIcons().stream().filter(markerIcon1 -> markerIcon1.getMarkerIconID().equals(areas.stream().findFirst().orElse(null) instanceof Depot ? "factory" : "building")).findFirst().orElse(null);
			markerSetAreas.addAllowedMarkerIcon(markerIcon);

			iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
				markerSetAreas.createMarker(id, name, worldId, areaX, areaY, areaZ, markerIcon, false);
				final AreaMarker areaMarker = markerSetAreaAreas.createAreaMarker(id, name, false, worldId, new double[]{areaCorner1X, areaCorner2X}, new double[]{areaCorner1Z, areaCorner2Z}, false);
				areaMarker.setFillStyle(0.5, color.getRGB() & RGB_WHITE);
				areaMarker.setLineStyle(1, 1, color.darker().getRGB() & RGB_WHITE);
			});
		}
	}

	private static <T extends AreaBase> void updateBlueMap(Level world, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle) throws IOException {
		final BlueMapAPI api = BlueMapAPI.getInstance().orElse(null);
		if (api == null) {
			return;
		}

		final BlueMapMap map = api.getMaps().stream().filter(map1 -> world.dimension().location().getPath().contains(map1.getId())).findFirst().orElse(null);
		if (map == null) {
			return;
		}

		final MarkerAPI markerApi = api.getMarkerAPI();
		final int areaY = world.getSeaLevel();

		final MarkerSet markerSetAreas = markerApi.createMarkerSet(areasId);
		markerSetAreas.setLabel(areasTitle);
		markerSetAreas.setDefaultHidden(true);
		markerSetAreas.getMarkers().forEach(markerSetAreas::removeMarker);

		final MarkerSet markerSetAreaAreas = markerApi.createMarkerSet(areaAreasId);
		markerSetAreaAreas.setLabel(areaAreasTitle);
		markerSetAreaAreas.getMarkers().forEach(markerSetAreaAreas::removeMarker);

		iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
			final ShapeMarker markerArea = markerSetAreas.createShapeMarker(id, map, areaX, areaY, areaZ, Shape.createCircle(areaX, areaZ, 4, 32), areaY);
			markerArea.setLabel(name);
			markerArea.setFillColor(color);
			markerArea.setLineColor(color.darker());
			final ShapeMarker markerAreaArea = markerSetAreaAreas.createShapeMarker(id, map, areaX, areaY, areaZ, Shape.createRect(areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z), areaY);
			markerAreaArea.setLabel(name);
			markerAreaArea.setFillColor(new Color((color.getRGB() & RGB_WHITE) | ARGB_BLACK_TRANSLUCENT, true));
			markerAreaArea.setLineColor(color.darker());
		});

		markerApi.save();
	}

	private static <T extends AreaBase> void updateSquaremap(Level world, Set<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle) {
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
			providerAreas = SimpleLayerProvider.builder(areasTitle).showControls(true).defaultHidden(true).build();
			layerRegistry.register(Key.of(areasId), providerAreas);
		}

		final SimpleLayerProvider providerAreaAreas;
		if (layerRegistry.hasEntry(Key.of(areaAreasId))) {
			providerAreaAreas = (SimpleLayerProvider) layerRegistry.get(Key.of(areaAreasId));
			providerAreaAreas.clearMarkers();
		} else {
			providerAreaAreas = SimpleLayerProvider.builder(areaAreasTitle).showControls(true).build();
			layerRegistry.register(Key.of(areaAreasId), providerAreaAreas);
		}

		iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
			final MarkerOptions markerOptions = MarkerOptions.builder().hoverTooltip(name).fillColor(color).strokeColor(color.darker()).build();
			providerAreas.addMarker(Key.of(id), Marker.circle(Point.of(areaX, areaZ), 4).markerOptions(markerOptions));
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

	@FunctionalInterface
	private interface AreaCallback {
		void areaCallback(String id, String name, Color color, int areaCorner1X, int areaCorner1Z, int areaCorner2X, int areaCorner2Z, int areaX, int areaZ);
	}
}
