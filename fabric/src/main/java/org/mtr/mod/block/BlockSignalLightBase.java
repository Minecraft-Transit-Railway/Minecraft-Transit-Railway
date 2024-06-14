package org.mtr.mod.block;

import org.mtr.mapping.holder.*;

import javax.annotation.Nonnull;

public abstract class BlockSignalLightBase extends BlockSignalBase {

	private final int shapeX;
	private final int shapeHeight;

	public BlockSignalLightBase(BlockSettings blockSettings, int shapeX, int shapeHeight) {
		super(blockSettings);
		this.shapeX = shapeX;
		this.shapeHeight = shapeHeight;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(shapeX, 0, 5, 16 - shapeX, shapeHeight, 11, IBlock.getStatePropertySafe(state, FACING));
	}
}
