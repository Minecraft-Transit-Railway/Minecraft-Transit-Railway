package org.mtr.packet;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockPIDSBase;

public final class PacketUpdatePIDSConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final String[] messages;
	private final boolean[] hideArrivalArray;
	private final LongAVLTreeSet platformIds;
	private final int displayPage;

	public PacketUpdatePIDSConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());

		final int maxMessages = packetBufferReceiver.readInt();
		messages = new String[maxMessages];
		for (int i = 0; i < maxMessages; i++) {
			messages[i] = packetBufferReceiver.readString();
		}

		hideArrivalArray = new boolean[maxMessages];
		for (int i = 0; i < maxMessages; i++) {
			hideArrivalArray[i] = packetBufferReceiver.readBoolean();
		}

		final int platformIdCount = packetBufferReceiver.readInt();
		platformIds = new LongAVLTreeSet();
		for (int i = 0; i < platformIdCount; i++) {
			platformIds.add(packetBufferReceiver.readLong());
		}

		displayPage = packetBufferReceiver.readInt();
	}

	public PacketUpdatePIDSConfig(BlockPos blockPos, String[] messages, boolean[] hideArrivalArray, LongAVLTreeSet platformIds, int displayPage) {
		this.blockPos = blockPos;
		this.messages = messages;
		this.hideArrivalArray = hideArrivalArray;
		this.platformIds = platformIds;
		this.displayPage = displayPage;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());

		packetBufferSender.writeInt(messages.length);
		for (final String message : messages) {
			packetBufferSender.writeString(message);
		}

		for (final boolean hideArrival : hideArrivalArray) {
			packetBufferSender.writeBoolean(hideArrival);
		}

		packetBufferSender.writeInt(platformIds.size());
		platformIds.forEach(packetBufferSender::writeLong);
		packetBufferSender.writeInt(displayPage);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!MTR.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity instanceof BlockPIDSBase.BlockEntityBase) {
			((BlockPIDSBase.BlockEntityBase) entity).setData(messages, hideArrivalArray, platformIds, displayPage);
		}
	}
}
