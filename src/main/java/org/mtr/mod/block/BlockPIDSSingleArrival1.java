package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDSSingleArrival1 extends BlockPIDSBaseVertical {

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityVerticalBase {

		public static final int MAX_ARRIVALS = 1;
		public static final int LINES_PER_ARRIVAL = 16;
		private final BlockState state;

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PIDS_SINGLE_ARRIVAL_1.get(), pos, state);
			this.state = state;
		}

		@Override
		public int getMaxArrivals() {
			final boolean isBottom = IBlock.getStatePropertySafe(this.state, HALF) == DoubleBlockHalf.LOWER;
			return isBottom ? 0 : MAX_ARRIVALS;
		}

		@Override
		public int getLinesPerArrival() {
			return LINES_PER_ARRIVAL;
		}
	}
}
