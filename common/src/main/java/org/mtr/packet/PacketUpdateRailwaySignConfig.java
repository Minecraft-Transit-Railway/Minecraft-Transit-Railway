package org.mtr.packet;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockRailwaySign;
import org.mtr.block.BlockRouteSignBase;

public final class PacketUpdateRailwaySignConfig extends PacketHandler {

	private final BlockPos blockPos;
	private final LongAVLTreeSet selectedIds;
	private final String[] signIds;

	public PacketUpdateRailwaySignConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		final int selectedIdsLength = packetBufferReceiver.readInt();
		selectedIds = new LongAVLTreeSet();
		for (int i = 0; i < selectedIdsLength; i++) {
			selectedIds.add(packetBufferReceiver.readLong());
		}
		final int signLength = packetBufferReceiver.readInt();
		signIds = new String[signLength];
		for (int i = 0; i < signLength; i++) {
			final String signId = packetBufferReceiver.readString();
			signIds[i] = signId.isEmpty() ? null : signId;
		}
	}

	public PacketUpdateRailwaySignConfig(BlockPos blockPos, LongAVLTreeSet selectedIds, String[] signIds) {
		this.blockPos = blockPos;
		this.selectedIds = selectedIds;
		this.signIds = signIds;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeInt(selectedIds.size());
		selectedIds.forEach(packetBufferSender::writeLong);
		packetBufferSender.writeInt(signIds.length);
		for (final String signType : signIds) {
			packetBufferSender.writeString(signType == null ? "" : signType);
		}
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!MTR.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null) {
			if (entity instanceof BlockRailwaySign.RailwaySignBlockEntity) {
				((BlockRailwaySign.RailwaySignBlockEntity) entity).setData(selectedIds, signIds);
			} else if (entity instanceof BlockRouteSignBase.BlockEntityBase) {
				final long platformId = selectedIds.isEmpty() ? 0 : (long) selectedIds.toArray()[0];
				((BlockRouteSignBase.BlockEntityBase) entity).setPlatformId(platformId);
				final BlockEntity entityAbove = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos.up());
				if (entityAbove instanceof BlockRouteSignBase.BlockEntityBase) {
					((BlockRouteSignBase.BlockEntityBase) entityAbove).setPlatformId(platformId);
				}
			}
		}
	}
}
