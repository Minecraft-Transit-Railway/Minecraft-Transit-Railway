package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
import org.mtr.registry.Items;

import javax.annotation.Nonnull;

public abstract class BlockEscalatorBase extends Block implements IBlock {

	public static final EnumProperty<EnumEscalatorOrientation> ORIENTATION = EnumProperty.of("orientation", EnumEscalatorOrientation.class);

	public BlockEscalatorBase(AbstractBlock.Settings settings) {
		super(settings.nonOpaque());
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (getSideDirection(state) == direction && !neighborState.isOf(this)) {
			return net.minecraft.block.Blocks.AIR.getDefaultState();
		} else {
			return state.with(ORIENTATION, getOrientation(world, pos, state));
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return super.getOutlineShape(state, world, pos, context);
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);

		if (orientation == EnumEscalatorOrientation.SLOPE || orientation == EnumEscalatorOrientation.TRANSITION_TOP) {
			return VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 8, 16), IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 15, 8, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING)));
		} else {
			return VoxelShapes.fullCube();
		}
	}

	@Nonnull
	@Override
	public Item asItem() {
		return Items.ESCALATOR.createAndGet();
	}

	@Override
	protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
		return new ItemStack(asItem());
	}

	protected final EnumEscalatorOrientation getOrientation(BlockView world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);

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
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
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
		public String asString() {
			return name;
		}
	}
}
