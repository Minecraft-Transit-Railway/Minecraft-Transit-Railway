package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class BlockStationColorGlassSlab extends BlockStationColorSlab {

	public BlockStationColorGlassSlab(Properties settings) {
		super(settings);
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
		if (neighborState.getBlock() instanceof BlockStationColorGlassSlab) {
			final SlabType slabType = IBlock.getStatePropertySafe(state, TYPE);
			final SlabType neighborSlabType = IBlock.getStatePropertySafe(neighborState, TYPE);
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
			return IBlock.getStatePropertySafe(state, TYPE) == SlabType.DOUBLE;
		} else {
			return super.skipRendering(state, neighborState, direction);
		}
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter world, BlockPos pos) {
		return 1;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}
}
