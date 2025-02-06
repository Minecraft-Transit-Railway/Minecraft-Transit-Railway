package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import java.util.List;

public abstract class BlockTrainPoweredSensorBase extends BlockTrainSensorBase {

	public static final IntegerProperty POWERED = IntegerProperty.of("powered", 0, 2);
	private static final int UPDATE_TICKS = 10;

	public BlockTrainPoweredSensorBase() {
		super();
	}

	@Override
	public void scheduledTick2(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered > 0) {
			world.setBlockState(pos, state.with(new Property<>(POWERED.data), oldPowered - 1));
			if (!hasScheduledBlockTick(World.cast(world), pos, new Block(this))) {
				scheduleBlockTick(World.cast(world), pos, new Block(this), UPDATE_TICKS);
			}
		}
	}

	@Override
	public boolean emitsRedstonePower2(BlockState blockState) {
		return true;
	}

	@Override
	public int getWeakRedstonePower2(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return IBlock.getStatePropertySafe(state, POWERED) > 0 ? 15 : 0;
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(POWERED);
	}

	public void power(World world, BlockState state, BlockPos pos) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered < 2) {
			world.setBlockState(pos, state.with(new Property<>(POWERED.data), 2));
			if (oldPowered == 0 && !hasScheduledBlockTick(world, pos, new Block(this))) {
				scheduleBlockTick(world, pos, new Block(this), UPDATE_TICKS);
			}
		}
	}
}
