package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mod.Blocks;

public class BlockStationColorGlass extends BlockStationColor {

	public BlockStationColorGlass() {
		super(Blocks.createDefaultBlockSettings(false).nonOpaque());
	}

	@Override
	public boolean isSideInvisible2(BlockState state, BlockState neighborState, Direction direction) {
		return neighborState.getBlock().data instanceof BlockStationColorGlass || (neighborState.getBlock().data instanceof BlockStationColorGlassSlab && SlabBlockExtension.getType(neighborState) == SlabType.DOUBLE) || super.isSideInvisible2(state, neighborState, direction);
	}

	@Override
	public float getAmbientOcclusionLightLevel2(BlockState state, BlockView world, BlockPos pos) {
		return 1;
	}

	@Override
	public boolean isTranslucent2(BlockState state, BlockView world, BlockPos pos) {
		return true;
	}
}
