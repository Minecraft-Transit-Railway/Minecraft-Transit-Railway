package mtr.gui;

import mtr.data.Platform;
import mtr.packet.IPacket;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class ScheduleScreen extends Screen implements IGui, IPacket {

	private final Platform platform;
	private final TextFieldWidget textFieldPlatformNumber;
	private final WidgetShorterSlider sliderDwellTime;

	private final Text platformNumberText = new TranslatableText("gui.mtr.platform_number");
	private final Text dwellTimeText = new TranslatableText("gui.mtr.dwell_time");

	private final int textWidth, startX;

	private static final int MAX_PLATFORM_NAME_LENGTH = 10;
	private static final int SLIDER_WIDTH = 160;

	public ScheduleScreen(Platform platform) {
		super(new LiteralText(""));
		this.platform = platform;

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldPlatformNumber = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		textWidth = Math.max(textRenderer.getWidth(platformNumberText), textRenderer.getWidth(dwellTimeText)) + TEXT_PADDING;
		startX = (width - textWidth - SLIDER_WIDTH) / 2 + SLIDER_WIDTH;

		sliderDwellTime = new WidgetShorterSlider(startX + textWidth, SLIDER_WIDTH, Platform.MAX_DWELL_TIME - 1, value -> platform.setDwellTime(value + 1, packet -> ClientPlayNetworking.send(PACKET_UPDATE_PLATFORM, packet)), value -> String.format("%ss", (value + 1) / 2F));
	}

	@Override
	protected void init() {
		super.init();

		IGui.setPositionAndWidth(textFieldPlatformNumber, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, SLIDER_WIDTH - TEXT_FIELD_PADDING);
		textFieldPlatformNumber.setText(platform.name);
		textFieldPlatformNumber.setMaxLength(MAX_PLATFORM_NAME_LENGTH);
		textFieldPlatformNumber.setChangedListener(text -> {
			textFieldPlatformNumber.setSuggestion(text.isEmpty() ? "1" : "");
			platform.name = textFieldPlatformNumber.getText();
		});

		addChild(textFieldPlatformNumber);
		addButton(sliderDwellTime);

		sliderDwellTime.y = height / 2 + TEXT_FIELD_PADDING / 2;
		sliderDwellTime.setHeight(SQUARE_SIZE);
		sliderDwellTime.setValue(platform.getDwellTime() - 1);
	}

	@Override
	public void tick() {
		textFieldPlatformNumber.tick();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			textFieldPlatformNumber.render(matrices, mouseX, mouseY, delta);
			textRenderer.draw(matrices, platformNumberText, startX, height / 2F - SQUARE_SIZE - TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			textRenderer.draw(matrices, dwellTimeText, startX, height / 2F + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		platform.setNameColor(packet -> ClientPlayNetworking.send(PACKET_UPDATE_PLATFORM, packet));
		if (client != null) {
			client.openScreen(new DashboardScreen(0));
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
