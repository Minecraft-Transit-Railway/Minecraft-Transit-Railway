package mtr.data;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockTrainAnnouncer;
import mtr.block.BlockTrainSensor;
import mtr.config.CustomResources;
import mtr.path.PathData;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public class TrainServer extends Train {

	private boolean canDeploy;
	private Set<UUID> trainPositions;
	private Map<PlayerEntity, Set<TrainServer>> trainsInPlayerRange = new HashMap<>();
	private final Map<Long, Route.ScheduleEntry> schedule = new HashMap<>();

	private final List<Float> timePoints;

	private static final int TRAIN_UPDATE_DISTANCE = 128;
	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	public TrainServer(long id, long sidingId, float railLength, CustomResources.TrainMapping trainMapping, int trainLength, List<PathData> path, List<Float> distances, List<Float> timePoints) {
		super(id, sidingId, railLength, trainMapping, trainLength, path, distances);
		this.timePoints = timePoints;
	}

	public TrainServer(long sidingId, float railLength, List<PathData> path, List<Float> distances, List<Float> timePoints, NbtCompound nbtCompound) {
		super(sidingId, railLength, path, distances, nbtCompound);
		this.timePoints = timePoints;
	}

	@Override
	protected void startUp(World world, int trainLength, int trainSpacing, boolean isOppositeRail) {
		canDeploy = false;
		isOnRoute = true;
		stopCounter = 0;
		speed = ACCELERATION;
		if (isOppositeRail) {
			railProgress += trainLength * trainSpacing;
			reversed = !reversed;
		}
		nextStoppingIndex = getNextStoppingIndex();
	}

	@Override
	protected void simulateCar(
			World world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double prevCarX, double prevCarY, double prevCarZ, float prevCarYaw, float prevCarPitch,
			boolean doorLeftOpen, boolean doorRightOpen, double realSpacing,
			float doorValueRaw, float oldSpeed, float oldDoorValue, float oldRailProgress
	) {
		final TrainType trainType = trainMapping.trainType;
		final float doorValue = Math.abs(doorValueRaw);
		final float halfSpacing = trainType.getSpacing() / 2F;
		final float halfWidth = trainType.width / 2F;

		final BlockPos soundPos = new BlockPos(carX, carY, carZ);
		trainType.playSpeedSoundEffect(world, soundPos, oldSpeed, speed);

		if (doorLeftOpen || doorRightOpen) {
			if (oldDoorValue <= 0 && doorValue > 0 && trainType.doorOpenSoundEvent != null) {
				world.playSound(null, soundPos, trainType.doorOpenSoundEvent, SoundCategory.BLOCKS, 1, 1);
			} else if (oldDoorValue >= trainType.doorCloseSoundTime && doorValue < trainType.doorCloseSoundTime && trainType.doorCloseSoundEvent != null) {
				world.playSound(null, soundPos, trainType.doorCloseSoundEvent, SoundCategory.BLOCKS, 1, 1);
			}

			final float margin = halfSpacing + BOX_PADDING;
			world.getEntitiesByClass(PlayerEntity.class, new Box(carX + margin, carY + margin, carZ + margin, carX - margin, carY - margin, carZ - margin), player -> !player.isSpectator() && !ridingEntities.contains(player.getUuid())).forEach(player -> {
				final Vec3d positionRotated = player.getPos().subtract(carX, carY, carZ).rotateY(-carYaw).rotateX(-carPitch);
				if (Math.abs(positionRotated.x) < halfWidth + INNER_PADDING && Math.abs(positionRotated.y) < 1.5 && Math.abs(positionRotated.z) <= halfSpacing) {
					ridingEntities.add(player.getUuid());
					final PacketByteBuf packet = PacketByteBufs.create();
					packet.writeLong(id);
					packet.writeFloat((float) (positionRotated.x / trainType.width + 0.5));
					packet.writeFloat((float) (positionRotated.z / realSpacing + 0.5) + ridingCar);
					ServerPlayNetworking.send((ServerPlayerEntity) player, PACKET_UPDATE_TRAIN_RIDING_POSITION, packet);
				}
			});
		}

		final RailwayData railwayData = RailwayData.getInstance(world);
		final Set<UUID> entitiesToRemove = new HashSet<>();
		ridingEntities.forEach(uuid -> {
			final PlayerEntity player = world.getPlayerByUuid(uuid);
			if (player != null) {
				final Vec3d positionRotated = player.getPos().subtract(carX, carY, carZ).rotateY(-carYaw).rotateX(-carPitch);
				if (player.isSpectator() || player.isSneaking() || (doorLeftOpen || doorRightOpen) && Math.abs(positionRotated.z) <= halfSpacing && (Math.abs(positionRotated.x) > halfWidth + INNER_PADDING || Math.abs(positionRotated.y) > 1.5)) {
					entitiesToRemove.add(uuid);
				}
				if (railwayData != null) {
					railwayData.updatePlayerRiding(player);
				}
			}
		});
		if (!entitiesToRemove.isEmpty()) {
			entitiesToRemove.forEach(ridingEntities::remove);
		}
	}

	@Override
	protected void handlePositions(World world, Vec3d[] positions, float ticksElapsed, float doorValueRaw, float oldDoorValue, float oldRailProgress) {
		final Box trainBox = new Box(positions[0], positions[positions.length - 1]).expand(TRAIN_UPDATE_DISTANCE);
		world.getPlayers().forEach(player -> {
			if (trainBox.contains(player.getPos())) {
				if (!trainsInPlayerRange.containsKey(player)) {
					trainsInPlayerRange.put(player, new HashSet<>());
				}
				trainsInPlayerRange.get(player).add(this);
			}
		});

		final BlockPos frontPos = new BlockPos(positions[reversed ? positions.length - 1 : 0]);
		if (world.isChunkLoaded(frontPos.getX() / 16, frontPos.getZ() / 16)) {
			for (int i = 0; i <= 3; i++) {
				final BlockPos checkPos = frontPos.down(i);
				final Block block = world.getBlockState(checkPos).getBlock();
				if (block instanceof BlockTrainSensor) {
					world.setBlockState(checkPos, world.getBlockState(checkPos).with(BlockTrainSensor.POWERED, true));
					world.getBlockTickScheduler().schedule(checkPos, block, 20);
					break;
				}
			}
		}
		if (!ridingEntities.isEmpty()) {
			for (int i = 0; i <= 3; i++) {
				final BlockPos checkPos = new BlockPos(frontPos).down(i);
				if (world.getBlockState(checkPos).getBlock() instanceof BlockTrainAnnouncer) {
					final BlockEntity entity = world.getBlockEntity(checkPos);
					if (entity instanceof BlockTrainAnnouncer.TileEntityTrainAnnouncer) {
						ridingEntities.forEach(uuid -> ((BlockTrainAnnouncer.TileEntityTrainAnnouncer) entity).announce(world.getPlayerByUuid(uuid)));
					}
				}
			}
		}
	}

	@Override
	protected boolean canDeploy(Depot depot) {
		if (path.size() > 1 && depot != null) {
			depot.requestDeploy(sidingId, this);
		}
		return canDeploy;
	}

	@Override
	protected boolean isRailBlocked(int checkIndex) {
		if (trainPositions != null && checkIndex < path.size()) {
			return trainPositions.contains(path.get(checkIndex).getRailProduct());
		} else {
			return false;
		}
	}

	@Override
	protected boolean skipScanBlocks(World world, double trainX, double trainY, double trainZ) {
		return world.getClosestPlayer(trainX, trainY, trainZ, MAX_CHECK_DISTANCE, entity -> true) == null;
	}

	@Override
	protected boolean openDoors(World world, Block block, BlockPos checkPos, float doorValue) {
		if (block instanceof BlockPSDAPGDoorBase) {
			for (int i = -1; i <= 1; i++) {
				final BlockPos doorPos = checkPos.up(i);
				final BlockState state = world.getBlockState(doorPos);
				final Block doorBlock = state.getBlock();

				if (doorBlock instanceof BlockPSDAPGDoorBase) {
					final int doorStateValue = (int) MathHelper.clamp(doorValue * DOOR_MOVE_TIME, 0, BlockPSDAPGDoorBase.MAX_OPEN_VALUE);
					world.setBlockState(doorPos, state.with(BlockPSDAPGDoorBase.OPEN, doorStateValue));

					if (doorStateValue > 0 && !world.getBlockTickScheduler().isScheduled(checkPos.up(), doorBlock)) {
						world.getBlockTickScheduler().schedule(new BlockPos(doorPos), doorBlock, 20);
					}
				}
			}
		}

		return false;
	}

	public boolean simulateTrain(World world, float ticksElapsed, Depot depot, DataCache dataCache, Set<UUID> trainPositions, Map<PlayerEntity, Set<TrainServer>> trainsInPlayerRange, Map<Long, Set<Route.ScheduleEntry>> schedulesForPlatform, boolean isUnlimited) {
		this.trainPositions = trainPositions;
		this.trainsInPlayerRange = trainsInPlayerRange;
		final int oldStoppingIndex = nextStoppingIndex;
		final int oldPassengerCount = ridingEntities.size();
		final int index = getIndex(0, 0, true);

		simulateTrain(world, ticksElapsed, depot);

		final boolean update = oldPassengerCount > ridingEntities.size() || oldStoppingIndex != nextStoppingIndex;
		final PathData currentPathData = path.get(index);
		final int currentDwellTime = currentPathData.dwellTime;
		final long currentMillis = System.currentTimeMillis();

		if (update || schedule.isEmpty() || speed == 0 && currentDwellTime == 0) {
			schedule.clear();
			final long startingMillis = currentMillis + (long) (((isOnRoute ? 0 : Math.max(0, depot.getNextDepartureTicks(Depot.getHour(world)))) - timePoints.get(index) - currentDwellTime * 10L) * Depot.MILLIS_PER_TICK);
			for (int i = index + 1; i < path.size() + (isUnlimited ? 0 : index); i++) {
				final PathData pathData = path.get(i % path.size());
				if (pathData.dwellTime > 0) {
					final float extraTicks = i >= timePoints.size() ? timePoints.get(timePoints.size() - 1) : 0;
					final long arrivalMillis = startingMillis + (long) ((timePoints.get(i % timePoints.size()) + extraTicks) * Depot.MILLIS_PER_TICK);
					addScheduleEntry(pathData, depot, dataCache, arrivalMillis);
				}
			}
		}

		final long currentPlatformId = currentPathData.savedRailBaseId;
		if (currentPlatformId != 0 && speed == 0 && !schedule.containsKey(currentPlatformId)) {
			addScheduleEntry(currentPathData, depot, dataCache, 0);
		}

		schedule.forEach((platformId, scheduleEntry) -> {
			if (!schedulesForPlatform.containsKey(platformId)) {
				schedulesForPlatform.put(platformId, new HashSet<>());
			}
			final long arrivalMillis = scheduleEntry.arrivalMillis;
			final long newArrivalMillis = currentPlatformId == platformId && speed == 0 ? 0 : Math.max(currentMillis + 2000, arrivalMillis);
			schedulesForPlatform.get(platformId).add(newArrivalMillis == arrivalMillis ? scheduleEntry : new Route.ScheduleEntry(scheduleEntry, newArrivalMillis));
		});

		return update;
	}

	public void writeTrainPositions(Set<UUID> trainPositions) {
		if (!path.isEmpty()) {
			final int trainSpacing = trainMapping.trainType.getSpacing();
			final int headIndex = getIndex(0, trainSpacing, true);
			final int tailIndex = getIndex(trainLength, trainSpacing, false);
			for (int i = tailIndex; i <= headIndex; i++) {
				if (i > 0 && path.get(i).savedRailBaseId != sidingId) {
					trainPositions.add(path.get(i).getRailProduct());
				}
			}
		}
	}

	public void deployTrain() {
		canDeploy = true;
	}

	private int getNextStoppingIndex() {
		final int headIndex = getIndex(0, 0, false);
		for (int i = headIndex; i < path.size(); i++) {
			if (path.get(i).dwellTime > 0) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private void addScheduleEntry(final PathData pathData, Depot depot, DataCache dataCache, long arrivalMillis) {
		final long platformId = pathData.savedRailBaseId;
		RailwayData.useRoutesAndStationsFromIndex(pathData.stopIndex, depot.routeIds, dataCache, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
			if (lastStation != null) {
				final String destinationString;
				if (thisRoute != null && thisRoute.isLightRailRoute) {
					final String lightRailRouteNumber = thisRoute.lightRailRouteNumber;
					final String[] lastStationSplit = lastStation.name.split("\\|");
					final StringBuilder destination = new StringBuilder();
					for (final String lastStationSplitPart : lastStationSplit) {
						destination.append("|").append(lightRailRouteNumber.isEmpty() ? "" : lightRailRouteNumber + " ").append(lastStationSplitPart);
					}
					destinationString = destination.length() > 0 ? destination.substring(1) : "";
				} else {
					destinationString = lastStation.name;
				}

				schedule.put(platformId, new Route.ScheduleEntry(arrivalMillis, trainMapping.trainType, trainLength, platformId, destinationString, nextStation == null));
			}
		});
	}
}
