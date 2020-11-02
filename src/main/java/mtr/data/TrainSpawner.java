package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class TrainSpawner extends DataBase {

	public final BlockPos pos;
	public final Set<Long> routeIds;

	private static final String KEY_POS = "pos";
	private static final String KEY_ROUTE_IDS = "route_ids";

	public TrainSpawner(BlockPos pos, Set<Long> routeIds) {
		this.pos = pos;
		this.routeIds = routeIds;
	}

	public TrainSpawner(CompoundTag tag) {
		pos = BlockPos.fromLong(tag.getLong(KEY_POS));
		routeIds = new HashSet<>();
		final long[] routeIdsArray = tag.getLongArray(KEY_ROUTE_IDS);
		for (final long routeId : routeIdsArray) {
			routeIds.add(routeId);
		}
	}

	public TrainSpawner(PacketByteBuf packet) {
		pos = packet.readBlockPos();
		routeIds = new HashSet<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			routeIds.add(packet.readLong());
		}
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putLong(KEY_POS, pos.asLong());
		tag.putLongArray(KEY_ROUTE_IDS, new ArrayList<>(routeIds));
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		packet.writeBlockPos(pos);
		packet.writeInt(routeIds.size());
		for (final long routeId : routeIds) {
			packet.writeLong(routeId);
		}
	}

	@Override
	public String toString() {
		return String.format("Train Spawner: (%d, %d, %d) %s", pos.getX(), pos.getY(), pos.getZ(), routeIds);
	}
}
