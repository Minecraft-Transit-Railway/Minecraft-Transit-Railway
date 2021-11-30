package mtr.block;

import mapper.BlockEntityMapper;
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
		return new TileEntityArrivalProjector1Large(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Large(pos, state);
	}

	public static class TileEntityArrivalProjector1Large extends BlockEntityMapper {

		public TileEntityArrivalProjector1Large(BlockPos pos, BlockState state) {
			super(MTR.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY, pos, state);
		}
	}
}
