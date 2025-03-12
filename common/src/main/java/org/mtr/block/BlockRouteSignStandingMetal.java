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

public class BlockRouteSignStandingMetal extends BlockRouteSignBase implements IBlock {

	public BlockRouteSignStandingMetal(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final boolean isLower = IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.LOWER;
		final Direction facing = IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING);
		final VoxelShape main = IBlock.getVoxelShapeByDirection(2, isLower ? 10 : 0, 0, 14, 16, 1, facing);
		final VoxelShape leg1 = IBlock.getVoxelShapeByDirection(1.5, 0, 0, 2.5, 16, 1, facing);
		final VoxelShape leg2 = IBlock.getVoxelShapeByDirection(13.5, 0, 0, 14.5, 16, 1, facing);
		return VoxelShapes.union(main, VoxelShapes.union(leg1, leg2));
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new RouteSignStandingMetalBlockEntity(blockPos, blockState);
	}

	public static class RouteSignStandingMetalBlockEntity extends BlockEntityBase {

		public RouteSignStandingMetalBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.ROUTE_SIGN_STANDING_METAL.createAndGet(), pos, state);
		}
	}
}
