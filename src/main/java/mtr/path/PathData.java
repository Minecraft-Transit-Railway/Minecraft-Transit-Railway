package mtr.path;

import mtr.data.Rail;
import mtr.data.RailwayData;
import mtr.data.SerializedDataBase;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.Map;
import java.util.UUID;

public class PathData extends SerializedDataBase {

	public final Rail rail;
	public final long savedRailBaseId;
	public final int dwellTime;
	public final int stopIndex;

	private final BlockPos startingPos;
	private final BlockPos endingPos;

	private static final String KEY_RAIL = "rail";
	private static final String KEY_SAVED_RAIL_BASE_ID = "saved_rail_base_id";
	private static final String KEY_DWELL_TIME = "dwell_time";
	private static final String KEY_STOP_INDEX = "stop_index";
	private static final String KEY_STARTING_POS = "starting_pos";
	private static final String KEY_ENDING_POS = "ending_pos";

	public PathData(Rail rail, long savedRailBaseId, int dwellTime, BlockPos startingPos, BlockPos endingPos, int stopIndex) {
		this.rail = rail;
		this.savedRailBaseId = savedRailBaseId;
		this.dwellTime = dwellTime;
		this.startingPos = startingPos;
		this.endingPos = endingPos;
		this.stopIndex = stopIndex;
	}

	public PathData(NbtCompound nbtCompound) {
		rail = new Rail(nbtCompound.getCompound(KEY_RAIL));
		savedRailBaseId = nbtCompound.getLong(KEY_SAVED_RAIL_BASE_ID);
		dwellTime = nbtCompound.getInt(KEY_DWELL_TIME);
		stopIndex = nbtCompound.getInt(KEY_STOP_INDEX);
		startingPos = BlockPos.fromLong(nbtCompound.getLong(KEY_STARTING_POS));
		endingPos = BlockPos.fromLong(nbtCompound.getLong(KEY_ENDING_POS));
	}

	public PathData(PacketByteBuf packet) {
		rail = new Rail(packet);
		savedRailBaseId = packet.readLong();
		dwellTime = packet.readInt();
		stopIndex = packet.readInt();
		startingPos = BlockPos.fromLong(packet.readLong());
		endingPos = BlockPos.fromLong(packet.readLong());
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.put(KEY_RAIL, rail.toCompoundTag());
		nbtCompound.putLong(KEY_SAVED_RAIL_BASE_ID, savedRailBaseId);
		nbtCompound.putInt(KEY_DWELL_TIME, dwellTime);
		nbtCompound.putInt(KEY_STOP_INDEX, stopIndex);
		nbtCompound.putLong(KEY_STARTING_POS, startingPos.asLong());
		nbtCompound.putLong(KEY_ENDING_POS, endingPos.asLong());
		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		rail.writePacket(packet);
		packet.writeLong(savedRailBaseId);
		packet.writeInt(dwellTime);
		packet.writeInt(stopIndex);
		packet.writeLong(startingPos.asLong());
		packet.writeLong(endingPos.asLong());
	}

	public boolean isSameRail(PathData pathData) {
		return startingPos.equals(pathData.startingPos) && endingPos.equals(pathData.endingPos);
	}

	public boolean isOppositeRail(PathData pathData) {
		return startingPos.equals(pathData.endingPos) && endingPos.equals(pathData.startingPos);
	}

	public UUID getRailProduct() {
		final long startingPosLong = startingPos.asLong();
		final long endingPosLong = endingPos.asLong();
		return new UUID(Math.min(startingPosLong, endingPosLong), Math.max(startingPosLong, endingPosLong));
	}

	public Rail getOppositeRail(Map<BlockPos, Map<BlockPos, Rail>> rails) {
		return RailwayData.containsRail(rails, endingPos, startingPos) ? rails.get(endingPos).get(startingPos) : null;
	}
}
