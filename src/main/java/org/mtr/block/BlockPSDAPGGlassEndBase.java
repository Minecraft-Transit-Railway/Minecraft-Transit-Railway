package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public abstract class BlockPSDAPGGlassEndBase extends BlockPSDAPGGlassBase {

	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_LEFT = EnumProperty.create("touching_left", EnumPSDAPGGlassEndSide.class);
	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_RIGHT = EnumProperty.create("touching_right", EnumPSDAPGGlassEndSide.class);

	public BlockPSDAPGGlassEndBase(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		final BlockState superState = super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random);
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		final BlockState superState = super.updateShape(state, direction, neighborState, world, pos, neighborPos);
*///? }
		if (superState.getBlock().equals(Blocks.AIR)) {
			return superState;
		} else {
			final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			final EnumPSDAPGGlassEndSide touchingLeft = getSideEnd(world, pos, facing.getCounterClockWise());
			final EnumPSDAPGGlassEndSide touchingRight = getSideEnd(world, pos, facing.getClockWise());
			return superState.setValue(TOUCHING_LEFT, touchingLeft).setValue(TOUCHING_RIGHT, touchingRight);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final VoxelShape superShape = super.getShape(state, world, pos, context);
		final int height = isAPG() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
		final boolean leftAir = IBlock.getStatePropertySafe(state, TOUCHING_LEFT) == EnumPSDAPGGlassEndSide.AIR;
		final boolean rightAir = IBlock.getStatePropertySafe(state, TOUCHING_RIGHT) == EnumPSDAPGGlassEndSide.AIR;
		return getEndOutlineShape(superShape, state, height, 4, leftAir, rightAir);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
		builder.add(SIDE_EXTENDED);
		builder.add(TOUCHING_LEFT);
		builder.add(TOUCHING_RIGHT);
	}

	private EnumPSDAPGGlassEndSide getSideEnd(LevelReader world, BlockPos pos, Direction offset) {
		final BlockPos checkPos = pos.relative(offset);
		if (world.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGDoorBase) {
			return EnumPSDAPGGlassEndSide.DOOR;
		} else if (world.getBlockState(checkPos).getBlock() instanceof BlockPSDAPGBase) {
			return EnumPSDAPGGlassEndSide.NONE;
		} else {
			return EnumPSDAPGGlassEndSide.AIR;
		}
	}

	public static VoxelShape getEndOutlineShape(VoxelShape baseShape, BlockState state, int height, int thickness, boolean leftAir, boolean rightAir) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);

		if (facing == Direction.NORTH && leftAir || facing == Direction.SOUTH && rightAir) {
			baseShape = Shapes.or(baseShape, Block.box(0, 0, 0, thickness, height, 16));
		}
		if (facing == Direction.EAST && leftAir || facing == Direction.WEST && rightAir) {
			baseShape = Shapes.or(baseShape, Block.box(0, 0, 0, 16, height, thickness));
		}
		if (facing == Direction.SOUTH && leftAir || facing == Direction.NORTH && rightAir) {
			baseShape = Shapes.or(baseShape, Block.box(16 - thickness, 0, 0, 16, height, 16));
		}
		if (facing == Direction.WEST && leftAir || facing == Direction.EAST && rightAir) {
			baseShape = Shapes.or(baseShape, Block.box(0, 0, 16 - thickness, 16, height, 16));
		}

		return baseShape;
	}

	public enum EnumPSDAPGGlassEndSide implements StringRepresentable {

		AIR("air"), DOOR("door"), NONE("none");
		private final String name;

		EnumPSDAPGGlassEndSide(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
