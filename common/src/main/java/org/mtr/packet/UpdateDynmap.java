package org.mtr.packet;

import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.AreaMarker;
import org.mtr.MTR;
import org.mtr.client.MinecraftClientData;
import org.mtr.core.data.AreaBase;
import org.mtr.core.data.SavedRailBase;
import org.mtr.data.IGui;

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
						MTR.LOGGER.error("", e);
					}
				}
			});
		} catch (
				Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	public static void updateDynmap(World world) {
		try {
			updateDynmap(world, MinecraftClientData.getInstance().stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, STATION_ICON_KEY);
			updateDynmap(world, MinecraftClientData.getInstance().depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, DEPOT_ICON_KEY);
		} catch (Exception e) {
			MTR.LOGGER.error("", e);
		}
	}

	private static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void updateDynmap(World world, ObjectArraySet<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		if (dynmapCommonAPI != null) {
			final String worldIdString;
			final Identifier worldId = world.getRegistryKey().getValue();
			worldIdString = switch (worldId.toString()) {
				case "minecraft:overworld" -> {
					final MinecraftServer minecraftServer = world.getServer();
					yield minecraftServer == null ? "world" : minecraftServer.getSaveProperties().getLevelName();
				}
				case "minecraft:the_nether" -> "DIM-1";
				case "minecraft:the_end" -> "DIM1";
				default -> worldId.getPath();
			};

			final int areaY = world.getSeaLevel();
			final org.dynmap.markers.MarkerAPI markerAPI = dynmapCommonAPI.getMarkerAPI();

			final org.dynmap.markers.MarkerSet markerSetAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreas = markerAPI.getMarkerSet(areasId);
			markerSetAreas = tempMarkerSetAreas == null ? markerAPI.createMarkerSet(areasId, areasTitle, ObjectSet.of(markerAPI.getMarkerIcon(iconKey)), false) : tempMarkerSetAreas;
			markerSetAreas.getMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldIdString)) {
					marker.deleteMarker();
				}
			});

			final org.dynmap.markers.MarkerSet markerSetAreaAreas;
			org.dynmap.markers.MarkerSet tempMarkerSetAreaAreas = markerAPI.getMarkerSet(areaAreasId);
			markerSetAreaAreas = tempMarkerSetAreaAreas == null ? markerAPI.createMarkerSet(areaAreasId, areaAreasTitle, new ObjectArraySet<>(), false) : tempMarkerSetAreaAreas;
			markerSetAreaAreas.setHideByDefault(true);
			markerSetAreaAreas.getAreaMarkers().forEach(marker -> {
				if (marker.getMarkerID().startsWith(worldIdString)) {
					marker.deleteMarker();
				}
			});

			IUpdateWebMap.iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
				markerSetAreas.createMarker(worldIdString + id, name, worldIdString, areaX, areaY, areaZ, markerAPI.getMarkerIcon(iconKey), false);
				final AreaMarker areaMarker = markerSetAreaAreas.createAreaMarker(worldIdString + id, name, false, worldIdString, new double[]{areaCorner1X, areaCorner2X}, new double[]{areaCorner1Z, areaCorner2Z}, false);
				areaMarker.setFillStyle(0.5, color.getRGB() & RGB_WHITE);
				areaMarker.setLineStyle(1, 1, color.darker().getRGB() & RGB_WHITE);
			});
		}
	}
}
