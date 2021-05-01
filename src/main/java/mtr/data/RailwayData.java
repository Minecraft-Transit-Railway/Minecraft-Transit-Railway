package mtr.data;

import mtr.block.BlockRail;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RailwayData extends PersistentState {

	// TODO temporary code start
	private boolean generated;
	// TODO temporary code end

	private static final long GENERATE_ROUTE_MAX_MILLIS = 20;

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_RAILS = "rails";

	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private final Set<Route> routes;
	private final Set<Rail.RailEntry> rails;

	private final List<Route> scheduleGenerate = new ArrayList<>();
	private final List<Rail.RailEntry> scheduleValidate = new ArrayList<>();
	private final List<PlayerEntity> scheduleBroadcast = new ArrayList<>();

	public RailwayData() {
		super(NAME);
		stations = new HashSet<>();
		platforms = new HashSet<>();
		routes = new HashSet<>();
		rails = new HashSet<>();
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

			final CompoundTag tagNewRoutes = tag.getCompound(KEY_ROUTES);
			for (final String key : tagNewRoutes.getKeys()) {
				routes.add(new Route(tagNewRoutes.getCompound(key)));
			}

			final CompoundTag tagNewRails = tag.getCompound(KEY_RAILS);
			for (final String key : tagNewRails.getKeys()) {
				rails.add(new Rail.RailEntry(tagNewRails.getCompound(key)));
			}

			// TODO temporary code start
			generated = tag.getBoolean("generated");
			// TODO temporary code end

			validateData();
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
			writeTag(tag, routes, KEY_ROUTES);
			writeTag(tag, rails, KEY_RAILS);

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

	public Set<Route> getRoutes() {
		return routes;
	}

	public void simulateTrains(World world) {
		// TODO temporary code start
		if (!generated) {
			routes.forEach(route -> route.generateRails(world, this));
			scheduleBroadcast.addAll(world.getPlayers());
			generated = true;
			markDirty();
			System.out.println("Generated rails (this should only happen once!)");
		}
		// TODO temporary code end

		final long millsStart = System.currentTimeMillis();
		final long lunarTime = world.getLunarTime();
		routes.forEach(route -> route.getPositionYaw(world, lunarTime));

		while (!scheduleGenerate.isEmpty() && System.currentTimeMillis() - millsStart < GENERATE_ROUTE_MAX_MILLIS) {
			final Route route = scheduleGenerate.get(0);
			route.startFindingPath(rails, platforms);
			if (route.findPath()) {
				scheduleGenerate.remove(route);
			}

			if (scheduleGenerate.isEmpty()) {
				scheduleBroadcast.clear();
				scheduleBroadcast.addAll(world.getPlayers());
			}
		}

		while (!scheduleValidate.isEmpty()) {
			final Rail.RailEntry railEntryValidate = scheduleValidate.remove(0);
			final boolean loadedChunk = world.isChunkLoaded(railEntryValidate.pos.getX() / 16, railEntryValidate.pos.getZ() / 16);
			if (loadedChunk && !(world.getBlockState(railEntryValidate.pos).getBlock() instanceof BlockRail)) {
				removeNode(world, railEntryValidate.pos);
			}

			if (scheduleValidate.isEmpty()) {
				rails.removeIf(railEntry -> railEntry.connections.size() == 0);
				validateData();
			}

			if (loadedChunk) {
				break;
			}
		}

		if (!scheduleBroadcast.isEmpty()) {
			final PlayerEntity player = scheduleBroadcast.remove(0);
			if (player != null) {
				PacketTrainDataGuiServer.sendAllInChunks((ServerPlayerEntity) player, stations, platforms, routes, rails);
			}
		}
	}

	public void addAllRoutesToGenerate() {
		routes.forEach(route -> {
			if (!scheduleGenerate.contains(route)) {
				scheduleGenerate.add(route);
			}
		});
	}

	public void addPlayerToBroadcast(PlayerEntity player) {
		if (!scheduleBroadcast.contains(player)) {
			scheduleBroadcast.add(player);
		}
	}

	// writing data

	public void addRail(BlockPos posStart, BlockPos posEnd, Rail rail, boolean validate) {
		try {
			final Rail.RailEntry railEntry = getRailEntry(rails, posStart);
			if (railEntry == null) {
				rails.add(new Rail.RailEntry(posStart, posEnd, rail));
			} else {
				railEntry.connections.put(posEnd, rail);
			}

			if (validate) {
				if (rail.railType == Rail.RailType.PLATFORM && platforms.stream().noneMatch(platform -> platform.containsPos(posStart) || platform.containsPos(posEnd))) {
					final Platform newPlatform = new Platform(posStart, posEnd);
					platforms.add(newPlatform);
				}
				validateRails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeNode(World world, BlockPos pos) {
		removeNode(world, rails, pos);
		validateRails();
	}

	public void removeRailConnection(World world, BlockPos pos1, BlockPos pos2) {
		removeRailConnection(world, rails, pos1, pos2);
		validateRails();
	}

	public boolean hasPlatform(BlockPos pos1, BlockPos pos2) {
		return rails.stream().anyMatch(railEntry -> (railEntry.pos.equals(pos1) || railEntry.pos.equals(pos2)) && railEntry.connections.values().stream().anyMatch(rail -> rail.railType == Rail.RailType.PLATFORM));
	}

	// validation

	private void validateRails() {
		validateData();
		scheduleValidate.clear();
		scheduleValidate.addAll(rails);
		addAllRoutesToGenerate();
	}

	private void validateData() {
		if (generated) {
			platforms.removeIf(platform -> !platform.isValidPlatform(rails));
		}
		routes.forEach(route -> route.platformIds.removeIf(platformId -> getDataById(platforms, platformId) == null));
		markDirty();
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

	public static Station getStationByPlatform(Set<Station> stations, Platform platform) {
		try {
			final BlockPos pos = platform.getMidPos();
			return stations.stream().filter(station -> station.inStation(pos.getX(), pos.getZ())).findFirst().orElse(null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Rail.RailEntry getRailEntry(Set<Rail.RailEntry> rails, BlockPos pos) {
		return rails.stream().filter(railEntry -> railEntry.pos.equals(pos)).findFirst().orElse(null);
	}

	// other

	public static void removeNode(World world, Set<Rail.RailEntry> rails, BlockPos pos) {
		try {
			final Rail.RailEntry railEntry = getRailEntry(rails, pos);
			if (railEntry != null) {
				rails.remove(railEntry);
				final Set<Rail.RailEntry> railEntriesToRemove = new HashSet<>();
				rails.forEach(validateRailEntry -> {
					validateRailEntry.connections.remove(pos);
					if (validateRailEntry.connections.size() == 0) {
						railEntriesToRemove.add(validateRailEntry);
						if (world != null) {
							BlockRail.resetRailNode(world, validateRailEntry.pos);
						}
					}
				});
				railEntriesToRemove.forEach(rails::remove);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void removeRailConnection(World world, Set<Rail.RailEntry> rails, BlockPos pos1, BlockPos pos2) {
		try {
			rails.forEach(railEntry -> {
				if (railEntry.pos.equals(pos1)) {
					railEntry.connections.remove(pos2);
				}
				if (railEntry.pos.equals(pos2)) {
					railEntry.connections.remove(pos1);
				}
				if (railEntry.connections.size() == 0 && world != null) {
					BlockRail.resetRailNode(world, railEntry.pos);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static boolean isBetween(double value, double value1, double value2) {
		return isBetween(value, value1, value2, 0);
	}

	public static boolean isBetween(double value, double value1, double value2, double padding) {
		return value >= Math.min(value1, value2) - padding && value <= Math.max(value1, value2) + padding;
	}

	public static RailwayData getInstance(World world) {
		if (world instanceof ServerWorld) {
			return ((ServerWorld) world).getPersistentStateManager().getOrCreate(RailwayData::new, NAME);
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
}
