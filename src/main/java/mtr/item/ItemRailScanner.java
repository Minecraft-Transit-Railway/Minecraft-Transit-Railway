package mtr.item;

import mtr.RailTools;
import mtr.TrainData;
import net.minecraft.block.BlockRailBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemRailScanner extends Item {

	public ItemRailScanner() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		if (!(state.getBlock() instanceof BlockRailBase))
			return EnumActionResult.FAIL;
		if (worldIn.isRemote)
			return EnumActionResult.PASS;

		TrainData data = TrainData.get(worldIn);
		RailTools.traverseConnectedTracks(worldIn, pos, data::addRail);
		data.markDirty();
		return EnumActionResult.PASS;
	}
}
