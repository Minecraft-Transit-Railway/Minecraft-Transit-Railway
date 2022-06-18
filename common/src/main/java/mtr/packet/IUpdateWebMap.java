package mtr.packet;

import mtr.MTR;
import mtr.data.AreaBase;
import mtr.data.IGui;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Tuple;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.function.Consumer;

public interface IUpdateWebMap {

	String MARKER_SET_STATIONS_ID = "mtr_stations";
	String MARKER_SET_STATION_AREAS_ID = "mtr_station_areas";
	String MARKER_SET_STATIONS_TITLE = "Stations";
	String MARKER_SET_STATION_AREAS_TITLE = "Station Areas";
	String MARKER_SET_DEPOTS_ID = "mtr_depots";
	String MARKER_SET_DEPOT_AREAS_ID = "mtr_depot_areas";
	String MARKER_SET_DEPOTS_TITLE = "Depots";
	String MARKER_SET_DEPOT_AREAS_TITLE = "Depot Areas";
	String STATION_ICON_PATH = "/assets/mtr/textures/sign/logo.png";
	String DEPOT_ICON_PATH = "/assets/mtr/textures/sign/logo_grayscale.png";
	String STATION_ICON_KEY = "mtr_station";
	String DEPOT_ICON_KEY = "mtr_depot";
	int ICON_SIZE = 24;

	static void readResource(String path, Consumer<InputStream> callback) {
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

	static <T extends AreaBase> void iterateAreas(Set<T> areas, AreaCallback areaCallback) {
		areas.forEach(area -> {
			final Tuple<Integer, Integer> corner1 = area.corner1;
			final Tuple<Integer, Integer> corner2 = area.corner2;
			final BlockPos areaPos = area.getCenter();
			if (corner1 != null && corner2 != null && areaPos != null) {
				areaCallback.areaCallback(area.id + "_" + System.currentTimeMillis(), IGui.formatStationName(area.name), new Color(area.color), corner1.getA(), corner1.getB(), corner2.getA(), corner2.getB(), areaPos.getX(), areaPos.getZ());
			}
		});
	}

	@FunctionalInterface
	interface AreaCallback {
		void areaCallback(String id, String name, Color color, int areaCorner1X, int areaCorner1Z, int areaCorner2X, int areaCorner2Z, int areaX, int areaZ);
	}
}
