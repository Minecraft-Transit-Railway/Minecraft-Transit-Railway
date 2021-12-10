package mtr.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class BlockPIDSPole extends BlockPoleCheckBase {

	public BlockPIDSPole(Settings settings) {
		super(settings);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
		return mtr.block.IBlock.getVoxelShapeByDirection(7.5, 0, 12.5, 8.5, 16, 13.5, state.get(FACING));
	}

	@Override
	protected boolean isBlock(Block block) {
		return block instanceof BlockPIDSBase || block instanceof BlockPIDSPole;
	}

	@Override
	protected Text getTooltipBlockText() {
		return new TranslatableText("block.mtr.pids_1");
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}
}
