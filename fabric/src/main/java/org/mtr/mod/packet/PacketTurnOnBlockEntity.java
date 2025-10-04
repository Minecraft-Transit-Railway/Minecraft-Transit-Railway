package org.mtr.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockSignalBase;
import org.mtr.mod.block.BlockTrainPoweredSensorBase;

public final class PacketTurnOnBlockEntity extends PacketHandler {

	private final BlockPos blockPos;
	private final int level;

	public PacketTurnOnBlockEntity(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
		level = packetBufferReceiver.readInt();
	}

	public PacketTurnOnBlockEntity(BlockPos blockPos) {
		this(blockPos, 0);
	}

	public PacketTurnOnBlockEntity(BlockPos blockPos, int level) {
		this.blockPos = blockPos;
		this.level = level;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
		packetBufferSender.writeInt(level);
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final World world = new World(serverPlayerEntity.getServerWorld().data);
		if (!Init.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();
		if (block.data instanceof BlockTrainPoweredSensorBase) {
			((BlockTrainPoweredSensorBase) block.data).power(world, blockState, blockPos);
		}
		if (block.data instanceof BlockSignalBase) {
			((BlockSignalBase) block.data).power(world, blockState, blockPos, level);
		}
	}
}
