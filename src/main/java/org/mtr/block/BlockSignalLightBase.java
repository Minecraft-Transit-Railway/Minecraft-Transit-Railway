package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class BlockSignalLightBase extends BlockSignalBase {

	private final int shapeX;
	private final int shapeHeight;

	public BlockSignalLightBase(BlockBehaviour.Properties blockSettings, int shapeX, int shapeHeight) {
		super(blockSettings);
		this.shapeX = shapeX;
		this.shapeHeight = shapeHeight;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		final int newShapeX;
		if (IBlock.getStatePropertySafe(state, IS_22_5).booleanValue || IBlock.getStatePropertySafe(state, IS_45).booleanValue) {
			newShapeX = shapeX - 1;
		} else {
			newShapeX = shapeX;
		}
		return Block.box(newShapeX, 0, newShapeX, 16 - newShapeX, shapeHeight, 16 - newShapeX);
	}
}
