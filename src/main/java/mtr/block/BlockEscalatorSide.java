package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockEscalatorSide extends BlockEscalatorBase {

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof BlockEscalatorStep))
			worldIn.setBlockToAir(pos);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		final EnumEscalatorOrientation orientation = getOrientation(source, pos, state);
		final boolean isBottom = orientation == EnumEscalatorOrientation.LANDING_BOTTOM;
		final boolean isTop = orientation == EnumEscalatorOrientation.LANDING_TOP;
		final boolean side = state.getValue(SIDE);

		switch (state.getValue(FACING)) {
			case NORTH:
				return new AxisAlignedBB(side ? 0.75 : 0, 0, isTop ? 0.5 : 0, side ? 1 : 0.25, 1, isBottom ? 0.5 : 1);
			case EAST:
				return new AxisAlignedBB(isBottom ? 0.5 : 0, 0, side ? 0.75 : 0, isTop ? 0.5 : 1, 1, side ? 1 : 0.25);
			case SOUTH:
				return new AxisAlignedBB(side ? 0 : 0.75, 0, isBottom ? 0.5 : 0, side ? 0.25 : 1, 1, isTop ? 0.5 : 1);
			case WEST:
				return new AxisAlignedBB(isTop ? 0.5 : 0, 0, side ? 0 : 0.75, isBottom ? 0.5 : 1, 1, side ? 0.25 : 1);
			default:
				return NULL_AABB;
		}
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, ORIENTATION, SIDE);
	}
}
