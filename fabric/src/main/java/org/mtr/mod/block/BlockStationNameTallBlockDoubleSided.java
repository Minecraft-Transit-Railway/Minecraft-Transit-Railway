package org.mtr.mod.block;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mod.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameTallBlockDoubleSided extends BlockStationNameTallBase {

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final ImmutablePair<Integer, Integer> bounds = getBounds(state);
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(2, bounds.getLeft(), 5, 14, bounds.getRight(), 11, IBlock.getStatePropertySafe(state, FACING)), BlockStationColorPole.getStationPoleShape());
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

		public BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get(), pos, state, 0.6875F, true);
		}
	}
}
