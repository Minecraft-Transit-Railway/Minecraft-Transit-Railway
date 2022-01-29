package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.block.BlockPIDSBase;
import mtr.block.BlockRailwaySign;
import mtr.block.BlockRouteSignBase;
import mtr.block.BlockTrainSensorBase;
import mtr.data.*;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.scores.Score;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

public class PacketTrainDataGuiServer extends PacketTrainDataBase {

	private static final int PACKET_CHUNK_SIZE = (int) Math.pow(2, 14); // 16384

	public static void openDashboardScreenS2C(ServerPlayer player, TransportMode transportMode) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeUtf(transportMode.toString());
		Registry.sendToPlayer(player, PACKET_OPEN_DASHBOARD_SCREEN, packet);
	}

	public static void openRailwaySignScreenS2C(ServerPlayer player, BlockPos signPos) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(signPos);
		Registry.sendToPlayer(player, PACKET_OPEN_RAILWAY_SIGN_SCREEN, packet);
	}

	public static void openTicketMachineScreenS2C(Level world, ServerPlayer player) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeInt(TicketSystem.getPlayerScore(world, player, TicketSystem.BALANCE_OBJECTIVE).getScore());
		Registry.sendToPlayer(player, PACKET_OPEN_TICKET_MACHINE_SCREEN, packet);
	}

	public static void openTrainSensorScreenS2C(ServerPlayer player, BlockPos blockPos) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(blockPos);
		Registry.sendToPlayer(player, PACKET_OPEN_TRAIN_SENSOR_SCREEN, packet);
	}

	public static void openPIDSConfigScreenS2C(ServerPlayer player, BlockPos pos1, BlockPos pos2, int maxArrivals) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		packet.writeInt(maxArrivals);
		Registry.sendToPlayer(player, PACKET_OPEN_PIDS_CONFIG_SCREEN, packet);
	}

	public static void openResourcePackCreatorScreenS2C(ServerPlayer player) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		Registry.sendToPlayer(player, PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN, packet);
	}

	public static void announceS2C(ServerPlayer player, String message) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeUtf(message);
		Registry.sendToPlayer(player, PACKET_ANNOUNCE, packet);
	}

	public static void createRailS2C(Level world, TransportMode transportMode, BlockPos pos1, BlockPos pos2, Rail rail1, Rail rail2, long savedRailId) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeUtf(transportMode.toString());
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		rail1.writePacket(packet);
		rail2.writePacket(packet);
		packet.writeLong(savedRailId);
		world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_CREATE_RAIL, packet));
	}

	public static void createSignalS2C(Level world, long id, DyeColor dyeColor, UUID rail) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeInt(dyeColor.ordinal());
		packet.writeUUID(rail);
		world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_CREATE_SIGNAL, packet));
	}

	public static void updateRailActionsS2C(Level world, List<Rail.RailActions> railActions) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeInt(railActions.size());
		railActions.forEach(railAction -> railAction.writePacket(packet));
		world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_UPDATE_RAIL_ACTIONS, packet));
	}

	public static void removeNodeS2C(Level world, BlockPos pos) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos);
		world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_NODE, packet));
	}

	public static void removeRailConnectionS2C(Level world, BlockPos pos1, BlockPos pos2) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_RAIL, packet));
	}

	public static void removeSignalS2C(Level world, long id, DyeColor dyeColor, UUID rail) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeInt(1);
		packet.writeLong(id);
		packet.writeInt(dyeColor.ordinal());
		packet.writeUUID(rail);
		world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_SIGNALS, packet));
	}

	public static void sendAllInChunks(ServerPlayer player, Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots, SignalBlocks signalBlocks) {
		final long tempPacketId = new Random().nextLong();
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

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

	public static <T extends NameColorDataBase> void receiveUpdateOrDeleteC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet, ResourceLocation packetId, Function<RailwayData, Set<T>> dataSet, Function<RailwayData, Map<Long, T>> cacheMap, BiFunction<Long, TransportMode, T> createDataWithId, boolean isDelete) {
		final Level world = player.level;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		final PacketCallback packetCallback = (updatePacket, fullPacket) -> world.players().forEach(worldPlayer -> {
			if (!worldPlayer.getUUID().equals(player.getUUID())) {
				Registry.sendToPlayer((ServerPlayer) worldPlayer, packetId, fullPacket);
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

	public static void generatePathS2C(Level world, long depotId, int successfulSegments) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(depotId);
		packet.writeInt(successfulSegments);
		world.players().forEach(player -> Registry.sendToPlayer((ServerPlayer) player, PACKET_GENERATE_PATH, packet));
	}

	public static void generatePathC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final Level world = player.level;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final long depotId = packet.readLong();
			minecraftServer.execute(() -> railwayData.generatePath(minecraftServer, depotId));
		}
	}

	public static void clearTrainsC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final Level world = player.level;
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

	public static void receiveTrainSensorC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		final Set<Long> filterIds = new HashSet<>();
		final int filterLength = packet.readInt();
		for (int i = 0; i < filterLength; i++) {
			filterIds.add(packet.readLong());
		}
		final boolean stoppedOnly = packet.readBoolean();
		final boolean movingOnly = packet.readBoolean();
		final int number = packet.readInt();
		final String string = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		minecraftServer.execute(() -> {
			final BlockEntity entity = player.level.getBlockEntity(pos);
			if (entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase) {
				((BlockTrainSensorBase.TileEntityTrainSensorBase) entity).setData(filterIds, stoppedOnly, movingOnly, number, string);
			}
		});
	}

	public static void receiveSignIdsC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final BlockPos signPos = packet.readBlockPos();
		final int selectedIdsLength = packet.readInt();
		final Set<Long> selectedIds = new HashSet<>();
		for (int i = 0; i < selectedIdsLength; i++) {
			selectedIds.add(packet.readLong());
		}
		final int signLength = packet.readInt();
		final String[] signIds = new String[signLength];
		for (int i = 0; i < signLength; i++) {
			final String signId = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
			signIds[i] = signId.isEmpty() ? null : signId;
		}

		minecraftServer.execute(() -> {
			final BlockEntity entity = player.level.getBlockEntity(signPos);
			if (entity instanceof BlockRailwaySign.TileEntityRailwaySign) {
				((BlockRailwaySign.TileEntityRailwaySign) entity).setData(selectedIds, signIds);
			} else if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
				((BlockRouteSignBase.TileEntityRouteSignBase) entity).setPlatformId(selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0]);
			}
		});
	}

	public static void receiveAddBalanceC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final int addAmount = packet.readInt();
		final int emeralds = packet.readInt();

		minecraftServer.execute(() -> {
			final Level world = player.level;

			TicketSystem.addObjectivesIfMissing(world);
			Score balanceScore = TicketSystem.getPlayerScore(world, player, TicketSystem.BALANCE_OBJECTIVE);
			balanceScore.setScore(balanceScore.getScore() + addAmount);

			ContainerHelper.clearOrCountMatchingItems(Utilities.getInventory(player), itemStack -> itemStack.getItem() == Items.EMERALD, emeralds, false);
			world.playSound(null, player.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 1, 1);
		});
	}

	public static void receivePIDSMessageC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final BlockPos pos1 = packet.readBlockPos();
		final BlockPos pos2 = packet.readBlockPos();
		final int maxArrivals = packet.readInt();
		final String[] messages = new String[maxArrivals];
		final boolean[] hideArrivals = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
			hideArrivals[i] = packet.readBoolean();
		}
		minecraftServer.execute(() -> {
			final BlockEntity entity1 = player.level.getBlockEntity(pos1);
			if (entity1 instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				((BlockPIDSBase.TileEntityBlockPIDSBase) entity1).setData(messages, hideArrivals);
			}
			final BlockEntity entity2 = player.level.getBlockEntity(pos2);
			if (entity2 instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				((BlockPIDSBase.TileEntityBlockPIDSBase) entity2).setData(messages, hideArrivals);
			}
		});
	}

	public static void receiveRemoveRailAction(MinecraftServer minecraftServer, Player player, FriendlyByteBuf packet) {
		final Level world = player.level;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final long id = packet.readLong();
			minecraftServer.execute(() -> railwayData.removeRailAction(id));
		}
	}

	private static <T extends SerializedDataBase> void serializeData(FriendlyByteBuf packet, Set<T> objects) {
		packet.writeInt(objects.size());
		objects.forEach(object -> object.writePacket(packet));
	}

	private static boolean sendChunk(ServerPlayer player, FriendlyByteBuf packet, long tempPacketId, int chunk) {
		final FriendlyByteBuf packetChunk = new FriendlyByteBuf(Unpooled.buffer());
		packetChunk.writeLong(tempPacketId);
		packetChunk.writeInt(chunk);

		final boolean success = chunk * PACKET_CHUNK_SIZE > packet.readableBytes();
		packetChunk.writeBoolean(success);
		if (!success) {
			packetChunk.writeBytes(packet.copy(chunk * PACKET_CHUNK_SIZE, Math.min(PACKET_CHUNK_SIZE, packet.readableBytes() - chunk * PACKET_CHUNK_SIZE)));
		}

		try {
			Registry.sendToPlayer(player, PACKET_CHUNK_S2C, packetChunk);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}
}
