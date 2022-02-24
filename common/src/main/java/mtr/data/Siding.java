package mtr.data;

import io.netty.buffer.Unpooled;
import mtr.packet.IPacket;
import mtr.path.PathData;
import mtr.path.PathFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;

public class Siding extends SavedRailBase implements IPacket {

	private Level world;
	private Depot depot;
	private String trainId;
	private TrainType baseTrainType;
	private int trainCars;
	private boolean unlimitedTrains;
	private int maxTrains;

	private float accelerationConstant;

	private final float railLength;
	private final List<PathData> path = new ArrayList<>();
	private final List<Float> distances = new ArrayList<>();
	private final List<TimeSegment> timeSegments = new ArrayList<>();
	private final Map<Long, Map<Long, Float>> platformTimes = new HashMap<>();
	private final Set<TrainServer> trains = new HashSet<>();

	private static final String KEY_RAIL_LENGTH = "rail_length";
	private static final String KEY_BASE_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_ID = "train_custom_id";
	private static final String KEY_UNLIMITED_TRAINS = "unlimited_trains";
	private static final String KEY_MAX_TRAINS = "max_trains";
	private static final String KEY_PATH = "path";
	private static final String KEY_TRAINS = "trains";
	private static final String KEY_ACCELERATION_CONSTANT = "acceleration_constant";

	public Siding(long id, TransportMode transportMode, BlockPos pos1, BlockPos pos2, float railLength) {
		super(id, transportMode, pos1, pos2);
		this.railLength = railLength;
		setTrainDetails("", getTrainType(transportMode));
		accelerationConstant = Train.ACCELERATION_DEFAULT;
	}

	public Siding(TransportMode transportMode, BlockPos pos1, BlockPos pos2, float railLength) {
		super(transportMode, pos1, pos2);
		this.railLength = railLength;
		setTrainDetails("", getTrainType(transportMode));
		accelerationConstant = Train.ACCELERATION_DEFAULT;
	}

	public Siding(Map<String, Value> map) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
		railLength = messagePackHelper.getFloat(KEY_RAIL_LENGTH);
		setTrainDetails(messagePackHelper.getString(KEY_TRAIN_ID), TrainType.getOrDefault(messagePackHelper.getString(KEY_BASE_TRAIN_TYPE)));
		unlimitedTrains = messagePackHelper.getBoolean(KEY_UNLIMITED_TRAINS);
		maxTrains = messagePackHelper.getInt(KEY_MAX_TRAINS);
		final float tempAccelerationConstant = messagePackHelper.getFloat(KEY_ACCELERATION_CONSTANT, Train.ACCELERATION_DEFAULT);
		accelerationConstant = tempAccelerationConstant <= 0 ? Train.ACCELERATION_DEFAULT : tempAccelerationConstant;

		messagePackHelper.iterateArrayValue(KEY_PATH, pathSection -> path.add(new PathData(RailwayData.castMessagePackValueToSKMap(pathSection))));

		generateTimeSegments(path, timeSegments, platformTimes);

