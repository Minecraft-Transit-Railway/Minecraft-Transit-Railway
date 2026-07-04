package org.mtr.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mtr.data.IGui;

public final class ItemCreativeDriverKey extends ItemDriverKey {

	public ItemCreativeDriverKey(Item.Properties settings) {
		super(settings, true, true, true, IGui.RGB_WHITE);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return true;
	}
}
