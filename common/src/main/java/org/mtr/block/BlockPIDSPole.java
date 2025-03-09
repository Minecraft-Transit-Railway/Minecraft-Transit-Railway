package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.mtr.generated.lang.TranslationProvider;

import javax.annotation.Nonnull;

public class BlockPIDSPole extends BlockPoleCheckBase {

	public BlockPIDSPole(AbstractBlock.Settings blockSettings) {
		super(blockSettings);
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return IBlock.getVoxelShapeByDirection(7.5, 0, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, Properties.FACING));
	}

	@Override
	protected boolean isBlock(Block block) {
		return block instanceof BlockPIDSHorizontalBase || block instanceof BlockPIDSPole;
	}

	@Override
	protected Text getTooltipBlockText() {
		return TranslationProvider.BLOCK_MTR_PIDS_1.getText();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
	}
}
