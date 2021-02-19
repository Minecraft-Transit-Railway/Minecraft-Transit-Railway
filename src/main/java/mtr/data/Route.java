package mtr.data;

import mtr.path.PathData;
import mtr.path.PathFinder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class Route extends NameColorDataBase {

	public String customDestination;
	public boolean shuffleTrains;

	public final List<Long> platformIds;
	public final List<Train.TrainType> trainTypes;
	public final List<PathData> path;

	private final int[] frequencies;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;

	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_TRAIN_TYPES = "train_types";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_CUSTOM_DESTINATION = "custom_destination";
	private static final String KEY_SHUFFLE_TRAINS = "shuffle_trains";
	private static final String KEY_PATH = "path";

	public Route() {
		super();
		platformIds = new ArrayList<>();
		trainTypes = new ArrayList<>();
		path = new ArrayList<>();
		frequencies = new int[HOURS_IN_DAY];
		customDestination = "";
		shuffleTrains = true;
	}

	public Route(CompoundTag tag) {
		super(tag);

		platformIds = new ArrayList<>();
		final long[] platformIdsArray = tag.getLongArray(KEY_PLATFORM_IDS);
		for (final long platformId : platformIdsArray) {
			platformIds.add(platformId);
		}

		trainTypes = new ArrayList<>();
		final int[] trainTypesIndices = tag.getIntArray(KEY_TRAIN_TYPES);
		for (final int trainTypeIndex : trainTypesIndices) {
			trainTypes.add(Train.TrainType.values()[trainTypeIndex]);
		}

		frequencies = tag.getIntArray(KEY_FREQUENCIES);
		customDestination = tag.getString(KEY_CUSTOM_DESTINATION);
		shuffleTrains = tag.getBoolean(KEY_SHUFFLE_TRAINS);

		path = new ArrayList<>();
		final CompoundTag tagPath = tag.getCompound(KEY_PATH);
		for (final String key : tagPath.getKeys()) {
			path.add(new PathData(tagPath.getCompound(key)));
		}
	}

	public Route(PacketByteBuf packet) {
		super(packet);

		platformIds = new ArrayList<>();
		final int platformCount = packet.readInt();
		for (int i = 0; i < platformCount; i++) {
			platformIds.add(packet.readLong());
		}

		trainTypes = new ArrayList<>();
		final int trainTypeCount = packet.readInt();
		for (int i = 0; i < trainTypeCount; i++) {
			trainTypes.add(Train.TrainType.values()[packet.readInt()]);
		}

		frequencies = packet.readIntArray();
		customDestination = packet.readString(PACKET_STRING_READ_LENGTH);
		shuffleTrains = packet.readBoolean();

		path = new ArrayList<>();
		final int pathLength = packet.readInt();
		for (int i = 0; i < pathLength; i++) {
			path.add(new PathData(packet));
		}
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putLongArray(KEY_PLATFORM_IDS, platformIds);
		tag.putIntArray(KEY_TRAIN_TYPES, trainTypes.stream().map(Enum::ordinal).collect(Collectors.toList()));
		tag.putIntArray(KEY_FREQUENCIES, frequencies);
		tag.putString(KEY_CUSTOM_DESTINATION, customDestination);
		tag.putBoolean(KEY_SHUFFLE_TRAINS, shuffleTrains);

		final CompoundTag tagPath = new CompoundTag();
		for (int i = 0; i < path.size(); i++) {
			tagPath.put(KEY_PATH + i, path.get(i).toCompoundTag());
		}
		tag.put(KEY_PATH, tagPath);

		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(platformIds.size());
		platformIds.forEach(packet::writeLong);
		packet.writeInt(trainTypes.size());
		trainTypes.forEach(trainType -> packet.writeInt(trainType.ordinal()));
		packet.writeIntArray(frequencies);
		packet.writeString(customDestination);
		packet.writeBoolean(shuffleTrains);

		packet.writeInt(path.size());
		path.forEach(pathData -> pathData.writePacket(packet));
	}

	public void generateGraph(WorldAccess world, Set<Platform> platforms) {
		System.out.println("generated");
		final PathFinder routePathFinder = new PathFinder(world, platformIds.stream().map(platformId -> RailwayData.getDataById(platforms, platformId)).collect(Collectors.toList()));
		path.clear();
		path.addAll(routePathFinder.findPath());
	}

	public List<Pos3f> getPositions(float value) {
		final Train.TrainType trainType = Train.TrainType.M_TRAIN_MINI; // TODO
		final int trainCars = 5; // TODO

		final List<Pos3f> positions = new ArrayList<>();

		int pathDataIndex = getPathDataIndex(value);
		if (pathDataIndex < 0) {
			return new ArrayList<>();
		}

		float positionIndex = path.get(pathDataIndex).getPositionIndex(value);
		if (positionIndex < 0) {
			return new ArrayList<>();
		}

		int segmentsCreated = 0;
		float length = path.get(pathDataIndex).length;

		while (true) {
			final PathData pathData = path.get(pathDataIndex);
			length -= pathData.length;

			while (positionIndex >= length) {
				positions.add(pathData.getPosition(positionIndex - length));
				positionIndex -= trainType.getSpacing();
				segmentsCreated++;
				if (segmentsCreated > trainCars) {
					return positions;
				}
			}

			pathDataIndex--;
			if (pathDataIndex < 0) {
				return new ArrayList<>();
			}
		}
	}

	public int getFrequency(int index) {
		if (index >= 0 && index < frequencies.length) {
			return frequencies[index];
		} else {
			return 0;
		}
	}

	public void setFrequencies(int frequency, int index) {
		if (index >= 0 && index < frequencies.length) {
			frequencies[index] = frequency;
		}
	}

	private int getPathDataIndex(float value) {
		for (int i = 0; i < path.size(); i++) {
			final float thisTPrevious = path.get(i).tOffset;
			final float nextTPrevious = i + 1 < path.size() ? path.get(i + 1).tOffset : value + 1;
			if (value >= thisTPrevious && value < nextTPrevious) {
				return i;
			}
		}
		return -1;
	}
}
