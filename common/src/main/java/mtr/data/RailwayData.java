package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.MTR;
import mtr.Registry;
import mtr.block.BlockRailNode;
import mtr.mappings.PersistentStateMapper;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.path.PathData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.stream.Collectors;

public class RailwayData extends PersistentStateMapper implements IPacket {

	public final Set<Station> stations;
	public final Set<Platform> platforms;
	public final Set<Siding> sidings;
	public final Set<Route> routes;
	public final Set<Depot> depots;
	public final DataCache dataCache;

	private int prevPlatformCount;
	private int prevSidingCount;

	private final Level world;
	private final Map<BlockPos, Map<BlockPos, Rail>> rails;
	private final SignalBlocks signalBlocks = new SignalBlocks();

	private final List<Map<UUID, Long>> trainPositions = new ArrayList<>(2);
	private final Map<Player, BlockPos> playerLastUpdatedPositions = new HashMap<>();
	private final List<Player> playersToSyncSchedules = new ArrayList<>();
	private final Map<Player, Set<TrainServer>> trainsInPlayerRange = new HashMap<>();
	private final Map<Long, List<Route.ScheduleEntry>> schedulesForPlatform = new HashMap<>();
	private final Map<Player, Integer> playerRidingCoolDown = new HashMap<>();
	private final List<Rail.RailActions> railsToCreateTunnel = new ArrayList<>();
	private final List<Rail.RailActions> railsToCreateTunnelWall = new ArrayList<>();
	private final List<Rail.RailActions> railsToCreateBridge = new ArrayList<>();
	private final Map<Long, Thread> generatingPathThreads = new HashMap<>();

	private static final int RAIL_UPDATE_DISTANCE = 128;
	private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;
	private static final int SCHEDULE_UPDATE_TICKS = 60;

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_SIDINGS = "sidings";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_DEPOTS = "depots";
	private static final String KEY_RAILS = "rails";
	private static final String KEY_SIGNAL_BLOCKS = "signal_blocks";

	public RailwayData(Level world) {
		super(NAME);
		this.world = world;
		stations = new HashSet<>();
		platforms = new HashSet<>();
		sidings = new HashSet<>();
		routes = new HashSet<>();
		depots = new HashSet<>();
		rails = new HashMap<>();
		dataCache = new DataCache(stations, platforms, sidings, routes, depots);

		trainPositions.add(new HashMap<>());
		trainPositions.add(new HashMap<>());
	}

