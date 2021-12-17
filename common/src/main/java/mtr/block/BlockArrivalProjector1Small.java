package mtr.block;

import mapper.BlockEntityMapper;
import mtr.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockArrivalProjector1Small extends BlockArrivalProjectorBase {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(6, 15, 0, 10, 16, 1, facing);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Small(pos, state);
	}

	public static class TileEntityArrivalProjector1Small extends BlockEntityMapper {

		public TileEntityArrivalProjector1Small(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ARRIVAL_PROJECTOR_1_SMALL_TILE_ENTITY, pos, state);
		}
	}
}
