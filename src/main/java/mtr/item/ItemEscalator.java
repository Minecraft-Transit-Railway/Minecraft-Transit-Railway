package mtr.item;

import mtr.Blocks;
import mtr.MTRUtilities;
import mtr.block.BlockEscalatorBase;
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
		BlockPos pos1 = pos.offset(facing);
		EnumFacing playerFacing = player.getHorizontalFacing();
		BlockPos pos2 = pos1.offset(playerFacing.rotateY());

		if (!MTRUtilities.blocksAreReplacable(worldIn, pos1, playerFacing, 2, 2))
			return EnumActionResult.FAIL;

		final IBlockState frontState = worldIn.getBlockState(pos1.offset(playerFacing));
		if (frontState.getBlock() instanceof BlockEscalatorBase) {
			if (frontState.getValue(BlockEscalatorBase.FACING) == playerFacing.getOpposite()) {
				playerFacing = playerFacing.getOpposite();
				final BlockPos pos3 = pos1;
				pos1 = pos2;
				pos2 = pos3;
			}
		}

		final IBlockState stepState = Blocks.escalator_step.getDefaultState().withProperty(BlockEscalatorStep.FACING, playerFacing);
		worldIn.setBlockState(pos1, stepState.withProperty(BlockEscalatorStep.SIDE, false));
		worldIn.setBlockState(pos2, stepState.withProperty(BlockEscalatorStep.SIDE, true));

		final IBlockState sideState = Blocks.escalator_side.getDefaultState().withProperty(BlockEscalatorSide.FACING, playerFacing);
		worldIn.setBlockState(pos1.up(), sideState.withProperty(BlockEscalatorSide.SIDE, false));
		worldIn.setBlockState(pos2.up(), sideState.withProperty(BlockEscalatorSide.SIDE, true));

		return EnumActionResult.SUCCESS;
	}
}
