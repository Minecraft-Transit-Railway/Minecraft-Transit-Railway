package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockArrivalProjector1Large extends BlockArrivalProjectorBase {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(4, 15, 0, 12, 16, 1, facing);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Large(pos, state);
	}

	public static class TileEntityArrivalProjector1Large extends BlockEntityMapper {

		public TileEntityArrivalProjector1Large(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ARRIVAL_PROJECTOR_1_LARGE_TILE_ENTITY, pos, state);
		}
	}
}
