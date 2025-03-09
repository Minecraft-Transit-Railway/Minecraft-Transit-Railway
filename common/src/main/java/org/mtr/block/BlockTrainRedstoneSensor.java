package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockTrainRedstoneSensor extends BlockTrainPoweredSensorBase {

	public BlockTrainRedstoneSensor(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainRedstoneSensorBlockEntity(blockPos, blockState);
	}

	public static class TrainRedstoneSensorBlockEntity extends BlockEntityBase {

		public TrainRedstoneSensorBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_REDSTONE_SENSOR.createAndGet(), pos, state);
		}
	}
}
