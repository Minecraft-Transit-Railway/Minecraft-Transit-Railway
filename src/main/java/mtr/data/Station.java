package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;

public final class Station {

	public final String name;
	public Pair<Integer, Integer> corner1, corner2;

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
		corner1 = new Pair<>(tag.getInt(KEY_X_MIN), tag.getInt(KEY_Z_MIN));
		corner2 = new Pair<>(tag.getInt(KEY_X_MAX), tag.getInt(KEY_Z_MAX));
	}

	public Station(PacketByteBuf packet) {
		name = packet.readString();
		corner1 = new Pair<>(packet.readInt(), packet.readInt());
		corner2 = new Pair<>(packet.readInt(), packet.readInt());
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putString(KEY_NAME, name);
		tag.putInt(KEY_X_MIN, corner1.getLeft());
		tag.putInt(KEY_Z_MIN, corner1.getRight());
		tag.putInt(KEY_X_MAX, corner2.getLeft());
		tag.putInt(KEY_Z_MAX, corner2.getRight());
		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeString(name);
		packet.writeInt(corner1.getLeft());
		packet.writeInt(corner1.getRight());
		packet.writeInt(corner2.getLeft());
		packet.writeInt(corner2.getRight());
	}

	public void changeArea(Pair<Integer, Integer> corner1, Pair<Integer, Integer> corner2) {
		this.corner1 = corner1;
		this.corner2 = corner2;
	}

	@Override
	public String toString() {
		return String.format("Station %s: (%d, %d) (%d, %d)", name, corner1.getLeft(), corner1.getRight(), corner2.getLeft(), corner2.getRight());
	}
}
