package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockDirectionalDoubleBlockBase extends BlockExtension implements IBlock, DirectionHelper {

	public BlockDirectionalDoubleBlockBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return DoubleVerticalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(new Block(this)), super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos));
	}

	@Override
	public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		DoubleVerticalBlock.onPlaced(world, pos, state, getAdditionalState(pos, IBlock.getStatePropertySafe(state, FACING)));
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return DoubleVerticalBlock.getPlacementState(ctx, getAdditionalState(ctx.getBlockPos(), ctx.getPlayerFacing()));
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleVerticalBlock.onBreak(world, pos, state, player);
		super.onBreak2(world, pos, state, player);
	}

	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return getDefaultState2();
	}
}
