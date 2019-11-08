package mtr.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockRailScaffold extends Block {

	public static final PropertyInteger PASS = PropertyInteger.create("pass", 0, 8);

	public BlockRailScaffold() {
		super(Material.WOOD);
		setHardness(0.1F);
		setCreativeTab(CreativeTabs.DECORATIONS);
		setDefaultState(blockState.getBaseState().withProperty(PASS, 0));
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 0;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		worldIn.setBlockToAir(pos);
		return true;
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		removeBlock1(worldIn, pos);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(PASS, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(PASS);
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return new AxisAlignedBB(0, 0, 0, 1, 0.25, 1);
	}

	private void removeBlock1(World world, BlockPos pos) {
		removeBlock2(world, pos.north());
		removeBlock2(world, pos.east());
		removeBlock2(world, pos.south());
		removeBlock2(world, pos.west());
	}

	private void removeBlock2(World world, BlockPos pos) {
		final Block block = world.getBlockState(pos).getBlock();
		if (block instanceof BlockRailScaffold) {
			world.setBlockToAir(pos);
			((BlockRailScaffold) block).removeBlock1(world, pos);
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { PASS });
	}
}
