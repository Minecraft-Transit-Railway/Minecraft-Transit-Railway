package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;

public abstract class BlockTrainPoweredSensorBase extends BlockTrainSensorBase {

	public static final IntProperty POWERED = IntProperty.of("powered", 0, 2);
	private static final int UPDATE_TICKS = 10;

	public BlockTrainPoweredSensorBase(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered > 0) {
			world.setBlockState(pos, state.with(POWERED, oldPowered - 1));
			if (!world.getBlockTickScheduler().isQueued(pos, this)) {
				world.getBlockTickScheduler().scheduleTick(new OrderedTick<>(this, pos, UPDATE_TICKS, 0));
			}
		}
	}

	@Override
	public boolean emitsRedstonePower(BlockState blockState) {
		return true;
	}

	@Override
	public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction direction) {
		return IBlock.getStatePropertySafe(state, POWERED) > 0 ? 15 : 0;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	public void power(World world, BlockState state, BlockPos pos) {
		final int oldPowered = IBlock.getStatePropertySafe(state, POWERED);
		if (oldPowered < 2) {
			world.setBlockState(pos, state.with(POWERED, 2));
			if (oldPowered == 0 && !world.getBlockTickScheduler().isQueued(pos, this)) {
				world.getBlockTickScheduler().scheduleTick(new OrderedTick<>(this, pos, UPDATE_TICKS, 0));
			}
		}
	}
}
