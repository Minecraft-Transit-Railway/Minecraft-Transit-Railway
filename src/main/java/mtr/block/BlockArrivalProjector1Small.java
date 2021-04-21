package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockArrivalProjector1Small extends BlockArrivalProjectorBase {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(6, 15, 0, 10, 16, 1, facing);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityArrivalProjector1Small();
	}

	public static class TileEntityArrivalProjector1Small extends BlockEntity {

		public TileEntityArrivalProjector1Small() {
			super(MTR.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY);
		}
	}
}
