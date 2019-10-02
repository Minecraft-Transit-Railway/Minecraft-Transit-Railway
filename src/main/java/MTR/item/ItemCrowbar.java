package mtr.item;

import java.util.List;

import mtr.entity.EntityTrain;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemCrowbar extends Item {

	public ItemCrowbar() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TOOLS);
	}

	public EntityTrain train = null;

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		if (train != null)
			tooltip.add(train.toString());
	}
}
