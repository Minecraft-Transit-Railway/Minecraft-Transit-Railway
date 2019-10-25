package mtr.gui;

import mtr.container.ContainerBridgeCreator;
import mtr.container.ContainerTemplate;
import mtr.item.ItemTemplate;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.items.CapabilityItemHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		switch (ID) {
		case 0:
			if (tileEntity != null)
				return new ContainerBridgeCreator(player.inventory, (IInventory) tileEntity);
			else
				return null;
		case 1:
			return new ContainerTemplate(player.inventory, player.getHeldItem(player.getActiveHand()).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
		default:
			return null;
		}
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		switch (ID) {
		case 0:
			if (tileEntity != null)
				return new GuiBridgeCreator(player.inventory, (IInventory) tileEntity);
			else
				return null;
		case 1:
			ItemStack stack = player.getHeldItemMainhand();
			if (!(stack.getItem() instanceof ItemTemplate))
				stack = player.getHeldItemOffhand();
			if (stack.getItem() instanceof ItemTemplate)
				return new GuiTemplate(player.inventory, stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null));
			else
				return null;
		default:
			return null;
		}
	}
}
