package mtr.slot;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class SlotBlockPickaxeOnly extends SlotItemHandler {

	public SlotBlockPickaxeOnly(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
		super(itemHandler, index, xPosition, yPosition);
	}

	@Override
	public boolean isItemValid(ItemStack stack) {
		if (isValid(stack))
			putStack(new ItemStack(stack.getItem(), 1));
		return false;
	}

	@Override
	public ItemStack decrStackSize(int amount) {
		getItemHandler().extractItem(getSlotIndex(), amount, false);
		return ItemStack.EMPTY;
	}

	@Override
	public int getItemStackLimit(ItemStack stack) {
		return 1;
	}

	public static boolean isValid(ItemStack stack) {
		return stack.getItem() instanceof ItemBlock || stack.getItem() instanceof ItemPickaxe;
	}
}
