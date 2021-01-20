package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.StateManager;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
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
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos offsetPos = pos.down();
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.offset(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		super.onBreak(world, pos, state, player);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);
		final boolean isBottom = orientation == EnumEscalatorOrientation.LANDING_BOTTOM;
		final boolean isTop = orientation == EnumEscalatorOrientation.LANDING_TOP;
		final boolean isRight = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
		return IBlock.getVoxelShapeByDirection(isRight ? 12 : 0, 0, isTop ? 8 : 0, isRight ? 16 : 4, 16, isBottom ? 8 : 16, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, ORIENTATION, SIDE);
	}
}
