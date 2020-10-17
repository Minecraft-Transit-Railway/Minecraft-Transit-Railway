package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

import java.util.ArrayList;
import java.util.List;

public final class Train {

	public enum TrainType {SP1900, M_TRAIN, LIGHT_RAIL_1}

	public final TrainType trainType;
	public final List<Long> stationIds;

	public double posX, posY, posZ, yaw, speed;

	private final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_STATION_IDS = "station_ids";
	private static final String KEY_POS_X = "pos_x";
	private static final String KEY_POS_Y = "pos_y";
	private static final String KEY_POS_Z = "pos_z";
	private static final String KEY_YAW = "yaw";
	private static final String KEY_SPEED = "speed";

	public Train(TrainType trainType) {
		this.trainType = trainType;
		stationIds = new ArrayList<>();
	}

	public Train(CompoundTag tag) {
		trainType = TrainType.values()[tag.getInt(KEY_TRAIN_TYPE)];
		stationIds = new ArrayList<>();
		long[] stationIdsArray = tag.getLongArray(KEY_STATION_IDS);
		for (long stationId : stationIdsArray) {
			stationIds.add(stationId);
		}

		posX = tag.getDouble(KEY_POS_X);
		posY = tag.getDouble(KEY_POS_Y);
		posZ = tag.getDouble(KEY_POS_Z);
		yaw = tag.getDouble(KEY_YAW);
		speed = tag.getDouble(KEY_SPEED);
	}

	public Train(PacketByteBuf packet) {
		trainType = TrainType.values()[packet.readInt()];
		stationIds = new ArrayList<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			stationIds.add(packet.readLong());
		}

		posX = packet.readLong();
		posY = packet.readLong();
		posZ = packet.readLong();
		yaw = packet.readLong();
		speed = packet.readLong();
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putInt(KEY_TRAIN_TYPE, trainType.ordinal());
		tag.putLongArray(KEY_STATION_IDS, stationIds);

		tag.putDouble(KEY_POS_X, posX);
		tag.putDouble(KEY_POS_Y, posY);
		tag.putDouble(KEY_POS_Z, posZ);
		tag.putDouble(KEY_YAW, yaw);
		tag.putDouble(KEY_SPEED, speed);

		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeInt(trainType.ordinal());
		packet.writeInt(stationIds.size());
		for (long stationId : stationIds) {
			packet.writeLong(stationId);
		}

		packet.writeDouble(posX);
		packet.writeDouble(posY);
		packet.writeDouble(posZ);
		packet.writeDouble(yaw);
		packet.writeDouble(speed);
	}
}
