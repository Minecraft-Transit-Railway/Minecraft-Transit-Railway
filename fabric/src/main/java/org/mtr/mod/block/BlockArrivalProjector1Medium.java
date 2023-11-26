package org.mtr.mod.block;

import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.BlockState;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockArrivalProjector1Medium extends BlockArrivalProjectorBase {

	private static final int MAX_ARRIVALS = 12;

	public BlockArrivalProjector1Medium() {
		super(MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityArrivalProjectorBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM.get(), pos, state);
		}
	}
}
