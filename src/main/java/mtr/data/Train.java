package mtr.data;

import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGDoorBase;
import mtr.block.BlockPlatform;
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

import java.util.List;

public class Train extends SerializedDataBase implements IGui {

	public float speed;

	private float railProgress;
	private float stopTime;
	private int nextStoppingIndex;
	private long lastNanos;

	public final TrainType trainType;
	public final int trainLength;
	public final long scheduledStartTime;

	private final List<PathData2> path;

	private static final int TICK_DURATION_NANOS = 50000000;
	private static final float ACCELERATION = 0.01F;
	private static final int DOOR_DELAY = 20;
	private static final int DOOR_MOVE_TIME = 64;
	private static final float CONNECTION_HEIGHT = 2.25F;
	private static final float CONNECTION_Z_OFFSET = 0.5F;
	private static final float CONNECTION_X_OFFSET = 0.25F;

	public Train(TrainType trainType, int trainLength, long scheduledStartTime, List<PathData2> path) {
		this.trainType = trainType;
		this.trainLength = trainLength;
		this.scheduledStartTime = scheduledStartTime;
		this.path = path;
		speed = ACCELERATION;
		lastNanos = System.nanoTime();
	}

	@Override
	public CompoundTag toCompoundTag() {
		return null;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {

	}

	public void move(WorldAccess world, RenderTrainCallback renderTrainCallback, RenderConnectionCallback renderConnectionCallback) {
		if (path.isEmpty()) {
			return;
		}

		final long currentNanos = System.nanoTime();
		final float ticksElapsed = renderTrainCallback == null ? 1 : (float) (currentNanos - lastNanos) / TICK_DURATION_NANOS;
		final float newAcceleration = ACCELERATION * ticksElapsed;
		final float doorValue;
		final boolean opening;

		if (currentNanos < scheduledStartTime) {
			railProgress = 0;
			doorValue = 0;
			opening = false;
		} else {
			if (speed <= 0) {
				speed = 0;
				stopTime += ticksElapsed;

				final int dwellTicks = path.get(nextStoppingIndex).dwellTime * 10;
				doorValue = getDoorValue(dwellTicks, stopTime);
				opening = stopTime < dwellTicks / 2F;
				if (stopTime > dwellTicks) {
					stopTime = 0;
					speed = newAcceleration;
					nextStoppingIndex = getNextStoppingIndex();
				}
			} else if (path.get(nextStoppingIndex).distance - railProgress < 0.5 * speed * speed / ACCELERATION) {
				speed -= newAcceleration;
				doorValue = 0;
				opening = false;
			} else {
				final int headIndex = getIndex(0);
				final float railSpeed = path.get(headIndex).rail.railType.maxBlocksPerTick;
				if (speed < railSpeed && (speed < RailType.WOODEN.maxBlocksPerTick || headIndex < nextStoppingIndex)) {
					speed += newAcceleration;
				} else if (speed > railSpeed) {
					speed -= newAcceleration;
				}
				doorValue = 0;
				opening = false;
			}
			railProgress += speed * ticksElapsed;

			if (railProgress > path.get(path.size() - 1).distance) {
				railProgress = 0;
				speed = 0;
			}
		}

		final Pos3f[] positions = new Pos3f[trainLength + 1];
		for (int i = 0; i < trainLength + 1; i++) {
			positions[i] = getPosition(i);
		}

		float prevCarX = 0, prevCarY = 0, prevCarZ = 0, prevCarYaw = 0, prevCarPitch = 0;
		int previousRendered = 0;
		for (int i = 0; i < trainLength; i++) {
			final Pos3f pos1 = positions[i];
			final Pos3f pos2 = positions[i + 1];

			if (pos1 != null && pos2 != null) {
				final float x = getAverage(pos1.x, pos2.x);
				final float y = getAverage(pos1.y, pos2.y) + 1;
				final float z = getAverage(pos1.z, pos2.z);

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

			lastNanos = currentNanos;
		}
	}

	private int getIndex(int car) {
		for (int i = 0; i < path.size(); i++) {
			if (railProgress - car * trainType.getSpacing() < path.get(i).distance) {
				return i;
			}
		}
		return path.size() - 1;
	}

	private Pos3f getPosition(int car) {
		final int index = getIndex(car);
		final float newRailProgress = railProgress - car * trainType.getSpacing();
		return path.get(index).rail.getPosition(index == 0 ? newRailProgress : newRailProgress - path.get(index - 1).distance);
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

	@FunctionalInterface
	public interface RenderTrainCallback {
		void renderTrainCallback(float x, float y, float z, float yaw, float pitch, String customId, TrainType trainType, boolean isEnd1Head, boolean isEnd2Head, float doorLeftValue, float doorRightValue, boolean opening, boolean shouldOffsetRender);
	}

	@FunctionalInterface
	public interface RenderConnectionCallback {
		void renderConnectionCallback(Pos3f prevPos1, Pos3f prevPos2, Pos3f prevPos3, Pos3f prevPos4, Pos3f thisPos1, Pos3f thisPos2, Pos3f thisPos3, Pos3f thisPos4, float x, float y, float z, TrainType trainType, boolean shouldOffsetRender);
	}
}
