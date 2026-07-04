package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.registry.Items;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public abstract class BlockEscalatorBase extends Block implements IBlock {

	public static final EnumProperty<EnumEscalatorOrientation> ORIENTATION = EnumProperty.create("orientation", EnumEscalatorOrientation.class);

	public BlockEscalatorBase(BlockBehaviour.Properties settings) {
		super(settings.noOcclusion());
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		if (getSideDirection(state) == direction && !neighborState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state.setValue(ORIENTATION, getOrientation(world, pos, state));
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return super.getShape(state, world, pos, context);
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);

		if (orientation == EnumEscalatorOrientation.SLOPE || orientation == EnumEscalatorOrientation.TRANSITION_TOP) {
			return Shapes.or(Block.box(0, 0, 0, 16, 8, 16), IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 15, 8, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING)));
		} else {
			return Shapes.block();
		}
	}

	@Override
	public Item asItem() {
		return Items.ESCALATOR.get();
	}

	@Override
//? if >= 1.21.4 {
	protected ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
//? } else {
	/*public ItemStack getCloneItemStack(LevelReader levelReader, BlockPos blockPos, BlockState blockState) {
//
*///? }
		return new ItemStack(asItem());
	}

	protected final EnumEscalatorOrientation getOrientation(BlockGetter world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);

		final BlockPos posAhead = pos.relative(facing);
		final BlockPos posBehind = pos.relative(facing, -1);

		final boolean isAhead = isBlock(world, posAhead, state);
		final boolean isAheadUp = isBlock(world, posAhead.above(), state);

		final boolean isBehind = isBlock(world, posBehind, state);
		final boolean isBehindDown = isBlock(world, posBehind.below(), state);

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
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		return IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.getCounterClockWise() : facing.getClockWise();
	}

	private static boolean isBlock(BlockGetter world, BlockPos blockPos, BlockState checkState) {
		try {
			return checkState.is(world.getBlockState(blockPos).getBlock());
		} catch (Exception ignored) {
			return false;
		}
	}

	public enum EnumEscalatorOrientation implements StringRepresentable {

		LANDING_BOTTOM("landing_bottom"), LANDING_TOP("landing_top"), FLAT("flat"), SLOPE("slope"), TRANSITION_BOTTOM("transition_bottom"), TRANSITION_TOP("transition_top");
		private final String name;

		EnumEscalatorOrientation(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
