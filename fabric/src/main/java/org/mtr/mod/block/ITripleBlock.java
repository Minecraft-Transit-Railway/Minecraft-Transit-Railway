package org.mtr.mod.block;

import org.mtr.mapping.holder.*;

import java.util.function.Supplier;

public interface ITripleBlock extends IBlock {

	BooleanProperty ODD = BooleanProperty.of("odd");

	static BlockState updateShape(BlockState state, Direction direction, boolean isThis, Supplier<BlockState> getDefaultState) {
		final Direction sideDirection = IBlock.getSideDirection(state);
		if ((sideDirection == direction || sideDirection == direction.getOpposite() && IBlock.getStatePropertySafe(state, ODD)) && !isThis) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return getDefaultState.get();
		}
	}

	static void playerWillDestroy(World world, BlockPos pos, BlockState state, PlayerEntity player, boolean isTop) {
		BlockPos offsetPos = pos;
		if (isTop) {
			offsetPos = offsetPos.down();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.offset(IBlock.getSideDirection(state), IBlock.getStatePropertySafe(state, ODD) ? 1 : 2);
		}
		IBlock.onBreakCreative(world, player, offsetPos);
	}
}
