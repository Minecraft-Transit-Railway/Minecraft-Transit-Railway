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

	public final BlockPos pos;
	public final Direction.Axis axis;
	public final int length;
	public final long stationId;

	private static final int RADIUS = 4;
	private static final String KEY_POS = "pos";
	private static final String KEY_AXIS = "axis";
	private static final String KEY_LENGTH = "length";
	private static final String KEY_STATION_ID = "station_id";

	public Platform(BlockPos pos, Direction.Axis axis, int length) {
		this.pos = pos;
		this.axis = axis;
		this.length = length;
		stationId = 0;
	}

	public Platform(CompoundTag tag) {
		final int[] posArray = tag.getIntArray(KEY_POS);
		pos = new BlockPos(posArray[0], posArray[1], posArray[2]);
		axis = tag.getBoolean(KEY_AXIS) ? Direction.Axis.X : Direction.Axis.Z;
		length = tag.getInt(KEY_LENGTH);
		stationId = tag.getLong(KEY_STATION_ID);
	}

	public Platform(PacketByteBuf packet) {
		pos = packet.readBlockPos();
		axis = packet.readBoolean() ? Direction.Axis.X : Direction.Axis.Z;
		length = packet.readInt();
		stationId = packet.readLong();
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putIntArray(KEY_POS, new int[]{pos.getX(), pos.getY(), pos.getZ()});
		tag.putBoolean(KEY_AXIS, axis == Direction.Axis.X);
		tag.putInt(KEY_LENGTH, length);
		tag.putLong(KEY_STATION_ID, stationId);
		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeBlockPos(pos);
		packet.writeBoolean(axis == Direction.Axis.X);
		packet.writeInt(length);
		packet.writeLong(stationId);
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
		if (platform.axis != axis || platform.pos.getY() != pos.getY()) {
			return false;
		} else if (axis == Direction.Axis.X) {
			return platform.pos.getZ() == pos.getZ() && (isBetween(platform.pos.getX(), pos.getX(), pos.getX() + length) || isBetween(pos.getX(), platform.pos.getX(), platform.pos.getX() + platform.length));
		} else {
			return platform.pos.getX() == pos.getX() && (isBetween(platform.pos.getZ(), pos.getZ(), pos.getZ() + length) || isBetween(pos.getZ(), platform.pos.getZ(), platform.pos.getZ() + platform.length));
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
			BlockState state = world.getBlockState(pos.offset(direction, i));
			if (!(state.getBlock() instanceof BlockPlatformRail) || state.get(BlockPlatformRail.SHAPE) != checkShape) {
				return false;
			}
		}
		return true;
	}

	private boolean isBetween(int value, int lower, int upper) {
		return value >= lower && value <= upper;
	}

	@Override
	public String toString() {
		return String.format("Platform: %s, +%d%s", pos, length, axis);
	}
}
