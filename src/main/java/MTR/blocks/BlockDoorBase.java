package MTR.blocks;

import java.util.Random;

import MTR.MTR;
import MTR.TileEntityDoorEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoorBase extends BlockPSD implements ITileEntityProvider {

	public static final PropertyInteger X = PropertyInteger.create("x", 0, 32);

	public BlockDoorBase() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false)
				.withProperty(TOP, false).withProperty(X, 0));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntityDoorEntity te = (TileEntityDoorEntity) worldIn.getTileEntity(pos);
		return state.withProperty(X, te.position);
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		worldIn.scheduleUpdate(pos, this, 1);
	}

	@Override
	public void setBlockBoundsBasedOnState(IBlockAccess access, BlockPos pos) {
		EnumFacing var3 = access.getBlockState(pos).getValue(FACING);
		boolean side = access.getBlockState(pos).getValue(SIDE);
		float height = 1F;
		if (access.getBlockState(pos).getValue(TOP) && this instanceof BlockAPGDoor)
			height = 0.5F;
		float var1 = 0;
		try {
			TileEntityDoorEntity te = (TileEntityDoorEntity) access.getTileEntity(pos);
			var1 = te.position / 32F;
		} catch (Exception e) {
		}
		if (side)
			var1 = -var1;
		switch (var3) {
		case NORTH:
			setBlockBounds(0.0F, 0.0F, 0.0F + var1, 0.125F, height, 1.0F + var1);
			break;
		case SOUTH:
			setBlockBounds(0.875F, 0.0F, 0.0F - var1, 1.0F, height, 1.0F - var1);
			break;
		case EAST:
			setBlockBounds(0.0F - var1, 0.0F, 0.0F, 1.0F - var1, height, 0.125F);
			break;
		case WEST:
			setBlockBounds(0.0F + var1, 0.0F, 0.875F, 1.0F + var1, height, 1.0F);
			break;
		default:
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!(Boolean) state.getValue(TOP))
			try {
				TileEntityDoorEntity te = (TileEntityDoorEntity) worldIn.getTileEntity(pos);
				TileEntityDoorEntity te2 = (TileEntityDoorEntity) worldIn.getTileEntity(pos.up());
				if (worldIn.isBlockPowered(pos.down(2))) {
					// opening
					if (te.position < 32) {
						if (te.position == 0 && !worldIn.isRemote && state.getValue(SIDE))
							worldIn.playSoundEffect(pos.getX(), pos.getY() + 1, pos.getZ(),
									this instanceof BlockAPGDoor ? "mtr:Platform.APGOpen" : "mtr:Platform.PSDOpen", 1F,
									1F);
						te.position++;
						worldIn.scheduleUpdate(pos, this, 1);
					}
				} else if (te.position > 0) {
					// closing
					if (te.position == 3 && !worldIn.isRemote && state.getValue(SIDE))
						worldIn.playSoundEffect(pos.getX(), pos.getY() + 1, pos.getZ(),
								this instanceof BlockAPGDoor ? "mtr:Platform.APGClose" : "mtr:Platform.PSDClose", 1F,
								1F);
					te.position--;
					worldIn.scheduleUpdate(pos, this, 1);
				}
				te2.position = te.position;
				worldIn.markBlockForUpdate(pos);
				worldIn.markBlockForUpdate(pos.up());
				if (te.position == 0) {
					EnumFacing facing = state.getValue(FACING);
					boolean side = state.getValue(SIDE);
					Block block;
					if (this instanceof BlockPSDDoor)
						block = MTR.blockpsddoorclosed;
					else
						block = MTR.blockapgdoorclosed;
					worldIn.setBlockState(pos, block.getDefaultState().withProperty(BlockPSD.FACING, facing)
							.withProperty(BlockPSD.SIDE, side).withProperty(BlockPSD.TOP, false));
					worldIn.setBlockState(pos.up(), block.getDefaultState().withProperty(BlockPSD.FACING, facing)
							.withProperty(BlockPSD.SIDE, side).withProperty(BlockPSD.TOP, true));
				}
			} catch (Exception e) {
			}
	}

	@Override
	public TileEntity createNewTileEntity(World arg0, int arg1) {
		return new TileEntityDoorEntity();
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public BlockState createBlockState() {
		return new BlockState(this, new IProperty[] { FACING, SIDE, TOP, X });
	}
}
