package mtr.block;

import mtr.mappings.Utilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class BlockTrainPoweredSensorBase extends BlockTrainSensorBase {

	public static final IntegerProperty POWERED = IntegerProperty.create("powered", 0, 2);
	private static final int UPDATE_TICKS = 10;

	public BlockTrainPoweredSensorBase() {
		super();
		registerDefaultState(defaultBlockState().setValue(POWERED, 0));
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered > 0) {
			world.setBlockAndUpdate(pos, state.setValue(POWERED, oldPowered - 1));
			if (!world.getBlockTicks().hasScheduledTick(pos, this)) {
				Utilities.scheduleBlockTick(world, pos, this, UPDATE_TICKS);
			}
		}
	}

	@Override
	public boolean isSignalSource(BlockState blockState) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter blockGetter, BlockPos blockPos, Direction direction) {
		return IBlock.getStatePropertySafe(state, POWERED) > 0 ? 15 : 0;
	}

	public void power(Level world, BlockState state, BlockPos pos) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered < 2) {
			world.setBlockAndUpdate(pos, state.setValue(POWERED, 2));
			if (oldPowered == 0 && !world.getBlockTicks().hasScheduledTick(pos, this)) {
				Utilities.scheduleBlockTick(world, pos, this, UPDATE_TICKS);
			}
		}
	}
}
