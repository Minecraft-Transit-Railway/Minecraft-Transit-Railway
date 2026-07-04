package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.mtr.core.tool.Vector;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public abstract class BlockLiftTrackBase extends Block {

	public BlockLiftTrackBase(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, getFacing(context));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}

	public abstract ObjectArrayList<Direction> getConnectingDirections(BlockState blockState);

	public Vector getCenterPoint(BlockPos blockPos, BlockState blockState) {
		return new Vector(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	protected static Direction getFacing(BlockPlaceContext context) {
		final Direction oppositeFace = context.getClickedFace().getOpposite();
		if (oppositeFace.getStepY() == 0) {
			return oppositeFace;
		} else {
			final BlockState state = context.getLevel().getBlockState(context.getClickedPos().relative(oppositeFace));
			if (state.getBlock() instanceof BlockLiftTrackBase) {
				return IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING);
			} else {
				return context.getHorizontalDirection();
			}
		}
	}
}
