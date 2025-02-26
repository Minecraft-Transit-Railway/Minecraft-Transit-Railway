package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockTrainSensorBase;

public class PacketUpdateTrainSensorConfig extends PacketHandler {

	protected final BlockPos blockPos;
	protected final LongAVLTreeSet filterRouteIds;
	protected final boolean stoppedOnly;
	protected final boolean movingOnly;

	public PacketUpdateTrainSensorConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		final int routeIdCount = packetBufferReceiver.readInt();
		filterRouteIds = new LongAVLTreeSet();
		for (int i = 0; i < routeIdCount; i++) {
			filterRouteIds.add(packetBufferReceiver.readLong());
		}
		stoppedOnly = packetBufferReceiver.readBoolean();
		movingOnly = packetBufferReceiver.readBoolean();
	}

	public PacketUpdateTrainSensorConfig(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		this.blockPos = blockPos;
		this.filterRouteIds = filterRouteIds;
		this.stoppedOnly = stoppedOnly;
		this.movingOnly = movingOnly;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeInt(filterRouteIds.size());
		filterRouteIds.forEach(packetBufferSender::writeLong);
		packetBufferSender.writeBoolean(stoppedOnly);
		packetBufferSender.writeBoolean(movingOnly);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!Init.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockTrainSensorBase.BlockEntityBase) {
			((BlockTrainSensorBase.BlockEntityBase) entity.data).setData(filterRouteIds, stoppedOnly, movingOnly);
		}
	}
}
