package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mod.Blocks;

import javax.annotation.Nonnull;

public abstract class BlockPSDAPGBase extends BlockDirectionalDoubleBlockBase {

	public BlockPSDAPGBase() {
		super(Blocks.createDefaultBlockSettings(true).nonOpaque());
	}

	@Nonnull
	@Override
	public ItemStack getPickStack2(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(new ItemConvertible(asItem2().data));
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final int height = isAPG() && IBlock.getStatePropertySafe(state, HALF) == DoubleBlockHalf.UPPER ? 9 : 16;
		return IBlock.getVoxelShapeByDirection(0, 0, 0, 16, height, 4, IBlock.getStatePropertySafe(state, FACING));
	}

	@Nonnull
	@Override
	public VoxelShape getCameraCollisionShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return VoxelShapes.empty();
	}

	protected boolean isAPG() {
		return this instanceof BlockAPGDoor || this instanceof BlockAPGGlass || this instanceof BlockAPGGlassEnd;
	}
}
