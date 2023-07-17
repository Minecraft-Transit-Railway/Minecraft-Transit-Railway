package mtr.block;

import mtr.mappings.BlockDirectionalMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public abstract class BlockDirectionalDoubleBlockBase extends BlockDirectionalMapper implements IBlock {

	public BlockDirectionalDoubleBlockBase(Properties settings) {
		super(settings);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor world, BlockPos pos, BlockPos posFrom) {
		final boolean isTop = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER;
		if ((isTop && direction == Direction.DOWN || !isTop && direction == Direction.UP) && !newState.is(this)) {
			return Blocks.AIR.defaultBlockState();
		} else {
			return state;
		}
	}

	@Override
	public void setPlacedBy(Level world, BlockPos pos, BlockState state, LivingEntity livingEntity, ItemStack itemStack) {
		if (!world.isClientSide) {
			final Direction facing = IBlock.getStatePropertySafe(state, FACING);
			world.setBlock(pos.above(), getAdditionalState(pos, facing).setValue(FACING, facing).setValue(HALF, DoubleBlockHalf.UPPER), 3);
			world.updateNeighborsAt(pos, Blocks.AIR);
			state.updateNeighbourShapes(world, pos, 3);
		}
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		final Direction facing = ctx.getHorizontalDirection();
		return IBlock.isReplaceable(ctx, Direction.UP, 2) ? getAdditionalState(ctx.getClickedPos(), facing).setValue(FACING, facing).setValue(HALF, DoubleBlockHalf.LOWER) : null;
	}

	@Override
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if (IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER) {
			IBlock.onBreakCreative(world, player, pos.below());
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state;
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return state;
	}

	protected BlockState getAdditionalState(BlockPos pos, Direction facing) {
		return defaultBlockState();
	}
}
