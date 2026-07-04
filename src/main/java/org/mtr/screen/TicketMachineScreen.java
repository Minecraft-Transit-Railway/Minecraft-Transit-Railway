package org.mtr.screen;

import gg.essential.elementa.components.ScrollComponent;
import gg.essential.elementa.components.UIWrappedText;
import gg.essential.elementa.constraints.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Items;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectImmutableList;
import org.mtr.packet.PacketAddBalance;
import org.mtr.registry.RegistryClient;
import org.mtr.tool.GuiHelper;
import org.mtr.widget.BackgroundComponent;
import org.mtr.widget.ButtonComponent;
import org.mtr.widget.ScrollPanelComponent;

import java.awt.*;

/**
 * Adds ticket machine balance with a vertically stacked list of preset emerald values.
 */
public final class TicketMachineScreen extends WindowBase {

	private int balance;

	private final UIWrappedText balanceText;
	private final UIWrappedText emeraldsText;
	private final ButtonComponent[] buttons = new ButtonComponent[BUTTON_COUNT];

	private static final int BUTTON_COUNT = 10;

	public TicketMachineScreen(int balance) {
		this.balance = balance;
		final BackgroundComponent backgroundComponent = new BackgroundComponent(getWindow(), ObjectImmutableList.of());

		balanceText = (UIWrappedText) new UIWrappedText("", false)
			.setChildOf(backgroundComponent)
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		emeraldsText = (UIWrappedText) new UIWrappedText("", false)
			.setChildOf(backgroundComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setColor(new Color(GuiHelper.MINECRAFT_GUI_TITLE_TEXT_COLOR));

		final ScrollComponent scrollComponent = ((ScrollPanelComponent) new ScrollPanelComponent(true)
			.setChildOf(backgroundComponent)
			.setY(new SiblingConstraint(GuiHelper.DEFAULT_PADDING))
			.setWidth(new RelativeConstraint())
			.setHeight(new SubtractiveConstraint(new FillConstraint(), new PixelConstraint(GuiHelper.DEFAULT_PADDING * 2)))).contentContainer;

		for (int i = 0; i < BUTTON_COUNT; i++) {
			buttons[i] = (ButtonComponent) new ButtonComponent(false)
				.setChildOf(scrollComponent)
				.setY(new SiblingConstraint())
				.setWidth(new RelativeConstraint());

			final int addBalanceAmount = PacketAddBalance.getAddBalanceAmount(i);
			buttons[i].setText(TranslationProvider.GUI_MTR_ADD_BALANCE_FOR_EMERALDS.getString(addBalanceAmount, PacketAddBalance.getEmeraldAmount(i)));
			final int index = i;
			buttons[i].onClick(() -> {
				RegistryClient.sendPacketToServer(new PacketAddBalance(index));
				this.balance += addBalanceAmount;
				updateButtons();
			});
		}

		updateButtons();
	}

	@Override
	public void onTick() {
		super.onTick();
		balanceText.setText(TranslationProvider.GUI_MTR_BALANCE.getString(balance));
		emeraldsText.setText(TranslationProvider.GUI_MTR_EMERALDS.getString(getEmeraldCount()));
	}

	private int getEmeraldCount() {
		final LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;
		final Inventory playerInventory = clientPlayerEntity == null ? null : clientPlayerEntity.getInventory();
		return playerInventory == null ? 0 : playerInventory.countItem(Items.EMERALD);
	}

	private void updateButtons() {
		for (int i = 0; i < BUTTON_COUNT; i++) {
			buttons[i].setDisabled(getEmeraldCount() < PacketAddBalance.getEmeraldAmount(i));
		}
	}
}
