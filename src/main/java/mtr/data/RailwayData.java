package mtr.data;

import mtr.block.BlockRail;
import mtr.mixin.PlayerTeleportationStateAccessor;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.*;

public class RailwayData extends PersistentState implements IPacket {

	// TODO temporary code start
	private boolean generated;
	// TODO temporary code end

	public final Set<Station> stations;
	public final Set<Platform> platforms;
	public final Set<Siding> sidings;
	public final Set<Route> routes;
	public final Set<Depot> depots;

	private final World world;
	private final Map<BlockPos, Map<BlockPos, Rail>> rails;

	private final List<Set<UUID>> trainPositions = new ArrayList<>(2);
	private final Map<PlayerEntity, BlockPos> playerLastUpdatedPositions = new HashMap<>();
	private final Map<PlayerEntity, Integer> playerRidingCoolDown = new HashMap<>();

	private final List<Depot> depotsToGenerate = new ArrayList<>();

	private static final int PLAYER_MOVE_UPDATE_THRESHOLD = 16;
	private static final int RAIL_UPDATE_DISTANCE = 64;

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_SIDINGS = "sidings";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_DEPOTS = "depots";
	private static final String KEY_RAILS = "rails";

	public RailwayData(World world) {
		super(NAME);
		this.world = world;
		stations = new HashSet<>();
		platforms = new HashSet<>();
		sidings = new HashSet<>();
		routes = new HashSet<>();
		depots = new HashSet<>();
		rails = new HashMap<>();

		trainPositions.add(new HashSet<>());
		trainPositions.add(new HashSet<>());

		// TODO temporary code start
		generated = true;
		// TODO temporary code end
	}

	@Override
	public void fromTag(NbtCompound nbtCompound) {
		try {
			final NbtCompound tagStations = nbtCompound.getCompound(KEY_STATIONS);
			for (final String key : tagStations.getKeys()) {
				stations.add(new Station(tagStations.getCompound(key)));
			}

			final NbtCompound tagNewPlatforms = nbtCompound.getCompound(KEY_PLATFORMS);
			for (final String key : tagNewPlatforms.getKeys()) {
				platforms.add(new Platform(tagNewPlatforms.getCompound(key)));
			}

			final NbtCompound tagNewSidings = nbtCompound.getCompound(KEY_SIDINGS);
			for (final String key : tagNewSidings.getKeys()) {
				sidings.add(new Siding(tagNewSidings.getCompound(key)));
			}

			final NbtCompound tagNewRoutes = nbtCompound.getCompound(KEY_ROUTES);
			for (final String key : tagNewRoutes.getKeys()) {
				routes.add(new Route(tagNewRoutes.getCompound(key)));
			}

			final NbtCompound tagNewDepots = nbtCompound.getCompound(KEY_DEPOTS);
			for (final String key : tagNewDepots.getKeys()) {
				depots.add(new Depot(tagNewDepots.getCompound(key)));
			}

			final NbtCompound tagNewRails = nbtCompound.getCompound(KEY_RAILS);
			for (final String key : tagNewRails.getKeys()) {
				final RailEntry railEntry = new RailEntry(tagNewRails.getCompound(key));
				rails.put(railEntry.pos, railEntry.connections);
			}

			// TODO temporary code start
			generated = nbtCompound.getBoolean("generated");
			// TODO temporary code end

			if (generated) {
				validateData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbtCompound) {
		try {
			if (generated) {
				validateData();
			}
			markDirty();
			writeTag(nbtCompound, stations, KEY_STATIONS);
			writeTag(nbtCompound, platforms, KEY_PLATFORMS);
			writeTag(nbtCompound, sidings, KEY_SIDINGS);
			writeTag(nbtCompound, routes, KEY_ROUTES);
			writeTag(nbtCompound, depots, KEY_DEPOTS);

			final Set<RailEntry> railSet = new HashSet<>();
			rails.forEach((startPos, railMap) -> railSet.add(new RailEntry(startPos, railMap)));
			writeTag(nbtCompound, railSet, KEY_RAILS);

			// TODO temporary code start
			nbtCompound.putBoolean("generated", generated);
			// TODO temporary code end
		} catch (Exception e) {
			e.printStackTrace();
		}

		return nbtCompound;
	}

	public void simulateTrains() {
		// TODO temporary code start
		if (!generated) {
			routes.forEach(route -> route.generateRails(world, this));
			generated = true;
			markDirty();
			System.out.println("Generated rails (this should only happen once!)");
		}
		// TODO temporary code end

		if (!depotsToGenerate.isEmpty()) {
			final Depot depot = depotsToGenerate.get(0);
			if (depot != null) {
				final boolean success = depot.generateSidingRoute(world, rails);
				if (success) {
					depotsToGenerate.remove(depot);
				}
			}
		}

		world.getPlayers().forEach(player -> {
			final BlockPos playerPos = player.getBlockPos();

			if (!playerLastUpdatedPositions.containsKey(player) || playerLastUpdatedPositions.get(player).getManhattanDistance(playerPos) > PLAYER_MOVE_UPDATE_THRESHOLD) {
				final PacketByteBuf packet = PacketByteBufs.create();

				final Map<BlockPos, Map<BlockPos, Rail>> railsToAdd = new HashMap<>();
				rails.forEach((startPos, blockPosRailMap) -> blockPosRailMap.forEach((endPos, rail) -> {
					if (new Box(startPos, endPos).expand(RAIL_UPDATE_DISTANCE).contains(player.getPos())) {
						if (!railsToAdd.containsKey(startPos)) {
							railsToAdd.put(startPos, new HashMap<>());
						}
						railsToAdd.get(startPos).put(endPos, rail);
					}
				}));

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
					ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_WRITE_RAILS, packet);
				}
				playerLastUpdatedPositions.put(player, playerPos);
			}
		});

		routes.forEach(route -> route.platformIds.removeIf(platformId -> getDataById(platforms, platformId) == null));
		depots.forEach(depot -> depot.routeIds.removeIf(routeId -> RailwayData.getDataById(routes, routeId) == null));

		trainPositions.remove(0);
		trainPositions.add(new HashSet<>());
		sidings.forEach(siding -> {
			siding.setSidingData(world, depots.stream().filter(depot -> {
				final BlockPos sidingMidPos = siding.getMidPos();
				return depot.inArea(sidingMidPos.getX(), sidingMidPos.getZ());
			}).findFirst().orElse(null), rails);
			siding.simulateTrain(null, 1, trainPositions, null, null, null, null, null);
		});

		final Set<PlayerEntity> playersToRemove = new HashSet<>();
		playerRidingCoolDown.forEach((player, coolDown) -> {
			if (coolDown <= 0) {
				updatePlayerRiding(player, false);
				playersToRemove.add(player);
			}
			playerRidingCoolDown.put(player, coolDown - 1);
		});
		playersToRemove.forEach(playerRidingCoolDown::remove);

		markDirty();
	}

