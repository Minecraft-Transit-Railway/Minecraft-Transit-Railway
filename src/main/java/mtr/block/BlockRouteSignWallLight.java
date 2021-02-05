package mtr.block;

import mtr.MTR;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public class BlockRouteSignWallLight extends BlockRouteSignBase implements BlockEntityProvider, IPropagateBlock, IBlock {

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(1.5, isLower ? 10 : 0, 0, 14.5, 16, 1, facing);
		if (isLower) {
			return main;
		} else {
			final VoxelShape light = IBlock.getVoxelShapeByDirection(1.5, 15, 0, 14.5, 16, 4, facing);
			return VoxelShapes.union(main, light);
		}
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new TileEntityRouteSignWallLight();
	}

	public static class TileEntityRouteSignWallLight extends TileEntityRouteSignBase {

		public TileEntityRouteSignWallLight() {
			super(MTR.ROUTE_SIGN_WALL_LIGHT_TILE_ENTITY);
		}
	}
}
