package org.mtr.mod.packet;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mod.block.BlockEntityPIDS;

public class PacketUpdatePIDSConfig extends PacketUpdateArrivalProjectorConfig {

	private final BlockPos blockPos2;
	private final String[] messages;
	private final boolean[] hideArrivals;

	public PacketUpdatePIDSConfig(PacketBuffer packetBuffer) {
		super(packetBuffer);
		blockPos2 = packetBuffer.readBlockPos();
		final int maxMessages = packetBuffer.readInt();
		messages = new String[maxMessages];
		for (int i = 0; i < maxMessages; i++) {
			messages[i] = readString(packetBuffer);
		}
		final int maxArrivals = packetBuffer.readInt();
		hideArrivals = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			hideArrivals[i] = packetBuffer.readBoolean();
		}
	}

	public PacketUpdatePIDSConfig(BlockPos blockPos1, BlockPos blockPos2, String[] messages, boolean[] hideArrivals, LongAVLTreeSet filterPlatformIds, int displayPage) {
		super(blockPos1, filterPlatformIds, displayPage);
		this.blockPos2 = blockPos2;
		this.messages = messages;
		this.hideArrivals = hideArrivals;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		super.write(packetBuffer);
		packetBuffer.writeBlockPos(blockPos2);
		packetBuffer.writeInt(messages.length);
		for (final String message : messages) {
			writeString(packetBuffer, message);
		}
		packetBuffer.writeInt(hideArrivals.length);
		for (final boolean hideArrival : hideArrivals) {
			packetBuffer.writeBoolean(hideArrival);
		}
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity1 = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos1);
		final BlockEntity entity2 = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos2);
		if (entity1 != null && entity2 != null && entity1.data instanceof BlockEntityPIDS && entity2.data instanceof BlockEntityPIDS) {
			((BlockEntityPIDS) entity1.data).setData(messages, hideArrivals, filterPlatformIds, displayPage);
			((BlockEntityPIDS) entity2.data).setData(messages, hideArrivals, filterPlatformIds, displayPage);
		}
	}
}
