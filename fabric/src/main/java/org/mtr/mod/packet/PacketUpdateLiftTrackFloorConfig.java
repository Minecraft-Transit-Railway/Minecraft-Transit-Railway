package org.mtr.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.BlockLiftTrackFloor;

public final class PacketUpdateLiftTrackFloorConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final String floorNumber;
	private final String floorDescription;
	private final boolean shouldDing;

	public PacketUpdateLiftTrackFloorConfig(PacketBuffer packetBuffer) {
		blockPos = packetBuffer.readBlockPos();
		floorNumber = readString(packetBuffer);
		floorDescription = readString(packetBuffer);
		shouldDing = packetBuffer.readBoolean();
	}

	public PacketUpdateLiftTrackFloorConfig(BlockPos blockPos, String floorNumber, String floorDescription, boolean shouldDing) {
		this.blockPos = blockPos;
		this.floorNumber = floorNumber;
		this.floorDescription = floorDescription;
		this.shouldDing = shouldDing;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos);
		writeString(packetBuffer, floorNumber);
		writeString(packetBuffer, floorDescription);
		packetBuffer.writeBoolean(shouldDing);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockLiftTrackFloor.BlockEntity) {
			((BlockLiftTrackFloor.BlockEntity) entity.data).setData(floorNumber, floorDescription, shouldDing);
		}
	}
}
