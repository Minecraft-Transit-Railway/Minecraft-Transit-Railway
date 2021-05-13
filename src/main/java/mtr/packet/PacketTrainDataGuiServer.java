package mtr.packet;

import mtr.block.BlockRailwaySign;
import mtr.block.BlockRouteSignBase;
import mtr.data.*;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class PacketTrainDataGuiServer extends PacketTrainDataBase {

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

	public static void openTicketMachineScreenS2C(World world, ServerPlayerEntity player) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(TicketSystem.getPlayerScore(world, player, TicketSystem.BALANCE_OBJECTIVE).getScore());
		ServerPlayNetworking.send(player, PACKET_OPEN_TICKET_MACHINE_SCREEN, packet);
	}

	public static void createRailS2C(World world, BlockPos pos1, BlockPos pos2, Rail rail1, Rail rail2) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		rail1.writePacket(packet);
		rail2.writePacket(packet);
		world.getPlayers().forEach(worldPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_CREATE_RAIL, packet));
	}

	public static void removeNodeS2C(World world, BlockPos pos) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(pos);
		world.getPlayers().forEach(worldPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_REMOVE_NODE, packet));
	}

	public static void removeRailConnectionS2C(World world, BlockPos pos1, BlockPos pos2) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		world.getPlayers().forEach(worldPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_REMOVE_RAIL, packet));
	}

	public static void sendAllInChunks(ServerPlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<Depot> depots, Set<Rail.RailEntry> rails) {
		final long tempPacketId = new Random().nextLong();
		final PacketByteBuf packet = PacketByteBufs.create();
		serializeData(packet, stations);
		serializeData(packet, platforms);
		serializeData(packet, routes);
		serializeData(packet, depots);
		serializeData(packet, rails);
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

		try {
			UpdateBlueMap.updateBlueMap(player.world);
		} catch (NoClassDefFoundError ignored) {
			System.out.println("BlueMap is not loaded");
		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public static void receiveUpdateOrDeleteDepot(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet, boolean isDelete) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(player.world);
		if (railwayData == null) {
			return;
		}

		if (isDelete) {
			deleteData(railwayData.getDepots(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_DELETE_DEPOT, fullPacket);
					}
				});
				railwayData.markDirty();
			});
		} else {
			updateData(railwayData.getDepots(), minecraftServer, packet, (updatePacket, fullPacket) -> {
				world.getPlayers().forEach(worldPlayer -> {
					if (!worldPlayer.getUuid().equals(player.getUuid())) {
						ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_UPDATE_DEPOT, fullPacket);
					}
				});
				railwayData.markDirty();
			}, Depot::new);
		}
	}

	public static void receiveGenerateAllRoutes(MinecraftServer minecraftServer, ServerPlayerEntity player) {
		final RailwayData railwayData = RailwayData.getInstance(player.world);
		if (railwayData == null) {
			return;
		}

		minecraftServer.execute(railwayData::addAllRoutesToGenerate);
	}

	public static void receiveSignIdsC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final BlockPos signPos = packet.readBlockPos();
		final int selectedIdsLength = packet.readInt();
		final Set<Long> selectedIds = new HashSet<>();
		for (int i = 0; i < selectedIdsLength; i++) {
			selectedIds.add(packet.readLong());
		}
		final int signLength = packet.readInt();
		final String[] signIds = new String[signLength];
		for (int i = 0; i < signLength; i++) {
			final String signId = packet.readString(SerializedDataBase.PACKET_STRING_READ_LENGTH);
			signIds[i] = signId.isEmpty() ? null : signId;
		}

		minecraftServer.execute(() -> {
			final BlockEntity entity = player.world.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.TileEntityRailwaySign) {
				((BlockRailwaySign.TileEntityRailwaySign) entity).setData(selectedIds, signIds);
			} else if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
				((BlockRouteSignBase.TileEntityRouteSignBase) entity).setPlatformId(selectedIds.size() == 0 ? 0 : (long) selectedIds.toArray()[0]);
			}
		});
	}

	public static void receiveAddBalanceC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final int addAmount = packet.readInt();
		final int emeralds = packet.readInt();

		minecraftServer.execute(() -> {
			final World world = player.world;

			TicketSystem.addObjectivesIfMissing(world);
			ScoreboardPlayerScore balanceScore = TicketSystem.getPlayerScore(world, player, TicketSystem.BALANCE_OBJECTIVE);
			balanceScore.setScore(balanceScore.getScore() + addAmount);

			Inventories.remove(player.inventory, itemStack -> itemStack.getItem() == Items.EMERALD, emeralds, false);
			world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 1);
		});
	}

	public static void handleResponseFromReceiver(ServerPlayerEntity player, PacketByteBuf packet) {
		final long tempPacketId = packet.readLong();
		final int chunk = packet.readInt();
		sendChunk(player, tempPacketId, chunk);
	}

	private static <T extends SerializedDataBase> void serializeData(PacketByteBuf packet, Set<T> objects) {
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
}
