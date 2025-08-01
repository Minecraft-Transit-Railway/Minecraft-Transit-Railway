package org.mtr.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.mtr.generated.lang.TranslationProvider;

import java.util.List;

public abstract class ItemDriverKey extends Item {

	public final boolean canDrive;
	public final boolean canOpenDoors;
	public final boolean canBoardAnyVehicle;
	public final int color;

	public ItemDriverKey(Item.Settings settings, boolean canDrive, boolean canOpenDoors, boolean canBoardAnyVehicle, int color) {
		super(settings.maxCount(1));
		this.canDrive = canDrive;
		this.canOpenDoors = canOpenDoors;
		this.canBoardAnyVehicle = canBoardAnyVehicle;
		this.color = color;
	}

	@Override
	public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
		if (canBoardAnyVehicle) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_CAN_BOARD_ANY_VEHICLE_TRUE.getMutableText().formatted(Formatting.GOLD));
		}
		tooltip.add((canDrive ? TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_FALSE).getMutableText().formatted(canDrive ? Formatting.GRAY : Formatting.DARK_GRAY));
		tooltip.add((canOpenDoors ? TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_FALSE).getMutableText().formatted(canOpenDoors ? Formatting.GRAY : Formatting.DARK_GRAY));
	}
}
