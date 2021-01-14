package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public abstract class BlockPSDAPGGlassEndBase extends BlockPSDAPGGlassBase {

	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_LEFT = EnumProperty.of("touching_left", EnumPSDAPGGlassEndSide.class);
	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_RIGHT = EnumProperty.of("touching_right", EnumPSDAPGGlassEndSide.class);

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		BlockState superState = super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		if (superState.getBlock() == Blocks.AIR) {
			return superState;
		} else {
			final Direction facing = state.get(FACING);
			final EnumPSDAPGGlassEndSide touchingLeft = getSideEnd(world, pos, facing.rotateYCounterclockwise());
			final EnumPSDAPGGlassEndSide touchingRight = getSideEnd(world, pos, facing.rotateYClockwise());
			return superState.with(TOUCHING_LEFT, touchingLeft).with(TOUCHING_RIGHT, touchingRight);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape superShape = super.getOutlineShape(state, world, pos, context);
		final boolean leftAir = state.get(TOUCHING_LEFT) == EnumPSDAPGGlassEndSide.AIR;
		final boolean rightAir = state.get(TOUCHING_RIGHT) == EnumPSDAPGGlassEndSide.AIR;
		final Direction facing = state.get(FACING);
		final double height = isAPG() && state.get(HALF) == DoubleBlockHalf.UPPER ? 9 : 16;

		if (facing == Direction.NORTH && leftAir || facing == Direction.SOUTH && rightAir) {
			superShape = VoxelShapes.union(superShape, Block.createCuboidShape(0, 0, 0, 4, height, 16));
		}
		if (facing == Direction.EAST && leftAir || facing == Direction.WEST && rightAir) {
			superShape = VoxelShapes.union(superShape, Block.createCuboidShape(0, 0, 0, 16, height, 4));
		}
		if (facing == Direction.SOUTH && leftAir || facing == Direction.NORTH && rightAir) {
			superShape = VoxelShapes.union(superShape, Block.createCuboidShape(12, 0, 0, 16, height, 16));
		}
		if (facing == Direction.WEST && leftAir || facing == Direction.EAST && rightAir) {
			superShape = VoxelShapes.union(superShape, Block.createCuboidShape(0, 0, 12, 16, height, 16));
		}

		return superShape;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, HALF, SIDE_EXTENDED, TOUCHING_LEFT, TOUCHING_RIGHT);
	}

	private EnumPSDAPGGlassEndSide getSideEnd(BlockView world, BlockPos pos, Direction offset) {
		final BlockPos checkPos = pos.offset(offset);
		if (world.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGDoorBase) {
			return EnumPSDAPGGlassEndSide.DOOR;
		} else if (world.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGBase) {
			return EnumPSDAPGGlassEndSide.NONE;
		} else {
			return EnumPSDAPGGlassEndSide.AIR;
		}
	}

	public enum EnumPSDAPGGlassEndSide implements StringIdentifiable {

		AIR("air"), DOOR("door"), NONE("none");
		private final String name;

		EnumPSDAPGGlassEndSide(String nameIn) {
			name = nameIn;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
