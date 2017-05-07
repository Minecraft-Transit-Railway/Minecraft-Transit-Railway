package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import MTR.TileEntityPSDTopEntity;
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

public class BlockPSDTop extends BlockPSDTopBase implements ITileEntityProvider {

	private static final String name = "BlockPSDTop";
	// 0, 1 - PSD Glass (northbound)
	// 2, 3 - PSD Glass (southbound)
	// 4, 5 - PSD Glass End (also middle)
	// 6, 7 - PSD Glass Very End
	// 8, 9 - PSD Door (closed left arrow)
	// 10, 11 - PSD Door (opened left arrow)
	// 12, 13 - PSD Door (closed right arrow)
	// 14, 15 - PSD Door (opened right arrow)
	// 16, 17 - PSD Door (closed warning)
	// 18, 19 - PSD Door (opened warning)

	public BlockPSDTop() {
		super();
		GameRegistry.registerBlock(this, name);
		setUnlocalizedName(name);
		setLightLevel(1F);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		IBlockState stateBelow = worldIn.getBlockState(pos.down());
		try {
			TileEntityPSDTopEntity te = (TileEntityPSDTopEntity) worldIn.getTileEntity(pos);
			int color = te.color;
			int number = te.number;
			int bound = te.bound;
			boolean arrow = te.arrow == 1;
			int side = 0;
			boolean sideBelow = stateBelow.getValue(BlockPSD.SIDE);
			EnumFacing facing = state.getValue(FACING);
			boolean warning = getWarning(facing, pos, sideBelow);
			Block blockBelow = stateBelow.getBlock();
			if (blockBelow instanceof BlockPSDGlass || blockBelow instanceof BlockPSDGlass2015)
				side = bound % 2 == 1 ^ arrow ? 2 : 0;
			if (blockBelow instanceof BlockPSDGlassEnd || blockBelow instanceof BlockPSDGlassMiddle)
				side = 4;
			if (blockBelow instanceof BlockPSDGlassVeryEnd)
				side = 6;
			if (blockBelow instanceof BlockPSDDoor || blockBelow instanceof BlockPSDDoorClosed) {
				side = 8;
				if (arrow)
					side += 4;
				if (warning)
					side = 16;
				side += blockBelow instanceof BlockPSDDoor ? 2 : 0;
			}
			side += sideBelow ? 1 : 0;
			return state.withProperty(COLOR, color).withProperty(NUMBER, number).withProperty(SIDE, side);
		} catch (Exception e) {
			return getDefaultState();
		}
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		if (access.getBlockState(pos.down()).getBlock() instanceof BlockPSDGlassVeryEnd)
			setBlockBounds(0F, 0F, 0F, 1F, 1F, 1F);
		else {
			EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
			switch (var3) {
			case NORTH:
				setBlockBounds(0.0F, 0.0F, 0.0F, 0.375F, 1.0F, 1.0F);
				break;
			case EAST:
				setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.375F);
				break;
			case SOUTH:
				setBlockBounds(0.625F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
				break;
			case WEST:
				setBlockBounds(0.0F, 0.0F, 0.625F, 1.0F, 1.0F, 1.0F);
				break;
			default:
			}
		}
	}

	@Override
	public Item getItem(World arg0, BlockPos arg1) {
		return MTR.itempsd;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return MTR.itempsd;
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityPSDTopEntity();
	}

	public String getName() {
		return name;
	}
}
