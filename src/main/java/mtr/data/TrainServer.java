package mtr.data;

import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockTrainAnnouncer;
import mtr.block.BlockTrainRedstoneSensor;
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
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.Consumer;

public class TrainServer extends Train {

	private boolean canDeploy;
	private List<Map<UUID, Long>> trainPositions;
	private Map<PlayerEntity, Set<TrainServer>> trainsInPlayerRange = new HashMap<>();
	private long routeId;

	private final List<Siding.TimeSegment> timeSegments;

	private static final int TRAIN_UPDATE_DISTANCE = 128;
	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	public TrainServer(long id, long sidingId, float railLength, String trainId, TrainType baseTrainType, int trainCars, List<PathData> path, List<Float> distances, List<Siding.TimeSegment> timeSegments) {
		super(id, sidingId, railLength, trainId, baseTrainType, trainCars, path, distances);
		this.timeSegments = timeSegments;
	}

	public TrainServer(long sidingId, float railLength, List<PathData> path, List<Float> distances, List<Siding.TimeSegment> timeSegments, NbtCompound nbtCompound) {
		super(sidingId, railLength, path, distances, nbtCompound);
		this.timeSegments = timeSegments;
	}

	@Override
	protected void startUp(World world, int trainCars, int trainSpacing, boolean isOppositeRail) {
		canDeploy = false;
		isOnRoute = true;
		stopCounter = 0;
		speed = ACCELERATION;
		if (isOppositeRail) {
			railProgress += trainCars * trainSpacing;
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
		final float halfSpacing = baseTrainType.getSpacing() / 2F;
		final float halfWidth = baseTrainType.width / 2F;

		if (doorLeftOpen || doorRightOpen) {
			final float margin = halfSpacing + BOX_PADDING;
			world.getEntitiesByClass(PlayerEntity.class, new Box(carX + margin, carY + margin, carZ + margin, carX - margin, carY - margin, carZ - margin), player -> !player.isSpectator() && !ridingEntities.contains(player.getUuid())).forEach(player -> {
				final Vec3d positionRotated = player.getPos().subtract(carX, carY, carZ).rotateY(-carYaw).rotateX(-carPitch);
				if (Math.abs(positionRotated.x) < halfWidth + INNER_PADDING && Math.abs(positionRotated.y) < 1.5 && Math.abs(positionRotated.z) <= halfSpacing) {
					ridingEntities.add(player.getUuid());
					final PacketByteBuf packet = PacketByteBufs.create();
					packet.writeLong(id);
					packet.writeFloat((float) (positionRotated.x / baseTrainType.width + 0.5));
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
			checkBlock(frontPos, checkPos -> {
				final BlockState state = world.getBlockState(checkPos);
				if (state.getBlock() instanceof BlockTrainRedstoneSensor) {
					final BlockEntity entity = world.getBlockEntity(checkPos);
					if (entity instanceof BlockTrainRedstoneSensor.TileEntityTrainRedstoneSensor && ((BlockTrainRedstoneSensor.TileEntityTrainRedstoneSensor) entity).matchesFilter(routeId)) {
						world.setBlockState(checkPos, state.with(BlockTrainRedstoneSensor.POWERED, true));
						world.getBlockTickScheduler().schedule(checkPos, state.getBlock(), 20);
					}
				}
			});
		}
		if (!ridingEntities.isEmpty()) {
			checkBlock(frontPos, checkPos -> {
				if (world.getBlockState(checkPos).getBlock() instanceof BlockTrainAnnouncer) {
					final BlockEntity entity = world.getBlockEntity(checkPos);
					if (entity instanceof BlockTrainAnnouncer.TileEntityTrainAnnouncer && ((BlockTrainAnnouncer.TileEntityTrainAnnouncer) entity).matchesFilter(routeId)) {
						ridingEntities.forEach(uuid -> ((BlockTrainAnnouncer.TileEntityTrainAnnouncer) entity).announce(world.getPlayerByUuid(uuid)));
					}
				}
			});
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
			final UUID railProduct = path.get(checkIndex).getRailProduct();
			for (final Map<UUID, Long> trainPositionsMap : trainPositions) {
				if (trainPositionsMap.containsKey(railProduct) && trainPositionsMap.get(railProduct) != id) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected boolean skipScanBlocks(World world, double trainX, double trainY, double trainZ) {
		return world.getClosestPlayer(trainX, trainY, trainZ, MAX_CHECK_DISTANCE, entity -> true) == null;
	}

	@Override
	protected boolean openDoors(World world, Block block, BlockPos checkPos, float doorValue, int dwellTicks) {
		if (block instanceof BlockPSDAPGDoorBase) {
			for (int i = -1; i <= 1; i++) {
				final BlockPos doorPos = checkPos.up(i);
				final BlockState state = world.getBlockState(doorPos);
				final Block doorBlock = state.getBlock();

				if (doorBlock instanceof BlockPSDAPGDoorBase) {
					final int doorStateValue = (int) MathHelper.clamp(doorValue * DOOR_MOVE_TIME, 0, BlockPSDAPGDoorBase.MAX_OPEN_VALUE);
					world.setBlockState(doorPos, state.with(BlockPSDAPGDoorBase.OPEN, doorStateValue));

					if (doorStateValue > 0 && !world.getBlockTickScheduler().isScheduled(doorPos, doorBlock)) {
						world.getBlockTickScheduler().schedule(doorPos, doorBlock, dwellTicks);
					}
				}
			}
		}

		return false;
	}

	public boolean simulateTrain(World world, float ticksElapsed, Depot depot, DataCache dataCache, List<Map<UUID, Long>> trainPositions, Map<PlayerEntity, Set<TrainServer>> trainsInPlayerRange, Map<Long, Set<Route.ScheduleEntry>> schedulesForPlatform, boolean isUnlimited) {
		this.trainPositions = trainPositions;
		this.trainsInPlayerRange = trainsInPlayerRange;
		final int oldStoppingIndex = nextStoppingIndex;
		final int oldPassengerCount = ridingEntities.size();

		simulateTrain(world, ticksElapsed, depot);

		final long currentMillis = System.currentTimeMillis() - (long) (stopCounter * Depot.MILLIS_PER_TICK) + (isOnRoute ? 0 : (long) Math.max(0, depot.getNextDepartureTicks(Depot.getHour(world))) * Depot.MILLIS_PER_TICK);

		float currentTime = -1;
		int startingIndex = 0;
		for (final Siding.TimeSegment timeSegment : timeSegments) {
			if (RailwayData.isBetween(railProgress, timeSegment.startRailProgress, timeSegment.endRailProgress)) {
				currentTime = timeSegment.getTime(railProgress);
				break;
			}
			startingIndex++;
		}

		if (currentTime >= 0) {
			float offsetTime = 0;
			routeId = 0;
			for (int i = startingIndex; i < timeSegments.size() + (isUnlimited ? 0 : startingIndex); i++) {
				final Siding.TimeSegment timeSegment = timeSegments.get(i % timeSegments.size());

				if (timeSegment.savedRailBaseId != 0) {
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

					final long platformId = timeSegment.savedRailBaseId;
					if (!schedulesForPlatform.containsKey(platformId)) {
						schedulesForPlatform.put(platformId, new HashSet<>());
					}

					final long arrivalMillis = currentMillis + (long) ((timeSegment.endTime + offsetTime - currentTime) * Depot.MILLIS_PER_TICK);
					schedulesForPlatform.get(platformId).add(new Route.ScheduleEntry(arrivalMillis, trainCars, platformId, timeSegment.routeId, destinationString, timeSegment.isTerminating));
				}

				if (routeId == 0) {
					routeId = timeSegment.routeId;
				}

				if (i == timeSegments.size() - 1) {
					offsetTime = timeSegment.endTime;
				}
			}
		}

		return oldPassengerCount > ridingEntities.size() || oldStoppingIndex != nextStoppingIndex;
	}

	public void writeTrainPositions(Map<UUID, Long> trainPositions, SignalBlocks signalBlocks) {
		if (!path.isEmpty()) {
			final int trainSpacing = baseTrainType.getSpacing();
			final int headIndex = getIndex(0, trainSpacing, true);
			final int tailIndex = getIndex(trainCars, trainSpacing, false);
			for (int i = tailIndex; i <= headIndex; i++) {
				if (i > 0 && path.get(i).savedRailBaseId != sidingId) {
					signalBlocks.occupy(path.get(i).getRailProduct(), trainPositions, id);
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

	private void checkBlock(BlockPos pos, Consumer<BlockPos> callback) {
		final int checkRadius = (int) Math.floor(speed);
		for (int x = -checkRadius; x <= checkRadius; x++) {
			for (int z = -checkRadius; z <= checkRadius; z++) {
				for (int y = 0; y <= 3; y++) {
					callback.accept(pos.add(x, -y, z));
				}
			}
		}
	}
}
