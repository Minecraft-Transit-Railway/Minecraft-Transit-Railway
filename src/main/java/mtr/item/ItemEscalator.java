package mtr.item;

import mtr.Blocks;
import mtr.MTRUtilities;
import mtr.block.BlockEscalatorSide;
import mtr.block.BlockEscalatorStep;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemEscalator extends Item {

	public ItemEscalator() {
		super();
		setCreativeTab(CreativeTabs.REDSTONE);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		pos = pos.offset(facing);
		final EnumFacing playerFacing = player.getHorizontalFacing();

		if (!MTRUtilities.blocksAreReplacable(worldIn, pos, playerFacing, 2, 2))
			return EnumActionResult.FAIL;

		final IBlockState stepState = Blocks.escalator_step.getDefaultState().withProperty(BlockEscalatorStep.FACING, playerFacing);
		worldIn.setBlockState(pos, stepState.withProperty(BlockEscalatorStep.SIDE, false));
		worldIn.setBlockState(pos.offset(playerFacing.rotateY()), stepState.withProperty(BlockEscalatorStep.SIDE, false));

		final IBlockState sideState = Blocks.escalator_side.getDefaultState().withProperty(BlockEscalatorSide.FACING, playerFacing);
		worldIn.setBlockState(pos.up(), stepState.withProperty(BlockEscalatorSide.SIDE, false));
		worldIn.setBlockState(pos.up().offset(playerFacing.rotateY()), stepState.withProperty(BlockEscalatorSide.SIDE, false));

		return EnumActionResult.SUCCESS;
	}
}
