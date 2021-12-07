package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPlatform;
import mtr.packet.IPacket;
import mtr.path.PathData;
import net.minecraft.block.Block;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.*;

public abstract class Train extends NameColorDataBase implements IPacket, IGui {

	protected float speed;
	protected float railProgress;
	protected float stopCounter;
	protected int nextStoppingIndex;
	protected boolean reversed;
	protected boolean isOnRoute = false;

	public final long sidingId;
	protected final String trainId;
	protected final TrainType baseTrainType;
	protected final int trainCars;
	protected final List<PathData> path;
	protected final List<Float> distances;
	protected final Set<UUID> ridingEntities = new HashSet<>();
	private final float railLength;
	protected final DefaultedList<ItemStack> cargo;

	public static final float ACCELERATION = 0.01F;
	protected static final int MAX_CHECK_DISTANCE = 32;
	protected static final int DOOR_MOVE_TIME = 64;
	private static final int DOOR_DELAY = 20;

	private static final String KEY_SPEED = "speed";
	private static final String KEY_RAIL_PROGRESS = "rail_progress";
	private static final String KEY_STOP_COUNTER = "stop_counter";
	private static final String KEY_NEXT_STOPPING_INDEX = "next_stopping_index";
	private static final String KEY_REVERSED = "reversed";
	private static final String KEY_IS_ON_ROUTE = "is_on_route";
	private static final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";
	private static final String KEY_RIDING_ENTITIES = "riding_entities";
	private static final String KEY_CARGOS = "cargos";

	public Train(long id, long sidingId, float railLength, String trainId, TrainType baseTrainType, int trainCars, List<PathData> path, List<Float> distances) {
		super(id);
		this.sidingId = sidingId;
		this.railLength = railLength;
		this.trainId = trainId;
		this.baseTrainType = baseTrainType;
		this.trainCars = trainCars;
		this.path = path;
		this.distances = distances;
		this.cargo = DefaultedList.ofSize(baseTrainType.cargo_slot, ItemStack.EMPTY);
	}

	public Train(long sidingId, float railLength, List<PathData> path, List<Float> distances, NbtCompound nbtCompound) {
		super(nbtCompound);

		this.sidingId = sidingId;
		this.railLength = railLength;
		this.path = path;
		this.distances = distances;

		speed = nbtCompound.getFloat(KEY_SPEED);
		railProgress = nbtCompound.getFloat(KEY_RAIL_PROGRESS);
		stopCounter = nbtCompound.getFloat(KEY_STOP_COUNTER);
		nextStoppingIndex = nbtCompound.getInt(KEY_NEXT_STOPPING_INDEX);
		reversed = nbtCompound.getBoolean(KEY_REVERSED);

		trainId = nbtCompound.getString(KEY_TRAIN_CUSTOM_ID);
		baseTrainType = TrainType.getOrDefault(nbtCompound.getString(KEY_TRAIN_TYPE));
		trainCars = (int) Math.floor(railLength / baseTrainType.getSpacing());

		isOnRoute = nbtCompound.getBoolean(KEY_IS_ON_ROUTE);
		final NbtCompound tagRidingEntities = nbtCompound.getCompound(KEY_RIDING_ENTITIES);
		tagRidingEntities.getKeys().forEach(key -> ridingEntities.add(tagRidingEntities.getUuid(key)));

		cargo = DefaultedList.ofSize(baseTrainType.cargo_slot, ItemStack.EMPTY);
		if (nbtCompound.contains(KEY_CARGOS, 10)) {
			Inventories.readNbt(nbtCompound.getCompound(KEY_CARGOS), this.cargo);
		}
	}

