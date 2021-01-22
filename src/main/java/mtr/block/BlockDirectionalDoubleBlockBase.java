package mtr.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class BlockDirectionalDoubleBlockBase extends HorizontalFacingBlock implements IBlock {

	public BlockDirectionalDoubleBlockBase(Settings settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom) {
		return IBlock.breakCheckTwoBlock(state, direction, newState, this);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.up(), getAdditionalState(pos, facing).with(FACING, facing).with(HALF, DoubleBlockHalf.UPPER), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction facing = ctx.getPlayerFacing();
		return IBlock.isReplaceable(ctx, Direction.UP, 2) ? getAdditionalState(ctx.getBlockPos(), facing).with(FACING, facing).with(HALF, DoubleBlockHalf.LOWER) : null;
	}

	@Override
	public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			IBlock.onBreakCreative(world, player, pos.down());
		}
		super.onBreak(world, pos, state, player);
	}

	@Override
	public BlockState rotate(BlockState state, BlockRotation rotation) {
		return state;
	}

	@Override
	public BlockState mirror(BlockState state, BlockMirror mirror) {
		return state;
	}

	@Override
	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.BLOCK;
	}

	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return getDefaultState();
	}
}
