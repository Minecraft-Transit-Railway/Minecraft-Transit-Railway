package mtr.item;

import java.util.List;

import mtr.Blocks;
import mtr.block.BlockPSDAPGBase;
import mtr.block.BlockPSDAPGBase.PSDAPGSide;
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

public abstract class ItemPSDAPGBase extends Item {

	public ItemPSDAPGBase() {
		super();
		setHasSubtypes(true);
		setCreativeTab(CreativeTabs.REDSTONE);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		pos = pos.offset(facing);

		final boolean isPSD = this instanceof ItemPSD;
		final int itemDamage = player.getHeldItem(hand).getItemDamage();
		final boolean isDoor = itemDamage == 0;
		final EnumFacing playerFacing = player.getHorizontalFacing();

		if (!blocksAreReplacable(worldIn, pos, playerFacing, isDoor ? 2 : 1, isPSD ? 3 : 2))
			return EnumActionResult.FAIL;

		for (int x = 0; x < (isDoor ? 2 : 1); x++)
			for (int y = 0; y < 2; y++) {
				final IBlockState state = getBlockStateFromItem(itemDamage, isPSD);
				final PSDAPGSide side = isDoor ? x == 0 ? PSDAPGSide.LEFT : PSDAPGSide.RIGHT : PSDAPGSide.SINGLE;
				worldIn.setBlockState(pos.up(y).offset(playerFacing.rotateY(), x), state.withProperty(BlockPSDAPGBase.FACING, playerFacing).withProperty(BlockPSDAPGBase.SIDE, side));
			}

		if (isPSD) {
			// TODO PSD top
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		switch (stack.getItemDamage()) {
		case 0:
			tooltip.add(new TextComponentTranslation("gui.psd_apg_door").getFormattedText());
			break;
		case 1:
			tooltip.add(new TextComponentTranslation("gui.psd_apg_glass").getFormattedText());
			break;
		case 2:
			tooltip.add(new TextComponentTranslation("gui.psd_apg_glass_end").getFormattedText());
			break;
		}
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab))
			for (int i = 0; i < 3; i++)
				items.add(new ItemStack(this, 1, i));
	}

	private boolean blocksAreReplacable(World worldIn, BlockPos pos, EnumFacing facing, int width, int height) {
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				if (!worldIn.getBlockState(pos.up(y).offset(facing.rotateY(), x)).getMaterial().isReplaceable())
					return false;

		return true;
	}

	private IBlockState getBlockStateFromItem(int itemDamage, boolean isPSD) {
		switch (itemDamage) {
		case 0:
			return isPSD ? Blocks.psd_door.getDefaultState() : Blocks.apg_door.getDefaultState();
		case 1:
			return isPSD ? Blocks.psd_glass.getDefaultState() : Blocks.apg_glass.getDefaultState();
		case 2:
			return isPSD ? Blocks.psd_glass_end.getDefaultState() : Blocks.apg_glass_end.getDefaultState();
		default:
			return null;
		}
	}
}
