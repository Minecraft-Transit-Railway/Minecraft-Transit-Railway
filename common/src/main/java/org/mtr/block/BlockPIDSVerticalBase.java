package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class BlockPIDSVerticalBase extends BlockPIDSBase implements IBlock {

	public BlockPIDSVerticalBase(AbstractBlock.Settings settings, int maxArrivals) {
		super(settings, maxArrivals, BlockPIDSVerticalBase::canStoreData, BlockPIDSVerticalBase::getBlockPosWithData);
	}

	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		return DoubleVerticalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(this), super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random));
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		DoubleVerticalBlock.onPlaced(world, pos, state, getDefaultState());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return DoubleVerticalBlock.getPlacementState(ctx, getDefaultState());
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleVerticalBlock.onBreak(world, pos, state, player);
		return super.onBreak(world, pos, state, player);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(HALF);
	}

	private static boolean canStoreData(World world, BlockPos blockPos) {
		return IBlock.getStatePropertySafe(world, blockPos, HALF) == DoubleBlockHalf.UPPER;
	}

	private static BlockPos getBlockPosWithData(World world, BlockPos blockPos) {
		if (canStoreData(world, blockPos)) {
			return blockPos;
		} else {
			return blockPos.up();
		}
	}

	public abstract static class BlockEntityVerticalBase extends BlockEntityBase {

		public BlockEntityVerticalBase(int maxArrivals, BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(maxArrivals, BlockPIDSVerticalBase::canStoreData, BlockPIDSVerticalBase::getBlockPosWithData, type, pos, state);
		}

		@Override
		public boolean showArrivalNumber() {
			return false;
		}

		@Override
		public boolean alternateLines() {
			return true;
		}

		@Override
		public int textColorArrived() {
			return 0xFF9900;
		}

		@Override
		public int textColor() {
			return 0xFF9900;
		}
	}
}
