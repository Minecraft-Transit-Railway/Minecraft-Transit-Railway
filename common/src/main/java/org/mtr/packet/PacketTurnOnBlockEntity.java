package org.mtr.packet;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.mtr.MTR;
import org.mtr.block.BlockTrainPoweredSensorBase;
import org.mtr.block.IBlock;

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
		final World world = serverPlayerEntity.getServerWorld();
		if (!MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();
		if (block instanceof BlockTrainPoweredSensorBase && (IBlock.getStatePropertySafe(blockState, BlockTrainPoweredSensorBase.POWERED) <= 1 || !world.getBlockTickScheduler().isQueued(blockPos, block))) {
			((BlockTrainPoweredSensorBase) block).power(world, blockState, blockPos);
		}
	}
}
