package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class BlockStationColorGlassSlab extends BlockStationColorSlab {

	public BlockStationColorGlassSlab(AbstractBlock.Settings settings) {
		super(settings.nonOpaque());
	}

	@Override
	public boolean isSideInvisible(BlockState state, BlockState neighborState, Direction direction) {
		if (neighborState.getBlock() instanceof BlockStationColorGlassSlab) {
			final SlabType slabType = state.get(SlabBlock.TYPE);
			final SlabType neighborSlabType = neighborState.get(SlabBlock.TYPE);
			if (direction.getAxis().isHorizontal()) {
				return slabType == neighborSlabType;
			} else {
				if (direction == Direction.UP) {
					return slabType != SlabType.BOTTOM && neighborSlabType != SlabType.TOP;
				} else {
					return slabType != SlabType.TOP && neighborSlabType != SlabType.BOTTOM;
				}
			}
		} else if (neighborState.getBlock() instanceof BlockStationColorGlass) {
			return state.get(SlabBlock.TYPE) == SlabType.DOUBLE;
		} else {
			return super.isSideInvisible(state, neighborState, direction);
		}
	}

	@Override
	public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
		return 1;
	}

	@Override
	public boolean isTransparent(BlockState state) {
		return true;
	}
}
