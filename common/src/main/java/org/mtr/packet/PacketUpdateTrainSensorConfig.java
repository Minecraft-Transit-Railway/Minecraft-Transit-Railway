package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.block.BlockTrainSensorBase;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;

public class PacketUpdateTrainSensorConfig extends BlockEntityPacketHandler {

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
	protected void setData(@Nullable World world) {
		if (world == null || !MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof BlockTrainSensorBase.BlockEntityBase) {
			((BlockTrainSensorBase.BlockEntityBase) entity).setData(filterRouteIds, stoppedOnly, movingOnly);
		}
	}
}
