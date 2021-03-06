package mtr.data;

import mtr.block.BlockRail;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Platform extends NameColorDataBase {

	private int dwellTime;
	private final Set<BlockPos> positions;

	public static final int MAX_DWELL_TIME = 120;
	private static final int DEFAULT_DWELL_TIME = 12;

	private static final String KEY_DWELL_TIME = "dwell_time";
	private static final String KEY_POS_1 = "pos_1";
	private static final String KEY_POS_2 = "pos_2";

	public Platform(BlockPos pos1, BlockPos pos2) {
		super();
		name = "1";
		dwellTime = DEFAULT_DWELL_TIME;
		positions = new HashSet<>();
		positions.add(pos1);
		positions.add(pos2);
	}

	public Platform(CompoundTag tag) {
		super(tag);
		dwellTime = tag.getInt(KEY_DWELL_TIME);
		positions = new HashSet<>();
		positions.add(BlockPos.fromLong(tag.getLong(KEY_POS_1)));
		positions.add(BlockPos.fromLong(tag.getLong(KEY_POS_2)));
	}

	public Platform(PacketByteBuf packet) {
		super(packet);
		dwellTime = packet.readInt();
		positions = new HashSet<>();
		positions.add(packet.readBlockPos());
		positions.add(packet.readBlockPos());
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putInt(KEY_DWELL_TIME, dwellTime);
		tag.putLong(KEY_POS_1, getPosition(0).asLong());
		tag.putLong(KEY_POS_2, getPosition(1).asLong());
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(dwellTime);
		packet.writeBlockPos(getPosition(0));
		packet.writeBlockPos(getPosition(1));
	}

	public int getDwellTime() {
		if (dwellTime <= 0 || dwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		}
		return dwellTime;
	}

	public void setDwellTime(int newDwellTime) {
		if (newDwellTime <= 0 || newDwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		} else {
			dwellTime = newDwellTime;
		}
	}

	public BlockPos getMidPos() {
		final BlockPos pos = getPosition(0).add(getPosition(1));
		return new BlockPos(pos.getX() / 2, pos.getY() / 2, pos.getZ() / 2);
	}

	public boolean inPlatform(int x, int z) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		return RailwayData.isBetween(x, pos1.getX(), pos2.getX()) && RailwayData.isBetween(z, pos1.getZ(), pos2.getZ());
	}

	public boolean isValidPlatform(WorldAccess world) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		return isValidPlatform(world, pos1, pos2) && isValidPlatform(world, pos2, pos1);
	}

	public boolean containsPos(BlockPos pos) {
		return positions.contains(pos);
	}

	public boolean isOverlapping(Platform newPlatform) {
		return containsPos(newPlatform.getPosition(0)) || containsPos(newPlatform.getPosition(1));
	}

	public boolean isCloseToPlatform(BlockPos pos) {
		return new Box(getPosition(0), getPosition(1)).stretch(-4, 0, -4).stretch(5, 5, 5).contains(pos.getX(), pos.getY(), pos.getZ());
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

	private static boolean isValidPlatform(WorldAccess world, BlockPos posStart, BlockPos posEnd) {
		final BlockEntity entity = world.getBlockEntity(posStart);
		if (entity instanceof BlockRail.TileEntityRail) {
			final BlockRail.TileEntityRail entityRail = (BlockRail.TileEntityRail) entity;
			return entityRail.railMap.containsKey(posEnd) && entityRail.railMap.get(posEnd).railType == Rail.RailType.PLATFORM;
		} else {
			return false;
		}
	}
}
