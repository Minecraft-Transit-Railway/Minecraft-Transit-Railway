package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockRouteSignStandingLight extends BlockRouteSignBase implements IBlock {

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(2, isLower ? 10 : 0, 0, 14, 16, 1, facing);
		final VoxelShape leg1 = IBlock.getVoxelShapeByDirection(1.5, 0, 0, 2, 16, 1, facing);
		final VoxelShape leg2 = IBlock.getVoxelShapeByDirection(14, 0, 0, 14.5, 16, 1, facing);
		if (isLower) {
			return VoxelShapes.union(main, VoxelShapes.union(leg1, leg2));
		} else {
			final VoxelShape light = IBlock.getVoxelShapeByDirection(1.5, 15, 0, 14.5, 16, 4, facing);
			return VoxelShapes.union(VoxelShapes.union(main, light), VoxelShapes.union(leg1, leg2));
		}
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT.get(), pos, state);
		}
	}
}
