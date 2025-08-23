package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockTrainScheduleSensor;
import org.mtr.client.IDrawing;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateTrainScheduleSensorConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.BetterTextFieldWidget;

public class TrainScheduleSensorScreen extends TrainSensorScreenBase {

	private final int seconds;
	private final CheckboxWidget realtimeOnlyCheckbox;

	private static final int MAX_SECONDS_LENGTH = 5;
	private static final int DEFAULT_SECONDS = 10;

	public TrainScheduleSensorScreen(BlockPos pos, BlockTrainScheduleSensor.TrainScheduleSensorBlockEntity blockEntity) {
		super(pos, false, new ObjectObjectImmutablePair<>(new BetterTextFieldWidget(MAX_SECONDS_LENGTH, TextCase.DEFAULT, "[^\\d-]", null, 100, text -> {
		}), TranslationProvider.GUI_MTR_TRAIN_SCHEDULE_SENSOR.getMutableText()));

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		final boolean realtimeOnly;
		if (clientWorld == null) {
			seconds = DEFAULT_SECONDS;
			realtimeOnly = false;
		} else {
			seconds = blockEntity.getSeconds();
			realtimeOnly = blockEntity.getRealtimeOnly();
		}

		realtimeOnlyCheckbox = CheckboxWidget.builder(TranslationProvider.GUI_MTR_REALTIME_ONLY.getText(), textRenderer).checked(realtimeOnly).callback((checkboxWidget, checked) -> {
		}).build();
	}

	@Override
	protected void init() {
		super.init();
		textFields[0].setText(String.valueOf(seconds));
		IDrawing.setPositionAndWidth(realtimeOnlyCheckbox, SQUARE_SIZE, SQUARE_SIZE * 2 + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING, PANEL_WIDTH);
		addDrawableChild(realtimeOnlyCheckbox);
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		int secondsParsed = DEFAULT_SECONDS;
		try {
			secondsParsed = Integer.parseInt(textFields[0].getText());
		} catch (Exception ignored) {
		}
		RegistryClient.sendPacketToServer(new PacketUpdateTrainScheduleSensorConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, secondsParsed, realtimeOnlyCheckbox.isChecked()));
	}
}
