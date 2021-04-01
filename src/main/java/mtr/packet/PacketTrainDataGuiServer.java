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
import java.util.*;

public class PacketTrainDataGuiServer extends PacketTrainDataBase {

	private static final String BLUE_MAP_MARKER_SET_STATIONS_ID = "mtr_stations";
	private static final String BLUE_MAP_MARKER_SET_STATIONS_TITLE = "Minecraft Transit Railway Stations";

	private static final int PACKET_CHUNK_SIZE = (int) Math.pow(2, 14); // 16384
	private static final Map<Long, PacketByteBuf> TEMP_PACKETS_SENDER = new HashMap<>();

	public static void openDashboardScreenS2C(ServerPlayerEntity player) {
		final PacketByteBuf packet = PacketByteBufs.create();
		ServerPlayNetworking.send(player, PACKET_OPEN_DASHBOARD_SCREEN, packet);
	}

	public static void openRailwaySignScreenS2C(ServerPlayerEntity player, BlockPos signPos) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(signPos);
		ServerPlayNetworking.send(player, PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet);
	}

	public static void sendAllInChunks(ServerPlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		final long tempPacketId = new Random().nextLong();
		final PacketByteBuf packet = PacketByteBufs.create();
		serializeData(packet, stations);
		serializeData(packet, platforms);
		serializeData(packet, routes);
		TEMP_PACKETS_SENDER.put(tempPacketId, packet);
		sendChunk(player, tempPacketId, 0);
	}

	public static void receiveUpdateOrDeleteStation(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet, boolean isDelete) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(player.world);
		if (railwayData == null) {
			return;
		}

		if (isDelete) {
			deleteData(railwayData.getStations(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_DELETE_STATION, fullPacket);
					}
				});
				railwayData.markDirty();
			});
		} else {
			updateData(railwayData.getStations(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_UPDATE_STATION, fullPacket);
					}
				});
				railwayData.markDirty();
			}, Station::new);
		}

		updateBlueMap(player.world);
	}

	public static void receiveUpdateOrDeletePlatform(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet, boolean isDelete) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(player.world);
		if (railwayData == null) {
			return;
		}

		if (isDelete) {
			deleteData(railwayData.getPlatforms(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_DELETE_PLATFORM, fullPacket);
					}
				});
				railwayData.markDirty();
			});
		} else {
			updateData(railwayData.getPlatforms(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_UPDATE_PLATFORM, fullPacket);
					}
				});
				railwayData.markDirty();
			}, null);
		}
	}

	public static void receiveUpdateOrDeleteRoute(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet, boolean isDelete) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		if (isDelete) {
			deleteData(railwayData.getRoutes(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_DELETE_ROUTE, fullPacket);
					}
				});
				railwayData.markDirty();
			});
		} else {
			updateData(railwayData.getRoutes(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_UPDATE_ROUTE, fullPacket);
					}
				});
				railwayData.markDirty();
			}, Route::new);
		}
	}

	public static void receiveGenerateRoute(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final RailwayData railwayData = RailwayData.getInstance(player.world);
		if (railwayData == null) {
			return;
		}

		final long id = packet.readLong();
		minecraftServer.execute(() -> railwayData.addRouteToGenerate(id));
	}

	public static void receiveGenerateAllRoutes(MinecraftServer minecraftServer, ServerPlayerEntity player) {
		final RailwayData railwayData = RailwayData.getInstance(player.world);
		if (railwayData == null) {
			return;
		}

		minecraftServer.execute(railwayData::addAllRoutesToGenerate);
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

	public static void handleResponseFromReceiver(ServerPlayerEntity player, PacketByteBuf packet) {
		final long tempPacketId = packet.readLong();
		final int chunk = packet.readInt();
		sendChunk(player, tempPacketId, chunk);
	}

	private static <T extends NameColorDataBase> void serializeData(PacketByteBuf packet, Set<T> objects) {
		packet.writeInt(objects.size());
		objects.forEach(object -> object.writePacket(packet));
	}

	private static void sendChunk(ServerPlayerEntity player, long tempPacketId, int chunk) {
		final PacketByteBuf packetChunk = PacketByteBufs.create();
		packetChunk.writeLong(tempPacketId);
		packetChunk.writeInt(chunk);

		final PacketByteBuf tempPacket = TEMP_PACKETS_SENDER.get(tempPacketId);
		if (chunk * PACKET_CHUNK_SIZE > tempPacket.readableBytes()) {
			TEMP_PACKETS_SENDER.remove(tempPacketId);
			packetChunk.writeBoolean(true);
		} else {
			packetChunk.writeBoolean(false);
			packetChunk.writeBytes(tempPacket.copy(chunk * PACKET_CHUNK_SIZE, Math.min(PACKET_CHUNK_SIZE, tempPacket.readableBytes() - chunk * PACKET_CHUNK_SIZE)));
		}

		try {
			ServerPlayNetworking.send(player, PACKET_CHUNK_S2C, packetChunk);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void updateBlueMap(World world) {
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

		try {
			final Set<Station> stations = railwayData.getStations();
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
