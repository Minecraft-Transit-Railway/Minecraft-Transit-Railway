package org.mtr.packet;

import net.minecraft.util.math.BlockPos;

public final class PacketOpenPIDSConfigScreen extends PacketHandler {

	private final BlockPos blockPos;
	private final int maxArrivals;

	public PacketOpenPIDSConfigScreen(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		maxArrivals = packetBufferReceiver.readInt();
	}

	public PacketOpenPIDSConfigScreen(BlockPos blockPos, int maxArrivals) {
		this.blockPos = blockPos;
		this.maxArrivals = maxArrivals;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeInt(maxArrivals);
	}

	@Override
	public void runClient() {
		ClientPacketHelper.openPIDSConfigScreen(blockPos, maxArrivals);
	}
}
