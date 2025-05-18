package org.mtr.mod.item;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ItemExtension;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ItemDriverKey extends ItemExtension {

	public final boolean canDrive;
	public final boolean canOpenDoors;
	public final boolean canBoardAnyVehicle;
	public final int color;

	public ItemDriverKey(ItemSettings itemSettings, boolean canDrive, boolean canOpenDoors, boolean canBoardAnyVehicle, int color) {
		super(itemSettings.maxCount(1));
		this.canDrive = canDrive;
		this.canOpenDoors = canOpenDoors;
		this.canBoardAnyVehicle = canBoardAnyVehicle;
		this.color = color;
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		if (canBoardAnyVehicle) {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_CAN_BOARD_ANY_VEHICLE_TRUE.getMutableText().formatted(TextFormatting.GOLD));
		}
		tooltip.add((canDrive ? TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_DRIVE_FALSE).getMutableText().formatted(canDrive ? TextFormatting.GRAY : TextFormatting.DARK_GRAY));
		tooltip.add((canOpenDoors ? TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_TRUE : TranslationProvider.TOOLTIP_MTR_CAN_OPEN_DOORS_FALSE).getMutableText().formatted(canOpenDoors ? TextFormatting.GRAY : TextFormatting.DARK_GRAY));
	}
}
