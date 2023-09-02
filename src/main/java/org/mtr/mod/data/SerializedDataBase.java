package mtr.data;

import net.minecraft.network.FriendlyByteBuf;
import org.msgpack.core.MessagePacker;

import java.io.IOException;

public abstract class SerializedDataBase {

	public static final int PACKET_STRING_READ_LENGTH = 32767;

	public abstract void toMessagePack(MessagePacker messagePacker) throws IOException;

	public abstract int messagePackLength();

	public abstract void writePacket(FriendlyByteBuf packet);
}
