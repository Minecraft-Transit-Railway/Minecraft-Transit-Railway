package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockSignalSemaphore1 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore1(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalSemaphore1BlockEntity(blockPos, blockState);
	}

	public static class SignalSemaphore1BlockEntity extends BlockEntityBase {

		public SignalSemaphore1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_SEMAPHORE_1.get(), false, pos, state);
		}
	}
}
