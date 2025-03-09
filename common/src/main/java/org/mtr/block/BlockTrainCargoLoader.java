package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockTrainCargoLoader extends BlockTrainSensorBase {

	public BlockTrainCargoLoader(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainCargoLoaderBlockEntity(blockPos, blockState);
	}

	public static class TrainCargoLoaderBlockEntity extends BlockEntityBase {

		public TrainCargoLoaderBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_CARGO_LOADER.createAndGet(), pos, state);
		}
	}
}
