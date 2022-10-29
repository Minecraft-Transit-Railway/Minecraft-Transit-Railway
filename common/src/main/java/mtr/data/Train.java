package mtr.data;

import mtr.Items;
import mtr.Keys;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPlatform;
import mtr.packet.IPacket;
import mtr.path.PathData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.*;
import java.util.*;

public abstract class Train extends NameColorDataBase implements IPacket {

	protected float speed;
	protected double railProgress;
	protected boolean doorOpen;
	protected float doorValue;
	protected float stopCounter;
	protected int nextStoppingIndex;
	protected boolean reversed;
	protected boolean isOnRoute = false;
	protected boolean isCurrentlyManual;
	protected int manualAccelerationSign;

	public final long sidingId;
	public final String trainId;
	public final String baseTrainType;
	public final TransportMode transportMode;
	public final int spacing;
	public final int width;
	public final int trainCars;
	public final float accelerationConstant;
	protected final boolean isManual;
	protected final int maxManualSpeed;
	protected final int manualToAutomaticTime;
	protected final List<PathData> path;
	protected final List<Double> distances;
	protected final int repeatIndex1;
	protected final int repeatIndex2;
	protected final Set<UUID> ridingEntities = new HashSet<>();
	protected final SimpleContainer inventory;
	private final float railLength;

	public static final float ACCELERATION_DEFAULT = 0.01F; // m/tick^2
	public static final float MAX_ACCELERATION = 0.05F; // m/tick^2
	public static final float MIN_ACCELERATION = 0.001F; // m/tick^2
	public static final int DOOR_MOVE_TIME = 64;
	protected static final int MAX_CHECK_DISTANCE = 32;
	private static final int DOOR_DELAY = 20;

	private static final String KEY_SPEED = "speed";
	private static final String KEY_RAIL_PROGRESS = "rail_progress";
	private static final String KEY_STOP_COUNTER = "stop_counter";
	private static final String KEY_NEXT_STOPPING_INDEX = "next_stopping_index";
	private static final String KEY_REVERSED = "reversed";
	private static final String KEY_IS_CURRENTLY_MANUAL = "is_currently_manual";
	private static final String KEY_IS_ON_ROUTE = "is_on_route";
	private static final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";
	private static final String KEY_RIDING_ENTITIES = "riding_entities";
	private static final String KEY_CARGO = "cargo";

	public Train(long id, long sidingId, float railLength, String trainId, String baseTrainType, int trainCars, List<PathData> path, List<Double> distances, int repeatIndex1, int repeatIndex2, float accelerationConstant, boolean isManual, int maxManualSpeed, int manualToAutomaticTime) {
		super(id);
		this.sidingId = sidingId;
		this.railLength = railLength;
		this.trainId = trainId;
		// TODO temporary code for backwards compatibility
		baseTrainType = baseTrainType.startsWith("base_") ? baseTrainType.replace("base_", "train_") : baseTrainType;
		// TODO temporary code end
		this.baseTrainType = baseTrainType;
		transportMode = TrainType.getTransportMode(baseTrainType);
		spacing = TrainType.getSpacing(baseTrainType);
		width = TrainType.getWidth(baseTrainType);
		this.trainCars = trainCars;
		this.isManual = isManual;
		isCurrentlyManual = isManual;
		this.maxManualSpeed = maxManualSpeed;
		this.manualToAutomaticTime = manualToAutomaticTime;
		this.path = path;
		this.distances = distances;
		this.repeatIndex1 = repeatIndex1;
		this.repeatIndex2 = repeatIndex2;
		final float tempAccelerationConstant = RailwayData.round(accelerationConstant, 3);
		this.accelerationConstant = tempAccelerationConstant <= 0 ? ACCELERATION_DEFAULT : tempAccelerationConstant;
		inventory = new SimpleContainer(trainCars);
	}

