package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockRouteSignWallMetal extends BlockRouteSignBase implements IBlock {

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isBottom = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		return IBlock.getVoxelShapeByDirection(2, isBottom ? 10 : 0, 0, 14, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_WALL_METAL.get(), pos, state);
		}
	}
}
