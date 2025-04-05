package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mod.Items;

import javax.annotation.Nonnull;

public abstract class BlockEscalatorBase extends BlockExtension implements IBlock, DirectionHelper {

	public static final EnumProperty<EnumEscalatorOrientation> ORIENTATION = EnumProperty.of("orientation", EnumEscalatorOrientation.class);

	protected BlockEscalatorBase() {
		super(org.mtr.mod.Blocks.createDefaultBlockSettings(true).nonOpaque());
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (getSideDirection(state) == direction && !neighborState.isOf(new Block(this))) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return state.with(new Property<>(ORIENTATION.data), getOrientation(BlockView.cast(world), pos, state));
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return super.getOutlineShape2(state, world, pos, context);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);

		if (orientation == EnumEscalatorOrientation.SLOPE || orientation == EnumEscalatorOrientation.TRANSITION_TOP) {
			return VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 8, 16), IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 15, 8, IBlock.getStatePropertySafe(state, FACING)));
		} else {
			return VoxelShapes.fullCube();
		}
	}

	@Nonnull
	@Override
	public Item asItem2() {
		return Items.ESCALATOR.get();
	}

	@Nonnull
	@Override
	public ItemStack getPickStack2(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(new ItemConvertible(asItem2().data));
	}

	protected final EnumEscalatorOrientation getOrientation(BlockView world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos posAhead = pos.offset(facing);
		final BlockPos posBehind = pos.offset(facing, -1);

		final boolean isAhead = isBlock(world, posAhead, state);
		final boolean isAheadUp = isBlock(world, posAhead.up(), state);

		final boolean isBehind = isBlock(world, posBehind, state);
		final boolean isBehindDown = isBlock(world, posBehind.down(), state);

		if (isAhead && isBehind) {
			return EnumEscalatorOrientation.FLAT;
		} else if (isAheadUp && isBehindDown) {
			return EnumEscalatorOrientation.SLOPE;
		} else if (isAheadUp && isBehind) {
			return EnumEscalatorOrientation.TRANSITION_BOTTOM;
		} else if (isAhead && isBehindDown) {
			return EnumEscalatorOrientation.TRANSITION_TOP;
		} else if (isBehind) {
			return EnumEscalatorOrientation.LANDING_TOP;
		} else {
			return EnumEscalatorOrientation.LANDING_BOTTOM;
		}
	}

	private Direction getSideDirection(BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
	}

	private static boolean isBlock(BlockView world, BlockPos blockPos, BlockState checkState) {
		try {
			return checkState.isOf(world.getBlockState(blockPos).getBlock());
		} catch (Exception ignored) {
			return false;
		}
	}

	public enum EnumEscalatorOrientation implements StringIdentifiable {

		LANDING_BOTTOM("landing_bottom"), LANDING_TOP("landing_top"), FLAT("flat"), SLOPE("slope"), TRANSITION_BOTTOM("transition_bottom"), TRANSITION_TOP("transition_top");
		private final String name;

		EnumEscalatorOrientation(String nameIn) {
			name = nameIn;
		}

		@Nonnull
		@Override
		public String asString2() {
			return name;
		}
	}
}
