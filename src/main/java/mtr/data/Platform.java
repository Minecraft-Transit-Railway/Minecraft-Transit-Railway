package mtr.data;

import mtr.block.BlockRail;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
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

	private final Set<BlockPos> positions;
	private List<Triple<Integer, Long, Train.TrainType>> schedule;

	private final int[] frequencies;

	private static final String KEY_POS_1 = "pos_1";
	private static final String KEY_POS_2 = "pos_2";
	private static final String KEY_ROUTE_IDS = "route_ids";
	private static final String KEY_TRAIN_TYPES = "train_types";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_SHUFFLE_ROUTES = "shuffle_routes";
	private static final String KEY_SHUFFLE_TRAINS = "shuffle_trains";

	public Platform(BlockPos pos1, BlockPos pos2) {
		super();
		name = "1";
		positions = new HashSet<>();
		positions.add(pos1);
		positions.add(pos2);
		routeIds = new ArrayList<>();
		trainTypes = new ArrayList<>();
		frequencies = new int[HOURS_IN_DAY];
		shuffleRoutes = false;
		shuffleTrains = true;
		schedule = new ArrayList<>();
	}

	public Platform(CompoundTag tag) {
		super(tag);
		positions = new HashSet<>();
		positions.add(BlockPos.fromLong(tag.getLong(KEY_POS_1)));
		positions.add(BlockPos.fromLong(tag.getLong(KEY_POS_2)));

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
		positions = new HashSet<>();
		positions.add(packet.readBlockPos());
		positions.add(packet.readBlockPos());

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
		tag.putLong(KEY_POS_1, getPosition(0).asLong());
		tag.putLong(KEY_POS_2, getPosition(1).asLong());
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
		packet.writeBlockPos(getPosition(0));
		packet.writeBlockPos(getPosition(1));
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
		packet.writeInt(trainTypes.size());
		trainTypes.forEach(trainType -> packet.writeInt(trainType.ordinal()));
		packet.writeIntArray(frequencies);
		packet.writeBoolean(shuffleRoutes);
		packet.writeBoolean(shuffleTrains);
	}

	public BlockPos getMidPos() {
		final BlockPos pos = getPosition(0).add(getPosition(1));
		return new BlockPos(pos.getX() / 2, pos.getY() / 2, pos.getZ() / 2);
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

	public boolean inPlatform(int x, int z) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		return RailwayData.isBetween(x, pos1.getX(), pos2.getX()) && RailwayData.isBetween(z, pos1.getZ(), pos2.getZ());
	}

	public boolean isValidPlatform(WorldAccess world) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		return isValidPlatform(world, pos1, pos2) && isValidPlatform(world, pos2, pos1);
	}

	public boolean containsPos(BlockPos pos) {
		return positions.contains(pos);
	}

	public boolean isOverlapping(Platform newPlatform) {
		return containsPos(newPlatform.getPosition(0)) || containsPos(newPlatform.getPosition(1));
	}

	public List<Triple<Integer, Long, Train.TrainType>> getSchedule() {
		return schedule;
	}

	public float getHeadway(int hour) {
		return frequencies[hour] == 0 ? 0 : 2F * TICKS_PER_HOUR / frequencies[hour];
	}

	private BlockPos getPosition(int index) {
		return positions.size() > index ? new ArrayList<>(positions).get(index) : new BlockPos(0, 0, 0);
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

	private static boolean isValidPlatform(WorldAccess world, BlockPos posStart, BlockPos posEnd) {
		final BlockEntity entity = world.getBlockEntity(posStart);
		if (entity instanceof BlockRail.TileEntityRail) {
			final BlockRail.TileEntityRail entityRail = (BlockRail.TileEntityRail) entity;
			return entityRail.railMap.containsKey(posEnd) && entityRail.railMap.get(posEnd).railType == Rail.RailType.PLATFORM;
		} else {
			return false;
		}
	}

	private static <T> T getRandomElementFromList(List<T> list) {
		return list.get(new Random().nextInt(list.size()));
	}
}
