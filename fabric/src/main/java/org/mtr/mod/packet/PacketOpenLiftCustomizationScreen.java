package org.mtr.mod.packet;

import org.mtr.core.data.Lift;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.holder.Screen;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.Init;
import org.mtr.mod.client.ClientData;
import org.mtr.mod.screen.LiftCustomizationScreen;

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
		for (final Lift lift : ClientData.getInstance().lifts) {
			if (lift.getFloorIndex(Init.blockPosToPosition(blockPos)) >= 0) {
				MinecraftClient.getInstance().openScreen(new Screen(new LiftCustomizationScreen(lift)));
				break;
			}
		}
	}
}
