package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockStationNameWallBase extends BlockStationNameBase implements BlockWithEntity {

	public BlockStationNameWallBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		final Direction side = ctx.getSide();
		if (side != Direction.UP && side != Direction.DOWN) {
			return getDefaultState2().with(new Property<>(FACING.data), side.getOpposite().data);
		} else {
			return null;
		}
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, 16, 1, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public VoxelShape getCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public abstract static class BlockEntityWallBase extends BlockEntityBase {

		public BlockEntityWallBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state, 0, 0, false);
		}
	}
}
