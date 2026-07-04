package org.mtr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.mtr.registry.BlockEntityTypes;

public class BlockLiftPanelOdd1 extends BlockLiftPanelBase {

	public BlockLiftPanelOdd1(BlockBehaviour.Properties settings) {
		super(settings, true, false);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BlockStateProperties.HORIZONTAL_FACING);
		builder.add(CENTER);
		builder.add(SIDE);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new LiftPanelOdd1BlockEntity(blockPos, blockState);
	}

	public static class LiftPanelOdd1BlockEntity extends BlockEntityBase {

		public LiftPanelOdd1BlockEntity(BlockPos pos, BlockState state) {
			super(BlockEntityTypes.LIFT_PANEL_ODD_1.get(), pos, state, true);
		}
	}
}
