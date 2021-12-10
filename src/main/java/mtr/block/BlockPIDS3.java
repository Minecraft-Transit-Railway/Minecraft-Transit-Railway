package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockPIDS3 extends BlockPIDSBase {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		VoxelShape shape1 = IBlock.getVoxelShapeByDirection(6, 0, 0, 10, 10, 16, IBlock.getStatePropertySafe(state, FACING));
		VoxelShape shape2 = IBlock.getVoxelShapeByDirection(7.5, 10, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
		return VoxelShapes.union(shape1, shape2);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityBlockPIDS3(pos, state);
	}

	public static class TileEntityBlockPIDS3 extends TileEntityBlockPIDSBase {

		public static final int MAX_ARRIVALS = 2;

		public TileEntityBlockPIDS3(BlockPos pos, BlockState state) {
			super(MTR.PIDS_3_TILE_ENTITY, pos, state);
		}

		@Override
		protected int getMaxArrivals() {
			return MAX_ARRIVALS;
		}
	}
}