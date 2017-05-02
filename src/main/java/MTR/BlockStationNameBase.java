package MTR;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockStationNameBase extends BlockWithDirection {

	public static final PropertyBool POLE = PropertyBool.create("pole");
	public static final PropertyBool SIDE = PropertyBool.create("side");

	protected BlockStationNameBase() {
		super();
		setCreativeTab(null);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false));
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		if ((Boolean) access.getBlockState(pos).getValue(POLE))
			setBlockBounds(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
		else {
			EnumFacing var3 = (EnumFacing) access.getBlockState(pos).getValue(FACING);
			if ((Boolean) access.getBlockState(pos).getValue(SIDE))
				switch (var3) {
				case NORTH:
					setBlockBounds(0.0F, 0.0F, 0.1484375F, 0.0625F, 1.0F, 0.8515625F);
					break;
				case SOUTH:
					setBlockBounds(0.9375F, 0.0F, 0.1484375F, 1.0F, 1.0F, 0.8515625F);
					break;
				case EAST:
					setBlockBounds(0.1484375F, 0.0F, 0.0F, 0.8515625F, 1.0F, 0.0625F);
					break;
				case WEST:
					setBlockBounds(0.1484375F, 0.0F, 0.9375F, 0.8515625F, 1.0F, 1.0F);
					break;
				default:
				}
			else if (var3.getAxis() == EnumFacing.Axis.X)
				setBlockBounds(0.1484375F, 0.0F, 0.3125F, 0.8515625F, 1.0F, 0.6875F);
			else
				setBlockBounds(0.3125F, 0.0F, 0.1484375F, 0.6875F, 1.0F, 0.8515625F);
		}
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!((Boolean) state.getValue(POLE)) && !((Boolean) state.getValue(SIDE))) {
			boolean pole = false;
			if (worldIn.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockStationNameBase)
				pole = (Boolean) worldIn.getBlockState(pos.add(0, -1, 0)).getValue(BlockStationNameBase.POLE);
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
		int var3 = ((EnumFacing) state.getValue(FACING)).getHorizontalIndex();
		if ((Boolean) state.getValue(POLE))
			var3 = var3 + 4;
		if ((Boolean) state.getValue(SIDE))
			var3 = var3 + 8;
		return var3;
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, POLE, SIDE });
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		if ((Boolean) state.getValue(POLE))
			return MTR.itemstationnamepole;
		else
			return MTR.itemstationname;
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		if ((Boolean) worldIn.getBlockState(pos).getValue(POLE))
			return MTR.itemstationnamepole;
		else
			return MTR.itemstationname;
	}
}
