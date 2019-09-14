package mtr.item;

import mtr.entity.EntityTrain;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.items.IItemHandler;

public class ItemCrowbar extends Item {

	public ItemCrowbar() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TOOLS);
	}

	@CapabilityInject(IItemHandler.class)
	public EntityTrain train = null;

	@CapabilityInject(IItemHandler.class)
	static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;
}
