package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

public abstract class SavedRailBase extends NameColorDataBase {

	private final Set<BlockPos> positions;

	private static final String KEY_POS_1 = "pos_1";
	private static final String KEY_POS_2 = "pos_2";

	public SavedRailBase(long id, TransportMode transportMode, BlockPos pos1, BlockPos pos2) {
		super(id, transportMode);
		name = "1";
		positions = new HashSet<>();
		positions.add(pos1);
		positions.add(pos2);
	}

	public SavedRailBase(TransportMode transportMode, BlockPos pos1, BlockPos pos2) {
		super(transportMode);
		name = "1";
		positions = new HashSet<>();
		positions.add(pos1);
		positions.add(pos2);
	}

	public SavedRailBase(CompoundTag compoundTag) {
		super(compoundTag);
		positions = new HashSet<>();
		positions.add(BlockPos.of(compoundTag.getLong(KEY_POS_1)));
		positions.add(BlockPos.of(compoundTag.getLong(KEY_POS_2)));
	}

	public SavedRailBase(FriendlyByteBuf packet) {
		super(packet);
		positions = new HashSet<>();
		positions.add(packet.readBlockPos());
		positions.add(packet.readBlockPos());
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag compoundTag = super.toCompoundTag();
		compoundTag.putLong(KEY_POS_1, getPosition(0).asLong());
		compoundTag.putLong(KEY_POS_2, getPosition(1).asLong());
		return compoundTag;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeBlockPos(getPosition(0));
		packet.writeBlockPos(getPosition(1));
	}

	@Override
	protected final boolean hasTransportMode() {
		return true;
	}

	public boolean containsPos(BlockPos pos) {
		return positions.contains(pos);
	}

	public BlockPos getMidPos() {
		return getMidPos(false);
	}

	public BlockPos getMidPos(boolean zeroY) {
		final BlockPos pos = getPosition(0).offset(getPosition(1));
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

	public boolean isCloseToSavedRail(BlockPos pos, int radius, int lower, int upper) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		final int x1 = Math.min(pos1.getX(), pos2.getX());
		final int y1 = Math.min(pos1.getY(), pos2.getY());
		final int z1 = Math.min(pos1.getZ(), pos2.getZ());
		final int x2 = Math.max(pos1.getX(), pos2.getX());
		final int y2 = Math.max(pos1.getY(), pos2.getY());
		final int z2 = Math.max(pos1.getZ(), pos2.getZ());
		return new AABB(x1 - radius, y1 - lower, z1 - radius, x2 + radius + 1, y2 + upper + 1, z2 + radius + 1).contains(pos.getX(), pos.getY(), pos.getZ());
	}

	public List<BlockPos> getOrderedPositions(BlockPos pos, boolean reverse) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		final double d1 = pos1.distSqr(pos);
		final double d2 = pos2.distSqr(pos);
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
