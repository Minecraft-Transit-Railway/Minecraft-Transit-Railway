package mtr.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

public abstract class SavedRailBase extends NameColorDataBase {

	private final Set<BlockPos> positions;

	private static final String KEY_POS_1 = "pos_1";
	private static final String KEY_POS_2 = "pos_2";

	public SavedRailBase(long id, BlockPos pos1, BlockPos pos2) {
		super(id);
		name = "1";
		positions = new HashSet<>();
		positions.add(pos1);
		positions.add(pos2);
	}

	public SavedRailBase(BlockPos pos1, BlockPos pos2) {
		super();
		name = "1";
		positions = new HashSet<>();
		positions.add(pos1);
		positions.add(pos2);
	}

	public SavedRailBase(NbtCompound nbtCompound) {
		super(nbtCompound);
		positions = new HashSet<>();
		positions.add(BlockPos.fromLong(nbtCompound.getLong(KEY_POS_1)));
		positions.add(BlockPos.fromLong(nbtCompound.getLong(KEY_POS_2)));
	}

	public SavedRailBase(PacketByteBuf packet) {
		super(packet);
		positions = new HashSet<>();
		positions.add(packet.readBlockPos());
		positions.add(packet.readBlockPos());
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();
		nbtCompound.putLong(KEY_POS_1, getPosition(0).asLong());
		nbtCompound.putLong(KEY_POS_2, getPosition(1).asLong());
		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeBlockPos(getPosition(0));
		packet.writeBlockPos(getPosition(1));
	}

	public boolean containsPos(BlockPos pos) {
		return positions.contains(pos);
	}

	public BlockPos getMidPos() {
		return getMidPos(false);
	}

	public BlockPos getMidPos(boolean zeroY) {
		final BlockPos pos = getPosition(0).add(getPosition(1));
		return new BlockPos(pos.getX() / 2, zeroY ? 0 : pos.getY() / 2, pos.getZ() / 2);
	}

	public Direction.Axis getAxis() {
		final BlockPos difference = getPosition(0).subtract(getPosition(1));
		return Math.abs(difference.getX()) > Math.abs(difference.getZ()) ? Direction.Axis.X : Direction.Axis.Z;
	}

	public boolean isInvalidSavedRail(Map<BlockPos, Map<BlockPos, Rail>> rails) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		return isInvalidSavedRail(rails, pos1, pos2) || isInvalidSavedRail(rails, pos2, pos1);
	}

	public boolean isCloseToSavedRail(BlockPos pos) {
		return isCloseToSavedRail(pos, 4, 0, 4);
	}

	public boolean isCloseToSavedRail(BlockPos pos, int radius, int lower, int upper) {
		return new Box(getPosition(0), getPosition(1)).stretch(-radius, -lower, -radius).stretch(radius + 1, upper + 1, radius + 1).contains(pos.getX(), pos.getY(), pos.getZ());
	}

	public List<BlockPos> getOrderedPositions(BlockPos pos, boolean reverse) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		final double d1 = pos1.getSquaredDistance(pos);
		final double d2 = pos2.getSquaredDistance(pos);
		final List<BlockPos> orderedPositions = new ArrayList<>();
		if (d2 > d1 == reverse) {
			orderedPositions.add(pos2);
			orderedPositions.add(pos1);
		} else {
			orderedPositions.add(pos1);
			orderedPositions.add(pos2);
		}
		return orderedPositions;
	}

	public BlockPos getOtherPosition(BlockPos pos) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		return pos.equals(pos1) ? pos2 : pos1;
	}

	private BlockPos getPosition(int index) {
		return positions.size() > index ? new ArrayList<>(positions).get(index) : new BlockPos(0, 0, 0);
	}

	public static boolean isInvalidSavedRail(Map<BlockPos, Map<BlockPos, Rail>> rails, BlockPos pos1, BlockPos pos2) {
		return !RailwayData.containsRail(rails, pos1, pos2) || !rails.get(pos1).get(pos2).railType.hasSavedRail;
	}

	@Override
	public int compareTo(NameColorDataBase compare) {
		final boolean thisIsNumber = NumberUtils.isParsable(name);
		final boolean compareIsNumber = NumberUtils.isParsable(compare.name);

		if (thisIsNumber && compareIsNumber) {
			final int floatCompare = Float.compare(Float.parseFloat(name), Float.parseFloat(compare.name));
			return floatCompare == 0 ? super.compareTo(compare) : floatCompare;
		} else if (thisIsNumber) {
			return -1;
		} else if (compareIsNumber) {
			return 1;
		} else {
			return super.compareTo(compare);
		}
	}
}
