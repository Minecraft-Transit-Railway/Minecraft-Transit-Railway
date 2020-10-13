package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public final class Station {

	public final String name;
	public int xMin, zMin, xMax, zMax;

	private static final String KEY_NAME = "name";
	private static final String KEY_X_MIN = "x_min";
	private static final String KEY_Z_MIN = "z_min";
	private static final String KEY_X_MAX = "x_max";
	private static final String KEY_Z_MAX = "z_max";

	public Station(String name) {
		this.name = name;
	}

	public Station(CompoundTag tag) {
		name = tag.getString(KEY_NAME);
		xMin = tag.getInt(KEY_X_MIN);
		zMin = tag.getInt(KEY_Z_MIN);
		xMax = tag.getInt(KEY_X_MAX);
		zMax = tag.getInt(KEY_Z_MAX);
	}

	public Station(PacketByteBuf packet) {
		name = packet.readString();
		xMin = packet.readInt();
		zMin = packet.readInt();
		xMax = packet.readInt();
		zMax = packet.readInt();
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putString(KEY_NAME, name);
		tag.putInt(KEY_X_MIN, xMin);
		tag.putInt(KEY_Z_MIN, zMin);
		tag.putInt(KEY_X_MAX, xMax);
		tag.putInt(KEY_Z_MAX, zMax);
		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeString(name);
		packet.writeInt(xMin);
		packet.writeInt(zMin);
		packet.writeInt(xMax);
		packet.writeInt(zMax);
	}

	@Override
	public String toString() {
		return String.format("Station %s: (%d, %d) (%d, %d)", name, xMin, zMin, xMax, zMax);
	}
}
