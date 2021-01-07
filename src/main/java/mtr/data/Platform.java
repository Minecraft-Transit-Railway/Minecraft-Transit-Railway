package mtr.data;

import mtr.block.BlockPlatformRail;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.Collectors;

public final class Platform extends DataBase {

	public boolean shuffleRoutes;
	public boolean shuffleTrains;

	public final List<Long> routeIds;
	public final List<Train.TrainType> trainTypes;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;

	private BlockPos pos;
	private Direction.Axis axis;
	private int length;

	private List<Triple<Integer, Long, Train.TrainType>> schedule;

	private final int[] frequencies;

	private static final String KEY_POS = "pos";
	private static final String KEY_AXIS = "axis";
	private static final String KEY_LENGTH = "length";
	private static final String KEY_ROUTE_IDS = "route_ids";
	private static final String KEY_TRAIN_TYPES = "train_types";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_SHUFFLE_ROUTES = "shuffle_routes";
	private static final String KEY_SHUFFLE_TRAINS = "shuffle_trains";

	public Platform(BlockPos pos, Direction.Axis axis, int length) {
		super();
		name = "1";
		this.pos = pos;
		this.axis = axis;
		this.length = length;
		routeIds = new ArrayList<>();
		trainTypes = new ArrayList<>();
		frequencies = new int[HOURS_IN_DAY];
		shuffleRoutes = false;
		shuffleTrains = true;
		schedule = new ArrayList<>();
	}

	public Platform(CompoundTag tag) {
		super(tag);
		pos = BlockPos.fromLong(tag.getLong(KEY_POS));
		axis = tag.getBoolean(KEY_AXIS) ? Direction.Axis.X : Direction.Axis.Z;
		length = tag.getInt(KEY_LENGTH);

		routeIds = new ArrayList<>();
		final long[] routeIdsArray = tag.getLongArray(KEY_ROUTE_IDS);
		for (final long routeId : routeIdsArray) {
			routeIds.add(routeId);
		}

		trainTypes = new ArrayList<>();
		final int[] trainTypesIndices = tag.getIntArray(KEY_TRAIN_TYPES);
		for (final int trainTypeIndex : trainTypesIndices) {
			trainTypes.add(Train.TrainType.values()[trainTypeIndex]);
		}

		frequencies = tag.getIntArray(KEY_FREQUENCIES);
		generateSchedule();

		shuffleRoutes = tag.getBoolean(KEY_SHUFFLE_ROUTES);
		shuffleTrains = tag.getBoolean(KEY_SHUFFLE_TRAINS);
	}

	public Platform(PacketByteBuf packet) {
		super(packet);
		pos = packet.readBlockPos();
		axis = packet.readBoolean() ? Direction.Axis.X : Direction.Axis.Z;
		length = packet.readInt();

		routeIds = new ArrayList<>();
		final int routeCount = packet.readInt();
		for (int i = 0; i < routeCount; i++) {
			routeIds.add(packet.readLong());
		}

		trainTypes = new ArrayList<>();
		final int trainTypeCount = packet.readInt();
		for (int i = 0; i < trainTypeCount; i++) {
			trainTypes.add(Train.TrainType.values()[packet.readInt()]);
		}

		frequencies = packet.readIntArray();
		generateSchedule();

		shuffleRoutes = packet.readBoolean();
		shuffleTrains = packet.readBoolean();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putLong(KEY_POS, pos.asLong());
		tag.putBoolean(KEY_AXIS, axis == Direction.Axis.X);
		tag.putInt(KEY_LENGTH, length);
		tag.putLongArray(KEY_ROUTE_IDS, routeIds);
		tag.putIntArray(KEY_TRAIN_TYPES, trainTypes.stream().map(Enum::ordinal).collect(Collectors.toList()));
		tag.putIntArray(KEY_FREQUENCIES, frequencies);
		tag.putBoolean(KEY_SHUFFLE_ROUTES, shuffleRoutes);
		tag.putBoolean(KEY_SHUFFLE_TRAINS, shuffleTrains);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeBlockPos(pos);
		packet.writeBoolean(axis == Direction.Axis.X);
		packet.writeInt(length);
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
		packet.writeInt(trainTypes.size());
		trainTypes.forEach(trainType -> packet.writeInt(trainType.ordinal()));
		packet.writeIntArray(frequencies);
		packet.writeBoolean(shuffleRoutes);
		packet.writeBoolean(shuffleTrains);
	}

