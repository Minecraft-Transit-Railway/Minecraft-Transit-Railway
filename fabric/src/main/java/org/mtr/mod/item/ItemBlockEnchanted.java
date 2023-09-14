package org.mtr.mod.item;

import org.mtr.mapping.holder.Block;
import org.mtr.mapping.holder.ItemSettings;
import org.mtr.mapping.holder.ItemStack;
import org.mtr.mapping.mapper.BlockItemExtension;

public class ItemBlockEnchanted extends BlockItemExtension {

	public ItemBlockEnchanted(Block block, ItemSettings itemSettings) {
		super(block, itemSettings);
	}

	@Override
	public boolean hasGlint2(ItemStack stack) {
		return true;
	}
}
