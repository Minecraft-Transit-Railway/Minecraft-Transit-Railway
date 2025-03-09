package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockSignalSemaphore2 extends BlockSignalSemaphoreBase {

	public BlockSignalSemaphore2(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SignalSemaphore2BlockEntity(blockPos, blockState);
	}

	public static class SignalSemaphore2BlockEntity extends BlockEntityBase {

		public SignalSemaphore2BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.SIGNAL_SEMAPHORE_2.createAndGet(), true, pos, state);
		}
	}
}
