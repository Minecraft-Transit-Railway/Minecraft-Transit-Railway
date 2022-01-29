package mtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Utilities;
import mtr.mappings.UtilitiesClient;
import mtr.packet.IPacket;
import mtr.packet.PacketTrainDataGuiClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Items;

public class TicketMachineScreen extends ScreenMapper implements IGui, IPacket {

	private final Button[] buttons = new Button[BUTTON_COUNT];
	private final Component balanceText;

	private static final int EMERALD_TO_DOLLAR = 10;
	private static final int BUTTON_COUNT = 10;
	private static final int BUTTON_WIDTH = 80;

	public TicketMachineScreen(int balance) {
		super(new TextComponent(""));

		for (int i = 0; i < BUTTON_COUNT; i++) {
			final int index = i;
			buttons[i] = new Button(0, 0, 0, SQUARE_SIZE, new TranslatableComponent("gui.mtr.add_value"), button -> {
				PacketTrainDataGuiClient.addBalanceC2S(getAddAmount(index), (int) Math.pow(2, index));
				if (minecraft != null) {
					UtilitiesClient.setScreen(minecraft, null);
				}
			});
		}

		balanceText = new TranslatableComponent("gui.mtr.balance", balance);
	}

	@Override
	protected void init() {
		super.init();

		for (int i = 0; i < BUTTON_COUNT; i++) {
			IDrawing.setPositionAndWidth(buttons[i], width - BUTTON_WIDTH, SQUARE_SIZE * (i + 1), BUTTON_WIDTH - TEXT_FIELD_PADDING);
		}

		for (final Button button : buttons) {
			addDrawableChild(button);
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
	public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
		try {
			renderBackground(matrices);
			final Component emeraldsText = new TranslatableComponent("gui.mtr.emeralds", getEmeraldCount());
			font.draw(matrices, balanceText, TEXT_PADDING, TEXT_PADDING, ARGB_WHITE);
			font.draw(matrices, emeraldsText, width - TEXT_PADDING - font.width(emeraldsText), TEXT_PADDING, ARGB_WHITE);

			for (int i = 0; i < BUTTON_COUNT; i++) {
				font.draw(matrices, new TranslatableComponent("gui.mtr.add_balance_for_emeralds", getAddAmount(i), (int) Math.pow(2, i)), TEXT_PADDING, (i + 1) * SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
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

	private int getEmeraldCount() {
		if (minecraft != null && minecraft.player != null) {
			return Utilities.getInventory(minecraft.player).countItem(Items.EMERALD);
		} else {
			return 0;
		}
	}

	private static int getAddAmount(int index) {
		return (int) Math.ceil(Math.pow(2, index) * (EMERALD_TO_DOLLAR + index));
	}
}
