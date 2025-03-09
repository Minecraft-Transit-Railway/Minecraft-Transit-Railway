package org.mtr.packet;

import net.minecraft.util.math.BlockPos;

public final class PacketOpenBlockEntityScreen extends PacketHandler {

	private final BlockPos blockPos;

	public PacketOpenBlockEntityScreen(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
	}

	public PacketOpenBlockEntityScreen(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
	}

	@Override
	public void runClient() {
		ClientPacketHelper.openBlockEntityScreen(blockPos);
	}
}
