package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockHelper;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockLiftTrack extends BlockExtension implements DirectionHelper {

	public BlockLiftTrack() {
		super(BlockHelper.createBlockSettings(true));
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction facing;
		final Direction oppositeFace = ctx.getSide().getOpposite();
		if (oppositeFace.getOffsetY() == 0) {
			facing = oppositeFace;
		} else {
			final BlockState state = ctx.getWorld().getBlockState(ctx.getBlockPos().offset(oppositeFace));
			if (state.getBlock().data instanceof BlockLiftTrack) {
				facing = IBlock.getStatePropertySafe(state, FACING);
			} else {
				facing = ctx.getPlayerFacing();
			}
		}
		return getDefaultState2().with(new Property<>(FACING.data), facing.data);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}
}
