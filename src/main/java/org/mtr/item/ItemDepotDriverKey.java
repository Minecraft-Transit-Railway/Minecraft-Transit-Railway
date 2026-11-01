package org.mtr.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
//? if >= 26.1 {
/*import net.minecraft.world.item.component.TooltipDisplay;
*///? }
import org.mtr.core.data.Depot;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.DataComponentTypes;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public final class ItemDepotDriverKey extends ItemDriverKey {

	public ItemDepotDriverKey(Item.Properties settings, boolean canDrive, boolean canOpenDoors, boolean canBoardAnyVehicle, int color) {
		super(settings, canDrive, canOpenDoors, canBoardAnyVehicle, color);
	}

	@Override
//? if >= 26.1 {
/*	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> tooltip, TooltipFlag type) {
*///? } else {
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipList, TooltipFlag type) {
		final Consumer<Component> tooltip = tooltipList::add;
//? }
		if (isUsable(stack)) {
			final long timeout = (getExpiryTime(stack) - System.currentTimeMillis()) / Utilities.MILLIS_PER_SECOND;
			final long hours = timeout / 3600;
			final long minutes = (timeout % 3600) / 60;
			final long seconds = timeout % 60;
			tooltip.accept(TranslationProvider.TOOLTIP_MTR_EXPIRES_IN.getMutableText(hours == 0 ? String.format("%02d:%02d", minutes, seconds) : String.format("%d:%02d:%02d", hours, minutes, seconds)).withStyle(ChatFormatting.GOLD));
		} else {
			tooltip.accept(TranslationProvider.TOOLTIP_MTR_EXPIRED.getMutableText().withStyle(ChatFormatting.RED));
		}
//? if >= 26.1 {
/*		super.appendHoverText(stack, context, tooltipDisplay, tooltip, type);
*///? } else {
		super.appendHoverText(stack, context, tooltipList, type);
//? }
	}

	public static void setData(ItemStack itemStack, Depot depot, long timeout) {
		if (itemStack.getItem() instanceof ItemDepotDriverKey) {
			itemStack.set(DataComponentTypes.DEPOT_ID.get(), depot.getId());
			itemStack.set(DataComponentTypes.EXPIRY_TIME.get(), System.currentTimeMillis() + timeout);
		}
	}

	public static boolean isCreativeDriverKeyOrMatchesDepot(ItemStack itemStack, long depotId) {
		return itemStack.getItem() instanceof ItemCreativeDriverKey || isUsable(itemStack) && Objects.equals(itemStack.get(DataComponentTypes.DEPOT_ID.get()), depotId);
	}

	private static boolean isUsable(ItemStack itemStack) {
		return itemStack.getItem() instanceof ItemDepotDriverKey && getExpiryTime(itemStack) > System.currentTimeMillis();
	}

	private static long getExpiryTime(ItemStack itemStack) {
		final Long expiryTime = itemStack.get(DataComponentTypes.EXPIRY_TIME.get());
		return expiryTime == null ? 0 : expiryTime;
	}
}
