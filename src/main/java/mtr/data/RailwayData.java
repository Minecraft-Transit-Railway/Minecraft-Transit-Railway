package mtr.data;

import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RailwayData extends PersistentState {

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
	private final List<Platform> scheduleValidate = new ArrayList<>();
	private final List<PlayerEntity> scheduleBroadcast = new ArrayList<>();

	public RailwayData() {
		super(NAME);
		stations = new HashSet<>();
		platforms = new HashSet<>();
		routes = new HashSet<>();
		rails = new HashSet<>();
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
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tag;
	}

	public Set<Station> getStations() {
		return stations;
	}

	public Set<Platform> getPlatforms() {
		scheduleValidate.clear();
		scheduleValidate.addAll(platforms);
		return platforms;
	}

	public Set<Route> getRoutes() {
		scheduleValidate.clear();
		scheduleValidate.addAll(platforms);
		return routes;
	}

	public void simulateTrains(WorldAccess world) {
		final long lunarTime = world.getLunarTime();
		routes.forEach(route -> route.getPositionYaw(world, lunarTime));

		if (!scheduleGenerate.isEmpty()) {
			final Route route = scheduleGenerate.get(0);
			route.startFindingPath(world, platforms);
			if (route.findPath()) {
				scheduleGenerate.remove(route);
				System.out.println(String.format("Generated route %s, %s remaining", route.name, scheduleGenerate.size()));
				scheduleBroadcast.clear();
				scheduleBroadcast.addAll(world.getPlayers());
			}
		}

		if (!scheduleValidate.isEmpty()) {
			final Platform platform = scheduleValidate.remove(0);
			final BlockPos platformMidPos = platform.getMidPos();
			if (world.isChunkLoaded(platformMidPos.getX() / 16, platformMidPos.getZ() / 16) && !platform.isValidPlatform(world)) {
				platforms.remove(platform);
			}

			if (scheduleValidate.isEmpty()) {
				System.out.println("Done validating platforms");
				scheduleBroadcast.clear();
				scheduleBroadcast.addAll(world.getPlayers());
				validateData();
			}
		}

		if (!scheduleBroadcast.isEmpty()) {
			final PlayerEntity player = scheduleBroadcast.remove(0);
			if (player != null) {
				PacketTrainDataGuiServer.sendAllInChunks((ServerPlayerEntity) player, stations, platforms, routes, rails);
				System.out.println("Sending all data to player " + player);
			}
		}
	}

	public void addRouteToGenerate(long id) {
		final Route route = getDataById(routes, id);
		if (route != null && !scheduleGenerate.contains(route)) {
			scheduleGenerate.add(route);
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

	public void setData(Platform newPlatform) {
		try {
			platforms.removeIf(platform -> platform.isOverlapping(newPlatform));
			platforms.add(newPlatform);
			scheduleValidate.clear();
			scheduleValidate.addAll(platforms);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addRail(BlockPos start, BlockPos end, Rail rail) {
		final Rail.RailEntry railEntry = rails.stream().filter(railEntry2 -> railEntry2.pos.equals(start)).findFirst().orElse(null);
		if (railEntry == null) {
			rails.add(new Rail.RailEntry(start, end, rail));
		} else {
			railEntry.connections.put(end, rail);
		}
	}

	// validation

	private void validateData() {
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

	// other

	public static boolean isBetween(double value, double value1, double value2) {
		return value >= Math.min(value1, value2) && value <= Math.max(value1, value2);
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
