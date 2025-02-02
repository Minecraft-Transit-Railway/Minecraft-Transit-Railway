package org.mtr.mod.block;

import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.Blocks;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockLiftTrackBase extends BlockExtension implements DirectionHelper {

	public BlockLiftTrackBase() {
		super(Blocks.createDefaultBlockSettings(true));
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext context) {
		return getDefaultState2().with(new Property<>(FACING.data), getFacing(context).data);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public abstract ObjectArrayList<Direction> getConnectingDirections(BlockState blockState);

	public Vector getCenterPoint(BlockPos blockPos, BlockState blockState) {
		return new Vector(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	protected static Direction getFacing(ItemPlacementContext context) {
		final Direction oppositeFace = context.getSide().getOpposite();
		if (oppositeFace.getOffsetY() == 0) {
			return oppositeFace;
		} else {
			final BlockState state = context.getWorld().getBlockState(context.getBlockPos().offset(oppositeFace));
			if (state.getBlock().data instanceof BlockLiftTrackBase) {
				return IBlock.getStatePropertySafe(state, FACING);
			} else {
				return context.getPlayerFacing();
			}
		}
	}
}
