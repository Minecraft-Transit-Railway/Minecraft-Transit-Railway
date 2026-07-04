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
import org.mtr.libraries.it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import org.mtr.registry.BlockEntityTypes;

public class BlockStationNameTallBlockDoubleSided extends BlockStationNameTallBase {

	public BlockStationNameTallBlockDoubleSided(BlockBehaviour.Properties settings) {
		super(settings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final IntIntImmutablePair bounds = getBounds(state);
		return Shapes.or(IBlock.getVoxelShapeByDirection(2, bounds.leftInt(), 5, 14, bounds.rightInt(), 11, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING)), BlockStationColorPole.getStationPoleShape());
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection()).setValue(METAL, true).setValue(THIRD, EnumThird.LOWER) : null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameTallBlockDoubleSidedBlockEntity(blockPos, blockState);
	}

	public static class StationNameTallBlockDoubleSidedBlockEntity extends BlockEntityTallBase {

		public StationNameTallBlockDoubleSidedBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.get(), pos, state, 0.6875F, true);
		}
	}
}