	public BlockPos getPos1() {
		return pos;
	}

	public BlockPos getPos2() {
		if (axis == Direction.Axis.X) {
			return pos.offset(Direction.EAST, length);
		} else {
			return pos.offset(Direction.SOUTH, length);
		}
	}

	public BlockPos getMidPos() {
		final BlockPos pos2 = getPos2();
		return new BlockPos((pos.getX() + pos2.getX()) / 2, pos.getY(), (pos.getZ() + pos2.getZ()) / 2);
	}

	public int getFrequency(int index) {
		if (index >= 0 && index < HOURS_IN_DAY) {
			return frequencies[index];
		} else {
			return 0;
		}
	}

	public void setFrequencies(int frequency, int index) {
		if (index >= 0 && index < HOURS_IN_DAY) {
			frequencies[index] = frequency;
		}
		generateSchedule();
	}

	public void updateDimensions(WorldAccess world) {
		final Platform tempPlatform = BlockPlatformRail.createNewPlatform(world, getMidPos());
		if (tempPlatform != null) {
			pos = tempPlatform.pos;
			axis = tempPlatform.axis;
			length = tempPlatform.length;
		}
	}

	public boolean inPlatform(int x, int z) {
		final BlockPos pos2 = getPos2();
		return RailwayData.isBetween(x, pos.getX(), pos2.getX()) && RailwayData.isBetween(z, pos.getZ(), pos2.getZ());
	}

	public Train createTrainOnPlatform(WorldAccess world, Set<Platform> platforms, Set<Route> routes, int worldTime) {
		final Direction spawnDirection = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
		final Optional<Triple<Integer, Long, Train.TrainType>> optionalScheduleEntry = getSchedule().stream().filter(scheduleEntry -> scheduleEntry.getLeft() == worldTime).findFirst();
		if (optionalScheduleEntry.isPresent()) {
			final Triple<Integer, Long, Train.TrainType> scheduleEntry = optionalScheduleEntry.get();
			final Route route = RailwayData.getDataById(routes, shuffleRoutes ? getRandomElementFromList(routeIds) : scheduleEntry.getMiddle());
			final Train.TrainType trainType = shuffleTrains ? getRandomElementFromList(trainTypes) : scheduleEntry.getRight();

			final Train newTrain = new Train(trainType, pos, (length + 1) / trainType.getSpacing(), spawnDirection);
			newTrain.paths.addAll(route.getPath(world, platforms, this));
			return newTrain;
		} else {
			return null;
		}
	}

	public int getLength() {
		return length;
	}

	public List<Triple<Integer, Long, Train.TrainType>> getSchedule() {
		return schedule;
	}

	public float getHeadway(int hour) {
		return frequencies[hour] == 0 ? 0 : 2F * TICKS_PER_HOUR / frequencies[hour];
	}

	private void generateSchedule() {
		final List<Triple<Integer, Long, Train.TrainType>> tempSchedule = new ArrayList<>();

		if (routeIds.size() > 0 && trainTypes.size() > 0) {
			int lastTime = -HOURS_IN_DAY * TICKS_PER_HOUR;
			int lastRouteIndex = -1;
			int lastTrainTypeIndex = -1;

			for (int i = 0; i < HOURS_IN_DAY * TICKS_PER_HOUR; i++) {
				final float headway = getHeadway(i / TICKS_PER_HOUR);
				if (headway > 0 && i >= headway + lastTime) {

					final long route;
					if (shuffleRoutes) {
						route = -1;
					} else {
						lastRouteIndex++;
						if (lastRouteIndex >= routeIds.size()) {
							lastRouteIndex = 0;
						}
						route = routeIds.get(lastRouteIndex);
					}

					final Train.TrainType trainType;
					if (shuffleTrains) {
						trainType = null;
					} else {
						lastTrainTypeIndex++;
						if (lastTrainTypeIndex >= trainTypes.size()) {
							lastTrainTypeIndex = 0;
						}
						trainType = trainTypes.get(lastTrainTypeIndex);
					}

					tempSchedule.add(new ImmutableTriple<>(i, route, trainType));
					lastTime = i;
				}
			}
		}

		schedule = tempSchedule;
	}

	private static <T> T getRandomElementFromList(List<T> list) {
		return list.get(new Random().nextInt(list.size()));
	}

	@Override
	public String toString() {
		return String.format("Platform: %s, +%d%s", pos, length, axis);
	}
}
