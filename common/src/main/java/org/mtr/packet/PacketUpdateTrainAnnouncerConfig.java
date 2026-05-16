package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.block.BlockTrainAnnouncer;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;

public class PacketUpdateTrainAnnouncerConfig extends PacketUpdateTrainSensorConfig {

	private final String message;
	private final String soundId;
	private final int delay;

	public PacketUpdateTrainAnnouncerConfig(PacketBufferReceiver packetBufferReceiver) {
		super(packetBufferReceiver);
		message = packetBufferReceiver.readString();
		soundId = packetBufferReceiver.readString();
		delay = packetBufferReceiver.readInt();
	}

	public PacketUpdateTrainAnnouncerConfig(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly, String message, String soundId, int delay) {
		super(blockPos, filterRouteIds, stoppedOnly, movingOnly);
		this.message = message;
		this.soundId = soundId;
		this.delay = delay;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		super.write(packetBufferSender);
		packetBufferSender.writeString(message);
		packetBufferSender.writeString(soundId);
		packetBufferSender.writeInt(delay);
	}

	@Override
	protected void setData(@Nullable World world) {
		if (world == null || !MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof BlockTrainAnnouncer.TrainAnnouncerBlockEntity) {
			((BlockTrainAnnouncer.TrainAnnouncerBlockEntity) entity).setData(filterRouteIds, stoppedOnly, movingOnly, message, soundId, delay);
		}
	}
}
