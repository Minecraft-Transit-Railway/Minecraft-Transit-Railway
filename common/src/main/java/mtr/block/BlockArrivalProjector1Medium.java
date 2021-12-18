package mtr.block;

import minecraftmappings.BlockEntityMapper;
import mtr.BlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockArrivalProjector1Medium extends BlockArrivalProjectorBase {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		return IBlock.getVoxelShapeByDirection(5, 15, 0, 11, 16, 1, facing);
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityArrivalProjector1Medium(pos, state);
	}

	public static class TileEntityArrivalProjector1Medium extends BlockEntityMapper {

		public TileEntityArrivalProjector1Medium(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ARRIVAL_PROJECTOR_1_MEDIUM_TILE_ENTITY, pos, state);
		}
	}
}
