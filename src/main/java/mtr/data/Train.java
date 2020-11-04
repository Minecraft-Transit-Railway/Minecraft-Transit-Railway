package mtr.data;

import mtr.entity.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Train extends NamedColoredBase {

	public final TrainType trainType;
	public final List<Long> stationIds;

	public final float[] posX, posY, posZ;
	public final int[] pathIndex;
	public final EntityTrainBase[] entities;
	public float speed;

	public final List<Pos3f> path;

	private final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_STATION_IDS = "station_ids";
	private static final String KEY_TRAIN_LENGTH = "train_length";
	private static final String KEY_POS_X = "pos_x";
	private static final String KEY_POS_Y = "pos_y";
	private static final String KEY_POS_Z = "pos_z";
	private static final String KEY_PATH_INDEX = "path_index";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_PATH_LENGTH = "path_length";
	private static final String KEY_PATH = "path";

	public Train(TrainType trainType, BlockPos pos, int cars, Direction spawnDirection) {
		super();
		this.trainType = trainType;
		posX = new float[cars + 1];
		posY = new float[cars + 1];
		posZ = new float[cars + 1];
		for (int i = 0; i <= cars; i++) {
			final BlockPos carPos = pos.offset(spawnDirection, i * trainType.getSpacing());
			posX[i] = carPos.getX() + 0.5F;
			posY[i] = carPos.getY() + 0.5F;
			posZ[i] = carPos.getZ() + 0.5F;
		}
		pathIndex = new int[cars + 1];
		entities = new EntityTrainBase[cars];
		stationIds = new ArrayList<>();
		path = new ArrayList<>();
	}

	public Train(CompoundTag tag) {
		super(tag);
		trainType = TrainType.values()[tag.getInt(KEY_TRAIN_TYPE)];
		name = trainType.getName();
		color = trainType.color;
		stationIds = new ArrayList<>();
		final long[] stationIdsArray = tag.getLongArray(KEY_STATION_IDS);
		for (final long stationId : stationIdsArray) {
			stationIds.add(stationId);
		}

		final int trainLength = tag.getInt(KEY_TRAIN_LENGTH);
		posX = new float[trainLength];
		posY = new float[trainLength];
		posZ = new float[trainLength];
		pathIndex = new int[trainLength];
		entities = new EntityTrainBase[trainLength - 1];
		for (int i = 0; i < trainLength; i++) {
			posX[i] = tag.getFloat(KEY_POS_X + i);
			posY[i] = tag.getFloat(KEY_POS_Y + i);
			posZ[i] = tag.getFloat(KEY_POS_Z + i);
			pathIndex[i] = tag.getInt(KEY_PATH_INDEX + i);
		}

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
		super(packet);
		trainType = TrainType.values()[packet.readInt()];
		name = trainType.getName();
		color = trainType.color;
		stationIds = new ArrayList<>();
		final int stationCount = packet.readInt();
		for (int i = 0; i < stationCount; i++) {
			stationIds.add(packet.readLong());
		}

		final int trainLength = packet.readInt();
		posX = new float[trainLength];
		posY = new float[trainLength];
		posZ = new float[trainLength];
		pathIndex = new int[trainLength];
		entities = new EntityTrainBase[trainLength - 1];
		for (int i = 0; i < trainLength; i++) {
			posX[i] = packet.readFloat();
			posY[i] = packet.readFloat();
			posZ[i] = packet.readFloat();
			pathIndex[i] = packet.readInt();
		}

		speed = packet.readFloat();

		path = new ArrayList<>();
		final int pathLength = packet.readInt();
		for (int i = 0; i < pathLength; i++) {
			path.add(new Pos3f(packet.readFloat(), packet.readFloat(), packet.readFloat()));
		}
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putInt(KEY_TRAIN_TYPE, trainType.ordinal());
		tag.putLongArray(KEY_STATION_IDS, stationIds);

		final int trainLength = posX.length;
		tag.putInt(KEY_TRAIN_LENGTH, trainLength);
		for (int i = 0; i < trainLength; i++) {
			tag.putFloat(KEY_POS_X + i, posX[i]);
			tag.putFloat(KEY_POS_Y + i, posY[i]);
			tag.putFloat(KEY_POS_Z + i, posZ[i]);
			tag.putInt(KEY_PATH_INDEX + i, pathIndex[i]);
		}

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

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);

		packet.writeInt(trainType.ordinal());
		packet.writeInt(stationIds.size());
		for (final long stationId : stationIds) {
			packet.writeLong(stationId);
		}

		final int trainLength = posX.length;
		packet.writeInt(trainLength);
		for (int i = 0; i < trainLength; i++) {
			packet.writeFloat(posX[i]);
			packet.writeFloat(posY[i]);
			packet.writeFloat(posZ[i]);
			packet.writeInt(pathIndex[i]);
		}

		packet.writeFloat(speed);

		packet.writeInt(path.size());
		path.forEach(pos3f -> {
			packet.writeFloat(pos3f.getX());
			packet.writeFloat(pos3f.getY());
			packet.writeFloat(pos3f.getZ());
		});
	}

	public void resetPathIndex(boolean reverse) {
		final int cars = pathIndex.length;
		for (int i = 0; i < cars; i++) {
			pathIndex[i] = (reverse ? i : (cars - i - 1)) * trainType.spacing;
		}
	}

	@Override
	public String toString() {
		return String.format("Train %s: (%s, %s, %s) %s %f", trainType.name(), Arrays.toString(posX), Arrays.toString(posY), Arrays.toString(posZ), stationIds, speed);
	}

	public enum TrainType {
		MINECART(0x666666, 0.5F, 0.01F, 2, EntityMinecart::new),
		SP1900(0xB42249, 0.5F, 0.01F, 25, EntitySP1900::new),
		M_TRAIN(0x999999, 0.5F, 0.01F, 25, EntityMTrain::new),
		LIGHT_RAIL_1(0xFA831F, 0.5F, 0.01F, 25, EntityLightRail1::new);

		private final int color;
		private final float maxSpeed; // blocks per tick
		private final float acceleration;
		private final int spacing;
		private final EntityTrainFactory entityFactory;

		TrainType(int color, float maxSpeed, float acceleration, int spacing, EntityTrainFactory entityTrainFactory) {
			this.color = color;
			this.maxSpeed = maxSpeed;
			this.acceleration = acceleration;
			this.spacing = spacing;
			entityFactory = entityTrainFactory;
		}

		public String getName() {
			return new TranslatableText("train.mtr." + toString()).getString();
		}

		public int getColor() {
			return color;
		}

		public float getMaxSpeed() {
			return maxSpeed;
		}

		public float getAcceleration() {
			return acceleration;
		}

		public int getSpacing() {
			return spacing;
		}

		public EntityTrainBase create(World world, double x, double y, double z) {
			return entityFactory.create(world, x, y, z);
		}

		private interface EntityTrainFactory {
			EntityTrainBase create(World world, double x, double y, double z);
		}
	}
}
