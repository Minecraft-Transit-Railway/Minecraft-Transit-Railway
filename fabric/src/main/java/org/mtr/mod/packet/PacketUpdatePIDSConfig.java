package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mod.block.BlockPIDSBase;

public final class PacketUpdatePIDSConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final String[] messages;
	private final LongAVLTreeSet platformIds;
	private final int displayPage;

	public PacketUpdatePIDSConfig(PacketBuffer packetBuffer) {
		blockPos = packetBuffer.readBlockPos();

		final int maxMessages = packetBuffer.readInt();
		messages = new String[maxMessages];
		for (int i = 0; i < maxMessages; i++) {
			messages[i] = readString(packetBuffer);
		}

		final int platformIdCount = packetBuffer.readInt();
		platformIds = new LongAVLTreeSet();
		for (int i = 0; i < platformIdCount; i++) {
			platformIds.add(packetBuffer.readLong());
		}

		displayPage = packetBuffer.readInt();
	}

	public PacketUpdatePIDSConfig(BlockPos blockPos, String[] messages, LongAVLTreeSet platformIds, int displayPage) {
		this.blockPos = blockPos;
		this.messages = messages;
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

		packetBuffer.writeInt(platformIds.size());
		platformIds.forEach(packetBuffer::writeLong);
		packetBuffer.writeInt(displayPage);
	}

	@Override
	public void runServerQueued(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockPIDSBase.BlockEntityBase) {
			((BlockPIDSBase.BlockEntityBase) entity.data).setData(messages, platformIds, displayPage);
		}
	}
}
