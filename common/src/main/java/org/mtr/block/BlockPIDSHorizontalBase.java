package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPIDSHorizontalBase extends BlockPIDSBase {

	public BlockPIDSHorizontalBase(AbstractBlock.Settings settings, int maxArrivals) {
		super(settings, maxArrivals, BlockPIDSHorizontalBase::canStoreData, BlockPIDSHorizontalBase::getBlockPosWithData);
	}


	@Nonnull
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
		if (IBlock.getStatePropertySafe(state, Properties.FACING) == direction && !neighborState.isOf(this)) {
			return Blocks.AIR.getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(state, Properties.FACING);
			world.setBlockState(pos.offset(direction), getDefaultState().with(Properties.FACING, direction.getOpposite()), 3);
			world.updateNeighbors(pos, Blocks.AIR);
			state.updateNeighbors(world, pos, 3);
			// TODO copy NBT when copying block
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? getDefaultState().with(Properties.FACING, direction) : null;
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.offset(facing));
		}
		return super.onBreak(world, pos, state, player);
	}

	@Override
	public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_ARRIVALS.getMutableText(maxArrivals).formatted(Formatting.GRAY));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
	}

	private static boolean canStoreData(World world, BlockPos blockPos) {
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, Properties.FACING);
		return facing == Direction.NORTH || facing == Direction.EAST;
	}

	private static BlockPos getBlockPosWithData(World world, BlockPos blockPos) {
		if (canStoreData(world, blockPos)) {
			return blockPos;
		} else {
			return blockPos.offset(IBlock.getStatePropertySafe(world, blockPos, Properties.FACING));
		}
	}

	public abstract static class BlockEntityHorizontalBase extends BlockEntityBase {

		public BlockEntityHorizontalBase(int maxArrivals, BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(maxArrivals, BlockPIDSHorizontalBase::canStoreData, BlockPIDSHorizontalBase::getBlockPosWithData, type, pos, state);
		}

		@Override
		public boolean alternateLines() {
			return false;
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
