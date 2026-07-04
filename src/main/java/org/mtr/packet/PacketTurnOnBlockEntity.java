package org.mtr.packet;

import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.MTR;
import org.mtr.block.BlockSignalBase;
import org.mtr.block.BlockTrainPoweredSensorBase;

public final class PacketTurnOnBlockEntity extends PacketHandler {

	private final BlockPos blockPos;
	private final int level;

	public PacketTurnOnBlockEntity(PacketBufferReceiver packetBufferReceiver) {
		blockPos = BlockPos.of(packetBufferReceiver.readLong());
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
	public void runServer(MinecraftServer minecraftServer, ServerPlayer serverPlayerEntity) {
		final Level world = serverPlayerEntity.serverLevel();
		if (!MTR.isChunkLoaded(world, blockPos)) {
			return;
		}

		final BlockState blockState = world.getBlockState(blockPos);
		final Block block = blockState.getBlock();
		if (block instanceof BlockTrainPoweredSensorBase) {
			((BlockTrainPoweredSensorBase) block).power(world, blockState, blockPos);
		}
		if (block instanceof BlockSignalBase) {
			((BlockSignalBase) block).power(world, blockState, blockPos, level);
		}
	}
}
