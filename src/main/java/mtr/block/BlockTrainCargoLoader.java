package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

import java.util.Set;

public class BlockTrainCargoLoader extends BlockTrainSensorBase {

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainCargoLoader(pos, state);
	}

	public static class TileEntityTrainCargoLoader extends TileEntityTrainSensorBase {

		public TileEntityTrainCargoLoader(BlockPos pos, BlockState state) {
			super(MTR.TRAIN_CARGO_LOADER_TILE_ENTITY, pos, state);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, int number, String string) {
			setData(filterRouteIds);
		}
	}
}
