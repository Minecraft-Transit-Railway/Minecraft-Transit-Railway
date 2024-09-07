package org.mtr.mod.screen;

import org.mtr.core.tool.Utilities;
import org.mtr.libraries.it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.InitClient;
import org.mtr.mod.block.BlockTrainAnnouncer;
import org.mtr.mod.generated.lang.TranslationProvider;
import org.mtr.mod.packet.PacketUpdateTrainAnnouncerConfig;
import org.mtr.mod.resource.CustomResourceTools;

import java.util.Collections;

public class TrainAnnouncerScreen extends TrainSensorScreenBase {

	private final String initialMessage;
	private final String initialSoundId;
	private final int initialDelay;
	private final DashboardList availableSoundsList;

	private static final int MAX_MESSAGE_LENGTH = 256;
	private static final int MAX_DELAY_LENGTH = 3;

	public TrainAnnouncerScreen(BlockPos pos, BlockTrainAnnouncer.BlockEntity blockEntity) {
		super(pos, true,
				new ObjectObjectImmutablePair<>(new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, null), TranslationProvider.GUI_MTR_ANNOUNCEMENT_MESSAGE.getMutableText()),
				new ObjectObjectImmutablePair<>(new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, null), TranslationProvider.GUI_MTR_SOUND_FILE.getMutableText()),
				new ObjectObjectImmutablePair<>(new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_DELAY_LENGTH, TextCase.DEFAULT, "\\D", null), TranslationProvider.GUI_MTR_ANNOUNCEMENT_DELAY.getMutableText())
		);

		final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
		if (clientWorld != null) {
			initialMessage = blockEntity.getMessage();
			initialSoundId = blockEntity.getSoundId();
			initialDelay = blockEntity.getDelay();
		} else {
			initialMessage = "";
			initialSoundId = "";
			initialDelay = 0;
		}

		availableSoundsList = new DashboardList((data, color) -> {
			final String soundId = CustomResourceTools.formatIdentifierString(data.getName(true));
			if (!soundId.isEmpty() && clientWorld != null && MinecraftClient.getInstance().getPlayerMapped() != null) {
				clientWorld.playSoundAtBlockCenter(pos, SoundHelper.createSoundEvent(new Identifier(soundId)), SoundCategory.BLOCKS, 1000000, 1, false);
			}
		}, null, null, null, (data, color) -> {
			textFields[1].setText2(data.getName(true));
			setListVisibility(false);
		}, null, null, () -> "", text -> {
		}, false);

		final ObjectArrayList<DashboardListItem> soundIds = new ObjectArrayList<>();
		AbstractSoundInstanceExtension.iterateSoundIds(identifier -> soundIds.add(new DashboardListItem(0, identifier.data.toString(), ARGB_BACKGROUND)));
		Collections.sort(soundIds);
		availableSoundsList.setData(soundIds, true, false, false, false, true, false);
	}

	@Override
	protected void init2() {
		super.init2();
		textFields[0].setText2(initialMessage);
		textFields[1].setText2(initialSoundId);
		textFields[2].setText2(String.valueOf(initialDelay));

		setListVisibility(false);
		availableSoundsList.y = SQUARE_SIZE * 2 + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING;
		availableSoundsList.height = height - availableSoundsList.y - SQUARE_SIZE;
		availableSoundsList.width = (width - SQUARE_SIZE * 2) * 2 / 3;
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
	public boolean mouseScrolled2(double mouseX, double mouseY, double amount) {
		availableSoundsList.mouseScrolled(mouseX, mouseY, amount);
		return super.mouseScrolled2(mouseX, mouseY, amount);
	}

	@Override
	protected void renderAdditional(GraphicsHolder graphicsHolder) {
		final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
		guiDrawing.beginDrawingRectangle();
		guiDrawing.drawRectangle(availableSoundsList.x, availableSoundsList.y, availableSoundsList.x + availableSoundsList.width, availableSoundsList.y + availableSoundsList.height, ARGB_BACKGROUND);
		guiDrawing.finishDrawingRectangle();
		availableSoundsList.render(graphicsHolder);
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		int delay = 0;
		try {
			delay = Integer.parseInt(textFields[2].getText2());
		} catch (Exception ignored) {
		}
		InitClient.REGISTRY_CLIENT.sendPacketToServer(new PacketUpdateTrainAnnouncerConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, textFields[0].getText2(), textFields[1].getText2(), delay));
	}

	private void setListVisibility(boolean visible) {
		availableSoundsList.x = visible ? (width - SQUARE_SIZE * 2) / 3 + SQUARE_SIZE : width;
	}
}
