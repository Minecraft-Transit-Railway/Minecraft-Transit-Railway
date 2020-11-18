package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class TrainSpawner extends DataBase {

	public boolean removeTrains;
	public boolean shuffleRoutes;
	public boolean shuffleTrains;

	public final BlockPos pos;
	public final List<Long> routeIds;
	public final List<Train.TrainType> trainTypes;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;

	private List<Triple<Integer, Long, Train.TrainType>> schedule;

	private final int[] frequencies;

	private static final String KEY_POS = "pos";
	private static final String KEY_ROUTE_IDS = "route_ids";
	private static final String KEY_TRAIN_TYPES = "train_types";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_REMOVE_TRAINS = "remove_trains";
	private static final String KEY_SHUFFLE_ROUTES = "shuffle_routes";
	private static final String KEY_SHUFFLE_TRAINS = "shuffle_trains";

	public TrainSpawner(BlockPos pos) {
		this.pos = pos;
		routeIds = new ArrayList<>();
		trainTypes = new ArrayList<>();
		frequencies = new int[HOURS_IN_DAY];
		removeTrains = true;
		shuffleRoutes = false;
		shuffleTrains = true;
		schedule = new ArrayList<>();
	}

	public TrainSpawner(CompoundTag tag) {
		pos = BlockPos.fromLong(tag.getLong(KEY_POS));

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

		removeTrains = tag.getBoolean(KEY_REMOVE_TRAINS);
		shuffleRoutes = tag.getBoolean(KEY_SHUFFLE_ROUTES);
		shuffleTrains = tag.getBoolean(KEY_SHUFFLE_TRAINS);
	}

	public TrainSpawner(PacketByteBuf packet) {
		pos = packet.readBlockPos();

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

		removeTrains = packet.readBoolean();
		shuffleRoutes = packet.readBoolean();
		shuffleTrains = packet.readBoolean();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putLong(KEY_POS, pos.asLong());
		tag.putLongArray(KEY_ROUTE_IDS, routeIds);
		tag.putIntArray(KEY_TRAIN_TYPES, trainTypes.stream().map(Enum::ordinal).collect(Collectors.toList()));
		tag.putIntArray(KEY_FREQUENCIES, frequencies);
		tag.putBoolean(KEY_REMOVE_TRAINS, removeTrains);
		tag.putBoolean(KEY_SHUFFLE_ROUTES, shuffleRoutes);
		tag.putBoolean(KEY_SHUFFLE_TRAINS, shuffleTrains);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		packet.writeBlockPos(pos);
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
		packet.writeInt(trainTypes.size());
		trainTypes.forEach(trainType -> packet.writeInt(trainType.ordinal()));
		packet.writeIntArray(frequencies);
		packet.writeBoolean(removeTrains);
		packet.writeBoolean(shuffleRoutes);
		packet.writeBoolean(shuffleTrains);
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

	@Override
	public String toString() {
		return String.format("Train Spawner: (%d, %d, %d) %s", pos.getX(), pos.getY(), pos.getZ(), routeIds);
	}
}
