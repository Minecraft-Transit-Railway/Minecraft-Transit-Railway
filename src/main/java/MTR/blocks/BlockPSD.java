package MTR.blocks;

import java.util.Random;

import MTR.BlockBase;
import MTR.MTRItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPSD extends BlockBase {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool SIDE = PropertyBool.create("side");
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockPSD(String name) {
		super(Material.ROCK, name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false)
				.withProperty(TOP, false));
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		BlockPos var10 = state.getValue(TOP) ? pos.down() : pos;
		BlockPos var11 = var10.up();
		Block block1 = worldIn.getBlockState(var10).getBlock();
		Block block2 = worldIn.getBlockState(var11).getBlock();
		if (block1 instanceof BlockAPGDoorClosed || block1 instanceof BlockAPGDoor
				|| block1 instanceof BlockAPGGlassMiddle) {
			if (!(block1 instanceof BlockPSD) || !(block2 instanceof BlockPSD)) {
				worldIn.setBlockToAir(var10);
				worldIn.setBlockToAir(var11);
			}
		} else {
			BlockPos var12 = var10.add(0, 2, 0);
			Block block3 = worldIn.getBlockState(var12).getBlock();
			if (!(block1 instanceof BlockPSD) || !(block2 instanceof BlockPSD) || !(block3 instanceof BlockPSDTop)) {
				worldIn.setBlockToAir(var10);
				worldIn.setBlockToAir(var11);
				worldIn.setBlockToAir(var12);
			}
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		switch (var3) {
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
		case SOUTH:
			return new AxisAlignedBB(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
		case WEST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4))
				.withProperty(SIDE, (meta & 4) > 0).withProperty(TOP, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).getHorizontalIndex() + (state.getValue(SIDE) ? 4 : 0)
				+ (state.getValue(TOP) ? 8 : 0);
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE, TOP });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTRItems.itempsd;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itempsd);
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.IGNORE;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return false;
	}

}
