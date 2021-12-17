package mtr.path;

import mtr.data.Rail;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

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

	public PathData(CompoundTag compoundTag) {
		rail = new Rail(compoundTag.getCompound(KEY_RAIL));
		savedRailBaseId = compoundTag.getLong(KEY_SAVED_RAIL_BASE_ID);
		dwellTime = compoundTag.getInt(KEY_DWELL_TIME);
		stopIndex = compoundTag.getInt(KEY_STOP_INDEX);
		startingPos = BlockPos.of(compoundTag.getLong(KEY_STARTING_POS));
		endingPos = BlockPos.of(compoundTag.getLong(KEY_ENDING_POS));
	}

	public PathData(FriendlyByteBuf packet) {
		rail = new Rail(packet);
		savedRailBaseId = packet.readLong();
		dwellTime = packet.readInt();
		stopIndex = packet.readInt();
		startingPos = BlockPos.of(packet.readLong());
		endingPos = BlockPos.of(packet.readLong());
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag compoundTag = new CompoundTag();
		compoundTag.put(KEY_RAIL, rail.toCompoundTag());
		compoundTag.putLong(KEY_SAVED_RAIL_BASE_ID, savedRailBaseId);
		compoundTag.putInt(KEY_DWELL_TIME, dwellTime);
		compoundTag.putInt(KEY_STOP_INDEX, stopIndex);
		compoundTag.putLong(KEY_STARTING_POS, startingPos.asLong());
		compoundTag.putLong(KEY_ENDING_POS, endingPos.asLong());
		return compoundTag;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
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
		return getRailProduct(startingPos, endingPos);
	}

	public static UUID getRailProduct(BlockPos startingPos, BlockPos endingPos) {
		final long startingPosLong = startingPos.asLong();
		final long endingPosLong = endingPos.asLong();
		return new UUID(Math.min(startingPosLong, endingPosLong), Math.max(startingPosLong, endingPosLong));
	}
}
