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

public class BlockLiftPanelOdd2 extends BlockLiftPanelBase {

	public BlockLiftPanelOdd2(AbstractBlock.Settings settings) {
		super(settings, true, true);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.HORIZONTAL_FACING);
		builder.add(CENTER);
		builder.add(SIDE);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftPanelOdd2BlockEntity(blockPos, blockState);
	}

	public static class LiftPanelOdd2BlockEntity extends BlockEntityBase {

		public LiftPanelOdd2BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_ODD_2.get(), pos, state, true);
		}
	}
}
