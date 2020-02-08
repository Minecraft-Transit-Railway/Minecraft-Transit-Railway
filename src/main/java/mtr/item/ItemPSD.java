package mtr.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class ItemPSD extends ItemPSDAPGBase {

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		super.getSubItems(tab, items);
		if (isInCreativeTab(tab))
			items.add(new ItemStack(this, 1, 2));
	}
}
