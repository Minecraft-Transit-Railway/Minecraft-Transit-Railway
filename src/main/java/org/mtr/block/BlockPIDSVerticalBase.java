package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.Nullable;

//? if >= 1.21.4 {
import net.minecraft.world.level.ScheduledTickAccess;
//? } else {
/*import net.minecraft.world.level.LevelAccessor;
 *///? }

public abstract class BlockPIDSVerticalBase extends BlockPIDSBase implements IBlock {

	public BlockPIDSVerticalBase(BlockBehaviour.Properties settings, int maxArrivals) {
		super(settings, maxArrivals, BlockPIDSVerticalBase::canStoreData, BlockPIDSVerticalBase::getBlockPosWithData);
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		return DoubleVerticalBlock.updateShape(state, direction, neighborState.is(this), super.updateShape(state, world, tickView, pos, direction, neighborPos, neighborState, random));
//? } else {
  /*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
		return DoubleVerticalBlock.updateShape(state, direction, neighborState.is(this), super.updateShape(state, direction, neighborState, world, pos, neighborPos));
*///? }
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		DoubleVerticalBlock.setPlacedBy(world, pos, state, defaultBlockState());
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return DoubleVerticalBlock.getStateForPlacement(ctx, defaultBlockState());
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		DoubleVerticalBlock.playerWillDestroy(world, pos, state, player);
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(HALF);
	}

	private static boolean canStoreData(Level world, BlockPos blockPos) {
		return IBlock.getStatePropertySafe(world, blockPos, HALF) == DoubleBlockHalf.UPPER;
	}

	private static BlockPos getBlockPosWithData(Level world, BlockPos blockPos) {
		if (canStoreData(world, blockPos)) {
			return blockPos;
		} else {
			return blockPos.above();
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