	public void broadcastToPlayer(ServerPlayerEntity serverPlayerEntity) {
		PacketTrainDataGuiServer.sendAllInChunks(serverPlayerEntity, stations, platforms, sidings, routes, depots, rails);
	}

	public void updatePlayerRiding(PlayerEntity player) {
		updatePlayerRiding(player, true);
		playerRidingCoolDown.put(player, 2);
	}

	// writing data

	public long addRail(BlockPos posStart, BlockPos posEnd, Rail rail, boolean validate) {
		final long newId = validate ? new Random().nextLong() : 0;
		addRail(rails, platforms, sidings, posStart, posEnd, rail, newId);

		if (validate && generated) {
			validateData();
		}

		return newId;
	}

	public void removeNode(BlockPos pos) {
		removeNode(world, rails, pos);
		if (generated) {
			validateData();
		}
	}

	public void removeRailConnection(BlockPos pos1, BlockPos pos2) {
		removeRailConnection(world, rails, pos1, pos2);
		if (generated) {
			validateData();
		}
	}

	public boolean hasSavedRail(BlockPos pos) {
		return rails.containsKey(pos) && rails.get(pos).values().stream().anyMatch(rail -> rail.railType.hasSavedRail);
	}

	public void disconnectPlayer(PlayerEntity player) {
		playerLastUpdatedPositions.remove(player);
	}

