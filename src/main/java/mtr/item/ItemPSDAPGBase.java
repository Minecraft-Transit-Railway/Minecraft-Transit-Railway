package mtr.item;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public abstract class ItemPSDAPGBase extends Item {

	public ItemPSDAPGBase() {
		super();
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.REDSTONE);
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		switch (stack.getItemDamage()) {
		case 0:
			tooltip.add(new TextComponentTranslation("gui.psd_apg_door").getFormattedText());
			break;
		case 1:
			tooltip.add(new TextComponentTranslation("gui.psd_apg_glass").getFormattedText());
			break;
		case 2:
			tooltip.add(new TextComponentTranslation("gui.psd_apg_glass_end").getFormattedText());
			break;
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab))
			for (int i = 0; i < 3; i++)
				items.add(new ItemStack(this, 1, i));
	}
}
