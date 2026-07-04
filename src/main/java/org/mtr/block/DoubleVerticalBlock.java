package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

public interface DoubleVerticalBlock extends IBlock {

	static BlockState updateShape(BlockState blockState, Direction direction, boolean isThis, BlockState defaultBlockState) {
		final boolean isTop = IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER;
		if ((isTop && direction == Direction.DOWN || !isTop && direction == Direction.UP) && !isThis) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return defaultBlockState;
		}
	}

	static void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, BlockState defaultPlacementState) {
		if (!world.isClientSide()) {
			final Direction direction = IBlock.getStatePropertySafe(blockState, BlockStateProperties.HORIZONTAL_FACING);
			world.setBlock(blockPos.above(), defaultPlacementState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction).setValue(HALF, DoubleBlockHalf.UPPER), 3);
			world.blockUpdated(blockPos, Blocks.AIR);
			blockState.updateNeighbourShapes(world, blockPos, 3);
		}
	}

	@Nullable
	static BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext, BlockState defaultPlacementState) {
		final Direction direction = itemPlacementContext.getHorizontalDirection();
		return IBlock.isReplaceable(itemPlacementContext, Direction.UP, 2) ? defaultPlacementState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction).setValue(HALF, DoubleBlockHalf.LOWER) : null;
	}

	static void playerWillDestroy(Level world, BlockPos blockPos, BlockState blockState, Player playerEntity) {
		final BlockPos breakBlockPos;
		if (IBlock.getStatePropertySafe(blockState, HALF) == DoubleBlockHalf.UPPER) {
			breakBlockPos = blockPos.below();
		} else {
			breakBlockPos = blockPos;
		}
		IBlock.playerWillDestroyCreative(world, playerEntity, breakBlockPos);
	}
}
