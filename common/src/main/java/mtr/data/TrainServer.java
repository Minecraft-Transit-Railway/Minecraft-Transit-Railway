package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.Registry;
import mtr.TrigCache;
import mtr.block.*;
import mtr.entity.EntitySeat;
import mtr.mappings.Utilities;
import mtr.path.PathData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.msgpack.value.Value;

import java.util.*;
import java.util.function.Consumer;

public class TrainServer extends Train {

	private boolean canDeploy;
	private List<Map<UUID, Long>> trainPositions;
	private Map<Player, Set<TrainServer>> trainsInPlayerRange = new HashMap<>();
	private long routeId;

	private final List<Siding.TimeSegment> timeSegments;

	private static final int TRAIN_UPDATE_DISTANCE = 128;
	private static final float INNER_PADDING = 0.5F;
	private static final int BOX_PADDING = 3;

	public TrainServer(long id, long sidingId, float railLength, String trainId, TrainType baseTrainType, int trainCars, List<PathData> path, List<Float> distances, List<Siding.TimeSegment> timeSegments) {
		super(id, sidingId, railLength, trainId, baseTrainType, trainCars, path, distances);
		this.timeSegments = timeSegments;
	}

	public TrainServer(long sidingId, float railLength, List<PathData> path, List<Float> distances, List<Siding.TimeSegment> timeSegments, Map<String, Value> map) {
		super(sidingId, railLength, path, distances, map);
		this.timeSegments = timeSegments;
	}

	@Deprecated
	public TrainServer(long sidingId, float railLength, List<PathData> path, List<Float> distances, List<Siding.TimeSegment> timeSegments, CompoundTag compoundTag) {
		super(sidingId, railLength, path, distances, compoundTag);
		this.timeSegments = timeSegments;
	}

	@Override
	protected void startUp(Level world, int trainCars, int trainSpacing, boolean isOppositeRail) {
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
			Level world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double prevCarX, double prevCarY, double prevCarZ, float prevCarYaw, float prevCarPitch,
			boolean doorLeftOpen, boolean doorRightOpen, double realSpacing,
			float doorValueRaw, float oldSpeed, float oldDoorValue, float oldRailProgress
	) {
		final RailwayData railwayData = RailwayData.getInstance(world);
		if (railwayData == null) {
			return;
		}

		final float halfSpacing = baseTrainType.getSpacing() / 2F;
		final float halfWidth = baseTrainType.width / 2F;

		if (doorLeftOpen || doorRightOpen) {
			final float margin = halfSpacing + BOX_PADDING;
			world.getEntitiesOfClass(Player.class, new AABB(carX + margin, carY + margin, carZ + margin, carX - margin, carY - margin, carZ - margin), player -> !player.isSpectator() && !ridingEntities.contains(player.getUUID())).forEach(player -> {
				final Vec3 positionRotated = player.position().subtract(carX, carY, carZ).yRot(-carYaw).xRot(-carPitch);
				if (Math.abs(positionRotated.x) < halfWidth + INNER_PADDING && Math.abs(positionRotated.y) < 2.5 && Math.abs(positionRotated.z) <= halfSpacing) {
					final EntitySeat seat = railwayData.getSeatFromPlayer(player);
					if (seat != null) {
						ridingEntities.add(player.getUUID());
						player.startRiding(seat);
						seat.updateRidingByTrainServer(id);
						seat.percentageX = (float) (positionRotated.x / baseTrainType.width + 0.5);
						seat.percentageZ = (float) (realSpacing == 0 ? 0 : positionRotated.z / realSpacing + 0.5) + ridingCar;
						seat.updateDataToClient(railProgress);
					}
					final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
					packet.writeLong(id);
					Registry.sendToPlayer((ServerPlayer) player, PACKET_UPDATE_TRAIN_PASSENGERS, packet);
				}
			});
		}

		final Set<UUID> ridersToRemove = new HashSet<>();
		ridingEntities.forEach(uuid -> {
			final Player player = world.getPlayerByUUID(uuid);
			if (player != null) {
				final Vec3 positionRotated = player.position().subtract(carX, carY, carZ).yRot(-carYaw).xRot(-carPitch);
				if (player.isSpectator() || (doorLeftOpen || doorRightOpen) && Math.abs(positionRotated.z) <= halfSpacing && (Math.abs(positionRotated.x) > halfWidth + INNER_PADDING || Math.abs(positionRotated.y) > 1.5)) {
					ridersToRemove.add(uuid);
					player.stopRiding();
				}
			}
		});
		if (!ridersToRemove.isEmpty()) {
			ridersToRemove.forEach(ridingEntities::remove);
		}
	}

