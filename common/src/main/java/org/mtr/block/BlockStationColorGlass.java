package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class BlockStationColorGlass extends BlockStationColor {

	public BlockStationColorGlass(AbstractBlock.Settings settings) {
		super(settings.nonOpaque());
	}

	@Override
	public boolean isSideInvisible(BlockState state, BlockState neighborState, Direction direction) {
		return neighborState.getBlock() instanceof BlockStationColorGlass || (neighborState.getBlock() instanceof BlockStationColorGlassSlab && neighborState.get(SlabBlock.TYPE) == SlabType.DOUBLE) || super.isSideInvisible(state, neighborState, direction);
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1;
	}

	@Override
	protected boolean isTransparent(BlockState state) {
		return true;
	}
}
