package mtr.block;

import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class BlockPIDS1 extends HorizontalFacingBlock {

	public BlockPIDS1(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 16, 16, state.get(FACING));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		if (state.get(FACING) == direction && !newState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction direction = ctx.getPlayerFacing();
		return IBlock.isReplaceable(ctx, direction, 2) ? getDefaultState().with(FACING, direction) : null;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = state.get(FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.offset(facing));
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			final Direction direction = state.get(FACING);
			world.setBlockState(pos.offset(direction), getDefaultState().with(FACING, direction.getOpposite()), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
