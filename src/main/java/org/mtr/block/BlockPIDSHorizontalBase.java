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
import net.minecraft.world.level.block.Blocks;
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

public abstract class BlockPIDSHorizontalBase extends BlockPIDSBase {

	public BlockPIDSHorizontalBase(BlockBehaviour.Properties settings, int maxArrivals) {
		super(settings, maxArrivals, BlockPIDSHorizontalBase::canStoreData, BlockPIDSHorizontalBase::getBlockPosWithData);
	}

	@Override
//? if >= 1.21.4 {
	protected BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
//? } else {
	/*protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
//
*///? }
		if (IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING) == direction && !neighborState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		if (!world.isClientSide()) {
			final Direction direction = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			world.setBlock(pos.relative(direction), defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 3);
			world.blockUpdated(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
			// TODO copy NBT when copying block
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction direction = ctx.getHorizontalDirection().getOpposite();
		return IBlock.isReplaceable(ctx, direction, 2) ? defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction) : null;
	}

	@Override
	public BlockState playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		final Direction facing = IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
		if (facing == Direction.SOUTH || facing == Direction.WEST) {
			IBlock.playerWillDestroyCreative(world, player, pos.relative(facing));
		}
		return super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	private static boolean canStoreData(Level world, BlockPos blockPos) {
		final Direction facing = IBlock.getStatePropertySafe(world, blockPos, BlockStateProperties.HORIZONTAL_FACING);
		return facing == Direction.NORTH || facing == Direction.EAST;
	}

	private static BlockPos getBlockPosWithData(Level world, BlockPos blockPos) {
		if (canStoreData(world, blockPos)) {
			return blockPos;
		} else {
			return blockPos.relative(IBlock.getStatePropertySafe(world, blockPos, BlockStateProperties.HORIZONTAL_FACING));
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
