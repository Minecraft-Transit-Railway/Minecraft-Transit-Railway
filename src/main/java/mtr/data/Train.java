package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class Train {

	public final TrainType trainType;
	public final List<Long> stationIds;

	public double posX, posY, posZ, yaw, speed;

	public final List<BlockPos> path;

	private final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_STATION_IDS = "station_ids";
	private static final String KEY_POS_X = "pos_x";
	private static final String KEY_POS_Y = "pos_y";
	private static final String KEY_POS_Z = "pos_z";
	private static final String KEY_YAW = "yaw";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_PATH = "path";

	public Train(TrainType trainType, BlockPos pos) {
		this.trainType = trainType;
		posX = pos.getX() + 0.5;
		posY = pos.getY() + 0.5;
		posZ = pos.getZ() + 0.5;
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

		posX = tag.getDouble(KEY_POS_X);
		posY = tag.getDouble(KEY_POS_Y);
		posZ = tag.getDouble(KEY_POS_Z);
		yaw = tag.getDouble(KEY_YAW);
		speed = tag.getDouble(KEY_SPEED);

		path = new ArrayList<>();
		final long[] pathArray = tag.getLongArray(KEY_PATH);
		for (final long blockPosLong : pathArray) {
			path.add(BlockPos.fromLong(blockPosLong));
		}
	}

	public Train(PacketByteBuf packet) {
		trainType = TrainType.values()[packet.readInt()];
		stationIds = new ArrayList<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			stationIds.add(packet.readLong());
		}

		posX = packet.readDouble();
		posY = packet.readDouble();
		posZ = packet.readDouble();
		yaw = packet.readDouble();
		speed = packet.readDouble();

		path = new ArrayList<>();
		final int pathLength = packet.readInt();
		for (int i = 0; i < pathLength; i++) {
			path.add(packet.readBlockPos());
		}
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

		tag.putLongArray(KEY_PATH, path.stream().map(BlockPos::asLong).collect(Collectors.toList()));

		return tag;
	}

	public void writePacket(PacketByteBuf packet) {
		packet.writeInt(trainType.ordinal());
		packet.writeInt(stationIds.size());
		for (final long stationId : stationIds) {
			packet.writeLong(stationId);
		}

		packet.writeDouble(posX);
		packet.writeDouble(posY);
		packet.writeDouble(posZ);
		packet.writeDouble(yaw);
		packet.writeDouble(speed);

		packet.writeInt(path.size());
		for (final BlockPos blockPos : path) {
			packet.writeBlockPos(blockPos);
		}
	}

	@Override
	public String toString() {
		return String.format("Train %s: (%f, %f, %f) %s %f", trainType.name(), posX, posY, posZ, stationIds, speed);
	}

	public enum TrainType {
		SP1900(0.5F, 0.1F),
		M_TRAIN(0.5F, 0.1F),
		LIGHT_RAIL_1(0.5F, 0.1F);

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
