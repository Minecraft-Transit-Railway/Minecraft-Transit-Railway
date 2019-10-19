package mtr.tile;

import mtr.container.ContainerBridgeCreator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public class TileEntityBridgeCreator extends TileEntityLockableLoot implements ITickable {

	private NonNullList<ItemStack> chestContents = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);

	@Override
	public void update() {

	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		chestContents = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, chestContents);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, chestContents);
		return compound;
	}

	@Override
	protected NonNullList<ItemStack> getItems() {
		return chestContents;
	}

	@Override
	public int getSizeInventory() {
		return 36;
	}

	@Override
	public boolean isEmpty() {
		for (final ItemStack itemstack : chestContents)
			if (!itemstack.isEmpty())
				return false;
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public String getName() {
		return hasCustomName() ? customName : "tile.bridge_creator.name";
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return new ContainerBridgeCreator(playerInventory, this);
	}

	@Override
	public String getGuiID() {
		return "mtr:bridge_creator";
	}
}
