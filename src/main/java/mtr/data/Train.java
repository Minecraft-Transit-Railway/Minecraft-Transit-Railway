package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public final class Train {

	public final TrainType trainType;
	public final List<Long> stationIds;

	public float posX, posY, posZ, yaw, speed;

	public final List<Pos3f> path;

	private final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_STATION_IDS = "station_ids";
	private static final String KEY_POS_X = "pos_x";
	private static final String KEY_POS_Y = "pos_y";
	private static final String KEY_POS_Z = "pos_z";
	private static final String KEY_YAW = "yaw";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_PATH_LENGTH = "path_length";
	private static final String KEY_PATH = "path";

	public Train(TrainType trainType, BlockPos pos) {
		this.trainType = trainType;
		posX = pos.getX() + 0.5F;
		posY = pos.getY() + 0.5F;
		posZ = pos.getZ() + 0.5F;
		stationIds = new ArrayList<>();
		path = new ArrayList<>();
	}

	public Train(CompoundTag tag) {
		trainType = TrainType.values()[tag.getInt(KEY_TRAIN_TYPE)];
		stationIds = new ArrayList<>();
		final long[] stationIdsArray = tag.getLongArray(KEY_STATION_IDS);
		for (final long stationId : stationIdsArray) {
			stationIds.add(stationId);
		}

		posX = tag.getFloat(KEY_POS_X);
		posY = tag.getFloat(KEY_POS_Y);
		posZ = tag.getFloat(KEY_POS_Z);
		yaw = tag.getFloat(KEY_YAW);
		speed = tag.getFloat(KEY_SPEED);

		path = new ArrayList<>();
		final int pathLength = tag.getInt(KEY_PATH_LENGTH);
		for (int i = 0; i < pathLength; i++) {
			final float x = tag.getFloat(String.format("%s_%d_X", KEY_PATH, i));
			final float y = tag.getFloat(String.format("%s_%d_Y", KEY_PATH, i));
			final float z = tag.getFloat(String.format("%s_%d_Z", KEY_PATH, i));
			path.add(new Pos3f(x, y, z));
		}
	}

	public Train(PacketByteBuf packet) {
		trainType = TrainType.values()[packet.readInt()];
		stationIds = new ArrayList<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			stationIds.add(packet.readLong());
		}

		posX = packet.readFloat();
		posY = packet.readFloat();
		posZ = packet.readFloat();
		yaw = packet.readFloat();
		speed = packet.readFloat();

		path = new ArrayList<>();
		final int pathLength = packet.readInt();
		for (int i = 0; i < pathLength; i++) {
			path.add(new Pos3f(packet.readFloat(), packet.readFloat(), packet.readFloat()));
		}
	}

	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.putInt(KEY_TRAIN_TYPE, trainType.ordinal());
		tag.putLongArray(KEY_STATION_IDS, stationIds);

		tag.putFloat(KEY_POS_X, posX);
		tag.putFloat(KEY_POS_Y, posY);
		tag.putFloat(KEY_POS_Z, posZ);
		tag.putFloat(KEY_YAW, yaw);
		tag.putFloat(KEY_SPEED, speed);

		final int pathLength = path.size();
		tag.putInt(KEY_PATH_LENGTH, pathLength);
		for (int i = 0; i < pathLength; i++) {
			tag.putFloat(String.format("%s_%d_X", KEY_PATH, i), path.get(i).getX());
			tag.putFloat(String.format("%s_%d_Y", KEY_PATH, i), path.get(i).getY());
			tag.putFloat(String.format("%s_%d_Z", KEY_PATH, i), path.get(i).getZ());
		}

		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeInt(trainType.ordinal());
		packet.writeInt(stationIds.size());
		for (final long stationId : stationIds) {
			packet.writeLong(stationId);
		}

		packet.writeFloat(posX);
		packet.writeFloat(posY);
		packet.writeFloat(posZ);
		packet.writeFloat(yaw);
		packet.writeFloat(speed);

		packet.writeInt(path.size());
		path.forEach(pos3f -> {
			packet.writeFloat(pos3f.getX());
			packet.writeFloat(pos3f.getY());
			packet.writeFloat(pos3f.getZ());
		});
	}

	@Override
	public String toString() {
		return String.format("Train %s: (%f, %f, %f) %s %f", trainType.name(), posX, posY, posZ, stationIds, speed);
	}

	public enum TrainType {
		SP1900(0.5F, 0.01F),
		M_TRAIN(0.5F, 0.01F),
		LIGHT_RAIL_1(0.5F, 0.01F);

		// blocks per tick
		private final float maxSpeed;
		private final float acceleration;

		TrainType(float maxSpeed, float acceleration) {
			this.maxSpeed = maxSpeed;
			this.acceleration = acceleration;
		}

		public float getMaxSpeed() {
			return maxSpeed;
		}

		public float getAcceleration() {
			return acceleration;
		}
	}
}
