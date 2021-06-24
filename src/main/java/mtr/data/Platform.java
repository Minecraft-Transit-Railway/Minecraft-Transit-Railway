package mtr.data;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.function.Consumer;

public final class Platform extends SavedRailBase {

	private int dwellTime;

	public static final int MAX_DWELL_TIME = 120;
	private static final int DEFAULT_DWELL_TIME = 20;
	private static final String KEY_DWELL_TIME = "dwell_time";

	public Platform(long id, BlockPos pos1, BlockPos pos2) {
		super(id, pos1, pos2);
		dwellTime = DEFAULT_DWELL_TIME;
	}

	public Platform(BlockPos pos1, BlockPos pos2) {
		super(pos1, pos2);
		dwellTime = DEFAULT_DWELL_TIME;
	}

	public Platform(NbtCompound nbtCompound) {
		super(nbtCompound);
		dwellTime = nbtCompound.getInt(KEY_DWELL_TIME);
	}

	public Platform(PacketByteBuf packet) {
		super(packet);
		dwellTime = packet.readInt();
	}

	@Override
	public NbtCompound toCompoundTag() {
		final NbtCompound nbtCompound = super.toCompoundTag();
		nbtCompound.putInt(KEY_DWELL_TIME, dwellTime);
		return nbtCompound;
	}

	@Override
	public void writePacket(PacketByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(dwellTime);
	}

	@Override
	public void update(String key, PacketByteBuf packet) {
		if (KEY_DWELL_TIME.equals(key)) {
			dwellTime = packet.readInt();
		} else {
			super.update(key, packet);
		}
	}

	public int getDwellTime() {
		if (dwellTime <= 0 || dwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		}
		return dwellTime;
	}

	public void setDwellTime(int newDwellTime, Consumer<PacketByteBuf> sendPacket) {
		if (newDwellTime <= 0 || newDwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		} else {
			dwellTime = newDwellTime;
		}

		final PacketByteBuf packet = PacketByteBufs.create();
		packet.writeLong(id);
		packet.writeString(KEY_DWELL_TIME);
		packet.writeInt(dwellTime);
		sendPacket.accept(packet);
	}
}
