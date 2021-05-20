package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.Map;

public class Train extends SerializedDataBase {

	public int pathIndex;
	public float pathPartProgress;
	public float speed;
	public int delay;

	public final TrainType trainType;
	public final int length;
	public final long scheduledStartTime;

	private static final int MINECRAFT_DAY_MILLIS = 20 * 60 * 1000;

	public Train(TrainType trainType, int length, long scheduledStartTime) {
		this.trainType = trainType;
		this.length = length;
		this.scheduledStartTime = scheduledStartTime;
	}

	@Override
	public CompoundTag toCompoundTag() {
		return null;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {

	}

	public void move(Map<BlockPos, Map<BlockPos, Rail>> rails, List<BlockPos> path) {
		if (path.size() == 0) {
			return;
		}

		final long currentMillis = System.currentTimeMillis();
		if (pathIndex >= path.size() || scheduledStartTime + MINECRAFT_DAY_MILLIS < currentMillis) {
			pathIndex = 0;
		}

		path.get(pathIndex);
	}
}
