package mtr.gui;

import mtr.block.BlockTrainAnnouncer;
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

public class TrainAnnouncerScreen extends Screen implements IGui, IPacket {

	private final BlockPos pos;
	private final String initialMessage;
	private final TextFieldWidget textFieldMessage;
	private final Text text = new TranslatableText("gui.mtr.announcement_message");

	private static final int MAX_MESSAGE_LENGTH = 256;

	public TrainAnnouncerScreen(BlockPos pos) {
		super(new LiteralText(""));

		client = MinecraftClient.getInstance();
		this.pos = pos;
		textFieldMessage = new TextFieldWidget(client.textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		final World world = client.world;
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
		IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		textFieldMessage.setMaxLength(MAX_MESSAGE_LENGTH);
		textFieldMessage.setText(initialMessage);
		addChild(textFieldMessage);
	}

	@Override
	public void tick() {
		textFieldMessage.tick();
	}

	@Override
	public void onClose() {
		PacketTrainDataGuiClient.sendTrainAnnouncerMessageC2S(pos, IGui.formatStationName(textFieldMessage.getText()));
		super.onClose();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			textRenderer.draw(matrices, text, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			textFieldMessage.render(matrices, mouseX, mouseY, delta);
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
