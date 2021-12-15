package mtr.packet;

import minecraftmappings.Utilities;
import mtr.block.BlockPIDSBase;
import mtr.block.BlockRailwaySign;
import mtr.block.BlockRouteSignBase;
import mtr.block.BlockTrainSensorBase;
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
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Function;

public class PacketTrainDataGuiServer extends PacketTrainDataBase {

	private static final int PACKET_CHUNK_SIZE = (int) Math.pow(2, 14); // 16384

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

	public static void openTrainSensorScreenS2C(ServerPlayerEntity player, BlockPos blockPos) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(blockPos);
		ServerPlayNetworking.send(player, PACKET_OPEN_TRAIN_SENSOR_SCREEN, packet);
	}

	public static void openPIDSConfigScreenS2C(ServerPlayerEntity player, BlockPos pos1, BlockPos pos2, int maxArrivals) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		packet.writeInt(maxArrivals);
		ServerPlayNetworking.send(player, PACKET_OPEN_PIDS_CONFIG_SCREEN, packet);
	}

	public static void announceS2C(ServerPlayerEntity player, String message) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeString(message);
		ServerPlayNetworking.send(player, PACKET_ANNOUNCE, packet);
	}

	public static void createRailS2C(World world, BlockPos pos1, BlockPos pos2, Rail rail1, Rail rail2, long savedRailId) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		rail1.writePacket(packet);
		rail2.writePacket(packet);
		packet.writeLong(savedRailId);
		world.getPlayers().forEach(worldPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_CREATE_RAIL, packet));
	}

	public static void createSignalS2C(World world, long id, DyeColor dyeColor, UUID rail) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeInt(dyeColor.ordinal());
		packet.writeUuid(rail);
		world.getPlayers().forEach(worldPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_CREATE_SIGNAL, packet));
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

	public static void removeSignalS2C(World world, long id, DyeColor dyeColor, UUID rail) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeInt(1);
		packet.writeLong(id);
		packet.writeInt(dyeColor.ordinal());
		packet.writeUuid(rail);
		world.getPlayers().forEach(worldPlayer -> ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, PACKET_REMOVE_SIGNALS, packet));
	}

	public static void sendAllInChunks(ServerPlayerEntity player, Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots, SignalBlocks signalBlocks) {
		final long tempPacketId = new Random().nextLong();
		final PacketByteBuf packet = PacketByteBufs.create();

		serializeData(packet, stations);
		serializeData(packet, platforms);
		serializeData(packet, sidings);
		serializeData(packet, routes);
		serializeData(packet, depots);
		serializeData(packet, signalBlocks.signalBlocks);

		int i = 0;
		while (!sendChunk(player, packet, tempPacketId, i)) {
			i++;
		}
	}

	public static <T extends NameColorDataBase> void receiveUpdateOrDeleteC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet, Identifier packetId, Function<RailwayData, Set<T>> dataSet, Function<RailwayData, Map<Long, T>> cacheMap, Function<Long, T> createDataWithId, boolean isDelete) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		final PacketCallback packetCallback = (updatePacket, fullPacket) -> world.getPlayers().forEach(worldPlayer -> {
			if (!worldPlayer.getUuid().equals(player.getUuid())) {
				ServerPlayNetworking.send((ServerPlayerEntity) worldPlayer, packetId, fullPacket);
			}
			railwayData.dataCache.sync();
		});

		if (isDelete) {
			deleteData(dataSet.apply(railwayData), minecraftServer, packet, packetCallback);
		} else {
			updateData(dataSet.apply(railwayData), cacheMap.apply(railwayData), minecraftServer, packet, packetCallback, createDataWithId);
		}

		if (packetId.equals(PACKET_UPDATE_STATION) || packetId.equals(PACKET_DELETE_STATION)) {
			try {
				UpdateBlueMap.updateBlueMap(world);
			} catch (NoClassDefFoundError ignored) {
				System.out.println("BlueMap is not loaded");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void generatePathS2C(World world, long depotId, int successfulSegments) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(depotId);
		packet.writeInt(successfulSegments);
		world.getPlayers().forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_GENERATE_PATH, packet));
	}

	public static void generatePathC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final long depotId = packet.readLong();
			minecraftServer.execute(() -> railwayData.generatePath(minecraftServer, depotId));
		}
	}

	public static void clearTrainsC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final World world = player.world;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final int sidingCount = packet.readInt();
			final Set<Long> sidingIds = new HashSet<>();
			for (int i = 0; i < sidingCount; i++) {
				sidingIds.add(packet.readLong());
			}
			minecraftServer.execute(() -> sidingIds.forEach(sidingId -> {
				final Siding siding = railwayData.dataCache.sidingIdMap.get(sidingId);
				if (siding != null) {
					siding.clearTrains();
				}
			}));
		}
	}

	public static void receiveTrainSensorC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		final Set<Long> filterIds = new HashSet<>();
		final int filterLength = packet.readInt();
		for (int i = 0; i < filterLength; i++) {
			filterIds.add(packet.readLong());
		}
		final int number = packet.readInt();
		final String string = packet.readString(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		minecraftServer.execute(() -> {
			final BlockEntity entity = player.world.getBlockEntity(pos);
			if (entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase) {
				((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).setData(filterIds, number, string);
			}
		});
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
				((BlockRouteSignBase.TileEntityRouteSignBase) entity).setPlatformId(selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0]);
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

			Inventories.remove(Utilities.getInventory(player), itemStack -> itemStack.getItem() == Items.EMERALD, emeralds, false);
			world.playSound(null, player.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.BLOCKS, 1, 1);
		});
	}

	public static void receivePIDSMessageC2S(MinecraftServer minecraftServer, ServerPlayerEntity player, PacketByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		final int maxArrivals = packet.readInt();
		final String[] messages = new String[maxArrivals];
		final boolean[] hideArrivals = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = packet.readString(SerializedDataBase.PACKET_STRING_READ_LENGTH);
			hideArrivals[i] = packet.readBoolean();
		}
		minecraftServer.execute(() -> {
			final BlockEntity entity1 = player.world.getBlockEntity(pos1);
			if (entity1 instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				((BlockPIDSBase.TileEntityBlockPIDSBase) entity1).setData(messages, hideArrivals);
			}
			final BlockEntity entity2 = player.world.getBlockEntity(pos2);
			if (entity2 instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				((BlockPIDSBase.TileEntityBlockPIDSBase) entity2).setData(messages, hideArrivals);
			}
		});
	}

	private static <T extends SerializedDataBase> void serializeData(PacketByteBuf packet, Set<T> objects) {
		packet.writeInt(objects.size());
		objects.forEach(object -> object.writePacket(packet));
	}

	private static boolean sendChunk(ServerPlayerEntity player, PacketByteBuf packet, long tempPacketId, int chunk) {
		final PacketByteBuf packetChunk = PacketByteBufs.create();
		packetChunk.writeLong(tempPacketId);
		packetChunk.writeInt(chunk);

		final boolean success = chunk * PACKET_CHUNK_SIZE > packet.readableBytes();
		packetChunk.writeBoolean(success);
		if (!success) {
			packetChunk.writeBytes(packet.copy(chunk * PACKET_CHUNK_SIZE, Math.min(PACKET_CHUNK_SIZE, packet.readableBytes() - chunk * PACKET_CHUNK_SIZE)));
		}

		try {
			ServerPlayNetworking.send(player, PACKET_CHUNK_S2C, packetChunk);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
}
