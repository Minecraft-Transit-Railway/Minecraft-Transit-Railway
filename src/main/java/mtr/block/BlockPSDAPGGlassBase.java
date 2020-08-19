package mtr.block;

import mtr.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public abstract class BlockPSDAPGGlassBase extends BlockPSDAPGBase {

	public static final EnumProperty<EnumPSDAPGGlassSide> SIDE = EnumProperty.of("side", EnumPSDAPGGlassSide.class);

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (player.isHolding(Items.BRUSH)) {
			for (int y = -1; y <= 1; y++) {
				BlockState scanState = world.getBlockState(pos.up(y));
				if (is(scanState.getBlock())) {
					connectGlass(world, pos.up(y), scanState);
				}
			}
			return ActionResult.SUCCESS;
		} else {
			return ActionResult.FAIL;
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, SIDE, TOP);
	}

	private void connectGlass(World world, BlockPos pos, BlockState state) {
		final Direction facing = state.get(FACING);

		final BlockPos leftPos = pos.offset(facing.rotateYCounterclockwise());
		final BlockState leftState = world.getBlockState(leftPos);
		final boolean leftValid = is(leftState.getBlock());

		if (leftValid) {
			final EnumPSDAPGGlassSide side = leftState.get(SIDE);
			EnumPSDAPGGlassSide newLeftSide;

			if (side == EnumPSDAPGGlassSide.RIGHT) {
				newLeftSide = EnumPSDAPGGlassSide.MIDDLE;
			} else if (side == EnumPSDAPGGlassSide.SINGLE) {
				newLeftSide = EnumPSDAPGGlassSide.LEFT;
			} else {
				newLeftSide = side;
			}

			world.setBlockState(leftPos, leftState.with(SIDE, newLeftSide));
		}

		final BlockPos rightPos = pos.offset(facing.rotateYClockwise());
		final BlockState rightState = world.getBlockState(rightPos);
		final boolean rightValid = is(rightState.getBlock());

		if (rightValid) {
			final EnumPSDAPGGlassSide side = rightState.get(SIDE);
			EnumPSDAPGGlassSide newRightSide;

			if (side == EnumPSDAPGGlassSide.LEFT) {
				newRightSide = EnumPSDAPGGlassSide.MIDDLE;
			} else if (side == EnumPSDAPGGlassSide.SINGLE) {
				newRightSide = EnumPSDAPGGlassSide.RIGHT;
			} else {
				newRightSide = side;
			}

			world.setBlockState(rightPos, rightState.with(SIDE, newRightSide));
		}

		EnumPSDAPGGlassSide newSide;
		if (leftValid && rightValid) {
			newSide = EnumPSDAPGGlassSide.MIDDLE;
		} else if (leftValid) {
			newSide = EnumPSDAPGGlassSide.RIGHT;
		} else if (rightValid) {
			newSide = EnumPSDAPGGlassSide.LEFT;
		} else {
			newSide = EnumPSDAPGGlassSide.SINGLE;
		}

		world.setBlockState(pos, state.with(SIDE, newSide));
	}

	public enum EnumPSDAPGGlassSide implements StringIdentifiable {

		LEFT("left"), RIGHT("right"), MIDDLE("middle"), SINGLE("single");

		private final String name;

		EnumPSDAPGGlassSide(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
