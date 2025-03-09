package org.mtr.screen;

import it.unimi.dsi.fastutil.longs.LongAVLTreeSet;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import org.mtr.block.BlockTrainAnnouncer;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketUpdateTrainAnnouncerConfig;
import org.mtr.registry.RegistryClient;
import org.mtr.resource.CustomResourceTools;

import java.util.Collections;

public class TrainAnnouncerScreen extends TrainSensorScreenBase {

	private final String initialMessage;
	private final String initialSoundId;
	private final int initialDelay;
	private final DashboardList availableSoundsList;

	private static final int MAX_MESSAGE_LENGTH = 256;
	private static final int MAX_DELAY_LENGTH = 3;

	public TrainAnnouncerScreen(BlockPos pos, BlockTrainAnnouncer.TrainAnnouncerBlockEntity blockEntity) {
		super(pos, true,
				new ObjectObjectImmutablePair<>(new WidgetBetterTextField(MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, null), TranslationProvider.GUI_MTR_ANNOUNCEMENT_MESSAGE.getMutableText()),
				new ObjectObjectImmutablePair<>(new WidgetBetterTextField(MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, null), TranslationProvider.GUI_MTR_SOUND_FILE.getMutableText()),
				new ObjectObjectImmutablePair<>(new WidgetBetterTextField(MAX_DELAY_LENGTH, TextCase.DEFAULT, "\\D", null), TranslationProvider.GUI_MTR_ANNOUNCEMENT_DELAY.getMutableText())
		);

		final ClientWorld clientWorld = MinecraftClient.getInstance().world;
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
			if (!soundId.isEmpty() && clientWorld != null && MinecraftClient.getInstance().player != null) {
				clientWorld.playSoundAtBlockCenter(pos, SoundEvent.of(Identifier.of(soundId)), SoundCategory.BLOCKS, 1000000, 1, false);
			}
		}, null, null, null, (data, color) -> {
			textFields[1].setText(data.getName(true));
			setListVisibility(false);
		}, null, null, () -> "", text -> {
		}, false);

		final ObjectArrayList<DashboardListItem> soundIds = new ObjectArrayList<>();
		MinecraftClient.getInstance().getSoundManager().getKeys().forEach(identifier -> soundIds.add(new DashboardListItem(0, identifier.toString(), ARGB_BACKGROUND)));
		Collections.sort(soundIds);
		availableSoundsList.setData(soundIds, true, false, false, false, true, false);
	}

	@Override
	protected void init() {
		super.init();
		textFields[0].setText(initialMessage);
		textFields[1].setText(initialSoundId);
		textFields[2].setText(String.valueOf(initialDelay));

		setListVisibility(false);
		availableSoundsList.y = SQUARE_SIZE * 2 + TEXT_HEIGHT + TEXT_PADDING + TEXT_FIELD_PADDING;
		availableSoundsList.height = height - availableSoundsList.y - SQUARE_SIZE;
		availableSoundsList.width = (width - SQUARE_SIZE * 2) * 2 / 3;
		availableSoundsList.init(this::addSelectableChild);
	}

	@Override
	public void tick() {
		super.tick();
		availableSoundsList.tick();
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (button == 0) {
			if (Utilities.isBetween(mouseX, textFields[1].getX(), textFields[1].getX() + textFields[1].getWidth()) && Utilities.isBetween(mouseY, textFields[1].getY(), textFields[1].getY() + textFields[1].getHeight())) {
				setListVisibility(true);
			} else if (!Utilities.isBetween(mouseX, availableSoundsList.x, availableSoundsList.x + availableSoundsList.width) || !Utilities.isBetween(mouseY, availableSoundsList.y, availableSoundsList.y + availableSoundsList.height)) {
				setListVisibility(false);
			}
		}
		return super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		availableSoundsList.mouseMoved(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		availableSoundsList.mouseScrolled(mouseX, mouseY, verticalAmount);
		return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
	}

	@Override
	protected void renderAdditional(DrawContext context) {
		context.fill(availableSoundsList.x, availableSoundsList.y, availableSoundsList.x + availableSoundsList.width, availableSoundsList.y + availableSoundsList.height, ARGB_BACKGROUND);
		availableSoundsList.render(context);
	}

	@Override
	protected void sendUpdate(BlockPos blockPos, LongAVLTreeSet filterRouteIds, boolean stoppedOnly, boolean movingOnly) {
		int delay = 0;
		try {
			delay = Integer.parseInt(textFields[2].getText());
		} catch (Exception ignored) {
		}
		RegistryClient.sendPacketToServer(new PacketUpdateTrainAnnouncerConfig(blockPos, filterRouteIds, stoppedOnly, movingOnly, textFields[0].getText(), textFields[1].getText(), delay));
	}

	private void setListVisibility(boolean visible) {
		availableSoundsList.x = visible ? (width - SQUARE_SIZE * 2) / 3 + SQUARE_SIZE : width;
	}
}
