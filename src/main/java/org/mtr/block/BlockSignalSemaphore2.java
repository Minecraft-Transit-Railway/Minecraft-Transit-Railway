package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockSignalSemaphore2 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore2(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalSemaphore2BlockEntity(blockPos, blockState);
	}

	public static class SignalSemaphore2BlockEntity extends BlockEntityBase {

		public SignalSemaphore2BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_SEMAPHORE_2.get(), true, pos, state);
		}
	}
}
