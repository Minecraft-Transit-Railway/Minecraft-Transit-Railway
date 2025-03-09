package org.mtr.item;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemBlockEnchanted extends BlockItem {

	public ItemBlockEnchanted(Block block, Item.Settings itemSettings) {
		super(block, itemSettings);
	}

	@Override
	public boolean hasGlint(ItemStack stack) {
		return true;
	}
}
