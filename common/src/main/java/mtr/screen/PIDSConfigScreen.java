package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.block.BlockPIDSBase;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class PIDSConfigScreen extends ScreenMapper implements IGui, IPacket {

	private final BlockPos pos1;
	private final BlockPos pos2;
	private final String[] messages;
	private final boolean[] hideArrival;
	private final WidgetBetterTextField[] textFieldMessages;
	private final WidgetBetterCheckbox[] buttonsHideArrival;
	private final Component messageText = Text.translatable("gui.mtr.pids_message");
	private final Component hideArrivalText = Text.translatable("gui.mtr.hide_arrival");

	private static final int MAX_MESSAGE_LENGTH = 2048;

	public PIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals) {
		super(Text.literal(""));
		this.pos1 = pos1;
		this.pos2 = pos2;
		messages = new String[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = "";
		}
		hideArrival = new boolean[maxArrivals];

		textFieldMessages = new WidgetBetterTextField[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			textFieldMessages[i] = new WidgetBetterTextField("", MAX_MESSAGE_LENGTH);
		}

		buttonsHideArrival = new WidgetBetterCheckbox[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			buttonsHideArrival[i] = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, hideArrivalText, checked -> {
			});
		}

		final Level world = Minecraft.getInstance().level;
		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(pos1);
			if (entity instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				for (int i = 0; i < maxArrivals; i++) {
					messages[i] = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getMessage(i);
					hideArrival[i] = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getHideArrival(i);
				}
			}
		}
	}

	@Override
	protected void init() {
		super.init();
		final int textWidth = font.width(hideArrivalText) + SQUARE_SIZE + TEXT_PADDING * 2;

		for (int i = 0; i < textFieldMessages.length; i++) {
			final WidgetBetterTextField textFieldMessage = textFieldMessages[i];
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
			textFieldMessage.setValue(messages[i]);
			addDrawableChild(textFieldMessage);

			final WidgetBetterCheckbox buttonHideArrival = buttonsHideArrival[i];
			IDrawing.setPositionAndWidth(buttonHideArrival, width - SQUARE_SIZE - textWidth + TEXT_PADDING, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, textWidth);
			buttonHideArrival.setChecked(hideArrival[i]);
			addDrawableChild(buttonHideArrival);
		}
	}

	@Override
	public void tick() {
		for (final WidgetBetterTextField textFieldMessage : textFieldMessages) {
			textFieldMessage.tick();
		}
	}

	@Override
	public void onClose() {
		for (int i = 0; i < textFieldMessages.length; i++) {
			messages[i] = textFieldMessages[i].getValue();
			hideArrival[i] = buttonsHideArrival[i].selected();
		}
		PacketTrainDataGuiClient.sendPIDSConfigC2S(pos1, pos2, messages, hideArrival);
		super.onClose();
	}

	@Override
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			font.draw(matrices, messageText, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}