	@Override
	public void load(CompoundTag compoundTag) {
		try {
			final CompoundTag tagStations = compoundTag.getCompound(KEY_STATIONS);
			for (final String key : tagStations.getAllKeys()) {
				stations.add(new Station(tagStations.getCompound(key)));
			}

			final CompoundTag tagNewPlatforms = compoundTag.getCompound(KEY_PLATFORMS);
			for (final String key : tagNewPlatforms.getAllKeys()) {
				platforms.add(new Platform(tagNewPlatforms.getCompound(key)));
			}

			final CompoundTag tagNewSidings = compoundTag.getCompound(KEY_SIDINGS);
			for (final String key : tagNewSidings.getAllKeys()) {
				sidings.add(new Siding(tagNewSidings.getCompound(key)));
			}

			final CompoundTag tagNewRoutes = compoundTag.getCompound(KEY_ROUTES);
			for (final String key : tagNewRoutes.getAllKeys()) {
				routes.add(new Route(tagNewRoutes.getCompound(key)));
			}

			final CompoundTag tagNewDepots = compoundTag.getCompound(KEY_DEPOTS);
			for (final String key : tagNewDepots.getAllKeys()) {
				depots.add(new Depot(tagNewDepots.getCompound(key)));
			}

			final CompoundTag tagNewRails = compoundTag.getCompound(KEY_RAILS);
			for (final String key : tagNewRails.getAllKeys()) {
				final RailEntry railEntry = new RailEntry(tagNewRails.getCompound(key));
				rails.put(railEntry.pos, railEntry.connections);
			}

			final CompoundTag tagNewSignalBlocks = compoundTag.getCompound(KEY_SIGNAL_BLOCKS);
			for (final String key : tagNewSignalBlocks.getAllKeys()) {
				signalBlocks.signalBlocks.add(new SignalBlocks.SignalBlock(tagNewSignalBlocks.getCompound(key)));
			}

			validateData();
			dataCache.sync();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public CompoundTag save(CompoundTag compoundTag) {
		try {
			validateData();
			setDirty();
			writeTag(compoundTag, stations, KEY_STATIONS, false);
			writeTag(compoundTag, platforms, KEY_PLATFORMS);
			writeTag(compoundTag, sidings, KEY_SIDINGS);
			writeTag(compoundTag, routes, KEY_ROUTES, false);
			writeTag(compoundTag, depots, KEY_DEPOTS, false);
			writeTag(compoundTag, signalBlocks.signalBlocks, KEY_SIGNAL_BLOCKS);

			final Set<RailEntry> railSet = new HashSet<>();
			rails.forEach((startPos, railMap) -> railSet.add(new RailEntry(startPos, railMap)));
			writeTag(compoundTag, railSet, KEY_RAILS);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return compoundTag;
	}

	public void simulateTrains() {
		world.players().forEach(player -> {
			final BlockPos playerBlockPos = player.blockPosition();
			final Vec3 playerPos = player.position();

			if (!playerLastUpdatedPositions.containsKey(player) || playerLastUpdatedPositions.get(player).distManhattan(playerBlockPos) > PLAYER_MOVE_UPDATE_THRESHOLD) {
				final Map<BlockPos, Map<BlockPos, Rail>> railsToAdd = new HashMap<>();
				rails.forEach((startPos, blockPosRailMap) -> blockPosRailMap.forEach((endPos, rail) -> {
					if (new AABB(startPos, endPos).inflate(RAIL_UPDATE_DISTANCE).contains(playerPos)) {
						if (!railsToAdd.containsKey(startPos)) {
							railsToAdd.put(startPos, new HashMap<>());
						}
						railsToAdd.get(startPos).put(endPos, rail);
					}
				}));

				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeInt(railsToAdd.size());
				railsToAdd.forEach((posStart, railMap) -> {
					packet.writeBlockPos(posStart);
					packet.writeInt(railMap.size());
					railMap.forEach((posEnd, rail) -> {
						packet.writeBlockPos(posEnd);
						rail.writePacket(packet);
					});
				});

				if (packet.readableBytes() <= MAX_PACKET_BYTES) {
					Registry.sendToPlayer((ServerPlayer) player, PACKET_WRITE_RAILS, packet);
				}
				playerLastUpdatedPositions.put(player, playerBlockPos);
			}
		});

		trainPositions.remove(0);
		trainPositions.add(new HashMap<>());
		final Map<Player, Set<TrainServer>> newTrainsInPlayerRange = new HashMap<>();
		final Set<TrainServer> trainsToSync = new HashSet<>();
		schedulesForPlatform.clear();
		signalBlocks.resetOccupied();
		sidings.forEach(siding -> {
			siding.setSidingData(world, depots.stream().filter(depot -> {
				final BlockPos sidingMidPos = siding.getMidPos();
				return depot.inArea(sidingMidPos.getX(), sidingMidPos.getZ());
			}).findFirst().orElse(null), rails);
			siding.simulateTrain(1, dataCache, trainPositions, signalBlocks, newTrainsInPlayerRange, trainsToSync, schedulesForPlatform);
		});
		final int hour = Depot.getHour(world);
		depots.forEach(depot -> depot.deployTrain(this, hour));

		final Set<Player> playersToRemove = new HashSet<>();
		playerRidingCoolDown.forEach((player, coolDown) -> {
			if (coolDown <= 0) {
				updatePlayerRiding(player, false);
				playersToRemove.add(player);
			}
			playerRidingCoolDown.put(player, coolDown - 1);
		});
		playersToRemove.forEach(playerRidingCoolDown::remove);

		if (!railsToCreateTunnel.isEmpty() && railsToCreateTunnel.get(0).createTunnel()) {
			railsToCreateTunnel.remove(0);
		}
		if (!railsToCreateTunnelWall.isEmpty() && railsToCreateTunnelWall.get(0).createTunnelWall()) {
			railsToCreateTunnelWall.remove(0);
		}
		if (!railsToCreateBridge.isEmpty() && railsToCreateBridge.get(0).createBridge()) {
			railsToCreateBridge.remove(0);
		}

		trainsInPlayerRange.forEach((player, trains) -> {
			for (final TrainServer train : trains) {
				if (!newTrainsInPlayerRange.containsKey(player) || !newTrainsInPlayerRange.get(player).contains(train)) {
					final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

					if (newTrainsInPlayerRange.containsKey(player)) {
						packet.writeInt(newTrainsInPlayerRange.get(player).size());
						newTrainsInPlayerRange.get(player).forEach(trainToKeep -> packet.writeLong(trainToKeep.id));
					} else {
						packet.writeInt(0);
					}

					if (packet.readableBytes() <= MAX_PACKET_BYTES) {
						Registry.sendToPlayer((ServerPlayer) player, PACKET_DELETE_TRAINS, packet);
					}

					break;
				}
			}
		});

		newTrainsInPlayerRange.forEach((player, trains) -> {
			final List<FriendlyByteBuf> trainsPacketsToUpdate = new ArrayList<>();
			trains.forEach(train -> {
				if (trainsToSync.contains(train) || !trainsInPlayerRange.containsKey(player) || !trainsInPlayerRange.get(player).contains(train)) {
					final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
					train.writePacket(packet);
					if (packet.readableBytes() < MAX_PACKET_BYTES) {
						trainsPacketsToUpdate.add(packet);
					}
				}
			});

			while (!trainsPacketsToUpdate.isEmpty()) {
				FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());

				while (!trainsPacketsToUpdate.isEmpty()) {
					final FriendlyByteBuf trainPacket = trainsPacketsToUpdate.get(0);
					if (packet.readableBytes() + trainPacket.readableBytes() < MAX_PACKET_BYTES) {
						packet.writeBytes(trainPacket);
						trainsPacketsToUpdate.remove(0);
					} else {
						break;
					}
				}

				Registry.sendToPlayer((ServerPlayer) player, PACKET_UPDATE_TRAINS, packet);
			}
		});

		trainsInPlayerRange.clear();
		trainsInPlayerRange.putAll(newTrainsInPlayerRange);

		if (MTR.isGameTickInterval(SCHEDULE_UPDATE_TICKS)) {
			world.players().forEach(player -> {
				if (!playersToSyncSchedules.contains(player)) {
					playersToSyncSchedules.add(player);
				}
			});
		}
		if (!playersToSyncSchedules.isEmpty()) {
			final Player player = playersToSyncSchedules.remove(0);
			final BlockPos playerBlockPos = player.blockPosition();
			final Vec3 playerPos = player.position();

			final Set<Long> platformIds = platforms.stream().filter(platform -> {
				if (platform.isCloseToSavedRail(playerBlockPos, PLAYER_MOVE_UPDATE_THRESHOLD, PLAYER_MOVE_UPDATE_THRESHOLD, PLAYER_MOVE_UPDATE_THRESHOLD)) {
					return true;
				}
				final Station station = dataCache.platformIdToStation.get(platform.id);
				return station != null && station.inArea(playerBlockPos.getX(), playerBlockPos.getZ());
			}).map(platform -> platform.id).collect(Collectors.toSet());

			final Set<UUID> railsToAdd = new HashSet<>();
			rails.forEach((startPos, blockPosRailMap) -> blockPosRailMap.forEach((endPos, rail) -> {
				if (new AABB(startPos, endPos).inflate(RAIL_UPDATE_DISTANCE).contains(playerPos)) {
					railsToAdd.add(PathData.getRailProduct(startPos, endPos));
				}
			}));
			final Map<Long, Boolean> signalBlockStatus = new HashMap<>();
			railsToAdd.forEach(rail -> signalBlocks.getSignalBlockStatus(signalBlockStatus, rail));

			if (!platformIds.isEmpty() || !signalBlockStatus.isEmpty()) {
				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeInt(platformIds.size());
				platformIds.forEach(platformId -> {
					packet.writeLong(platformId);
					final List<Route.ScheduleEntry> scheduleEntries = schedulesForPlatform.get(platformId);
					if (scheduleEntries == null) {
						packet.writeInt(0);
					} else {
						packet.writeInt(scheduleEntries.size());
						scheduleEntries.forEach(scheduleEntry -> scheduleEntry.writePacket(packet));
					}
				});

				packet.writeInt(signalBlockStatus.size());
				signalBlockStatus.forEach((id, occupied) -> {
					packet.writeLong(id);
					packet.writeBoolean(occupied);
				});

				if (packet.readableBytes() <= MAX_PACKET_BYTES) {
					Registry.sendToPlayer((ServerPlayer) player, PACKET_UPDATE_SCHEDULE, packet);
				}
			}
		}

		if (prevPlatformCount != platforms.size() || prevSidingCount != sidings.size()) {
			dataCache.sync();
		}
		prevPlatformCount = platforms.size();
		prevSidingCount = sidings.size();

		setDirty();
	}

	public void broadcastToPlayer(ServerPlayer serverPlayerEntity) {
		PacketTrainDataGuiServer.sendAllInChunks(serverPlayerEntity, stations, platforms, sidings, routes, depots, signalBlocks);
		playerRidingCoolDown.put(serverPlayerEntity, 2);
	}

	public void updatePlayerRiding(Player player) {
		updatePlayerRiding(player, true);
		playerRidingCoolDown.put(player, 2);
	}

	// writing data

	public long addRail(BlockPos posStart, BlockPos posEnd, Rail rail, boolean validate) {
		final long newId = validate ? new Random().nextLong() : 0;
		addRail(rails, platforms, sidings, posStart, posEnd, rail, newId);

		if (validate) {
			validateData();
		}

		return newId;
	}

	public long addSignal(DyeColor color, BlockPos posStart, BlockPos posEnd) {
		return signalBlocks.add(0, color, PathData.getRailProduct(posStart, posEnd));
	}

	public void removeNode(BlockPos pos) {
		removeNode(world, rails, pos);
		validateData();
		final FriendlyByteBuf packet = signalBlocks.getValidationPacket(rails);
		if (packet != null) {
			world.players().forEach(player -> Registry.sendToPlayer((ServerPlayer) player, PACKET_REMOVE_SIGNALS, packet));
		}
	}

	public void removeRailConnection(BlockPos pos1, BlockPos pos2) {
		removeRailConnection(world, rails, pos1, pos2);
		validateData();
		final FriendlyByteBuf packet = signalBlocks.getValidationPacket(rails);
		if (packet != null) {
			world.players().forEach(player -> Registry.sendToPlayer((ServerPlayer) player, PACKET_REMOVE_SIGNALS, packet));
		}
	}

	public boolean hasSavedRail(BlockPos pos) {
		return rails.containsKey(pos) && rails.get(pos).values().stream().anyMatch(rail -> rail.railType.hasSavedRail);
	}

	public boolean containsRail(BlockPos pos1, BlockPos pos2) {
		return containsRail(rails, pos1, pos2);
	}

	public long removeSignal(DyeColor color, BlockPos posStart, BlockPos posEnd) {
		return signalBlocks.remove(0, color, PathData.getRailProduct(posStart, posEnd));
	}

	public boolean markRailForTunnel(BlockPos pos1, BlockPos pos2, int radius, int height) {
		if (containsRail(pos1, pos2)) {
			railsToCreateTunnel.add(new Rail.RailActions(world, rails.get(pos1).get(pos2), radius, height, null));
			return true;
		} else {
			return false;
		}
	}

	public boolean markRailForTunnelWall(BlockPos pos1, BlockPos pos2, int radius, int height, BlockState state) {
		if (containsRail(pos1, pos2)) {
			railsToCreateTunnelWall.add(new Rail.RailActions(world, rails.get(pos1).get(pos2), radius + 1, height + 1, state));
			return true;
		} else {
			return false;
		}
	}

	public boolean markRailForBridge(BlockPos pos1, BlockPos pos2, int radius, int height, BlockState state) {
		if (containsRail(pos1, pos2)) {
			railsToCreateBridge.add(new Rail.RailActions(world, rails.get(pos1).get(pos2), radius, 0, state));
			return true;
		} else {
			return false;
		}
	}

	public void disconnectPlayer(Player player) {
		playerLastUpdatedPositions.remove(player);
	}

	public void generatePath(MinecraftServer minecraftServer, long depotId) {
		generatingPathThreads.keySet().removeIf(id -> !generatingPathThreads.get(id).isAlive());
		final Depot depot = dataCache.depotIdMap.get(depotId);
		if (depot != null) {
			if (generatingPathThreads.containsKey(depotId)) {
				generatingPathThreads.get(depotId).interrupt();
				System.out.println("Restarting path generation" + (depot.name.isEmpty() ? "" : " for " + depot.name));
			} else {
				System.out.println("Starting path generation" + (depot.name.isEmpty() ? "" : " for " + depot.name));
			}
			depot.generateMainRoute(minecraftServer, world, dataCache, rails, sidings, thread -> generatingPathThreads.put(depotId, thread));
		}
	}

	public void getSchedulesForStation(Map<Long, List<Route.ScheduleEntry>> schedulesForStation, long stationId) {
		schedulesForPlatform.forEach((platformId, schedules) -> {
			final Station station = dataCache.platformIdToStation.get(platformId);
			if (station != null && station.id == stationId) {
				schedulesForStation.put(platformId, schedules);
			}
		});
	}

	public List<Route.ScheduleEntry> getSchedulesAtPlatform(long platformId) {
		return schedulesForPlatform.get(platformId);
	}

	private void validateData() {
		removeSavedRailS2C(world, platforms, rails, PACKET_DELETE_PLATFORM);
		removeSavedRailS2C(world, sidings, rails, PACKET_DELETE_SIDING);

		final List<BlockPos> railsToRemove = new ArrayList<>();
		rails.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
			if (rail.railType.hasSavedRail && SavedRailBase.isInvalidSavedRail(rails, endPos, startPos)) {
				railsToRemove.add(startPos);
				railsToRemove.add(endPos);
			}
		}));
		for (int i = 0; i < railsToRemove.size() - 1; i += 2) {
			removeRailConnection(null, rails, railsToRemove.get(i), railsToRemove.get(i + 1));
		}
	}

	// static finders

	public static Platform getPlatformByPos(Set<Platform> platforms, BlockPos pos) {
		try {
			return platforms.stream().filter(platform -> platform.containsPos(pos)).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// other

	public static void addRail(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Siding> sidings, BlockPos posStart, BlockPos posEnd, Rail rail, long savedRailId) {
		try {
			if (!rails.containsKey(posStart)) {
				rails.put(posStart, new HashMap<>());
			}
			rails.get(posStart).put(posEnd, rail);

			if (savedRailId != 0) {
				if (rail.railType == RailType.PLATFORM && platforms.stream().noneMatch(platform -> platform.containsPos(posStart) || platform.containsPos(posEnd))) {
					platforms.add(new Platform(savedRailId, posStart, posEnd));
				} else if (rail.railType == RailType.SIDING && sidings.stream().noneMatch(siding -> siding.containsPos(posStart) || siding.containsPos(posEnd))) {
					sidings.add(new Siding(savedRailId, posStart, posEnd, (int) Math.floor(rail.getLength())));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeNode(Level world, Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos) {
		try {
			rails.remove(pos);
			rails.forEach((startPos, railMap) -> {
				railMap.remove(pos);
				if (railMap.isEmpty() && world != null) {
					BlockRailNode.resetRailNode(world, startPos);
				}
			});
			if (world != null) {
				validateRails(world, rails);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeRailConnection(Level world, Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos1, BlockPos pos2) {
		try {
			if (rails.containsKey(pos1)) {
				rails.get(pos1).remove(pos2);
				if (rails.get(pos1).isEmpty() && world != null) {
					BlockRailNode.resetRailNode(world, pos1);
				}
			}
			if (rails.containsKey(pos2)) {
				rails.get(pos2).remove(pos1);
				if (rails.get(pos2).isEmpty() && world != null) {
					BlockRailNode.resetRailNode(world, pos2);
				}
			}
			if (world != null) {
				validateRails(world, rails);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean containsRail(Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos1, BlockPos pos2) {
		return rails.containsKey(pos1) && rails.get(pos1).containsKey(pos2);
	}

	public static Station getStation(Set<Station> stations, DataCache dataCache, BlockPos pos) {
		try {
			if (dataCache.blockPosToStation.containsKey(pos)) {
				return dataCache.blockPosToStation.get(pos);
			} else {
				return stations.stream().filter(station -> station.inArea(pos.getX(), pos.getZ())).findFirst().orElse(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static long getClosePlatformId(Set<Platform> platforms, DataCache dataCache, BlockPos pos) {
		return getClosePlatformId(platforms, dataCache, pos, 4, 0, 4);
	}

	public static long getClosePlatformId(Set<Platform> platforms, DataCache dataCache, BlockPos pos, int radius, int lower, int upper) {
		try {
			if (dataCache.blockPosToPlatformId.containsKey(pos)) {
				return dataCache.blockPosToPlatformId.get(pos);
			} else {
				long platformId = 0;
				for (int i = 1; i <= radius; i++) {
					final int searchRadius = i;
					platformId = platforms.stream().filter(platform -> platform.isCloseToSavedRail(pos, searchRadius, lower, upper)).min(Comparator.comparingInt(platform -> platform.getMidPos().distManhattan(pos))).map(platform -> platform.id).orElse(0L);
					if (platformId != 0) {
						break;
					}
				}
				dataCache.blockPosToPlatformId.put(pos, platformId);
				return platformId;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public static boolean useRoutesAndStationsFromIndex(int stopIndex, List<Long> routeIds, DataCache dataCache, RouteAndStationsCallback routeAndStationsCallback) {
		if (stopIndex < 0) {
			return false;
		}

		int sum = 0;
		for (int i = 0; i < routeIds.size(); i++) {
			final Route thisRoute = dataCache.routeIdMap.get(routeIds.get(i));
			final Route nextRoute = i < routeIds.size() - 1 ? dataCache.routeIdMap.get(routeIds.get(i + 1)) : null;
			if (thisRoute != null) {
				final int difference = stopIndex - sum;
				sum += thisRoute.platformIds.size();
				if (!thisRoute.platformIds.isEmpty() && nextRoute != null && !nextRoute.platformIds.isEmpty() && thisRoute.platformIds.get(thisRoute.platformIds.size() - 1).equals(nextRoute.platformIds.get(0))) {
					sum--;
				}
				if (stopIndex < sum) {
					final Station thisStation = dataCache.platformIdToStation.get(thisRoute.platformIds.get(difference));
					final Station nextStation = difference < thisRoute.platformIds.size() - 1 ? dataCache.platformIdToStation.get(thisRoute.platformIds.get(difference + 1)) : null;
					final Station lastStation = thisRoute.platformIds.isEmpty() ? null : dataCache.platformIdToStation.get(thisRoute.platformIds.get(thisRoute.platformIds.size() - 1));
					routeAndStationsCallback.routeAndStationsCallback(thisRoute, nextRoute, thisStation, nextStation, lastStation);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isBetween(double value, double value1, double value2) {
		return isBetween(value, value1, value2, 0);
	}

	public static boolean isBetween(double value, double value1, double value2, double padding) {
		return value >= Math.min(value1, value2) - padding && value <= Math.max(value1, value2) + padding;
	}

	public static void writeTag(CompoundTag compoundTag, Collection<? extends SerializedDataBase> dataSet, String key) {
		writeTag(compoundTag, dataSet, key, true);
	}

	public static void writeTag(CompoundTag compoundTag, Collection<? extends SerializedDataBase> dataSet, String key, boolean skipVerify) {
		final CompoundTag tagSet = new CompoundTag();
		int i = 0;
		for (final SerializedDataBase data : dataSet) {
			if (skipVerify || !(data instanceof NameColorDataBase) || !((NameColorDataBase) data).name.isEmpty()) {
				tagSet.put(key + i, data.toCompoundTag());
				i++;
			}
		}
		compoundTag.put(key, tagSet);
	}

	public static RailwayData getInstance(Level world) {
		return getInstance(world, () -> new RailwayData(world), NAME);
	}

	public static void benchmark(Runnable runnable, float threshold) {
		final long nanos = System.nanoTime();
		runnable.run();
		final float duration = (System.nanoTime() - nanos) / 1000000000F;
		if (duration >= threshold) {
			System.out.println(duration);
		}
	}

	private static void validateRails(Level world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		final Set<BlockPos> railsToRemove = new HashSet<>();
		rails.forEach((startPos, railMap) -> {
			final boolean loadedChunk = world.hasChunk(startPos.getX() / 16, startPos.getZ() / 16);
			if (loadedChunk && !(world.getBlockState(startPos).getBlock() instanceof BlockRailNode)) {
				removeNode(null, rails, startPos);
			}

			if (railMap.isEmpty()) {
				railsToRemove.add(startPos);
			}
		});
		railsToRemove.forEach(rails::remove);
	}

	private static void removeSavedRailS2C(Level world, Set<? extends SavedRailBase> savedRailBases, Map<BlockPos, Map<BlockPos, Rail>> rails, ResourceLocation packetId) {
		savedRailBases.removeIf(savedRailBase -> {
			final boolean delete = savedRailBase.isInvalidSavedRail(rails);
			if (delete) {
				final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
				packet.writeLong(savedRailBase.id);
				world.players().forEach(player -> Registry.sendToPlayer((ServerPlayer) player, packetId, packet));
			}
			return delete;
		});
	}

	private static void updatePlayerRiding(Player player, boolean isRiding) {
		player.fallDistance = 0;
		player.setNoGravity(isRiding);
		player.noPhysics = isRiding;
	}

	private static class RailEntry extends SerializedDataBase {

		public final BlockPos pos;
		public final Map<BlockPos, Rail> connections;

		private static final String KEY_NODE_POS = "node_pos";
		private static final String KEY_RAIL_CONNECTIONS = "rail_connections";

		public RailEntry(BlockPos pos, Map<BlockPos, Rail> connections) {
			this.pos = pos;
			this.connections = connections;
		}

		public RailEntry(CompoundTag compoundTag) {
			pos = BlockPos.of(compoundTag.getLong(KEY_NODE_POS));
			connections = new HashMap<>();

			final CompoundTag tagConnections = compoundTag.getCompound(KEY_RAIL_CONNECTIONS);
			for (final String keyConnection : tagConnections.getAllKeys()) {
				connections.put(BlockPos.of(tagConnections.getCompound(keyConnection).getLong(KEY_NODE_POS)), new Rail(tagConnections.getCompound(keyConnection)));
			}
		}

		@Override
		public CompoundTag toCompoundTag() {
			final CompoundTag tagRail = new CompoundTag();
			tagRail.putLong(KEY_NODE_POS, pos.asLong());

			final CompoundTag tagConnections = new CompoundTag();
			connections.forEach((endNodePos, rail) -> {
				final CompoundTag tagConnection = rail.toCompoundTag();
				tagConnection.putLong(KEY_NODE_POS, endNodePos.asLong());
				tagConnections.put(KEY_RAIL_CONNECTIONS + endNodePos.asLong(), tagConnection);
			});

			tagRail.put(KEY_RAIL_CONNECTIONS, tagConnections);
			return tagRail;
		}

		@Override
		public void writePacket(FriendlyByteBuf packet) {
		}
	}

	@FunctionalInterface
	public interface RouteAndStationsCallback {
		void routeAndStationsCallback(Route thisRoute, Route nextRoute, Station thisStation, Station nextStation, Station lastStation);
	}
}
