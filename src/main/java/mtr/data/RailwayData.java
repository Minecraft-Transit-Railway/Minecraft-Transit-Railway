package mtr.data;

import mtr.block.BlockRail;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.*;

public class RailwayData extends PersistentState {

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
		// TODO temporary code start
		generated = true;
		// TODO temporary code end
	}

	@Override
	public void fromTag(CompoundTag tag) {
		try {
			final CompoundTag tagStations = tag.getCompound(KEY_STATIONS);
			for (final String key : tagStations.getKeys()) {
				stations.add(new Station(tagStations.getCompound(key)));
			}

			final CompoundTag tagNewPlatforms = tag.getCompound(KEY_PLATFORMS);
			for (final String key : tagNewPlatforms.getKeys()) {
				platforms.add(new Platform(tagNewPlatforms.getCompound(key)));
			}

			final CompoundTag tagNewSidings = tag.getCompound(KEY_SIDINGS);
			for (final String key : tagNewSidings.getKeys()) {
				sidings.add(new Siding(tagNewSidings.getCompound(key)));
			}

			final CompoundTag tagNewRoutes = tag.getCompound(KEY_ROUTES);
			for (final String key : tagNewRoutes.getKeys()) {
				routes.add(new Route(tagNewRoutes.getCompound(key)));
			}

			final CompoundTag tagNewDepots = tag.getCompound(KEY_DEPOTS);
			for (final String key : tagNewDepots.getKeys()) {
				depots.add(new Depot(tagNewDepots.getCompound(key)));
			}

			final CompoundTag tagNewRails = tag.getCompound(KEY_RAILS);
			for (final String key : tagNewRails.getKeys()) {
				final RailEntry railEntry = new RailEntry(tagNewRails.getCompound(key));
				rails.put(railEntry.pos, railEntry.connections);
			}

			// TODO temporary code start
			generated = tag.getBoolean("generated");
			// TODO temporary code end

			validateData();
			sidings.forEach(siding -> siding.generateRoute(rails, platforms, routes, depots));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		try {
			validateData();
			writeTag(tag, stations, KEY_STATIONS);
			writeTag(tag, platforms, KEY_PLATFORMS);
			writeTag(tag, sidings, KEY_SIDINGS);
			writeTag(tag, routes, KEY_ROUTES);
			writeTag(tag, depots, KEY_DEPOTS);

			final Set<RailEntry> railSet = new HashSet<>();
			rails.forEach((startPos, railMap) -> railSet.add(new RailEntry(startPos, railMap)));
			writeTag(tag, railSet, KEY_RAILS);

			// TODO temporary code start
			tag.putBoolean("generated", generated);
			// TODO temporary code end

		} catch (Exception e) {
			e.printStackTrace();
		}

		return tag;
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

		sidings.forEach(siding -> siding.simulateTrain(world, null, null, () -> siding.generateRoute(rails, platforms, routes, depots)));
	}

	public void addPlayerToBroadcast(PlayerEntity player) {
		if (!scheduleBroadcast.contains(player)) {
			scheduleBroadcast.add(player);
		}
	}

	// writing data

	public void addRail(BlockPos posStart, BlockPos posEnd, Rail rail, boolean validate) {
		try {
			if (!rails.containsKey(posStart)) {
				rails.put(posStart, new HashMap<>());
			}
			rails.get(posStart).put(posEnd, rail);

			if (validate) {
				if (rail.railType == RailType.PLATFORM && platforms.stream().noneMatch(platform -> platform.containsPos(posStart) || platform.containsPos(posEnd))) {
					platforms.add(new Platform(posStart, posEnd));
				} else if (rail.railType == RailType.SIDING && sidings.stream().noneMatch(depotRail -> depotRail.containsPos(posStart) || depotRail.containsPos(posEnd))) {
					sidings.add(new Siding(posStart, posEnd, (int) Math.floor(rail.getLength())));
				}
				validateRails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeNode(BlockPos pos) {
		removeNode(world, rails, pos);
		validateRails();
	}

	public void removeRailConnection(BlockPos pos1, BlockPos pos2) {
		removeRailConnection(world, rails, pos1, pos2);
		validateRails();
	}

	public boolean hasSavedRail(BlockPos pos) {
		return rails.containsKey(pos) && rails.get(pos).values().stream().anyMatch(rail -> rail.railType.hasSavedRail);
	}

	// validation

	private void validateRails() {
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
		validateData();
	}

	private void validateData() {
		if (generated) {
			platforms.removeIf(platform -> !platform.isValidSavedRail(rails));
			sidings.removeIf(siding -> !siding.isValidSavedRail(rails));

			final List<BlockPos> railsToRemove = new ArrayList<>();
			rails.forEach((startPos, railMap) -> railMap.forEach((endPos, rail) -> {
				if (rail.railType.hasSavedRail && !SavedRailBase.isValidSavedRail(rails, endPos, startPos)) {
					railsToRemove.add(startPos);
					railsToRemove.add(endPos);
				}
			}));
			for (int i = 0; i < railsToRemove.size() - 1; i += 2) {
				removeRailConnection(null, rails, railsToRemove.get(i), railsToRemove.get(i + 1));
			}

			routes.forEach(route -> route.platformIds.removeIf(platformId -> getDataById(platforms, platformId) == null));
			markDirty();
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

	public static void removeNode(World world, Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos) {
		try {
			rails.remove(pos);
			rails.forEach((startPos, railMap) -> {
				railMap.remove(pos);
				if (railMap.isEmpty() && world != null) {
					BlockRail.resetRailNode(world, startPos);
				}
			});
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

	public static RailwayData getInstance(World world) {
		if (world instanceof ServerWorld) {
			return ((ServerWorld) world).getPersistentStateManager().getOrCreate(() -> new RailwayData(world), NAME);
		} else {
			return null;
		}
	}

	private static void writeTag(CompoundTag tag, Set<? extends SerializedDataBase> dataSet, String key) {
		final CompoundTag tagSet = new CompoundTag();
		int i = 0;
		for (final SerializedDataBase data : dataSet) {
			tagSet.put(key + i, data.toCompoundTag());
			i++;
		}
		tag.put(key, tagSet);
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

		public RailEntry(CompoundTag tag) {
			pos = BlockPos.fromLong(tag.getLong(KEY_NODE_POS));
			connections = new HashMap<>();

			final CompoundTag tagConnections = tag.getCompound(KEY_RAIL_CONNECTIONS);
			for (final String keyConnection : tagConnections.getKeys()) {
				connections.put(BlockPos.fromLong(tagConnections.getCompound(keyConnection).getLong(KEY_NODE_POS)), new Rail(tagConnections.getCompound(keyConnection)));
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
		public void writePacket(PacketByteBuf packet) {
		}
	}
}