	public Train(PacketByteBuf packet) {
		super(packet);

		path = new ArrayList<>();
		distances = new ArrayList<>();
		final int pathSize = packet.readInt();
		for (int i = 0; i < pathSize; i++) {
			path.add(new PathData(packet));
			distances.add(packet.readFloat());
		}

		sidingId = packet.readLong();
		railLength = packet.readFloat();
		speed = packet.readFloat();
		railProgress = packet.readFloat();
		stopCounter = packet.readFloat();
		nextStoppingIndex = packet.readInt();
		reversed = packet.readBoolean();
		trainId = packet.readString(PACKET_STRING_READ_LENGTH);
		baseTrainType = TrainType.values()[packet.readInt()];
		trainCars = (int) Math.floor(railLength / baseTrainType.getSpacing());
		isOnRoute = packet.readBoolean();

		final int ridingEntitiesCount = packet.readInt();
		for (int i = 0; i < ridingEntitiesCount; i++) {
			ridingEntities.add(packet.readUuid());
		}

		cargo = DefaultedList.ofSize(baseTrainType.cargo_slot, ItemStack.EMPTY);
		Inventories.readNbt(packet.readNbt().getCompound(KEY_CARGOS), this.cargo);
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();

		nbtCompound.putFloat(KEY_SPEED, speed);
		nbtCompound.putFloat(KEY_RAIL_PROGRESS, railProgress);
		nbtCompound.putFloat(KEY_STOP_COUNTER, stopCounter);
		nbtCompound.putInt(KEY_NEXT_STOPPING_INDEX, nextStoppingIndex);
		nbtCompound.putBoolean(KEY_REVERSED, reversed);
		nbtCompound.putString(KEY_TRAIN_CUSTOM_ID, trainId);
		nbtCompound.putString(KEY_TRAIN_TYPE, baseTrainType.toString());
		nbtCompound.putBoolean(KEY_IS_ON_ROUTE, isOnRoute);

		final NbtCompound tagRidingEntities = new NbtCompound();
		ridingEntities.forEach(uuid -> tagRidingEntities.putUuid(KEY_RIDING_ENTITIES + uuid, uuid));
		nbtCompound.put(KEY_RIDING_ENTITIES, tagRidingEntities);

		nbtCompound.put(KEY_CARGOS, Inventories.writeNbt(new NbtCompound(), this.cargo));

		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);

		final int pathSize = Math.min(path.size(), distances.size());
		packet.writeInt(pathSize);
		for (int i = 0; i < pathSize; i++) {
			path.get(i).writePacket(packet);
			packet.writeFloat(distances.get(i));
		}

		packet.writeLong(sidingId);
		packet.writeFloat(railLength);
		packet.writeFloat(speed);
		packet.writeFloat(railProgress);
		packet.writeFloat(stopCounter);
		packet.writeInt(nextStoppingIndex);
		packet.writeBoolean(reversed);
		packet.writeString(trainId);
		packet.writeInt(baseTrainType.ordinal());
		packet.writeBoolean(isOnRoute);
		packet.writeInt(ridingEntities.size());
		ridingEntities.forEach(packet::writeUuid);

