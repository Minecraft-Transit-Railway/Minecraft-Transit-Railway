package org.mtr.mod.screen;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.BlockEntity;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ClientWorld;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.block.BlockTrainScheduleSensor;

public class TrainScheduleSensorScreen extends TrainSensorScreenBase {

	private final int seconds;

	private static final int MAX_SECONDS_LENGTH = 5;
	private static final int DEFAULT_SECONDS = 10;

	public TrainScheduleSensorScreen(BlockPos pos) {
		super(pos, false, new ObjectObjectImmutablePair<>(new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_SECONDS_LENGTH, TextCase.DEFAULT, "\\D", null), TextHelper.translatable("gui.mtr.train_schedule_sensor")));

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld == null) {
			seconds = 0;
		} else {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(pos);
			seconds = blockEntity != null && blockEntity.data instanceof BlockTrainScheduleSensor.BlockEntity ? ((BlockTrainScheduleSensor.BlockEntity) blockEntity.data).getSeconds() : DEFAULT_SECONDS;
		}
	}

	@Override
	protected void init2() {
		super.init2();
		textFields[0].setText2(String.valueOf(seconds));
	}

	@Override
	protected int getNumber() {
		int secondsParsed = 10;
		try {
			secondsParsed = Integer.parseInt(textFields[0].getText2());
		} catch (Exception ignored) {
		}
		return secondsParsed;
	}
}
