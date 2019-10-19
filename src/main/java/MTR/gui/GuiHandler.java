package mtr.gui;

import mtr.container.ContainerBridgeCreator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null)
			switch (ID) {
			case 0:
				return new ContainerBridgeCreator(player.inventory, (IInventory) tileEntity);
			}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		if (tileEntity != null)
			switch (ID) {
			case 0:
				return new GuiBridgeCreator(player.inventory, (IInventory) tileEntity);
			}
		return null;
	}
}
