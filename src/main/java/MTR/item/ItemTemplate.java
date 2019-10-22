package mtr.item;

import javax.annotation.Nullable;

import mtr.MTR;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ItemTemplate extends Item implements IInventory {

	private final NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
	private boolean update = false;

	public ItemTemplate() {
		super();
		setMaxStackSize(1);
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		final ItemStack stack = playerIn.getHeldItem(handIn);
		if (!worldIn.isRemote) {
			final int[] storedItems = getNBTTagCompound(stack).getIntArray("inventory");
			for (int i = 0; i < storedItems.length; i++)
				inventory.set(i, new ItemStack(Item.getItemById(storedItems[i])));

			playerIn.openGui(MTR.instance, 1, worldIn, 0, 0, 0);
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if (!worldIn.isRemote && update) {
			setDamage(stack, isEmpty() ? 0 : 1);

			final int[] items = new int[getSizeInventory()];
			for (int i = 0; i < items.length; i++)
				items[i] = Item.getIdFromItem(inventory.get(i).getItem());
			getNBTTagCompound(stack).setIntArray("inventory", items);

			update = false;
		}
	}

	@Override
	public String getName() {
		return "item.template.name";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		return new TextComponentTranslation(getName());
	}

	@Override
	public int getSizeInventory() {
		return 54;
	}

	@Override
	public boolean isEmpty() {
		for (final ItemStack itemStack : inventory)
			if (!itemStack.isEmpty())
				return false;
		return true;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return inventory.get(index);
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(inventory, index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(inventory, index);
	}

	@Override
	public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
		inventory.set(index, stack);
		if (stack.getCount() > getInventoryStackLimit())
			stack.setCount(getInventoryStackLimit());
	}

	@Override
	public int getInventoryStackLimit() {
		return 1;
	}

	@Override
	public void markDirty() {
		update = true;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return true;
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	@Override
	public int getField(int id) {
		return 0;
	}

	@Override
	public void setField(int id, int value) {
	}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		inventory.clear();
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab))
			for (int i = 0; i < 2; i++)
				items.add(new ItemStack(this, 1, i));
	}

	private NBTTagCompound getNBTTagCompound(ItemStack stack) {
		return stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
	}
}
