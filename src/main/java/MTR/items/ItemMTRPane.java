package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTRBlocks;
import MTR.blocks.BlockMTRPane;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemMTRPane extends ItemBase {

	private static final String[] name = { "ItemMTRPane1", "ItemMTRPane2", "ItemMTRPane3", "ItemMTRPane4",
			"ItemMTRPane5", "ItemMTRPane6", "ItemMTRPane7", "ItemMTRPane8" };
	private static final String name2 = "ItemMTRPane";

	public ItemMTRPane() {
		super(name, name2);
		setHasSubtypes(true);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		switch (stack.getMetadata()) {
		case 0:
			list.add(I18n.format("station.81", new Object[0]));
			break;
		case 1:
			list.add(I18n.format("station.82", new Object[0]));
			break;
		case 2:
			list.add(I18n.format("station.83", new Object[0]));
			break;
		case 3:
			list.add(I18n.format("station.84", new Object[0]));
			break;
		case 4:
			list.add(I18n.format("station.85", new Object[0]));
			break;
		case 5:
			list.add(I18n.format("station.86", new Object[0]));
			break;
		case 6:
			list.add(I18n.format("station.87", new Object[0]));
			break;
		case 7:
			list.add(I18n.format("station.88", new Object[0]));
			break;
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState var9 = worldIn.getBlockState(pos);
		Block var10 = var9.getBlock();
		if (!var10.isReplaceable(worldIn, pos))
			pos = pos.offset(facing);
		Block block = MTRBlocks.blockmtrpanea;
		switch (stack.getItemDamage()) {
		case 0:
			block = MTRBlocks.blockmtrpanea;
			break;
		case 1:
			block = MTRBlocks.blockmtrpaneb;
			break;
		case 2:
			block = MTRBlocks.blockmtrpanec;
			break;
		case 3:
			block = MTRBlocks.blockmtrpaned;
			break;
		case 4:
			block = MTRBlocks.blockmtrpanee;
			break;
		case 5:
			block = MTRBlocks.blockmtrpanef;
			break;
		case 6:
			block = MTRBlocks.blockmtrpaneg;
			break;
		case 7:
			block = MTRBlocks.blockmtrpaneh;
			break;
		}
		EnumFacing var3 = EnumFacing.fromAngle(playerIn.rotationYaw).rotateY();
		if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
				&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())) {
			worldIn.setBlockState(pos, block.getDefaultState().withProperty(BlockMTRPane.FACING, var3)
					.withProperty(BlockMTRPane.NAME, false).withProperty(BlockMTRPane.TOP, false));
			worldIn.setBlockState(pos.add(0, 1, 0), block.getDefaultState().withProperty(BlockMTRPane.NAME, false)
					.withProperty(BlockMTRPane.FACING, var3).withProperty(BlockMTRPane.TOP, true));
			--stack.stackSize;
			return EnumActionResult.SUCCESS;
		} else
			return EnumActionResult.FAIL;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 8; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}
