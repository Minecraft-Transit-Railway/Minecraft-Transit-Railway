package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockDirectionalDoubleBlockBase extends Block implements IBlock {

	public BlockDirectionalDoubleBlockBase(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		return DoubleVerticalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(this), super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		DoubleVerticalBlock.onPlaced(world, pos, state, getAdditionalState(pos, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING)));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return DoubleVerticalBlock.getPlacementState(ctx, getAdditionalState(ctx.getBlockPos(), ctx.getHorizontalPlayerFacing()));
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleVerticalBlock.onBreak(world, pos, state, player);
		return super.onBreak(world, pos, state, player);
	}

	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return getDefaultState();
	}
}
