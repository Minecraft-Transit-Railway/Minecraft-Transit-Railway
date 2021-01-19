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

public final class Train extends DataBase {

	public final TrainType trainType;
	public final List<List<Pos3f>> paths;

	public final float[] posX, posY, posZ;
	public final int[] pathIndex;
	public final EntityTrainBase[] entities;
	public float speed;
	public int stationCoolDown;

	private final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_LENGTH = "train_length";
	private static final String KEY_POS_X = "pos_x";
	private static final String KEY_POS_Y = "pos_y";
	private static final String KEY_POS_Z = "pos_z";
	private static final String KEY_PATH_INDEX = "path_index";
	private static final String KEY_SPEED = "speed";
	private static final String KEY_PATHS_LENGTH = "paths_length";
	private static final String KEY_PATH = "path";
	private static final String KEY_STATION_COOL_DOWN = "station_cool_down";

	public Train(TrainType trainType, BlockPos pos, int cars, Direction spawnDirection) {
		super();
		this.trainType = trainType;
		posX = new float[cars + 1];
		posY = new float[cars + 1];
		posZ = new float[cars + 1];
		for (int i = 0; i <= cars; i++) {
			final BlockPos carPos = pos.offset(spawnDirection, i * trainType.getSpacing());
			posX[i] = carPos.getX() + 0.5F;
			posY[i] = carPos.getY();
			posZ[i] = carPos.getZ() + 0.5F;
		}
		pathIndex = new int[cars + 1];
		entities = new EntityTrainBase[cars];
		paths = new ArrayList<>();
	}

	public Train(CompoundTag tag) {
		super(tag);
		trainType = TrainType.values()[tag.getInt(KEY_TRAIN_TYPE)];
		name = getName();
		color = trainType.color;

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
		stationCoolDown = tag.getInt(KEY_STATION_COOL_DOWN);

		paths = new ArrayList<>();
		final int pathsLength = tag.getInt(KEY_PATHS_LENGTH);
		for (int i = 0; i < pathsLength; i++) {
			final List<Pos3f> path = new ArrayList<>();
			final int pathLength = tag.getInt(String.format("%s_%d", KEY_PATHS_LENGTH, i));
			for (int j = 0; j < pathLength; j++) {
				final float x = tag.getFloat(String.format("%s_%d_%d_X", KEY_PATH, i, j));
				final float y = tag.getFloat(String.format("%s_%d_%d_Y", KEY_PATH, i, j));
				final float z = tag.getFloat(String.format("%s_%d_%d_Z", KEY_PATH, i, j));
				path.add(new Pos3f(x, y, z));
			}
			paths.add(path);
		}
	}

	public Train(PacketByteBuf packet) {
		super(packet);
		trainType = TrainType.values()[packet.readInt()];
		name = getName();
		color = trainType.color;

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
		stationCoolDown = packet.readInt();

		paths = new ArrayList<>();
		final int pathsLength = packet.readInt();
		for (int i = 0; i < pathsLength; i++) {
			final List<Pos3f> path = new ArrayList<>();
			final int pathLength = packet.readInt();
			for (int j = 0; j < pathLength; j++) {
				path.add(new Pos3f(packet.readFloat(), packet.readFloat(), packet.readFloat()));
			}
			paths.add(path);
		}
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putInt(KEY_TRAIN_TYPE, trainType.ordinal());

		final int trainLength = posX.length;
		tag.putInt(KEY_TRAIN_LENGTH, trainLength);
		for (int i = 0; i < trainLength; i++) {
			tag.putFloat(KEY_POS_X + i, posX[i]);
			tag.putFloat(KEY_POS_Y + i, posY[i]);
			tag.putFloat(KEY_POS_Z + i, posZ[i]);
			tag.putInt(KEY_PATH_INDEX + i, pathIndex[i]);
		}

		tag.putFloat(KEY_SPEED, speed);
		tag.putInt(KEY_STATION_COOL_DOWN, stationCoolDown);

		final int pathsLength = paths.size();
		tag.putInt(KEY_PATHS_LENGTH, pathsLength);
		for (int i = 0; i < pathsLength; i++) {
			final List<Pos3f> path = paths.get(i);
			final int pathLength = path.size();
			tag.putInt(String.format("%s_%d", KEY_PATHS_LENGTH, i), pathLength);
			for (int j = 0; j < pathLength; j++) {
				tag.putFloat(String.format("%s_%d_%d_X", KEY_PATH, i, j), path.get(j).getX());
				tag.putFloat(String.format("%s_%d_%d_Y", KEY_PATH, i, j), path.get(j).getY());
				tag.putFloat(String.format("%s_%d_%d_Z", KEY_PATH, i, j), path.get(j).getZ());
			}
		}

		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);

