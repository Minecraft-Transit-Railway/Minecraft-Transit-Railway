package org.mtr.screen;

import gg.essential.elementa.components.UIContainer;
import gg.essential.elementa.components.UIText;
import gg.essential.elementa.constraints.PixelConstraint;
import gg.essential.elementa.constraints.RelativeConstraint;
import gg.essential.elementa.constraints.SiblingConstraint;
import gg.essential.elementa.constraints.SubtractiveConstraint;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Items;
import org.mtr.data.IGui;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.packet.PacketAddBalance;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.ButtonComponent;

import java.awt.*;

/**
 * Adds ticket machine balance with a vertically stacked list of preset emerald values.
 */
public final class TicketMachineScreen extends SingleTabBackgroundScreenBase implements IGui {

	private final ButtonComponent[] buttons = new ButtonComponent[BUTTON_COUNT];
	private final UIText emeraldsText;

	private static final int BUTTON_COUNT = 10;
	private static final int BUTTON_WIDTH = 80;
	private static final int BUTTON_PANEL_HEIGHT = SQUARE_SIZE * BUTTON_COUNT;

	public TicketMachineScreen(int balance) {
		super(TranslationProvider.BLOCK_MTR_TICKET_MACHINE.getString());

		new UIText(TranslationProvider.GUI_MTR_BALANCE.getMutableText(balance).getString(), false)
			.setChildOf(contentContainer)
			.setX(new PixelConstraint(TEXT_PADDING))
			.setY(new PixelConstraint(TEXT_PADDING))
			.setColor(new Color(GuiHelper.WHITE_COLOR));

		emeraldsText = (UIText) new UIText("", false)
			.setChildOf(contentContainer)
			.setX(new PixelConstraint(TEXT_PADDING, true))
			.setY(new PixelConstraint(TEXT_PADDING))
			.setColor(new Color(GuiHelper.WHITE_COLOR));

		final UIContainer buttonContainer = (UIContainer) new UIContainer()
			.setChildOf(contentContainer)
			.setX(new SubtractiveConstraint(new RelativeConstraint(), new PixelConstraint(BUTTON_WIDTH + SQUARE_SIZE)))
			.setY(new PixelConstraint(SQUARE_SIZE + TEXT_PADDING))
			.setWidth(new PixelConstraint(BUTTON_WIDTH))
			.setHeight(new PixelConstraint(BUTTON_PANEL_HEIGHT));

		for (int i = 0; i < BUTTON_COUNT; i++) {
			final int index = i;
			buttons[i] = (ButtonComponent) new ButtonComponent(false)
				.setChildOf(buttonContainer)
				.setY(i == 0 ? new PixelConstraint(0) : new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
				.setWidth(new RelativeConstraint());
			buttons[i].setText(TranslationProvider.GUI_MTR_ADD_VALUE.getString());
			buttons[i].onClick(() -> {
				RegistryClient.sendPacketToServer(new PacketAddBalance(index));
				MinecraftClient.getInstance().setScreen(null);
			});

			new UIText(TranslationProvider.GUI_MTR_ADD_BALANCE_FOR_EMERALDS.getMutableText(PacketAddBalance.getAddAmount(i), (int) Math.pow(2, i)).getString(), false)
				.setChildOf(contentContainer)
				.setX(new PixelConstraint(TEXT_PADDING))
				.setY(new PixelConstraint(SQUARE_SIZE * (i + 1) + TEXT_PADDING))
				.setColor(new Color(GuiHelper.WHITE_COLOR));
		}

		emeraldsText.setText(TranslationProvider.GUI_MTR_EMERALDS.getMutableText(getEmeraldCount()).getString());
	}

	@Override
	public void onTick() {
		super.onTick();
		final int emeraldCount = getEmeraldCount();
		for (int i = 0; i < BUTTON_COUNT; i++) {
			buttons[i].setDisabled(emeraldCount < Math.pow(2, i));
		}
		emeraldsText.setText(TranslationProvider.GUI_MTR_EMERALDS.getMutableText(emeraldCount).getString());
	}

	private int getEmeraldCount() {
		final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
		final PlayerInventory playerInventory = clientPlayerEntity == null ? null : clientPlayerEntity.getInventory();
		return playerInventory == null ? 0 : playerInventory.count(Items.EMERALD);
	}
}
