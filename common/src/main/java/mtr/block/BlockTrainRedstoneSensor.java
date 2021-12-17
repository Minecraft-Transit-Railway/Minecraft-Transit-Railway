package mtr.block;

import mapper.BlockEntityMapper;
import mtr.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

import java.util.Random;
import java.util.Set;

public class BlockTrainRedstoneSensor extends BlockTrainSensorBase {

	public static final BooleanProperty POWERED = BooleanProperty.create("powered");

	public BlockTrainRedstoneSensor() {
		super();
		registerDefaultState(defaultBlockState().setValue(POWERED, false));
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
		world.setBlockAndUpdate(pos, state.setValue(POWERED, false));
	}

	@Override
	public boolean isSignalSource(BlockState blockState) {
		return true;
	}

	@Override
	public int getDirectSignal(BlockState state, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
		return state.getValue(POWERED) ? 15 : 0;
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
			super(BlockEntityTypes.TRAIN_REDSTONE_SENSOR_TILE_ENTITY, pos, state);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, int number, String string) {
			setData(filterRouteIds);
		}
	}
}