		packet.writeInt(trainType.ordinal());

		final int trainLength = posX.length;
		packet.writeInt(trainLength);
		for (int i = 0; i < trainLength; i++) {
			packet.writeFloat(posX[i]);
			packet.writeFloat(posY[i]);
			packet.writeFloat(posZ[i]);
			packet.writeInt(pathIndex[i]);
		}

		packet.writeFloat(speed);
		packet.writeInt(stationCoolDown);

		packet.writeInt(paths.size());
		paths.forEach(path -> {
			packet.writeInt(path.size());
			path.forEach(pos3f -> {
				packet.writeFloat(pos3f.getX());
				packet.writeFloat(pos3f.getY());
				packet.writeFloat(pos3f.getZ());
			});
		});
	}

	public void spaceOut() {
		final int trainLength = posX.length;
		final int startIndex = trainLength / 2;
		for (int i = startIndex; i > 0; i--) {
			spaceCar(i, false);
		}
		for (int i = startIndex; i < trainLength - 1; i++) {
			spaceCar(i, true);
		}
	}

	private void spaceCar(int index, boolean forwards) {
		final int indexNext = index + (forwards ? 1 : -1);
		final Pos3f posMove = new Pos3f(posX[indexNext] - posX[index], posY[indexNext] - posY[index], posZ[indexNext] - posZ[index]);

		posMove.normalize();
		posMove.scale(trainType.getSpacing());

		final float changeX = posMove.getX() + posX[index] - posX[indexNext];
		final float changeY = posMove.getY() + posY[index] - posY[indexNext];
		final float changeZ = posMove.getZ() + posZ[index] - posZ[indexNext];

		int i = indexNext;
		while (i >= 0 && i < posX.length) {
			posX[i] += changeX;
			posY[i] += changeY;
			posZ[i] += changeZ;
			i = i + (forwards ? 1 : -1);
		}
	}

	private String getName() {
		return trainType.getName() + " " + id;
	}

	@Override
	public String toString() {
		return String.format("Train %s: (%s, %s, %s) %f", getName(), Arrays.toString(posX), Arrays.toString(posY), Arrays.toString(posZ), speed);
	}

	public enum TrainType {
		MINECART(0x666666, 0.5F, 0.01F, 1, 1, 1, EntityMinecart::new),
		SP1900(0xB42249, 0.9F, 0.01F, 24, 2, 50, EntitySP1900::new),
		SP1900_MINI(0xB42249, 0.9F, 0.01F, 12, 2, 20, EntitySP1900Mini::new),
		M_TRAIN(0x999999, 0.9F, 0.01F, 24, 2, 50, EntityMTrain::new),
		M_TRAIN_MINI(0x999999, 0.9F, 0.01F, 9, 2, 20, EntityMTrainMini::new);
		// TODO add light rail
		// LIGHT_RAIL_1(0xFA831F, 0.5F, 0.01F, 24, 2, 50, EntityLightRail1::new);

		private final int color;
		private final float maxSpeed; // blocks per tick
		private final float acceleration;
		private final int length;
		private final int width;
		private final int capacity;
		private final EntityTrainFactory entityFactory;

		TrainType(int color, float maxSpeed, float acceleration, int length, int width, int capacity, EntityTrainFactory entityTrainFactory) {
			this.color = color;
			this.maxSpeed = maxSpeed;
			this.acceleration = acceleration;
			this.length = length;
			this.width = width;
			this.capacity = capacity;
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

		public int getLength() {
			return length;
		}

		public int getWidth() {
			return width;
		}

		public int getSpacing() {
			return length + 1;
		}

		public int getCapacity() {
			return capacity;
		}

		public EntityTrainBase create(World world, double x, double y, double z) {
			return entityFactory.create(world, x, y, z);
		}

		private interface EntityTrainFactory {
			EntityTrainBase create(World world, double x, double y, double z);
		}
	}
}
