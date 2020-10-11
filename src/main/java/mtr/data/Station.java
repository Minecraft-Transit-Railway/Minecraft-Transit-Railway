package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public final class Station {

	public final String name;
	public final long id;

	private static final String KEY_NAME = "name";
	private static final String KEY_ID = "id";

	public Station(String name) {
		this.name = name;
		id = System.currentTimeMillis();
	}

	public Station(CompoundTag tag) {
		name = tag.getString(KEY_NAME);
		id = tag.getLong(KEY_ID);
	}

	public Station(PacketByteBuf packet) {
		name = packet.readString();
		id = packet.readLong();
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putString(KEY_NAME, name);
		tag.putLong(KEY_ID, id);
		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeString(name);
		packet.writeLong(id);
	}

	@Override
	public String toString() {
		return String.format("Station %s (%s)", id, name);
	}
}
