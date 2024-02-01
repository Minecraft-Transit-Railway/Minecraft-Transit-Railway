package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;

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
		ClientPacketHelper.openPIDSConfigScreen(blockPos, maxArrivals);
	}
}
