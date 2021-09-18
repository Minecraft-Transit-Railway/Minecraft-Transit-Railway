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

	private final List<Siding.TimeSegment> timeSegments;

	private static final int TRAIN_UPDATE_DISTANCE = 128;
	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	public TrainServer(long id, long sidingId, float railLength, CustomResources.TrainMapping trainMapping, int trainLength, List<PathData> path, List<Float> distances, List<Siding.TimeSegment> timeSegments) {
		super(id, sidingId, railLength, trainMapping, trainLength, path, distances);
		this.timeSegments = timeSegments;
	}

	public TrainServer(long sidingId, float railLength, List<PathData> path, List<Float> distances, List<Siding.TimeSegment> timeSegments, NbtCompound nbtCompound) {
		super(sidingId, railLength, path, distances, nbtCompound);
		this.timeSegments = timeSegments;
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

		simulateTrain(world, ticksElapsed, depot);

		final long currentMillis = System.currentTimeMillis() - (long) (stopCounter * Depot.MILLIS_PER_TICK) + (isOnRoute ? 0 : (long) Math.max(0, depot.getNextDepartureTicks(Depot.getHour(world))) * Depot.MILLIS_PER_TICK);
		float currentTime = -1;
		for (final Siding.TimeSegment timeSegment : timeSegments) {
			if (currentTime < 0 && RailwayData.isBetween(railProgress, timeSegment.startRailProgress, timeSegment.endRailProgress)) {
				currentTime = timeSegment.getTime(railProgress);
			}

			if (currentTime >= 0 && timeSegment.savedRailBaseId != 0) {
				if (timeSegment.routeId == 0) {
					RailwayData.useRoutesAndStationsFromIndex(path.get(getIndex(timeSegment.endRailProgress, true)).stopIndex - 1, depot.routeIds, dataCache, (thisRoute, nextRoute, thisStation, nextStation, lastStation) -> {
						timeSegment.lastStationId = lastStation == null ? 0 : lastStation.id;
						timeSegment.routeId = thisRoute == null ? 0 : thisRoute.id;
						timeSegment.isTerminating = nextStation == null;
					});
				}

				final String destinationString;
				final Station lastStation = dataCache.stationIdMap.get(timeSegment.lastStationId);
				if (lastStation != null) {
					final Route thisRoute = dataCache.routeIdMap.get(timeSegment.routeId);
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
				} else {
					destinationString = "";
				}

				if (!schedulesForPlatform.containsKey(timeSegment.savedRailBaseId)) {
					schedulesForPlatform.put(timeSegment.savedRailBaseId, new HashSet<>());
				}

				final long arrivalMillis = currentMillis + (long) (timeSegment.getTimeDifference(currentTime) * Depot.MILLIS_PER_TICK);
				schedulesForPlatform.get(timeSegment.savedRailBaseId).add(new Route.ScheduleEntry(arrivalMillis, trainMapping.trainType, trainLength, timeSegment.savedRailBaseId, destinationString, timeSegment.isTerminating));
			}
		}

		return oldPassengerCount > ridingEntities.size() || oldStoppingIndex != nextStoppingIndex;
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
}
