package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockWaterloggable extends BlockExtension implements Waterloggable {

	public BlockWaterloggable(BlockSettings blockSettings) {
		super(blockSettings);
		setDefaultState2(getDefaultState2().with(new Property<>(SlabBlockExtension.WATERLOGGED), false));
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext itemPlacementContext) {
		return getDefaultState2().with(new Property<>(SlabBlockExtension.WATERLOGGED), itemPlacementContext.getWorld().getFluidState(itemPlacementContext.getBlockPos()).getFluid().data == Fluids.getWaterMapped().data);
	}

	@Nonnull
	@Override
	public FluidState getFluidState2(BlockState state) {
		return state.get(new Property<>(SlabBlockExtension.WATERLOGGED)) ? Fluids.getWaterMapped().getStill(false) : super.getFluidState2(state);
	}

	@Nonnull
	@Override
	public BlockState getStateForNeighborUpdate2(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(new Property<>(SlabBlockExtension.WATERLOGGED))) {
			scheduleFluidTick(World.cast(world), pos, Fluid.cast(Fluids.getWaterMapped()), 5);
		}

		return super.getStateForNeighborUpdate2(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(new Property<>(SlabBlockExtension.WATERLOGGED));
	}
}
