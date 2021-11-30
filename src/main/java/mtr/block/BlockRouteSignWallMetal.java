package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockRouteSignWallMetal extends BlockRouteSignBase implements IPropagateBlock, IBlock {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isBottom = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		return IBlock.getVoxelShapeByDirection(2, isBottom ? 10 : 0, 0, 14, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityRouteSignWallMetal(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityRouteSignWallMetal(pos, state);
	}

	public static class TileEntityRouteSignWallMetal extends TileEntityRouteSignBase {

		public TileEntityRouteSignWallMetal(BlockPos pos, BlockState state) {
			super(MTR.ROUTE_SIGN_WALL_METAL_TILE_ENTITY, pos, state);
		}
	}
}
