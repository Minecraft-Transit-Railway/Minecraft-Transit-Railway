package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.function.Supplier;

public interface ITripleBlock extends IBlock {

	BooleanProperty ODD = BooleanProperty.create("odd");

	static BlockState updateShape(BlockState state, Direction direction, boolean isThis, Supplier<BlockState> getDefaultState) {
		final Direction sideDirection = IBlock.getSideDirection(state);
		if ((sideDirection == direction || sideDirection == direction.getOpposite() && IBlock.getStatePropertySafe(state, ODD)) && !isThis) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return getDefaultState.get();
		}
	}

	static void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player, boolean isTop) {
		BlockPos offsetPos = pos;
		if (isTop) {
			offsetPos = offsetPos.below();
		}
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.relative(IBlock.getSideDirection(state), IBlock.getStatePropertySafe(state, ODD) ? 1 : 2);
		}
		IBlock.onBreakCreative(world, player, offsetPos);
	}
}
