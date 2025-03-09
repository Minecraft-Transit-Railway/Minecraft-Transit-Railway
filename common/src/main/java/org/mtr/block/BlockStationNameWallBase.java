package org.mtr.block;

import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import javax.annotation.Nonnull;

public abstract class BlockStationNameWallBase extends BlockStationNameBase implements BlockEntityProvider {

	public BlockStationNameWallBase(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		final Direction side = ctx.getSide();
		if (side != Direction.UP && side != Direction.DOWN) {
			return getDefaultState().with(Properties.FACING, side.getOpposite());
		} else {
			return null;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, Properties.FACING));
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
	}

	public abstract static class BlockEntityWallBase extends BlockEntityBase {

		public BlockEntityWallBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state, 0, 0, false);
		}
	}
}
