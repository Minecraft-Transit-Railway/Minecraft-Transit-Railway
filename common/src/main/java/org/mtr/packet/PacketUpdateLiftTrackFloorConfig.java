package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockLiftTrackFloor;

public final class PacketUpdateLiftTrackFloorConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final String floorNumber;
	private final String floorDescription;
	private final boolean shouldDing;

	public PacketUpdateLiftTrackFloorConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		floorNumber = packetBufferReceiver.readString();
		floorDescription = packetBufferReceiver.readString();
		shouldDing = packetBufferReceiver.readBoolean();
	}

	public PacketUpdateLiftTrackFloorConfig(BlockPos blockPos, String floorNumber, String floorDescription, boolean shouldDing) {
		this.blockPos = blockPos;
		this.floorNumber = floorNumber;
		this.floorDescription = floorDescription;
		this.shouldDing = shouldDing;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeString(floorNumber);
		packetBufferSender.writeString(floorDescription);
		packetBufferSender.writeBoolean(shouldDing);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!Init.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockLiftTrackFloor.BlockEntity) {
			((BlockLiftTrackFloor.BlockEntity) entity.data).setData(floorNumber, floorDescription, shouldDing);
			// TODO update lift floor
		}
	}
}
