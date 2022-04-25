package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class BlockTrainCargoLoader extends BlockTrainSensorBase {

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainCargoLoader(pos, state);
	}

	public static class TileEntityTrainCargoLoader extends TileEntityTrainSensorBase {

		public TileEntityTrainCargoLoader(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_CARGO_LOADER_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String... strings) {
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}
	}
}
