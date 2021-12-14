package mtr.data;

import mtr.block.BlockRail;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.*;

public class RailwayData extends PersistentState implements IPacket {

	// TODO temporary code start
	private boolean generated;
	// TODO temporary code end

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_SIDINGS = "sidings";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_DEPOTS = "depots";
	private static final String KEY_RAILS = "rails";

	private final World world;
	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private final Set<Siding> sidings;
	private final Set<Route> routes;
	private final Set<Depot> depots;
	private final Map<BlockPos, Map<BlockPos, Rail>> rails;

	private final List<Set<Rail>> trainPositions = new ArrayList<>(2);

	private final List<PlayerEntity> scheduleBroadcast = new ArrayList<>();

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
				validateData(rails, platforms, sidings, routes);
			}
			updateSidings();
			markDirty();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public NbtCompound writeNbt(NbtCompound nbtCompound) {
		try {
			if (generated) {
				validateData(rails, platforms, sidings, routes);
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

	public Set<Station> getStations() {
		return stations;
	}

	public Set<Platform> getPlatforms() {
		return platforms;
	}

	public Set<Siding> getSidings() {
		return sidings;
	}

	public Set<Route> getRoutes() {
		return routes;
	}

	public Set<Depot> getDepots() {
		return depots;
	}

	public void simulateTrains() {
		// TODO temporary code start
		if (!generated) {
			routes.forEach(route -> route.generateRails(world, this));
			scheduleBroadcast.addAll(world.getPlayers());
			generated = true;
			markDirty();
			System.out.println("Generated rails (this should only happen once!)");
		}
		// TODO temporary code end

		if (!scheduleBroadcast.isEmpty()) {
			final PlayerEntity player = scheduleBroadcast.remove(0);
			if (player != null) {
				PacketTrainDataGuiServer.sendAllInChunks((ServerPlayerEntity) player, stations, platforms, sidings, routes, depots, rails);
			}
		}

		trainPositions.remove(0);
		trainPositions.add(new HashSet<>());
		sidings.forEach(siding -> {
			siding.simulateTrain(null, 1, trainPositions.get(0), null, null, null, null, null, () -> siding.generateRoute(world, rails, platforms, routes, depots));
			siding.writeTrainPositions(trainPositions.get(1), rails);
		});
	}

	public void addPlayerToBroadcast(PlayerEntity player) {
		if (!scheduleBroadcast.contains(player)) {
			scheduleBroadcast.add(player);
		}
	}

	public void updateSidings() {
		sidings.forEach(siding -> {
			siding.generateRoute(world, rails, platforms, routes, depots);
			siding.writeTrainPositions(trainPositions.get(1), rails);
		});
	}

	// writing data

	public long addRail(BlockPos posStart, BlockPos posEnd, Rail rail, boolean validate) {
		final long newId = validate ? new Random().nextLong() : 0;
		addRail(rails, platforms, sidings, posStart, posEnd, rail, newId);

		if (validate) {
			if (generated) {
				validateData(rails, platforms, sidings, routes);
			}
			updateSidings();
			markDirty();
		}

		return newId;
	}

	public void removeNode(BlockPos pos) {
		removeNode(world, rails, pos);
		if (generated) {
			validateData(rails, platforms, sidings, routes);
		}
		updateSidings();
		markDirty();
	}

	public void removeRailConnection(BlockPos pos1, BlockPos pos2) {
		removeRailConnection(world, rails, pos1, pos2);
		if (generated) {
			validateData(rails, platforms, sidings, routes);
		}
		updateSidings();
		markDirty();
	}

	public boolean hasSavedRail(BlockPos pos) {
		return rails.containsKey(pos) && rails.get(pos).values().stream().anyMatch(rail -> rail.railType.hasSavedRail);
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

	public static void validateData(Map<BlockPos, Map<BlockPos, Rail>> rails, Set<Platform> platforms, Set<Siding> sidings, Set<Route> routes) {
		platforms.removeIf(platform -> platform.isInvalidSavedRail(rails));
		sidings.removeIf(siding -> siding.isInvalidSavedRail(rails));

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

		routes.forEach(route -> route.platformIds.removeIf(platformId -> getDataById(platforms, platformId) == null));
	}

	public static boolean isBetween(double value, double value1, double value2) {
		return isBetween(value, value1, value2, 0);
	}

	public static boolean isBetween(double value, double value1, double value2, double padding) {
		return value >= Math.min(value1, value2) - padding && value <= Math.max(value1, value2) + padding;
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

	private static void writeTag(NbtCompound nbtCompound, Set<? extends SerializedDataBase> dataSet, String key) {
		final NbtCompound tagSet = new NbtCompound();
		int i = 0;
		for (final SerializedDataBase data : dataSet) {
			tagSet.put(key + i, data.toCompoundTag());
			i++;
		}
		nbtCompound.put(key, tagSet);
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
