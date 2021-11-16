package mtr.gui;

import mtr.block.BlockTrainScheduleSensor;
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

public class EditTrainScheduleSensorScreen extends Screen implements IGui, IPacket {

	private final BlockPos pos;
	private final int seconds;
	private final TextFieldWidget textFieldSeconds;
	private final Text text = new TranslatableText("gui.mtr.train_schedule_sensor");

	private static final int MAX_SECONDS_LENGTH = 3;

	public EditTrainScheduleSensorScreen(BlockPos pos) {
		super(new LiteralText(""));
		this.pos = pos;

		client = MinecraftClient.getInstance();
		textFieldSeconds = new TextFieldWidget(client.textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		final World world = client.world;
		if (world == null) {
			seconds = 0;
		} else {
			final BlockEntity entity = world.getBlockEntity(pos);
			seconds = entity instanceof BlockTrainScheduleSensor.TileEntityTrainScheduleSensor ? ((BlockTrainScheduleSensor.TileEntityTrainScheduleSensor) entity).getSeconds() : 10;
		}
	}

	@Override
	protected void init() {
		super.init();
		IDrawing.setPositionAndWidth(textFieldSeconds, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + TEXT_FIELD_PADDING / 2, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
		textFieldSeconds.setMaxLength(MAX_SECONDS_LENGTH);
		textFieldSeconds.setText(String.valueOf(seconds));
		addChild(textFieldSeconds);
	}

	@Override
	public void tick() {
		textFieldSeconds.tick();
	}

	@Override
	public void onClose() {
		int secondsParsed = 10;
		try {
			secondsParsed = Integer.parseInt(textFieldSeconds.getText());
		} catch (Exception ignored) {
		}
		PacketTrainDataGuiClient.sendScheduleSensorC2S(pos, secondsParsed);
		super.onClose();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			textRenderer.draw(matrices, text, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
			textFieldSeconds.render(matrices, mouseX, mouseY, delta);
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
