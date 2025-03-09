package org.mtr.packet;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockTrainSensorBase;

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
		if (!MTR.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity instanceof BlockTrainSensorBase.BlockEntityBase) {
			((BlockTrainSensorBase.BlockEntityBase) entity).setData(filterRouteIds, stoppedOnly, movingOnly);
		}
	}
}
