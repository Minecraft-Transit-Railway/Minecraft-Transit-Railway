package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;
import org.mtr.registry.BlockEntityTypes;

public class BlockStationNameTallStanding extends BlockStationNameTallBase {

	public static final float WIDTH = 0.6875F;
	public static final float HEIGHT = 1;

	public BlockStationNameTallStanding(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		switch (IBlock.getStatePropertySafe(state, THIRD)) {
			case LOWER:
				final VoxelShape shape1 = IBlock.getVoxelShapeByDirection(1, 0, 0, 2, 16, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
				final VoxelShape shape2 = IBlock.getVoxelShapeByDirection(14, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
				return Shapes.or(shape1, shape2);
			case MIDDLE:
				return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 16, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
			case UPPER:
				return IBlock.getVoxelShapeByDirection(1, 0, 0, 15, 6, 1, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
			default:
				return Shapes.empty();
		}
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection()).setValue(METAL, true).setValue(THIRD, EnumThird.LOWER) : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameTallStandingBlockEntity(blockPos, blockState);
	}

	public static class StationNameTallStandingBlockEntity extends BlockEntityTallBase {

		public StationNameTallStandingBlockEntity(BlockPos blockPos, BlockState blockState) {
			super(BlockEntityTypes.STATION_NAME_TALL_STANDING.get(), blockPos, blockState, 0.07F, false);
		}
	}
}
