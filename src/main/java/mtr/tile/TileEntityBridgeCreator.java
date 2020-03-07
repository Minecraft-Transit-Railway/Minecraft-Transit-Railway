package mtr.tile;

import mtr.block.BlockBridgeCreator;
import mtr.block.BlockRailScaffold;
import mtr.container.ContainerBridgeCreator;
import mtr.slot.SlotBlockOnly;
import mtr.slot.SlotTemplateOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TileEntityBridgeCreator extends TileEntityLockableLoot implements ITickable, ISidedInventory {

	private NonNullList<ItemStack> inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
	private int burnTime, totalBurnTime;
	private final Map<BlockPos, Block> structure = new HashMap<>();

	@Override
	public void update() {
		final boolean prevBurning = isBurning();
		boolean dirty = false;

		if (isBurning())
			burnTime--;

		if (!world.isRemote) {
			final ItemStack fuelStack = getFuelStack();
			final ItemStack templateStack = getTemplateStack();
			if (!templateStack.isEmpty() && structure.size() == 0)
				generateStructure();

			if (!isBurning() && !fuelStack.isEmpty() && !templateStack.isEmpty() && structure.size() > 0) {
				totalBurnTime = burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);

				if (isBurning()) {
					dirty = true;
					if (!fuelStack.isEmpty())
						fuelStack.shrink(1);
				}
			}

			if (isBurning() && structure.size() > 0)
				paintBlock();

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
		inventory = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, inventory);

		burnTime = compound.getInteger("burnTime");
		totalBurnTime = compound.getInteger("totalBurnTime");
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
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if (index == getSizeInventory() - 1)
			return TileEntityFurnace.isItemFuel(stack);
		else if (index == getSizeInventory() - 2)
			return SlotTemplateOnly.isValid(stack);
		else
			return SlotBlockOnly.isValid(stack);
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.UP) {
			final int[] slots = new int[getSizeInventory() - 2];
			for (int i = 0; i < slots.length; i++)
				slots[i] = i;
			return slots;
		} else {
			return new int[]{getSizeInventory() - 2, getSizeInventory() - 1};
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return false;
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

	private boolean isBurning() {
		return burnTime > 0;
	}

	private ItemStack getFuelStack() {
		return inventory.get(getSizeInventory() - 1);
	}

	private ItemStack getTemplateStack() {
		return inventory.get(getSizeInventory() - 2);
	}

	private BlockPos findNextScaffold(BlockPos thisPos, BlockPos prevPos) {
		final BlockPos[] positions = {thisPos.north(), thisPos.east(), thisPos.south(), thisPos.west()};
		for (final BlockPos position : positions)
			if (!position.equals(prevPos) && world.getBlockState(position).getBlock() instanceof BlockRailScaffold)
				return position;
		return null;
	}

	private void paintBlock() {
		final BlockPos currentPos = (BlockPos) structure.keySet().toArray()[0];
		final IBlockState currentState = world.getBlockState(currentPos);
		final Block currentBlock = currentState.getBlock();
		final Block newBlock = structure.get(currentPos);

		if (currentBlock == newBlock || currentBlock.hasTileEntity(currentState)) {
			structure.remove(currentPos);
		} else {
			boolean canPlaceBlock = newBlock instanceof BlockAir;
			if (!canPlaceBlock && newBlock != null)
				for (int j = 0; j < getSizeInventory() - 2; j++)
					if (inventory.get(j).getItem() == new ItemStack(newBlock).getItem()) {
						inventory.get(j).shrink(1);
						canPlaceBlock = true;
						break;
					}

			if (canPlaceBlock) {
				ItemStack droppedItemStack = new ItemStack(currentBlock.getItemDropped(currentState, new Random(), 0), currentBlock.quantityDropped(new Random()));

				final BlockPos[] positions = {pos.north(), pos.east(), pos.south(), pos.west(), pos.up(), pos.down(), pos};
				for (final BlockPos position : positions) {
					if (droppedItemStack.isEmpty())
						break;
					final IInventory nearby = TileEntityHopper.getInventoryAtPosition(world, position.getX(), position.getY(), position.getZ());
					if (nearby != null)
						droppedItemStack = TileEntityHopper.putStackInInventoryAllSlots(null, nearby, droppedItemStack, null);
				}

				if (!droppedItemStack.isEmpty())
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), droppedItemStack));

				world.setBlockState(currentPos, newBlock.getDefaultState());
				structure.remove(currentPos);
			}
		}
	}

	private void generateStructure() {
		final BlockPos startPos = findNextScaffold(pos, new BlockPos(0, 0, 0));
		if (startPos == null)
			return;

		final IItemHandler templateInventory = getTemplateStack().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		if (templateInventory == null)
			return;

		final Block[] templateBlocks = new Block[54];
		boolean hasBlockFlag = false;
		for (int i = 0; i < 54; i++) {
			if (i % 9 == 0)
				hasBlockFlag = false;

			final Item item = templateInventory.getStackInSlot(i).getItem();

			if (item instanceof ItemPickaxe) {
				templateBlocks[i] = net.minecraft.init.Blocks.AIR;
			} else if (item instanceof ItemBlock) {
				templateBlocks[i] = Block.getBlockFromItem(item);
				hasBlockFlag = true;
			} else if (hasBlockFlag) {
				templateBlocks[i] = net.minecraft.init.Blocks.AIR;
			} else {
				templateBlocks[i] = null;
			}
		}

		for (int pass = 0; pass < 9; pass++) {
			final int radius = 8 - pass;
			BlockPos scaffoldPos = startPos, prevPos = pos;
			for (int max = 0; max < 4096; max++) {
				final BlockPos tempPos = scaffoldPos;
				scaffoldPos = findNextScaffold(scaffoldPos, prevPos);
				prevPos = tempPos;

				if (scaffoldPos == null)
					break;

				for (int x = -radius; x <= radius; x++) {
					for (int z = -radius; z <= radius; z++) {
						if (Math.abs(x) == radius || Math.abs(z) == radius) {
							for (int y = -1; y < 5; y++) {
								final BlockPos currentPos = scaffoldPos.add(x, y, z);
								final Block newBlock = templateBlocks[pass - y * 9 + 36];
								if (newBlock != null)
									structure.put(currentPos, newBlock);
							}
						}
					}
				}
			}
		}
	}
}
