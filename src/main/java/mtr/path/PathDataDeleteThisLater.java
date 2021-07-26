package mtr.path;

import mtr.data.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class PathDataDeleteThisLater extends SerializedDataBase {

	public final float length;
	public final float tOffset;
	public final float tEnd;
	public final float finalSpeed;

	private final Rail rail;
	private final float a1, b1;
	private final float a2, b2;
	private final float tSwitch;
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

	public PathDataDeleteThisLater(NbtCompound nbtCompound) {
		rail = new Rail(nbtCompound.getCompound(KEY_RAIL));
		length = nbtCompound.getFloat(KEY_LENGTH);
		tOffset = nbtCompound.getFloat(KEY_T_OFFSET);
		finalSpeed = nbtCompound.getFloat(KEY_FINAL_SPEED);
		a1 = nbtCompound.getFloat(KEY_A_1);
		b1 = nbtCompound.getFloat(KEY_B_1);
		a2 = nbtCompound.getFloat(KEY_A_2);
		b2 = nbtCompound.getFloat(KEY_B_2);
		tSwitch = nbtCompound.getFloat(KEY_T_SWITCH);
		tEnd = nbtCompound.getFloat(KEY_T_END);
		delay = nbtCompound.getInt(KEY_DELAY);
	}

	public PathDataDeleteThisLater(PacketByteBuf packet) {
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
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = new NbtCompound();
		nbtCompound.put(KEY_RAIL, rail.toCompoundTag());
		nbtCompound.putFloat(KEY_LENGTH, length);
		nbtCompound.putFloat(KEY_T_OFFSET, tOffset);
		nbtCompound.putFloat(KEY_FINAL_SPEED, finalSpeed);
		nbtCompound.putFloat(KEY_A_1, a1);
		nbtCompound.putFloat(KEY_B_1, b1);
		nbtCompound.putFloat(KEY_A_2, a2);
		nbtCompound.putFloat(KEY_B_2, b2);
		nbtCompound.putFloat(KEY_T_SWITCH, tSwitch);
		nbtCompound.putFloat(KEY_T_END, tEnd);
		nbtCompound.putInt(KEY_DELAY, delay);
		return nbtCompound;
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

	public float getTime() {
		return tEnd + delay;
	}

	public boolean isPlatform(long platformId, Set<Platform> platforms) {
		if (rail.railType != RailType.PLATFORM) {
			return false;
		}

		final Pos3f pos3f = rail.getPosition(0);
		final Platform platform = RailwayData.getPlatformByPos(platforms, new BlockPos(pos3f.x, pos3f.y, pos3f.z));
		return platform != null && platform.id == platformId;
	}
}
