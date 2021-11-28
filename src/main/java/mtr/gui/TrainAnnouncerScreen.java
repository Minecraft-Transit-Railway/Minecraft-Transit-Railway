package mtr.gui;

import mtr.block.BlockTrainAnnouncer;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TrainAnnouncerScreen extends TrainSensorScreenBase {

	private final String initialMessage;

	private static final int MAX_MESSAGE_LENGTH = 256;

	public TrainAnnouncerScreen(BlockPos pos) {
		super(pos, new WidgetBetterTextField(null, ""), new TranslatableText("gui.mtr.announcement_message"));

		final World world = MinecraftClient.getInstance().world;
		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(pos);
			initialMessage = entity instanceof BlockTrainAnnouncer.TileEntityTrainAnnouncer ? ((BlockTrainAnnouncer.TileEntityTrainAnnouncer) entity).getMessage() : "";
		} else {
			initialMessage = "";
		}
	}

	@Override
	protected void init() {
		super.init();
		textField.setMaxLength(MAX_MESSAGE_LENGTH);
		textField.setText(initialMessage);
	}

	@Override
	protected String getString() {
		return textField.getText();
	}
}
