package org.mtr.mod.packet;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.block.BlockTrainAnnouncer;

public class PacketUpdateTrainAnnouncerConfig extends PacketUpdateTrainSensorConfig {

	private final String message;
	private final String soundId;

	public PacketUpdateTrainAnnouncerConfig(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		message = packetBufferReceiver.readString();
		soundId = packetBufferReceiver.readString();
	}

	public PacketUpdateTrainAnnouncerConfig(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, String message, String soundId) {
		super(blockPos, filterRouteIds, stoppedOnly, movingOnly);
		this.message = message;
		this.soundId = soundId;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeString(message);
		packetBufferSender.writeString(soundId);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockTrainAnnouncer.BlockEntity) {
			((BlockTrainAnnouncer.BlockEntity) entity.data).setData(filterRouteIds, stoppedOnly, movingOnly, message, soundId);
		}
	}
}
