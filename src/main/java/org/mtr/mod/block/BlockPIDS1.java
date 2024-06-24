package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDS1 extends BlockPIDSBaseHorizontal {

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 11, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 11, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityHorizontalBase {

		public static final int MAX_ARRIVALS = 1;
		public static final int LINES_PER_ARRIVAL = 1;

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.PIDS_1.get(), pos, state);
		}

		@Override
		public int getMaxArrivals() {
			return MAX_ARRIVALS;
		}
	}
}