package mtr.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBridgeCreator extends Container {

	private final IInventory bridgeCreatorInventory;

	public ContainerBridgeCreator(IInventory playerInventory, IInventory bridgeCreatorInventoryIn) {
		bridgeCreatorInventory = bridgeCreatorInventoryIn;

		// container inventory
		for (int indexY = 0; indexY < 6; ++indexY)
			for (int indexX = 0; indexX < 6; ++indexX)
				addSlotToContainer(
						new Slot(bridgeCreatorInventoryIn, indexX + indexY * 6, indexX * 18 + 8, indexY * 18 + 18));
		// player inventory
		for (int indexY = 0; indexY < 3; ++indexY)
			for (int indexX = 0; indexX < 9; ++indexX)
				addSlotToContainer(
						new Slot(playerInventory, indexX + indexY * 9 + 9, indexX * 18 + 8, indexY * 18 + 139));
		// hotbar
		for (int index = 0; index < 9; index++)
			addSlotToContainer(new Slot(playerInventory, index, index * 18 + 8, 197));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return bridgeCreatorInventory.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		final int slotCount = bridgeCreatorInventory.getSizeInventory();

		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index < slotCount) {
				if (!mergeItemStack(itemstack1, slotCount, slotCount + 36, true))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(itemstack1, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			if (itemstack1.getCount() == itemstack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}
}
