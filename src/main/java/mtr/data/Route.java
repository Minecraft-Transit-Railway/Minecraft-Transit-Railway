package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Route extends DataBase {

	public String customDestination;
	public boolean shuffleTrains;

	public final List<Long> platformIds;
	public final List<Train.TrainType> trainTypes;

	private final int[] frequencies;

	public static final int HOURS_IN_DAY = 24;
	public static final int TICKS_PER_HOUR = 1000;

	private static final String KEY_PLATFORM_IDS = "platform_ids";
	private static final String KEY_TRAIN_TYPES = "train_types";
	private static final String KEY_FREQUENCIES = "frequencies";
	private static final String KEY_CUSTOM_DESTINATION = "custom_destination";
	private static final String KEY_SHUFFLE_TRAINS = "shuffle_trains";

	public Route() {
		super();
		platformIds = new ArrayList<>();
		trainTypes = new ArrayList<>();
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
		customDestination = packet.readString(32767);
		shuffleTrains = packet.readBoolean();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putLongArray(KEY_PLATFORM_IDS, platformIds);
		tag.putIntArray(KEY_TRAIN_TYPES, trainTypes.stream().map(Enum::ordinal).collect(Collectors.toList()));
		tag.putIntArray(KEY_FREQUENCIES, frequencies);
		tag.putString(KEY_CUSTOM_DESTINATION, customDestination);
		tag.putBoolean(KEY_SHUFFLE_TRAINS, shuffleTrains);
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
}
