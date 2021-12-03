package mtr.block;

import mtr.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

import java.util.Random;
import java.util.Set;

public class BlockTrainRedstoneSensor extends BlockTrainSensorBase {

	public static final BooleanProperty POWERED = BooleanProperty.of("powered");

	public BlockTrainRedstoneSensor() {
		super();
		setDefaultState(getStateManager().getDefaultState().with(POWERED, false));
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		world.setBlockState(pos, state.with(POWERED, false));
	}

	@Override
	public boolean emitsRedstonePower(BlockState state) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return state.get(POWERED) ? 15 : 0;
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityTrainRedstoneSensor(pos, state);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public static class TileEntityTrainRedstoneSensor extends TileEntityTrainSensorBase {

		public TileEntityTrainRedstoneSensor(BlockPos pos, BlockState state) {
			super(MTR.TRAIN_REDSTONE_SENSOR_TILE_ENTITY, pos, state);
		}

		@Override
		public void setData(Set<Long> filterRouteIds, int number, String string) {
			setData(filterRouteIds);
		}
	}
}
