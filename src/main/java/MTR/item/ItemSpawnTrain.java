package mtr.item;

import mtr.entity.EntityTrain;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
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
		IBlockState iBlockState = worldIn.getBlockState(pos);
		if (!BlockRailBase.isRailBlock(iBlockState)) {
			return EnumActionResult.FAIL;
		} else {
			ItemStack itemStack = player.getHeldItem(hand);

			if (!worldIn.isRemote) {
				BlockRailBase.EnumRailDirection railDirection = iBlockState.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iBlockState.getBlock()).getRailDirection(worldIn, pos, iBlockState, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				double yOffset = railDirection.isAscending() ? 0.5 : 0;

				EntityTrain entity1 = getTrain(worldIn, pos.getX() + 0.5, pos.getY() + 0.0625 + yOffset, pos.getZ() + 0.5);
				int xOffset = player.getHorizontalFacing().getFrontOffsetX() * -entity1.getSpacing();
				int zOffset = player.getHorizontalFacing().getFrontOffsetZ() * -entity1.getSpacing();
				EntityTrain entity2 = getTrain(worldIn, pos.getX() + 0.5 + xOffset, pos.getY() + 0.0625 + yOffset, pos.getZ() + 0.5 + zOffset);
				entity1.setSibling(entity2);
				entity2.setSibling(entity1);
				worldIn.spawnEntity(entity1);
				worldIn.spawnEntity(entity2);
			}

			itemStack.shrink(1);
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
