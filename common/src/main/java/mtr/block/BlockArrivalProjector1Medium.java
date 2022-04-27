package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockArrivalProjector1Medium extends BlockArrivalProjectorBase {

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Medium(pos, state);
	}

	public static class TileEntityArrivalProjector1Medium extends TileEntityArrivalProjectorBase {

		public TileEntityArrivalProjector1Medium(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY.get(), pos, state);
		}
	}
}
