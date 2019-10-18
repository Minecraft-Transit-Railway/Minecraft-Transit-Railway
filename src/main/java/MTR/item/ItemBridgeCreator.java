package mtr.item;

import mtr.block.BlockRailScaffold;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlassPane;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBridgeCreator extends Item {

	public ItemBridgeCreator() {
		super();
		setMaxStackSize(1);
		setCreativeTab(CreativeTabs.TOOLS);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (!worldIn.isRemote) {
			if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockRailScaffold))
				return EnumActionResult.FAIL;
			for (int i = 0; i < 3; i++)
				findNextScaffold(worldIn, pos, i);
		}
		return EnumActionResult.SUCCESS;
	}

	private void findNextScaffold(World worldIn, BlockPos pos, int pass) {
		placeBlocksForScaffold(worldIn, pos.north(), pass);
		placeBlocksForScaffold(worldIn, pos.east(), pass);
		placeBlocksForScaffold(worldIn, pos.south(), pass);
		placeBlocksForScaffold(worldIn, pos.west(), pass);
	}

	private void placeBlocksForScaffold(World worldIn, BlockPos pos, int pass) {
		if (worldIn.getBlockState(pos.up(pass)).getBlock() instanceof BlockRailScaffold) {
			switch (pass) {
			case 0:
				final int[] outerOffsets = { 0, 1, 2, 2, 2, 2, 2, 1, 0, -1, -2, -2, -2, -2, -2, -1 };
				for (int i = 0; i < 16; i++)
					placeSideBlocks(worldIn, pos.north(outerOffsets[i]).east(outerOffsets[(i + 4) % 16]), Blocks.STAINED_GLASS_PANE.getDefaultState().withProperty(BlockStainedGlassPane.COLOR, EnumDyeColor.WHITE), Blocks.STONEBRICK.getDefaultState(), Blocks.AIR.getDefaultState());
				worldIn.setBlockToAir(pos);
				worldIn.setBlockState(pos.up(), mtr.Blocks.rail_scaffold.getDefaultState());
				break;
			case 1:
				final int[] innerOffsets = { 0, 1, 1, 1, 0, -1, -1, -1 };
				for (int i = 0; i < 8; i++)
					placeSideBlocks(worldIn, pos.north(innerOffsets[i]).east(innerOffsets[(i + 2) % 8]), Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
				worldIn.setBlockToAir(pos.up());
				worldIn.setBlockState(pos.up(2), mtr.Blocks.rail_scaffold.getDefaultState());
				break;
			case 2:
				worldIn.setBlockState(pos.down(), Blocks.STONEBRICK.getDefaultState());
				worldIn.setBlockState(pos, Blocks.RAIL.getDefaultState());
				worldIn.setBlockToAir(pos.up());
				worldIn.setBlockToAir(pos.up(2));
				break;
			}

			findNextScaffold(worldIn, pos, pass);
		}
	}

	private void placeSideBlocks(World worldIn, BlockPos pos, IBlockState stateAbove, IBlockState stateMiddle, IBlockState stateBelow) {
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockRailScaffold))
			worldIn.setBlockState(pos.down(), stateBelow);
		if (!(worldIn.getBlockState(pos).getBlock() instanceof BlockRailScaffold))
			worldIn.setBlockState(pos, stateMiddle);
		if (!(worldIn.getBlockState(pos.up()).getBlock() instanceof BlockRailScaffold))
			worldIn.setBlockState(pos.up(), stateAbove);
	}
}
