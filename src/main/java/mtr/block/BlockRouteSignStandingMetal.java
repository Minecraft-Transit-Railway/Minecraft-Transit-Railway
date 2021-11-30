package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockRouteSignStandingMetal extends BlockRouteSignBase implements IPropagateBlock, IBlock {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(2, isLower ? 10 : 0, 0, 14, 16, 1, facing);
		final VoxelShape leg1 = IBlock.getVoxelShapeByDirection(1.5, 0, 0, 2.5, 16, 1, facing);
		final VoxelShape leg2 = IBlock.getVoxelShapeByDirection(13.5, 0, 0, 14.5, 16, 1, facing);
		return VoxelShapes.union(main, VoxelShapes.union(leg1, leg2));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityRouteSignStandingMetal(null, null);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new TileEntityRouteSignStandingMetal(pos, state);
	}

	public static class TileEntityRouteSignStandingMetal extends TileEntityRouteSignBase {

		public TileEntityRouteSignStandingMetal(BlockPos pos, BlockState state) {
			super(MTR.ROUTE_SIGN_STANDING_METAL_TILE_ENTITY, pos, state);
		}
	}
}
