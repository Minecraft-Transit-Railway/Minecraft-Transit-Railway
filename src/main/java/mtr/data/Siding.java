package mtr.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class Siding extends SavedRailBase {

	public Siding(BlockPos pos1, BlockPos pos2) {
		super(pos1, pos2);
	}

	public Siding(CompoundTag tag) {
		super(tag);
	}

	public Siding(PacketByteBuf packet) {
		super(packet);
	}
}
