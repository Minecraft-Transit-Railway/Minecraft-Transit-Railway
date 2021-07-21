package mtr.packet;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.ShapeMarker;
import mtr.data.RailwayData;
import mtr.data.Station;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.io.IOException;
import java.util.Set;

public class UpdateBlueMap {

	private static final String BLUE_MAP_MARKER_SET_STATIONS_ID = "mtr_stations";
	private static final String BLUE_MAP_MARKER_SET_STATIONS_TITLE = "Minecraft Transit Railway Stations";

	public static void updateBlueMap(World world) throws IOException {
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		final BlueMapAPI api = BlueMapAPI.getInstance().orElse(null);
		if (api == null) {
			return;
		}

		final BlueMapMap map = api.getMaps().stream().filter(map1 -> world.getRegistryKey().getValue().getPath().contains(map1.getId())).findFirst().orElse(null);
		if (map == null) {
			return;
		}

		final Set<Station> stations = railwayData.stations;
		final MarkerAPI markerApi = api.getMarkerAPI();

		final MarkerSet markerSetStations = markerApi.createMarkerSet(BLUE_MAP_MARKER_SET_STATIONS_ID);
		markerSetStations.setLabel(BLUE_MAP_MARKER_SET_STATIONS_TITLE);
		markerSetStations.getMarkers().forEach(markerSetStations::removeMarker);
		final int stationY = world.getSeaLevel();

		stations.forEach(station -> {
			final BlockPos stationPos = station.getCenter();
			if (stationPos != null) {
				final int stationX = stationPos.getX();
				final int stationZ = stationPos.getZ();
				final ShapeMarker marker = markerSetStations.createShapeMarker(String.valueOf(station.id), map, stationX, stationY, stationZ, Shape.createCircle(stationX, stationZ, 4, 32), stationY);
				marker.setLabel(station.name.replace("|", "\n"));
				final Color stationColor = new Color(station.color);
				marker.setFillColor(stationColor);
				marker.setBorderColor(stationColor.darker());
			}
		});

		markerApi.save();
	}
}
