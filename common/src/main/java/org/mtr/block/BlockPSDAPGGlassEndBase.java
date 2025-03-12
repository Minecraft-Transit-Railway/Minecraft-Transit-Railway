package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import javax.annotation.Nonnull;

public abstract class BlockPSDAPGGlassEndBase extends BlockPSDAPGGlassBase {

	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_LEFT = EnumProperty.of("touching_left", EnumPSDAPGGlassEndSide.class);
	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_RIGHT = EnumProperty.of("touching_right", EnumPSDAPGGlassEndSide.class);

	public BlockPSDAPGGlassEndBase(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		final BlockState superState = super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
		if (superState.getBlock().equals(Blocks.AIR)) {
			return superState;
		} else {
			final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
			final EnumPSDAPGGlassEndSide touchingLeft = getSideEnd(world, pos, facing.rotateYCounterclockwise());
			final EnumPSDAPGGlassEndSide touchingRight = getSideEnd(world, pos, facing.rotateYClockwise());
			return superState.with(TOUCHING_LEFT, touchingLeft).with(TOUCHING_RIGHT, touchingRight);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final VoxelShape superShape = super.getOutlineShape(state, world, pos, context);
		final int height = isAPG() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
		final boolean leftAir = IBlock.getStatePropertySafe(state, TOUCHING_LEFT) == EnumPSDAPGGlassEndSide.AIR;
		final boolean rightAir = IBlock.getStatePropertySafe(state, TOUCHING_RIGHT) == EnumPSDAPGGlassEndSide.AIR;
		return getEndOutlineShape(superShape, state, height, 4, leftAir, rightAir);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(SIDE_EXTENDED);
		builder.add(TOUCHING_LEFT);
		builder.add(TOUCHING_RIGHT);
	}

	private EnumPSDAPGGlassEndSide getSideEnd(WorldView world, BlockPos pos, Direction offset) {
		final BlockPos checkPos = pos.offset(offset);
		if (world.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGDoorBase) {
			return EnumPSDAPGGlassEndSide.DOOR;
		} else if (world.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGBase) {
			return EnumPSDAPGGlassEndSide.NONE;
		} else {
			return EnumPSDAPGGlassEndSide.AIR;
		}
	}

	public static VoxelShape getEndOutlineShape(VoxelShape baseShape, BlockState state, int height, int thickness, boolean leftAir, boolean rightAir) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);

		if (facing == Direction.NORTH && leftAir || facing == Direction.SOUTH && rightAir) {
			baseShape = VoxelShapes.union(baseShape, Block.createCuboidShape(0, 0, 0, thickness, height, 16));
		}
		if (facing == Direction.EAST && leftAir || facing == Direction.WEST && rightAir) {
			baseShape = VoxelShapes.union(baseShape, Block.createCuboidShape(0, 0, 0, 16, height, thickness));
		}
		if (facing == Direction.SOUTH && leftAir || facing == Direction.NORTH && rightAir) {
			baseShape = VoxelShapes.union(baseShape, Block.createCuboidShape(16 - thickness, 0, 0, 16, height, 16));
		}
		if (facing == Direction.WEST && leftAir || facing == Direction.EAST && rightAir) {
			baseShape = VoxelShapes.union(baseShape, Block.createCuboidShape(0, 0, 16 - thickness, 16, height, 16));
		}

		return baseShape;
	}

	public enum EnumPSDAPGGlassEndSide implements StringIdentifiable {

		AIR("air"), DOOR("door"), NONE("none");
		private final String name;

		EnumPSDAPGGlassEndSide(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString() {
			return name;
		}
	}
}
