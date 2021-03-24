package mtr.packet;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.marker.MarkerAPI;
import de.bluecolored.bluemap.api.marker.MarkerSet;
import de.bluecolored.bluemap.api.marker.Shape;
import de.bluecolored.bluemap.api.marker.ShapeMarker;
import mtr.block.BlockRailwaySign;
import mtr.block.BlockRouteSignBase;
import mtr.data.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.awt.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PacketTrainDataGuiServer extends PacketTrainDataBase {

	private static final String BLUE_MAP_MARKER_SET_STATIONS_ID = "mtr_stations";
	private static final String BLUE_MAP_MARKER_SET_STATIONS_TITLE = "Minecraft Transit Railway Stations";

	public static void openDashboardScreenS2C(ServerPlayerEntity player) {
		final PacketByteBuf packet = PacketByteBufs.create();
		ServerPlayNetworking.send(player, PACKET_OPEN_DASHBOARD_SCREEN, packet);
	}

	public static void openRailwaySignScreenS2C(ServerPlayerEntity player, BlockPos signPos) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(signPos);
		ServerPlayNetworking.send(player, PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet);
	}

	public static void receiveAllC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final Set<Station> stations = deserializeData(packet, Station::new);
			final Set<Platform> platforms = deserializeData(packet, Platform::new);
			final Set<Route> routes = deserializeData(packet, Route::new);
			minecraftServer.execute(() -> {
				railwayData.setData(stations, platforms, routes);
				try {
					updateBlueMap(world, stations);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		}
	}

	public static void receiveSignTypesC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final BlockPos signPos = packet.readBlockPos();
		final int selectedIdsLength = packet.readInt();
		final Set<Long> selectedIds = new HashSet<>();
		for (int i = 0; i < selectedIdsLength; i++) {
			selectedIds.add(packet.readLong());
		}
		final int signLength = packet.readInt();
		final BlockRailwaySign.SignType[] signTypes = new BlockRailwaySign.SignType[signLength];
		for (int i = 0; i < signLength; i++) {
			try {
				signTypes[i] = BlockRailwaySign.SignType.valueOf(packet.readString(SerializedDataBase.PACKET_STRING_READ_LENGTH));
			} catch (Exception e) {
				signTypes[i] = null;
			}
		}

		minecraftServer.execute(() -> {
			final BlockEntity entity = player.world.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.TileEntityRailwaySign) {
				((BlockRailwaySign.TileEntityRailwaySign) entity).setData(selectedIds, signTypes);
			} else if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
				((BlockRouteSignBase.TileEntityRouteSignBase) entity).setPlatformId(selectedIds.size() == 0 ? 0 : (long) selectedIds.toArray()[0]);
			}
		});
	}

	private static void updateBlueMap(World world, Set<Station> stations) throws IOException {
		final BlueMapAPI api = BlueMapAPI.getInstance().orElse(null);
		if (api == null) {
			return;
		}

		final BlueMapMap map = api.getMaps().stream().filter(map1 -> world.getRegistryKey().getValue().getPath().contains(map1.getId())).findFirst().orElse(null);
		if (map == null) {
			return;
		}

		final MarkerAPI markerApi = api.getMarkerAPI();

		final MarkerSet markerSetStations = markerApi.createMarkerSet(BLUE_MAP_MARKER_SET_STATIONS_ID);
		markerSetStations.setLabel(BLUE_MAP_MARKER_SET_STATIONS_TITLE);
		markerSetStations.getMarkers().forEach(markerSetStations::removeMarker);
		final int stationY = world.getSeaLevel();

		stations.forEach(station -> {
			final BlockPos stationPos = station.getCenter();
			final int stationX = stationPos.getX();
			final int stationZ = stationPos.getZ();
			final ShapeMarker marker = markerSetStations.createShapeMarker(String.valueOf(station.id), map, stationX, stationY, stationZ, Shape.createCircle(stationX, stationZ, 4, 32), stationY);
			marker.setLabel(station.name.replace("|", "\n"));
			final Color stationColor = new Color(station.color);
			marker.setFillColor(stationColor);
			marker.setBorderColor(stationColor.darker());
		});

		markerApi.save();
	}
}
