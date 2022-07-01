package mtr.data;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.AABB;
import org.apache.commons.lang3.math.NumberUtils;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.*;

public abstract class SavedRailBase extends NameColorDataBase {

	protected int dwellTime;
	private final Set<BlockPos> positions;

	public static final int MAX_DWELL_TIME = 1200;
	private static final int DEFAULT_DWELL_TIME = 20;
	private static final String KEY_POS_1 = "pos_1";
	private static final String KEY_POS_2 = "pos_2";
	private static final String KEY_DWELL_TIME = "dwell_time";

	public SavedRailBase(long id, TransportMode transportMode, BlockPos pos1, BlockPos pos2) {
		super(id, transportMode);
		name = "1";
		positions = new HashSet<>(2);
		positions.add(pos1);
		positions.add(pos2);
		dwellTime = transportMode.continuousMovement ? 1 : DEFAULT_DWELL_TIME;
	}

	public SavedRailBase(TransportMode transportMode, BlockPos pos1, BlockPos pos2) {
		super(transportMode);
		name = "1";
		positions = new HashSet<>(2);
		positions.add(pos1);
		positions.add(pos2);
		dwellTime = transportMode.continuousMovement ? 1 : DEFAULT_DWELL_TIME;
	}

	public SavedRailBase(Map<String, Value> map) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
		positions = new HashSet<>(2);
		positions.add(BlockPos.of(messagePackHelper.getLong(KEY_POS_1)));
		positions.add(BlockPos.of(messagePackHelper.getLong(KEY_POS_2)));
		dwellTime = transportMode.continuousMovement ? 1 : messagePackHelper.getInt(KEY_DWELL_TIME);
	}

	@Deprecated
	public SavedRailBase(CompoundTag compoundTag) {
		super(compoundTag);
		positions = new HashSet<>(2);
		positions.add(BlockPos.of(compoundTag.getLong(KEY_POS_1)));
		positions.add(BlockPos.of(compoundTag.getLong(KEY_POS_2)));
		dwellTime = transportMode.continuousMovement ? 1 : compoundTag.getInt(KEY_DWELL_TIME);
	}

	public SavedRailBase(FriendlyByteBuf packet) {
		super(packet);
		positions = new HashSet<>(2);
		positions.add(packet.readBlockPos());
		positions.add(packet.readBlockPos());
		dwellTime = packet.readInt();
		dwellTime = transportMode.continuousMovement ? 1 : dwellTime;
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		super.toMessagePack(messagePacker);

		messagePacker.packString(KEY_POS_1).packLong(getPosition(0).asLong());
		messagePacker.packString(KEY_POS_2).packLong(getPosition(1).asLong());
		messagePacker.packString(KEY_DWELL_TIME).packInt(dwellTime);
	}

	@Override
	public int messagePackLength() {
		return super.messagePackLength() + 3;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeBlockPos(getPosition(0));
		packet.writeBlockPos(getPosition(1));
		packet.writeInt(dwellTime);
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

	public int getDwellTime() {
		if (dwellTime <= 0 || dwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		}
		return transportMode.continuousMovement ? 1 : dwellTime;
	}

	protected void writeDwellTimePacket(FriendlyByteBuf packet, int newDwellTime) {
		if (transportMode.continuousMovement) {
			dwellTime = 1;
		} else if (newDwellTime <= 0 || newDwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		} else {
			dwellTime = newDwellTime;
		}
		packet.writeInt(dwellTime);
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
