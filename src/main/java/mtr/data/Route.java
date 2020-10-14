package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;

public class Route extends NamedColoredBase {

	public Route() {
		super();
	}

	public Route(CompoundTag tag) {
		super(tag);
	}

	public Route(PacketByteBuf packet) {
		super(packet);
	}
}
