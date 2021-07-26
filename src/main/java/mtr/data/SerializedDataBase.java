package mtr.data;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;

public abstract class SerializedDataBase {

	public static final int PACKET_STRING_READ_LENGTH = 32767;

	public abstract NbtCompound toCompoundTag();

	public abstract void writePacket(PacketByteBuf packet);
}
