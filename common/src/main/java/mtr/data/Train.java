package mtr.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mtr.Items;
import mtr.Keys;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPlatform;
import mtr.client.TrainClientRegistry;
import mtr.client.TrainProperties;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dynmap.web.Json;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.*;
import java.util.*;

import static mtr.MTR.MOD_ID;

public abstract class Train extends NameColorDataBase implements IPacket {

	protected float speed;
	protected double railProgress;
	protected boolean doorTarget;
	protected float doorValue;
	protected float elapsedDwellTicks;
	protected int nextStoppingIndex;
	protected int nextPlatformIndex;
	protected boolean reversed;
	protected boolean isOnRoute = false;
	protected boolean isCurrentlyManual;
	protected int manualNotch;

	public final long sidingId;
	public final String trainId;
	public final String baseTrainType;
	public final TransportMode transportMode;
	public final int spacing;
	public final int width;
	public final int trainCars;
	public final float accelerationConstant;
	public final boolean isManualAllowed;
	public final int maxManualSpeed;
	public final int manualToAutomaticTime;
	public final List<PathData> path;

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
	protected static final int DOOR_DELAY = 20;

	private static final String KEY_SPEED = "speed";
	private static final String KEY_RAIL_PROGRESS = "rail_progress";
	private static final String KEY_ELAPSED_DWELL_TICKS = "stop_counter";
	private static final String KEY_NEXT_STOPPING_INDEX = "next_stopping_index";
	private static final String KEY_NEXT_PLATFORM_INDEX = "next_platform_index";
	private static final String KEY_REVERSED = "reversed";
	private static final String KEY_IS_CURRENTLY_MANUAL = "is_currently_manual";
	private static final String KEY_IS_ON_ROUTE = "is_on_route";
	private static final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";
	private static final String KEY_RIDING_ENTITIES = "riding_entities";
	private static final String KEY_CARGO = "cargo";

