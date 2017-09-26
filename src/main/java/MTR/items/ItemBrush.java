package MTR.items;

import java.util.List;

import MTR.ItemBase;
import MTR.MTRBlocks;
import MTR.blocks.BlockRailDummy;
import MTR.blocks.BlockRailStraight;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBrush extends ItemBase {

	private static final String[] name = { "ItemBrush" };

	public ItemBrush() {
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos,
			EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		Block blockIn = worldIn.getBlockState(pos).getBlock();
		if (blockIn instanceof BlockRailStraight) {
			BlockRailStraight block = (BlockRailStraight) blockIn;
			int rotation = worldIn.getBlockState(pos).getValue(BlockRailStraight.ROTATION);
			block.destroy1(worldIn, pos, 10, rotation);
			block.destroy2(worldIn, pos, 10, rotation);
		}
		if (blockIn instanceof BlockRailDummy)
			worldIn.setBlockState(pos,
					MTRBlocks.blockraildummy.getDefaultState().withProperty(BlockRailDummy.ROTATION, 0));
		return EnumActionResult.PASS;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean par4) {
		list.add(I18n.format("gui.brush", new Object[0]));
	}
}
