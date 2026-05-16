package org.mtr.screen;

import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockTrainScheduleSensor;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.packet.PacketUpdateTrainScheduleSensorConfig;
import org.mtr.widget.CheckboxComponent;
import org.mtr.widget.TextInputComponent;

public class TrainScheduleSensorScreen extends TrainSensorScreenBase {

	private final TextInputComponent secondsTextField;
	private final CheckboxComponent realtimeOnlyCheckbox;

	private static final int MAX_SECONDS_LENGTH = 5;
	private static final int DEFAULT_SECONDS = 10;

	public TrainScheduleSensorScreen(BlockPos pos, BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity blockEntity) {
		super(pos, false);

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		final int seconds;
		final boolean realtimeOnly;
		if (clientWorld == null) {
			seconds = DEFAULT_SECONDS;
			realtimeOnly = false;
		} else {
			seconds = blockEntity.getSeconds();
			realtimeOnly = blockEntity.getRealtimeOnly();
		}

		secondsTextField = addTextField(TranslationProvider.GUI_MTR_TRAIN_SCHEDULE_SENSOR.getString(), "[^\\d-]", MAX_SECONDS_LENGTH, String.valueOf(seconds));

		realtimeOnlyCheckbox = (CheckboxComponent) new CheckboxComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint());
		realtimeOnlyCheckbox.setText(TranslationProvider.GUI_MTR_REALTIME_ONLY.getString());
		realtimeOnlyCheckbox.setChecked(realtimeOnly);
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		int secondsParsed = DEFAULT_SECONDS;
		try {
			secondsParsed = Integer.parseInt(secondsTextField.getText());
		} catch (Exception ignored) {
		}

		new PacketUpdateTrainScheduleSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, secondsParsed, realtimeOnlyCheckbox.isChecked()).send(MinecraftClient.getInstance().world);
	}
}
