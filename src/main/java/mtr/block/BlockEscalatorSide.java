package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public class BlockEscalatorSide extends BlockEscalatorBase {

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (direction == Direction.DOWN && !(world.getBlockState(pos.down()).getBlock() instanceof BlockEscalatorStep)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return super.getStateForNeighborUpdate(state, direction, newState, world, pos, posFrom);
		}
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.combineAndSimplify(getOutlineShape(state, world, pos, context), super.getCollisionShape(state, world, pos, context), BooleanBiFunction.AND);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);
		final boolean isBottom = orientation == EnumEscalatorOrientation.LANDING_BOTTOM;
		final boolean isTop = orientation == EnumEscalatorOrientation.LANDING_TOP;
		final boolean side = state.get(SIDE);

		switch (state.get(FACING)) {
			case NORTH:
				return Block.createCuboidShape(side ? 12 : 0, 0, isTop ? 8 : 0, side ? 16 : 4, 16, isBottom ? 8 : 16);
			case EAST:
				return Block.createCuboidShape(isBottom ? 8 : 0, 0, side ? 12 : 0, isTop ? 8 : 16, 16, side ? 16 : 4);
			case SOUTH:
				return Block.createCuboidShape(side ? 0 : 12, 0, isBottom ? 8 : 0, side ? 4 : 16, 16, isTop ? 8 : 16);
			case WEST:
				return Block.createCuboidShape(isTop ? 8 : 0, 0, side ? 0 : 12, isBottom ? 8 : 16, 16, side ? 4 : 16);
			default:
				return VoxelShapes.empty();
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, ORIENTATION, SIDE);
	}
}
