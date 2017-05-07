package MTR.blocks;

import MTR.MTR;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class BlockDoorClosedBase extends BlockPSD {

	public BlockDoorClosedBase() {
		super();
	}

	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
		Block block;
		if (this instanceof BlockPSDDoorClosed)
			block = MTR.blockpsddoor;
		else
			block = MTR.blockapgdoor;
		if (!state.getValue(TOP) && worldIn.isBlockPowered(pos.add(0, -2, 0))) {
			EnumFacing facing = state.getValue(FACING);
			boolean side = state.getValue(SIDE);
			worldIn.setBlockState(pos, block.getDefaultState().withProperty(BlockPSD.FACING, facing)
					.withProperty(BlockPSD.SIDE, side).withProperty(BlockPSD.TOP, false));
			worldIn.setBlockState(pos.up(), block.getDefaultState().withProperty(BlockPSD.FACING, facing)
					.withProperty(BlockPSD.SIDE, side).withProperty(BlockPSD.TOP, true));
		}
	}
}
