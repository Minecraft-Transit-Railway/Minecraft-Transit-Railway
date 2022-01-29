package mtr.screen;

import mtr.block.BlockTrainAnnouncer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class TrainAnnouncerScreen extends TrainSensorScreenBase {

	private final String initialMessage;

	private static final int MAX_MESSAGE_LENGTH = 256;

	public TrainAnnouncerScreen(BlockPos pos) {
		super(pos, true, new WidgetBetterTextField(null, "", MAX_MESSAGE_LENGTH), new TranslatableComponent("gui.mtr.announcement_message"));

		final Level world = Minecraft.getInstance().level;
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
		textField.setValue(initialMessage);
	}

	@Override
	protected String getString() {
		return textField.getValue();
	}
}
