package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockTrainScheduleSensor;

public class PacketUpdateTrainScheduleSensorConfig extends PacketUpdateTrainSensorConfig {

	private final int seconds;
	private final boolean realtimeOnly;

	public PacketUpdateTrainScheduleSensorConfig(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		seconds = packetBufferReceiver.readInt();
		realtimeOnly = packetBufferReceiver.readBoolean();
	}

	public PacketUpdateTrainScheduleSensorConfig(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, int seconds, boolean realtimeOnly) {
		super(blockPos, filterRouteIds, stoppedOnly, movingOnly);
		this.seconds = seconds;
		this.realtimeOnly = realtimeOnly;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeInt(seconds);
		packetBufferSender.writeBoolean(realtimeOnly);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!Init.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockTrainScheduleSensor.BlockEntity) {
			((BlockTrainScheduleSensor.BlockEntity) entity.data).setData(filterRouteIds, stoppedOnly, movingOnly, seconds, realtimeOnly);
		}
	}
}
