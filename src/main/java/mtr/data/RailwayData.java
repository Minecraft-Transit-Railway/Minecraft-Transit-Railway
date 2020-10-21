package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.HashSet;
import java.util.Set;

public class RailwayData extends PersistentState {

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_STATION = "station_";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_PLATFORM = "platform_";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_ROUTE = "route_";
	private static final String KEY_TRAINS = "trains";
	private static final String KEY_TRAIN = "train_";

	private final Set<Station> stations;
	private final Set<Platform> platforms;
	private final Set<Route> routes;
	private final Set<Train> trains;

	public RailwayData() {
		super(NAME);
		stations = new HashSet<>();
		platforms = new HashSet<>();
		routes = new HashSet<>();
		trains = new HashSet<>();
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

		final CompoundTag tagNewTrains = tag.getCompound(KEY_TRAINS);
		for (String key : tagNewTrains.getKeys()) {
			trains.add(new Train(tagNewTrains.getCompound(key)));
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

		final CompoundTag tagNewTrains = new CompoundTag();
		int l = 0;
		for (Train train : trains) {
			tagNewTrains.put(KEY_TRAIN + l, train.toCompoundTag());
			l++;
		}
		tag.put(KEY_TRAINS, tagNewTrains);

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

	public void addTrain(Train train) {
		trains.add(train);
		markDirty();
	}

	public Set<Train> getTrains() {
		return trains;
	}

	public void removeTrains() {
		trains.clear();
	}

	public void simulateTrains(WorldAccess world) {
		trains.forEach(train -> {
			if (train.path.isEmpty()) {
				train.speed = 0;

				if (train.stationIds.isEmpty()) {
					// TODO train is dead
				} else {
					RailPathFinder railPathFinder = new RailPathFinder(world, new BlockPos(train.posX, train.posY, train.posZ), getStationById(train.stationIds.get(0)));
					train.path.clear();
					train.path.addAll(railPathFinder.findPath());
					train.stationIds.remove(0);
				}
			} else {
				if (train.speed < train.trainType.getMaxSpeed()) {
					train.speed += train.trainType.getAcceleration();
				}

				final Pos3f newPos = train.path.get(0);
				final Pos3f movement = new Pos3f(newPos.getX() - train.posX, newPos.getY() - train.posY, newPos.getZ() - train.posZ);

				if (movement.lengthSquared() < MathHelper.square(2 * train.speed)) {
					train.path.remove(0);
				}

				movement.normalize();
				movement.scale(train.speed);
				train.posX += movement.getX();
				train.posY += movement.getY();
				train.posZ += movement.getZ();
			}
		});
		markDirty();
	}

	public void setData(WorldAccess world, Set<Station> stations, Set<Platform> platforms, Set<Route> routes, Set<Train> trains) {
		this.stations.clear();
		this.stations.addAll(stations);
		this.platforms.clear();
		this.platforms.addAll(platforms);
		this.routes.clear();
		this.routes.addAll(routes);
		this.trains.clear();
		this.trains.addAll(trains);
		validateData(world);
	}

	private Station getStationById(long id) {
		return stations.stream().filter(station -> station.id == id).findFirst().orElse(null);
	}

	private void validateData(WorldAccess world) {
		platforms.removeIf(platform -> !platform.hasRail(world));
		routes.forEach(route -> route.stationIds.removeIf(stationId -> getStationById(stationId) == null));
		trains.removeIf(train -> train.stationIds.isEmpty());
		markDirty();
	}

	public static boolean isBetween(int value, int value1, int value2) {
		return value >= Math.min(value1, value2) && value <= Math.max(value1, value2);
	}

	public static RailwayData getInstance(World world) {
		if (world instanceof ServerWorld) {
			return ((ServerWorld) world).getPersistentStateManager().getOrCreate(RailwayData::new, NAME);
		} else {
			return null;
		}
	}
}
