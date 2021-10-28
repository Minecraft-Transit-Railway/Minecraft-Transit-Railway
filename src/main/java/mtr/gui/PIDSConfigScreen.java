package mtr.gui;

import mtr.block.BlockPIDSBase;
import mtr.data.IGui;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
	private final TextFieldWidget[] textFieldMessages;
	private final Text text = new TranslatableText("gui.mtr.pids_message");

	private static final int MAX_MESSAGE_LENGTH = 256;

	public PIDSConfigScreen(BlockPos pos1, BlockPos pos2, int maxArrivals) {
		super(new LiteralText(""));
		this.pos1 = pos1;
		this.pos2 = pos2;
		messages = new String[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			messages[i] = "";
		}

		client = MinecraftClient.getInstance();
		textFieldMessages = new TextFieldWidget[maxArrivals];
		for (int i = 0; i < maxArrivals; i++) {
			textFieldMessages[i] = new TextFieldWidget(client.textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));
		}

		final World world = client.world;
		if (world != null) {
			final BlockEntity entity = world.getBlockEntity(pos1);
			if (entity instanceof BlockPIDSBase.TileEntityBlockPIDSBase) {
				for (int i = 0; i < maxArrivals; i++) {
					messages[i] = ((BlockPIDSBase.TileEntityBlockPIDSBase) entity).getMessage(i);
				}
			}
		}
	}

	@Override
	protected void init() {
		super.init();
		for (int i = 0; i < textFieldMessages.length; i++) {
			final TextFieldWidget textFieldMessage = textFieldMessages[i];
			IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
			textFieldMessage.setMaxLength(MAX_MESSAGE_LENGTH);
			textFieldMessage.setText(messages[i]);
			addChild(textFieldMessage);
		}
	}

	@Override
	public void tick() {
		for (final TextFieldWidget textFieldMessage : textFieldMessages) {
			textFieldMessage.tick();
		}
	}

	@Override
	public void onClose() {
		for (int i = 0; i < textFieldMessages.length; i++) {
			messages[i] = textFieldMessages[i].getText();
		}
		PacketTrainDataGuiClient.sendPIDSConfigC2S(pos1, pos2, messages);
		super.onClose();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			textRenderer.draw(matrices, text, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			for (final TextFieldWidget textFieldMessage : textFieldMessages) {
				textFieldMessage.render(matrices, mouseX, mouseY, delta);
			}
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