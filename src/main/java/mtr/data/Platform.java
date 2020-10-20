package mtr.data;

import mtr.block.BlockPlatformRail;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.RailShape;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;

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
		pos = BlockPos.fromLong(tag.getLong(KEY_POS));
		axis = tag.getBoolean(KEY_AXIS) ? Direction.Axis.X : Direction.Axis.Z;
		length = tag.getInt(KEY_LENGTH);
	}

	public Platform(PacketByteBuf packet) {
		pos = packet.readBlockPos();
		axis = packet.readBoolean() ? Direction.Axis.X : Direction.Axis.Z;
		length = packet.readInt();
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putLong(KEY_POS, pos.asLong());
		tag.putBoolean(KEY_AXIS, axis == Direction.Axis.X);
		tag.putInt(KEY_LENGTH, length);
		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeBlockPos(pos);
		packet.writeBoolean(axis == Direction.Axis.X);
		packet.writeInt(length);
	}

	public BlockPos getPos1() {
		return pos;
	}

	public BlockPos getPos2() {
		if (axis == Direction.Axis.X) {
			return pos.offset(Direction.EAST, length);
		} else {
			return pos.offset(Direction.SOUTH, length);
		}
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

	public boolean overlaps(Platform platform) {
		final BlockPos pos3 = platform.pos;

		if (platform.axis != axis || pos3.getY() != pos.getY()) {
			return false;
		} else {
			final BlockPos pos2 = getPos2();
			final BlockPos pos4 = platform.getPos2();

			if (axis == Direction.Axis.X) {
				return pos3.getZ() == pos.getZ() && (RailwayData.isBetween(pos3.getX(), pos.getX(), pos2.getX()) || RailwayData.isBetween(pos.getX(), pos3.getX(), pos4.getX()));
			} else {
				return pos3.getX() == pos.getX() && (RailwayData.isBetween(pos3.getZ(), pos.getZ(), pos2.getZ()) || RailwayData.isBetween(pos.getZ(), pos3.getZ(), pos4.getZ()));
			}
		}
	}

	public boolean hasRail(WorldAccess world) {
		final Direction direction;
		final RailShape checkShape;
		if (axis == Direction.Axis.X) {
			direction = Direction.EAST;
			checkShape = RailShape.EAST_WEST;
		} else {
			direction = Direction.SOUTH;
			checkShape = RailShape.NORTH_SOUTH;
		}
		for (int i = 0; i <= length; i++) {
			final BlockState state = world.getBlockState(pos.offset(direction, i));
			if (!(state.getBlock() instanceof BlockPlatformRail) || state.get(BlockPlatformRail.SHAPE) != checkShape) {
				return false;
			}
		}
		return true;
	}

	@Override
	public String toString() {
		return String.format("Platform: %s, +%d%s", pos, length, axis);
	}
}
