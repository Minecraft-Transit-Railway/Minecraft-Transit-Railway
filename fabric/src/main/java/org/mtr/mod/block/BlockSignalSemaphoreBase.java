package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockSignalSemaphoreBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	public BlockSignalSemaphoreBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(4, 4, 5, 12, 8, 11, IBlock.getStatePropertySafe(state, FACING)), Block.createCuboidShape(6, 0, 6, 10, 12, 10));
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}

	public static abstract class BlockEntityBase extends BlockEntityExtension {

		public float angle1;
		public float angle2;

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}
	}
}
