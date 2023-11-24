package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.BlockEntityPIDS;
import org.mtr.mod.screen.PIDSConfigScreen;

public final class PacketOpenPIDSConfigScreen extends PacketHandler {

	private final BlockPos blockPos1;
	private final BlockPos blockPos2;
	private final int maxArrivals;
	private final int linesPerArrival;

	public PacketOpenPIDSConfigScreen(PacketBuffer packetBuffer) {
		blockPos1 = packetBuffer.readBlockPos();
		blockPos2 = packetBuffer.readBlockPos();
		maxArrivals = packetBuffer.readInt();
		linesPerArrival = packetBuffer.readInt();
	}

	public PacketOpenPIDSConfigScreen(BlockPos blockPos1, BlockPos blockPos2, int maxArrivals, int linesPerArrival) {
		this.blockPos1 = blockPos1;
		this.blockPos2 = blockPos2;
		this.maxArrivals = maxArrivals;
		this.linesPerArrival = linesPerArrival;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos1);
		packetBuffer.writeBlockPos(blockPos2);
		packetBuffer.writeInt(maxArrivals);
		packetBuffer.writeInt(linesPerArrival);
	}

	@Override
	public void runClientQueued() {
		IPacket.getBlockEntity(blockPos1, blockEntity1 -> {
			if (blockEntity1.data instanceof BlockEntityPIDS) {
				IPacket.getBlockEntity(blockPos2, blockEntity2 -> {
					if (blockEntity2.data instanceof BlockEntityPIDS) {
						IPacket.openScreen(new PIDSConfigScreen(blockPos1, blockPos2, maxArrivals, linesPerArrival), screenExtension -> screenExtension instanceof PIDSConfigScreen);
					}
				});
			}
		});
	}
}
