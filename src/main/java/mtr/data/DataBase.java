package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public abstract class DataBase {

	public abstract CompoundTag toCompoundTag();

	public abstract void writePacket(PacketByteBuf packet);
}
