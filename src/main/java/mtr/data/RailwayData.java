package mtr.data;

import mtr.block.BlockPlatform;
import mtr.packet.PacketTrainDataGuiServer;
import mtr.path.PathFinderBase;
import mtr.path.RoutePathFinder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.*;

public class RailwayData extends PersistentState {

	public static final int STATION_COOL_DOWN = 120;
	public static final int TRAIN_STOP_TIME = 20;

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_TRAINS = "trains";
	private static final String KEY_TRAIN_SPAWNERS = "train_spawners";

	private static final int VIEW_DISTANCE = 32;

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

		validateData();
	}

	@Override
	public CompoundTag toTag(CompoundTag tag) {
		validateData();
		final CompoundTag tagStations = new CompoundTag();
		int i = 0;
		for (Station station : stations) {
			tagStations.put(KEY_STATIONS + i, station.toCompoundTag());
			i++;
		}
		tag.put(KEY_STATIONS, tagStations);

		final CompoundTag tagNewPlatforms = new CompoundTag();
		int j = 0;
		for (Platform platform : platforms) {
			tagNewPlatforms.put(KEY_PLATFORMS + j, platform.toCompoundTag());
			j++;
		}
		tag.put(KEY_PLATFORMS, tagNewPlatforms);

		final CompoundTag tagNewRoutes = new CompoundTag();
		int k = 0;
		for (Route route : routes) {
			tagNewRoutes.put(KEY_ROUTES + k, route.toCompoundTag());
			k++;
		}
		tag.put(KEY_ROUTES, tagNewRoutes);

		final CompoundTag tagNewTrains = new CompoundTag();
		int l = 0;
		for (Train train : trains) {
			tagNewTrains.put(KEY_TRAINS + l, train.toCompoundTag());
			l++;
		}
		tag.put(KEY_TRAINS, tagNewTrains);

		return tag;
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
		validatePlatforms(world);
		return platforms;
	}

	public Set<Route> getRoutes() {
		validateData();
		return routes;
	}

	public void simulateTrains(WorldAccess world) {
		final int worldTime = (int) (world.getLunarTime() + 6000) % (Platform.HOURS_IN_DAY * Platform.TICKS_PER_HOUR);
		final Set<Train> trainsToRemove = new HashSet<>();

		trains.forEach(train -> {
			final int trainLength = train.posX.length;
			final int distanceRemaining = train.path.size() - Math.max(train.pathIndex[0], train.pathIndex[trainLength - 1]);

			if (distanceRemaining <= 0) {
				train.speed = 0;

				if (train.stationCoolDown < STATION_COOL_DOWN) {
					train.stationCoolDown++;
				} else if (train.stationIds.isEmpty()) {
					// TODO dead train finds path to station spawner
					trainsToRemove.add(train);
				} else {
					final Station station = getStationById(stations, train.stationIds.get(0));

					if (station != null) {
						final BlockPos start1 = new BlockPos(train.posX[0], train.posY[0], train.posZ[0]);
						final BlockPos start2 = new BlockPos(train.posX[trainLength - 1], train.posY[trainLength - 1], train.posZ[trainLength - 1]);
						final BlockPos destinationPos = station.getCenter();
						final boolean reverse = PathFinderBase.distanceBetween(start1, destinationPos) > PathFinderBase.distanceBetween(start2, destinationPos);
						final RoutePathFinder routePathFinder = new RoutePathFinder(world, reverse ? start1 : start2, station);

						train.resetPathIndex(reverse);
						train.path.clear();
						train.path.addAll(routePathFinder.findPath());
						train.stationCoolDown = 0;
					}
					train.stationIds.remove(0);
				}
			} else {
				if (MathHelper.square(train.speed) >= 2 * train.trainType.getAcceleration() * (distanceRemaining - 1)) {
					if (train.speed >= train.trainType.getAcceleration() * 2) {
						train.speed -= train.trainType.getAcceleration();
					}
				} else if (train.speed < train.trainType.getMaxSpeed()) {
					train.speed += train.trainType.getAcceleration();
				}

				for (int i = 0; i < trainLength; i++) {
					if (train.pathIndex[i] < train.path.size()) {
						final Pos3f newPos = train.path.get(train.pathIndex[i]);
						final Pos3f movement = new Pos3f(newPos.getX() - train.posX[i], newPos.getY() - train.posY[i], newPos.getZ() - train.posZ[i]);

						if (movement.lengthSquared() < MathHelper.square(2 * train.speed)) {
							train.pathIndex[i]++;
						}

						movement.normalize();
						movement.scale(train.speed);
						train.posX[i] += movement.getX();
						train.posY[i] += movement.getY();
						train.posZ[i] += movement.getZ();

						if (i == 0 || i == trainLength - 1) {
							final BlockPos posBelow = new BlockPos(train.posX[i], train.posY[i] - 1, train.posZ[i]);
							if (world.getBlockState(posBelow).getBlock() instanceof BlockPlatform && getPlatformByPos(platforms, posBelow).removeTrains) {
								trainsToRemove.add(train);
							}
						}
					}
				}
			}

			final List<? extends PlayerEntity> players = world.getPlayers();
			for (int i = 0; i < trainLength - 1; i++) {
				final float xAverage = (train.posX[i] + train.posX[i + 1]) / 2;
				final float yAverage = (train.posY[i] + train.posY[i + 1]) / 2;
				final float zAverage = (train.posZ[i] + train.posZ[i + 1]) / 2;
				final boolean playerNearby = players.stream().anyMatch(player -> PathFinderBase.distanceBetween(new BlockPos(xAverage, yAverage, zAverage), player.getBlockPos()) < VIEW_DISTANCE);

				if (playerNearby && train.entities[i] == null) {
					train.entities[i] = train.trainType.create((World) world, xAverage, yAverage, zAverage);
					world.spawnEntity(train.entities[i]);
				}
				if (train.entities[i] != null) {
					if (playerNearby) {
						final float yaw = (float) Math.toDegrees(MathHelper.atan2(train.posX[i + 1] - train.posX[i], train.posZ[i + 1] - train.posZ[i]));
						final float pitch = (float) Math.toDegrees(Math.asin((train.posY[i + 1] - train.posY[i]) / train.trainType.getSpacing()));
						train.entities[i].updatePositionAndAngles(xAverage, yAverage, zAverage, yaw, pitch);
						train.entities[i].stationCoolDown = train.stationCoolDown;
					} else {
						train.entities[i].kill();
						train.entities[i] = null;
					}
				}
			}
		});

		trainsToRemove.forEach(this::removeTrain);
		PacketTrainDataGuiServer.sendTrainsS2C(world, trains);

		if (world.getLevelProperties().getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).get()) {
			platforms.forEach(platform -> {
				final Train newTrain = platform.createTrainOnPlatform(routes, worldTime);
				if (newTrain != null) {
					trains.add(newTrain);
				}
			});
		}

		markDirty();
	}

	// writing data

	public void setData(Set<Station> stations, Set<Route> routes) {
		this.stations.clear();
		this.stations.addAll(stations);
		this.routes.clear();
		this.routes.addAll(routes);
		validateData();
	}

	public void setData(WorldAccess world, Platform newPlatform) {
		platforms.removeIf(platform -> platform.getPos1().equals(newPlatform.getPos1()));
		platforms.add(newPlatform);
		validatePlatforms(world);
	}

	private void removeTrain(Train train) {
		Arrays.stream(train.entities).filter(Objects::nonNull).forEach(Entity::kill);
		trains.remove(train);
	}

	// validation

	private void validatePlatforms(WorldAccess world) {
		platforms.removeIf(platform -> !platform.hasRail(world));
		validateData();
	}

	private void validateData() {
		routes.forEach(route -> route.stationIds.removeIf(stationId -> getStationById(stations, stationId) == null));
		trains.removeIf(train -> train.stationIds.isEmpty());
		platforms.forEach(platform -> platform.routeIds.removeIf(routeId -> getRouteById(routes, routeId) == null));
		markDirty();
	}

	// static finders

	public static Station getStationById(Set<Station> stations, long id) {
		return stations.stream().filter(station -> station.id == id).findFirst().orElse(null);
	}

	public static Route getRouteById(Set<Route> routes, long id) {
		return routes.stream().filter(route -> route.id == id).findFirst().orElse(null);
	}

	public static Platform getPlatformByPos(Set<Platform> platforms, BlockPos pos) {
		return platforms.stream().filter(platform -> platform.getPos1().equals(pos)).findFirst().orElse(null);
	}

	// other

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
