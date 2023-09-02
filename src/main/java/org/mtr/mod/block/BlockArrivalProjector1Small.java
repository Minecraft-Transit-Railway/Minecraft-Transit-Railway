package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockArrivalProjector1Small extends BlockArrivalProjectorBase {

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Small(pos, state);
	}

	public static class TileEntityArrivalProjector1Small extends TileEntityArrivalProjectorBase {

		public TileEntityArrivalProjector1Small(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY.get(), pos, state);
		}
	}
}
