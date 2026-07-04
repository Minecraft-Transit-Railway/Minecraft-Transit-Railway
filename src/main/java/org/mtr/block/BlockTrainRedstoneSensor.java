package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockTrainRedstoneSensor extends BlockTrainPoweredSensorBase {

	public BlockTrainRedstoneSensor(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainRedstoneSensorBlockEntity(blockPos, blockState);
	}

	public static class TrainRedstoneSensorBlockEntity extends BlockEntityBase {

		public TrainRedstoneSensorBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_REDSTONE_SENSOR.get(), pos, state);
		}
	}
}
