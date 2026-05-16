package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.block.BlockLiftTrackFloor;

public final class PacketUpdateLiftTrackFloorConfig extends BlockEntityPacketHandler {

	private final BlockPos blockPos;
	private final String floorNumber;
	private final String floorDescription;
	private final boolean shouldDing;

	public PacketUpdateLiftTrackFloorConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		floorNumber = packetBufferReceiver.readString();
		floorDescription = packetBufferReceiver.readString();
		shouldDing = packetBufferReceiver.readBoolean();
	}

	public PacketUpdateLiftTrackFloorConfig(BlockPos blockPos, String floorNumber, String floorDescription, boolean shouldDing) {
		this.blockPos = blockPos;
		this.floorNumber = floorNumber;
		this.floorDescription = floorDescription;
		this.shouldDing = shouldDing;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeString(floorNumber);
		packetBufferSender.writeString(floorDescription);
		packetBufferSender.writeBoolean(shouldDing);
	}

	@Override
	protected void setData(@Nullable World world) {
		if (world == null || !MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof BlockLiftTrackFloor.LiftTrackFloorBlockEntity) {
			((BlockLiftTrackFloor.LiftTrackFloorBlockEntity) entity).setData(floorNumber, floorDescription, shouldDing);
			// TODO update lift floor
		}
	}
}
