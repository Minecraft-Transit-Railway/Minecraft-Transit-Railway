package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;

public final class PacketOpenLiftCustomizationScreen extends PacketHandler {

	private final BlockPos blockPos;

	public PacketOpenLiftCustomizationScreen(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
	}

	public PacketOpenLiftCustomizationScreen(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
	}

	@Override
	public void runClient() {
		ClientPacketHelper.openLiftCustomizationScreen(blockPos);
	}
}
