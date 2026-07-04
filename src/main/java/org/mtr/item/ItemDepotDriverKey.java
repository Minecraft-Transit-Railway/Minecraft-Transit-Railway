package org.mtr.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.mtr.core.data.Depot;
import org.mtr.core.tool.Utilities;
import org.mtr.generated.lang.TranslationProvider;
import org.mtr.registry.DataComponentTypes;

import java.util.List;
import java.util.Objects;

public final class ItemDepotDriverKey extends ItemDriverKey {

	public ItemDepotDriverKey(Item.Properties settings, boolean canDrive, boolean canOpenDoors, boolean canBoardAnyVehicle, int color) {
		super(settings, canDrive, canOpenDoors, canBoardAnyVehicle, color);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag type) {
		if (isUsable(stack)) {
			final long timeout = (getExpiryTime(stack) - System.currentTimeMillis()) / Utilities.MILLIS_PER_SECOND;
			final long hours = timeout / 3600;
			final long minutes = (timeout % 3600) / 60;
			final long seconds = timeout % 60;
			tooltip.add(TranslationProvider.TOOLTIP_MTR_EXPIRES_IN.getMutableText(hours == 0 ? String.format("%02d:%02d", minutes, seconds) : String.format("%d:%02d:%02d", hours, minutes, seconds)).withStyle(ChatFormatting.GOLD));
		} else {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_EXPIRED.getMutableText().withStyle(ChatFormatting.RED));
		}
		super.appendHoverText(stack, context, tooltip, type);
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
