package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.SlabBlockExtension;
import org.mtr.mod.Blocks;

public class BlockStationColorGlassSlab extends BlockStationColorSlab {

	public BlockStationColorGlassSlab() {
		super(Blocks.createDefaultBlockSettings(false).nonOpaque());
	}

	@Override
	public boolean isSideInvisible2(BlockState state, BlockState neighborState, Direction direction) {
		if (neighborState.getBlock().data instanceof BlockStationColorGlassSlab) {
			final SlabType slabType = SlabBlockExtension.getType(state);
			final SlabType neighborSlabType = SlabBlockExtension.getType(neighborState);
			if (direction.getAxis().isHorizontal()) {
				return slabType == neighborSlabType;
			} else {
				if (direction == Direction.UP) {
					return slabType != SlabType.BOTTOM && neighborSlabType != SlabType.TOP;
				} else {
					return slabType != SlabType.TOP && neighborSlabType != SlabType.BOTTOM;
				}
			}
		} else if (neighborState.getBlock().data instanceof BlockStationColorGlass) {
			return SlabBlockExtension.getType(state) == SlabType.DOUBLE;
		} else {
			return super.isSideInvisible2(state, neighborState, direction);
		}
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
