package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public final class Route extends NamedColoredBase {

	public final List<Long> stationIds;

	private static final String KEY_STATION_IDS = "station_ids";
	private static final String KEY_STATION_ID = "station_id_";

	public Route() {
		super();
		stationIds = new ArrayList<>();
	}

	public Route(CompoundTag tag) {
		super(tag);
		stationIds = new ArrayList<>();
		final CompoundTag tagStations = tag.getCompound(KEY_STATION_IDS);
		for (String key : tagStations.getKeys()) {
			stationIds.add(tagStations.getLong(key));
		}
	}

	public Route(PacketByteBuf packet) {
		super(packet);
		stationIds = new ArrayList<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			stationIds.add(packet.readLong());
		}
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tagStationIds = new CompoundTag();
		int i = 0;
		for (Long stationId : stationIds) {
			tagStationIds.putLong(KEY_STATION_ID + i, stationId);
			i++;
		}
		final CompoundTag tag = super.toCompoundTag();
		tag.put(KEY_STATION_IDS, tagStationIds);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(stationIds.size());
		for (long stationId : stationIds) {
			packet.writeLong(stationId);
		}
	}
}
