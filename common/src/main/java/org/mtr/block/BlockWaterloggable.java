package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import javax.annotation.Nonnull;

public abstract class BlockWaterloggable extends Block implements Waterloggable {

	public BlockWaterloggable(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
		setDefaultState(getDefaultState().with(Properties.WATERLOGGED, false));
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		return getDefaultState().with(Properties.WATERLOGGED, itemPlacementContext.getWorld().getFluidState(itemPlacementContext.getBlockPos()).getFluid() == Fluids.WATER);
	}

	@Nonnull
	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (state.get(Properties.WATERLOGGED)) {
			tickView.scheduleFluidTick(pos, Fluids.WATER, 5);
		}

		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.WATERLOGGED);
	}
}
