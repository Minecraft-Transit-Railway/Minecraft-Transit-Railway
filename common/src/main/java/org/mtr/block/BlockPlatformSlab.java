package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import javax.annotation.Nonnull;

public class BlockPlatformSlab extends SlabBlock implements PlatformHelper {


	public BlockPlatformSlab(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		return PlatformHelper.getActualState(world, pos, state);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final BlockState slabState = super.getPlacementState(ctx);
		return (slabState == null ? getDefaultState() : slabState).with(Properties.FACING, ctx.getHorizontalPlayerFacing());
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(Properties.FACING);
		builder.add(DOOR_TYPE);
		builder.add(SIDE);
	}
}
