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
import net.minecraft.world.BlockView;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockStationNameTallWall extends BlockStationNameTallBase {

	public BlockStationNameTallWall(AbstractBlock.Settings settings) {
		super(settings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final IntIntImmutablePair bounds = getBounds(state);
		return IBlock.getVoxelShapeByDirection(2, bounds.leftInt(), 0, 14, bounds.rightInt(), 0.5, IBlock.getStatePropertySafe(state, Properties.HORIZONTAL_FACING));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction blockSide = ctx.getSide();
		final Direction facing = blockSide == Direction.UP || blockSide == Direction.DOWN ? ctx.getHorizontalPlayerFacing() : blockSide.getOpposite();
		return IBlock.isReplaceable(ctx, Direction.UP, 3) ? getDefaultState().with(Properties.HORIZONTAL_FACING, facing).with(METAL, true).with(THIRD, EnumThird.LOWER) : null;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new StationNameTallWallBlockEntity(blockPos, blockState);
	}

	public static class StationNameTallWallBlockEntity extends BlockEntityTallBase {

		public StationNameTallWallBlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.STATION_NAME_TALL_WALL.createAndGet(), pos, state, 0.03125F, false);
		}
	}
}
