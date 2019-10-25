package mtr.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class CapabilityInventoryProvider implements ICapabilityProvider, ICapabilitySerializable<NBTBase> {

	private final ItemStackHandler inventory;

	public CapabilityInventoryProvider(int slots) {
		inventory = new ItemStackHandler(slots);
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
			return (T) inventory;
		return null;
	}

	@Override
	public NBTBase serializeNBT() {
		return inventory.serializeNBT();
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		inventory.deserializeNBT((NBTTagCompound) nbt);
	}
}
