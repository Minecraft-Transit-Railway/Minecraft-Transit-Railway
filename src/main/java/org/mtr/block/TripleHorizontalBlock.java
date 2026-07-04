package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jspecify.annotations.Nullable;

public interface TripleHorizontalBlock extends IBlock {

	/**
	 * This {@link BooleanProperty} will be {@code true} if this is the center block of the multi block structure; {@code false} otherwise.
	 * <br/>
	 * The multi-block structure will have the following states:
	 * <br/>
	 * {@code LF RT RF}
	 * <br/>
	 * where {@code L} is {@code SIDE = LEFT}, {@code R} is {@code SIDE = RIGHT},
	 * {@code F} is {@code CENTER = false}, and {@code T} is {@code CENTER = true}.
	 */
	BooleanProperty CENTER = BooleanProperty.create("odd");

	static BlockState updateShape(BlockState blockState, Direction direction, boolean isThis, BlockState defaultBlockState) {
		final Direction sideDirection = IBlock.getSideDirection(blockState);
		if ((sideDirection == direction || sideDirection == direction.getOpposite() && IBlock.getStatePropertySafe(blockState, CENTER)) && !isThis) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return defaultBlockState;
		}
	}

	static void setPlacedBy(Level world, BlockPos blockPos, BlockState blockState, BlockState defaultPlacementState) {
		if (!world.isClientSide()) {
			final Direction direction = IBlock.getStatePropertySafe(blockState, BlockStateProperties.HORIZONTAL_FACING);
			final Direction rotatedDirection = direction.getClockWise();
			final BlockState newBlockState = defaultPlacementState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction).setValue(SIDE, EnumSide.RIGHT);

			world.setBlock(blockPos.relative(rotatedDirection), newBlockState.setValue(CENTER, true), 3);
			world.blockUpdated(blockPos, Blocks.AIR);
			blockState.updateNeighbourShapes(world, blockPos, 3);

			world.setBlock(blockPos.relative(rotatedDirection, 2), newBlockState.setValue(CENTER, false), 3);
			world.blockUpdated(blockPos.relative(rotatedDirection), Blocks.AIR);
			blockState.updateNeighbourShapes(world, blockPos.relative(rotatedDirection), 3);
		}
	}

	@Nullable
	static BlockState getStateForPlacement(BlockPlaceContext itemPlacementContext, BlockState defaultPlacementState) {
		final Direction direction = itemPlacementContext.getHorizontalDirection();
		return IBlock.isReplaceable(itemPlacementContext, direction.getClockWise(), 3) ? defaultPlacementState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction).setValue(SIDE, EnumSide.LEFT).setValue(CENTER, false) : null;
	}

	static void playerWillDestroy(Level world, BlockPos blockPos, BlockState blockState, Player playerEntity) {
		final BlockPos breakBlockPos;
		if (IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT) {
			breakBlockPos = blockPos.relative(IBlock.getSideDirection(blockState), IBlock.getStatePropertySafe(blockState, CENTER) ? 1 : 2);
		} else {
			breakBlockPos = blockPos;
		}
		IBlock.playerWillDestroyCreative(world, playerEntity, breakBlockPos);
	}
}
