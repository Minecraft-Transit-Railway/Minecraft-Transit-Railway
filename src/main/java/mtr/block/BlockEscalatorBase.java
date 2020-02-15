package mtr.block;

import java.util.Random;

import mtr.Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockEscalatorBase extends BlockHorizontal {

	public static final PropertyEnum<EnumEscalatorOrientation> ORIENTATION = PropertyEnum.create("orientation", EnumEscalatorOrientation.class);
	public static final PropertyBool SIDE = PropertyBool.create("side");

	protected BlockEscalatorBase() {
		super(Material.ROCK);
		setHardness(2);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (!Block.isEqualTo(this, worldIn.getBlockState(getSidePos(pos, state)).getBlock()))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(getItemDropped(state, null, 0), 1, 0);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Items.escalator;
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return state.withProperty(ORIENTATION, getOrientation(worldIn, pos, state));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(SIDE) ? 4 : 0) + state.getValue(FACING).getHorizontalIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(SIDE, (meta & 4) > 0).withProperty(FACING, EnumFacing.getHorizontal(meta & 3));
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

	protected final EnumEscalatorOrientation getOrientation(IBlockAccess worldIn, BlockPos pos, IBlockState state) {
		final EnumFacing facing = state.getValue(FACING);

		final BlockPos posAhead = pos.offset(facing);
		final BlockPos posBehind = pos.offset(facing, -1);

		final boolean isAhead = Block.isEqualTo(this, worldIn.getBlockState(posAhead).getBlock());
		final boolean isAheadUp = Block.isEqualTo(this, worldIn.getBlockState(posAhead.up()).getBlock());

		final boolean isBehind = Block.isEqualTo(this, worldIn.getBlockState(posBehind).getBlock());
		final boolean isBehindDown = Block.isEqualTo(this, worldIn.getBlockState(posBehind.down()).getBlock());

		if (isAhead && isBehind)
			return EnumEscalatorOrientation.FLAT;
		else if (isAheadUp && isBehindDown)
			return EnumEscalatorOrientation.SLOPE;
		else if (isAheadUp && isBehind)
			return EnumEscalatorOrientation.TRANSITION_BOTTOM;
		else if (isAhead && isBehindDown)
			return EnumEscalatorOrientation.TRANSITION_TOP;
		else if (isBehind)
			return EnumEscalatorOrientation.LANDING_TOP;
		else
			return EnumEscalatorOrientation.LANDING_BOTTOM;
	}

	protected final BlockPos getSidePos(BlockPos pos, IBlockState state) {
		final EnumFacing facing = state.getValue(FACING);
		return pos.offset(state.getValue(SIDE) ? facing.rotateYCCW() : facing.rotateY());
	}

	protected enum EnumEscalatorOrientation implements IStringSerializable {

		LANDING_BOTTOM("landing_bottom"), LANDING_TOP("landing_top"), FLAT("flat"), SLOPE("slope"), TRANSITION_BOTTOM("transition_bottom"), TRANSITION_TOP("transition_top");

		private final String name;

		private EnumEscalatorOrientation(String nameIn) {
			name = nameIn;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
