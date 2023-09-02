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

public class BlockRouteSignWallLight extends BlockRouteSignBase implements IBlock {

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext collisionContext) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(1.5, isLower ? 10 : 0, 0, 14.5, 16, 1, facing);
		if (isLower) {
			return main;
		} else {
			final VoxelShape light = IBlock.getVoxelShapeByDirection(1.5, 15, 0, 14.5, 16, 4, facing);
			return Shapes.or(main, light);
		}
	}

	@Override
	public BlockEntityMapper createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityRouteSignWallLight(pos, state);
	}

	public static class TileEntityRouteSignWallLight extends TileEntityRouteSignBase {

		public TileEntityRouteSignWallLight(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY.get(), pos, state);
		}
	}
}
