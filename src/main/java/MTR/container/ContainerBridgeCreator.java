package mtr.container;

import mtr.slot.SlotBlockOnly;
import mtr.slot.SlotTemplateOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBridgeCreator extends Container {

	private final IInventory tileBridgeCreator;
	private int burnTime, totalBurnTime;

	public ContainerBridgeCreator(IInventory playerInventory, IInventory bridgeCreatorInventoryIn) {
		tileBridgeCreator = bridgeCreatorInventoryIn;

		// container inventory
		for (int indexY = 0; indexY < 6; ++indexY)
			for (int indexX = 0; indexX < 6; ++indexX)
				addSlotToContainer(new SlotBlockOnly(bridgeCreatorInventoryIn, indexX + indexY * 6, indexX * 18 + 8, indexY * 18 + 18));
		// template and fuel slot
		addSlotToContainer(new SlotTemplateOnly(bridgeCreatorInventoryIn, 36, 134, 36));
		addSlotToContainer(new SlotFurnaceFuel(bridgeCreatorInventoryIn, 37, 134, 90));
		// player inventory
		for (int indexY = 0; indexY < 3; ++indexY)
			for (int indexX = 0; indexX < 9; ++indexX)
				addSlotToContainer(new Slot(playerInventory, indexX + indexY * 9 + 9, indexX * 18 + 8, indexY * 18 + 140));
		// hotbar
		for (int index = 0; index < 9; index++)
			addSlotToContainer(new Slot(playerInventory, index, index * 18 + 8, 198));
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn) {
		return tileBridgeCreator.isUsableByPlayer(playerIn);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
		ItemStack itemStack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);
		final int slotCount = tileBridgeCreator.getSizeInventory();

		if (slot != null && slot.getHasStack()) {
			final ItemStack itemStack1 = slot.getStack();
			itemStack = itemStack1.copy();

			if (index < slotCount) {
				if (!mergeItemStack(itemStack1, slotCount, slotCount + 36, true))
					return ItemStack.EMPTY;
			} else if (!mergeItemStack(itemStack1, 0, slotCount, false)) {
				return ItemStack.EMPTY;
			}

			if (itemStack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			if (itemStack1.getCount() == itemStack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, itemStack1);
		}

		return itemStack;
	}

	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		listener.sendAllWindowProperties(this, tileBridgeCreator);
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < listeners.size(); i++) {
			final IContainerListener listener = listeners.get(i);
			if (burnTime != tileBridgeCreator.getField(0))
				listener.sendWindowProperty(this, 0, tileBridgeCreator.getField(0));
			if (totalBurnTime != tileBridgeCreator.getField(1))
				listener.sendWindowProperty(this, 1, tileBridgeCreator.getField(1));
		}
		burnTime = tileBridgeCreator.getField(0);
		totalBurnTime = tileBridgeCreator.getField(1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		tileBridgeCreator.setField(id, data);
	}
}
