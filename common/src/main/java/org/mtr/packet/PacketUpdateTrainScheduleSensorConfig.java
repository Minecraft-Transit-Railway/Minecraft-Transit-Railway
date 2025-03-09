package org.mtr.packet;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockTrainScheduleSensor;

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
		if (!MTR.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity instanceof BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity) {
			((BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity) entity).setData(filterRouteIds, stoppedOnly, movingOnly, seconds, realtimeOnly);
		}
	}
}
