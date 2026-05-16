package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.block.BlockPIDSBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;

public final class PacketUpdatePIDSConfig extends BlockEntityPacketHandler {

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
	protected void setData(@Nullable World world) {
		if (world == null || !MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof BlockPIDSBase.BlockEntityBase) {
			((BlockPIDSBase.BlockEntityBase) entity).setData(messages, hideArrivalArray, platformIds, displayPage);
		}
	}
}
