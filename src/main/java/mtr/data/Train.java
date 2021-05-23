package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class Train extends SerializedDataBase {

	public float speed;

	private float railProgress;
	private long lastMillis;

	public final TrainType trainType;
	public final int trainLength;
	public final long scheduledStartTime;

	private final List<Rail> path;
	private final List<Float> railLengths;

	private static final float ACCELERATION = 0.01F;

	public Train(TrainType trainType, int trainLength, long scheduledStartTime, List<Rail> path, List<Float> railLengths) {
		this.trainType = trainType;
		this.trainLength = trainLength;
		this.scheduledStartTime = scheduledStartTime;
		this.path = path;
		this.railLengths = railLengths;
		lastMillis = System.currentTimeMillis();
	}

	@Override
	public CompoundTag toCompoundTag() {
		return null;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {

	}

	public void move(RenderTrainCallback renderTrainCallback) {
		if (path.isEmpty() || railLengths.isEmpty()) {
			return;
		}

		final long currentMillis = System.currentTimeMillis();
		final float newAcceleration = renderTrainCallback != null ? ACCELERATION * (currentMillis - lastMillis) / 50 : ACCELERATION;
		if (currentMillis < scheduledStartTime) {
			railProgress = 0;
		} else {
			final int headIndex = getIndex(0);
			final float lastRailLength = railLengths.get(railLengths.size() - 1);

			if (lastRailLength - railProgress < 0.5 * speed * speed / ACCELERATION) {
				speed -= newAcceleration;
			} else if (speed < path.get(headIndex).railType.maxBlocksPerTick) {
				speed += newAcceleration;
			} else if (speed > path.get(headIndex).railType.maxBlocksPerTick) {
				speed -= newAcceleration;
			}
			railProgress += speed;

			if (railProgress > lastRailLength) {
				railProgress = 0;
				speed = 0;
			}
		}

		if (renderTrainCallback != null) {
			final Pos3f[] positions = new Pos3f[trainLength + 1];
			for (int i = 0; i < trainLength + 1; i++) {
				positions[i] = getPosition(i);
			}

			for (int i = 0; i < trainLength; i++) {
				final Pos3f pos1 = positions[i];
				final Pos3f pos2 = positions[i + 1];

				if (pos1 != null && pos2 != null) {
					final float x = getAverage(pos1.x, pos2.x);
					final float y = getAverage(pos1.y, pos2.y) + 1;
					final float z = getAverage(pos1.z, pos2.z);

					final float realSpacing = pos2.getDistanceTo(pos1);
					final float yaw = (float) MathHelper.atan2(pos2.x - pos1.x, pos2.z - pos1.z);
					final float pitch = realSpacing == 0 ? 0 : (float) Math.asin((pos2.y - pos1.y) / realSpacing);

					renderTrainCallback.renderTrainCallback(x, y, z, yaw, pitch, "", trainType, true, false, 0, 0, false, false);
				}
			}

			lastMillis = currentMillis;
		}
	}

	private int getIndex(int car) {
		for (int i = 0; i < railLengths.size(); i++) {
			if (railProgress - car * trainType.getSpacing() < railLengths.get(i)) {
				return i;
			}
		}
		return railLengths.size() - 1;
	}

	private Pos3f getPosition(int car) {
		final int index = getIndex(car);
		final float newRailProgress = railProgress - car * trainType.getSpacing();
		return path.get(index).getPosition(index == 0 ? newRailProgress : newRailProgress - railLengths.get(index - 1));
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
