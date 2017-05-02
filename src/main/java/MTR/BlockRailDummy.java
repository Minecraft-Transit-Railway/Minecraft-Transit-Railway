package MTR;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockRailDummy extends Block {

	private static final String name = "BlockRailDummy";

	protected BlockRailDummy() {
		super(Material.rock);
		setHardness(0.5F);
		setBlockBounds(0, 0, 0, 1F, 0.0625F, 1F);
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		breakOtherBlocks(worldIn, pos);
	}

	public void breakOtherBlocks(World worldIn, BlockPos pos) {
		worldIn.setBlockToAir(pos);
		BlockPos pos1 = pos.add(1, 0, 0), pos2 = pos.add(0, 0, 1), pos3 = pos.add(-1, 0, 0), pos4 = pos.add(0, 0, -1);
		BlockPos pos5 = pos.add(1, 0, 1), pos6 = pos.add(1, 0, -1), pos7 = pos.add(-1, 0, 1), pos8 = pos.add(-1, 0, -1);
		Block block1 = worldIn.getBlockState(pos1).getBlock();
		Block block2 = worldIn.getBlockState(pos2).getBlock();
		Block block3 = worldIn.getBlockState(pos3).getBlock();
		Block block4 = worldIn.getBlockState(pos4).getBlock();
		Block block5 = worldIn.getBlockState(pos5).getBlock();
		Block block6 = worldIn.getBlockState(pos6).getBlock();
		Block block7 = worldIn.getBlockState(pos7).getBlock();
		Block block8 = worldIn.getBlockState(pos8).getBlock();
		breakOtherBlocks2(worldIn, block1, pos1);
		breakOtherBlocks2(worldIn, block2, pos2);
		breakOtherBlocks2(worldIn, block3, pos3);
		breakOtherBlocks2(worldIn, block4, pos4);
		breakOtherBlocks2(worldIn, block5, pos5);
		breakOtherBlocks2(worldIn, block6, pos6);
		breakOtherBlocks2(worldIn, block7, pos7);
		breakOtherBlocks2(worldIn, block8, pos8);
	}

	private void breakOtherBlocks2(World worldIn, Block block, BlockPos pos) {
		if (block instanceof BlockRailDummy)
			((BlockRailDummy) block).breakOtherBlocks(worldIn, pos);
		if (block instanceof BlockRailCurved)
			((BlockRailCurved) block).breakOtherBlocks(worldIn, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTR.itemrail;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public int getMobilityFlag() {
		return 2;
	}

	public String getName() {
		return name;
	}
}