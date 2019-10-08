package mtr.item;

import java.util.List;
import java.util.UUID;

import mtr.entity.EntityTrain;
import mtr.entity.EntityTrain.EnumTrainType;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public abstract class ItemSpawnTrain extends Item {

	public ItemSpawnTrain() {
		super();
		setHasSubtypes(true);
		setMaxDamage(0);
		setMaxStackSize(16);
		setCreativeTab(CreativeTabs.TRANSPORTATION);
	}

	protected abstract EntityTrain getTrain(World worldIn, double x, double y, double z, int trainType);

	protected abstract int getSubtypeCount();

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		final IBlockState iBlockState = worldIn.getBlockState(pos);
		if (!BlockRailBase.isRailBlock(iBlockState)) {
			return EnumActionResult.FAIL;
		} else {
			final ItemStack itemStack = player.getHeldItem(hand);

			if (!worldIn.isRemote) {
				final BlockRailBase.EnumRailDirection railDirection = iBlockState.getBlock() instanceof BlockRailBase ? ((BlockRailBase) iBlockState.getBlock()).getRailDirection(worldIn, pos, iBlockState, null) : BlockRailBase.EnumRailDirection.NORTH_SOUTH;
				final double yOffset = railDirection.isAscending() ? 0.5 : 0;

				final EntityTrain entity1 = getTrain(worldIn, pos.getX() + 0.5, pos.getY() + 0.0625 + yOffset, pos.getZ() + 0.5, itemStack.getItemDamage() + 1);
				final int xOffset = player.getHorizontalFacing().getFrontOffsetX() * -entity1.getSiblingSpacing();
				final int zOffset = player.getHorizontalFacing().getFrontOffsetZ() * -entity1.getSiblingSpacing();
				final EntityTrain entity2 = getTrain(worldIn, pos.getX() + 0.5 + xOffset, pos.getY() + 0.0625 + yOffset, pos.getZ() + 0.5 + zOffset, -itemStack.getItemDamage() - 1);
				worldIn.spawnEntity(entity1);
				worldIn.spawnEntity(entity2);
				entity1.setUUID(entity2.getUniqueID(), new UUID(0, 0));
				entity2.setUUID(entity1.getUniqueID(), new UUID(0, 0));
			}

			itemStack.shrink(1);
			return EnumActionResult.SUCCESS;
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(new TextComponentTranslation("gui.train_" + EnumTrainType.getByIndex(stack.getItemDamage()).getName()).getFormattedText());
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab))
			for (int i = 0; i < getSubtypeCount(); i++)
				items.add(new ItemStack(this, 1, i));
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + stack.getMetadata();
	}
}