	public Train(
			long sidingId, float railLength,
			List<PathData> path, List<Double> distances, int repeatIndex1, int repeatIndex2,
			float accelerationConstant, boolean isManual, int maxManualSpeed, int manualToAutomaticTime,
			Map<String, Value> map
	) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);

		this.sidingId = sidingId;
		this.railLength = railLength;
		this.path = path;
		this.distances = distances;
		this.repeatIndex1 = repeatIndex1;
		this.repeatIndex2 = repeatIndex2;
		this.accelerationConstant = accelerationConstant;
		this.isManual = isManual;
		this.maxManualSpeed = maxManualSpeed;
		this.manualToAutomaticTime = manualToAutomaticTime;

		speed = messagePackHelper.getFloat(KEY_SPEED);
		railProgress = messagePackHelper.getDouble(KEY_RAIL_PROGRESS);
		stopCounter = messagePackHelper.getFloat(KEY_STOP_COUNTER);
		nextStoppingIndex = messagePackHelper.getInt(KEY_NEXT_STOPPING_INDEX);
		reversed = messagePackHelper.getBoolean(KEY_REVERSED);

		final String tempTrainId = messagePackHelper.getString(KEY_TRAIN_CUSTOM_ID).toLowerCase(Locale.ENGLISH);
		// TODO temporary code for backwards compatibility
		String tempBaseTrainType = messagePackHelper.getString(KEY_TRAIN_TYPE).toLowerCase(Locale.ENGLISH);
		baseTrainType = tempBaseTrainType.startsWith("base_") ? tempBaseTrainType.replace("base_", "train_") : tempBaseTrainType;
		// TODO temporary code end
		trainId = tempTrainId.isEmpty() ? baseTrainType : tempTrainId;
		transportMode = TrainType.getTransportMode(baseTrainType);
		spacing = TrainType.getSpacing(baseTrainType);
		width = TrainType.getWidth(baseTrainType);
		trainCars = Math.min(transportMode.maxLength, (int) Math.floor(railLength / spacing + 0.01F));
		isCurrentlyManual = messagePackHelper.getBoolean(KEY_IS_CURRENTLY_MANUAL);

		isOnRoute = messagePackHelper.getBoolean(KEY_IS_ON_ROUTE);
		messagePackHelper.iterateArrayValue(KEY_RIDING_ENTITIES, value -> ridingEntities.add(UUID.fromString(value.asStringValue().asString())));

		SimpleContainer inventory1 = new SimpleContainer(trainCars);
		if (map.containsKey(KEY_CARGO) && !map.get(KEY_CARGO).isNilValue()) {
			final byte[] rawNbt = map.get(KEY_CARGO).asBinaryValue().asByteArray();
			final ByteArrayInputStream inputStream = new ByteArrayInputStream(rawNbt);
			try {
				final CompoundTag compoundTag = NbtIo.read(new DataInputStream(inputStream));
				final NonNullList<ItemStack> stacks = NonNullList.withSize(trainCars, ItemStack.EMPTY);
				ContainerHelper.loadAllItems(compoundTag.getCompound(KEY_CARGO), stacks);
				inventory1 = new SimpleContainer(stacks.toArray(new ItemStack[0]));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		inventory = inventory1;
	}

	@Deprecated
	public Train(
			long sidingId, float railLength,
			List<PathData> path, List<Double> distances, int repeatIndex1, int repeatIndex2,
			float accelerationConstant, boolean isManual, int maxManualSpeed, int manualToAutomaticTime,
			CompoundTag compoundTag
	) {
		super(compoundTag);

		this.sidingId = sidingId;
		this.railLength = railLength;
		this.path = path;
		this.distances = distances;
		this.repeatIndex1 = repeatIndex1;
		this.repeatIndex2 = repeatIndex2;
		this.accelerationConstant = accelerationConstant;
		this.isManual = isManual;
		this.maxManualSpeed = maxManualSpeed;
		this.manualToAutomaticTime = manualToAutomaticTime;

		speed = compoundTag.getFloat(KEY_SPEED);
		railProgress = compoundTag.getDouble(KEY_RAIL_PROGRESS);
		stopCounter = compoundTag.getFloat(KEY_STOP_COUNTER);
		nextStoppingIndex = compoundTag.getInt(KEY_NEXT_STOPPING_INDEX);
		reversed = compoundTag.getBoolean(KEY_REVERSED);

		trainId = compoundTag.getString(KEY_TRAIN_CUSTOM_ID);
		baseTrainType = compoundTag.getString(KEY_TRAIN_TYPE);
		transportMode = TrainType.getTransportMode(baseTrainType);
		spacing = TrainType.getSpacing(baseTrainType);
		width = TrainType.getWidth(baseTrainType);
		trainCars = Math.min(transportMode.maxLength, (int) Math.floor(railLength / spacing + 0.01F));
		isCurrentlyManual = compoundTag.getBoolean(KEY_IS_CURRENTLY_MANUAL);

		isOnRoute = compoundTag.getBoolean(KEY_IS_ON_ROUTE);
		final CompoundTag tagRidingEntities = compoundTag.getCompound(KEY_RIDING_ENTITIES);
		tagRidingEntities.getAllKeys().forEach(key -> ridingEntities.add(tagRidingEntities.getUUID(key)));

		final NonNullList<ItemStack> stacks = NonNullList.withSize(trainCars, ItemStack.EMPTY);
		ContainerHelper.loadAllItems(compoundTag.getCompound(KEY_CARGO), stacks);
		inventory = new SimpleContainer(stacks.toArray(new ItemStack[0]));
	}

	public Train(FriendlyByteBuf packet) {
		super(packet);

		path = new ArrayList<>();
		distances = new ArrayList<>();
		final int pathSize = packet.readInt();
		for (int i = 0; i < pathSize; i++) {
			path.add(new PathData(packet));
			distances.add(packet.readDouble());
		}
		repeatIndex1 = packet.readInt();
		repeatIndex2 = packet.readInt();

		sidingId = packet.readLong();
		railLength = packet.readFloat();
		speed = packet.readFloat();
		final float tempAccelerationConstant = RailwayData.round(packet.readFloat(), 3);
		accelerationConstant = tempAccelerationConstant <= 0 ? ACCELERATION_DEFAULT : tempAccelerationConstant;
		railProgress = packet.readDouble();
		stopCounter = packet.readFloat();
		nextStoppingIndex = packet.readInt();
		reversed = packet.readBoolean();
		trainId = packet.readUtf(PACKET_STRING_READ_LENGTH);
		baseTrainType = packet.readUtf(PACKET_STRING_READ_LENGTH);
		transportMode = TrainType.getTransportMode(baseTrainType);
		spacing = TrainType.getSpacing(baseTrainType);
		width = TrainType.getWidth(baseTrainType);
		trainCars = Math.min(transportMode.maxLength, (int) Math.floor(railLength / spacing + 0.01F));
		isManual = packet.readBoolean();
		isCurrentlyManual = packet.readBoolean();
		maxManualSpeed = packet.readInt();
		manualToAutomaticTime = packet.readInt();
		isOnRoute = packet.readBoolean();
		manualAccelerationSign = packet.readInt();
		doorOpen = packet.readBoolean();

		final int ridingEntitiesCount = packet.readInt();
		for (int i = 0; i < ridingEntitiesCount; i++) {
			ridingEntities.add(packet.readUUID());
		}

		inventory = null;
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		super.toMessagePack(messagePacker);

		messagePacker.packString(KEY_SPEED).packFloat(speed);
		messagePacker.packString(KEY_RAIL_PROGRESS).packDouble(railProgress);
		messagePacker.packString(KEY_STOP_COUNTER).packFloat(stopCounter);
		messagePacker.packString(KEY_NEXT_STOPPING_INDEX).packLong(nextStoppingIndex);
		messagePacker.packString(KEY_REVERSED).packBoolean(reversed);
		messagePacker.packString(KEY_TRAIN_CUSTOM_ID).packString(trainId);
		messagePacker.packString(KEY_TRAIN_TYPE).packString(baseTrainType);
		messagePacker.packString(KEY_IS_CURRENTLY_MANUAL).packBoolean(isCurrentlyManual);
		messagePacker.packString(KEY_IS_ON_ROUTE).packBoolean(isOnRoute);

		messagePacker.packString(KEY_RIDING_ENTITIES).packArrayHeader(ridingEntities.size());
		for (final UUID uuid : ridingEntities) {
			messagePacker.packString(uuid.toString());
		}

		messagePacker.packString(KEY_CARGO);
		if (inventory != null) {
			final NonNullList<ItemStack> stacks = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
			int totalCount = 0;
			for (int i = 0; i < inventory.getContainerSize(); i++) {
				stacks.set(i, inventory.getItem(i));
				totalCount += inventory.getItem(i).getCount();
			}
			if (totalCount > 0) {
				CompoundTag tag = ContainerHelper.saveAllItems(new CompoundTag(), stacks);
				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				NbtIo.write(tag, new DataOutputStream(outputStream));
				messagePacker.packBinaryHeader(outputStream.size());
				messagePacker.writePayload(outputStream.toByteArray());
			} else {
				messagePacker.packNil();
			}
		} else {
			messagePacker.packNil();
		}
	}

	@Override
	public int messagePackLength() {
		return super.messagePackLength() + 11;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);

		final int pathSize = Math.min(path.size(), distances.size());
		packet.writeInt(pathSize);
		for (int i = 0; i < pathSize; i++) {
			path.get(i).writePacket(packet);
			packet.writeDouble(distances.get(i));
		}
		packet.writeInt(repeatIndex1);
		packet.writeInt(repeatIndex2);

		packet.writeLong(sidingId);
		packet.writeFloat(railLength);
		packet.writeFloat(speed);
		packet.writeFloat(accelerationConstant);
		packet.writeDouble(railProgress);
		packet.writeFloat(stopCounter);
		packet.writeInt(nextStoppingIndex);
		packet.writeBoolean(reversed);
		packet.writeUtf(trainId);
		packet.writeUtf(baseTrainType);
		packet.writeBoolean(isManual);
		packet.writeBoolean(isCurrentlyManual);
		packet.writeInt(maxManualSpeed);
		packet.writeInt(manualToAutomaticTime);
		packet.writeBoolean(isOnRoute);
		packet.writeInt(manualAccelerationSign);
		packet.writeBoolean(doorOpen);
		packet.writeInt(ridingEntities.size());
		ridingEntities.forEach(packet::writeUUID);
	}

	@Override
	protected final boolean hasTransportMode() {
		return false;
	}

	public final boolean getIsOnRoute() {
		return isOnRoute;
	}

	public final double getRailProgress() {
		return railProgress;
	}

	public final boolean closeToDepot(int trainDistance) {
		return !isOnRoute || railProgress < trainDistance + railLength;
	}

	public final boolean isCurrentlyManual() {
		return isCurrentlyManual;
	}

	public boolean changeManualSpeed(boolean isAccelerate) {
		if (doorValue == 0 && isAccelerate && manualAccelerationSign >= -2 && manualAccelerationSign < 2) {
			manualAccelerationSign++;
			return true;
		} else if (!isAccelerate && manualAccelerationSign > -2) {
			manualAccelerationSign--;
			return true;
		} else {
			return false;
		}
	}

	public boolean toggleDoors() {
		if (speed == 0) {
			doorOpen = !doorOpen;
			manualAccelerationSign = -2;
			return true;
		} else {
			doorOpen = false;
			return false;
		}
	}

	protected final void simulateTrain(Level world, float ticksElapsed, Depot depot) {
		if (world == null) {
			return;
		}

		try {
			final float tempDoorValue1;
			if (nextStoppingIndex >= path.size()) {
				return;
			}
			final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;

			if (!isOnRoute) {
				railProgress = (railLength + trainCars * spacing) / 2;
				reversed = false;
				tempDoorValue1 = 0;
				speed = 0;
				nextStoppingIndex = 0;

				if (!isCurrentlyManual && canDeploy(depot) || isCurrentlyManual && manualAccelerationSign > 0) {
					startUp(world, trainCars, spacing, isOppositeRail());
				}
			} else {
				final float newAcceleration = accelerationConstant * ticksElapsed;

				if (railProgress >= distances.get(distances.size() - 1) - (railLength - trainCars * spacing) / 2) {
					isOnRoute = false;
					manualAccelerationSign = -2;
					ridingEntities.clear();
					tempDoorValue1 = 0;
				} else {
					final float tempDoorValue2;

					if (speed <= 0) {
						speed = 0;

						if (dwellTicks == 0) {
							tempDoorValue2 = 0;
						} else {
							if (stopCounter == 0 && isRepeat() && getIndex(railProgress, false) >= repeatIndex2 && distances.size() > repeatIndex1) {
								if (path.get(repeatIndex2).isOppositeRail(path.get(repeatIndex1))) {
									railProgress = distances.get(repeatIndex1 - 1) + trainCars * spacing;
									reversed = !reversed;
								} else {
									railProgress = distances.get(repeatIndex1);
								}
							}

							stopCounter += ticksElapsed;
							if (isCurrentlyManual) {
								doorValue = Mth.clamp(doorValue + ticksElapsed * (doorOpen ? 1 : -1) / DOOR_MOVE_TIME, 0, 1);
							}

							tempDoorValue2 = getDoorValue();
						}

						if (!world.isClientSide() && (isCurrentlyManual || stopCounter >= dwellTicks)) {
							final boolean isOppositeRail = isOppositeRail();
							if (!isRailBlocked(getIndex(0, spacing, true) + (isOppositeRail ? 2 : 1)) && (!isCurrentlyManual || manualAccelerationSign > 0)) {
								startUp(world, trainCars, spacing, isOppositeRail);
							}
						}
					} else {
						if (!world.isClientSide()) {
							final int checkIndex = getIndex(0, spacing, true) + 1;
							if (isRailBlocked(checkIndex)) {
								nextStoppingIndex = checkIndex - 1;
							}
						}

						final double stoppingDistance = distances.get(nextStoppingIndex) - railProgress;
						if (!transportMode.continuousMovement && stoppingDistance < 0.5 * speed * speed / accelerationConstant) {
							speed = stoppingDistance <= 0 ? Train.ACCELERATION_DEFAULT : (float) Math.max(speed - (0.5 * speed * speed / stoppingDistance) * ticksElapsed, Train.ACCELERATION_DEFAULT);
							manualAccelerationSign = -3;
						} else {
							if (isCurrentlyManual) {
								if (manualAccelerationSign >= -2) {
									final RailType railType = convertMaxManualSpeed(maxManualSpeed);
									speed = Mth.clamp(speed + manualAccelerationSign * newAcceleration / 2, 0, railType == null ? RailType.IRON.maxBlocksPerTick : railType.maxBlocksPerTick);
								}
							} else {
								final float railSpeed = getRailSpeed(getIndex(0, spacing, false));
								if (speed < railSpeed) {
									speed = Math.min(speed + newAcceleration, railSpeed);
									manualAccelerationSign = 2;
								} else if (speed > railSpeed) {
									speed = Math.max(speed - newAcceleration, railSpeed);
									manualAccelerationSign = -2;
								} else {
									manualAccelerationSign = 0;
								}
							}
						}

						tempDoorValue2 = 0;
						doorOpen = false;
						doorValue = 0;
					}

					railProgress += speed * ticksElapsed;
					if (!transportMode.continuousMovement && railProgress > distances.get(nextStoppingIndex)) {
						railProgress = distances.get(nextStoppingIndex);
						speed = 0;
						manualAccelerationSign = -2;
					}

					tempDoorValue1 = tempDoorValue2 + (transportMode.continuousMovement ? getDoorValueContinuous() : 0);
				}
			}

			doorValue = tempDoorValue1;

			if (!path.isEmpty()) {
				final Vec3[] positions = new Vec3[trainCars + 1];
				for (int i = 0; i <= trainCars; i++) {
					positions[i] = getRoutePosition(reversed ? trainCars - i : i, spacing);
				}

				if (handlePositions(world, positions, ticksElapsed)) {
					final double[] prevX = {0};
					final double[] prevY = {0};
					final double[] prevZ = {0};
					final float[] prevYaw = {0};
					final float[] prevPitch = {0};

					for (int i = 0; i < trainCars; i++) {
						final int ridingCar = i;
						calculateCar(world, positions, i, dwellTicks, (x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen) -> {
							simulateCar(
									world, ridingCar, ticksElapsed,
									x, y, z,
									yaw, pitch,
									prevX[0], prevY[0], prevZ[0],
									prevYaw[0], prevPitch[0],
									doorLeftOpen, doorRightOpen, realSpacing
							);
							prevX[0] = x;
							prevY[0] = y;
							prevZ[0] = z;
							prevYaw[0] = yaw;
							prevPitch[0] = pitch;
						});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected final void calculateCar(Level world, Vec3[] positions, int index, int dwellTicks, CalculateCarCallback calculateCarCallback) {
		final Vec3 pos1 = positions[index];
		final Vec3 pos2 = positions[index + 1];

		if (pos1 != null && pos2 != null) {
			final double x = getAverage(pos1.x, pos2.x);
			final double y = getAverage(pos1.y, pos2.y) + 1;
			final double z = getAverage(pos1.z, pos2.z);

			final double realSpacing = pos2.distanceTo(pos1);
			final float yaw = (float) Mth.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
			final float pitch = realSpacing == 0 ? 0 : (float) asin((pos2.y - pos1.y) / realSpacing);
			final boolean doorLeftOpen = scanDoors(world, x, y, z, (float) Math.PI + yaw, pitch, realSpacing / 2, dwellTicks) && doorValue > 0;
			final boolean doorRightOpen = scanDoors(world, x, y, z, yaw, pitch, realSpacing / 2, dwellTicks) && doorValue > 0;

			calculateCarCallback.calculateCarCallback(x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen);
		}
	}

	public final int getIndex(int car, int trainSpacing, boolean roundDown) {
		return getIndex(getRailProgress(car, trainSpacing), roundDown);
	}

	public final int getIndex(double tempRailProgress, boolean roundDown) {
		for (int i = 0; i < path.size(); i++) {
			final double tempDistance = distances.get(i);
			if (tempRailProgress < tempDistance || roundDown && tempRailProgress == tempDistance) {
				return i;
			}
		}
		return path.size() - 1;
	}

	public final float getRailSpeed(int railIndex) {
		final RailType thisRail = path.get(railIndex).rail.railType;
		final float railSpeed;
		if (thisRail.canAccelerate) {
			railSpeed = thisRail.maxBlocksPerTick;
		} else {
			final RailType lastRail = railIndex > 0 ? path.get(railIndex - 1).rail.railType : thisRail;
			railSpeed = Math.max(lastRail.canAccelerate ? lastRail.maxBlocksPerTick : RailType.getDefaultMaxBlocksPerTick(transportMode), speed);
		}
		return railSpeed;
	}

	protected void startUp(Level world, int trainCars, int trainSpacing, boolean isOppositeRail) {
		doorOpen = false;
		doorValue = 0;
	}

	protected float getModelZOffset() {
		return 0;
	}

	protected boolean isRepeat() {
		return repeatIndex1 > 0 && repeatIndex2 > 0;
	}

	protected abstract void simulateCar(
			Level world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double prevCarX, double prevCarY, double prevCarZ, float prevCarYaw, float prevCarPitch,
			boolean doorLeftOpen, boolean doorRightOpen, double realSpacing
	);

	protected abstract boolean handlePositions(Level world, Vec3[] positions, float ticksElapsed);

	protected abstract boolean canDeploy(Depot depot);

	protected abstract boolean isRailBlocked(int checkIndex);

	protected abstract boolean skipScanBlocks(Level world, double trainX, double trainY, double trainZ);

	protected abstract boolean openDoors(Level world, Block block, BlockPos checkPos, int dwellTicks);

	protected abstract double asin(double value);

	private boolean isOppositeRail() {
		return path.size() > nextStoppingIndex + 1 && railProgress == distances.get(nextStoppingIndex) && path.get(nextStoppingIndex).isOppositeRail(path.get(nextStoppingIndex + 1));
	}

	private double getRailProgress(int car, int trainSpacing) {
		return railProgress - car * trainSpacing;
	}

	private Vec3 getRoutePosition(int car, int trainSpacing) {
		final double tempRailProgress = Math.max(getRailProgress(car, trainSpacing) - getModelZOffset(), 0);
		final int index = getIndex(tempRailProgress, false);
		return path.get(index).rail.getPosition(tempRailProgress - (index == 0 ? 0 : distances.get(index - 1))).add(0, transportMode.railOffset, 0);
	}

	private float getDoorValue() {
		if (!isCurrentlyManual) {
			final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;
			final float maxDoorMoveTime = Math.min(DOOR_MOVE_TIME, dwellTicks / 2 - DOOR_DELAY);
			final float stage1 = DOOR_DELAY;
			final float stage2 = DOOR_DELAY + maxDoorMoveTime;
			final float stage3 = dwellTicks - DOOR_DELAY - maxDoorMoveTime;
			final float stage4 = dwellTicks - DOOR_DELAY;
			if (stopCounter < stage1 || stopCounter >= stage4) {
				doorOpen = false;
				doorValue = 0;
			} else if (stopCounter >= stage2 && stopCounter < stage3) {
				doorOpen = true;
				doorValue = 1;
			} else if (stopCounter < stage2) {
				doorOpen = true;
				doorValue = (stopCounter - stage1) / DOOR_MOVE_TIME;
			} else if (stopCounter >= stage3) {
				doorOpen = false;
				doorValue = (stage4 - stopCounter) / DOOR_MOVE_TIME;
			} else {
				doorOpen = false;
				doorValue = 0;
			}
		}
		if (doorOpen || doorValue != 0) {
			manualAccelerationSign = -2;
		}
		return doorValue;
	}

	private float getDoorValueContinuous() {
		final int index = getIndex(railProgress, false);
		if (path.get(index).dwellTime > 0 && index > 0) {
			final double distance1 = distances.get(index - 1);
			final double distance2 = distances.get(index);
			return (float) Mth.clamp(Math.min(railProgress - distance1, distance2 - railProgress) * 0.5, 0, 1);
		} else {
			return 0;
		}
	}

	private boolean scanDoors(Level world, double trainX, double trainY, double trainZ, float checkYaw, float pitch, double halfSpacing, int dwellTicks) {
		if (skipScanBlocks(world, trainX, trainY, trainZ)) {
			return false;
		}

		boolean hasPlatform = false;
		final Vec3 offsetVec = new Vec3(1, 0, 0).yRot(checkYaw).xRot(pitch);
		final Vec3 traverseVec = new Vec3(0, 0, 1).yRot(checkYaw).xRot(pitch);

		for (int checkX = 1; checkX <= 3; checkX++) {
			for (int checkY = -2; checkY <= 0; checkY++) {
				for (double checkZ = -halfSpacing; checkZ <= halfSpacing; checkZ++) {
					final BlockPos checkPos = new BlockPos(trainX + offsetVec.x * checkX + traverseVec.x * checkZ, trainY + checkY, trainZ + offsetVec.z * checkX + traverseVec.z * checkZ);
					final Block block = world.getBlockState(checkPos).getBlock();

					if (block instanceof BlockPlatform || block instanceof BlockPSDAPGBase) {
						if (openDoors(world, block, checkPos, dwellTicks)) {
							return true;
						}
						hasPlatform = true;
					}
				}
			}
		}

		return hasPlatform;
	}

	public static boolean isHoldingKey(Player player) {
		return player != null && !Keys.LIFTS_ONLY && player.isHolding(Items.DRIVER_KEY.get());
	}

	public static double getAverage(double a, double b) {
		return (a + b) / 2;
	}

	public static RailType convertMaxManualSpeed(int maxManualSpeed) {
		if (maxManualSpeed >= 0 && maxManualSpeed <= RailType.DIAMOND.ordinal()) {
			return RailType.values()[maxManualSpeed];
		} else {
			return null;
		}
	}

	@FunctionalInterface
	protected interface CalculateCarCallback {
		void calculateCarCallback(double x, double y, double z, float yaw, float pitch, double realSpacing, boolean doorLeftOpen, boolean doorRightOpen);
	}
}
