package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRouteSignWallMetal extends BlockRouteSignBase implements IBlock {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final boolean isBottom = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		return IBlock.getVoxelShapeByDirection(2, isBottom ? 10 : 0, 0, 14, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityRouteSignWallMetal(pos, state);
	}

	public static class TileEntityRouteSignWallMetal extends TileEntityRouteSignBase {

		public TileEntityRouteSignWallMetal(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_WALL_METAL_TILE_ENTITY.get(), pos, state);
		}
	}
}
