package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;

public final class PacketOpenLiftCustomizationScreen extends PacketHandler {

	private final BlockPos blockPos;

	public PacketOpenLiftCustomizationScreen(PacketBuffer packetBuffer) {
		blockPos = packetBuffer.readBlockPos();
	}

	public PacketOpenLiftCustomizationScreen(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos);
	}

	@Override
	public void runClientQueued() {
		ClientPacketHelper.openLiftCustomizationScreen(blockPos);
	}
}
