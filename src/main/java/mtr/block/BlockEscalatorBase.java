package mtr.block;

import mtr.Items;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public abstract class BlockEscalatorBase extends HorizontalFacingBlock implements IBlock {

	public static final EnumProperty<EnumEscalatorOrientation> ORIENTATION = EnumProperty.of("orientation", EnumEscalatorOrientation.class);

	protected BlockEscalatorBase() {
		super(FabricBlockSettings.of(Material.METAL, MaterialColor.IRON).requiresTool().hardness(2).nonOpaque());
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (getSideDirection(state) == direction && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state.with(ORIENTATION, getOrientation(world, pos, state));
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);

		if (orientation == EnumEscalatorOrientation.SLOPE || orientation == EnumEscalatorOrientation.TRANSITION_TOP) {
			VoxelShape box = VoxelShapes.empty();
			for (int step = 0; step < 16; step++) {
				box = VoxelShapes.union(box, IBlock.getVoxelShapeByDirection(0, 0, 0, 16, step, 15 - step, state.get(FACING)));
			}
			return box;
		} else {
			return VoxelShapes.fullCube();
		}
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state;
	}

	@Override
	public Item asItem() {
		return Items.ESCALATOR;
	}

	@Override
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(asItem());
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	protected final EnumEscalatorOrientation getOrientation(BlockView world, BlockPos pos, BlockState state) {
		final Direction facing = state.get(FACING);

		final BlockPos posAhead = pos.offset(facing);
		final BlockPos posBehind = pos.offset(facing, -1);

		final boolean isAhead = is(world.getBlockState(posAhead).getBlock());
		final boolean isAheadUp = is(world.getBlockState(posAhead.up()).getBlock());

		final boolean isBehind = is(world.getBlockState(posBehind).getBlock());
		final boolean isBehindDown = is(world.getBlockState(posBehind.down()).getBlock());

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
		final Direction facing = state.get(FACING);
		return state.get(SIDE) == EnumSide.RIGHT ? facing.rotateYCounterclockwise() : facing.rotateYClockwise();
	}

	protected enum EnumEscalatorOrientation implements StringIdentifiable {

		LANDING_BOTTOM("landing_bottom"), LANDING_TOP("landing_top"), FLAT("flat"), SLOPE("slope"), TRANSITION_BOTTOM("transition_bottom"), TRANSITION_TOP("transition_top");
		private final String name;

		EnumEscalatorOrientation(String nameIn) {
			name = nameIn;
		}

		@Override
		public String asString() {
			return name;
		}
	}
}
