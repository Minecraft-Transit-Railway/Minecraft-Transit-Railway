package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.tool.HolderBase;
import org.mtr.mod.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockRailwaySignPole extends BlockPoleCheckBase {

	public static final IntegerProperty TYPE = IntegerProperty.of("type", 0, 3);

	public BlockRailwaySignPole(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		final Direction facing = IBlock.getStatePropertySafe(state, FACING);
		switch (IBlock.getStatePropertySafe(state, TYPE)) {
			case 0:
				return IBlock.getVoxelShapeByDirection(14, 0, 7, 15.25, 16, 9, facing);
			case 1:
				return IBlock.getVoxelShapeByDirection(10, 0, 7, 11.25, 16, 9, facing);
			case 2:
				return IBlock.getVoxelShapeByDirection(6, 0, 7, 7.25, 16, 9, facing);
			case 3:
				return IBlock.getVoxelShapeByDirection(2, 0, 7, 3.25, 16, 9, facing);
			default:
				return VoxelShapes.fullCube();
		}
	}

	@Override
	protected BlockState placeWithState(BlockState stateBelow) {
		final int type;
		final Block block = stateBelow.getBlock();
		if (block.data instanceof BlockRailwaySign) {
			type = (((BlockRailwaySign) block.data).length + (((BlockRailwaySign) block.data).isOdd ? 2 : 0)) % 4;
		} else {
			type = IBlock.getStatePropertySafe(stateBelow, TYPE);
		}
		return super.placeWithState(stateBelow).with(new Property<>(TYPE.data), type);
	}

	@Override
	protected boolean isBlock(Block block) {
		return (block.data instanceof BlockRailwaySign && ((BlockRailwaySign) block.data).length > 0) || block.data instanceof BlockRailwaySignPole;
	}

	@Override
	protected Text getTooltipBlockText() {
		return TranslationProvider.BLOCK_MTR_RAILWAY_SIGN.getText();
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
		properties.add(TYPE);
	}
}