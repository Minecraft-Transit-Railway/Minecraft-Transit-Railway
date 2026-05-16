package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockTrainAnnouncer;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.packet.PacketUpdateTrainAnnouncerConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.widget.TextInputComponent;

public class TrainAnnouncerScreen extends TrainSensorScreenBase {

	private final TextInputComponent messageTextField;
	private final TextInputComponent soundIdTextField;
	private final TextInputComponent delayTextField;

	private static final int MAX_MESSAGE_LENGTH = 256;
	private static final int MAX_DELAY_LENGTH = 3;

	public TrainAnnouncerScreen(BlockPos pos, BlockTrainAnnouncer.TrainAnnouncerBlockEntity blockEntity) {
		super(pos, true);

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
		final String initialMessage;
		final String initialSoundId;
		final int initialDelay;
		if (clientWorld == null) {
			initialMessage = "";
			initialSoundId = "";
			initialDelay = 0;
		} else {
			initialMessage = blockEntity.getMessage();
			initialSoundId = blockEntity.getSoundId();
			initialDelay = blockEntity.getDelay();
		}

		messageTextField = addTextField(TranslationProvider.GUI_MTR_ANNOUNCEMENT_MESSAGE.getString(), null, MAX_MESSAGE_LENGTH, initialMessage);
		soundIdTextField = addTextField(TranslationProvider.GUI_MTR_SOUND_FILE.getString(), null, MAX_MESSAGE_LENGTH, initialSoundId);
		delayTextField = addTextField(TranslationProvider.GUI_MTR_ANNOUNCEMENT_DELAY.getString(), "\\D", MAX_DELAY_LENGTH, String.valueOf(initialDelay));
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		int delay = 0;
		try {
			delay = Integer.parseInt(delayTextField.getText());
		} catch (Exception ignored) {
		}
		RegistryClient.sendPacketToServer(new PacketUpdateTrainAnnouncerConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, messageTextField.getText(), soundIdTextField.getText(), delay));
	}
}
