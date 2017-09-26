package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTR;
import MTR.MTRBlocks;
import MTR.blocks.BlockStationNameBase;
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

public class ItemStationNamePole extends ItemBase {

	private static final String[] name = { "ItemStationNamePole1", "ItemStationNamePole2", "ItemStationNamePole3",
			"ItemStationNamePole4", "ItemStationNamePole5", "ItemStationNamePole6", "ItemStationNamePole7",
			"ItemStationNamePole8", "ItemStationNamePole9", "ItemStationNamePole10", "ItemStationNamePole11",
			"ItemStationNamePole12", "ItemStationNamePole13", "ItemStationNamePole14", "ItemStationNamePole15",
			"ItemStationNamePole16", "ItemStationNamePole17", "ItemStationNamePole18", "ItemStationNamePole19",
			"ItemStationNamePole20", "ItemStationNamePole21", "ItemStationNamePole22" };
	private static final String name2 = "ItemStationNamePole";

	public ItemStationNamePole() {
		super(name, name2);
		setCreativeTab(MTR.MTRTabStationName);
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
		case 8:
			list.add(I18n.format("station.59", new Object[0]));
			break;
		case 9:
			list.add(I18n.format("station.69", new Object[0]));
			break;
		case 10:
			list.add(I18n.format("station.34", new Object[0]));
			break;
		case 11:
			list.add(I18n.format("station.70", new Object[0]));
			break;
		case 12:
			list.add(I18n.format("station.71", new Object[0]));
			break;
		case 13:
			list.add(I18n.format("station.72", new Object[0]));
			break;
		case 14:
			list.add(I18n.format("station.73", new Object[0]));
			break;
		case 15:
			list.add(I18n.format("station.74", new Object[0]));
			break;
		case 16:
			list.add(I18n.format("station.75", new Object[0]));
			break;
		case 17:
			list.add(I18n.format("station.76", new Object[0]));
			break;
		case 18:
			list.add(I18n.format("station.77", new Object[0]));
			break;
		case 19:
			list.add(I18n.format("station.78", new Object[0]));
			break;
		case 20:
			list.add(I18n.format("station.79", new Object[0]));
			break;
		case 21:
			list.add(I18n.format("station.80", new Object[0]));
			break;
		}
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos2,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState var9 = worldIn.getBlockState(pos2);
		Block var10 = var9.getBlock();
		boolean s = facing.getIndex() >= 2;
		BlockPos pos = pos2;
		if (!var10.isReplaceable(worldIn, pos2))
			pos = pos2.offset(facing);
		if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)) {
			Block block = MTRBlocks.blockstationnamea;
			switch (stack.getItemDamage()) {
			case 0:
				block = MTRBlocks.blockstationnamea;
				break;
			case 1:
				block = MTRBlocks.blockstationnameb;
				break;
			case 2:
				block = MTRBlocks.blockstationnamec;
				break;
			case 3:
				block = MTRBlocks.blockstationnamed;
				break;
			case 4:
				block = MTRBlocks.blockstationnamee;
				break;
			case 5:
				block = MTRBlocks.blockstationnamef;
				break;
			case 6:
				block = MTRBlocks.blockstationnameg;
				break;
			case 7:
				block = MTRBlocks.blockstationnameh;
				break;
			case 8:
				block = MTRBlocks.blockstationnamei;
				break;
			case 9:
				block = MTRBlocks.blockstationnamej;
				break;
			case 10:
				block = MTRBlocks.blockstationnamek;
				break;
			case 11:
				block = MTRBlocks.blockstationnamel;
				break;
			case 12:
				block = MTRBlocks.blockstationnamem;
				break;
			case 13:
				block = MTRBlocks.blockstationnamen;
				break;
			case 14:
				block = MTRBlocks.blockstationnameo;
				break;
			case 15:
				block = MTRBlocks.blockstationnamep;
				break;
			case 16:
				block = MTRBlocks.blockstationnameq;
				break;
			case 17:
				block = MTRBlocks.blockstationnamer;
				break;
			case 18:
				block = MTRBlocks.blockstationnames;
				break;
			case 19:
				block = MTRBlocks.blockstationnamet;
				break;
			case 20:
				block = MTRBlocks.blockstationnameu;
				break;
			case 21:
				block = MTRBlocks.blockstationnamev;
				break;
			}
			EnumFacing var3 = s ? facing.getOpposite().rotateY() : EnumFacing.fromAngle(playerIn.rotationYaw).rotateY();
			worldIn.setBlockState(pos, block.getDefaultState().withProperty(BlockStationNameBase.FACING, var3)
					.withProperty(BlockStationNameBase.POLE, true).withProperty(BlockStationNameBase.SIDE, s));
			--stack.stackSize;
			return EnumActionResult.SUCCESS;
		} else
			return EnumActionResult.FAIL;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 22; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}
}
