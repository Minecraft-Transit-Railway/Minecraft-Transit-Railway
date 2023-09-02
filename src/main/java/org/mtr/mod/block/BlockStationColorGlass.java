package mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;

public class BlockStationColorGlass extends BlockStationColor {

	public BlockStationColorGlass(Properties settings) {
		super(settings);
	}

	@Override
	public boolean skipRendering(BlockState state, BlockState neighborState, Direction direction) {
		return neighborState.getBlock() instanceof BlockStationColorGlass || (neighborState.getBlock() instanceof BlockStationColorGlassSlab && IBlock.getStatePropertySafe(neighborState, SlabBlock.TYPE) == SlabType.DOUBLE) || super.skipRendering(state, neighborState, direction);
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
