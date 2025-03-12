package org.mtr.block;

import it.unimi.dsi.fastutil.ints.IntIntImmutablePair;
import net.minecraft.block.AbstractBlock;
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

public class BlockStationNameTallBlockDoubleSided extends BlockStationNameTallBase {

	public BlockStationNameTallBlockDoubleSided(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final IntIntImmutablePair bounds = getBounds(state);
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(2, bounds.leftInt(), 5, 14, bounds.rightInt(), 11, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING)), BlockStationColorPole.getStationPoleShape());
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing()).with(METAL, true).with(THIRD, EnumThird.LOWER) : null;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameTallBlockDoubleSidedBlockEntity(blockPos, blockState);
	}

	public static class StationNameTallBlockDoubleSidedBlockEntity extends BlockEntityTallBase {

		public StationNameTallBlockDoubleSidedBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_TALL_BLOCK_DOUBLE_SIDED.createAndGet(), pos, state, 0.6875F, true);
		}
	}
}
