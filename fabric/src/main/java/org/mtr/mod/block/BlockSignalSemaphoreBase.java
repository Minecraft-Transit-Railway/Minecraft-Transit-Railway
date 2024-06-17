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
		final VoxelShape poleShape = Block.createCuboidShape(6, 0, 6, 10, 12, 10);
		if (IBlock.getStatePropertySafe(state, IS_22_5).booleanValue || IBlock.getStatePropertySafe(state, IS_45).booleanValue) {
			return VoxelShapes.union(Block.createCuboidShape(3, 4, 3, 13, 8, 13), poleShape);
		} else {
			return VoxelShapes.union(IBlock.getVoxelShapeByDirection(4, 4, 5, 12, 8, 11, IBlock.getStatePropertySafe(state, FACING)), poleShape);
		}
	}

	public static abstract class BlockEntityBase extends BlockSignalBase.BlockEntityBase {

		public float angle1;
		public float angle2;

		public BlockEntityBase(BlockEntityType<?> type, boolean isDoubleSided, BlockPos pos, BlockState state) {
			super(type, isDoubleSided, pos, state);
		}
	}
}
