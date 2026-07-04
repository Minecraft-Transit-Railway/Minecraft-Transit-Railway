package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.mtr.generated.lang.TranslationProvider;

public class BlockPIDSPole extends BlockPoleCheckBase {

	public BlockPIDSPole(BlockBehaviour.Properties blockSettings) {
		super(blockSettings);
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return IBlock.getVoxelShapeByDirection(7.5, 0, 12.5, 8.5, 16, 13.5, IBlock.getStatePropertySafe(state, BlockStateProperties.HORIZONTAL_FACING));
	}

	@Override
	protected boolean isBlock(BlockBehaviour block) {
		return block instanceof BlockPIDSHorizontalBase || block instanceof BlockPIDSPole;
	}

	@Override
	protected Component getTooltipBlockText() {
		return TranslationProvider.BLOCK_MTR_PIDS_1.getText();
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
	}
}
