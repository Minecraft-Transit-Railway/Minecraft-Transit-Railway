package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public final class Platform {

	private final BlockPos pos;
	private final Direction.Axis axis;
	private final int length;

	private static final int RADIUS = 4;
	private static final String KEY_POS = "pos";
	private static final String KEY_AXIS = "axis";
	private static final String KEY_LENGTH = "length";

	public Platform(BlockPos pos, Direction.Axis axis, int length) {
		this.pos = pos;
		this.axis = axis;
		this.length = length;
	}

	public Platform(CompoundTag tag) {
		final int[] posArray = tag.getIntArray(KEY_POS);
		pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
		axis = tag.getBoolean(KEY_AXIS) ? Direction.Axis.X : Direction.Axis.Z;
		length = tag.getInt(KEY_LENGTH);
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putIntArray(KEY_POS, new int[]{pos.getX(), pos.getY(), pos.getZ()});
		tag.putBoolean(KEY_AXIS, axis == Direction.Axis.X);
		tag.putInt(KEY_LENGTH, length);
		return tag;
	}

	public boolean isInPlatform(BlockPos checkPos) {
		final int x = checkPos.getX();
		final int y = checkPos.getY();
		final int z = checkPos.getZ();

		final int x1 = pos.getX() - RADIUS;
		final int y1 = pos.getY();
		final int z1 = pos.getZ() - RADIUS;

		final int x2 = pos.getX() + RADIUS + (axis == Direction.Axis.X ? length : 0);
		final int y2 = pos.getY() + RADIUS;
		final int z2 = pos.getZ() + RADIUS + (axis == Direction.Axis.Z ? length : 0);

		return x1 <= x && x <= x2 && y1 <= y && y <= y2 && z1 <= z && z <= z2;
	}

	@Override
	public String toString() {
		return String.format("Platform: %s, +%d%s", pos, length, axis);
	}
}