		packet.writeNbt(Inventories.writeNbt(new NbtCompound(), this.cargo));
	}

	public final boolean getIsOnRoute() {
		return isOnRoute;
	}

	public final float getRailProgress() {
		return railProgress;
	}

	public final boolean closeToDepot(int trainDistance) {
		return !isOnRoute || railProgress < trainDistance + railLength;
	}

	protected final void simulateTrain(World world, float ticksElapsed, Depot depot) {
		if (world == null) {
			return;
		}

		try {
			final int trainSpacing = baseTrainType.getSpacing();
			final float oldRailProgress = railProgress;
			final float oldSpeed = speed;
			final float oldDoorValue;
			final float doorValueRaw;
			if (nextStoppingIndex >= path.size()) {
				return;
			}
			final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;

			if (!isOnRoute) {
				railProgress = (railLength + trainCars * trainSpacing) / 2;
				oldDoorValue = 0;
				doorValueRaw = 0;
				speed = 0;

				if (canDeploy(depot)) {
					startUp(world, trainCars, trainSpacing, isOppositeRail());
				}
			} else {
				oldDoorValue = Math.abs(getDoorValue());
				final float newAcceleration = ACCELERATION * ticksElapsed;

				if (railProgress >= distances.get(distances.size() - 1) - (railLength - trainCars * trainSpacing) / 2) {
					isOnRoute = false;
					ridingEntities.clear();
					doorValueRaw = 0;
				} else {
					if (speed <= 0) {
						speed = 0;

						if (dwellTicks == 0) {
							doorValueRaw = 0;
						} else {
							stopCounter += ticksElapsed;
							doorValueRaw = getDoorValue();
						}

						if (stopCounter >= dwellTicks) {
							final boolean isOppositeRail = isOppositeRail();
							if (!isRailBlocked(getIndex(0, trainSpacing, true) + (isOppositeRail ? 2 : 1))) {
								startUp(world, trainCars, trainSpacing, isOppositeRail);
							}
						}
					} else {
						if (!world.isClient()) {
							final int checkIndex = getIndex(0, trainSpacing, true) + 1;
							if (isRailBlocked(checkIndex)) {
								nextStoppingIndex = checkIndex - 1;
							}
						}

						final float stoppingDistance = distances.get(nextStoppingIndex) - railProgress;
						if (stoppingDistance < 0.5F * speed * speed / ACCELERATION) {
							speed = Math.max(speed - (0.5F * speed * speed / stoppingDistance) * ticksElapsed, ACCELERATION);
						} else {
							final float railSpeed = getRailSpeed(getIndex(0, trainSpacing, false));
							if (speed < railSpeed) {
								speed = Math.min(speed + newAcceleration, railSpeed);
							} else if (speed > railSpeed) {
								speed = Math.max(speed - newAcceleration, railSpeed);
							}
						}

						doorValueRaw = 0;
					}

					railProgress += speed * ticksElapsed;
					if (railProgress > distances.get(nextStoppingIndex)) {
						railProgress = distances.get(nextStoppingIndex);
						speed = 0;
					}
				}
			}

			if (!path.isEmpty()) {
				final Vec3d[] positions = new Vec3d[trainCars + 1];
				for (int i = 0; i <= trainCars; i++) {
					positions[i] = getRoutePosition(reversed ? trainCars - i : i, trainSpacing);
				}

				handlePositions(world, positions, ticksElapsed, doorValueRaw, oldDoorValue, oldRailProgress);

				final double[] prevX = {0};
				final double[] prevY = {0};
				final double[] prevZ = {0};
				final float[] prevYaw = {0};
				final float[] prevPitch = {0};

				for (int i = 0; i < trainCars; i++) {
					final int ridingCar = i;
					calculateCar(world, positions, i, Math.abs(doorValueRaw), dwellTicks, (x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen) -> {
						simulateCar(
								world, ridingCar, ticksElapsed,
								x, y, z,
								yaw, pitch,
								prevX[0], prevY[0], prevZ[0],
								prevYaw[0], prevPitch[0],
								doorLeftOpen, doorRightOpen, realSpacing,
								doorValueRaw, oldSpeed, oldDoorValue, oldRailProgress
						);
						prevX[0] = x;
						prevY[0] = y;
						prevZ[0] = z;
						prevYaw[0] = yaw;
						prevPitch[0] = pitch;
					});
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected final void calculateCar(World world, Vec3d[] positions, int index, float doorValue, int dwellTicks, CalculateCarCallback calculateCarCallback) {
		final Vec3d pos1 = positions[index];
		final Vec3d pos2 = positions[index + 1];

		if (pos1 != null && pos2 != null) {
			final double x = getAverage(pos1.x, pos2.x);
			final double y = getAverage(pos1.y, pos2.y) + 1;
			final double z = getAverage(pos1.z, pos2.z);

			final double realSpacing = pos2.distanceTo(pos1);
			final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
			final float pitch = realSpacing == 0 ? 0 : (float) Math.asin((pos2.y - pos1.y) / realSpacing);
			final boolean doorLeftOpen = scanDoors(world, x, y, z, (float) Math.PI + yaw, pitch, realSpacing / 2, doorValue, dwellTicks) && doorValue > 0;
			final boolean doorRightOpen = scanDoors(world, x, y, z, yaw, pitch, realSpacing / 2, doorValue, dwellTicks) && doorValue > 0;

			calculateCarCallback.calculateCarCallback(x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen);
		}
	}

	protected final int getIndex(int car, int trainSpacing, boolean roundDown) {
		return getIndex(getRailProgress(car, trainSpacing), roundDown);
	}

	protected final int getIndex(float tempRailProgress, boolean roundDown) {
		for (int i = 0; i < path.size(); i++) {
			final float tempDistance = distances.get(i);
			if (tempRailProgress < tempDistance || roundDown && tempRailProgress == tempDistance) {
				return i;
			}
		}
		return path.size() - 1;
	}

	protected final float getRailSpeed(int railIndex) {
		final RailType thisRail = path.get(railIndex).rail.railType;
		final float railSpeed;
		if (thisRail.canAccelerate) {
			railSpeed = thisRail.maxBlocksPerTick;
		} else {
			final RailType lastRail = railIndex > 0 ? path.get(railIndex - 1).rail.railType : thisRail;
			railSpeed = Math.max(lastRail.canAccelerate ? lastRail.maxBlocksPerTick : RailType.WOODEN.maxBlocksPerTick, speed);
		}
		return railSpeed;
	}

	protected void startUp(World world, int trainCars, int trainSpacing, boolean isOppositeRail) {
	}

	protected abstract void simulateCar(
			World world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double prevCarX, double prevCarY, double prevCarZ, float prevCarYaw, float prevCarPitch,
			boolean doorLeftOpen, boolean doorRightOpen, double realSpacing,
			float doorValueRaw, float oldSpeed, float oldDoorValue, float oldRailProgress
	);

	protected abstract void handlePositions(World world, Vec3d[] positions, float ticksElapsed, float doorValueRaw, float oldDoorValue, float oldRailProgress);

	protected abstract boolean canDeploy(Depot depot);

	protected abstract boolean isRailBlocked(int checkIndex);

	protected abstract boolean skipScanBlocks(World world, double trainX, double trainY, double trainZ);

	protected abstract boolean openDoors(World world, Block block, BlockPos checkPos, float doorValue, int dwellTicks);

	private boolean isOppositeRail() {
		return path.size() > nextStoppingIndex + 1 && path.get(nextStoppingIndex).isOppositeRail(path.get(nextStoppingIndex + 1));
	}

	private float getRailProgress(int car, int trainSpacing) {
		return railProgress - car * trainSpacing;
	}

	private Vec3d getRoutePosition(int car, int trainSpacing) {
		final float tempRailProgress = Math.max(getRailProgress(car, trainSpacing) - baseTrainType.offset, 0);
		final int index = getIndex(tempRailProgress, false);
		return path.get(index).rail.getPosition(tempRailProgress - (index == 0 ? 0 : distances.get(index - 1)));
	}

	private float getDoorValue() {
		final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;
		final float maxDoorMoveTime = Math.min(DOOR_MOVE_TIME, dwellTicks / 2 - DOOR_DELAY);
		final float stage1 = DOOR_DELAY;
		final float stage2 = DOOR_DELAY + maxDoorMoveTime;
		final float stage3 = dwellTicks - DOOR_DELAY - maxDoorMoveTime;
		final float stage4 = dwellTicks - DOOR_DELAY;
		if (stopCounter < stage1 || stopCounter >= stage4) {
			return 0;
		} else if (stopCounter >= stage2 && stopCounter < stage3) {
			return 1;
		} else if (stopCounter < stage2) {
			return (stopCounter - stage1) / DOOR_MOVE_TIME;
		} else if (stopCounter >= stage3) {
			return -(stage4 - stopCounter) / DOOR_MOVE_TIME;
		} else {
			return 0;
		}
	}

	private boolean scanDoors(World world, double trainX, double trainY, double trainZ, float checkYaw, float pitch, double halfSpacing, float doorValue, int dwellTicks) {
		if (skipScanBlocks(world, trainX, trainY, trainZ)) {
			return false;
		}

		boolean hasPlatform = false;
		final Vec3d offsetVec = new Vec3d(1, 0, 0).rotateY(checkYaw).rotateX(pitch);
		final Vec3d traverseVec = new Vec3d(0, 0, 1).rotateY(checkYaw).rotateX(pitch);

		for (int checkX = 1; checkX <= 3; checkX++) {
			for (int checkY = -1; checkY <= 0; checkY++) {
				for (double checkZ = -halfSpacing; checkZ <= halfSpacing; checkZ++) {
					final BlockPos checkPos = new BlockPos(trainX + offsetVec.x * checkX + traverseVec.x * checkZ, trainY + checkY, trainZ + offsetVec.z * checkX + traverseVec.z * checkZ);
					final Block block = world.getBlockState(checkPos).getBlock();

					if (block instanceof BlockPlatform || block instanceof BlockPSDAPGBase) {
						if (openDoors(world, block, checkPos, doorValue, dwellTicks)) {
							return true;
						}
						hasPlatform = true;
					}
				}
			}
		}

		return hasPlatform;
	}

	public void insert(Inventory from) {
		for ( int i = 0; i < cargo.size(); i++ ) {
			ItemStack slot = cargo.get(i);
			for ( int j = 0; j < from.size(); j++ ) {
				ItemStack putItem = from.getStack(j);
				if ( !putItem.isEmpty() && ( ItemStack.areItemsEqual(slot, putItem) || slot.isEmpty() ) ) {
					final int numItem = putItem.getCount();
					final int numAvail = baseTrainType.cargo_num - slot.getCount();
					final int takeNum = Math.min(numAvail, numItem);
					if (slot.isEmpty()) {
						cargo.set(i, new ItemStack(putItem.getItem(), takeNum));
					} else {
						slot.increment(takeNum);
					}
					putItem.decrement(takeNum);
				}
			}
		}
	}

	public void extract(Inventory to) {
		for ( int i = 0; i < cargo.size(); i++ ) {
			ItemStack putItem = cargo.get(i);
			for ( int j = 0; j < to.size(); j++ ) {
				ItemStack slot = to.getStack(j);
				if ( !putItem.isEmpty() && ( ItemStack.areItemsEqual(slot, putItem) || slot.isEmpty() ) ) {
					final int numItem = putItem.getCount();
					final int numAvail = slot.getMaxCount() - slot.getCount();
					final int takeNum = Math.min(numAvail, numItem);
					if (slot.isEmpty()) {
						to.setStack(i, new ItemStack(putItem.getItem(), takeNum));
					} else {
						slot.increment(takeNum);
					}
					putItem.decrement(takeNum);
				}
			}
		}
	}

	public static double getAverage(double a, double b) {
		return (a + b) / 2;
	}

	public static double getValueFromPercentage(double percentage, double total) {
		return (percentage - 0.5) * total;
	}

	@FunctionalInterface
	protected interface CalculateCarCallback {
		void calculateCarCallback(double x, double y, double z, float yaw, float pitch, double realSpacing, boolean doorLeftOpen, boolean doorRightOpen);
	}
}