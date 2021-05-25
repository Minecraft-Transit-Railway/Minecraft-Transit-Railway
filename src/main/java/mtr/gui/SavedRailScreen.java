package mtr.gui;

import mtr.data.Platform;
import mtr.data.SavedRailBase;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class SavedRailScreen extends Screen implements IGui, IPacket {

	private final SavedRailBase savedRailBase;
	private final DashboardScreen dashboardScreen;
	private final TextFieldWidget textFieldPlatformNumber;
	private final WidgetShorterSlider sliderDwellTime;

	private final Text platformNumberText;
	private final Text dwellTimeText = new TranslatableText("gui.mtr.dwell_time");

	private final int textWidth, startX;

	private static final int MAX_PLATFORM_NAME_LENGTH = 10;
	private static final int SLIDER_WIDTH = 160;

	public SavedRailScreen(SavedRailBase savedRailBase, DashboardScreen dashboardScreen) {
		super(new LiteralText(""));
		this.savedRailBase = savedRailBase;
		this.dashboardScreen = dashboardScreen;
		platformNumberText = new TranslatableText(savedRailBase instanceof Platform ? "gui.mtr.platform_number" : "gui.mtr.siding_number");

		textRenderer = MinecraftClient.getInstance().textRenderer;
		textFieldPlatformNumber = new TextFieldWidget(textRenderer, 0, 0, 0, SQUARE_SIZE, new LiteralText(""));

		textWidth = Math.max(textRenderer.getWidth(platformNumberText), textRenderer.getWidth(dwellTimeText)) + TEXT_PADDING;
		startX = (width - textWidth - SLIDER_WIDTH) / 2 + SLIDER_WIDTH;

		sliderDwellTime = new WidgetShorterSlider(startX + textWidth, SLIDER_WIDTH, Platform.MAX_DWELL_TIME - 1, value -> {
			if (savedRailBase instanceof Platform) {
				((Platform) savedRailBase).setDwellTime(value + 1, packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_PLATFORM, packet));
			}
		}, value -> String.format("%ss", (value + 1) / 2F));
	}

	@Override
	protected void init() {
		super.init();

		IGui.setPositionAndWidth(textFieldPlatformNumber, startX + textWidth + TEXT_FIELD_PADDING / 2, height / 2 - SQUARE_SIZE - TEXT_FIELD_PADDING / 2, SLIDER_WIDTH - TEXT_FIELD_PADDING);
		textFieldPlatformNumber.setText(savedRailBase.name);
		textFieldPlatformNumber.setMaxLength(MAX_PLATFORM_NAME_LENGTH);
		textFieldPlatformNumber.setChangedListener(text -> {
			textFieldPlatformNumber.setSuggestion(text.isEmpty() ? "1" : "");
			savedRailBase.name = textFieldPlatformNumber.getText();
		});

		addChild(textFieldPlatformNumber);

		if (savedRailBase instanceof Platform) {
			sliderDwellTime.y = height / 2 + TEXT_FIELD_PADDING / 2;
			sliderDwellTime.setHeight(SQUARE_SIZE);
			sliderDwellTime.setValue(((Platform) savedRailBase).getDwellTime() - 1);
			addButton(sliderDwellTime);
		}
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
			if (savedRailBase instanceof Platform) {
				textRenderer.draw(matrices, dwellTimeText, startX, height / 2F + TEXT_FIELD_PADDING / 2F + TEXT_PADDING, ARGB_WHITE);
			}
			super.render(matrices, mouseX, mouseY, delta);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClose() {
		super.onClose();
		savedRailBase.setNameColor(packet -> PacketTrainDataGuiClient.sendUpdate(PACKET_UPDATE_PLATFORM, packet));
		if (client != null) {
			client.openScreen(dashboardScreen);
		}
	}

	@Override
	public boolean isPauseScreen() {
		return false;
	}
}
