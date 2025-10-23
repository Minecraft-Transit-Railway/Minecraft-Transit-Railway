package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalSemaphore1 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore1(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalSemaphore1BlockEntity(blockPos, blockState);
	}

	public static class SignalSemaphore1BlockEntity extends BlockEntityBase {

		public SignalSemaphore1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_SEMAPHORE_1.get(), false, pos, state);
		}
	}
}
