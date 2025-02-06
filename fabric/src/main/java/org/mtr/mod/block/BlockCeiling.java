package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockCeiling extends BlockWaterloggable {

	public static final BooleanProperty FACING = BooleanProperty.of("facing");

	public BlockCeiling(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public BlockState getPlacementState2(ItemPlacementContext itemPlacementContext) {
		return super.getPlacementState2(itemPlacementContext).with(new Property<>(FACING.data), itemPlacementContext.getPlayerFacing().getAxis() == Axis.X);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0, 7, 0, 16, 10, 16);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		super.addBlockProperties(properties);
		properties.add(FACING);
	}
}
