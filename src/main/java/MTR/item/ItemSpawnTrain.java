package mtr.item;

import mtr.entity.EntityTrain;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class ItemSpawnTrain extends Item {

	public ItemSpawnTrain() {
		super();
		setHasSubtypes(true);
		setMaxDamage(0);
		setMaxStackSize(16);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
	}

	protected abstract EntityTrain getTrain(World worldIn, double x, double y, double z);

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState iblockstate = worldIn.getBlockState(pos);

		if (!BlockRailBase.isRailBlock(iblockstate)) {
			return EnumActionResult.FAIL;
		} else {
			ItemStack itemstack = player.getHeldItem(hand);

			if (!worldIn.isRemote) {
				BlockRailBase.EnumRailDirection blockrailbase$enumraildirection = iblockstate.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iblockstate.getBlock()).getRailDirection(worldIn, pos, iblockstate, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				double d0 = 0.0D;

				if (blockrailbase$enumraildirection.isAscending()) {
					d0 = 0.5D;
				}

				EntityMinecart entityminecart = getTrain(worldIn, pos.getX() + 0.5D, pos.getY() + 0.0625D + d0, pos.getZ() + 0.5D);

				if (itemstack.hasDisplayName()) {
					entityminecart.setCustomNameTag(itemstack.getDisplayName());
				}

				worldIn.spawnEntity(entityminecart);
			}

			itemstack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getMetadata();
	}

	protected abstract int getSubtypeCount();

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab))
			for (int i = 0; i < getSubtypeCount(); i++)
				items.add(new ItemStack(this, 1, i));
	}
}
