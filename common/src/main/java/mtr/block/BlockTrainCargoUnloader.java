package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Set;

public class BlockTrainCargoUnloader extends BlockTrainSensorBase {

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainCargoUnloader(pos, state);
	}

	public static class TileEntityTrainCargoUnloader extends TileEntityTrainSensorBase {

		public TileEntityTrainCargoUnloader(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_CARGO_UNLOADER_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String... strings) {
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}
	}
}
