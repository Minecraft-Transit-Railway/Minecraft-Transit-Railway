package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockPIDSVerticalBase extends BlockPIDSBase implements IBlock {

	public BlockPIDSVerticalBase(int maxArrivals) {
		super(maxArrivals, BlockPIDSVerticalBase::canStoreData, BlockPIDSVerticalBase::getBlockPosWithData);
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return DoubleVerticalBlock.getStateForNeighborUpdate(state, direction, neighborState.isOf(new Block(this)), super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos));
	}

	@Override
	public void onPlaced2(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		DoubleVerticalBlock.onPlaced(world, pos, state, getDefaultState2());
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return DoubleVerticalBlock.getPlacementState(ctx, getDefaultState2());
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		DoubleVerticalBlock.onBreak(world, pos, state, player);
		super.onBreak2(world, pos, state, player);
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable BlockView world, List<MutableText> tooltip, TooltipContext options) {
		final BlockEntityExtension blockEntity = createBlockEntity(new BlockPos(0, 0, 0), Blocks.getAirMapped().getDefaultState());
		if (blockEntity instanceof BlockEntityBase) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_ARRIVALS.getMutableText(maxArrivals / 2).formatted(TextFormatting.GRAY));
		}
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(HALF);
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
