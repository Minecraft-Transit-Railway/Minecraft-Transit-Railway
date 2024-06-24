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
		final boolean isTop = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		if ((isTop && direction == Direction.DOWN || !isTop && direction == Direction.UP) && !neighborState.isOf(new Block(this))) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.up(), getAdditionalState(pos, facing).with(new Property<>(FACING.data), facing.data).with(new Property<>(HALF.data), DoubleBlockHalf.UPPER), 3);
			world.updateNeighbors(pos, Blocks.getAirMapped());
			state.updateNeighbors(new WorldAccess(world.data), pos, 3);
		}
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction facing = ctx.getPlayerFacing();
		return IBlock.isReplaceable(ctx, Direction.UP, 2) ? getAdditionalState(ctx.getBlockPos(), facing).with(new Property<>(FACING.data), facing.data).with(new Property<>(HALF.data), DoubleBlockHalf.LOWER) : null;
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			IBlock.onBreakCreative(world, player, pos.down());
		}
		super.onBreak2(world, pos, state, player);
	}

	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return getDefaultState2();
	}
}
