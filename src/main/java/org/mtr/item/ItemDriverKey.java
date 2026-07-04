package org.mtr.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

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
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		if (canBoardAnyVehicle) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_CAN_BOARD_ANY_VEHICLE_TRUE.getMutableText().withStyle(ChatFormatting.GOLD));
		}
		tooltip.add((canDrive ? TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_FALSE).getMutableText().withStyle(canDrive ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY));
		tooltip.add((canOpenDoors ? TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_FALSE).getMutableText().withStyle(canOpenDoors ? ChatFormatting.GRAY : ChatFormatting.DARK_GRAY));
	}
}
