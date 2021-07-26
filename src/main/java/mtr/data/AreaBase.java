package mtr.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public abstract class AreaBase extends NameColorDataBase {

	public Pair<Integer, Integer> corner1, corner2;

	private static final String KEY_X_MIN = "x_min";
	private static final String KEY_Z_MIN = "z_min";
	private static final String KEY_X_MAX = "x_max";
	private static final String KEY_Z_MAX = "z_max";
	private static final String KEY_CORNERS = "corners";

	public AreaBase() {
		super();
	}

	public AreaBase(long id) {
		super(id);
	}

	public AreaBase(NbtCompound nbtCompound) {
		super(nbtCompound);
		setCorners(nbtCompound.getInt(KEY_X_MIN), nbtCompound.getInt(KEY_Z_MIN), nbtCompound.getInt(KEY_X_MAX), nbtCompound.getInt(KEY_Z_MAX));
	}

	public AreaBase(PacketByteBuf packet) {
		super(packet);
		setCorners(packet.readInt(), packet.readInt(), packet.readInt(), packet.readInt());
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();
		nbtCompound.putInt(KEY_X_MIN, corner1 == null ? 0 : corner1.getLeft());
		nbtCompound.putInt(KEY_Z_MIN, corner1 == null ? 0 : corner1.getRight());
		nbtCompound.putInt(KEY_X_MAX, corner2 == null ? 0 : corner2.getLeft());
		nbtCompound.putInt(KEY_Z_MAX, corner2 == null ? 0 : corner2.getRight());
		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(corner1 == null ? 0 : corner1.getLeft());
		packet.writeInt(corner1 == null ? 0 : corner1.getRight());
		packet.writeInt(corner2 == null ? 0 : corner2.getLeft());
		packet.writeInt(corner2 == null ? 0 : corner2.getRight());
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		if (key.equals(KEY_CORNERS)) {
			setCorners(packet.readInt(), packet.readInt(), packet.readInt(), packet.readInt());
		} else {
			super.update(key, packet);
		}
	}

	public void setCorners(Consumer<PacketByteBuf> sendPacket) {
		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_CORNERS);
		packet.writeInt(corner1 == null ? 0 : corner1.getLeft());
		packet.writeInt(corner1 == null ? 0 : corner1.getRight());
		packet.writeInt(corner2 == null ? 0 : corner2.getLeft());
		packet.writeInt(corner2 == null ? 0 : corner2.getRight());
		sendPacket.accept(packet);
	}


	public boolean inArea(int x, int z) {
		return nonNullCorners(this) && RailwayData.isBetween(x, corner1.getLeft(), corner2.getLeft()) && RailwayData.isBetween(z, corner1.getRight(), corner2.getRight());
	}

	public BlockPos getCenter() {
		return nonNullCorners(this) ? new BlockPos((corner1.getLeft() + corner2.getLeft()) / 2, 0, (corner1.getRight() + corner2.getRight()) / 2) : null;
	}

	private void setCorners(int corner1a, int corner1b, int corner2a, int corner2b) {
		corner1 = corner1a == 0 && corner1b == 0 ? null : new Pair<>(corner1a, corner1b);
		corner2 = corner2a == 0 && corner2b == 0 ? null : new Pair<>(corner2a, corner2b);
	}

	public static boolean nonNullCorners(AreaBase station) {
		return station != null && station.corner1 != null && station.corner2 != null;
	}
}
