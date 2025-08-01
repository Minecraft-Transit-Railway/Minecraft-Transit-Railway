package org.mtr.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.mtr.data.IGui;

public final class ItemCreativeDriverKey extends ItemDriverKey {

	public ItemCreativeDriverKey(Item.Settings settings) {
		super(settings, true, true, true, IGui.RGB_WHITE);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}
