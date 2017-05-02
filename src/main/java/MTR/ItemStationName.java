package MTR;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemStationName extends Item {

	public static final String name = "ItemStationName";

	public ItemStationName() {
		setHasSubtypes(true);
		setCreativeTab(MTR.MTRtab);
		GameRegistry.registerItem(this, name);
		setUnlocalizedName(name);
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
			list.add(I18n.format("station.69", new Object[0]));
			break;
		case 12:
			list.add(I18n.format("station.70", new Object[0]));
			break;
		case 13:
			list.add(I18n.format("station.71", new Object[0]));
			break;
		case 14:
			list.add(I18n.format("station.72", new Object[0]));
			break;
		case 15:
			list.add(I18n.format("station.73", new Object[0]));
			break;
		case 16:
			list.add(I18n.format("station.74", new Object[0]));
			break;
		case 17:
			list.add(I18n.format("station.75", new Object[0]));
			break;
		case 18:
			list.add(I18n.format("station.76", new Object[0]));
			break;
		case 19:
			list.add(I18n.format("station.77", new Object[0]));
			break;
		case 20:
			list.add(I18n.format("station.78", new Object[0]));
			break;
		case 21:
			list.add(I18n.format("station.79", new Object[0]));
			break;
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos2, EnumFacing side,
			float hitX, float hitY, float hitZ) {
		IBlockState var9 = worldIn.getBlockState(pos2);
		Block var10 = var9.getBlock();
		boolean s = side.getIndex() >= 2;
		BlockPos pos = pos2;
		if (!var10.isReplaceable(worldIn, pos2))
			pos = pos2.offset(side);
		boolean pole = false;
		if (worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockStationNameBase)
			pole = worldIn.getBlockState(pos.add(0, -1, 0)).getValue(BlockStationNameBase.POLE);
		if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos) && pole ^ s) {
			Block block = MTR.blockstationnamea;
			switch (stack.getItemDamage()) {
			case 0:
				block = MTR.blockstationnamea;
				break;
			case 1:
				block = MTR.blockstationnameb;
				break;
			case 2:
				block = MTR.blockstationnamec;
				break;
			case 3:
				block = MTR.blockstationnamed;
				break;
			case 4:
				block = MTR.blockstationnamee;
				break;
			case 5:
				block = MTR.blockstationnamef;
				break;
			case 6:
				block = MTR.blockstationnameg;
				break;
			case 7:
				block = MTR.blockstationnameh;
				break;
			case 8:
				block = MTR.blockstationnamei;
				break;
			case 9:
				block = MTR.blockstationnamej;
				break;
			case 10:
				block = MTR.blockstationnamek;
				break;
			case 11:
				block = MTR.blockstationnamel;
				break;
			case 12:
				block = MTR.blockstationnamem;
				break;
			case 13:
				block = MTR.blockstationnamen;
				break;
			case 14:
				block = MTR.blockstationnameo;
				break;
			case 15:
				block = MTR.blockstationnamep;
				break;
			case 16:
				block = MTR.blockstationnameq;
				break;
			case 17:
				block = MTR.blockstationnamer;
				break;
			case 18:
				block = MTR.blockstationnames;
				break;
			case 19:
				block = MTR.blockstationnamet;
				break;
			case 20:
				block = MTR.blockstationnameu;
				break;
			case 21:
				block = MTR.blockstationnamev;
				break;
			}
			EnumFacing var3 = s ? side.getOpposite().rotateY() : EnumFacing.fromAngle(playerIn.rotationYaw).rotateY();
			worldIn.setBlockState(pos, block.getDefaultState().withProperty(BlockStationNameBase.FACING, var3)
					.withProperty(BlockStationNameBase.POLE, false).withProperty(BlockStationNameBase.SIDE, s));
			--stack.stackSize;
			return true;
		} else
			return false;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 22; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}

	public static String getName(int a) {
		return name + a;
	}
}
