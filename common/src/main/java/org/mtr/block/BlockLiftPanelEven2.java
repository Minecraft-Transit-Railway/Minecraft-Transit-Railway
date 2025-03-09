package org.mtr.block;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import org.mtr.registry.BlockEntityTypes;

import javax.annotation.Nonnull;

public class BlockLiftPanelEven2 extends BlockLiftPanelBase {

	public BlockLiftPanelEven2(AbstractBlock.Settings settings) {
		super(settings, false, true);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
		builder.add(SIDE);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftPanelEven2BlockEntity(blockPos, blockState);
	}

	public static class LiftPanelEven2BlockEntity extends BlockEntityBase {

		public LiftPanelEven2BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_EVEN_2.createAndGet(), pos, state, false);
		}
	}
}
