package org.mtr.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//? if >= 26.1 {
/*import net.minecraft.world.item.component.TooltipDisplay;
*///? }
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;
import java.util.function.Consumer;

public abstract class ItemDriverKey extends Item {

	public final boolean canDrive;
	public final boolean canOpenDoors;
	public final boolean canBoardAnyVehicle;
	public final int color;

	public ItemDriverKey(Item.Properties settings, boolean canDrive, boolean canOpenDoors, boolean canBoardAnyVehicle, int color) {
		super(settings.stacksTo(1));
		this.canDrive = canDrive;
		this.canOpenDoors = canOpenDoors;
		this.canBoardAnyVehicle = canBoardAnyVehicle;
		this.color = color;
	}

	@Override
//? if >= 26.1 {
/*	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag type) {
*///? } else {
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipList, TooltipFlag type) {
		final Consumer<Component> tooltip = tooltipList::add;
//? }
		if (canBoardAnyVehicle) {
			tooltip.accept(TranslationProvider.TOOLTIP_MTR_CAN_BOARD_ANY_VEHICLE_TRUE.getMutableText().withStyle(ChatFormatting.GOLD));
		}
		tooltip.accept((canDrive ? TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_FALSE).getMutableText().withStyle(canDrive ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY));
		tooltip.accept((canOpenDoors ? TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_FALSE).getMutableText().withStyle(canOpenDoors ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY));
	}
}
