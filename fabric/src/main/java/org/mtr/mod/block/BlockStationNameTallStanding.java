package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameTallStanding extends BlockStationNameTallBase {

	public static final float WIDTH = 0.6875F;
	public static final float HEIGHT = 1;

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		switch (IBlock.getStatePropertySafe(state, new Property<>(THIRD.data))) {
			case LOWER:
				final VoxelShape shape1 = IBlock.getVoxelShapeByDirection(1, 0, 0, 2, 16, 1, IBlock.getStatePropertySafe(state, FACING));
				final VoxelShape shape2 = IBlock.getVoxelShapeByDirection(14, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, FACING));
				return VoxelShapes.union(shape1, shape2);
			case MIDDLE:
				return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, FACING));
			case UPPER:
				return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 6, 1, IBlock.getStatePropertySafe(state, FACING));
			default:
				return VoxelShapes.empty();
		}
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data).with(new Property<>(METAL.data), true).with(new Property<>(THIRD.data), EnumThird.LOWER) : null;
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityTallBase {

		public BlockEntity(BlockPos blockPos, BlockState blockState) {
			super(BlockEntityTypes.STATION_NAME_TALL_STANDING.get(), blockPos, blockState, 0.07F, false);
		}
	}
}