	public void generatePath(long depotId) {
		final Depot depot = getDataById(depots, depotId);
		if (depot != null) {
			depot.generateMainRoute(rails, platforms, sidings, routes);
			depotsToGenerate.add(depot);
		}
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

	public static <T extends NameColorDataBase> T getDataById(Set<T> data, long id) {
		try {
			return data.stream().filter(item -> item.id == id).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Platform getPlatformByPos(Set<Platform> platforms, BlockPos pos) {
		try {
			return platforms.stream().filter(platform -> platform.containsPos(pos)).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static <T extends AreaBase> T getAreaBySavedRail(Set<T> areas, SavedRailBase savedRail) {
		try {
			final BlockPos pos = savedRail.getMidPos();
			return areas.stream().filter(station -> station.inArea(pos.getX(), pos.getZ())).findFirst().orElse(null);
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

	public static void removeNode(World world, Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos) {
		try {
			rails.remove(pos);
			rails.forEach((startPos, railMap) -> {
				railMap.remove(pos);
				if (railMap.isEmpty() && world != null) {
					BlockRail.resetRailNode(world, startPos);
				}
			});
			if (world != null) {
				validateRails(world, rails);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeRailConnection(World world, Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos1, BlockPos pos2) {
		try {
			if (rails.containsKey(pos1)) {
				rails.get(pos1).remove(pos2);
				if (rails.get(pos1).isEmpty() && world != null) {
					BlockRail.resetRailNode(world, pos1);
				}
			}
			if (rails.containsKey(pos2)) {
				rails.get(pos2).remove(pos1);
				if (rails.get(pos2).isEmpty() && world != null) {
					BlockRail.resetRailNode(world, pos2);
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

	public static boolean isBetween(double value, double value1, double value2) {
		return isBetween(value, value1, value2, 0);
	}

	public static boolean isBetween(double value, double value1, double value2, double padding) {
		return value >= Math.min(value1, value2) - padding && value <= Math.max(value1, value2) + padding;
	}

	public static void writeTag(NbtCompound nbtCompound, Collection<? extends SerializedDataBase> dataSet, String key) {
		final NbtCompound tagSet = new NbtCompound();
		int i = 0;
		for (final SerializedDataBase data : dataSet) {
			tagSet.put(key + i, data.toCompoundTag());
			i++;
		}
		nbtCompound.put(key, tagSet);
	}

	public static RailwayData getInstance(World world) {
		if (world instanceof ServerWorld) {
			return ((ServerWorld) world).getPersistentStateManager().getOrCreate(() -> new RailwayData(world), NAME);
		} else {
			return null;
		}
	}

	private static void validateRails(World world, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		final Set<BlockPos> railsToRemove = new HashSet<>();
		rails.forEach((startPos, railMap) -> {
			final boolean loadedChunk = world.isChunkLoaded(startPos.getX() / 16, startPos.getZ() / 16);
			if (loadedChunk && !(world.getBlockState(startPos).getBlock() instanceof BlockRail)) {
				removeNode(null, rails, startPos);
			}

			if (railMap.isEmpty()) {
				railsToRemove.add(startPos);
			}
		});
		railsToRemove.forEach(rails::remove);
	}

	private static void removeSavedRailS2C(World world, Set<? extends SavedRailBase> savedRailBases, Map<BlockPos, Map<BlockPos, Rail>> rails, Identifier packetId) {
		savedRailBases.removeIf(savedRailBase -> {
			final boolean delete = savedRailBase.isInvalidSavedRail(rails);
			if (delete) {
				final PacketByteBuf packet = PacketByteBufs.create();
				packet.writeLong(savedRailBase.id);
				world.getPlayers().forEach(player -> ServerPlayNetworking.send((ServerPlayerEntity) player, packetId, packet));
			}
			return delete;
		});
	}

	private static void updatePlayerRiding(PlayerEntity player, boolean isRiding) {
		player.fallDistance = 0;
		player.setNoGravity(isRiding);
		player.noClip = isRiding;
		((PlayerTeleportationStateAccessor) player).setInTeleportationState(isRiding);
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

		public RailEntry(NbtCompound nbtCompound) {
			pos = BlockPos.fromLong(nbtCompound.getLong(KEY_NODE_POS));
			connections = new HashMap<>();

			final NbtCompound tagConnections = nbtCompound.getCompound(KEY_RAIL_CONNECTIONS);
			for (final String keyConnection : tagConnections.getKeys()) {
				connections.put(BlockPos.fromLong(tagConnections.getCompound(keyConnection).getLong(KEY_NODE_POS)), new Rail(tagConnections.getCompound(keyConnection)));
			}
		}

		@Override
		public NbtCompound toCompoundTag() {
			final NbtCompound tagRail = new NbtCompound();
			tagRail.putLong(KEY_NODE_POS, pos.asLong());

			final NbtCompound tagConnections = new NbtCompound();
			connections.forEach((endNodePos, rail) -> {
				final NbtCompound tagConnection = rail.toCompoundTag();
				tagConnection.putLong(KEY_NODE_POS, endNodePos.asLong());
				tagConnections.put(KEY_RAIL_CONNECTIONS + endNodePos.asLong(), tagConnection);
			});

			tagRail.put(KEY_RAIL_CONNECTIONS, tagConnections);
			return tagRail;
		}

		@Override
		public void writePacket(PacketByteBuf packet) {
		}
	}
}
