package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

import javax.annotation.Nonnull;

public abstract class BlockSignalLightBase extends BlockSignalBase {

	private final int shapeX;
	private final int shapeHeight;

	public BlockSignalLightBase(AbstractBlock.Settings blockSettings, int shapeX, int shapeHeight) {
		super(blockSettings);
		this.shapeX = shapeX;
		this.shapeHeight = shapeHeight;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final int newShapeX;
		if (IBlock.getStatePropertySafe(state, IS_22_5).booleanValue || IBlock.getStatePropertySafe(state, IS_45).booleanValue) {
			newShapeX = shapeX - 1;
		} else {
			newShapeX = shapeX;
		}
		return Block.createCuboidShape(newShapeX, 0, newShapeX, 16 - newShapeX, shapeHeight, 16 - newShapeX);
	}
}
