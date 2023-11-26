package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.BlockPIDSBase;

public final class PacketUpdatePIDSConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final String[] messages;
	private final boolean[] hideArrival;
	private final LongAVLTreeSet platformIds;
	private final int displayPage;

	public PacketUpdatePIDSConfig(PacketBuffer packetBuffer) {
		blockPos = packetBuffer.readBlockPos();

		final int maxMessages = packetBuffer.readInt();
		messages = new String[maxMessages];
		for (int i = 0; i < maxMessages; i++) {
			messages[i] = readString(packetBuffer);
		}

		final int maxArrivals = packetBuffer.readInt();
		hideArrival = new boolean[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			hideArrival[i] = packetBuffer.readBoolean();
		}

		final int platformIdCount = packetBuffer.readInt();
		platformIds = new LongAVLTreeSet();
		for (int i = 0; i < platformIdCount; i++) {
			platformIds.add(packetBuffer.readLong());
		}

		displayPage = packetBuffer.readInt();
	}

	public PacketUpdatePIDSConfig(BlockPos blockPos, String[] messages, boolean[] hideArrival, LongAVLTreeSet platformIds, int displayPage) {
		this.blockPos = blockPos;
		this.messages = messages;
		this.hideArrival = hideArrival;
		this.platformIds = platformIds;
		this.displayPage = displayPage;
	}

	@Override
	public void write(PacketBuffer packetBuffer) {
		packetBuffer.writeBlockPos(blockPos);

		packetBuffer.writeInt(messages.length);
		for (final String message : messages) {
			writeString(packetBuffer, message);
		}

		packetBuffer.writeInt(hideArrival.length);
		for (final boolean hideArrival : hideArrival) {
			packetBuffer.writeBoolean(hideArrival);
		}

		packetBuffer.writeInt(platformIds.size());
		platformIds.forEach(packetBuffer::writeLong);
		packetBuffer.writeInt(displayPage);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockPIDSBase.BlockEntityBase) {
			((BlockPIDSBase.BlockEntityBase) entity.data).setData(messages, hideArrival, platformIds, displayPage);
		}
	}
}
