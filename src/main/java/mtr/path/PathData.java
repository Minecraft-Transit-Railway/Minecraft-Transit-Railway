package mtr.path;

import mtr.data.Pos3f;
import mtr.data.Rail;
import mtr.data.SerializedDataBase;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public class PathData extends SerializedDataBase {

	public final float length;
	public final float tOffset;
	public final float finalSpeed;

	private final Rail rail;
	private final float a1, b1;
	private final float a2, b2;
	private final float tSwitch;
	private final float tEnd;
	private final int delay;

	private static final String KEY_RAIL = "rail";
	private static final String KEY_LENGTH = "length";
	private static final String KEY_T_OFFSET = "t_offset";
	private static final String KEY_FINAL_SPEED = "final_speed";
	private static final String KEY_A_1 = "a_1";
	private static final String KEY_B_1 = "b_1";
	private static final String KEY_A_2 = "a_2";
	private static final String KEY_B_2 = "b_2";
	private static final String KEY_T_SWITCH = "t_switch";
	private static final String KEY_T_END = "t_end";
	private static final String KEY_DELAY = "delay";

	// distance = aT^2 + bT

	private static final float HALF_ACCELERATION = 0.005F;

	public PathData(Rail rail, float startSpeed, boolean shouldStop, float tOffset) {
		length = rail.getLength();
		this.rail = rail;
		this.tOffset = tOffset;
		final float maxBlocksPerTick = rail.railType.maxBlocksPerTick;

		if (shouldStop) {
			delay = 40; // TODO editable platform dwell time

			a1 = 0;
			a2 = -Math.max(startSpeed * startSpeed / (4 * length), HALF_ACCELERATION);
			b1 = b2 = startSpeed;

			final float slowTime = getTimeAtSpeed(a2, b2, 0);
			final float distance = getDistance(a2, b2, slowTime);
			tSwitch = (length - distance) / startSpeed;
			tEnd = tSwitch + slowTime;

			finalSpeed = 0;
		} else {
			delay = 0;
			a1 = Math.signum(maxBlocksPerTick - startSpeed) * HALF_ACCELERATION;
			b1 = startSpeed;
			a2 = 0;
			b2 = maxBlocksPerTick;

			tSwitch = getTimeAtSpeed(a1, b1, maxBlocksPerTick);

			final float distance = getDistance(a1, b1, tSwitch);
			if (distance < length) {
				tEnd = tSwitch + (length - distance) / maxBlocksPerTick;
				finalSpeed = maxBlocksPerTick;
			} else {
				tEnd = solveQuadratic(a1, b1, -length);
				finalSpeed = 2 * a1 * tEnd + b1;
			}
		}
	}

	public PathData(CompoundTag tag) {
		rail = new Rail(tag.getCompound(KEY_RAIL));
		length = tag.getFloat(KEY_LENGTH);
		tOffset = tag.getFloat(KEY_T_OFFSET);
		finalSpeed = tag.getFloat(KEY_FINAL_SPEED);
		a1 = tag.getFloat(KEY_A_1);
		b1 = tag.getFloat(KEY_B_1);
		a2 = tag.getFloat(KEY_A_2);
		b2 = tag.getFloat(KEY_B_2);
		tSwitch = tag.getFloat(KEY_T_SWITCH);
		tEnd = tag.getFloat(KEY_T_END);
		delay = tag.getInt(KEY_DELAY);
	}

	public PathData(PacketByteBuf packet) {
		rail = new Rail(packet);
		length = packet.readFloat();
		tOffset = packet.readFloat();
		finalSpeed = packet.readFloat();
		a1 = packet.readFloat();
		b1 = packet.readFloat();
		a2 = packet.readFloat();
		b2 = packet.readFloat();
		tSwitch = packet.readFloat();
		tEnd = packet.readFloat();
		delay = packet.readInt();
	}

	@Override
	public CompoundTag toCompoundTag() {
		final CompoundTag tag = new CompoundTag();
		tag.put(KEY_RAIL, rail.toCompoundTag());
		tag.putFloat(KEY_LENGTH, length);
		tag.putFloat(KEY_T_OFFSET, tOffset);
		tag.putFloat(KEY_FINAL_SPEED, finalSpeed);
		tag.putFloat(KEY_A_1, a1);
		tag.putFloat(KEY_B_1, b1);
		tag.putFloat(KEY_A_2, a2);
		tag.putFloat(KEY_B_2, b2);
		tag.putFloat(KEY_T_SWITCH, tSwitch);
		tag.putFloat(KEY_T_END, tEnd);
		tag.putInt(KEY_DELAY, delay);
		return tag;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		rail.writePacket(packet);
		packet.writeFloat(length);
		packet.writeFloat(tOffset);
		packet.writeFloat(finalSpeed);
		packet.writeFloat(a1);
		packet.writeFloat(b1);
		packet.writeFloat(a2);
		packet.writeFloat(b2);
		packet.writeFloat(tSwitch);
		packet.writeFloat(tEnd);
		packet.writeInt(delay);
	}

	public Pos3f getPosition(float value) {
		return rail.getPosition(value);
	}

	public float getPositionIndex(float value) {
		final float offsetValue = value - tOffset;
		if (offsetValue < tSwitch) {
			return getDistance(a1, b1, offsetValue);
		} else if (offsetValue < tEnd) {
			return getDistance(a2, b2, offsetValue - tSwitch) + getDistance(a1, b1, tSwitch);
		} else if (offsetValue < tEnd + delay) {
			return getDistance(a2, b2, tEnd - tSwitch) + getDistance(a1, b1, tSwitch);
		} else {
			return -1;
		}
	}

	public float getTime() {
		return tEnd + delay;
	}

	private static float solveQuadratic(float a, float b, float c) {
		final float d = b * b - 4 * a * c;
		return d < 0 ? 0 : getTimeAtSpeed(a, b, (float) Math.sqrt(d));
	}

	private static float getDistance(float a, float b, float t) {
		return a * t * t + b * t;
	}

	private static float getTimeAtSpeed(float a, float b, float speed) {
		return a == 0 ? 0 : (speed - b) / (2 * a);
	}
}
