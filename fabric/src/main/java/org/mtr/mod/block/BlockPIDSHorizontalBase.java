package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPIDSHorizontalBase extends BlockPIDSBase {

	public BlockPIDSHorizontalBase(int maxArrivals) {
		super(maxArrivals, BlockPIDSHorizontalBase::canStoreData, BlockPIDSHorizontalBase::getBlockPosWithData);
	}


	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (IBlock.getStatePropertySafe(state, FACING) == direction && !neighborState.isOf(new Block(this))) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return state;
		}
	}

	@Override
	public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClient()) {
			final Direction direction = IBlock.getStatePropertySafe(state, FACING);
			world.setBlockState(pos.offset(direction), getDefaultState2().with(new Property<>(FACING.data), direction.getOpposite().data), 3);
			world.updateNeighbors(pos, Blocks.getAirMapped());
			state.updateNeighbors(new WorldAccess(world.data), pos, 3);
			// TODO copy NBT when copying block
		}
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction direction = ctx.getPlayerFacing().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? getDefaultState2().with(new Property<>(FACING.data), direction.data) : null;
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.onBreakCreative(world, player, pos.offset(facing));
		}
		super.onBreak2(world, pos, state, player);
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		tooltip.add(TranslationProvider.TOOLTIP_MTR_ARRIVALS.getMutableText(maxArrivals).formatted(TextFormatting.GRAY));
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	private static boolean canStoreData(World world, BlockPos blockPos) {
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, FACING);
		return facing == Direction.NORTH || facing == Direction.EAST;
	}

	private static BlockPos getBlockPosWithData(World world, BlockPos blockPos) {
		if (canStoreData(world, blockPos)) {
			return blockPos;
		} else {
			return blockPos.offset(IBlock.getStatePropertySafe(world, blockPos, FACING));
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
