package MTR.blocks;

import java.util.Random;

import MTR.MTRItems;
import MTR.TileEntityAPGGlassEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockAPGGlassTop extends BlockPSDTopBase implements ITileEntityProvider {

	private static final String name = "BlockAPGGlassTop";
	// 0, 1 - northbound
	// 2, 3 - southbound
	// 4, 5 - left arrow
	// 6, 7 - right arrow
	// 8, 9 - middle
	// 10, 11 -
	// 12, 13 -
	// 14, 15 -
	// 16, 17 -
	// 18, 19 -

	public BlockAPGGlassTop() {
		super(name);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState state2 = worldIn.getBlockState(pos.down());
		try {
			TileEntityAPGGlassEntity te = (TileEntityAPGGlassEntity) worldIn.getTileEntity(pos);
			int color = te.color;
			int number = te.number;
			int bound = te.bound;
			boolean arrow = te.arrow == 1;
			int side = 0;
			boolean sideBelow = state2.getValue(BlockAPGGlassBottom.SIDE);
			EnumFacing facing = state.getValue(FACING);
			if (getWarning(facing, pos, sideBelow))
				side = bound % 2 == 1 ^ arrow ? 2 : 0;
			else
				side = arrow ? 6 : 4;
			side += sideBelow ? 1 : 0;
			return state.withProperty(COLOR, color).withProperty(NUMBER, number).withProperty(SIDE, side);
		} catch (Exception e) {
			return getDefaultState();
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		switch (var3) {
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 0.125F, 0.5F, 1.0F);
		case SOUTH:
			return new AxisAlignedBB(0.875F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
		case EAST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.125F);
		case WEST:
			return new AxisAlignedBB(0.0F, 0.0F, 0.875F, 1.0F, 0.5F, 1.0F);
		default:
			return NULL_AABB;
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockAPGGlassBottom))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityAPGGlassEntity();
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(MTRItems.itemapg, 1, 1);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTRItems.itemapg;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}
}
