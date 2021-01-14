package mtr.block;

import mtr.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public interface IBlock {

	EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
	EnumProperty<EnumThird> THIRD = EnumProperty.of("third", EnumThird.class);
	EnumProperty<EnumSide> SIDE_EXTENDED = EnumProperty.of("side", EnumSide.class);
	EnumProperty<EnumSide> SIDE = EnumProperty.of("side", EnumSide.class, side -> side != EnumSide.MIDDLE && side != EnumSide.SINGLE);

	static ActionResult checkHoldingBrush(World world, PlayerEntity player, Runnable callbackBrush, Runnable callbackNoBrush) {
		if (player.isHolding(Items.BRUSH)) {
			if (!world.isClient) {
				callbackBrush.run();
			}
			return ActionResult.SUCCESS;
		} else {
			if (callbackNoBrush == null) {
				return ActionResult.FAIL;
			} else {
				if (!world.isClient) {
					callbackNoBrush.run();
					return ActionResult.CONSUME;
				} else {
					return ActionResult.SUCCESS;
				}
			}
		}
	}

	static ActionResult checkHoldingBrush(World world, PlayerEntity player, Runnable callbackBrush) {
		return checkHoldingBrush(world, player, callbackBrush, null);
	}

	static VoxelShape getVoxelShapeByDirection(double x1, double y1, double z1, double x2, double y2, double z2, Direction facing) {
		switch (facing) {
			case NORTH:
				return Block.createCuboidShape(x1, y1, z1, x2, y2, z2);
			case EAST:
				return Block.createCuboidShape(16 - z2, y1, x1, 16 - z1, y2, x2);
			case SOUTH:
				return Block.createCuboidShape(16 - x2, y1, 16 - z2, 16 - x1, y2, 16 - z1);
			case WEST:
				return Block.createCuboidShape(z1, y1, 16 - x2, z2, y2, 16 - x1);
			default:
				return VoxelShapes.fullCube();
		}
	}

	static boolean isReplaceable(ItemPlacementContext ctx, Direction direction, int totalLength) {
		for (int i = 0; i < totalLength; i++) {
			if (!ctx.getWorld().getBlockState(ctx.getBlockPos().offset(direction, i)).canReplace(ctx)) {
				return false;
			}
		}
		return true;
	}

	static void onBreakCreative(World world, PlayerEntity player, BlockPos pos) {
		if (!world.isClient && player.isCreative()) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState(), 35);
			final BlockState state = world.getBlockState(pos);
			world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(state));
		}
	}

	static Direction getSideDirection(BlockState state) {
		final Direction facing = state.get(HorizontalFacingBlock.FACING);
		return state.get(SIDE) == EnumSide.LEFT ? facing.rotateYClockwise() : facing.rotateYCounterclockwise();
	}

	enum EnumThird implements StringIdentifiable {

		LOWER("lower"), MIDDLE("middle"), UPPER("upper");
		private final String name;

		EnumThird(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}

	enum EnumSide implements StringIdentifiable {

		LEFT("left"), RIGHT("right"), MIDDLE("middle"), SINGLE("single");
		private final String name;

		EnumSide(String name) {
			this.name = name;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
