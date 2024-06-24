package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mapping.mapper.DirectionHelper;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public abstract class BlockSignalLightBase extends BlockExtension implements DirectionHelper, BlockWithEntity {

	private final int shapeX;
	private final int shapeHeight;

	public BlockSignalLightBase(BlockSettings blockSettings, int shapeX, int shapeHeight) {
		super(blockSettings);
		this.shapeX = shapeX;
		this.shapeHeight = shapeHeight;
	}

	@Override
	public BlockState getPlacementState2(ItemPlacementContext ctx) {
		return getDefaultState2().with(new Property<>(FACING.data), ctx.getPlayerFacing().data);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(shapeX, 0, 5, 16 - shapeX, shapeHeight, 11, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}
}
