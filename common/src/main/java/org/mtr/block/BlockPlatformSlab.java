package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockPlatformSlab extends SlabBlockExtension implements PlatformHelper {


	public BlockPlatformSlab(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		return PlatformHelper.getActualState(BlockView.cast(world), pos, state);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final BlockState slabState = super.getPlacementState2(ctx);
		return (slabState == null ? getDefaultState2() : slabState).with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		super.addBlockProperties(properties);
		properties.add(FACING);
		properties.add(DOOR_TYPE);
		properties.add(SIDE);
	}
}
