package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.BlockPIDSBase;
import org.mtr.mod.screen.PIDSConfigScreen;

public final class PacketOpenPIDSConfigScreen extends PacketHandler {

	private final BlockPos blockPos;
	private final int maxArrivals;

	public PacketOpenPIDSConfigScreen(PacketBuffer packetBuffer) {
		blockPos = packetBuffer.readBlockPos();
		maxArrivals = packetBuffer.readInt();
	}

	public PacketOpenPIDSConfigScreen(BlockPos blockPos, int maxArrivals) {
		this.blockPos = blockPos;
		this.maxArrivals = maxArrivals;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos);
		packetBuffer.writeInt(maxArrivals);
	}

	@Override
	public void runClientQueued() {
		IPacket.getBlockEntity(blockPos, blockEntity1 -> {
			if (blockEntity1.data instanceof BlockPIDSBase.BlockEntityBase) {
				IPacket.openScreen(new PIDSConfigScreen(blockPos, maxArrivals), screenExtension -> screenExtension instanceof PIDSConfigScreen);
			}
		});
	}
}
