package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockRouteSignWallLight extends BlockRouteSignBase implements IBlock {

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(1.5, isLower ? 10 : 0, 0, 14.5, 16, 1, facing);
		if (isLower) {
			return main;
		} else {
			final VoxelShape light = IBlock.getVoxelShapeByDirection(1.5, 15, 0, 14.5, 16, 4, facing);
			return VoxelShapes.union(main, light);
		}
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT.get(), pos, state);
		}
	}
}
