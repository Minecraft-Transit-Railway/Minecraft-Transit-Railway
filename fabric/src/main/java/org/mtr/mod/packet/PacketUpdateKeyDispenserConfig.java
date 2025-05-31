package org.mtr.mod.packet;

import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.MinecraftServer;
import org.mtr.mapping.holder.ServerPlayerEntity;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockDriverKeyDispenser;

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
		if (!Init.isChunkLoaded(serverPlayerEntity.getEntityWorld(), blockPos)) {
			return;
		}

		final BlockEntity entity = serverPlayerEntity.getEntityWorld().getBlockEntity(blockPos);
		if (entity != null && entity.data instanceof BlockDriverKeyDispenser.BlockEntity) {
			((BlockDriverKeyDispenser.BlockEntity) entity.data).setData(dispenseBasicDriverKey, dispenseAdvancedDriverKey, dispenseGuardKey, timeout);
		}
	}
}
