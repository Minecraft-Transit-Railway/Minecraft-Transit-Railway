package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.mtr.registry.BlockEntityTypes;

public class BlockTrainCargoLoader extends BlockTrainSensorBase {

	public BlockTrainCargoLoader(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainCargoLoaderBlockEntity(blockPos, blockState);
	}

	public static class TrainCargoLoaderBlockEntity extends BlockEntityBase {

		public TrainCargoLoaderBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_CARGO_LOADER.get(), pos, state);
		}
	}
}
