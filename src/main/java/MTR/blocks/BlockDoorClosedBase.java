package MTR.blocks;

import MTR.MTRBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDoorClosedBase extends BlockPSD {

	public BlockDoorClosedBase(String name) {
		super(name);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn) {
		super.neighborChanged(state, worldIn, pos, blockIn);
		Block block;
		if (this instanceof BlockPSDDoorClosed)
			block = MTRBlocks.blockpsddoor;
		else
			block = MTRBlocks.blockapgdoor;
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
