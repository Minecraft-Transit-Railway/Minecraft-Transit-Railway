package org.mtr.mod.screen;

import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.ClientWorld;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.CheckboxWidgetExtension;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockTrainScheduleSensor;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateTrainScheduleSensorConfig;

public class TrainScheduleSensorScreen extends TrainSensorScreenBase {

	private final int seconds;
	private final boolean realtimeOnly;
	private final CheckboxWidgetExtension realtimeOnlyCheckbox;

	private static final int MAX_SECONDS_LENGTH = 5;
	private static final int DEFAULT_SECONDS = 10;

	public TrainScheduleSensorScreen(BlockPos pos, BlockTrainScheduleSensor.BlockEntity blockEntity) {
		super(pos, false, new ObjectObjectImmutablePair<>(new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_SECONDS_LENGTH, TextCase.DEFAULT, "[^\\d-]", null), TranslationProvider.GUI_MTR_TRAIN_SCHEDULE_SENSOR.getMutableText()));

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			seconds = DEFAULT_SECONDS;
			realtimeOnly = false;
		} else {
			seconds = blockEntity.getSeconds();
			realtimeOnly = blockEntity.getRealtimeOnly();
		}

		realtimeOnlyCheckbox = new CheckboxWidgetExtension(0, 0, 0, SQUARE_SIZE, true, checked -> {
		});
		realtimeOnlyCheckbox.setMessage2(TranslationProvider.GUI_MTR_REALTIME_ONLY.getText());
	}

	@Override
	protected void init2() {
		super.init2();
		textFields[0].setText2(String.valueOf(seconds));
		IDrawing.setPositionAndWidth(realtimeOnlyCheckbox, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING, PANEL_WIDTH);
		realtimeOnlyCheckbox.setChecked(realtimeOnly);
		addChild(new ClickableWidget(realtimeOnlyCheckbox));
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		int secondsParsed = DEFAULT_SECONDS;
		try {
			secondsParsed = Integer.parseInt(textFields[0].getText2());
		} catch (Exception ignored) {
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateTrainScheduleSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, secondsParsed, realtimeOnlyCheckbox.isChecked2()));
	}
}
