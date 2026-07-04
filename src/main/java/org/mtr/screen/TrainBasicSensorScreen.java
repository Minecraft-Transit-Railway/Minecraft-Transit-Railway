package org.mtr.screen;

import net.minecraft.core.BlockPos;
import org.mtr.block.BlockTrainSensorBase;
import org.mtr.generated.lang.TranslationProvider;

public class TrainBasicSensorScreen extends TrainSensorScreenBase<BlockTrainSensorBase.BlockEntityBase> {

	public TrainBasicSensorScreen(BlockPos blockPos, BlockTrainSensorBase.BlockEntityBase blockEntity) {
		super(TranslationProvider.BLOCK_MTR_TRAIN_SENSOR.getString(), blockPos, blockEntity, true);
	}
}
