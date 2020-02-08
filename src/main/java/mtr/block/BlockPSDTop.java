package mtr.block;

import java.util.Random;

import mtr.Items;
import mtr.block.BlockPSDAPGBase.PSDAPGSide;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockPSDTop extends BlockHorizontal {

	public static final PropertyEnum<PSDAPGSide> SIDE = PropertyEnum.create("side", PSDAPGSide.class);

	public BlockPSDTop() {
		super(Material.ROCK);
		setHardness(2);
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.psd;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(getItemDropped(state, null, 0));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGBase))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getBlockState(pos.down()).getBlock() instanceof BlockPSDAPGBase)
			worldIn.setBlockToAir(pos.down());
	}

	@Override
	public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
		onBlockDestroyedByPlayer(worldIn, pos, null);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		final EnumFacing facing = getFacing(worldIn, pos);
		final PSDAPGSide side = getSide(worldIn, pos);
		return state.withProperty(FACING, facing).withProperty(SIDE, side);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		switch (getFacing(source, pos)) {
		case NORTH:
			return new AxisAlignedBB(0, 0, 0, 1, 1, 0.375);
		case EAST:
			return new AxisAlignedBB(0.625, 0, 0, 1, 1, 1);
		case SOUTH:
			return new AxisAlignedBB(0, 0, 0.625, 1, 1, 1);
		case WEST:
			return new AxisAlignedBB(0, 0, 0, 0.375, 1, 1);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
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
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE });
	}

	private EnumFacing getFacing(IBlockAccess worldIn, BlockPos pos) {
		final IBlockState stateBelow = worldIn.getBlockState(pos.down());
		if (stateBelow.getBlock() instanceof BlockPSDAPGBase)
			return stateBelow.getValue(FACING);
		else
			return EnumFacing.NORTH;
	}

	private PSDAPGSide getSide(IBlockAccess worldIn, BlockPos pos) {
		final IBlockState stateBelow = worldIn.getBlockState(pos.down());
		if (stateBelow.getBlock() instanceof BlockPSDAPGBase)
			return stateBelow.getValue(SIDE);
		else
			return PSDAPGSide.SINGLE;
	}
}
