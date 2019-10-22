package mtr.container;

import mtr.item.ItemTemplate;
import mtr.slot.SlotBlockOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerTemplate extends Container {

	private final IInventory itemTemplate;

	public ContainerTemplate(IInventory playerInventory, IInventory iInventory) {
		itemTemplate = iInventory;

		// container inventory
		for (int indexY = 0; indexY < 6; indexY++)
			for (int indexX = 0; indexX < 9; indexX++)
				addSlotToContainer(new SlotBlockOnly(iInventory, indexX + indexY * 9, indexX * 18 + 8, indexY * 18 + 18));
		// player inventory
		for (int indexY = 0; indexY < 3; indexY++)
			for (int indexX = 0; indexX < 9; indexX++)
				addSlotToContainer(new Slot(playerInventory, indexX + indexY * 9 + 9, indexX * 18 + 8, indexY * 18 + 140));
		// hotbar
		for (int index = 0; index < 9; index++)
			addSlotToContainer(new Slot(playerInventory, index, index * 18 + 8, 198));
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn) {
		((ItemTemplate) itemTemplate).markDirty();
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return itemTemplate.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		return ItemStack.EMPTY;
	}
}
