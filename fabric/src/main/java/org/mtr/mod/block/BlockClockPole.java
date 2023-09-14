package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;

import javax.annotation.Nonnull;

public class BlockClockPole extends BlockExtension {

	public BlockClockPole(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(7.5, 0, 7.5, 8.5, 16, 8.5);
	}
}
