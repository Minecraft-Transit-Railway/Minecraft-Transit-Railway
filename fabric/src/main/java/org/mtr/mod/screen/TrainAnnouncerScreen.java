package org.mtr.mod.screen;

import org.mtr.core.tools.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.block.BlockTrainAnnouncer;

import java.util.Collections;

public class TrainAnnouncerScreen extends TrainSensorScreenBase {

	private final String initialMessage;
	private final String initialSoundIdString;
	private final DashboardList availableSoundsList;

	private static final int MAX_MESSAGE_LENGTH = 256;

	public TrainAnnouncerScreen(BlockPos pos) {
		super(pos, true,
				new ObjectObjectImmutablePair<>(new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, null), TextHelper.translatable("gui.mtr.announcement_message")),
				new ObjectObjectImmutablePair<>(new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, null), TextHelper.translatable("gui.mtr.sound_file"))
		);

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld != null) {
			final BlockEntity blockEntity = clientWorld.getBlockEntity(pos);
			if (blockEntity != null && blockEntity.data instanceof BlockTrainAnnouncer.BlockEntity) {
				initialMessage = ((BlockTrainAnnouncer.BlockEntity) blockEntity.data).getMessage();
				initialSoundIdString = ((BlockTrainAnnouncer.BlockEntity) blockEntity.data).getSoundIdString();
			} else {
				initialMessage = "";
				initialSoundIdString = "";
			}
		} else {
			initialMessage = "";
			initialSoundIdString = "";
		}

		availableSoundsList = new DashboardList((data, color) -> {
			final String soundIdString = data.getName(true);
			if (!soundIdString.isEmpty() && clientWorld != null && MinecraftClient.getInstance().getPlayerMapped() != null) {
				clientWorld.playSoundAtBlockCenter(pos, AbstractSoundInstanceExtension.createSoundEvent(new Identifier(soundIdString)), SoundCategory.BLOCKS, 1000000, 1, false);
			}
		}, null, null, null, (data, color) -> {
			textFields[1].setText2(data.getName(true));
			setListVisibility(false);
		}, null, null, () -> "", text -> {
		}, false);

		final ObjectArrayList<DashboardListItem> soundIds = new ObjectArrayList<>();
		AbstractSoundInstanceExtension.iterateSoundIds(identifier -> soundIds.add(new DashboardListItem(0, identifier.toString(), ARGB_BACKGROUND)));
		Collections.sort(soundIds);
		availableSoundsList.setData(soundIds, true, false, false, false, true, false);
	}

	@Override
	protected void init2() {
		super.init2();
		textFields[0].setText2(initialMessage);
		textFields[1].setText2(initialSoundIdString);

		setListVisibility(false);
		availableSoundsList.y = SQUARE_SIZE * 2 + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING;
		availableSoundsList.height = height - availableSoundsList.y - SQUARE_SIZE;
		availableSoundsList.width = width / 2 - SQUARE_SIZE;
		availableSoundsList.init(this::addChild);
	}

	@Override
	public void tick2() {
		super.tick2();
		availableSoundsList.tick();
	}

	@Override
	public boolean mouseClicked2(double mouseX, double mouseY, int button) {
		if (button == 0) {
			if (Utilities.isBetween(mouseX, textFields[1].getX2(), textFields[1].getX2() + textFields[1].getWidth2()) && Utilities.isBetween(mouseY, textFields[1].getY2(), textFields[1].getY2() + textFields[1].getHeight2())) {
				setListVisibility(true);
			} else if (!Utilities.isBetween(mouseX, availableSoundsList.x, availableSoundsList.x + availableSoundsList.width) || !Utilities.isBetween(mouseY, availableSoundsList.y, availableSoundsList.y + availableSoundsList.height)) {
				setListVisibility(false);
			}
		}
		return super.mouseClicked2(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved2(double mouseX, double mouseY) {
		availableSoundsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled3(double mouseX, double mouseY, double amount) {
		availableSoundsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled3(mouseX, mouseY, amount);
	}

	@Override
	protected void renderAdditional(GraphicsHolder graphicsHolder) {
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(availableSoundsList.x, availableSoundsList.y, availableSoundsList.x + availableSoundsList.width, availableSoundsList.y + availableSoundsList.height, ARGB_BACKGROUND);
		guiDrawing.finishDrawingRectangle();
		availableSoundsList.render(graphicsHolder);
	}

	private void setListVisibility(boolean visible) {
		availableSoundsList.x = visible ? width / 2 : width;
	}
}
