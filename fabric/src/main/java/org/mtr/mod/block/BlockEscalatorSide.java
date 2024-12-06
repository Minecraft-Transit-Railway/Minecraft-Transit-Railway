package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockEscalatorSide extends BlockEscalatorBase {

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (direction == Direction.DOWN && !(world.getBlockState(pos.down()).getBlock().data instanceof BlockEscalatorStep)) {
			return Blocks.getAirMapped().getDefaultState();
		} else {
			return super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos);
		}
	}

	@Nonnull
	@Override
	public VoxelShape getCullingShape2(BlockState state, BlockView world, BlockPos pos) {
		// Prevents culling optimization mods from culling our see-through escalator side
		return VoxelShapes.empty();
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.combine(getOutlineShape2(state, world, pos, context), super.getCollisionShape2(state, world, pos, context), BooleanBiFunction.getAndMapped());
	}

	@Nonnull
	@Override
	public VoxelShape getCameraCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public void onBreak2(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos offsetPos = pos.down();
		if (IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT) {
			offsetPos = offsetPos.offset(IBlock.getSideDirection(state));
		}
		IBlock.onBreakCreative(world, player, offsetPos);
		super.onBreak2(world, pos, state, player);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final EnumEscalatorOrientation orientation = getOrientation(world, pos, state);
		final boolean isBottom = orientation == EnumEscalatorOrientation.LANDING_BOTTOM;
		final boolean isTop = orientation == EnumEscalatorOrientation.LANDING_TOP;
		final boolean isRight = IBlock.getStatePropertySafe(state, SIDE) == EnumSide.RIGHT;
		return IBlock.getVoxelShapeByDirection(isRight ? 12 : 0, 0, isTop ? 8 : 0, isRight ? 16 : 4, 16, isBottom ? 8 : 16, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(ORIENTATION);
		properties.add(SIDE);
	}
}
