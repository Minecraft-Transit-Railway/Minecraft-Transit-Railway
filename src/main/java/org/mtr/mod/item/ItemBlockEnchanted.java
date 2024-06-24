package mtr.item;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class ItemBlockEnchanted extends BlockItem {

	public ItemBlockEnchanted(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public boolean isFoil(ItemStack itemStack) {
		return true;
	}
}
