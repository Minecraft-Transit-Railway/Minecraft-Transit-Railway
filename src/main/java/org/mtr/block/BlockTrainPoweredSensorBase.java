package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.ticks.ScheduledTick;

public abstract class BlockTrainPoweredSensorBase extends BlockTrainSensorBase {

	public static final IntegerProperty POWERED = IntegerProperty.create("powered", 0, 2);
	private static final int UPDATE_TICKS = 10;

	public BlockTrainPoweredSensorBase(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered > 0) {
			world.setBlockAndUpdate(pos, state.setValue(POWERED, oldPowered - 1));
			if (!world.getBlockTicks().hasScheduledTick(pos, this)) {
				world.getBlockTicks().schedule(new ScheduledTick<>(this, pos, UPDATE_TICKS, 0));
			}
		}
	}

	@Override
	public boolean isSignalSource(BlockState blockState) {
		return true;
	}

	@Override
	public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction direction) {
		return IBlock.getStatePropertySafe(state, POWERED) > 0 ? 15 : 0;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public void power(Level world, BlockState state, BlockPos pos) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered < 2) {
			world.setBlockAndUpdate(pos, state.setValue(POWERED, 2));
			if (oldPowered == 0 && !world.getBlockTicks().hasScheduledTick(pos, this)) {
				world.getBlockTicks().schedule(new ScheduledTick<>(this, pos, UPDATE_TICKS, 0));
			}
		}
	}
}
