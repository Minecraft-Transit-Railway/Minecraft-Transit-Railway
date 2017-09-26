package MTR.blocks;

import java.util.Random;

import MTR.MTRItems;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStationNameBase extends BlockWithDirection {

	public static final PropertyBool POLE = PropertyBool.create("pole");
	public static final PropertyBool SIDE = PropertyBool.create("side");

	public BlockStationNameBase(String name) {
		super(name);
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		if (state.getValue(POLE))
			return new AxisAlignedBB(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
		else {
			EnumFacing var3 = state.getValue(FACING);
			if (state.getValue(SIDE))
				switch (var3) {
				case NORTH:
					return new AxisAlignedBB(0.0F, 0.0F, 0.1484375F, 0.0625F, 1.0F, 0.8515625F);
				case SOUTH:
					return new AxisAlignedBB(0.9375F, 0.0F, 0.1484375F, 1.0F, 1.0F, 0.8515625F);
				case EAST:
					return new AxisAlignedBB(0.1484375F, 0.0F, 0.0F, 0.8515625F, 1.0F, 0.0625F);
				case WEST:
					return new AxisAlignedBB(0.1484375F, 0.0F, 0.9375F, 0.8515625F, 1.0F, 1.0F);
				default:
					return NULL_AABB;
				}
			else if (var3.getAxis() == EnumFacing.Axis.X)
				return new AxisAlignedBB(0.1484375F, 0.0F, 0.3125F, 0.8515625F, 1.0F, 0.6875F);
			else
				return new AxisAlignedBB(0.3125F, 0.0F, 0.1484375F, 0.6875F, 1.0F, 0.8515625F);
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!((Boolean) state.getValue(POLE)) && !((Boolean) state.getValue(SIDE))) {
			boolean pole = false;
			if (worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockStationNameBase)
				pole = worldIn.getBlockState(pos.add(0, -1, 0)).getValue(BlockStationNameBase.POLE);
			if (!pole) {
				dropBlockAsItem(worldIn, pos, state, 0);
				worldIn.setBlockToAir(pos);
			}
		}
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta % 4))
				.withProperty(POLE, (meta & 4) > 0).withProperty(SIDE, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int var3 = state.getValue(FACING).getHorizontalIndex();
		if (state.getValue(POLE))
			var3 = var3 + 4;
		if (state.getValue(SIDE))
			var3 = var3 + 8;
		return var3;
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, POLE, SIDE });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if (state.getValue(POLE))
			return MTRItems.itemstationnamepole;
		else
			return MTRItems.itemstationname;
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		if (worldIn.getBlockState(pos).getValue(POLE))
			return new ItemStack(MTRItems.itemstationnamepole);
		else
			return new ItemStack(MTRItems.itemstationname);
	}
}
