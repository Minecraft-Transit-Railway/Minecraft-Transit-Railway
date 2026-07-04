package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class BlockStationColorGlassSlab extends BlockStationColorSlab {

	public BlockStationColorGlassSlab(BlockBehaviour.Properties settings) {
		super(settings.noOcclusion());
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
		if (neighborState.getBlock() instanceof BlockStationColorGlassSlab) {
			final SlabType slabType = state.getValue(SlabBlock.TYPE);
			final SlabType neighborSlabType = neighborState.getValue(SlabBlock.TYPE);
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
			return state.getValue(SlabBlock.TYPE) == SlabType.DOUBLE;
		} else {
			return super.skipRendering(state, neighborState, direction);
		}
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return 1;
	}

	@Override
//? if >= 1.21.4 {
	protected boolean propagatesSkylightDown(BlockState state) {
//? } else {
	/*protected boolean propagatesSkylightDown(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
//
*///? }
		return true;
	}
}
