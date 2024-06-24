package org.mtr.mod.block;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.HolderBase;

import javax.annotation.Nonnull;
import java.util.List;

public class BlockPIDSPole extends BlockPoleCheckBase {

	public BlockPIDSPole(BlockSettings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape2(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(7.5, 0, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, FACING));
	}

	@Override
	protected boolean isBlock(Block block) {
		return block.data instanceof BlockPIDSHorizontalBase || block.data instanceof BlockPIDSPole;
	}

	@Override
	protected Text getTooltipBlockText() {
		return new Text(TextHelper.translatable("block.mtr.pids_1").data);
	}

	@Override
	public void addBlockProperties(List<HolderBase<?>> properties) {
		properties.add(FACING);
	}
}
