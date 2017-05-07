package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumWorldBlockLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockPSD extends Block {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool SIDE = PropertyBool.create("side");
	public static final PropertyBool TOP = PropertyBool.create("top");

	public BlockPSD() {
		super(Material.rock);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false)
				.withProperty(TOP, false));
		setHardness(5F);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
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
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		switch (var3) {
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 1.0F, 1.0F);
			break;
		case SOUTH:
			setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			break;
		case EAST:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.125F);
			break;
		case WEST:
			setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 1.0F, 1.0F);
			break;
		default:
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4))
				.withProperty(SIDE, (meta & 4) > 0).withProperty(TOP, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(SIDE))
			var3 = var3 + 4;
		if (state.getValue(TOP))
			var3 = var3 + 8;
		return var3;
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIDE, TOP });
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
		setBlockBoundsBasedOnState(worldIn, pos);
		return super.getCollisionBoundingBox(worldIn, pos, state);
	}

	@Override
	public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
		setBlockBoundsBasedOnState(worldIn, pos);
		return super.getSelectedBoundingBox(worldIn, pos);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTR.itempsd;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return MTR.itempsd;
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

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return false;
	}

}
