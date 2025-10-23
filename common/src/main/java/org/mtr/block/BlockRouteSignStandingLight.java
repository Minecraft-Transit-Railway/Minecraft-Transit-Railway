package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockRouteSignStandingLight extends BlockRouteSignBase implements IBlock {

	public BlockRouteSignStandingLight(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(2, isLower ? 10 : 0, 0, 14, 16, 1, facing);
		final VoxelShape leg1 = IBlock.getVoxelShapeByDirection(1.5, 0, 0, 2, 16, 1, facing);
		final VoxelShape leg2 = IBlock.getVoxelShapeByDirection(14, 0, 0, 14.5, 16, 1, facing);
		if (isLower) {
			return VoxelShapes.union(main, VoxelShapes.union(leg1, leg2));
		} else {
			final VoxelShape light = IBlock.getVoxelShapeByDirection(1.5, 15, 0, 14.5, 16, 4, facing);
			return VoxelShapes.union(VoxelShapes.union(main, light), VoxelShapes.union(leg1, leg2));
		}
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new RouteSignStandingLightBlockEntity(blockPos, blockState);
	}

	public static class RouteSignStandingLightBlockEntity extends BlockEntityBase {

		public RouteSignStandingLightBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_STANDING_LIGHT.get(), pos, state);
		}
	}
}
