package mtr.data;

import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import org.msgpack.core.MessagePacker;
import org.msgpack.value.Value;

import java.io.IOException;
import java.util.Map;
import java.util.function.Consumer;

public final class Platform extends SavedRailBase {

	private int dwellTime;

	public static final int MAX_DWELL_TIME = 120;
	private static final int DEFAULT_DWELL_TIME = 20;
	private static final String KEY_DWELL_TIME = "dwell_time";

	public Platform(long id, TransportMode transportMode, BlockPos pos1, BlockPos pos2) {
		super(id, transportMode, pos1, pos2);
		dwellTime = DEFAULT_DWELL_TIME;
	}

	public Platform(TransportMode transportMode, BlockPos pos1, BlockPos pos2) {
		super(transportMode, pos1, pos2);
		dwellTime = DEFAULT_DWELL_TIME;
	}

	public Platform(Map<String, Value> map) {
		super(map);
		final MessagePackHelper messagePackHelper = new MessagePackHelper(map);
		dwellTime = messagePackHelper.getInt(KEY_DWELL_TIME);
	}

	@Deprecated
	public Platform(CompoundTag compoundTag) {
		super(compoundTag);
		dwellTime = compoundTag.getInt(KEY_DWELL_TIME);
	}

	public Platform(FriendlyByteBuf packet) {
		super(packet);
		dwellTime = packet.readInt();
	}

	@Override
	public void toMessagePack(MessagePacker messagePacker) throws IOException {
		super.toMessagePack(messagePacker);

		messagePacker.packString(KEY_DWELL_TIME).packInt(dwellTime);
	}

	@Override
	public int messagePackLength() {
		return super.messagePackLength() + 1;
	}

	@Override
	public void writePacket(FriendlyByteBuf packet) {
		super.writePacket(packet);
		packet.writeInt(dwellTime);
	}

	@Override
	public void update(String key, FriendlyByteBuf packet) {
		if (KEY_DWELL_TIME.equals(key)) {
			name = packet.readUtf(PACKET_STRING_READ_LENGTH);
			color = packet.readInt();
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

	public void setDwellTime(int newDwellTime, Consumer<FriendlyByteBuf> sendPacket) {
		if (newDwellTime <= 0 || newDwellTime > MAX_DWELL_TIME) {
			dwellTime = DEFAULT_DWELL_TIME;
		} else {
			dwellTime = newDwellTime;
		}

		final FriendlyByteBuf packet = new FriendlyByteBuf(Unpooled.buffer());
		packet.writeLong(id);
		packet.writeUtf(transportMode.toString());
		packet.writeUtf(KEY_DWELL_TIME);
		packet.writeUtf(name);
		packet.writeInt(color);
		packet.writeInt(dwellTime);
		sendPacket.accept(packet);
	}
}
