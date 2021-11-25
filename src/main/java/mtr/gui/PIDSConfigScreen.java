package mtr.gui;

import mtr.block.BlockPIDSBase;
import mtr.data.IGui;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PIDSConfigScreen extends Screen implements IGui, IPacket {

	private final BlockPos pos1;
	private final BlockPos pos2;
	private final String[] messages;
	private final boolean[] hideArrival;
	private final WidgetBetterTextField[] textFieldMessages;
	private final WidgetBetterCheckbox[] buttonsHideArrival;
	private final Text messageText = new TranslatableText("gui.mtr.pids_message");
	private final Text hideArrivalText = new TranslatableText("gui.mtr.hide_arrival");

	private static final int MAX_MESSAGE_LENGTH = 2048;

	public PIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals) {
		super(new LiteralText(""));
		this.pos1 = pos1;
		this.pos2 = pos2;
		messages = new String[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = "";
		}
		hideArrival = new boolean[maxArrivals];

		textFieldMessages = new WidgetBetterTextField[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			textFieldMessages[i] = new WidgetBetterTextField(null, "");
		}

		buttonsHideArrival = new WidgetBetterCheckbox[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			buttonsHideArrival[i] = new WidgetBetterCheckbox(0, 0, 0, SQUARE_SIZE, hideArrivalText, checked -> {
			});
		}

		final World world = MinecraftClient.getInstance().world;
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
		final int textWidth = textRenderer.getWidth(hideArrivalText) + SQUARE_SIZE + TEXT_PADDING * 2;

		for (int i = 0; i < textFieldMessages.length; i++) {
			final WidgetBetterTextField textFieldMessage = textFieldMessages[i];
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
			textFieldMessage.setMaxLength(MAX_MESSAGE_LENGTH);
			textFieldMessage.setText(messages[i]);
			addButton(textFieldMessage);

			final WidgetBetterCheckbox buttonHideArrival = buttonsHideArrival[i];
			IDrawing.setPositionAndWidth(buttonHideArrival, width - SQUARE_SIZE - textWidth + TEXT_PADDING, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, textWidth);
			buttonHideArrival.setChecked(hideArrival[i]);
			addButton(buttonHideArrival);
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
			messages[i] = textFieldMessages[i].getText();
			hideArrival[i] = buttonsHideArrival[i].isChecked();
		}
		PacketTrainDataGuiClient.sendPIDSConfigC2S(pos1, pos2, messages, hideArrival);
		super.onClose();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			textRenderer.draw(matrices, messageText, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
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