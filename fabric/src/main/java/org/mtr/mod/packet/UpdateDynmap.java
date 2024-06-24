package org.mtr.mod.packet;

import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.AreaMarker;
import org.mtr.core.data.AreaBase;
import org.mtr.core.data.SavedRailBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectSet;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;

public class UpdateDynmap implements IGui, IUpdateWebMap {

	private static DynmapCommonAPI dynmapCommonAPI;

	static {
		try {
			DynmapCommonAPIListener.register(new DynmapCommonAPIListener() {

				@Override
				public void apiEnabled(DynmapCommonAPI dynmapCommonAPI) {
					UpdateDynmap.dynmapCommonAPI = dynmapCommonAPI;
					try {
						final org.dynmap.markers.MarkerAPI markerAPI = dynmapCommonAPI.getMarkerAPI();
						IUpdateWebMap.readResource(STATION_ICON_PATH, inputStream -> markerAPI.createMarkerIcon(STATION_ICON_KEY, STATION_ICON_KEY, inputStream));
						IUpdateWebMap.readResource(DEPOT_ICON_PATH, inputStream -> markerAPI.createMarkerIcon(DEPOT_ICON_KEY, DEPOT_ICON_KEY, inputStream));
					} catch (Exception e) {
						Init.LOGGER.error("", e);
					}
				}
			});
		} catch (
				Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	public static void updateDynmap(World world) {
		try {
			updateDynmap(world, MinecraftClientData.getInstance().stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, STATION_ICON_KEY);
			updateDynmap(world, MinecraftClientData.getInstance().depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, DEPOT_ICON_KEY);
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	private static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void updateDynmap(World world, ObjectArraySet<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		if (dynmapCommonAPI != null) {
			final String worldId;
			switch (MinecraftServerHelper.getWorldId(world).toString()) {
				case "minecraft:overworld":
					final MinecraftServer minecraftServer = world.getServer();
					worldId = minecraftServer == null ? "world" : minecraftServer.getSaveProperties().getLevelName();
					break;
				case "minecraft:the_nether":
					worldId = "DIM-1";
					break;
				case "minecraft:the_end":
					worldId = "DIM1";
					break;
				default:
					worldId = MinecraftServerHelper.getWorldId(world).getPath();
					break;
			}

			final int areaY = world.getSeaLevel();
			final org.dynmap.markers.MarkerAPI markerAPI = dynmapCommonAPI.getMarkerAPI();

			final org.dynmap.markers.MarkerSet markerSetAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreas = markerAPI.getMarkerSet(areasId);
			markerSetAreas = tempMarkerSetAreas == null ? markerAPI.createMarkerSet(areasId, areasTitle, ObjectSet.of(markerAPI.getMarkerIcon(iconKey)), false) : tempMarkerSetAreas;
			markerSetAreas.getMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldId)) {
					marker.deleteMarker();
				}
			});

			final org.dynmap.markers.MarkerSet markerSetAreaAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreaAreas = markerAPI.getMarkerSet(areaAreasId);
			markerSetAreaAreas = tempMarkerSetAreaAreas == null ? markerAPI.createMarkerSet(areaAreasId, areaAreasTitle, new ObjectArraySet<>(), false) : tempMarkerSetAreaAreas;
			markerSetAreaAreas.setHideByDefault(true);
			markerSetAreaAreas.getAreaMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldId)) {
					marker.deleteMarker();
				}
			});

			IUpdateWebMap.iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
				markerSetAreas.createMarker(worldId + id, name, worldId, areaX, areaY, areaZ, markerAPI.getMarkerIcon(iconKey), false);
				final AreaMarker areaMarker = markerSetAreaAreas.createAreaMarker(worldId + id, name, false, worldId, new double[]{areaCorner1X, areaCorner2X}, new double[]{areaCorner1Z, areaCorner2Z}, false);
				areaMarker.setFillStyle(0.5, color.getRGB() & RGB_WHITE);
				areaMarker.setLineStyle(1, 1, color.darker().getRGB() & RGB_WHITE);
			});
		}
	}
}
