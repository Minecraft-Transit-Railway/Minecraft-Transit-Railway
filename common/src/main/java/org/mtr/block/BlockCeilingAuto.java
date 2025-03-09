package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import javax.annotation.Nonnull;

public class BlockCeilingAuto extends BlockCeiling {

	public static final BooleanProperty LIGHT = BooleanProperty.of("light");

	public BlockCeilingAuto(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext itemPlacementContext) {
		final boolean facing = itemPlacementContext.getHorizontalPlayerFacing().getAxis() == Direction.Axis.X;
		return super.getPlacementState(itemPlacementContext).with(FACING, facing).with(LIGHT, hasLight(facing, itemPlacementContext.getBlockPos()));
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random).with(LIGHT, hasLight(IBlock.getStatePropertySafe(state, FACING), pos));
	}

	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
		final boolean light = hasLight(IBlock.getStatePropertySafe(state, FACING), pos);
		if (IBlock.getStatePropertySafe(state, LIGHT) != light) {
			world.setBlockState(pos, state.with(LIGHT, light));
		}
	}

	@Override
	public boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(LIGHT);
	}

	private static boolean hasLight(boolean facing, BlockPos pos) {
		if (facing) {
			return pos.getZ() % 3 == 0;
		} else {
			return pos.getX() % 3 == 0;
		}
	}
}
