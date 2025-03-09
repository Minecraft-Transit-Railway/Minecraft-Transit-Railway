package org.mtr.block;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.mtr.core.tool.Vector;

import javax.annotation.Nonnull;

public abstract class BlockLiftTrackBase extends Block {

	public BlockLiftTrackBase(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		return getDefaultState().with(Properties.FACING, getFacing(context));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
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
			if (state.getBlock() instanceof BlockLiftTrackBase) {
				return IBlock.getStatePropertySafe(state, Properties.FACING);
			} else {
				return context.getHorizontalPlayerFacing();
			}
		}
	}
}
