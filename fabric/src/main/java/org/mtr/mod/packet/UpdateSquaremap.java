package org.mtr.mod.packet;

import org.mtr.core.data.AreaBase;
import org.mtr.core.data.SavedRailBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import org.mtr.mapping.holder.World;
import org.mtr.mapping.mapper.MinecraftServerHelper;
import org.mtr.mod.Init;
import org.mtr.mod.client.MinecraftClientData;
import org.mtr.mod.data.IGui;
import xyz.jpenilla.squaremap.api.*;
import xyz.jpenilla.squaremap.api.marker.Marker;
import xyz.jpenilla.squaremap.api.marker.MarkerOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class UpdateSquaremap implements IGui, IUpdateWebMap {

	static {
		try {
			final Registry<BufferedImage> iconRegistry = SquaremapProvider.get().iconRegistry();
			IUpdateWebMap.readResource(STATION_ICON_PATH, inputStream -> {
				try {
					iconRegistry.register(Key.of(STATION_ICON_KEY), ImageIO.read(inputStream));
				} catch (IOException e) {
					Init.LOGGER.error("", e);
				}
			});
			IUpdateWebMap.readResource(DEPOT_ICON_PATH, inputStream -> {
				try {
					iconRegistry.register(Key.of(DEPOT_ICON_KEY), ImageIO.read(inputStream));
				} catch (IOException e) {
					Init.LOGGER.error("", e);
				}
			});
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	public static void updateSquaremap(World world) {
		try {
			updateSquaremap(world, MinecraftClientData.getInstance().stations, MARKER_SET_STATIONS_ID, MARKER_SET_STATIONS_TITLE, MARKER_SET_STATION_AREAS_ID, MARKER_SET_STATION_AREAS_TITLE, STATION_ICON_KEY);
			updateSquaremap(world, MinecraftClientData.getInstance().depots, MARKER_SET_DEPOTS_ID, MARKER_SET_DEPOTS_TITLE, MARKER_SET_DEPOT_AREAS_ID, MARKER_SET_DEPOT_AREAS_TITLE, DEPOT_ICON_KEY);
		} catch (IllegalStateException ignored) {
		} catch (Exception e) {
			Init.LOGGER.error("", e);
		}
	}

	private static <T extends AreaBase<T, U>, U extends SavedRailBase<U, T>> void updateSquaremap(World world, ObjectArraySet<T> areas, String areasId, String areasTitle, String areaAreasId, String areaAreasTitle, String iconKey) {
		final MapWorld mapWorld = SquaremapProvider.get().getWorldIfEnabled(WorldIdentifier.parse(MinecraftServerHelper.getWorldId(world).toString())).orElse(null);
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

		IUpdateWebMap.iterateAreas(areas, (id, name, color, areaCorner1X, areaCorner1Z, areaCorner2X, areaCorner2Z, areaX, areaZ) -> {
			final MarkerOptions markerOptions = MarkerOptions.builder().hoverTooltip(name).fillColor(color).strokeColor(color.darker()).build();
			providerAreas.addMarker(Key.of(id), Marker.icon(Point.of(areaX, areaZ), Key.of(iconKey), ICON_SIZE).markerOptions(markerOptions));
			providerAreaAreas.addMarker(Key.of(id), Marker.rectangle(Point.of(areaCorner1X, areaCorner1Z), Point.of(areaCorner2X, areaCorner2Z)).markerOptions(markerOptions));
		});
	}
}
