package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import MTR.TileEntityAPGGlassEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
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
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		switch (var3) {
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.0F, 0.125F, 0.5F, 1.0F);
			break;
		case SOUTH:
			setBlockBounds(0.875F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			break;
		case EAST:
			setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.125F);
			break;
		case WEST:
			setBlockBounds(0.0F, 0.0F, 0.875F, 1.0F, 0.5F, 1.0F);
			break;
		default:
		}
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockAPGGlassBottom))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public Item getItem(World worldIn, BlockPos pos) {
		return MTR.itemapg;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTR.itemapg;
	}

	@Override
	public int damageDropped(IBlockState state) {
		return 1;
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityAPGGlassEntity();
	}

	public String getName() {
		return name;
	}
}
