package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BlockPSDAPGGlassBase extends BlockPSDAPGBase {

	@Override
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			for (int y = -1; y <= 1; y++) {
				final BlockState scanState = world.getBlockState(pos.above(y));
				if (state.is(scanState.getBlock())) {
					connectGlass(world, pos.above(y), scanState);
				}
			}
		});
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SIDE_EXTENDED);
	}

	private void connectGlass(Level world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos leftPos = pos.relative(facing.getCounterClockWise());
		final BlockState leftState = world.getBlockState(leftPos);
		final boolean leftValid = state.is(leftState.getBlock());

		if (leftValid) {
			final EnumSide side = IBlock.getStatePropertySafe(leftState, SIDE_EXTENDED);
			EnumSide newLeftSide;

			if (side == EnumSide.RIGHT) {
				newLeftSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newLeftSide = EnumSide.LEFT;
			} else {
				newLeftSide = side;
			}

			world.setBlockAndUpdate(leftPos, leftState.setValue(SIDE_EXTENDED, newLeftSide));
		}

		final BlockPos rightPos = pos.relative(facing.getClockWise());
		final BlockState rightState = world.getBlockState(rightPos);
		final boolean rightValid = state.is(rightState.getBlock());

		if (rightValid) {
			final EnumSide side = IBlock.getStatePropertySafe(rightState, SIDE_EXTENDED);
			EnumSide newRightSide;

			if (side == EnumSide.LEFT) {
				newRightSide = EnumSide.MIDDLE;
			} else if (side == EnumSide.SINGLE) {
				newRightSide = EnumSide.RIGHT;
			} else {
				newRightSide = side;
			}

			world.setBlockAndUpdate(rightPos, rightState.setValue(SIDE_EXTENDED, newRightSide));
		}

		EnumSide newSide;
		if (leftValid && rightValid) {
			newSide = EnumSide.MIDDLE;
		} else if (leftValid) {
			newSide = EnumSide.RIGHT;
		} else if (rightValid) {
			newSide = EnumSide.LEFT;
		} else {
			newSide = EnumSide.SINGLE;
		}

		world.setBlockAndUpdate(pos, state.setValue(SIDE_EXTENDED, newSide));
	}
}