	@Override
	protected boolean handlePositions(Level world, Vec3[] positions, float ticksElapsed, float doorValueRaw, float oldDoorValue, float oldRailProgress) {
		final AABB trainAABB = new AABB(positions[0], positions[positions.length - 1]).inflate(TRAIN_UPDATE_DISTANCE);
		final boolean[] playerNearby = {false};
		world.players().forEach(player -> {
			if (trainAABB.contains(player.position())) {
				if (!trainsInPlayerRange.containsKey(player)) {
					trainsInPlayerRange.put(player, new HashSet<>());
				}
				trainsInPlayerRange.get(player).add(this);
				playerNearby[0] = true;
			}
		});

		final BlockPos frontPos = new BlockPos(positions[reversed ? positions.length - 1 : 0]);
		if (world.hasChunk(frontPos.getX() / 16, frontPos.getZ() / 16)) {
			checkBlock(frontPos, checkPos -> {
				final BlockState state = world.getBlockState(checkPos);
				final Block block = state.getBlock();

				if (block instanceof BlockTrainRedstoneSensor && BlockTrainSensorBase.matchesFilter(world, checkPos, routeId, speed)) {
					((BlockTrainRedstoneSensor) block).power(world, state, checkPos);
				}

				if ((block instanceof BlockTrainCargoLoader || block instanceof BlockTrainCargoUnloader) && BlockTrainSensorBase.matchesFilter(world, checkPos, routeId, speed)) {
					for (final Direction direction : Direction.values()) {
						final Container nearbyInventory = HopperBlockEntity.getContainerAt(world, checkPos.relative(direction));
						if (nearbyInventory != null) {
							if (block instanceof BlockTrainCargoLoader) {
								transferItems(nearbyInventory, inventory);
							} else {
								transferItems(inventory, nearbyInventory);
							}
						}
					}
				}
			});
		}

		final Set<UUID> ridersToRemove = new HashSet<>();
		if (!ridingEntities.isEmpty()) {
			ridingEntities.forEach(uuid -> {
				final Player ridingPlayer = world.getPlayerByUUID(uuid);
				final RailwayData railwayData = RailwayData.getInstance(world);

				if (ridingPlayer != null && railwayData != null) {
					final EntitySeat seat = railwayData.getSeatFromPlayer(ridingPlayer);

					if (seat != null) {
						if (seat.hasPassenger(ridingPlayer) && seat.updateRidingByTrainServer(id)) {
							final CalculateCarCallback moveClient = (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
								final Vec3 playerOffset = new Vec3(getValueFromPercentage(seat.percentageX, baseTrainType.width), 0, getValueFromPercentage(Mth.frac(seat.percentageZ), realSpacingRender)).xRot(pitch).yRot(yaw).add(x, y, z);
								if (world.hasChunk((int) Math.floor(playerOffset.x / 16), (int) Math.floor(playerOffset.z / 16))) {
									seat.absMoveTo(playerOffset.x, playerOffset.y, playerOffset.z);
								}
							};

							final int currentRidingCar = (int) Math.floor(seat.percentageZ);
							final float doorValue = Math.abs(doorValueRaw);
							calculateCar(world, positions, currentRidingCar, doorValue, 0, (x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender) -> {
								final boolean hasGangwayConnection = baseTrainType.hasGangwayConnection;
								final Vec3 movement = new Vec3(ridingPlayer.xxa * ticksElapsed / 4, 0, ridingPlayer.zza * ticksElapsed / 4).yRot((float) -Math.toRadians(Utilities.getYaw(ridingPlayer)) - yaw);
								seat.percentageX += movement.x / baseTrainType.width;
								seat.percentageZ += realSpacingRender == 0 ? 0 : movement.z / realSpacingRender;
								seat.percentageX = Mth.clamp(seat.percentageX, doorLeftOpenRender ? -1 : 0, doorRightOpenRender ? 2 : 1);
								seat.percentageZ = Mth.clamp(seat.percentageZ, (hasGangwayConnection ? 0 : currentRidingCar + 0.05F) + 0.01F, (hasGangwayConnection ? trainCars : currentRidingCar + 0.95F) - 0.01F);
								seat.updateDataToClient(railProgress);
								final int newRidingCar = (int) Math.floor(seat.percentageZ);
								if (currentRidingCar == newRidingCar) {
									moveClient.calculateCarCallback(x, y, z, yaw, pitch, realSpacingRender, doorLeftOpenRender, doorRightOpenRender);
								} else {
									calculateCar(world, positions, newRidingCar, doorValue, 0, moveClient);
								}
							});
						} else {
							ridersToRemove.add(uuid);
						}
					}
				}
			});

			checkBlock(frontPos, checkPos -> {
				if (world.getBlockState(checkPos).getBlock() instanceof BlockTrainAnnouncer) {
					final BlockEntity entity = world.getBlockEntity(checkPos);
					if (entity instanceof BlockTrainAnnouncer.TileEntityTrainAnnouncer && ((BlockTrainAnnouncer.TileEntityTrainAnnouncer) entity).matchesFilter(routeId, speed)) {
						ridingEntities.forEach(uuid -> ((BlockTrainAnnouncer.TileEntityTrainAnnouncer) entity).announce(world.getPlayerByUUID(uuid)));
					}
				}
			});
		}
		if (!ridersToRemove.isEmpty()) {
			ridersToRemove.forEach(ridingEntities::remove);
		}

		return playerNearby[0];
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
	protected boolean skipScanBlocks(Level world, double trainX, double trainY, double trainZ) {
		return world.getNearestPlayer(trainX, trainY, trainZ, MAX_CHECK_DISTANCE, entity -> true) == null;
	}

	@Override
	protected boolean openDoors(Level world, Block block, BlockPos checkPos, float doorValue, int dwellTicks) {
		if (block instanceof BlockPSDAPGDoorBase) {
			for (int i = -1; i <= 1; i++) {
				final BlockPos doorPos = checkPos.above(i);
				final BlockState state = world.getBlockState(doorPos);
				final Block doorBlock = state.getBlock();

				if (doorBlock instanceof BlockPSDAPGDoorBase) {
					final int doorStateValue = (int) Mth.clamp(doorValue * DOOR_MOVE_TIME, 0, BlockPSDAPGDoorBase.MAX_OPEN_VALUE);
					world.setBlockAndUpdate(doorPos, state.setValue(BlockPSDAPGDoorBase.OPEN, doorStateValue));

					if (doorStateValue > 0 && !world.getBlockTicks().hasScheduledTick(doorPos, doorBlock)) {
						Utilities.scheduleBlockTick(world, doorPos, doorBlock, dwellTicks);
					}
				}
			}
		}

		return false;
	}

	@Override
	protected double asin(double value) {
		return TrigCache.asin(value);
	}

	public boolean simulateTrain(Level world, float ticksElapsed, Depot depot, DataCache dataCache, List<Map<UUID, Long>> trainPositions, Map<Player, Set<TrainServer>> trainsInPlayerRange, Map<Long, List<ScheduleEntry>> schedulesForPlatform, boolean isUnlimited) {
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
					final String routeNumber;
					final Station lastStation = dataCache.stationIdMap.get(timeSegment.lastStationId);
					if (lastStation != null) {
						final Route thisRoute = dataCache.routeIdMap.get(timeSegment.routeId);
						routeNumber = thisRoute != null && thisRoute.isLightRailRoute ? thisRoute.lightRailRouteNumber : "";
						destinationString = lastStation.name;
					} else {
						routeNumber = "";
						destinationString = "";
					}

					final long platformId = timeSegment.savedRailBaseId;
					if (!schedulesForPlatform.containsKey(platformId)) {
						schedulesForPlatform.put(platformId, new ArrayList<>());
					}

					final long arrivalMillis = currentMillis + (long) ((timeSegment.endTime + offsetTime - currentTime) * Depot.MILLIS_PER_TICK);
					schedulesForPlatform.get(platformId).add(new ScheduleEntry(arrivalMillis, trainCars, platformId, timeSegment.routeId, routeNumber, destinationString, timeSegment.isTerminating));
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

	public void writeTrainPositions(List<Map<UUID, Long>> trainPositions, SignalBlocks signalBlocks) {
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
					callback.accept(pos.offset(x, -y, z));
				}
			}
		}
	}

	private static void transferItems(Container inventoryFrom, Container inventoryTo) {
		for (int i = 0; i < inventoryFrom.getContainerSize(); i++) {
			if (!inventoryFrom.getItem(i).isEmpty()) {
				final ItemStack insertItem = new ItemStack(inventoryFrom.getItem(i).getItem(), 1);
				final ItemStack remainingStack = HopperBlockEntity.addItem(null, inventoryTo, insertItem, null);
				if (remainingStack.isEmpty()) {
					inventoryFrom.removeItem(i, 1);
					return;
				}
			}
		}
	}
}
