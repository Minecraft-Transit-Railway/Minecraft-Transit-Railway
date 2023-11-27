package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.DirectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface TripleHorizontalBlock extends IBlock, DirectionHelper {

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
	BooleanProperty CENTER = BooleanProperty.of("odd");

	@Nonnull
	static BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, boolean isThis, BlockState defaultBlockState) {
		final Direction sideDirection = IBlock.getSideDirection(blockState);
		if ((sideDirection == direction || sideDirection == direction.getOpposite() && IBlock.getStatePropertySafe(blockState, CENTER)) && !isThis) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return defaultBlockState;
		}
	}

	static void onPlaced(World world, BlockPos blockPos, BlockState blockState, BlockState defaultPlacementState) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(blockState, FACING);
			final Direction rotatedDirection = direction.rotateYClockwise();
			final BlockState newBlockState = defaultPlacementState.with(new Property<>(FACING.data), direction.data).with(new Property<>(SIDE.data), EnumSide.RIGHT);

			world.setBlockState(blockPos.offset(rotatedDirection), newBlockState.with(new Property<>(CENTER.data), true), 3);
			world.updateNeighbors(blockPos, Blocks.getAirMapped());
			blockState.updateNeighbors(new WorldAccess(world.data), blockPos, 3);

			world.setBlockState(blockPos.offset(rotatedDirection, 2), newBlockState.with(new Property<>(CENTER.data), false), 3);
			world.updateNeighbors(blockPos.offset(rotatedDirection), Blocks.getAirMapped());
			blockState.updateNeighbors(new WorldAccess(world.data), blockPos.offset(rotatedDirection), 3);
		}
	}

	@Nullable
	static BlockState getPlacementState(ItemPlacementContext itemPlacementContext, BlockState defaultPlacementState) {
		final Direction direction = itemPlacementContext.getPlayerFacing();
		return IBlock.isReplaceable(itemPlacementContext, direction.rotateYClockwise(), 3) ? defaultPlacementState.with(new Property<>(FACING.data), direction.data).with(new Property<>(SIDE.data), EnumSide.LEFT).with(new Property<>(CENTER.data), false) : null;
	}

	static void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity playerEntity) {
		final BlockPos breakBlockPos;
		if (IBlock.getStatePropertySafe(blockState, SIDE) == EnumSide.RIGHT) {
			breakBlockPos = blockPos.offset(IBlock.getSideDirection(blockState), IBlock.getStatePropertySafe(blockState, CENTER) ? 1 : 2);
		} else {
			breakBlockPos = blockPos;
		}
		IBlock.onBreakCreative(world, playerEntity, breakBlockPos);
	}
}
