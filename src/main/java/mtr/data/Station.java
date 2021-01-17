package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

public final class Station extends DataBase {

	public Pair<Integer, Integer> corner1, corner2;

	private static final String KEY_X_MIN = "x_min";
	private static final String KEY_Z_MIN = "z_min";
	private static final String KEY_X_MAX = "x_max";
	private static final String KEY_Z_MAX = "z_max";

	public Station() {
		super();
	}

	public Station(CompoundTag tag) {
		super(tag);
		corner1 = new Pair<>(tag.getInt(KEY_X_MIN), tag.getInt(KEY_Z_MIN));
		corner2 = new Pair<>(tag.getInt(KEY_X_MAX), tag.getInt(KEY_Z_MAX));
	}

	public Station(PacketByteBuf packet) {
		super(packet);
		corner1 = new Pair<>(packet.readInt(), packet.readInt());
		corner2 = new Pair<>(packet.readInt(), packet.readInt());
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putInt(KEY_X_MIN, corner1.getLeft());
		tag.putInt(KEY_Z_MIN, corner1.getRight());
		tag.putInt(KEY_X_MAX, corner2.getLeft());
		tag.putInt(KEY_Z_MAX, corner2.getRight());
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(corner1.getLeft());
		packet.writeInt(corner1.getRight());
		packet.writeInt(corner2.getLeft());
		packet.writeInt(corner2.getRight());
	}

	public boolean inStation(int x, int z) {
		return RailwayData.isBetween(x, corner1.getLeft(), corner2.getLeft()) && RailwayData.isBetween(z, corner1.getRight(), corner2.getRight());
	}

	public BlockPos getCenter() {
		return new BlockPos((corner1.getLeft() + corner2.getLeft()) / 2, 0, (corner1.getRight() + corner2.getRight()) / 2);
	}

	@Override
	public String toString() {
		return String.format("Station %s: (%d, %d) (%d, %d)", name, corner1.getLeft(), corner1.getRight(), corner2.getLeft(), corner2.getRight());
	}
}
