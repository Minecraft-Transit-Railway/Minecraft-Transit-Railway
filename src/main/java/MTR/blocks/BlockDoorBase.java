package MTR.blocks;

import java.util.Random;

import MTR.MTRBlocks;
import MTR.MTRSounds;
import MTR.TileEntityDoorEntity;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDoorBase extends BlockPSD implements ITileEntityProvider {

	public static final PropertyInteger X = PropertyInteger.create("x", 0, 32);

	public BlockDoorBase(String name) {
		super(name);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(SIDE, false)
				.withProperty(TOP, false).withProperty(X, 0));
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		TileEntityDoorEntity te = (TileEntityDoorEntity) worldIn.getTileEntity(pos);
		return state.withProperty(X, te.position);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		super.neighborChanged(state, worldIn, pos, blockIn);
		worldIn.scheduleUpdate(pos, this, 1);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing var3 = state.getValue(FACING);
		boolean side = state.getValue(SIDE);
		float height = 1F;
		if (state.getValue(TOP) && this instanceof BlockAPGDoor)
			height = 0.5F;
		float var1 = 0;
		try {
			TileEntityDoorEntity te = (TileEntityDoorEntity) source.getTileEntity(pos);
			var1 = te.position / 32F;
		} catch (Exception e) {
		}
		if (side)
			var1 = -var1;
		switch (var3) {
		case NORTH:
			return new AxisAlignedBB(0.0F, 0.0F, 0.0F + var1, 0.125F, height, 1.0F + var1);
		case SOUTH:
			return new AxisAlignedBB(0.875F, 0.0F, 0.0F - var1, 1.0F, height, 1.0F - var1);
		case EAST:
			return new AxisAlignedBB(0.0F - var1, 0.0F, 0.0F, 1.0F - var1, height, 0.125F);
		case WEST:
			return new AxisAlignedBB(0.0F + var1, 0.0F, 0.875F, 1.0F + var1, height, 1.0F);
		default:
			return NULL_AABB;
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
							worldIn.playSound(null, pos, this instanceof BlockAPGDoor ? MTRSounds.platformApgopen
									: MTRSounds.platformPsdopen, SoundCategory.RECORDS, 1, 1);
						te.position++;
						worldIn.scheduleUpdate(pos, this, 1);
					}
				} else if (te.position > 0) {
					// closing
					if (te.position == 3 && !worldIn.isRemote && state.getValue(SIDE))
						worldIn.playSound(null, pos,
								this instanceof BlockAPGDoor ? MTRSounds.platformApgclose : MTRSounds.platformPsdclose,
								SoundCategory.RECORDS, 1, 1);
					te.position--;
					worldIn.scheduleUpdate(pos, this, 1);
				}
				te2.position = te.position;
				IBlockState stateAbove = worldIn.getBlockState(pos.up());
				worldIn.notifyBlockUpdate(pos, state, state, 0);
				worldIn.notifyBlockUpdate(pos.up(), stateAbove, stateAbove, 0);
				if (te.position == 0) {
					EnumFacing facing = state.getValue(FACING);
					boolean side = state.getValue(SIDE);
					Block block;
					if (this instanceof BlockPSDDoor)
						block = MTRBlocks.blockpsddoorclosed;
					else
						block = MTRBlocks.blockapgdoorclosed;
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
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, SIDE, TOP, X });
	}
}
