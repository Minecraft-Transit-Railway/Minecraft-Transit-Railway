package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockPSDAPGGlassBase extends BlockPSDAPGBase {

	@Nonnull
	@Override
	public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		return IBlock.checkHoldingBrush(world, player, () -> {
			for (int y = -1; y <= 1; y++) {
				final BlockState scanState = world.getBlockState(pos.up(y));
				if (state.isOf(scanState.getBlock())) {
					connectGlass(world, pos.up(y), scanState);
				}
			}
		});
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
		properties.add(SIDE_EXTENDED);
	}

	private void connectGlass(World world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
		final BlockState leftState = world.getBlockState(leftPos);
		final boolean leftValid = state.isOf(leftState.getBlock());

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

			world.setBlockState(leftPos, leftState.with(new Property<>(SIDE_EXTENDED.data), newLeftSide));
		}

		final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
		final BlockState rightState = world.getBlockState(rightPos);
		final boolean rightValid = state.isOf(rightState.getBlock());

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

			world.setBlockState(rightPos, rightState.with(new Property<>(SIDE_EXTENDED.data), newRightSide));
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

		world.setBlockState(pos, state.with(new Property<>(SIDE_EXTENDED.data), newSide));
	}
}
