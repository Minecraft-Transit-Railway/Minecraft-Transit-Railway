package mtr.path;

import mtr.data.MessagePackHelper;
import mtr.data.Rail;
import mtr.data.RailwayData;
import mtr.data.SerializedDataBase;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class PathData extends SerializedDataBase {

	public final Rail rail;
	public final long savedRailBaseId;
	public final int dwellTime;
	public final int stopIndex;

	public final BlockPos startingPos;
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

	public PathData(Map<String, Value> map) {
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
		rail = new Rail(RailwayData.castMessagePackValueToSKMap(map.get(KEY_RAIL)));
		savedRailBaseId = messagePackHelper.getLong(KEY_SAVED_RAIL_BASE_ID);
		dwellTime = messagePackHelper.getInt(KEY_DWELL_TIME);
		stopIndex = messagePackHelper.getInt(KEY_STOP_INDEX);
		startingPos = BlockPos.of(messagePackHelper.getLong(KEY_STARTING_POS));
		endingPos = BlockPos.of(messagePackHelper.getLong(KEY_ENDING_POS));
	}

	@Deprecated
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
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		messagePacker.packString(KEY_RAIL);
		messagePacker.packMapHeader(rail.messagePackLength());
		rail.toMessagePack(messagePacker);

		messagePacker.packString(KEY_SAVED_RAIL_BASE_ID).packLong(savedRailBaseId);
		messagePacker.packString(KEY_DWELL_TIME).packInt(dwellTime);
		messagePacker.packString(KEY_STOP_INDEX).packInt(stopIndex);
		messagePacker.packString(KEY_STARTING_POS).packLong(startingPos.asLong());
		messagePacker.packString(KEY_ENDING_POS).packLong(endingPos.asLong());
	}

	@Override
	public int messagePackLength() {
		return 6;
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
