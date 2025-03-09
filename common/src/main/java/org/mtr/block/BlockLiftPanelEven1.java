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

public class BlockLiftPanelEven1 extends BlockLiftPanelBase {

	public BlockLiftPanelEven1(AbstractBlock.Settings settings) {
		super(settings, false, false);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(Properties.FACING);
		builder.add(SIDE);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftPanelEven1BlockEntity(blockPos, blockState);
	}

	public static class LiftPanelEven1BlockEntity extends BlockEntityBase {

		public LiftPanelEven1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_EVEN_1.createAndGet(), pos, state, false);
		}
	}
}
