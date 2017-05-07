package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.World;

public class BlockRailBase2 extends Block {

	public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);

	public BlockRailBase2() {
		super(Material.rock);
		setHardness(0.5F);
		setBlockBounds(0, 0, 0, 1F, 0.0625F, 1F);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(ROTATION, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(ROTATION);
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { ROTATION });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return null;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
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
}