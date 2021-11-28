package mtr.gui;

import mtr.block.BlockTrainScheduleSensor;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrainScheduleSensorScreen extends TrainSensorScreenBase {

	private final int seconds;

	private static final int MAX_SECONDS_LENGTH = 3;
	private static final int DEFAULT_SECONDS = 10;

	public TrainScheduleSensorScreen(BlockPos pos) {
		super(pos, new WidgetBetterTextField(WidgetBetterTextField.TextFieldFilter.INTEGER, ""), new TranslatableText("gui.mtr.train_schedule_sensor"));

		final World world = MinecraftClient.getInstance().world;
		if (world == null) {
			seconds = 0;
		} else {
			final BlockEntity entity = world.getBlockEntity(pos);
			seconds = entity instanceof BlockTrainScheduleSensor.TileEntityTrainScheduleSensor ? ((BlockTrainScheduleSensor.TileEntityTrainScheduleSensor) entity).getSeconds() : DEFAULT_SECONDS;
		}
	}

	@Override
	protected void init() {
		super.init();
		textField.setMaxLength(MAX_SECONDS_LENGTH);
		textField.setText(String.valueOf(seconds));
	}

	@Override
	protected int getNumber() {
		int secondsParsed = 10;
		try {
			secondsParsed = Integer.parseInt(textField.getText());
		} catch (Exception ignored) {
		}
		return secondsParsed;
	}
}
