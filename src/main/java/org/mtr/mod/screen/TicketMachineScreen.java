package org.mtr.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.packet.IPacket;
import org.mtr.mod.packet.PacketAddBalance;

public class TicketMachineScreen extends ScreenExtension implements IGui, IPacket {

	private final ButtonWidgetExtension[] buttons = new ButtonWidgetExtension[BUTTON_COUNT];
	private final MutableText balanceText;

	private static final int BUTTON_COUNT = 10;
	private static final int BUTTON_WIDTH = 80;

	public TicketMachineScreen(int balance) {
		super();

		for (int i = 0; i < BUTTON_COUNT; i++) {
			final int index = i;
			buttons[i] = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.translatable("gui.mtr.add_value"), button -> {
				RegistryClient.sendPacketToServer(new PacketAddBalance(index));
				MinecraftClient.getInstance().openScreen(null);
			});
		}

		balanceText = TextHelper.translatable("gui.mtr.balance", balance);
	}

	@Override
	protected void init2() {
		super.init2();

		for (int i = 0; i < BUTTON_COUNT; i++) {
			IDrawing.setPositionAndWidth(buttons[i], width - BUTTON_WIDTH, SQUARE_SIZE * (i + 1), BUTTON_WIDTH - TEXT_FIELD_PADDING);
		}

		for (final ButtonWidgetExtension button : buttons) {
			addChild(new ClickableWidget(button));
		}
	}

	@Override
	public void tick2() {
		final int emeraldCount = getEmeraldCount();
		for (int i = 0; i < BUTTON_COUNT; i++) {
			buttons[i].active = emeraldCount >= Math.pow(2, i);
		}
	}

	@Override
	public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
		renderBackground(graphicsHolder);
		final MutableText emeraldsText = TextHelper.translatable("gui.mtr.emeralds", getEmeraldCount());
		graphicsHolder.drawText(balanceText, TEXT_PADDING, TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		graphicsHolder.drawText(emeraldsText, width - TEXT_PADDING - GraphicsHolder.getTextWidth(emeraldsText), TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);

		for (int i = 0; i < BUTTON_COUNT; i++) {
			graphicsHolder.drawText(TextHelper.translatable("gui.mtr.add_balance_for_emeralds", PacketAddBalance.getAddAmount(i), (int) Math.pow(2, i)), TEXT_PADDING, (i + 1) * SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
		}

		super.render(graphicsHolder, mouseX, mouseY, delta);
	}

	@Override
	public boolean isPauseScreen2() {
		return false;
	}

	private int getEmeraldCount() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
		final PlayerInventory playerInventory = clientPlayerEntity == null ? null : PlayerHelper.getPlayerInventory(new PlayerEntity(clientPlayerEntity.data));
		return playerInventory == null ? 0 : playerInventory.count(Items.getEmeraldMapped());
	}
}
