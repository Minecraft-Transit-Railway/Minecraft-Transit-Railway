package mtr.data;

import mtr.block.BlockPlatformRail;
import mtr.entity.EntityTrainBase;
import mtr.packet.PacketTrainDataGuiServer;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameRules;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class RailwayData extends PersistentState {

	public static final int STATION_COOL_DOWN = 120;
	public static final int TRAIN_STOP_TIME = 20;

	private static final String NAME = "mtr_train_data";
	private static final String KEY_STATIONS = "stations";
	private static final String KEY_PLATFORMS = "platforms";
	private static final String KEY_ROUTES = "routes";
	private static final String KEY_TRAINS = "trains";

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

	public void checkPlatformPos(WorldAccess world, BlockPos pos) {
		validatePlatforms(world);
		final Platform newPlatform = BlockPlatformRail.createNewPlatform(world, pos);
		if (newPlatform != null && platforms.stream().noneMatch(platform -> platform.getPos1().equals(newPlatform.getPos1()))) {
			platforms.add(newPlatform);
		}
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

		new HashSet<>(trains).forEach(train -> {
			final int trainLength = train.posX.length;
			final boolean isDeadTrain = train.paths.isEmpty();
			final int pathLength = isDeadTrain ? 0 : train.paths.get(0).size();
			final int distanceRemaining = pathLength - Math.max(train.pathIndex[0], train.pathIndex[trainLength - 1]);

			if (distanceRemaining <= 0) {
				if (isDeadTrain) {
					trainsToRemove.add(train);
				} else if (train.stationCoolDown < STATION_COOL_DOWN) {
					train.stationCoolDown++;
				} else {
					train.paths.remove(0);
					for (int i = 0; i < trainLength; i++) {
						train.pathIndex[i] = 0;
					}
					train.stationCoolDown = 0;
				}
			} else {
				if (MathHelper.square(train.speed) >= 2 * train.trainType.getAcceleration() * (distanceRemaining - 1)) {
					if (train.speed >= train.trainType.getAcceleration() * 2) {
						train.speed -= train.trainType.getAcceleration();
					}
				} else if (train.speed < train.trainType.getMaxSpeed()) {
					train.speed += train.trainType.getAcceleration();
				}
				train.speed = MathHelper.clamp(train.speed, train.trainType.getAcceleration(), train.trainType.getMaxSpeed());
			}

			if (!train.paths.isEmpty()) {
				final boolean isForwards = train.pathIndex[0] > train.pathIndex[trainLength - 1];
				for (int i = 0; i < trainLength; i++) {
					if (train.pathIndex[i] < pathLength - train.trainType.getSpacing() * (isForwards ? i : trainLength - i - 1)) {
						final Pos3f newPos = train.paths.get(0).get(train.pathIndex[i]);
						final Pos3f movement = new Pos3f(newPos.getX() - train.posX[i], newPos.getY() - train.posY[i], newPos.getZ() - train.posZ[i]);

						if (movement.lengthSquared() < MathHelper.square(2 * train.speed)) {
							train.pathIndex[i] += Math.ceil(train.speed);
						}

						movement.normalize();
						movement.scale(train.speed);
						train.posX[i] += movement.getX();
						train.posY[i] += movement.getY();
						train.posZ[i] += movement.getZ();
					}
				}
			}

			for (int i = 0; i < trainLength - 1; i++) {
				final float xAverage = (train.posX[i] + train.posX[i + 1]) / 2;
				final float yAverage = (train.posY[i] + train.posY[i + 1]) / 2;
				final float zAverage = (train.posZ[i] + train.posZ[i + 1]) / 2;

				if (world.getChunkManager().shouldTickBlock(new BlockPos(xAverage, yAverage, zAverage))) {
					final float yaw = (float) Math.toDegrees(MathHelper.atan2(train.posX[i + 1] - train.posX[i], train.posZ[i + 1] - train.posZ[i]));
					final float pitch = (float) Math.toDegrees(Math.asin((train.posY[i + 1] - train.posY[i]) / train.trainType.getSpacing()));

					if (train.entities[i] != null) {
						final EntityTrainBase trainEntity = train.entities[i];
						final double prevTrainX = trainEntity.getX();
						final double prevTrainZ = trainEntity.getZ();

						trainEntity.updatePositionAndAngles(xAverage, yAverage, zAverage, yaw, pitch);
						trainEntity.setSpeed(train.speed);
						trainEntity.stationCoolDown = train.stationCoolDown;

						final float motionYaw = (float) Math.toDegrees(MathHelper.atan2(xAverage - prevTrainX, zAverage - prevTrainZ));
						trainEntity.setHead1IsFront(MathHelper.angleBetween(yaw, motionYaw) < 90);
					} else {
						train.entities[i] = train.trainType.create((World) world, xAverage, yAverage, zAverage);
						train.entities[i].updatePositionAndAngles(xAverage, yAverage, zAverage, yaw, pitch);
						train.entities[i].setIsEndHead(i == trainLength - 2, i == 0);
						world.spawnEntity(train.entities[i]);
					}
				} else {
					if (train.entities[i] != null) {
						train.entities[i].kill();
						train.entities[i] = null;
					}
				}
			}
		});

		trainsToRemove.forEach(this::removeTrain);
		PacketTrainDataGuiServer.sendTrainsS2C(world, trains);

		new Thread(() -> {
			if (world.getLevelProperties().getGameRules().get(GameRules.DO_DAYLIGHT_CYCLE).get()) {
				new HashSet<>(platforms).forEach(platform -> {
					final Train newTrain = platform.createTrainOnPlatform(world, platforms, routes, worldTime);
					if (newTrain != null) {
						trains.add(newTrain);
					}
				});
			}
		}).start();

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
		new HashSet<>(platforms).forEach(platform -> platform.updateDimensions(world));
		platforms.removeIf(platform -> !(world.getBlockState(platform.getMidPos()).getBlock() instanceof BlockPlatformRail));

		final Set<Platform> platformsToRemove = new HashSet<>();
		final Set<BlockPos> uniquePositions = new HashSet<>();
		new HashSet<>(platforms).forEach(platform -> {
			final BlockPos checkPos = platform.getPos1();
			if (uniquePositions.contains(checkPos)) {
				platformsToRemove.add(platform);
			} else {
				uniquePositions.add(checkPos);
			}
		});
		platforms.removeAll(platformsToRemove);

		validateData();
	}

	private void validateData() {
		new HashSet<>(routes).forEach(route -> route.platformIds.removeIf(platformId -> getDataById(platforms, platformId) == null));
		new HashSet<>(platforms).forEach(platform -> platform.routeIds.removeIf(routeId -> getDataById(routes, routeId) == null));
		markDirty();
	}

	// static finders

	public static <T extends DataBase> T getDataById(Set<T> data, long id) {
		return data.stream().filter(item -> item.id == id).findFirst().orElse(null);
	}

	public static Platform getPlatformByPos(Set<Platform> platforms, BlockPos pos) {
		return platforms.stream().filter(platform -> platform.getPos1().equals(pos)).findFirst().orElse(null);
	}

	public static Station getStationByPlatform(Set<Station> stations, Platform platform) {
		final BlockPos pos1 = platform.getPos1();
		return stations.stream().filter(station -> station.inStation(pos1.getX(), pos1.getZ())).findFirst().orElse(null);
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
}
