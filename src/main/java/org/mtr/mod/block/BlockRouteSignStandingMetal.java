package mtr.block;

import mtr.BlockEntityTypes;
import mtr.mappings.BlockEntityMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockRouteSignStandingMetal extends BlockRouteSignBase implements IBlock {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(2, isLower ? 10 : 0, 0, 14, 16, 1, facing);
		final VoxelShape leg1 = IBlock.getVoxelShapeByDirection(1.5, 0, 0, 2.5, 16, 1, facing);
		final VoxelShape leg2 = IBlock.getVoxelShapeByDirection(13.5, 0, 0, 14.5, 16, 1, facing);
		return Shapes.or(main, Shapes.or(leg1, leg2));
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityRouteSignStandingMetal(pos, state);
	}

	public static class TileEntityRouteSignStandingMetal extends TileEntityRouteSignBase {

		public TileEntityRouteSignStandingMetal(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY.get(), pos, state);
		}
	}
}
