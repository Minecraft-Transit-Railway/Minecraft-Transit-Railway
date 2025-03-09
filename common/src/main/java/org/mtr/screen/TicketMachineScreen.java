package org.mtr.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import org.mtr.client.IDrawing;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketAddBalance;
import org.mtr.registry.RegistryClient;

public class TicketMachineScreen extends MTRScreenBase implements IGui {

	private final ButtonWidget[] buttons = new ButtonWidget[BUTTON_COUNT];
	private final MutableText balanceText;

	private static final int BUTTON_COUNT = 10;
	private static final int BUTTON_WIDTH = 80;

	public TicketMachineScreen(int balance) {
		super();

		for (int i = 0; i < BUTTON_COUNT; i++) {
			final int index = i;
			buttons[i] = ButtonWidget.builder(TranslationProvider.GUI_MTR_ADD_VALUE.getMutableText(), button -> {
				RegistryClient.sendPacketToServer(new PacketAddBalance(index));
				MinecraftClient.getInstance().setScreen(null);
			}).build();
		}

		balanceText = TranslationProvider.GUI_MTR_BALANCE.getMutableText(balance);
	}

	@Override
	protected void init() {
		super.init();

		for (int i = 0; i < BUTTON_COUNT; i++) {
			IDrawing.setPositionAndWidth(buttons[i], width - BUTTON_WIDTH, SQUARE_SIZE * (i + 1), BUTTON_WIDTH - TEXT_FIELD_PADDING);
		}

		for (final ButtonWidget button : buttons) {
			addSelectableChild(button);
		}
	}

	@Override
	public void tick() {
		final int emeraldCount = getEmeraldCount();
		for (int i = 0; i < BUTTON_COUNT; i++) {
			buttons[i].active = emeraldCount >= Math.pow(2, i);
		}
	}

	@Override
	public void render(DrawContext context, int mouseX, int mouseY, float delta) {
		renderBackground(context, mouseX, mouseY, delta);
		final MutableText emeraldsText = TranslationProvider.GUI_MTR_EMERALDS.getMutableText(getEmeraldCount());
		context.drawText(textRenderer, balanceText, TEXT_PADDING, TEXT_PADDING, ARGB_WHITE, false);
		context.drawText(textRenderer, emeraldsText, width - TEXT_PADDING - textRenderer.getWidth(emeraldsText), TEXT_PADDING, ARGB_WHITE, false);

		for (int i = 0; i < BUTTON_COUNT; i++) {
			context.drawText(textRenderer, TranslationProvider.GUI_MTR_ADD_BALANCE_FOR_EMERALDS.getMutableText(PacketAddBalance.getAddAmount(i), (int) Math.pow(2, i)), TEXT_PADDING, (i + 1) * SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false);
		}

		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	public boolean shouldPause() {
		return false;
	}

	private int getEmeraldCount() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		final PlayerInventory playerInventory = clientPlayerEntity == null ? null : clientPlayerEntity.getInventory();
		return playerInventory == null ? 0 : playerInventory.count(Items.EMERALD);
	}
}