	public Train(long id, long sidingId, float railLength, String trainId, String baseTrainType, int trainCars, List<PathData> path, List<Double> distances, int repeatIndex1, int repeatIndex2, float accelerationConstant, boolean isManualAllowed, int maxManualSpeed, int manualToAutomaticTime) {
		super(id);
		this.sidingId = sidingId;
		this.railLength = RailwayData.round(railLength, 3);
		this.trainId = trainId;
		// TODO temporary code for backwards compatibility
		baseTrainType = baseTrainType.startsWith("base_") ? baseTrainType.replace("base_", "train_") : baseTrainType;
		// TODO temporary code end
		this.baseTrainType = baseTrainType;
		transportMode = TrainType.getTransportMode(baseTrainType);
		spacing = TrainType.getSpacing(baseTrainType);
		width = TrainType.getWidth(baseTrainType);
		this.trainCars = trainCars;
		this.isManualAllowed = isManualAllowed;
		isCurrentlyManual = isManualAllowed;
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
			float accelerationConstant, boolean isManualAllowed, int maxManualSpeed, int manualToAutomaticTime,
			Map<String, Value> map
	) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);

		this.sidingId = sidingId;
		this.railLength = RailwayData.round(railLength, 3);
		this.path = path;
		this.distances = distances;
		this.repeatIndex1 = repeatIndex1;
		this.repeatIndex2 = repeatIndex2;
		this.accelerationConstant = accelerationConstant;
		this.isManualAllowed = isManualAllowed;
		this.maxManualSpeed = maxManualSpeed;
		this.manualToAutomaticTime = manualToAutomaticTime;

		speed = messagePackHelper.getFloat(KEY_SPEED);
		railProgress = messagePackHelper.getDouble(KEY_RAIL_PROGRESS);
		elapsedDwellTicks = messagePackHelper.getFloat(KEY_ELAPSED_DWELL_TICKS);
		nextStoppingIndex = messagePackHelper.getInt(KEY_NEXT_STOPPING_INDEX);
		nextPlatformIndex = messagePackHelper.getInt(KEY_NEXT_PLATFORM_INDEX);
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
		trainCars = Math.min(transportMode.maxLength, (int) Math.floor(railLength / spacing));
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
			float accelerationConstant, boolean isManualAllowed, int maxManualSpeed, int manualToAutomaticTime,
			CompoundTag compoundTag
	) {
		super(compoundTag);

		this.sidingId = sidingId;
		this.railLength = RailwayData.round(railLength, 3);
		this.path = path;
		this.distances = distances;
		this.repeatIndex1 = repeatIndex1;
		this.repeatIndex2 = repeatIndex2;
		this.accelerationConstant = accelerationConstant;
		this.isManualAllowed = isManualAllowed;
		this.maxManualSpeed = maxManualSpeed;
		this.manualToAutomaticTime = manualToAutomaticTime;

		speed = compoundTag.getFloat(KEY_SPEED);
		railProgress = compoundTag.getDouble(KEY_RAIL_PROGRESS);
		elapsedDwellTicks = compoundTag.getFloat(KEY_ELAPSED_DWELL_TICKS);
		nextStoppingIndex = compoundTag.getInt(KEY_NEXT_STOPPING_INDEX);
		nextPlatformIndex = compoundTag.getInt(KEY_NEXT_PLATFORM_INDEX);
		reversed = compoundTag.getBoolean(KEY_REVERSED);

		trainId = compoundTag.getString(KEY_TRAIN_CUSTOM_ID);
		baseTrainType = compoundTag.getString(KEY_TRAIN_TYPE);
		transportMode = TrainType.getTransportMode(baseTrainType);
		spacing = TrainType.getSpacing(baseTrainType);
		width = TrainType.getWidth(baseTrainType);
		trainCars = Math.min(transportMode.maxLength, (int) Math.floor(railLength / spacing));
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
		railLength = RailwayData.round(packet.readFloat(), 3);
		speed = packet.readFloat();
		final float tempAccelerationConstant = RailwayData.round(packet.readFloat(), 3);
		accelerationConstant = tempAccelerationConstant <= 0 ? ACCELERATION_DEFAULT : tempAccelerationConstant;
		railProgress = packet.readDouble();
		elapsedDwellTicks = packet.readFloat();
		nextStoppingIndex = packet.readInt();
		nextPlatformIndex = packet.readInt();
		reversed = packet.readBoolean();
		trainId = packet.readUtf(PACKET_STRING_READ_LENGTH);
		baseTrainType = packet.readUtf(PACKET_STRING_READ_LENGTH);
		transportMode = TrainType.getTransportMode(baseTrainType);
		spacing = TrainType.getSpacing(baseTrainType);
		width = TrainType.getWidth(baseTrainType);
		trainCars = Math.min(transportMode.maxLength, (int) Math.floor(railLength / spacing));
		isManualAllowed = packet.readBoolean();
		isCurrentlyManual = packet.readBoolean();
		maxManualSpeed = packet.readInt();
		manualToAutomaticTime = packet.readInt();
		isOnRoute = packet.readBoolean();
		manualNotch = packet.readInt();
		doorTarget = packet.readBoolean();

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
		messagePacker.packString(KEY_ELAPSED_DWELL_TICKS).packFloat(elapsedDwellTicks);
		messagePacker.packString(KEY_NEXT_STOPPING_INDEX).packLong(nextStoppingIndex);
		messagePacker.packString(KEY_NEXT_PLATFORM_INDEX).packLong(nextPlatformIndex);
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
		return super.messagePackLength() + 12;
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
		packet.writeFloat(elapsedDwellTicks);
		packet.writeInt(nextStoppingIndex);
		packet.writeInt(nextPlatformIndex);
		packet.writeBoolean(reversed);
		packet.writeUtf(trainId);
		packet.writeUtf(baseTrainType);
		packet.writeBoolean(isManualAllowed);
		packet.writeBoolean(isCurrentlyManual);
		packet.writeInt(maxManualSpeed);
		packet.writeInt(manualToAutomaticTime);
		packet.writeBoolean(isOnRoute);
		packet.writeInt(manualNotch);
		packet.writeBoolean(doorTarget);
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
		if (doorValue == 0 && isAccelerate && manualNotch >= -2 && manualNotch < 2) {
			manualNotch++;
			return true;
		} else if (!isAccelerate && manualNotch > -2) {
			manualNotch--;
			return true;
		} else {
			return false;
		}
	}

	public boolean toggleDoors() {
		if (speed == 0) {
			doorTarget = !doorTarget;
			manualNotch = -2;
			return true;
		} else {
			doorTarget = false;
			return false;
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

	public final boolean isPlayerRiding(Player player) {
		return ridingEntities.contains(player.getUUID());
	}

	public final float getSpeed() {
		return speed;
	}

	public final float getDoorValue() {
		return doorValue;
	}

	public final float getElapsedDwellTicks() {
		return elapsedDwellTicks;
	}

	public final boolean isReversed() {
		return reversed;
	}

	public final boolean isOnRoute() {
		return isOnRoute;
	}

	public int getTotalDwellTicks() {
		return path.get(nextStoppingIndex).dwellTime * 10;
	}

	protected final void simulateTrain(Level world, float ticksElapsed, Depot depot) {
		if (world == null) {
			return;
		}

		try {
			if (nextStoppingIndex >= path.size()) {
				return;
			}

			final boolean tempDoorOpen;
			final float tempDoorValue;
			final int totalDwellTicks = getTotalDwellTicks();

			if (!isOnRoute) {
				railProgress = (railLength + trainCars * spacing) / 2;
				reversed = false;
				tempDoorOpen = false;
				tempDoorValue = 0;
				speed = 0;
				nextStoppingIndex = 0;

				if (!isCurrentlyManual && canDeploy(depot) || isCurrentlyManual && manualNotch > 0) {
					startUp(world, trainCars, spacing, isOppositeRail());
				}
			} else {
				final float newAcceleration = accelerationConstant * ticksElapsed;

				if (railProgress >= distances.get(distances.size() - 1) - (railLength - trainCars * spacing) / 2) {
					isOnRoute = false;
					manualNotch = -2;
					ridingEntities.clear();
					tempDoorOpen = false;
					tempDoorValue = 0;
				} else {
					if (speed <= 0) {
						speed = 0;

						final boolean isOppositeRail = isOppositeRail();
						final boolean railBlocked = isRailBlocked(getIndex(0, spacing, true) + (isOppositeRail ? 2 : 1));

						if (totalDwellTicks == 0) {
							tempDoorOpen = false;
						} else {
							if (elapsedDwellTicks == 0 && isRepeat() && getIndex(railProgress, false) >= repeatIndex2 && distances.size() > repeatIndex1) {
								if (path.get(repeatIndex2).isOppositeRail(path.get(repeatIndex1))) {
									railProgress = distances.get(repeatIndex1 - 1) + trainCars * spacing;
									reversed = !reversed;
								} else {
									railProgress = distances.get(repeatIndex1);
								}
							}

							if (elapsedDwellTicks < totalDwellTicks - DOOR_MOVE_TIME - DOOR_DELAY - ticksElapsed || !railBlocked) {
								elapsedDwellTicks += ticksElapsed;
							}

							tempDoorOpen = openDoors();
						}

						if (!world.isClientSide() && (isCurrentlyManual || elapsedDwellTicks >= totalDwellTicks) && !railBlocked && (!isCurrentlyManual || manualNotch > 0)) {
							startUp(world, trainCars, spacing, isOppositeRail);
						}
					} else {
						if (!world.isClientSide()) {
							final int checkIndex = getIndex(0, spacing, true) + 1;
							if (isRailBlocked(checkIndex)) {
								nextStoppingIndex = checkIndex - 1;
							} else if (nextPlatformIndex > 0 && nextPlatformIndex < path.size()) {
								nextStoppingIndex = nextPlatformIndex;
							}
						}

						final double stoppingDistance = distances.get(nextStoppingIndex) - railProgress;
						if (!transportMode.continuousMovement && stoppingDistance < 0.5 * speed * speed / accelerationConstant) {
							speed = stoppingDistance <= 0 ? Train.ACCELERATION_DEFAULT : (float) Math.max(speed - (0.5 * speed * speed / stoppingDistance) * ticksElapsed, Train.ACCELERATION_DEFAULT);
							manualNotch = -3;
						} else {
							if (isCurrentlyManual) {
								if (manualNotch >= -2) {
									final RailType railType = convertMaxManualSpeed(maxManualSpeed);
									speed = Mth.clamp(speed + manualNotch * newAcceleration / 2, 0, railType == null ? RailType.IRON.maxBlocksPerTick : railType.maxBlocksPerTick);
								}
							} else {
								final float railSpeed = getRailSpeed(getIndex(0, spacing, false));
								if (speed < railSpeed) {
									speed = Math.min(speed + newAcceleration, railSpeed);
									manualNotch = 2;
								} else if (speed > railSpeed) {
									speed = Math.max(speed - newAcceleration, railSpeed);
									manualNotch = -2;
								} else {
									manualNotch = 0;
								}
							}
						}

						tempDoorOpen = transportMode.continuousMovement && openDoors();
					}

					railProgress += speed * ticksElapsed;
					if (!transportMode.continuousMovement && railProgress > distances.get(nextStoppingIndex)) {
						railProgress = distances.get(nextStoppingIndex);
						speed = 0;
						manualNotch = -2;
					}

					tempDoorValue = Mth.clamp(doorValue + ticksElapsed * (doorTarget ? 1 : -1) / DOOR_MOVE_TIME, 0, 1);
				}
			}

			doorTarget = tempDoorOpen;
			doorValue = tempDoorValue;
			if (doorTarget || doorValue != 0) {
				manualNotch = -2;
			}

			if (!path.isEmpty()) {
				final Vec3[] positions = new Vec3[trainCars];
				/*for (int i = 0; i <= trainCars; i++) {
					positions[i] = getRoutePosition(reversed ? trainCars - i : i, spacing);
				}*/

				final Vec3[] accurateBFPositions = new Vec3[trainCars];
				final Vec3[] accurateBRPositions = new Vec3[trainCars];

				final float[] BRYaw = new float[trainCars + 1];
				final float[] BRPitch = new float[trainCars + 1];

				final float[] BFYaw = new float[trainCars + 1];
				final float[] BFPitch = new float[trainCars + 1];

				int realCarI = 0;

				final double realAccurateSpacing = spacing / 256F;
				double bogie_spacing = 0.1625;

				TrainProperties trainProperties = TrainClientRegistry.getTrainProperties(trainId);

				String[] bogies_p = trainProperties.bogiePositions.split("\\|");

				for (int i = 0; i < trainCars; i++) {

					double lastBogieSpacing = 0.1625;

					if(!trainProperties.bogiePositions.equalsIgnoreCase("")) {
						for (String cars : bogies_p) {
							//Logger LOGGER = LogManager.getLogger(MOD_ID);
							//LOGGER.error("cars: " + cars);
							String whitelisted_cars = cars.split(";")[1];
							String blacklisted_cars = cars.split(";")[2];

							if (whitelisted_cars.contains("%")) {
								int carIndex = Integer.parseInt(whitelisted_cars.replace("%", ""));
								if (i % carIndex == 0) {
									lastBogieSpacing = Double.parseDouble(cars.split(";")[0]);
									bogie_spacing = lastBogieSpacing;
								}
							} else if (Integer.parseInt(whitelisted_cars) == i || (Integer.parseInt(whitelisted_cars) == -1 && i == trainCars - 1)) {
								lastBogieSpacing = Double.parseDouble(cars.split(";")[0]);
								bogie_spacing = lastBogieSpacing;
							}

							if (whitelisted_cars.contains("%") || (!whitelisted_cars.contains("%") && (Integer.parseInt(whitelisted_cars) != i || (Integer.parseInt(whitelisted_cars) == -1 && i != trainCars - 1)))) {
								if(blacklisted_cars.contains(",")){
									String[] bcars = blacklisted_cars.split(",");
									for(String blacklistCar: bcars){
										if (blacklistCar.contains("%")) {
											int carIndex = Integer.parseInt(blacklistCar.replace("%", ""));
											if (i % carIndex == 0) {
												bogie_spacing = lastBogieSpacing;
											}
										} else if (Integer.parseInt(blacklistCar) == i || (Integer.parseInt(blacklistCar) == -1 && i == trainCars - 1)) {
											bogie_spacing = lastBogieSpacing;
										}
									}
								} else {
									if (blacklisted_cars.contains("%")) {
										int carIndex = Integer.parseInt(blacklisted_cars.replace("%", ""));
										if (i % carIndex == 0) {
											bogie_spacing = lastBogieSpacing;
										}
									} else if (Integer.parseInt(blacklisted_cars) == i || (Integer.parseInt(blacklisted_cars) == -1 && i == trainCars - 1)) {
										bogie_spacing = lastBogieSpacing;
									}
								}
							}
						}
					}

					realCarI += Math.floor(bogie_spacing * 256);

					final Vec3 BFPos1 = getRoutePosition(realCarI, realAccurateSpacing);
					final Vec3 BFPos2 = getRoutePosition(realCarI+1, realAccurateSpacing);

					double xBF = getAverage(BFPos1.x, BFPos2.x);
					double yBF = getAverage(BFPos1.y, BFPos2.y);
					double zBF = getAverage(BFPos1.z, BFPos2.z);

					final double realSpacingBF = BFPos2.distanceTo(BFPos1);
					final float yawBF = (float) Mth.atan2(BFPos2.x - BFPos1.x, BFPos2.z - BFPos1.z);
					final float pitchBF = realSpacingBF == 0 ? 0 : (float) asin((BFPos2.y - BFPos1.y) / realSpacingBF);

					BFYaw[i] = yawBF;
					BFPitch[i] = pitchBF;

					accurateBFPositions[i] = new Vec3(xBF, yBF, zBF);

					realCarI -= Math.max(0, Math.floor(bogie_spacing * 256));
					realCarI += 256;
					realCarI -= Math.floor(bogie_spacing * 256);

					final Vec3 BRPos1 = getRoutePosition(realCarI, realAccurateSpacing);
					final Vec3 BRPos2 = getRoutePosition(realCarI+1, realAccurateSpacing);

					double xBR = getAverage(BRPos1.x, BRPos2.x);
					double yBR = getAverage(BRPos1.y, BRPos2.y);
					double zBR = getAverage(BRPos1.z, BRPos2.z);

					final double realSpacingBR = BRPos2.distanceTo(BRPos1);
					final float yawBR = (float) Mth.atan2(BRPos2.x - BRPos1.x, BRPos2.z - BRPos1.z);
					final float pitchBR = realSpacingBR == 0 ? 0 : (float) asin((BRPos2.y - BRPos1.y) / realSpacingBR);

					BRYaw[i] = yawBR;
					BRPitch[i] = pitchBR;

					accurateBRPositions[i] = new Vec3(xBR, yBR, zBR);

					if(i%2 == 0){
						positions[i] = new Vec3(xBF, yBF, zBF);
					} else {
						positions[i] = new Vec3(xBR, yBR, zBR);
					}

					realCarI += Math.floor(bogie_spacing * 256);

				}

				/*final Vec3[] positions = new Vec3[trainCars + 1]; */


				if (handlePositions(world, positions, ticksElapsed)) {
					final double[] prevX = {0};
					final double[] prevY = {0};
					final double[] prevZ = {0};
					final float[] prevYaw = {0};
					final float[] prevPitch = {0};

					for (int i = 0; i < trainCars; i++) {
						final int ridingCar = i;
						calculateCar(world, accurateBFPositions, accurateBRPositions, i, totalDwellTicks, (x, y, z, yaw, pitch, realSpacing, doorLeftOpen, doorRightOpen) -> {
							simulateCar(
									world, ridingCar, ticksElapsed,
									x, y, z,
									yaw, pitch,
									accurateBFPositions[ridingCar].x, accurateBFPositions[ridingCar].y, accurateBFPositions[ridingCar].z,
									BFYaw[ridingCar], BFPitch[ridingCar],
									accurateBRPositions[ridingCar].x, accurateBRPositions[ridingCar].y, accurateBRPositions[ridingCar].z,
									BRYaw[ridingCar], BRPitch[ridingCar],
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

	protected final void calculateCar(Level world, Vec3[] accurateBFPositions, Vec3[] accurateBRPositions, int index, int dwellTicks, CalculateCarCallback calculateCarCallback) {
		final Vec3 pos1 = accurateBFPositions[index];
		final Vec3 pos2 = accurateBRPositions[index];

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

	protected void startUp(Level world, int trainCars, int trainSpacing, boolean isOppositeRail) {
		doorTarget = false;
		doorValue = 0;
		nextPlatformIndex = nextStoppingIndex;
	}

	protected boolean openDoors() {
		return doorTarget;
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

	protected abstract void simulateCar(
			Level world, int ridingCar, float ticksElapsed,
			double carX, double carY, double carZ, float carYaw, float carPitch,
			double xBF, double yBF, double zBF, float yawBF, float pitchBF,
			double xBR, double yBR, double zBR, float yawBR, float pitchBR,
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

	private double getRailProgress(int car, double trainSpacing) {
		return railProgress - car * trainSpacing;
	}

	private Vec3 getRoutePosition(int car, int trainSpacing) {
		final double tempRailProgress = Math.max(getRailProgress(car, trainSpacing) - getModelZOffset(), 0);
		final int index = getIndex(tempRailProgress, false);
		return path.get(index).rail.getPosition(tempRailProgress - (index == 0 ? 0 : distances.get(index - 1))).add(0, transportMode.railOffset, 0);
	}

	private Vec3 getRoutePosition(int car, double trainSpacing) {
		final double tempRailProgress = Math.max(getRailProgress(car, trainSpacing) - getModelZOffset(), 0);
		final int index = getIndex(tempRailProgress, false);
		return path.get(index).rail.getPosition(tempRailProgress - (index == 0 ? 0 : distances.get(index - 1))).add(0, transportMode.railOffset, 0);
	}

	private boolean scanDoors(Level world, double trainX, double trainY, double trainZ, float checkYaw, float pitch, double halfSpacing, int dwellTicks) {
		if (skipScanBlocks(world, trainX, trainY, trainZ)) {
			return false;
		}

		boolean hasPlatform = false;
		final Vec3 offsetVec = new Vec3(1, 0, 0).yRot(checkYaw).xRot(pitch);
		final Vec3 traverseVec = new Vec3(0, 0, 1).yRot(checkYaw).xRot(pitch);

		for (int checkX = 1; checkX <= 3; checkX++) {
			for (int checkY = -2; checkY <= 3; checkY++) {
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
