package mtr.packet;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.*;
import mtr.data.AreaBase;
import mtr.data.IGui;
import mtr.data.RailwayData;
import net.minecraft.world.level.Level;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.function.Consumer;

public class UpdateBlueMap implements IGui, IUpdateWebMap {

	private static String bluemapStationIcon = "";
	private static String bluemapDepotIcon = "";

	static {
		try {
			BlueMapAPI.getInstance().ifPresent(api -> {
				IUpdateWebMap.readResource(STATION_ICON_PATH, inputStream -> resizeImage(inputStream, bufferedImage -> {
					try {
						bluemapStationIcon = api.createImage(bufferedImage, STATION_ICON_KEY + "_bluemap");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
				IUpdateWebMap.readResource(DEPOT_ICON_PATH, inputStream -> resizeImage(inputStream, bufferedImage -> {
					try {
						bluemapDepotIcon = api.createImage(bufferedImage, DEPOT_ICON_KEY + "_bluemap");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}));
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void updateBlueMap(Level world, RailwayData railwayData) {
		try {
			updateBlueMap(world, railwayData.stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, bluemapStationIcon);
			updateBlueMap(world, railwayData.depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, bluemapDepotIcon);
		} catch (Exception e) {
			e.printStackTrace();
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

		IUpdateWebMap.iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
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
}
