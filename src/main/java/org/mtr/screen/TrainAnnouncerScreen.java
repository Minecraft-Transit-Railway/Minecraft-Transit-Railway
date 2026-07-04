package org.mtr.screen;

import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import org.mtr.block.BlockTrainAnnouncer;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.packet.PacketUpdateTrainAnnouncerConfig;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.NumberInputComponent;
import org.mtr.widget.TextInputComponent;

public class TrainAnnouncerScreen extends TrainSensorScreenBase<BlockTrainAnnouncer.TrainAnnouncerBlockEntity> {

	private final TextInputComponent messageTextInput;
	private final TextInputComponent soundIdTextInput;
	private final NumberInputComponent delayNumberInput;

	private static final int MAX_DELAY = 1000;

	public TrainAnnouncerScreen(BlockPos pos, BlockTrainAnnouncer.TrainAnnouncerBlockEntity blockEntity) {
		super(TranslationProvider.BLOCK_MTR_TRAIN_ANNOUNCER.getString(), pos, blockEntity, true);
		GuiHelper.createSpacing(contentContainer);
		GuiHelper.createLabel(contentContainer, TranslationProvider.GUI_MTR_ANNOUNCEMENT_MESSAGE.getString());

		messageTextInput = (TextInputComponent) new TextInputComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		messageTextInput.setText(blockEntity.getMessage());

		GuiHelper.createSpacing(contentContainer);
		GuiHelper.createLabel(contentContainer, TranslationProvider.GUI_MTR_SOUND_FILE.getString());

		soundIdTextInput = (TextInputComponent) new TextInputComponent()
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new RelativeConstraint())
			.setHeight(new PixelConstraint(20));

		soundIdTextInput.setText(blockEntity.getSoundId());

		GuiHelper.createSpacing(contentContainer);
		GuiHelper.createLabel(contentContainer, TranslationProvider.GUI_MTR_ANNOUNCEMENT_DELAY.getString());

		delayNumberInput = (NumberInputComponent) new NumberInputComponent(0, MAX_DELAY, 1, false, null)
			.setChildOf(contentContainer)
			.setY(new SiblingConstraint())
			.setWidth(new PixelConstraint(LEFT_WIDTH));

		delayNumberInput.setValue(blockEntity.getDelay());
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		new PacketUpdateTrainAnnouncerConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, messageTextInput.getText(), soundIdTextInput.getText(), (int) delayNumberInput.getValue()).send(Minecraft.getInstance().level);
	}
}
