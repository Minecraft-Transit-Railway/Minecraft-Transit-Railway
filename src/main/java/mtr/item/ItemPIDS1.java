package mtr.item;

import mtr.Blocks;
import mtr.block.BlockPIDS1;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemPIDS1 extends Item {

	public ItemPIDS1() {
		super();
		setCreativeTab(CreativeTabs.DECORATIONS);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		BlockPos pos1 = pos.offset(facing);
		EnumFacing playerFacing = player.getHorizontalFacing();
		BlockPos pos2 = pos1.offset(playerFacing.getOpposite());

		if (!worldIn.getBlockState(pos1).getMaterial().isReplaceable() || !worldIn.getBlockState(pos2).getMaterial().isReplaceable()) {
			return EnumActionResult.FAIL;
		} else {
			worldIn.setBlockState(pos1, Blocks.pids_1.getDefaultState().withProperty(BlockPIDS1.FACING, playerFacing.getOpposite()));
			worldIn.setBlockState(pos2, Blocks.pids_1.getDefaultState().withProperty(BlockPIDS1.FACING, playerFacing));
			return EnumActionResult.SUCCESS;
		}
	}
}
