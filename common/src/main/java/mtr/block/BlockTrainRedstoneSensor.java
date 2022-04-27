package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import java.util.Set;

public class BlockTrainRedstoneSensor extends BlockTrainPoweredSensorBase {

	public BlockTrainRedstoneSensor() {
		super();
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainRedstoneSensor(pos, state);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public static class TileEntityTrainRedstoneSensor extends TileEntityTrainSensorBase {

		public TileEntityTrainRedstoneSensor(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_REDSTONE_SENSOR_TILE_ENTITY.get(), pos, state);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, boolean stoppedOnly, boolean movingOnly, int number, String... strings) {
			setData(filterRouteIds, stoppedOnly, movingOnly);
		}
	}
}
