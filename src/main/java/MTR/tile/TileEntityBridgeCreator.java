package mtr.tile;

import mtr.Blocks;
import mtr.block.BlockBridgeCreator;
import mtr.block.BlockRailScaffold;
import mtr.container.ContainerBridgeCreator;
import mtr.slot.SlotBlockOnly;
import mtr.slot.SlotTemplateOnly;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class TileEntityBridgeCreator extends TileEntityLockableLoot implements ITickable, ISidedInventory {

	private NonNullList<ItemStack> inventory = NonNullList.<ItemStack>withSize(getSizeInventory(), ItemStack.EMPTY);
	private int burnTime, totalBurnTime;
	private BlockPos scaffoldPos;
	private IItemHandler templateShape;

	@Override
	public void update() {
		final boolean prevBurning = isBurning();
		boolean dirty = false;

		if (isBurning())
			burnTime--;

		if (!world.isRemote) {
			final ItemStack fuelStack = inventory.get(getSizeInventory() - 1);

			if (!isBurning() && !fuelStack.isEmpty()) {
				if (scaffoldPos == null)
					findScaffold(pos, 0);
				getTemplateShape();

				if (scaffoldPos != null && templateShape != null) {
					totalBurnTime = burnTime = TileEntityFurnace.getItemBurnTime(fuelStack);

					if (isBurning()) {
						dirty = true;
						if (!fuelStack.isEmpty())
							fuelStack.shrink(1);
					}
				}
			}

			if (isBurning() && burnTime % 10 == 0) {
				if (templateShape == null)
					getTemplateShape();
				if (scaffoldPos != null && templateShape != null) {
					final int pass = paintBlocks(scaffoldPos);
					findScaffold(scaffoldPos, pass);
					if (scaffoldPos == null)
						findScaffold(pos, pass + 1);
					System.out.println(pass + " " + scaffoldPos);
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
		ItemStackHelper.loadAllItems(compound, inventory);

		burnTime = compound.getInteger("burnTime");
		totalBurnTime = compound.getInteger("totalBurnTime");

		final int[] posArray = compound.getIntArray("scaffoldPos");
		scaffoldPos = posArray.length == 3 ? new BlockPos(posArray[0], posArray[1], posArray[2]) : null;
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		ItemStackHelper.saveAllItems(compound, inventory);

		compound.setInteger("burnTime", burnTime);
		compound.setInteger("totalBurnTime", totalBurnTime);

		if (scaffoldPos == null) {
			compound.removeTag("scaffoldPos");
		} else {
			final int[] posArray = { scaffoldPos.getX(), scaffoldPos.getY(), scaffoldPos.getZ() };
			compound.setIntArray("scaffoldPos", posArray);
		}

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
			final int[] slots = { getSizeInventory() - 2, getSizeInventory() - 1 };
			return slots;
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

	public boolean isBurning() {
		return burnTime > 0;
	}

	private void findScaffold(BlockPos findPos, int pass) {
		final BlockPos[] positions = { findPos.north(), findPos.east(), findPos.south(), findPos.west() };
		for (final BlockPos position : positions) {
			final IBlockState state = world.getBlockState(position);
			if (state.getBlock() instanceof BlockRailScaffold && state.getValue(BlockRailScaffold.PASS) == pass) {
				scaffoldPos = position;
				return;
			}
		}
		scaffoldPos = null;
	}

	private void getTemplateShape() {
		final ItemStack templateStack = inventory.get(getSizeInventory() - 2);
		if (templateStack.isEmpty())
			templateShape = null;
		else
			templateShape = templateStack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
	}

	private Block getBlockFromTemplate(int pass, int height) {
		if (templateShape == null)
			return null;
		if (pass < 0)
			pass = 0;
		if (pass > 8)
			pass = 8;
		if (height < -1)
			height = -1;
		if (height > 4)
			height = 4;
		final Item item = templateShape.getStackInSlot(pass - height * 9 + 36).getItem();
		if (item instanceof ItemPickaxe)
			return net.minecraft.init.Blocks.AIR;
		else
			return item instanceof ItemBlock ? Block.getBlockFromItem(item) : null;
	}

	private int paintBlocks(BlockPos buildPos) {
		final IBlockState buildState = world.getBlockState(buildPos);
		if (buildState.getBlock() instanceof BlockRailScaffold) {
			final int pass = buildState.getValue(BlockRailScaffold.PASS);
			final int radius = 8 - pass;
			for (int x = -radius; x <= radius; x++)
				for (int z = -radius; z <= radius; z++)
					if (Math.abs(x) == radius || Math.abs(z) == radius)
						for (int i = -1; i < 5; i++) {
							final BlockPos currentPos = buildPos.add(x, i, z);
							final IBlockState currentState = world.getBlockState(currentPos);
							if (!(currentState.getBlock() instanceof BlockRailScaffold && pass < 8) && !(currentState.getBlock() instanceof BlockBridgeCreator)) {
								final Block block = getBlockFromTemplate(pass, i);
								if (block != null)
									world.setBlockState(currentPos, block.getDefaultState());
							}
						}

			if (pass < 8)
				world.setBlockState(buildPos, Blocks.rail_scaffold.getDefaultState().withProperty(BlockRailScaffold.PASS, pass + 1));
			return pass;
		} else {
			return 0;
		}
	}
}
