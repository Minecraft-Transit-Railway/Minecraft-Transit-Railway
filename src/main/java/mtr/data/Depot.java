package mtr.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Depot extends AreaBase {

	public final List<Long> routeIds;

	private static final String KEY_ROUTE_IDS = "route_ids";

	public Depot() {
		super();
		routeIds = new ArrayList<>();
	}

	public Depot(long id) {
		super(id);
		routeIds = new ArrayList<>();
	}

	public Depot(CompoundTag tag) {
		super(tag);
		routeIds = new ArrayList<>();
		final long[] routeIdsArray = tag.getLongArray(KEY_ROUTE_IDS);
		for (final long routeId : routeIdsArray) {
			routeIds.add(routeId);
		}
	}

	public Depot(PacketByteBuf packet) {
		super(packet);
		routeIds = new ArrayList<>();
		final int routeIdCount = packet.readInt();
		for (int i = 0; i < routeIdCount; i++) {
			routeIds.add(packet.readLong());
		}
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putLongArray(KEY_ROUTE_IDS, routeIds);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		switch (key) {
			case KEY_ROUTE_IDS:
				routeIds.clear();
				final int routeIdCount = packet.readInt();
				for (int i = 0; i < routeIdCount; i++) {
					routeIds.add(packet.readLong());
				}
				break;
			default:
				super.update(key, packet);
		}
	}

	public void setRouteIds(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_ROUTE_IDS);
		packet.writeInt(routeIds.size());
		routeIds.forEach(packet::writeLong);
		sendPacket.accept(packet);
	}
}
