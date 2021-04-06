package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockArrivalProjector1Large extends BlockArrivalProjectorBase {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(4, 15, 0, 12, 16, 1, facing);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityArrivalProjector1Large();
	}

	public static class TileEntityArrivalProjector1Large extends BlockEntity {

		public TileEntityArrivalProjector1Large() {
			super(MTR.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY);
		}
	}
}
