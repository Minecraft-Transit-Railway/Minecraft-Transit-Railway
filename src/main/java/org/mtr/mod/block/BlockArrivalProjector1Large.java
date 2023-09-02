package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BlockArrivalProjector1Large extends BlockArrivalProjectorBase {

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Large(pos, state);
	}

	public static class TileEntityArrivalProjector1Large extends TileEntityArrivalProjectorBase {

		public TileEntityArrivalProjector1Large(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY.get(), pos, state);
		}
	}
}
