package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public abstract class SerializedDataBase {

	public static final int PACKET_STRING_READ_LENGTH = 32767;

	public abstract CompoundTag toCompoundTag();

	public abstract void writePacket(PacketByteBuf packet);
}