		messagePackHelper.iterateArrayValue(KEY_TRAINS, value -> trains.add(new TrainServer(id, railLength, path, distances, timeSegments, RailwayData.castMessagePackValueToSKMap(value))));
		generateDistances();
	}

	@Deprecated
	public Siding(CompoundTag compoundTag) {
		super(compoundTag);

		railLength = compoundTag.getFloat(KEY_RAIL_LENGTH);
		setTrainDetails(compoundTag.getString(KEY_TRAIN_ID), TrainType.getOrDefault(compoundTag.getString(KEY_BASE_TRAIN_TYPE)));
		unlimitedTrains = compoundTag.getBoolean(KEY_UNLIMITED_TRAINS);
		maxTrains = compoundTag.getInt(KEY_MAX_TRAINS);
		accelerationConstant = Train.ACCELERATION_DEFAULT;

		final CompoundTag tagPath = compoundTag.getCompound(KEY_PATH);
		final int pathCount = tagPath.getAllKeys().size();
		for (int i = 0; i < pathCount; i++) {
			path.add(new PathData(tagPath.getCompound(KEY_PATH + i)));
		}

		generateTimeSegments(path, timeSegments, platformTimes);

		final CompoundTag tagTrains = compoundTag.getCompound(KEY_TRAINS);
		tagTrains.getAllKeys().forEach(key -> trains.add(new TrainServer(id, railLength, path, distances, timeSegments, tagTrains.getCompound(key))));
		generateDistances();
	}

	public Siding(FriendlyByteBuf packet) {
		super(packet);
		railLength = packet.readFloat();
		setTrainDetails(packet.readUtf(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);
		unlimitedTrains = packet.readBoolean();
		maxTrains = packet.readInt();
		final float tempAccelerationConstant = packet.readFloat();
		accelerationConstant = tempAccelerationConstant <= 0 ? Train.ACCELERATION_DEFAULT : tempAccelerationConstant;
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		super.toMessagePack(messagePacker);

		messagePacker.packString(KEY_RAIL_LENGTH).packFloat(railLength);
		messagePacker.packString(KEY_TRAIN_ID).packString(trainId);
		messagePacker.packString(KEY_BASE_TRAIN_TYPE).packString(baseTrainType.toString());
		messagePacker.packString(KEY_UNLIMITED_TRAINS).packBoolean(unlimitedTrains);
		messagePacker.packString(KEY_MAX_TRAINS).packInt(maxTrains);
		messagePacker.packString(KEY_ACCELERATION_CONSTANT).packFloat(accelerationConstant);
		RailwayData.writeMessagePackDataset(messagePacker, path, KEY_PATH);
		RailwayData.writeMessagePackDataset(messagePacker, trains, KEY_TRAINS);
	}

	@Override
	public int messagePackLength() {
		return super.messagePackLength() + 8;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeFloat(railLength);
		packet.writeUtf(trainId);
		packet.writeInt(baseTrainType.ordinal());
		packet.writeBoolean(unlimitedTrains);
		packet.writeInt(maxTrains);
		packet.writeFloat(accelerationConstant);
	}

	@Override
	public void update(String key, FriendlyByteBuf packet) {
		switch (key) {
			case KEY_BASE_TRAIN_TYPE:
				setTrainDetails(packet.readUtf(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);
				trains.clear();
				break;
			case KEY_UNLIMITED_TRAINS:
				name = packet.readUtf(PACKET_STRING_READ_LENGTH);
				color = packet.readInt();
				unlimitedTrains = packet.readBoolean();
				maxTrains = packet.readInt();
				final float newAccelerationConstant = packet.readFloat();
				if (newAccelerationConstant > 0) {
					trains.clear();
					accelerationConstant = newAccelerationConstant;
				}
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	public void setTrainIdAndBaseType(String customId, TrainType trainType, Consumer<FriendlyByteBuf> sendPacket) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_BASE_TRAIN_TYPE);
		packet.writeUtf(customId);
		packet.writeInt(trainType.ordinal());
		sendPacket.accept(packet);
		setTrainDetails(customId, trainType);
	}

	public void setUnlimitedTrains(boolean unlimitedTrains, int maxTrains, float accelerationConstant, Consumer<FriendlyByteBuf> sendPacket) {
		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_UNLIMITED_TRAINS);
		packet.writeUtf(name);
		packet.writeInt(color);
		packet.writeBoolean(unlimitedTrains);
		packet.writeInt(maxTrains);
		packet.writeFloat(accelerationConstant);
		sendPacket.accept(packet);
		this.unlimitedTrains = unlimitedTrains;
		this.maxTrains = maxTrains;
		if (accelerationConstant > 0) {
			trains.clear();
			this.accelerationConstant = accelerationConstant;
		}
	}

	public String getTrainId() {
		return trainId;
	}

	public TrainType getBaseTrainType() {
		return baseTrainType;
	}

	public float getAccelerationConstant() {
		return accelerationConstant;
	}

	public void setSidingData(Level world, Depot depot, Map<BlockPos, Map<BlockPos, Rail>> rails) {
		this.world = world;
		this.depot = depot;

		if (depot == null) {
			trains.clear();
			path.clear();
			distances.clear();
		} else {
			if (path.isEmpty()) {
				generateDefaultPath(rails);
				generateDistances();
			}
			depot.platformTimes.clear();
			depot.platformTimes.putAll(platformTimes);
		}
	}

	public int generateRoute(MinecraftServer minecraftServer, List<PathData> mainPath, int successfulSegmentsMain, Map<BlockPos, Map<BlockPos, Rail>> rails, SavedRailBase firstPlatform, SavedRailBase lastPlatform) {
		final List<PathData> tempPath = new ArrayList<>();
		final int successfulSegments;
		if (firstPlatform == null || lastPlatform == null) {
			successfulSegments = 0;
		} else {
			final List<SavedRailBase> depotAndFirstPlatform = new ArrayList<>();
			depotAndFirstPlatform.add(this);
			depotAndFirstPlatform.add(firstPlatform);
			PathFinder.findPath(tempPath, rails, depotAndFirstPlatform, 0);

			if (tempPath.isEmpty()) {
				successfulSegments = 1;
			} else if (mainPath.isEmpty()) {
				tempPath.clear();
				successfulSegments = successfulSegmentsMain + 1;
			} else {
				PathFinder.appendPath(tempPath, mainPath);

				final List<SavedRailBase> lastPlatformAndDepot = new ArrayList<>();
				lastPlatformAndDepot.add(lastPlatform);
				lastPlatformAndDepot.add(this);
				final List<PathData> pathLastPlatformToDepot = new ArrayList<>();
				PathFinder.findPath(pathLastPlatformToDepot, rails, lastPlatformAndDepot, successfulSegmentsMain);

				if (pathLastPlatformToDepot.isEmpty()) {
					successfulSegments = successfulSegmentsMain + 1;
					tempPath.clear();
				} else {
					PathFinder.appendPath(tempPath, pathLastPlatformToDepot);
					successfulSegments = successfulSegmentsMain + 2;
				}
			}
		}

		final List<TimeSegment> tempTimeSegments = new ArrayList<>();
		final Map<Long, Map<Long, Float>> tempPlatformTimes = new HashMap<>();
		generateTimeSegments(tempPath, tempTimeSegments, tempPlatformTimes);

		minecraftServer.execute(() -> {
			try {
				path.clear();
				if (tempPath.isEmpty()) {
					generateDefaultPath(rails);
				} else {
					path.addAll(tempPath);
				}
				timeSegments.clear();
				timeSegments.addAll(tempTimeSegments);
				platformTimes.clear();
				platformTimes.putAll(tempPlatformTimes);
				generateDistances();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		return successfulSegments;
	}

	public void simulateTrain(DataCache dataCache, List<Map<UUID, Long>> trainPositions, SignalBlocks signalBlocks, Map<Player, Set<TrainServer>> trainsInPlayerRange, Set<TrainServer> trainsToSync, Map<Long, List<ScheduleEntry>> schedulesForPlatform) {
		if (depot == null) {
			return;
		}

		int trainsAtDepot = 0;
		boolean spawnTrain = true;

		final Set<Integer> railProgressSet = new HashSet<>();
		final Set<TrainServer> trainsToRemove = new HashSet<>();
		for (final TrainServer train : trains) {
			if (train.simulateTrain(world, 1, depot, dataCache, trainPositions, trainsInPlayerRange, schedulesForPlatform, unlimitedTrains)) {
				trainsToSync.add(train);
			}

			if (train.closeToDepot(baseTrainType.getSpacing() * trainCars)) {
				spawnTrain = false;
			}

			if (!train.getIsOnRoute()) {
				trainsAtDepot++;
				if (trainsAtDepot > 1) {
					trainsToRemove.add(train);
				}
			}

			final int roundedRailProgress = Math.round(train.getRailProgress() * 10);
			if (railProgressSet.contains(roundedRailProgress)) {
				trainsToRemove.add(train);
			}
			railProgressSet.add(roundedRailProgress);

			if (trainPositions != null) {
				train.writeTrainPositions(trainPositions, signalBlocks);
			}
		}

		if (trainCars > 0 && (trains.isEmpty() || spawnTrain && (unlimitedTrains || trains.size() <= maxTrains))) {
			final TrainServer train = new TrainServer(unlimitedTrains || maxTrains > 0 ? new Random().nextLong() : id, id, railLength, trainId, baseTrainType, trainCars, path, distances, accelerationConstant, timeSegments);
			trains.add(train);
		}

		if (!trainsToRemove.isEmpty()) {
			trainsToRemove.forEach(trains::remove);
		}
	}

	public int getMaxTrains() {
		return maxTrains;
	}

	public boolean getUnlimitedTrains() {
		return unlimitedTrains;
	}

	public void clearTrains() {
		trains.clear();
	}

	private void setTrainDetails(String trainId, TrainType baseTrainType) {
		this.trainId = trainId;
		this.baseTrainType = baseTrainType;
		trainCars = Math.min(transportMode.maxLength, (int) Math.floor(railLength / baseTrainType.getSpacing()));
	}

	private void generateDefaultPath(Map<BlockPos, Map<BlockPos, Rail>> rails) {
		trains.clear();

		final List<BlockPos> orderedPositions = getOrderedPositions(new BlockPos(0, 0, 0), false);
		final BlockPos pos1 = orderedPositions.get(0);
		final BlockPos pos2 = orderedPositions.get(1);
		if (RailwayData.containsRail(rails, pos1, pos2)) {
			path.add(new PathData(rails.get(pos1).get(pos2), id, 0, pos1, pos2, -1));
		}

		trains.add(new TrainServer(id, id, railLength, trainId, baseTrainType, trainCars, path, distances, accelerationConstant, timeSegments));
	}

	private void generateDistances() {
		distances.clear();

		float distanceSum = 0;
		for (final PathData pathData : path) {
			distanceSum += pathData.rail.getLength();
			distances.add(distanceSum);
		}

		if (path.size() != 1) {
			trains.removeIf(train -> (train.id == id) == unlimitedTrains);
		}
	}

	private void generateTimeSegments(List<PathData> path, List<TimeSegment> timeSegments, Map<Long, Map<Long, Float>> platformTimes) {
		timeSegments.clear();

		float distanceSum1 = 0;
		final List<Float> stoppingDistances = new ArrayList<>();
		for (final PathData pathData : path) {
			distanceSum1 += pathData.rail.getLength();
			if (pathData.dwellTime > 0) {
				stoppingDistances.add(distanceSum1);
			}
		}

		float railProgress = (railLength + trainCars * baseTrainType.getSpacing()) / 2;
		float nextStoppingDistance = 0;
		float speed = 0;
		float time = 0;
		float timeOld = 0;
		long savedRailBaseIdOld = 0;
		float distanceSum2 = 0;
		for (int i = 0; i < path.size(); i++) {
			if (railProgress >= nextStoppingDistance) {
				if (stoppingDistances.isEmpty()) {
					nextStoppingDistance = distanceSum1;
				} else {
					nextStoppingDistance = stoppingDistances.remove(0);
				}
			}

			final PathData pathData = path.get(i);
			final float railSpeed = pathData.rail.railType.canAccelerate ? pathData.rail.railType.maxBlocksPerTick : Math.max(speed, RailType.WOODEN.maxBlocksPerTick);
			distanceSum2 += pathData.rail.getLength();

			while (railProgress < distanceSum2) {
				final int speedChange;
				if (speed > railSpeed || nextStoppingDistance - railProgress + 1 < 0.5F * speed * speed / accelerationConstant) {
					speed = Math.max(speed - accelerationConstant, accelerationConstant);
					speedChange = -1;
				} else if (speed < railSpeed) {
					speed = Math.min(speed + accelerationConstant, railSpeed);
					speedChange = 1;
				} else {
					speedChange = 0;
				}

				if (timeSegments.isEmpty() || timeSegments.get(timeSegments.size() - 1).speedChange != speedChange) {
					timeSegments.add(new TimeSegment(railProgress, speed, time, speedChange, accelerationConstant));
				}

				railProgress = Math.min(railProgress + speed, distanceSum2);
				time++;

				final TimeSegment timeSegment = timeSegments.get(timeSegments.size() - 1);
				timeSegment.endRailProgress = railProgress;
				timeSegment.endTime = time;
				timeSegment.savedRailBaseId = nextStoppingDistance != distanceSum1 && railProgress == distanceSum2 ? pathData.savedRailBaseId : 0;
			}

			time += pathData.dwellTime * 5;

			if (pathData.savedRailBaseId != 0) {
				if (savedRailBaseIdOld != 0) {
					if (!platformTimes.containsKey(savedRailBaseIdOld)) {
						platformTimes.put(savedRailBaseIdOld, new HashMap<>());
					}
					platformTimes.get(savedRailBaseIdOld).put(pathData.savedRailBaseId, time - timeOld);
				}
				savedRailBaseIdOld = pathData.savedRailBaseId;
				timeOld = time;
			}

			time += pathData.dwellTime * 5;

			if (i + 1 < path.size() && pathData.isOppositeRail(path.get(i + 1))) {
				railProgress += baseTrainType.getSpacing() * trainCars;
			}
		}
	}

	private static TrainType getTrainType(TransportMode transportMode) {
		switch (transportMode) {
			case TRAIN:
				return TrainType.SP1900;
			case BOAT:
				return TrainType.OAK_BOAT;
			default:
				return TrainType.values()[0];
		}
	}

	public static class TimeSegment {

		public float endRailProgress;
		public long savedRailBaseId;
		public long lastStationId;
		public long routeId;
		public boolean isTerminating;
		public float endTime;

		public final float startRailProgress;
		private final float startSpeed;
		private final float startTime;
		private final int speedChange;
		private final float accelerationConstant;

		private TimeSegment(float startRailProgress, float startSpeed, float startTime, int speedChange, float accelerationConstant) {
			this.startRailProgress = startRailProgress;
			this.startSpeed = startSpeed;
			this.startTime = startTime;
			this.speedChange = Integer.compare(speedChange, 0);
			this.accelerationConstant = accelerationConstant <= 0 ? Train.ACCELERATION_DEFAULT : accelerationConstant;
		}

		public float getTime(float railProgress) {
			final float distance = railProgress - startRailProgress;
			if (speedChange == 0) {
				return startTime + distance / startSpeed;
			} else {
				final float acceleration = speedChange * accelerationConstant;
				return startTime + (float) (Math.sqrt(2 * acceleration * distance + startSpeed * startSpeed) - startSpeed) / acceleration;
			}
		}
	}
}
