package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public final class Route extends DataBase {

	public final List<Long> stationIds;

	private static final String KEY_STATION_IDS = "station_ids";

	public Route() {
		super();
		stationIds = new ArrayList<>();
	}

	public Route(CompoundTag tag) {
		super(tag);
		stationIds = new ArrayList<>();
		final long[] stationIdsArray = tag.getLongArray(KEY_STATION_IDS);
		for (final long stationId : stationIdsArray) {
			stationIds.add(stationId);
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
		final CompoundTag tag = super.toCompoundTag();
		tag.putLongArray(KEY_STATION_IDS, stationIds);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(stationIds.size());
		for (final long stationId : stationIds) {
			packet.writeLong(stationId);
		}
	}
}
