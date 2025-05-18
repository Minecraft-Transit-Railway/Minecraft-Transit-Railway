package org.mtr.mod.item;

import org.mtr.core.data.Depot;
import org.mtr.mapping.holder.*;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nullable;
import java.util.List;

public final class ItemDepotDriverKey extends ItemDriverKey {

	private static final String TAG_DEPOT_ID = "depot_id";
	private static final String TAG_EXPIRY_TIME = "expiry_time";

	public ItemDepotDriverKey(ItemSettings itemSettings, boolean canDrive, boolean canOpenDoors, boolean canBoardAnyVehicle, int color) {
		super(itemSettings, canDrive, canOpenDoors, canBoardAnyVehicle, color);
	}

	@Override
	public void addTooltips(ItemStack stack, @Nullable World world, List<MutableText> tooltip, TooltipContext options) {
		if (isUsable(stack)) {
			final long timeout = (stack.getOrCreateTag().getLong(TAG_EXPIRY_TIME) - System.currentTimeMillis()) / Depot.MILLIS_PER_SECOND;
			final long hours = timeout / 3600;
			final long minutes = (timeout % 3600) / 60;
			final long seconds = timeout % 60;
			tooltip.add(TranslationProvider.TOOLTIP_MTR_EXPIRES_IN.getMutableText(hours == 0 ? String.format("%02d:%02d", minutes, seconds) : String.format("%d:%02d:%02d", hours, minutes, seconds)).formatted(TextFormatting.GOLD));
		} else {
			tooltip.add(TranslationProvider.TOOLTIP_MTR_EXPIRED.getMutableText().formatted(TextFormatting.RED));
		}
		super.addTooltips(stack, world, tooltip, options);
	}

	public static void setData(ItemStack itemStack, Depot depot, long timeout) {
		if (itemStack.getItem().data instanceof ItemDepotDriverKey) {
			final CompoundTag compoundTag = itemStack.getOrCreateTag();
			compoundTag.putLong(TAG_DEPOT_ID, depot.getId());
			compoundTag.putLong(TAG_EXPIRY_TIME, System.currentTimeMillis() + timeout);
		}
	}

	public static boolean isCreativeDriverKeyOrMatchesDepot(ItemStack itemStack, long depotId) {
		return itemStack.getItem().data instanceof ItemCreativeDriverKey || isUsable(itemStack) && itemStack.getOrCreateTag().getLong(TAG_DEPOT_ID) == depotId;
	}

	private static boolean isUsable(ItemStack itemStack) {
		return itemStack.getItem().data instanceof ItemDepotDriverKey && itemStack.getOrCreateTag().getLong(TAG_EXPIRY_TIME) > System.currentTimeMillis();
	}
}
