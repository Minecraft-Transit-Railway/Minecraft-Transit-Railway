package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockTrainCargoUnloader extends BlockTrainSensorBase {

	public BlockTrainCargoUnloader(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new TrainCargoUnloaderBlockEntity(blockPos, blockState);
	}

	public static class TrainCargoUnloaderBlockEntity extends BlockEntityBase {

		public TrainCargoUnloaderBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.TRAIN_CARGO_UNLOADER.get(), pos, state);
		}
	}
}
