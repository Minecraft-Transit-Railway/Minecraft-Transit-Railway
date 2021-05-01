package mtr.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

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

	@Override
	public void update(String key, PacketByteBuf packet) {
		if (KEY_DWELL_TIME.equals(key)) {
			dwellTime = packet.readInt();
		} else {
			super.update(key, packet);
		}
	}

	public int getDwellTime() {
		if (dwellTime <= 0 || dwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		}
		return dwellTime;
	}

	public void setDwellTime(int newDwellTime, Consumer<PacketByteBuf> sendPacket) {
		if (newDwellTime <= 0 || newDwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		} else {
			dwellTime = newDwellTime;
		}

		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_DWELL_TIME);
		packet.writeInt(dwellTime);
		sendPacket.accept(packet);
	}

	public BlockPos getMidPos() {
		return getMidPos(false);
	}

	public BlockPos getMidPos(boolean zeroY) {
		final BlockPos pos = getPosition(0).add(getPosition(1));
		return new BlockPos(pos.getX() / 2, zeroY ? 0 : pos.getY() / 2, pos.getZ() / 2);
	}

	public boolean isValidPlatform(Set<Rail.RailEntry> rails) {
		final BlockPos pos1 = getPosition(0);
		final BlockPos pos2 = getPosition(1);
		return rails.stream().anyMatch(railEntry -> railEntry.hasConnection(pos1, pos2) && railEntry.connections.get(pos2).railType == Rail.RailType.PLATFORM || railEntry.hasConnection(pos2, pos1) && railEntry.connections.get(pos1).railType == Rail.RailType.PLATFORM);
	}

	public boolean containsPos(BlockPos pos) {
		return positions.contains(pos);
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
}
