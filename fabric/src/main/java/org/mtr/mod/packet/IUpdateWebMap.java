package org.mtr.mod.packet;

import org.mtr.core.data.AreaBase;
import org.mtr.core.data.Position;
import org.mtr.core.data.SavedRailBase;
import org.mtr.init.MTR;
import org.mtr.mod.Init;
import org.mtr.mod.data.IGui;

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
	String STATION_ICON_PATH = "/assets/mtr/textures/block/sign/logo.png";
	String DEPOT_ICON_PATH = "/assets/mtr/textures/block/sign/logo_grayscale.png";
	String STATION_ICON_KEY = "mtr_station";
	String DEPOT_ICON_KEY = "mtr_depot";
	int ICON_SIZE = 24;

	static void readResource(String path, Consumer<InputStream> callback) {
		try (final InputStream inputStream = MTR.class.getResourceAsStream(path)) {
			if (inputStream != null) {
				callback.accept(inputStream);
			}
		} catch (IOException e) {
			Init.LOGGER.error("", e);
		}
	}

	static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void iterateAreas(Set<T> areas, AreaCallback areaCallback) {
		areas.forEach(area -> {
			final int x1 = (int) area.getMinX();
			final int z1 = (int) area.getMinZ();
			final int x2 = (int) area.getMaxX();
			final int z2 = (int) area.getMaxZ();
			final Position center = area.getCenter();
			if (center != null) {
				areaCallback.areaCallback(area.getHexId() + "_" + System.currentTimeMillis(), IGui.formatStationName(area.getName()), new Color(area.getColor()), x1, z1, x2, z2, (int) center.getX(), (int) center.getZ());
			}
		});
	}

	@FunctionalInterface
	interface AreaCallback {
		void areaCallback(String id, String name, Color color, int areaCorner1X, int areaCorner1Z, int areaCorner2X, int areaCorner2Z, int areaX, int areaZ);
	}
}
