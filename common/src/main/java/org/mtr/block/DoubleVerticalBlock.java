package org.mtr.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface DoubleVerticalBlock extends IBlock {

	@Nonnull
	static BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, boolean isThis, BlockState defaultBlockState) {
		final boolean isTop = IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER;
		if ((isTop && direction == Direction.DOWN || !isTop && direction == Direction.UP) && !isThis) {
			return Blocks.AIR.getDefaultState();
		} else {
			return defaultBlockState;
		}
	}

	static void onPlaced(World world, BlockPos blockPos, BlockState blockState, BlockState defaultPlacementState) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(blockState, Properties.FACING);
			world.setBlockState(blockPos.up(), defaultPlacementState.with(Properties.FACING, direction).with(HALF, DoubleBlockHalf.UPPER), 3);
			world.updateNeighbors(blockPos, Blocks.AIR);
			blockState.updateNeighbors(world, blockPos, 3);
		}
	}

	@Nullable
	static BlockState getPlacementState(ItemPlacementContext itemPlacementContext, BlockState defaultPlacementState) {
		final Direction direction = itemPlacementContext.getHorizontalPlayerFacing();
		return IBlock.isReplaceable(itemPlacementContext, Direction.UP, 2) ? defaultPlacementState.with(Properties.FACING, direction).with(HALF, DoubleBlockHalf.LOWER) : null;
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
