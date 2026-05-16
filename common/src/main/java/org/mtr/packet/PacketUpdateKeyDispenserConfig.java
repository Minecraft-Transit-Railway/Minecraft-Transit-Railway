package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jspecify.annotations.Nullable;
import org.mtr.MTR;
import org.mtr.block.BlockDriverKeyDispenser;

public final class PacketUpdateKeyDispenserConfig extends BlockEntityPacketHandler {

	private final BlockPos blockPos;
	private final boolean dispenseBasicDriverKey;
	private final boolean dispenseAdvancedDriverKey;
	private final boolean dispenseGuardKey;
	private final long timeout;

	public PacketUpdateKeyDispenserConfig(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		dispenseBasicDriverKey = packetBufferReceiver.readBoolean();
		dispenseAdvancedDriverKey = packetBufferReceiver.readBoolean();
		dispenseGuardKey = packetBufferReceiver.readBoolean();
		timeout = packetBufferReceiver.readLong();
	}

	public PacketUpdateKeyDispenserConfig(BlockPos blockPos, boolean dispenseBasicDriverKey, boolean dispenseAdvancedDriverKey, boolean dispenseGuardKey, long timeout) {
		this.blockPos = blockPos;
		this.dispenseBasicDriverKey = dispenseBasicDriverKey;
		this.dispenseAdvancedDriverKey = dispenseAdvancedDriverKey;
		this.dispenseGuardKey = dispenseGuardKey;
		this.timeout = timeout;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeBoolean(dispenseBasicDriverKey);
		packetBufferSender.writeBoolean(dispenseAdvancedDriverKey);
		packetBufferSender.writeBoolean(dispenseGuardKey);
		packetBufferSender.writeLong(timeout);
	}

	protected void setData(@Nullable World world) {
		if (world == null || !MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockEntity entity = world.getBlockEntity(blockPos);
		if (entity instanceof BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity) {
			((BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity) entity).setData(dispenseBasicDriverKey, dispenseAdvancedDriverKey, dispenseGuardKey, timeout);
		}
	}
}
