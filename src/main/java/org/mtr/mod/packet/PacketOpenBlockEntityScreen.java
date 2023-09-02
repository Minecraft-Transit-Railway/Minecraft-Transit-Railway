package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.PacketBuffer;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.*;
import org.mtr.mod.screen.*;

public class PacketOpenBlockEntityScreen extends PacketHandler {

	private final BlockPos blockPos;

	public PacketOpenBlockEntityScreen(PacketBuffer packetBuffer) {
		blockPos = packetBuffer.readBlockPos();
	}

	public PacketOpenBlockEntityScreen(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos);
	}

	@Override
	public void runClientQueued() {
		IPacket.getBlockEntity(blockPos, blockEntity -> {
			if (blockEntity.data instanceof BlockTrainAnnouncer.BlockEntity) {
				IPacket.openScreen(new TrainAnnouncerScreen(blockPos), screenExtension -> screenExtension instanceof TrainAnnouncerScreen);
			} else if (blockEntity.data instanceof BlockTrainScheduleSensor.BlockEntity) {
				IPacket.openScreen(new TrainScheduleSensorScreen(blockPos), screenExtension -> screenExtension instanceof TrainScheduleSensorScreen);
			} else if (blockEntity.data instanceof BlockTrainSensorBase.BlockEntityBase) {
				IPacket.openScreen(new TrainBasicSensorScreen(blockPos), screenExtension -> screenExtension instanceof TrainBasicSensorScreen);
			} else if (blockEntity.data instanceof BlockRailwaySign.BlockEntity) {
				IPacket.openScreen(new RailwaySignScreen(blockPos), screenExtension -> screenExtension instanceof RailwaySignScreen);
			} else if (blockEntity.data instanceof BlockLiftTrackFloor.BlockEntity) {
				IPacket.openScreen(new LiftTrackFloorScreen(blockPos), screenExtension -> screenExtension instanceof LiftTrackFloorScreen);
			} else if (blockEntity.data instanceof BlockArrivalProjectorBase.BlockEntityBase) {
				IPacket.openScreen(new ArrivalProjectorConfigScreen(blockPos), screenExtension -> screenExtension instanceof ArrivalProjectorConfigScreen);
			}
		});
	}
}
