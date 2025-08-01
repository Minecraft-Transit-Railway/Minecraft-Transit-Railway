package org.mtr.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.MTR;
import org.mtr.block.BlockDriverKeyDispenser;

public final class PacketUpdateKeyDispenserConfig extends PacketHandler {

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

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		if (!MTR.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity instanceof BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity) {
			((BlockDriverKeyDispenser.DriverKeyDispenserBlockEntity) entity).setData(dispenseBasicDriverKey, dispenseAdvancedDriverKey, dispenseGuardKey, timeout);
		}
	}
}
