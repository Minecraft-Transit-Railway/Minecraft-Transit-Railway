package org.mtr.mod.block;

import org.mtr.mapping.holder.*;

import javax.annotation.Nonnull;

public abstract class BlockSignalSemaphoreBase extends BlockSignalBase {

	public BlockSignalSemaphoreBase(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.union(IBlock.getVoxelShapeByDirection(4, 4, 5, 12, 8, 11, IBlock.getStatePropertySafe(state, FACING)), Block.createCuboidShape(6, 0, 6, 10, 12, 10));
	}

	public static abstract class BlockEntityBase extends BlockSignalBase.BlockEntityBase {

		public float angle1;
		public float angle2;

		public BlockEntityBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
			super(type, pos, state);
		}
	}
}
