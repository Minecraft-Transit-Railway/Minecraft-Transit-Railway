package org.mtr.mod.block;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameTallWall extends BlockStationNameTallBase {

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final ImmutablePair<Integer, Integer> bounds = getBounds(state);
		return IBlock.getVoxelShapeByDirection(2, bounds.getLeft(), 0, 14, bounds.getRight(), 0.5, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction blockSide = ctx.getSide();
		final Direction facing = blockSide == Direction.UP || blockSide == Direction.DOWN ? ctx.getPlayerFacing() : blockSide.getOpposite();
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState2().with(new Property<>(FACING.data), facing.data).with(new Property<>(METAL.data), true).with(new Property<>(THIRD.data), EnumThird.LOWER) : null;
	}

	@Nonnull
	@Override
	public BlockEntityExtension createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new BlockEntity(blockPos, blockState);
	}

	public static class BlockEntity extends BlockEntityTallBase {

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_TALL_WALL.get(), pos, state, 0.03125F, false);
		}
	}
}
