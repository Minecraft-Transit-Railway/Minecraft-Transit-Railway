package mtr.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockRailMarker extends Block {

	public static final PropertyBool FACING = PropertyBool.create("facing");
	public static final PropertyBool KEEP = PropertyBool.create("keep");

	public BlockRailMarker() {
		super(Material.IRON);
		setHardness(1);
		setTickRandomly(true);
		setDefaultState(blockState.getBaseState().withProperty(FACING, false).withProperty(KEEP, false));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random random) {
		if (!state.getValue(KEEP))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getDefaultState().withProperty(FACING, getStateFromPlayerRotation(placer)).withProperty(KEEP, true);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, meta > 0).withProperty(KEEP, false);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING) ? 1 : 0;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
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
		return new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, KEEP });
	}

	public static boolean getStateFromPlayerRotation(EntityLivingBase placer) {
		return placer.getHorizontalFacing().getHorizontalIndex() % 2 == 1;
	}
}
