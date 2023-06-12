package mtr.packet;

import io.netty.buffer.Unpooled;
import mtr.Keys;
import mtr.Registry;
import mtr.block.*;
import mtr.data.*;
import mtr.mappings.BlockEntityMapper;
import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
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
import java.util.function.Consumer;
import java.util.function.Function;

public class PacketTrainDataGuiServer extends PacketTrainDataBase {

	private static final int PACKET_CHUNK_SIZE = (int) Math.pow(2, 14); // 16384

	public static void versionCheckS2C(ServerPlayer player) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeUtf(Keys.MOD_VERSION.split("-hotfix-")[0]);
		Registry.sendToPlayer(player, PACKET_VERSION_CHECK, packet);
	}

	public static void openDashboardScreenS2C(ServerPlayer player, TransportMode transportMode, boolean useTimeAndWindSync) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeUtf(transportMode.toString());
		packet.writeBoolean(useTimeAndWindSync);
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

	public static void openLiftTrackFloorScreenS2C(ServerPlayer player, BlockPos blockPos) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(blockPos);
		Registry.sendToPlayer(player, PACKET_OPEN_LIFT_TRACK_FLOOR_SCREEN, packet);
	}

	public static void openLiftCustomizationScreenS2C(ServerPlayer player, long id) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		Registry.sendToPlayer(player, PACKET_OPEN_LIFT_CUSTOMIZATION_SCREEN, packet);
	}

	public static void openPIDSConfigScreenS2C(ServerPlayer player, BlockPos pos1, BlockPos pos2, int maxArrivals, int linesPerArrival) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos1);
		packet.writeBlockPos(pos2);
		packet.writeInt(maxArrivals);
		packet.writeInt(linesPerArrival);
		Registry.sendToPlayer(player, PACKET_OPEN_PIDS_CONFIG_SCREEN, packet);
	}

	public static void openArrivalProjectorConfigScreenS2C(ServerPlayer player, BlockPos pos) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos);
		Registry.sendToPlayer(player, PACKET_OPEN_ARRIVAL_PROJECTOR_CONFIG_SCREEN, packet);
	}

	public static void openResourcePackCreatorScreenS2C(ServerPlayer player) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		Registry.sendToPlayer(player, PACKET_OPEN_RESOURCE_PACK_CREATOR_SCREEN, packet);
	}

	public static void announceS2C(ServerPlayer player, String message, ResourceLocation soundId) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeUtf(message);
		packet.writeUtf(soundId == null ? "" : soundId.toString());
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

	public static void removeLiftFloorTrackS2C(Level world, BlockPos pos) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeBlockPos(pos);
		world.players().forEach(worldPlayer -> Registry.sendToPlayer((ServerPlayer) worldPlayer, PACKET_REMOVE_LIFT_FLOOR_TRACK, packet));
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

	public static void sendAllInChunks(ServerPlayer player, Set<Station> stations, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes, Set<Depot> depots, Set<LiftServer> lifts, SignalBlocks signalBlocks) {
		final long tempPacketId = new Random().nextLong();
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

		serializeData(packet, stations);
		serializeData(packet, platforms);
		serializeData(packet, sidings);
		serializeData(packet, routes);
		serializeData(packet, depots);
		serializeData(packet, lifts);
		serializeData(packet, signalBlocks.signalBlocks);

		int i = 0;
		while (!sendChunk(player, packet, tempPacketId, i)) {
			i++;
		}
	}

	public static <T extends NameColorDataBase> void receiveUpdateOrDeleteC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet, ResourceLocation packetId, Function<RailwayData, Set<T>> dataSet, Function<RailwayData, Map<Long, T>> cacheMap, BiFunction<Long, TransportMode, T> createDataWithId, boolean isDelete) {
		if (RailwayData.hasNoPermission(player)) {
			return;
		}

		final Level world = player.level;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		final PacketCallback packetCallback = (updatePacket, fullPacket) -> {
			world.players().forEach(worldPlayer -> {
				if (!worldPlayer.getUUID().equals(player.getUUID())) {
					Registry.sendToPlayer((ServerPlayer) worldPlayer, packetId, fullPacket);
				}
				railwayData.dataCache.sync();
			});

			if (packetId.equals(PACKET_UPDATE_STATION) || packetId.equals(PACKET_DELETE_STATION) || packetId.equals(PACKET_UPDATE_DEPOT) || packetId.equals(PACKET_DELETE_DEPOT)) {
				try {
					UpdateDynmap.updateDynmap(world, railwayData);
				} catch (NoClassDefFoundError | Exception ignored) {
				}
				try {
					UpdateBlueMap.updateBlueMap(world, railwayData);
				} catch (NoClassDefFoundError | Exception ignored) {
				}
				try {
					UpdateSquaremap.updateSquaremap(world, railwayData);
				} catch (NoClassDefFoundError | Exception ignored) {
				}
			}
		};

		if (isDelete) {
			deleteData(dataSet.apply(railwayData), cacheMap.apply(railwayData), minecraftServer, packet, packetCallback, data -> railwayData.railwayDataLoggingModule.addEvent(player, data.getClass(), data.id, data.name, RailwayDataLoggingModule.getData(data), new ArrayList<>()));
		} else {
			updateData(dataSet.apply(railwayData), cacheMap.apply(railwayData), minecraftServer, packet, packetCallback, createDataWithId, (data, oldData) -> railwayData.railwayDataLoggingModule.addEvent(player, data.getClass(), data.id, data.name, oldData, RailwayDataLoggingModule.getData(data)));
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
			minecraftServer.execute(() -> railwayData.railwayDataPathGenerationModule.generatePath(minecraftServer, depotId));
		}
	}

	public static void clearTrainsC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		if (RailwayData.hasNoPermission(player)) {
			return;
		}

		final Level world = player.level;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final long depotId = packet.readLong();
			final int sidingCount = packet.readInt();
			final Set<Long> sidingIds = new HashSet<>();
			for (int i = 0; i < sidingCount; i++) {
				sidingIds.add(packet.readLong());
			}
			minecraftServer.execute(() -> {
				final Depot depot = railwayData.dataCache.depotIdMap.get(depotId);
				if (depot != null) {
					railwayData.resetTrainDelays(depot);
				}
				sidingIds.forEach(sidingId -> {
					final Siding siding = railwayData.dataCache.sidingIdMap.get(sidingId);
					if (siding != null) {
						siding.clearTrains();
					}
				});
			});
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
		final int stringCount = packet.readInt();
		final String[] strings = new String[stringCount];
		for (int i = 0; i < stringCount; i++) {
			strings[i] = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		}
		minecraftServer.execute(() -> {
			final BlockEntity entity = player.level.getBlockEntity(pos);
			if (entity instanceof BlockTrainSensorBase.TileEntityTrainSensorBase) {
				setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setData(filterIds, stoppedOnly, movingOnly, number, strings), (BlockTrainSensorBase.TileEntityTrainSensorBase) entity);
			}
		});
	}

	public static void receiveLiftTrackFloorC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		final String floorNumber = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		final String floorDescription = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		final boolean shouldDing = packet.readBoolean();
		minecraftServer.execute(() -> {
			final BlockEntity entity = player.level.getBlockEntity(pos);
			if (entity instanceof BlockLiftTrackFloor.TileEntityLiftTrackFloor) {
				setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setData(floorNumber, floorDescription, shouldDing), (BlockLiftTrackFloor.TileEntityLiftTrackFloor) entity);
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
				setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setData(selectedIds, signIds), (BlockRailwaySign.TileEntityRailwaySign) entity);
			} else if (entity instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
				final long platformId = selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0];
				final BlockEntity entityAbove = player.level.getBlockEntity(signPos.above());
				if (entityAbove instanceof BlockRouteSignBase.TileEntityRouteSignBase) {
					setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), ((BlockRouteSignBase.TileEntityRouteSignBase) entityAbove), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
				} else {
					setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setPlatformId(platformId), (BlockRouteSignBase.TileEntityRouteSignBase) entity);
				}
			}
		});
	}

	public static void receiveDriveTrainC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final RailwayData railwayData = RailwayData.getInstance(player.level);
		if (railwayData != null) {
			final boolean pressingAccelerate = packet.readBoolean();
			final boolean pressingBrake = packet.readBoolean();
			final boolean pressingDoors = packet.readBoolean();
			minecraftServer.execute(() -> railwayData.railwayDataDriveTrainModule.drive(player, pressingAccelerate, pressingBrake, pressingDoors));
		}
	}

	public static void receivePressLiftButtonC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final RailwayData railwayData = RailwayData.getInstance(player.level);
		if (railwayData != null) {
			final long id = packet.readLong();
			final int floor = packet.readInt();

			minecraftServer.execute(() -> {
				final Lift lift = railwayData.dataCache.liftsServerIdMap.get(id);
				if (lift != null) {
					lift.pressButton(floor);
				}
			});
		}
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
		final int maxMessages = packet.readInt();
		final String[] messages = new String[maxMessages];
		for (int i = 0; i < maxMessages; i++) {
			messages[i] = packet.readUtf(SerializedDataBase.PACKET_STRING_READ_LENGTH);
		}
		final int maxArrivals = packet.readInt();
		final boolean[] hideArrivals = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			hideArrivals[i] = packet.readBoolean();
		}
		final int platformIdCount = packet.readInt();
		final Set<Long> platformIds = new HashSet<>();
		for (int i = 0; i < platformIdCount; i++) {
			platformIds.add(packet.readLong());
		}
		final int arrivalPage = packet.readInt();
		minecraftServer.execute(() -> {
			final List<IPIDS.TileEntityPIDS> entities = new ArrayList<>();

			final BlockEntity entity1 = player.level.getBlockEntity(pos1);
			if (entity1 instanceof IPIDS.TileEntityPIDS) {
				entities.add((IPIDS.TileEntityPIDS) entity1);
			}

			final BlockEntity entity2 = player.level.getBlockEntity(pos2);
			if (entity2 instanceof IPIDS.TileEntityPIDS) {
				entities.add((IPIDS.TileEntityPIDS) entity2);
			}

			setTileEntityDataAndWriteUpdate(player, entity -> entity.setData(messages, hideArrivals, platformIds, arrivalPage), entities.toArray(new IPIDS.TileEntityPIDS[0]));
		});
	}

	public static void receiveArrivalProjectorMessageC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		final BlockPos pos = packet.readBlockPos();
		final int platformIdCount = packet.readInt();
		final Set<Long> platformIds = new HashSet<>();
		for (int i = 0; i < platformIdCount; i++) {
			platformIds.add(packet.readLong());
		}
		final int displayPage = packet.readInt();
		minecraftServer.execute(() -> {
			final BlockEntity entity = player.level.getBlockEntity(pos);
			if (entity instanceof BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) {
				setTileEntityDataAndWriteUpdate(player, entity2 -> entity2.setData(platformIds, displayPage), ((BlockArrivalProjectorBase.TileEntityArrivalProjectorBase) entity));
			}
		});
	}

	public static void receiveUseTimeAndWindSyncC2S(MinecraftServer minecraftServer, ServerPlayer player, FriendlyByteBuf packet) {
		if (RailwayData.hasNoPermission(player) || !player.hasPermissions(1)) {
			return;
		}

		final Level world = player.level;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final boolean useTimeAndWindSync = packet.readBoolean();
			minecraftServer.execute(() -> {
				final boolean useTimeAndWindSyncOld = railwayData.getUseTimeAndWindSync();
				railwayData.setUseTimeAndWindSync(useTimeAndWindSync);
				final String key = "\"use_time_and_wind_sync\":";
				railwayData.railwayDataLoggingModule.addEvent(player, RailwayData.class, Collections.singletonList(key + useTimeAndWindSyncOld), Collections.singletonList(key + useTimeAndWindSync));
			});
		}
	}

	public static void receiveRemoveRailAction(MinecraftServer minecraftServer, Player player, FriendlyByteBuf packet) {
		final Level world = player.level;
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData != null) {
			final long id = packet.readLong();
			minecraftServer.execute(() -> railwayData.railwayDataRailActionsModule.removeRailAction(id));
		}
	}

	public static void receiveUpdateTrainPassengerPosition(MinecraftServer minecraftServer, Player player, FriendlyByteBuf packet) {
		final FriendlyByteBuf packetFullCopy = new FriendlyByteBuf(packet.copy());
		minecraftServer.execute(() -> player.level.players().forEach(sendPlayer -> {
			if (sendPlayer != player) {
				Registry.sendToPlayer((ServerPlayer) sendPlayer, PACKET_UPDATE_TRAIN_PASSENGER_POSITION, packetFullCopy);
			}
		}));
	}

	public static void receiveUpdateLiftPassengerPosition(MinecraftServer minecraftServer, Player player, FriendlyByteBuf packet) {
		final FriendlyByteBuf packetFullCopy = new FriendlyByteBuf(packet.copy());
		minecraftServer.execute(() -> player.level.players().forEach(sendPlayer -> {
			if (sendPlayer != player) {
				Registry.sendToPlayer((ServerPlayer) sendPlayer, PACKET_UPDATE_LIFT_PASSENGER_POSITION, packetFullCopy);
			}
		}));
	}

	public static void receiveUpdateEntitySeatPassengerPosition(MinecraftServer minecraftServer, Player player, FriendlyByteBuf packet) {
		final RailwayData railwayData = RailwayData.getInstance(player.level);
		if (railwayData != null) {
			final double x = packet.readDouble();
			final double y = packet.readDouble();
			final double z = packet.readDouble();
			minecraftServer.execute(() -> railwayData.railwayDataCoolDownModule.moveSeat(player, x, y, z));
		}
	}

	private static <T extends SerializedDataBase> void serializeData(FriendlyByteBuf packet, Collection<T> objects) {
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

	@SafeVarargs
	private static <T extends BlockEntityMapper> void setTileEntityDataAndWriteUpdate(ServerPlayer player, Consumer<T> setData, T... entities) {
		final RailwayData railwayData = RailwayData.getInstance(player.level);

		if (railwayData != null && entities.length > 0) {
			final CompoundTag compoundTagOld = new CompoundTag();
			entities[0].writeCompoundTag(compoundTagOld);

			BlockPos blockPos = null;
			long posLong = 0;
			for (final T entity : entities) {
				setData.accept(entity);
				final BlockPos entityPos = entity.getBlockPos();
				if (blockPos == null || entityPos.asLong() > posLong) {
					blockPos = entityPos;
					posLong = entityPos.asLong();
				}
			}

			final CompoundTag compoundTagNew = new CompoundTag();
			entities[0].writeCompoundTag(compoundTagNew);

			railwayData.railwayDataLoggingModule.addEvent(player, entities[0].getClass(), RailwayDataLoggingModule.getData(compoundTagOld), RailwayDataLoggingModule.getData(compoundTagNew), blockPos);
		}
	}
}
