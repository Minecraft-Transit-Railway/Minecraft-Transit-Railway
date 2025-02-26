package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DoubleVerticalBlock extends IBlock, DirectionHelper {

	@Nonnull
	static BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, boolean isThis, BlockState defaultBlockState) {
		final boolean isTop = IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER;
		if ((isTop && direction == Direction.DOWN || !isTop && direction == Direction.UP) && !isThis) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return defaultBlockState;
		}
	}

	static void onPlaced(World world, BlockPos blockPos, BlockState blockState, BlockState defaultPlacementState) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(blockState, FACING);
			world.setBlockState(blockPos.up(), defaultPlacementState.with(new Property<>(FACING.data), direction.data).with(new Property<>(HALF.data), DoubleBlockHalf.UPPER), 3);
			world.updateNeighbors(blockPos, Blocks.getAirMapped());
			blockState.updateNeighbors(new WorldAccess(world.data), blockPos, 3);
		}
	}

	@Nullable
	static BlockState getPlacementState(ItemPlacementContext itemPlacementContext, BlockState defaultPlacementState) {
		final Direction direction = itemPlacementContext.getPlayerFacing();
		return IBlock.isReplaceable(itemPlacementContext, Direction.UP, 2) ? defaultPlacementState.with(new Property<>(FACING.data), direction.data).with(new Property<>(HALF.data), DoubleBlockHalf.LOWER) : null;
	}

	static void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
		final BlockPos breakBlockPos;
		if (IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER) {
			breakBlockPos = blockPos.down();
		} else {
			breakBlockPos = blockPos;
		}
		IBlock.onBreakCreative(world, playerEntity, breakBlockPos);
	}
}
