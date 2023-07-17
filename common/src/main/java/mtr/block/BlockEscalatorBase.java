package mtr.block;

import mtr.Items;
import mtr.mappings.HorizontalBlockWithSoftLanding;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockEscalatorBase extends HorizontalBlockWithSoftLanding implements IBlock {

	public static final EnumProperty<EnumEscalatorOrientation> ORIENTATION = EnumProperty.create("orientation", EnumEscalatorOrientation.class);

	protected BlockEscalatorBase() {
		super(Properties.of().requiresCorrectToolForDrops().strength(2).noOcclusion());
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		if (getSideDirection(state) == direction && !newState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state.setValue(ORIENTATION, getOrientation(world, pos, state));
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext collisionContext) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);

		if (orientation == EnumEscalatorOrientation.SLOPE || orientation == EnumEscalatorOrientation.TRANSITION_TOP) {
			return Shapes.or(Block.box(0, 0, 0, 16, 8, 16), IBlock.getVoxelShapeByDirection(0, 8, 0, 16, 15, 8, IBlock.getStatePropertySafe(state, FACING)));
		} else {
			return Shapes.block();
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state;
	}

	@Override
	public Item asItem() {
		return Items.ESCALATOR.get();
	}

	@Override
	public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
		return new ItemStack(asItem());
	}

	protected final EnumEscalatorOrientation getOrientation(BlockGetter world, BlockPos pos, BlockState state) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);

		final BlockPos posAhead = pos.relative(facing);
		final BlockPos posBehind = pos.relative(facing, -1);

		final boolean isAhead = state.is(world.getBlockState(posAhead).getBlock());
		final boolean isAheadUp = state.is(world.getBlockState(posAhead.above()).getBlock());

		final boolean isBehind = state.is(world.getBlockState(posBehind).getBlock());
		final boolean isBehindDown = state.is(world.getBlockState(posBehind.below()).getBlock());

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
		return IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT ? facing.getCounterClockWise() : facing.getClockWise();
	}

	protected enum EnumEscalatorOrientation implements StringRepresentable {

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
