package org.mtr.screen;

import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.mtr.block.BlockTrainScheduleSensor;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.packet.PacketUpdateTrainScheduleSensorConfig;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.NumberInputComponent;

public class TrainScheduleSensorScreen extends TrainSensorScreenBase<BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity> {

	private final NumberInputComponent secondsNumberInput;
	private final CheckboxComponent realtimeOnlyCheckbox;

	private static final int MAX_SECONDS = 100000;

	public TrainScheduleSensorScreen(BlockPos pos, BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity blockEntity) {
		super(TranslationProvider.BLOCK_MTR_TRAIN_SCHEDULE_SENSOR.getString(), pos, blockEntity, false);
		GuiHelper.createSpacing(contentContainer);
		GuiHelper.createLabel(contentContainer, TranslationProvider.GUI_MTR_TRAIN_SCHEDULE_SENSOR.getString());

		secondsNumberInput = (NumberInputComponent) new NumberInputComponent(0, MAX_SECONDS, 1, false, null)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		secondsNumberInput.setValue(blockEntity.getSeconds());

		GuiHelper.createSpacing(contentContainer);

		realtimeOnlyCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());

		realtimeOnlyCheckbox.setText(TranslationProvider.GUI_MTR_REALTIME_ONLY.getString());
		realtimeOnlyCheckbox.setChecked(blockEntity.getRealtimeOnly());
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		new PacketUpdateTrainScheduleSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, (int) secondsNumberInput.getValue(), realtimeOnlyCheckbox.isChecked()).send(Minecraft.getInstance().level);
	}
}
