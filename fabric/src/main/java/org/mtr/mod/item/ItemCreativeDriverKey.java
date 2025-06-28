package org.mtr.mod.item;

import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mod.data.IGui;

public final class ItemCreativeDriverKey extends ItemDriverKey {

	public ItemCreativeDriverKey(ItemSettings itemSettings) {
		super(itemSettings, true, true, true, IGui.RGB_WHITE);
	}

	@Override
	public boolean hasGlint2(ItemStack stack) {
		return true;
	}
}
