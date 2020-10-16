package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Set;

public class TrainData extends PersistentState {

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_STATION = "station_";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_PLATFORM = "platform_";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_ROUTE = "route_";

	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private final Set<Route> routes;

	public TrainData() {
		super(NAME);
		stations = new HashSet<>();
		platforms = new HashSet<>();
		routes = new HashSet<>();
	}

	@Override
	public void fromTag(CompoundTag tag) {
		final CompoundTag tagStations = tag.getCompound(KEY_STATIONS);
		for (String key : tagStations.getKeys()) {
			stations.add(new Station(tagStations.getCompound(key)));
		}

		final CompoundTag tagNewPlatforms = tag.getCompound(KEY_PLATFORMS);
		for (String key : tagNewPlatforms.getKeys()) {
			platforms.add(new Platform(tagNewPlatforms.getCompound(key)));
		}

		final CompoundTag tagNewRoutes = tag.getCompound(KEY_ROUTES);
		for (String key : tagNewRoutes.getKeys()) {
			routes.add(new Route(tagNewRoutes.getCompound(key)));
		}
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		final CompoundTag tagStations = new CompoundTag();
		int i = 0;
		for (Station station : stations) {
			tagStations.put(KEY_STATION + i, station.toCompoundTag());
			i++;
		}
		tag.put(KEY_STATIONS, tagStations);

		final CompoundTag tagNewPlatforms = new CompoundTag();
		int j = 0;
		for (Platform platform : platforms) {
			tagNewPlatforms.put(KEY_PLATFORM + j, platform.toCompoundTag());
			j++;
		}
		tag.put(KEY_PLATFORMS, tagNewPlatforms);

		final CompoundTag tagNewRoutes = new CompoundTag();
		int k = 0;
		for (Route route : routes) {
			tagNewRoutes.put(KEY_ROUTE + k, route.toCompoundTag());
			k++;
		}
		tag.put(KEY_ROUTES, tagNewRoutes);

		return tag;
	}

	public void addStation(Station station) {
		stations.add(station);
		markDirty();
	}

	public Set<Station> getStations() {
		return stations;
	}

	public void addPlatform(Platform newPlatform) {
		platforms.removeIf(newPlatform::overlaps);
		platforms.add(newPlatform);
		markDirty();
	}

	public Set<Platform> getPlatforms(WorldAccess world) {
		validateData(world);
		return platforms;
	}

	public Set<Route> getRoutes() {
		return routes;
	}

	public void setData(WorldAccess world, Set<Station> stations, Set<Platform> platforms, Set<Route> routes) {
		this.stations.clear();
		this.stations.addAll(stations);
		this.platforms.clear();
		this.platforms.addAll(platforms);
		this.routes.clear();
		this.routes.addAll(routes);
		validateData(world);
	}

	private void validateData(WorldAccess world) {
		platforms.removeIf(platform -> !platform.hasRail(world));
		routes.forEach(route -> route.stationIds.removeIf(stationId -> stations.stream().noneMatch(station -> station.id == stationId)));
		markDirty();
	}

	public static boolean isBetween(int value, int value1, int value2) {
		return value >= Math.min(value1, value2) && value <= Math.max(value1, value2);
	}

	public static TrainData getInstance(World world) {
		MinecraftServer minecraftServer = world.getServer();
		if (minecraftServer == null) {
			return null;
		} else {
			return minecraftServer.getOverworld().getPersistentStateManager().getOrCreate(TrainData::new, NAME);
		}
	}
}
