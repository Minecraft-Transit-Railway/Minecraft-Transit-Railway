package mtr.tile;

import mtr.block.BlockBridgeCreator;
import mtr.container.ContainerBridgeCreator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;

public class TileEntityBridgeCreator extends TileEntityLockableLoot implements ITickable {

	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
	private int burnTime, totalBurnTime;

	@Override
	public void update() {
		final boolean prevBurning = isBurning();
		boolean dirty = false;

		if (isBurning())
			burnTime--;

		if (!world.isRemote) {
			final ItemStack itemstack = inventory.get(getSizeInventory() - 1);
			if (!prevBurning && !itemstack.isEmpty()) {
				totalBurnTime = burnTime = TileEntityFurnace.getItemBurnTime(itemstack);
				if (isBurning()) {
					dirty = true;
					if (!itemstack.isEmpty())
						itemstack.shrink(1);
				}
			}
			if (prevBurning != isBurning()) {
				dirty = true;
				BlockBridgeCreator.setState(isBurning(), world, pos);
			}
		}

		if (dirty)
			markDirty();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
		burnTime = compound.getInteger("burnTime");
		totalBurnTime = compound.getInteger("totalBurnTime");
		ItemStackHelper.loadAllItems(compound, inventory);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, inventory);
		compound.setInteger("burnTime", burnTime);
		compound.setInteger("totalBurnTime", totalBurnTime);
		return compound;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return burnTime;
		case 1:
			return totalBurnTime;
		default:
			return 0;
		}
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			burnTime = value;
			break;
		case 1:
			totalBurnTime = value;
			break;
		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public int getSizeInventory() {
		return 38;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public boolean isEmpty() {
		for (final ItemStack itemStack : inventory)
			if (!itemStack.isEmpty())
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

	@Override
	protected NonNullList<ItemStack> getItems() {
		return inventory;
	}

	public boolean isBurning() {
		return burnTime > 0;
	}
}
