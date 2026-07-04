package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockSignalSemaphoreBase extends BlockSignalBase {

	public BlockSignalSemaphoreBase(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final VoxelShape poleShape = Block.box(6, 0, 6, 10, 12, 10);
		if (IBlock.getStatePropertySafe(state, IS_22_5).booleanValue || IBlock.getStatePropertySafe(state, IS_45).booleanValue) {
			return Shapes.or(Block.box(3, 4, 3, 13, 8, 13), poleShape);
		} else {
			return Shapes.or(IBlock.getVoxelShapeByDirection(4, 4, 5, 12, 8, 11, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING)), poleShape);
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
