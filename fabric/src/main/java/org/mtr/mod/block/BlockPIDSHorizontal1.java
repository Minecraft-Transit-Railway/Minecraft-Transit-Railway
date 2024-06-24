package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockPIDSHorizontal1 extends BlockPIDSHorizontalBase {

	private static final int MAX_ARRIVALS = 1;

	public BlockPIDSHorizontal1() {
		super(MAX_ARRIVALS);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 11, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 11, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityHorizontalBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(MAX_ARRIVALS, BlockEntityTypes.PIDS_HORIZONTAL_1.get(), pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return false;
		}
	}
}