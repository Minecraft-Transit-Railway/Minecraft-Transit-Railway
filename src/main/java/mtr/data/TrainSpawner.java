package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

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
	public final int[] frequencies;

	public static final int HOURS_IN_DAY = 24;

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
		frequencies = new int[24];
		removeTrains = true;
		shuffleRoutes = false;
		shuffleTrains = true;
	}

	public TrainSpawner(BlockPos pos, List<Long> routeIds, List<Train.TrainType> trainTypes, boolean removeTrains, boolean shuffleRoutes, boolean shuffleTrains) {
		this.pos = pos;
		this.routeIds = routeIds;
		this.trainTypes = trainTypes;
		frequencies = new int[24];
		this.removeTrains = removeTrains;
		this.shuffleRoutes = shuffleRoutes;
		this.shuffleTrains = shuffleTrains;
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

	@Override
	public String toString() {
		return String.format("Train Spawner: (%d, %d, %d) %s", pos.getX(), pos.getY(), pos.getZ(), routeIds);
	}
}
