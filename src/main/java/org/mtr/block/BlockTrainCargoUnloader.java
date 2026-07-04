package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockTrainCargoUnloader extends BlockTrainSensorBase {

	public BlockTrainCargoUnloader(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainCargoUnloaderBlockEntity(blockPos, blockState);
	}

	public static class TrainCargoUnloaderBlockEntity extends BlockEntityBase {

		public TrainCargoUnloaderBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_CARGO_UNLOADER.get(), pos, state);
		}
	}
}
