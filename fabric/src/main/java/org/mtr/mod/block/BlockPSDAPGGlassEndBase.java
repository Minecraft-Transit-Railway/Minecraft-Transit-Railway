package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockPSDAPGGlassEndBase extends BlockPSDAPGGlassBase {

	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_LEFT = EnumProperty.of("touching_left", EnumPSDAPGGlassEndSide.class);
	public static final EnumProperty<EnumPSDAPGGlassEndSide> TOUCHING_RIGHT = EnumProperty.of("touching_right", EnumPSDAPGGlassEndSide.class);

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		final BlockState superState = super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos);
		if (superState.getBlock().equals(Blocks.getAirMapped())) {
			return superState;
		} else {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			final EnumPSDAPGGlassEndSide touchingLeft = getSideEnd(world, pos, facing.rotateYCounterclockwise());
			final EnumPSDAPGGlassEndSide touchingRight = getSideEnd(world, pos, facing.rotateYClockwise());
			return superState.with(new Property<>(TOUCHING_LEFT.data), touchingLeft).with(new Property<>(TOUCHING_RIGHT.data), touchingRight);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final VoxelShape superShape = super.getOutlineShape2(state, world, pos, context);
		final int height = isAPG() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
		final boolean leftAir = IBlock.getStatePropertySafe(state, new Property<>(TOUCHING_LEFT.data)) == EnumPSDAPGGlassEndSide.AIR;
		final boolean rightAir = IBlock.getStatePropertySafe(state, new Property<>(TOUCHING_RIGHT.data)) == EnumPSDAPGGlassEndSide.AIR;
		return getEndOutlineShape(superShape, state, height, 4, leftAir, rightAir);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
		properties.add(SIDE_EXTENDED);
		properties.add(TOUCHING_LEFT);
		properties.add(TOUCHING_RIGHT);
	}

	private EnumPSDAPGGlassEndSide getSideEnd(WorldAccess world, BlockPos pos, Direction offset) {
		final BlockPos checkPos = pos.offset(offset);
		if (world.getBlockState(checkPos).getBlock().data instanceof BlockPSDAPGDoorBase) {
			return EnumPSDAPGGlassEndSide.DOOR;
		} else if (world.getBlockState(checkPos).getBlock().data instanceof BlockPSDAPGBase) {
			return EnumPSDAPGGlassEndSide.NONE;
		} else {
			return EnumPSDAPGGlassEndSide.AIR;
		}
	}

	public static VoxelShape getEndOutlineShape(VoxelShape baseShape, BlockState state, int height, int thickness,
												boolean leftAir, boolean rightAir) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

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
		public String asString2() {
			return name;
		}
	}
}
