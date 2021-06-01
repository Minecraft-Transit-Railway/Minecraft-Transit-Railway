package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
import mtr.config.CustomResources;
import mtr.gui.IGui;
import mtr.path.PathData;
import mtr.path.PathData2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Siding extends SavedRailBase implements IGui {

	private Depot depot;
	private Rail sidingRail;
	private CustomResources.TrainMapping trainTypeMapping;
	private int trainLength;

	private float speed;
	private float railProgress;
	private float stopCounter;
	private int nextStoppingIndex;
	private long lastNanos;
	private TrainState trainState = TrainState.AT_SIDING;

	private final int railLength;
	private final List<PathData2> path = new ArrayList<>();
	private final List<Float> distances = new ArrayList<>();

	private static final String KEY_RAIL_LENGTH = "rail_length";
	private static final String KEY_TRAIN_TYPE = "train_type";
	private static final String KEY_TRAIN_CUSTOM_ID = "train_custom_id";

	private static final int TICK_DURATION_NANOS = 50000000;
	private static final float ACCELERATION = 0.01F;
	private static final int DOOR_DELAY = 20;
	private static final int DOOR_MOVE_TIME = 64;
	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public Siding(BlockPos pos1, BlockPos pos2, int railLength) {
		super(pos1, pos2);
		this.railLength = railLength;
		trainTypeMapping = new CustomResources.TrainMapping("", TrainType.values()[0]);

		trainLength = railLength / trainTypeMapping.trainType.getSpacing();
		lastNanos = System.nanoTime();
	}

	public Siding(CompoundTag tag) {
		super(tag);
		railLength = tag.getInt(KEY_RAIL_LENGTH);
		TrainType trainType = TrainType.values()[0];
		try {
			trainType = TrainType.valueOf(tag.getString(KEY_TRAIN_TYPE));
		} catch (Exception ignored) {
		}
		trainTypeMapping = new CustomResources.TrainMapping(tag.getString(KEY_TRAIN_CUSTOM_ID), trainType);

		trainLength = railLength / trainTypeMapping.trainType.getSpacing();
		lastNanos = System.nanoTime();
	}

	public Siding(PacketByteBuf packet) {
		super(packet);
		railLength = packet.readInt();
		trainTypeMapping = new CustomResources.TrainMapping(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);

		trainLength = railLength / trainTypeMapping.trainType.getSpacing();
		lastNanos = System.nanoTime();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = super.toCompoundTag();
		tag.putInt(KEY_RAIL_LENGTH, railLength);
		tag.putString(KEY_TRAIN_CUSTOM_ID, trainTypeMapping.customId);
		tag.putString(KEY_TRAIN_TYPE, trainTypeMapping.trainType.toString());
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(railLength);
		packet.writeString(trainTypeMapping.customId);
		packet.writeInt(trainTypeMapping.trainType.ordinal());
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		switch (key) {
			case KEY_TRAIN_TYPE:
				trainTypeMapping = new CustomResources.TrainMapping(packet.readString(PACKET_STRING_READ_LENGTH), TrainType.values()[packet.readInt()]);
				trainLength = railLength / trainTypeMapping.trainType.getSpacing();
				break;
			default:
				super.update(key, packet);
				break;
		}
	}

	public void setPath(List<PathData2> path, Depot depot) {
		this.path.clear();
		this.path.addAll(path);

		distances.clear();
		float sum = 0;
		for (final PathData2 pathData : path) {
			sum += pathData.rail.getLength();
			distances.add(sum);
		}

		this.depot = depot;
	}

	public void simulateTrain(WorldAccess world, Map<BlockPos, Map<BlockPos, Rail>> rails, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback) {
		try {
			final long currentNanos = System.nanoTime();

			if (trainState == TrainState.AT_SIDING) {
				if (sidingRail == null) {
					try {
						final List<BlockPos> railPositions = getOrderedPositions(new BlockPos(0, 0, 0), false);
						sidingRail = rails.get(railPositions.get(0)).get(railPositions.get(1));
					} catch (Exception ignored) {
					}
				}

				if (sidingRail != null) {
					final Pos3f[] positions = new Pos3f[trainLength + 1];
					for (int i = 0; i < trainLength + 1; i++) {
						positions[i] = sidingRail.getPosition(railLength - i * trainTypeMapping.trainType.getSpacing());
					}
					render(world, positions, 0, renderTrainCallback, renderConnectionCallback);
				}

				if (!path.isEmpty() && depot.deployTrain(world)) {
					System.out.println("deploying train");
					trainState = TrainState.ON_ROUTE;
					speed = ACCELERATION;
					final PathData2 pathData = path.get(0);
					if (pathData.rail.equals(sidingRail)) {
						railProgress = distances.get(0);
					} else {
						railProgress = trainLength * trainTypeMapping.trainType.getSpacing();
					}
				}
			} else {
				final Pos3f[] positions = new Pos3f[trainLength + 1];
				for (int i = 0; i < trainLength + 1; i++) {
					positions[i] = getRoutePosition(i);
				}
				render(world, positions, moveTrain(world.isClient() ? (float) (currentNanos - lastNanos) / TICK_DURATION_NANOS : 1), renderTrainCallback, renderConnectionCallback);
			}

			lastNanos = currentNanos;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private float moveTrain(float ticksElapsed) {
		final float newAcceleration = ACCELERATION * ticksElapsed;
		if (path.isEmpty()) {
			return 0;
		}

		if (railProgress >= distances.get(distances.size() - 1)) {
			System.out.println("back at siding");
			trainState = TrainState.AT_SIDING;
			return 0;
		} else {
			final float doorValue;

			if (speed <= 0) {
				speed = 0;
				stopCounter += ticksElapsed;

				final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;
				doorValue = (stopCounter < dwellTicks / 2F ? 1 : -1) * getDoorValue(dwellTicks, stopCounter);

				if (stopCounter > dwellTicks) {
					stopCounter = 0;
					speed = newAcceleration;
					if (path.size() > nextStoppingIndex + 1 && path.get(nextStoppingIndex).isOpposite(path.get(nextStoppingIndex + 1))) {
						railProgress += trainLength * trainTypeMapping.trainType.getSpacing();
					}
					nextStoppingIndex = getNextStoppingIndex();
				}
			} else {
				if (distances.get(nextStoppingIndex) - railProgress < 0.5 * speed * speed / ACCELERATION) {
					speed -= newAcceleration;
				} else {
					final int headIndex = getIndex(0);
					final float railSpeed = path.get(headIndex).rail.railType.maxBlocksPerTick;
					if (speed < railSpeed && (speed < RailType.WOODEN.maxBlocksPerTick || headIndex < nextStoppingIndex)) {
						speed += newAcceleration;
					} else if (speed > railSpeed) {
						speed -= newAcceleration;
					}
				}

				doorValue = 0;
			}

			railProgress += speed * ticksElapsed;
			return doorValue;
		}
	}

	private void render(WorldAccess world, Pos3f[] positions, float doorValueRaw, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback) {
		final float doorValue = Math.abs(doorValueRaw);
		final boolean opening = doorValueRaw > 0;

		float prevCarX = 0, prevCarY = 0, prevCarZ = 0, prevCarYaw = 0, prevCarPitch = 0;
		int previousRendered = 0;
		for (int i = 0; i < trainLength; i++) {
			final Pos3f pos1 = positions[i];
			final Pos3f pos2 = positions[i + 1];

			if (pos1 != null && pos2 != null) {
				final float x = getAverage(pos1.x, pos2.x);
				final float y = getAverage(pos1.y, pos2.y) + 1;
				final float z = getAverage(pos1.z, pos2.z);

				final TrainType trainType = trainTypeMapping.trainType;
				final float realSpacing = pos2.getDistanceTo(pos1);
				final float halfSpacing = realSpacing / 2;
				final float halfWidth = trainType.width / 2F;
				final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
				final float pitch = realSpacing == 0 ? 0 : (float) Math.asin((pos2.y - pos1.y) / realSpacing);

				final boolean doorLeftOpen = openDoors(world, x, y, z, (float) Math.PI + yaw, halfSpacing, doorValue) && doorValue > 0;
				final boolean doorRightOpen = openDoors(world, x, y, z, yaw, halfSpacing, doorValue) && doorValue > 0;

				if (renderTrainCallback != null) {
					renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, "", trainType, i == 0, i == trainLength - 1, doorLeftOpen ? doorValue : 0, doorRightOpen ? doorValue : 0, opening, false);
				}

				previousRendered--;
				if (renderConnectionCallback != null && i > 0 && trainType.shouldRenderConnection && previousRendered > 0) {
					final float xStart = halfWidth - CONNECTION_X_OFFSET;
					final float zStart = trainType.getSpacing() / 2F - CONNECTION_Z_OFFSET;

					final Pos3f prevPos1 = new Pos3f(xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
					final Pos3f prevPos2 = new Pos3f(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
					final Pos3f prevPos3 = new Pos3f(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);
					final Pos3f prevPos4 = new Pos3f(-xStart, SMALL_OFFSET, zStart).rotateX(prevCarPitch).rotateY(prevCarYaw).add(prevCarX, prevCarY, prevCarZ);

					final Pos3f thisPos1 = new Pos3f(-xStart, SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);
					final Pos3f thisPos2 = new Pos3f(-xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);
					final Pos3f thisPos3 = new Pos3f(xStart, CONNECTION_HEIGHT + SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);
					final Pos3f thisPos4 = new Pos3f(xStart, SMALL_OFFSET, -zStart).rotateX(pitch).rotateY(yaw).add(x, y, z);

					renderConnectionCallback.renderConnectionCallback(prevPos1, prevPos2, prevPos3, prevPos4, thisPos1, thisPos2, thisPos3, thisPos4, x, y, z, trainType, false);
				}

				prevCarX = x;
				prevCarY = y;
				prevCarZ = z;
				prevCarYaw = yaw;
				prevCarPitch = pitch;
				previousRendered = 2;
			}
		}
	}

	private float getRailProgress(int car) {
		return railProgress - car * trainTypeMapping.trainType.getSpacing();
	}

	private int getIndex(int car) {
		for (int i = 0; i < path.size(); i++) {
			if (getRailProgress(car) < distances.get(i)) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private Pos3f getRoutePosition(int car) {
		final int index = getIndex(car);
		return path.get(index).rail.getPosition(getRailProgress(car) - (index == 0 ? 0 : distances.get(index - 1)));
	}

	private int getNextStoppingIndex() {
		final int headIndex = getIndex(0);
		for (int i = headIndex; i < path.size(); i++) {
			if (path.get(i).dwellTime > 0) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private static float getDoorValue(int dwellTicks, float value) {
		final float maxDoorMoveTime = Math.min(DOOR_MOVE_TIME, dwellTicks / 2 - DOOR_DELAY);
		final float stage1 = DOOR_DELAY;
		final float stage2 = DOOR_DELAY + maxDoorMoveTime;
		final float stage3 = dwellTicks - DOOR_DELAY - maxDoorMoveTime;
		final float stage4 = dwellTicks - DOOR_DELAY;
		if (value < stage1 || value >= stage4) {
			return 0;
		} else if (value >= stage2 && value < stage3) {
			return 1;
		} else if (value >= stage1 && value < stage2) {
			return (value - stage1) / DOOR_MOVE_TIME;
		} else if (value >= stage3 && value < stage4) {
			return (stage4 - value) / DOOR_MOVE_TIME;
		} else {
			return 0;
		}
	}

	private static boolean openDoors(WorldAccess world, float trainX, float trainY, float trainZ, float checkYaw, float halfSpacing, float doorValue) {
		boolean hasPlatform = false;
		final Vec3d offsetVec = new Vec3d(1, 0, 0).rotateY(checkYaw);
		final Vec3d traverseVec = new Vec3d(0, 0, 1).rotateY(checkYaw);

		for (int checkX = 1; checkX <= 3; checkX++) {
			for (int checkY = -1; checkY <= 0; checkY++) {
				for (float checkZ = -halfSpacing; checkZ <= halfSpacing; checkZ++) {
					final BlockPos checkPos = new BlockPos(trainX + offsetVec.x * checkX + traverseVec.x * checkZ, trainY + checkY, trainZ + offsetVec.z * checkX + traverseVec.z * checkZ);
					final Block block = world.getBlockState(checkPos).getBlock();

					if (block instanceof BlockPlatform || block instanceof BlockPSDAPGBase) {
						if (world.isClient()) {
							return true;
						} else if (block instanceof BlockPSDAPGDoorBase) {
							for (int i = -1; i <= 1; i++) {
								final BlockState state = world.getBlockState(checkPos.up(i));
								if (state.getBlock() instanceof BlockPSDAPGDoorBase) {
									((World) world).setBlockState(checkPos.up(i), state.with(BlockPSDAPGDoorBase.OPEN, (int) MathHelper.clamp(doorValue * PathData.DOOR_MOVE_TIME, 0, BlockPSDAPGDoorBase.MAX_OPEN_VALUE)));
								}
							}
						}

						hasPlatform = true;
					}
				}
			}
		}

		return hasPlatform;
	}

	private static float getAverage(float a, float b) {
		return (a + b) / 2;
	}

	private enum TrainState {AT_SIDING, ON_ROUTE}

	@FunctionalInterface
	public interface RenderTrainCallback {
		void renderTrainCallback(float x, float y, float z, float yaw, float pitch, String customId, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, float doorLeftValue, float doorRightValue, boolean opening, boolean shouldOffsetRender);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, TrainType trainType, boolean shouldOffsetRender);
	}
}
