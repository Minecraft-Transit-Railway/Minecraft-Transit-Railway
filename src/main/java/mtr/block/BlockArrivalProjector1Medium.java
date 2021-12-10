package mtr.block;

import minecraftmappings.BlockEntityMapper;
import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockArrivalProjector1Medium extends BlockArrivalProjectorBase {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(5, 15, 0, 11, 16, 1, facing);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Medium(pos, state);
	}

	public static class TileEntityArrivalProjector1Medium extends BlockEntityMapper {

		public TileEntityArrivalProjector1Medium(BlockPos pos, BlockState state) {
			super(MTR.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY, pos, state);
		}
	}
}
