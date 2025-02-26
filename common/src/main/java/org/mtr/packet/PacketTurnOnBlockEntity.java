package org.mtr.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.registry.PacketHandler;
import org.mtr.mapping.tool.PacketBufferReceiver;
import org.mtr.mapping.tool.PacketBufferSender;
import org.mtr.mod.Init;
import org.mtr.mod.block.BlockTrainPoweredSensorBase;
import org.mtr.mod.block.IBlock;

public final class PacketTurnOnBlockEntity extends PacketHandler {

	private final BlockPos blockPos;

	public PacketTurnOnBlockEntity(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.fromLong(packetBufferReceiver.readLong());
	}

	public PacketTurnOnBlockEntity(BlockPos blockPos) {
		this.blockPos = blockPos;
	}

	@Override
	public void write(PacketBufferSender packetBufferSender) {
		packetBufferSender.writeLong(blockPos.asLong());
	}

	@Override
	public void runServer(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity) {
		final World world = new World(serverPlayerEntity.getServerWorld().data);
		if (!Init.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();
		if (block.data instanceof BlockTrainPoweredSensorBase && (IBlock.getStatePropertySafe(blockState, BlockTrainPoweredSensorBase.POWERED) <= 1 || !BlockExtension.hasScheduledBlockTick(world, blockPos, block))) {
			((BlockTrainPoweredSensorBase) block.data).power(world, blockState, blockPos);
		}
	}
}
