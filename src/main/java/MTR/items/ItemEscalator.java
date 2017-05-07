package MTR.items;

import java.util.List;

import MTR.MTR;
import MTR.blocks.BlockEscalatorSide;
import MTR.blocks.BlockEscalatorStep;
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

public class ItemEscalator extends Item {

	public static final String name1 = "ItemEscalator";
	public static final String name2 = "ItemEscalator5";
	public static final String name3 = "ItemEscalator10";
	public static final String name4 = "ItemEscalator20";
	public static final String name5 = "ItemEscalator50";
	private static final int a[] = { 1, 5, 10, 20, 50 };

	public ItemEscalator() {
		setHasSubtypes(true);
		setCreativeTab(MTR.MTRtab);
		GameRegistry.registerItem(this, name1);
		setUnlocalizedName(name1);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		switch (stack.getMetadata()) {
		case 1:
			list.add(I18n.format("gui.escalator5", new Object[0]));
			break;
		case 2:
			list.add(I18n.format("gui.escalator10", new Object[0]));
			break;
		case 3:
			list.add(I18n.format("gui.escalator20", new Object[0]));
			break;
		case 4:
			list.add(I18n.format("gui.escalator50", new Object[0]));
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing facingSide,
			float hitX, float hitY, float hitZ) {
		IBlockState var9 = worldIn.getBlockState(pos);
		Block var10 = var9.getBlock();
		if (!var10.isReplaceable(worldIn, pos))
			pos = pos.offset(facingSide);
		Block blockStep = MTR.blockescalatorstep;
		Block blockSide = MTR.blockescalatorside;
		EnumFacing facing = EnumFacing.fromAngle(playerIn.rotationYaw).rotateY();

		BlockPos posRight = pos;
		switch (facing) {
		case NORTH:
			posRight = pos.add(0, 0, -1);
			break;
		case SOUTH:
			posRight = pos.add(0, 0, 1);
			break;
		case EAST:
			posRight = pos.add(1, 0, 0);
			break;
		case WEST:
			posRight = pos.add(-1, 0, 0);
			break;
		default:
		}
		for (int i = 0; i < a[stack.getMetadata()]; i++) {
			if (worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos)
					&& worldIn.getBlockState(pos.up()).getBlock().isReplaceable(worldIn, pos.up())
					&& worldIn.getBlockState(posRight).getBlock().isReplaceable(worldIn, posRight)
					&& worldIn.getBlockState(posRight.up()).getBlock().isReplaceable(worldIn, posRight.up())) {
				worldIn.setBlockState(pos, blockStep.getDefaultState().withProperty(BlockEscalatorStep.FACING, facing));
				worldIn.setBlockState(pos.up(), blockSide.getDefaultState()
						.withProperty(BlockEscalatorSide.FACING, facing).withProperty(BlockEscalatorSide.SIDE, false));
				worldIn.setBlockState(posRight,
						blockStep.getDefaultState().withProperty(BlockEscalatorStep.FACING, facing));
				worldIn.setBlockState(posRight.up(), blockSide.getDefaultState()
						.withProperty(BlockEscalatorSide.FACING, facing).withProperty(BlockEscalatorSide.SIDE, true));
			}
			pos = pos.offset(facing.rotateYCCW()).up();
			posRight = posRight.offset(facing.rotateYCCW()).up();
		}
		return true;
	}

	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (int var4 = 0; var4 < 5; ++var4)
			subItems.add(new ItemStack(itemIn, 1, var4));
	}

	public static String getName1() {
		return name1;
	}

	public static String getName2() {
		return name2;
	}

	public static String getName3() {
		return name3;
	}

	public static String getName4() {
		return name4;
	}

	public static String getName5() {
		return name5;
	}
}
