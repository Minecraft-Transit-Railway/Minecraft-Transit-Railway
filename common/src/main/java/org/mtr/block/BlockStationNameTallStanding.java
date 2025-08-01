package org.mtr.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameTallStanding extends BlockStationNameTallBase {

	public static final float WIDTH = 0.6875F;
	public static final float HEIGHT = 1;

	public BlockStationNameTallStanding(Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (IBlock.getStatePropertySafe(state, THIRD)) {
			case LOWER:
				final VoxelShape shape1 = IBlock.getVoxelShapeByDirection(1, 0, 0, 2, 16, 1, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
				final VoxelShape shape2 = IBlock.getVoxelShapeByDirection(14, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
				return VoxelShapes.union(shape1, shape2);
			case MIDDLE:
				return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
			case UPPER:
				return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 6, 1, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
			default:
				return VoxelShapes.empty();
		}
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing()).with(METAL, true).with(THIRD, EnumThird.LOWER) : null;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameTallStandingBlockEntity(blockPos, blockState);
	}

	public static class StationNameTallStandingBlockEntity extends BlockEntityTallBase {

		public StationNameTallStandingBlockEntity(BlockPos blockPos, BlockState blockState) {
			super(BlockEntityTypes.STATION_NAME_TALL_STANDING.createAndGet(), blockPos, blockState, 0.07F, false);
		}
	}
}
